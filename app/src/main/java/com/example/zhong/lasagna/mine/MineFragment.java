package com.example.zhong.lasagna.mine;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.GlideApp;
import com.example.zhong.lasagna.common.MyApplication;
import com.example.zhong.lasagna.home.HomeJB;
import com.example.zhong.lasagna.home.HomeRecyclerAdapter;
import com.example.zhong.lasagna.util.HttpUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MineFragment extends Fragment {
    @BindView(R.id.setting)
    TextView setting;
    @BindView(R.id.toolbar_mine)
    RelativeLayout toolbarMine;
    @BindView(R.id.profile_myimg)
    ImageView profileMyimg;
    @BindView(R.id.profile_myname)
    TextView profileMyname;
    @BindView(R.id.profile_mydescribe)
    TextView profileMydescribe;
    @BindView(R.id.myprofile_layout)
    RelativeLayout myprofileLayout;
    @BindView(R.id.profile_statuses_count)
    TextView profileStatusesCount;
    @BindView(R.id.yyweibo_layout)
    LinearLayout yyweiboLayout;
    @BindView(R.id.profile_friends_count)
    TextView profileFriendsCount;
    @BindView(R.id.friends_layout)
    LinearLayout friendsLayout;
    @BindView(R.id.profile_followers_count)
    TextView profileFollowersCount;
    @BindView(R.id.followers_layout)
    LinearLayout followersLayout;
    @BindView(R.id.myphoto_layout)
    RelativeLayout myphotoLayout;
    @BindView(R.id.favorities_layout)
    RelativeLayout favoritiesLayout;
    @BindView(R.id.nightMode_cb)
    CheckBox nightModeCb;
    @BindView(R.id.nightmode_rl)
    RelativeLayout nightmodeRl;
    @BindView(R.id.settingRl)
    RelativeLayout settingRl;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    Unbinder unbinder;

    //获取用户信息
    private String url = "https://api.weibo.com/2/users/show.json";
    private UserProfileJB userProfileJB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        sentHttp();
        return view;
    }

    private void sentHttp(){
        HttpUtil.sendOkHttp(url + MyApplication.getAccessToken() + MyApplication.getUserId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("hello", "sendOkHttpFailure....");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("hello", "sendOkHttpSuccess....");
                userProfileJB = HttpUtil.HandleHttpResponse(response.body().string(), UserProfileJB.class);
                if (userProfileJB!=null) {
                    getActivity().runOnUiThread(()->{
                        updateView();
                    });
                }
            }
        });
    }

    private void updateView() {
//        profileMyimg.setImageURI(Uri.parse(userProfileJB.getProfile_image_url()));
        GlideApp.with(getContext()).load(Uri.parse(userProfileJB.getProfile_image_url())).into(profileMyimg);
        profileMyname.setText(userProfileJB.getScreen_name());
        profileMydescribe.setText(userProfileJB.getDescription());
        profileStatusesCount.setText(String.valueOf(userProfileJB.getStatuses_count()));
        profileFriendsCount.setText(String.valueOf(userProfileJB.getFriends_count()));
        profileFollowersCount.setText(String.valueOf(userProfileJB.getFollowers_count()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.setting, R.id.toolbar_mine, R.id.profile_myimg, R.id.profile_myname, R.id.profile_mydescribe, R.id.myprofile_layout, R.id.profile_statuses_count, R.id.yyweibo_layout, R.id.profile_friends_count, R.id.friends_layout, R.id.profile_followers_count, R.id.followers_layout, R.id.myphoto_layout, R.id.favorities_layout, R.id.nightMode_cb, R.id.nightmode_rl, R.id.settingRl, R.id.scrollview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting:
                break;
            case R.id.toolbar_mine:
                break;
            case R.id.profile_myimg:
                break;
            case R.id.profile_myname:
                break;
            case R.id.profile_mydescribe:
                break;
            case R.id.myprofile_layout:
                break;
            case R.id.profile_statuses_count:
                break;
            case R.id.yyweibo_layout:
                break;
            case R.id.profile_friends_count:
                break;
            case R.id.friends_layout:
                break;
            case R.id.profile_followers_count:
                break;
            case R.id.followers_layout:
                break;
            case R.id.myphoto_layout:
                break;
            case R.id.favorities_layout:
                break;
            case R.id.nightMode_cb:
                break;
            case R.id.nightmode_rl:
                break;
            case R.id.settingRl:
                break;
            case R.id.scrollview:
                break;
        }
    }
}
