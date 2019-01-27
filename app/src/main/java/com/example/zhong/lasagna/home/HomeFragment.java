package com.example.zhong.lasagna.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.GlideApp;
import com.example.zhong.lasagna.common.MyApplication;
import com.example.zhong.lasagna.util.HttpUtil;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.io.IOException;
import java.util.ArrayList;
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
    Unbinder unbinder;

    private BaseQuickAdapter adapter;
    private HomeJB homeJB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

//        initRecycler();
        queryServices();
        return view;
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        homeJB = new HomeJB();
        HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(homeJB.getStatuses());
        recycler.setAdapter(adapter);
        adapter.setEmptyView(R.layout.empty_default,recycler);
    }

    private void queryServices() {
//        获取当前登录用户及其所关注（授权）用户的最新微博
        String url = "https://api.weibo.com/2/statuses/home_timeline.json";
        Log.e("hello", "queryServices url=" + url + MyApplication.getAccessToken());
        HttpUtil.sendOkHttp(url + MyApplication.getAccessToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("hello","sendOkHttpFailure....");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("hello","sendOkHttpSuccess....");
                homeJB = HttpUtil.HandleHttpResponse(response.body().string(), HomeJB.class);
                HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(homeJB.getStatuses());
                getActivity().runOnUiThread(() -> {
                    recycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    adapter.setEmptyView(R.layout.empty_default,recycler);
                    recycler.setAdapter(adapter);
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class HomeRecyclerAdapter  extends BaseMultiItemQuickAdapter<HomeJB.StatusesBean, BaseViewHolder> {

        public HomeRecyclerAdapter(List data) {
            super(data);
            addItemType(HomeJB.StatusesBean.ORIGINAL, R.layout.home_recycler_original_adapter_item);
            addItemType(HomeJB.StatusesBean.RETWEET, R.layout.mainfragment_weiboitem_retweet_pictext);
        }

        @Override
        protected void convert(BaseViewHolder holder, HomeJB.StatusesBean item) {
            switch (holder.getItemViewType()) {
                case HomeJB.StatusesBean.ORIGINAL:
                    FillContent.fillTitleBar(holder,item);
                    FillContent.fillButtonBar(holder,item);
                    FillContent.fillWeiBoContent(holder,item,HomeJB.StatusesBean.ORIGINAL);
                    FillContent.fillWeiBoImgList(holder,item,HomeJB.StatusesBean.ORIGINAL);
                    break;

                case HomeJB.StatusesBean.RETWEET:
                    FillContent.fillTitleBar(holder,item);
                    FillContent.fillButtonBar(holder,item);
                    FillContent.fillWeiBoContent(holder,item,HomeJB.StatusesBean.RETWEET);
                    FillContent.fillRetweetContent(holder,item);
                    FillContent.fillWeiBoImgList(holder,item,HomeJB.StatusesBean.RETWEET);
                    break;
            }

        }
    }

//    private void setRecyclerListenter(){
//        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override public void onLoadMoreRequested() {
//                mRecyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mCurrentCounter >= TOTAL_COUNTER) {
//                            //数据全部加载完毕
//                            mQuickAdapter.loadMoreEnd();
//                        } else {
//                            if (isErr) {
//                                //成功获取更多数据
//                                mQuickAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
//                                mCurrentCounter = mQuickAdapter.getData().size();
//                                mQuickAdapter.loadMoreComplete();
//                            } else {
//                                //获取更多数据失败
//                                isErr = true;
//                                Toast.makeText(PullToRefreshUseActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
//                                mQuickAdapter.loadMoreFail();
//
//                            }
//                        }
//                    }
//
//                }, delayMillis);
//            }
//        }, mReyclerView);
//
//        adapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
//            @Override
//            public void onUpFetch() {
//                startUpFetch();
//            }
//        });
//
//        private void startUpFetch() {
//            count++;
//            /**
//             * set fetching on when start network request.
//             */
//            mAdapter.setUpFetching(true);
//            /**
//             * get data from internet.
//             */
//            mRecyclerView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mAdapter.addData(0, genData());
//                    /**
//                     * set fetching off when network request ends.
//                     */
//                    mAdapter.setUpFetching(false);
//                    /**
//                     * set fetch enable false when you don't need anymore.
//                     */
//                    if (count > 5) {
//                        mAdapter.setUpFetchEnable(false);
//                    }
//                }
//            }, 300);
//        }
//    }

}
