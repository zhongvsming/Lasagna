package com.example.zhong.lasagna.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.util.DensityUtil;

public class MyToolBar extends LinearLayout {
    private Button btLeft;
    private Button btRight;
    private TextView textView;

    public MyToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));


        LayoutInflater.from(context).inflate(R.layout.my_tool_bar,this,true);
        btLeft = findViewById(R.id.bt_left);
        btRight = findViewById(R.id.bt_right);
        textView = findViewById(R.id.tv_middle);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyToolBar);

        btLeft.setText(typedArray.getString(R.styleable.MyToolBar_leftButtonName));
        btLeft.setTextColor(typedArray.getColor(R.styleable.MyToolBar_leftButtonTextColor, Color.BLACK));
        btLeft.setBackgroundResource(typedArray.getResourceId(R.styleable.MyToolBar_leftButtonBackground,Color.TRANSPARENT));
        if(typedArray.getInt(R.styleable.MyToolBar_leftButton, -1)==0)
        {
            btLeft.setVisibility(GONE);
        }

        btRight.setText(typedArray.getString(R.styleable.MyToolBar_rightButtonName));
        btRight.setTextColor(typedArray.getColor(R.styleable.MyToolBar_rightButtonTextColor, Color.BLACK));
        btRight.setBackgroundResource(typedArray.getResourceId(R.styleable.MyToolBar_rightButtonBackground,Color.TRANSPARENT));
        if(typedArray.getInt(R.styleable.MyToolBar_leftButton, -1)==0)
        {
            btRight.setVisibility(GONE);
        }

        textView.setText(typedArray.getString(R.styleable.MyToolBar_middleTextName));
        textView.setTextColor(typedArray.getColor(R.styleable.MyToolBar_middleTextColor,Color.BLACK));
        textView.setTextSize(typedArray.getDimensionPixelSize(R.styleable.MyToolBar_middleTextSize,18));

        typedArray.recycle();
    }

    public void setMiddleText(String text){
        textView.setText(text);
    }

    public void setLeftButtonClickListener(OnClickListener listener){
        btLeft.setOnClickListener(listener);
    }

    public void setRightButtonClickListener(OnClickListener listener)
    {
        btRight.setOnClickListener(listener);
    }

    public void setMiddleTextViewClickListener(OnClickListener listener)
    {
        textView.setOnClickListener(listener);
    }

}
