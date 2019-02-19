package com.example.zhong.lasagna;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.example.zhong.lasagna.discover.DiscoverFragment;
import com.example.zhong.lasagna.home.HomeFragment;
import com.example.zhong.lasagna.message.MessageFragment;
import com.example.zhong.lasagna.mine.MineFragment;
import com.example.zhong.lasagna.postweibo.PostActivity;
import com.example.zhong.lasagna.util.DensityUtil;

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
    @BindView(R.id.radio_button_main)
    ImageView radioButtonMain;

    private Fragment fgMine, fgHome, fgDiscover, fgMessage;
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

    private void showFragment(int i) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (i) {
            case 0:
                if (!fgHome.isAdded()) {
                    transaction.add(R.id.frame_layout, fgHome);
                    transaction.show(fgHome);
                    currentFg = fgHome;
                } else {
                    transaction.hide(currentFg).show(fgHome);
                    currentFg = fgHome;
                }
                break;
            case 1:
                if (!fgDiscover.isAdded()) {
                    transaction.add(R.id.frame_layout, fgDiscover);
                }
                transaction.hide(currentFg).show(fgDiscover);
                currentFg = fgDiscover;
                break;
            case 2:
                if (!fgMessage.isAdded()) {
                    transaction.add(R.id.frame_layout, fgMessage);
                }
                transaction.hide(currentFg).show(fgMessage);
                currentFg = fgMessage;
                break;
            case 3:
                if (!fgMine.isAdded()) {
                    transaction.add(R.id.frame_layout, fgMine);
                }
                transaction.hide(currentFg).show(fgMine);
                currentFg = fgMine;
                break;
        }
        transaction.commit();
    }

    @OnClick({R.id.radio_button_home, R.id.radio_button_discover, R.id.radio_button_message, R.id.radio_button_mine,R.id.radio_button_main})
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
            case R.id.radio_button_main:
                showChoseDialog();
                break;
        }
    }

    private void showChoseDialog(){
        View contentView = LayoutInflater.from(this).inflate(R.layout.write_weibo_chose_dialog, null);
        // 设置按钮的点击事件
        LinearLayout btWrite = contentView.findViewById(R.id.linearLayout_write_weiBo);
        LinearLayout btPhoto = contentView.findViewById(R.id.linearLayout_photo);
        btWrite.setOnClickListener(view->{
//            Log.e("hello", "btOnclick");
            Intent intent = new Intent(this, PostActivity.class);
            startActivity(intent);
        });
        btPhoto.setOnClickListener(view->{
            Log.e("hello", "btOnclick");
        });
        PopupWindow popupWindow = new PopupWindow(contentView, DensityUtil.dipToPx(this,150),ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setWidth(contentView.getWidth());
        // 设置动画
//        window.setAnimationStyle(R.style.popup_window_anim);
        // 设置背景颜色
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        // 设置可以获取焦点
        popupWindow.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        popupWindow.setOutsideTouchable(true);
        // 更新popupwindow的状态
//        window.update();
        // 以下拉的方式显示，并且可以设置显示的位置
//        popupWindow.showAsDropDown(radioButtonMain, 0, 50);
        popupWindow.showAtLocation(radioButtonMain, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 135);//这里的50是因为我底部按钮的高度是50
    }

}
