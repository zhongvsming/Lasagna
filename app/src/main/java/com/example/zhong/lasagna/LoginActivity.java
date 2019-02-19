package com.example.zhong.lasagna;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhong.lasagna.common.MyApplication;
import com.example.zhong.lasagna.common.UserIdJB;
import com.example.zhong.lasagna.home.HomeJB;
import com.example.zhong.lasagna.util.HttpUtil;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_head)
    ImageView loginHead;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.forgetPassWord)
    TextView forgetPassWord;
    @BindView(R.id.createNewAccount)
    TextView createNewAccount;
    @BindView(R.id.weibo_login)
    ImageView weiboLogin;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        mSsoHandler = new SsoHandler(this);


    }

    @OnClick({R.id.login, R.id.forgetPassWord, R.id.createNewAccount,R.id.weibo_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (TextUtils.isEmpty(account.getText())){
                    Toast.makeText(this,"请填写账号",Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password.getText())){
                    Toast.makeText(this,"请填写密码",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"账号或密码不正确",Toast.LENGTH_SHORT).show();
                }
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
                break;
            case R.id.forgetPassWord:
                break;
            case R.id.createNewAccount:
                break;
            case R.id.weibo_login:
                // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
                mSsoHandler.authorize(new SelfWbAuthListener());
                break;
        }
    }


    private class SelfWbAuthListener implements WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            LoginActivity.this.runOnUiThread(() -> {
                mAccessToken = token;
                if (mAccessToken.isSessionValid()) {
                    // 显示 Token
//                        updateTokenView(false);
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                    Toast.makeText(LoginActivity.this,
                            "登录成功", Toast.LENGTH_SHORT).show();
                    MyApplication.setAccessToken("?access_token="+AccessTokenKeeper.readAccessToken(MyApplication.getGlobalContext()).getToken());
                    getUserId();
                    Intent intent = new Intent(MyApplication.getGlobalContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public void cancel() {
//            Toast.makeText(LoginActivity.this, "取消登录", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Toast.makeText(LoginActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getUserId(){
        String url = "https://api.weibo.com/2/account/get_uid.json";
        HttpUtil.sendOkHttp(url + MyApplication.getAccessToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("hello", "sendOkHttpFailure....");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                UserIdJB userIdJB = HttpUtil.HandleHttpResponse(response.body().string(), UserIdJB.class);
                MyApplication.setUserId("&uid="+userIdJB.getUid());
            }
        });
    }

    // 用户登出
//    findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            AccessTokenKeeper.clear(getApplicationContext());
//            mAccessToken = new Oauth2AccessToken();
//            updateTokenView(false);
//        }
//    });

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }
}
