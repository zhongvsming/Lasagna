package com.example.zhong.lasagna.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.zhong.lasagna.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MessageFragment extends Fragment {
    @BindView(R.id.mention_layout)
    RelativeLayout mentionLayout;
    @BindView(R.id.comment_layout)
    RelativeLayout commentLayout;
    @BindView(R.id.attitude_layout)
    RelativeLayout attitudeLayout;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.mention_layout, R.id.comment_layout, R.id.attitude_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mention_layout:
                break;
            case R.id.comment_layout:
                break;
            case R.id.attitude_layout:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
