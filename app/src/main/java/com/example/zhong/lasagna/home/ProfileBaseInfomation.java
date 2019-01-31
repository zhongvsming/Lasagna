package com.example.zhong.lasagna.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.MyToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileBaseInfomation extends AppCompatActivity {
    @BindView(R.id.toolbar)
    MyToolBar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_base_infomation_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setLeftButtonClickListener(view -> {
            finish();
        });
    }
}
