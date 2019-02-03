package com.example.zhong.lasagna.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.zhong.lasagna.R;

public class DiscoverFragment extends Fragment {
    private RelativeLayout mPublicWeibo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.discover_fragment, container, false);
        mPublicWeibo =  mView.findViewById(R.id.publicweibo_layout);
        mPublicWeibo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HotWeiBoSwipeActivity.class);
            getContext().startActivity(intent);
        });
        return mView;
    }
}
