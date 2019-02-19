package com.example.zhong.lasagna.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.MyApplication;
import com.example.zhong.lasagna.util.HttpUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HomeFragment extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;

    //获取当前登录用户及其所关注（授权）用户的最新微博
    private String url = "https://api.weibo.com/2/statuses/home_timeline.json";
    private int mCurrentPage=1;
    private HomeRecyclerAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        queryServices();
        initView();
        return view;
    }

    private void initView() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            queryServices();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void queryServices() {
        Log.e("hello", "queryServices url=" + url + MyApplication.getAccessToken());
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
                if (homeJB!=null) {
                    adapter = new HomeRecyclerAdapter(homeJB.getStatuses(),getContext());
                    getActivity().runOnUiThread(() -> {
                        adapter.setEmptyView(R.layout.empty_default, recycler);
                        adapter.setOnLoadMoreListener(() -> startLoadMore(), recycler);
                        adapter.disableLoadMoreIfNotFullPage();
                        adapter.setListener();
                        recycler.setAdapter(adapter);
                    });
                }

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
                Toast.makeText(getContext(),"加载数据失败，请检查网络", Toast.LENGTH_LONG).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
