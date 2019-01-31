package com.example.zhong.lasagna.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.MyApplication;
import com.example.zhong.lasagna.util.HttpUtil;

import java.io.IOException;
import java.util.List;

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

    public static final String USER_ID="USER_ID";
    public static final String WEIBO_ID="WEIBO_ID";

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
                adapter = new HomeRecyclerAdapter(homeJB.getStatuses());
                getActivity().runOnUiThread(() -> {
                    adapter.setEmptyView(R.layout.empty_default, recycler);
                    adapter.setOnLoadMoreListener(() -> startLoadMore(), recycler);
                    adapter.disableLoadMoreIfNotFullPage();
                    adapter.setListener();
                    recycler.setAdapter(adapter);
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

    public class HomeRecyclerAdapter extends BaseMultiItemQuickAdapter<HomeJB.StatusesBean, BaseViewHolder> {

        HomeRecyclerAdapter(List data) {
            super(data);
            addItemType(HomeJB.StatusesBean.ORIGINAL, R.layout.home_recycler_original_adapter_item);
            addItemType(HomeJB.StatusesBean.RETWEET, R.layout.mainfragment_weiboitem_retweet_pictext);
        }

        @Override
        protected void convert(BaseViewHolder holder, HomeJB.StatusesBean item) {
            switch (holder.getItemViewType()) {
                case HomeJB.StatusesBean.ORIGINAL:
                    FillContent.fillTitleBar(holder, item);
                    FillContent.fillButtonBar(holder, item);
                    FillContent.fillWeiBoContent(holder, item, HomeJB.StatusesBean.ORIGINAL);
                    FillContent.fillWeiBoImgList(holder, item, HomeJB.StatusesBean.ORIGINAL);
                    FillContent.addListener(holder,HomeJB.StatusesBean.ORIGINAL);
                    break;

                case HomeJB.StatusesBean.RETWEET:
                    FillContent.fillTitleBar(holder, item);
                    FillContent.fillButtonBar(holder, item);
                    FillContent.fillWeiBoContent(holder, item, HomeJB.StatusesBean.RETWEET);
                    FillContent.fillRetweetContent(holder, item);
                    FillContent.fillWeiBoImgList(holder, item, HomeJB.StatusesBean.RETWEET);
                    break;
            }
        }

        void setListener(){
            this.setOnItemClickListener((adapter, view, position) -> {
                Log.d("hello", "onItemClick: ");
                Object item = adapter.getItem(position);
                if(item instanceof HomeJB.StatusesBean){
                    Intent intent = new Intent(getContext(), OriginDetailActivity.class);
                    intent.putExtra(WEIBO_ID, String.valueOf(((HomeJB.StatusesBean) item).getId()));
                    intent.putExtra(USER_ID, String.valueOf(((HomeJB.StatusesBean) item).getUser().getId()));
                    startActivity(intent);
                }
            });
            this.setOnItemChildClickListener((adapter, view, position) -> {
                Log.d("hello", "onItemChildClick: ");
                Object item = adapter.getItem(position);
                switch (view.getId())
                {
                    case R.id.im_head:
                        if(item instanceof HomeJB.StatusesBean){
                            Intent intent = new Intent(getContext(), ProfileSwipeActivity.class);
                            intent.putExtra(USER_ID, ((HomeJB.StatusesBean) item).getUser().getId());
                            startActivity(intent);
                        }
                        break;
                    case R.id.tv_name:
                        if(item instanceof HomeJB.StatusesBean){
                            Intent intent = new Intent(getContext(), ProfileSwipeActivity.class);
                            intent.putExtra(USER_ID, ((HomeJB.StatusesBean) item).getUser().getId());
                            startActivity(intent);
                        }
                        break;
                    case R.id.bt_more:
                        showListDialog();
                        break;
                    case R.id.ll_forward:
                        Toast.makeText(getContext(),"暂不能转发", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ll_comment:
                        if(item instanceof HomeJB.StatusesBean){
                            Intent intent = new Intent(getContext(), OriginDetailActivity.class);
                            intent.putExtra(WEIBO_ID, ((HomeJB.StatusesBean) item).getId());
                            intent.putExtra(USER_ID, ((HomeJB.StatusesBean) item).getUser().getId());
                            startActivity(intent);
                        }
                        break;
                    case R.id.ll_like:
                        break;
                    case R.id.retweetStatus_layout:
                        break;
                }
            });
        }

        private void showListDialog() {
            final String[] items = { "收藏","用此卡片背景","帮上头条","取消关注" ,"屏蔽","投诉"};
//            AlertDialog listDialog = new AlertDialog.Builder(MyApplication.getGlobalContext())
//                    .setTitle("")
//                    .setItems(items, (dialogInterface, i) -> {
//                    }).create();
//            listDialog.show();
            android.app.AlertDialog listDialog = new android.app.AlertDialog.Builder(getActivity())
                    .setItems(items, (dialogInterface, i) -> {

                    }).create();
            listDialog.show();
        }
    }

}
