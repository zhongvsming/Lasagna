package com.example.zhong.lasagna.common;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.zhong.lasagna.R;
import com.lzy.ninegrid.NineGridView;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

public class MyApplication extends Application {
    private static Context context;
//    private static String AccessToken = "?access_token=2.00G4RfvBdgLtNEa8077fcc92xQ5QBD";
    private static String AccessToken ;
//    private static String userId="&uid=1768222212";
    private static String userId;



    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        NineGridView.setImageLoader(new GlideImageLoader());
        WbSdk.install(context,new AuthInfo(context,"3869776663","https://api.weibo.com/oauth2/default.html",null));

    }

    public static Context getGlobalContext(){
        return context;
    }

    public static String getAccessToken() {
        return AccessToken;
    }

    public static void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public static void setUserId(String userId) {
        MyApplication.userId = userId;
    }

    public static String getUserId() {
        return userId;
    }

    /** Glide 加载 */
    private class GlideImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            GlideApp.with(context)
                    .load(Uri.parse(url))
                    .placeholder(R.drawable.blackimage)
                    .error(R.drawable.errorimage)
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }


}

