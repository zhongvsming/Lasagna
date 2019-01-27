package com.example.zhong.lasagna.util;

import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    public static void sendOkHttp(String address,okhttp3.Callback callback)
    {
        Request request=new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
        Log.e("hello","sendOkHttpRequest....");

    }

    public static void interruptAllOkHttp(){
        Dispatcher dispatcher = client.dispatcher();
        synchronized (dispatcher){
            dispatcher.cancelAll();
        }
        Log.e("hello","OkHttpRequest Cancel....");
    }

    public static<T> T HandleHttpResponse(String response,Class<T> classOfT){
        if(!TextUtils.isEmpty(response))
        {
            try{
                Gson gson=new Gson();
                T t=gson.fromJson(response, classOfT);
                Log.e("hello","new Gson successful");
                return t;
            }catch (Exception e){
                Log.e("hello","new Gson OnFailure");
                e.printStackTrace();
            }
        }
        return null;
    }

}
