package com.example.zhong.lasagna.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.MyApplication;
import com.example.zhong.lasagna.util.HttpUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MentionSwipeActivity extends AppCompatActivity {
    @BindView(R.id.base_RecyclerView)
    RecyclerView recyclerView;

    //    获取最新的提到登录用户的微博列表，即@我的微博
    private String url = "https://api.weibo.com/2/statuses/mentions.json";
    private MentionRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagefragment_mention_layout);
        ButterKnife.bind(this);
//        sentHttp();
    }

    private void sentHttp() {
        HttpUtil.sendOkHttp(url + MyApplication.getAccessToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("hello", "sendOkHttpFailure....");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("hello", "sendOkHttpSuccess....");
                MentionBean mentionBean = HttpUtil.HandleHttpResponse(response.body().string(), MentionBean.class);
                if (mentionBean != null)
//                    adapter = new MentionRecyclerAdapter(R.layout.messagefragment_mentionlist_item, mentionBean.getStatuses());
                runOnUiThread(() -> {
                    recyclerView.setLayoutManager(new LinearLayoutManager(MentionSwipeActivity.this,LinearLayoutManager.VERTICAL,false));
                    recyclerView.setAdapter(adapter);

                });

            }
        });
    }
}
