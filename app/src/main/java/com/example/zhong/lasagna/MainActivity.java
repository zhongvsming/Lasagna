package com.example.zhong.lasagna;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.example.zhong.lasagna.discover.DiscoverFragment;
import com.example.zhong.lasagna.home.HomeFragment;
import com.example.zhong.lasagna.message.MessageFragment;
import com.example.zhong.lasagna.mine.MineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.radio_button_home)
    RadioButton radioButtonHome;
    @BindView(R.id.radio_button_discover)
    RadioButton radioButtonDiscover;
    @BindView(R.id.radio_button_message)
    RadioButton radioButtonMessage;
    @BindView(R.id.radio_button_mine)
    RadioButton radioButtonMine;

    private Fragment fgMine,fgHome,fgDiscover,fgMessage;
    private FragmentManager fragmentManager;
    private Fragment currentFg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        initFragment();
        showFragment(0);
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fgMine = new MineFragment();
        fgHome = new HomeFragment();
        fgDiscover = new DiscoverFragment();
        fgMessage = new MessageFragment();
    }

    private void showFragment(int i){
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        switch (i){
            case 0:
                if(!fgHome.isAdded())
                {
                    transaction.add(R.id.frame_layout, fgHome);
                    transaction.show(fgHome);
                    currentFg = fgHome;
                }else {
                    transaction.hide(currentFg).show(fgHome);
                    currentFg = fgHome;
                }
                break;
            case 1:
                if (!fgDiscover.isAdded())
                {
                    transaction.add(R.id.frame_layout, fgDiscover);
                }
                transaction.hide(currentFg).show(fgDiscover);
                currentFg = fgDiscover;
                break;
            case 2:
                if (!fgMessage.isAdded())
                {
                    transaction.add(R.id.frame_layout, fgMessage);
                }
                transaction.hide(currentFg).show(fgMessage);
                currentFg = fgMessage;
                break;
            case 3:
                if (!fgMine.isAdded())
                {
                    transaction.add(R.id.frame_layout, fgMine);
                }
                transaction.hide(currentFg).show(fgMine);
                currentFg = fgMine;
                break;
        }
        transaction.commit();
    }

    @OnClick({R.id.radio_button_home, R.id.radio_button_discover, R.id.radio_button_message, R.id.radio_button_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radio_button_home:
                showFragment(0);
                break;
            case R.id.radio_button_discover:
                showFragment(1);
                break;
            case R.id.radio_button_message:
                showFragment(2);
                break;
            case R.id.radio_button_mine:
                showFragment(3);
                break;
        }
    }
}
