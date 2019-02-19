package com.example.zhong.lasagna.message;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.GlideApp;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

public class MentionRecyclerAdapter extends BaseQuickAdapter<MentionBean.StatusesBean, BaseViewHolder> {
    private Context context;

    MentionRecyclerAdapter(Context context,int layoutResId, @Nullable List<MentionBean.StatusesBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MentionBean.StatusesBean item) {
        helper.setText(R.id.tv_name, item.getUser().getScreen_name());
        helper.setText(R.id.tv_time, item.getCreated_at());
        Uri headImgUrl = Uri.parse(item.getUser().getProfile_image_url());
        GlideApp.with(context).load(headImgUrl).into((ImageView) helper.getView(R.id.im_head));
        helper.setText(R.id.mention_content, item.getText());

        NineGridView nineGridView=helper.getView(R.id.weibo_image);
        if (item.getPic_ids()!=null&&!item.getPic_ids().isEmpty()&&item.getBmiddle_pic()!=null)
        {
            nineGridView.setVisibility(View.VISIBLE);
            int lastIndexOf = item.getBmiddle_pic().lastIndexOf("/");
            String imgUrl = item.getBmiddle_pic().substring(0, lastIndexOf + 1);
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for (Object o : item.getPic_ids()) {
                ImageInfo info = new ImageInfo();
                    Log.e("hello", " 小图片地址="+item.getBmiddle_pic());
                info.setThumbnailUrl(item.getBmiddle_pic());
                Log.e("hello", " 大图片地址="+imgUrl+o.toString());
                info.setBigImageUrl(imgUrl+o.toString());
                imageInfo.add(info);
            }
            nineGridView.setAdapter(new NineGridViewClickAdapter(context,imageInfo));
        }else {
            nineGridView.setVisibility(View.GONE);
        }

        LinearLayout retweet = helper.getView(R.id.retweet);
        if(item.getRetweeted_status()!=null)
        {
            retweet.setVisibility(View.VISIBLE);
            GlideApp.with(context).load(item.getRetweeted_status().getBmiddle_pic()).into((ImageView) helper.getView(R.id.mentionitem_img));
//            helper.setText(R.id.mentionitem_name,item.getRetweeted_status().get);
            helper.setText(R.id.mentionitem_content, item.getRetweeted_status().getText());
        }

    }
}
