package com.example.zhong.lasagna.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.MyApplication;
import com.example.zhong.lasagna.home.HomeFragment;
import com.example.zhong.lasagna.home.HomeJB;
import com.example.zhong.lasagna.home.HomeRecyclerAdapter;
import com.example.zhong.lasagna.util.HttpUtil;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HotWeiBoSwipeActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_more)
    ImageView toolbarMore;
    @BindView(R.id.base_RecyclerView)
    RecyclerView baseRecyclerView;
    @BindView(R.id.base_swipe_refresh_widget)
    SwipeRefreshLayout baseSwipeRefreshWidget;

    //返回最新的公共微博
    private String url = "https://api.weibo.com/2/statuses/public_timeline.json";
    private HomeRecyclerAdapter adapter;
    private int mCurrentPage=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover_popweibo_layout);
        ButterKnife.bind(this);

        initView();
        sendHttp();
    }

    private void initView() {
        baseRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        toolbarBack.setOnClickListener((view)->{
            finish();
        });
        baseSwipeRefreshWidget.setOnRefreshListener(()->{
            sendHttp();
            baseSwipeRefreshWidget.setRefreshing(false);
        });
    }

    private void sendHttp() {
        HttpUtil.sendOkHttp(url + MyApplication.getAccessToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("hello", "sendOkHttpFailure....");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("hello", "sendOkHttpSuccess....");
                HomeJB homeJB = HttpUtil.HandleHttpResponse(response.body().string(), HomeJB.class);
                adapter = new HomeRecyclerAdapter(homeJB.getStatuses(),HotWeiBoSwipeActivity.this);
                runOnUiThread(() -> {
                    adapter.setEmptyView(R.layout.empty_default, baseRecyclerView);
                    adapter.setOnLoadMoreListener(() -> startLoadMore(), baseRecyclerView);
                    adapter.disableLoadMoreIfNotFullPage();
                    adapter.setListener();
                    baseRecyclerView.setAdapter(adapter);
                });
            }
        });
    }

    private void startLoadMore() {
        String parameter="&page="+(++mCurrentPage);
        Log.e("hello", "parameter=" + parameter);
        Log.e("hello", "onLoadMore address=" + url + MyApplication.getAccessToken() + parameter);
        HttpUtil.sendOkHttp(url + MyApplication.getAccessToken() + parameter, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("hello", "sendOkHttpFailure....");
                e.printStackTrace();
                Toast.makeText(HotWeiBoSwipeActivity.this,"加载数据失败，请检查网络", Toast.LENGTH_LONG).show();
                adapter.loadMoreFail();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                HomeJB homeJB = HttpUtil.HandleHttpResponse(response.body().string(), HomeJB.class);
                mCurrentPage++;
                adapter.addData(homeJB.getStatuses());
                adapter.loadMoreComplete();
            }
        });
    }

}
