package com.example.zhong.lasagna.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.GlideApp;
import com.example.zhong.lasagna.common.MyApplication;
import com.example.zhong.lasagna.common.MyToolBar;
import com.example.zhong.lasagna.util.HttpUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileSwipeActivity extends AppCompatActivity {
    @BindView(R.id.im_head_background)
    ImageView imHeadBackground;
    @BindView(R.id.toolbar)
    MyToolBar toolbar;
    @BindView(R.id.head_image)
    ImageView headImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.tv_following)
    TextView tvFollowing;
    @BindView(R.id.tv_followers)
    TextView tvFollowers;
    @BindView(R.id.base_information)
    TextView baseInformation;
//    @BindView(R.id.recycler_weibo)
//    RecyclerView recyclerWeibo;

    // 根据用户ID获取用户信息
    private String urlUser = "https://api.weibo.com/2/users/show.json";
    private String urlWeiboList = "https://api.weibo.com/2/statuses/user_timeline.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_swipe_activity);
        ButterKnife.bind(this);
        sentHttp();
        initView();
    }

    private void initView() {
        baseInformation.setOnClickListener((view)->{
            Intent intent = new Intent(this, ProfileBaseInfomation.class);
            startActivity(intent);
        });
        toolbar.setLeftButtonClickListener((view)->{
            finish();
        });
    }

    private void sentHttp() {
        String userId = getIntent().getStringExtra(HomeRecyclerAdapter.USER_ID);
        Log.e("hello", "urlUser=" + urlUser + MyApplication.getAccessToken() + "&uid=" + userId);
        HttpUtil.sendOkHttp(urlUser + MyApplication.getAccessToken() + "&uid=" + userId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("hello", "sendOkHttpFailure....");
                Toast.makeText(MyApplication.getGlobalContext(), "加载数据失败，请检查网络", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                UserBean userBean = HttpUtil.HandleHttpResponse(response.body().string(), UserBean.class);
                if (!response.isSuccessful())
                {
                    Toast.makeText(MyApplication.getGlobalContext(), "没有授权的用户资料无法读取", Toast.LENGTH_LONG).show();
                }
                if (userBean!=null)
                {
                    runOnUiThread(()->{
                        Uri headImageUri = Uri.parse(userBean.getProfile_image_url());
                        GlideApp.with(MyApplication.getGlobalContext()).load(headImageUri).into(headImage);
                        Uri coverImageUri = Uri.parse(userBean.getCover_image_phone());
                        GlideApp.with(MyApplication.getGlobalContext()).load(coverImageUri).into(imHeadBackground);
                        userName.setText(userBean.getScreen_name());
                        tvFollowing.setText("关注： "+userBean.getFriends_count());
                        tvFollowers.setText("粉丝: "+userBean.getFollowers_count());
                    });
                }
            }
        });

    }

}
