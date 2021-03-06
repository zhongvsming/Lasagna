package com.example.zhong.lasagna.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zhong.lasagna.R;
import com.example.zhong.lasagna.common.GlideApp;
import com.example.zhong.lasagna.common.MyApplication;
import com.example.zhong.lasagna.util.DateUtils;
import com.example.zhong.lasagna.util.TimeUtils;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FillContent {

    /**填充微博转发顶部信息
     *
     * @param holder
     * @param item
     */
    public static void fillTitleBar(BaseViewHolder holder,HomeJB.StatusesBean item) {
        setHeadImg(holder,item);
        setWeiBoName(holder,item);
        setWeiBoTime(holder,item);
//        setWeiBoComeFrom(holder,item);
    }

    private static void setWeiBoName(BaseViewHolder holder,HomeJB.StatusesBean item){
        holder.setText(R.id.tv_name,item.getUser().getName());
    }

    private static void setWeiBoTime(BaseViewHolder holder,HomeJB.StatusesBean item){
        Date data = DateUtils.parseDate(item.getCreated_at(), DateUtils.WeiBo_ITEM_DATE_FORMAT);
        TimeUtils timeUtils = TimeUtils.instance(MyApplication.getGlobalContext());
        holder.setText(R.id.tv_time, timeUtils.buildTimeString(data.getTime()) + " ");
    }

    private static void setHeadImg(BaseViewHolder holder,HomeJB.StatusesBean item){
        Uri uri = Uri.parse(item.getUser().getProfile_image_url());
        GlideApp.with(MyApplication.getGlobalContext()).load(uri).into((ImageView) holder.getView(R.id.im_head));
//        Log.e("hello", "headImage add=" + item.getUser().getProfile_image_url());
    }

    private static void setWeiBoComeFrom(BaseViewHolder holder,HomeJB.StatusesBean item){
    }

    /**填充微博转发，评论，赞的数量
     *
     */
    public static void fillButtonBar(BaseViewHolder holder,HomeJB.StatusesBean item) {
        if(item.getReposts_count()>0)
            holder.setText(R.id.tv_forward, String.valueOf(item.getReposts_count()));
        if(item.getComments_count()>0)
            holder.setText(R.id.tv_comment, String.valueOf(item.getComments_count()));
        if(item.getAttitudes_count()>0)
            holder.setText(R.id.tv_like, String.valueOf(item.getAttitudes_count()));
    }

    /**
     * 填充原创微博文字内容
     */
    public static void fillWeiBoContent(BaseViewHolder holder,HomeJB.StatusesBean item,int type) {
        if(type==HomeJB.StatusesBean.ORIGINAL)
            holder.setText(R.id.tv_content, weiBoContentFillter(item.getText()));
        if(type==HomeJB.StatusesBean.RETWEET)
            holder.setText(R.id.retweet_content, weiBoContentFillter(item.getText()));
    }
    private static String weiBoContentFillter(String text){
        if (text.contains("全文：")){
            String weiBoContent = text.substring(0, text.lastIndexOf("全文：")+2);
//            Log.e("hello", "on execute");
//            Log.e("hello", "全文： index="+text.lastIndexOf("全文："));
            return weiBoContent;
        }
        return text;
    }

    /**
     * 填充微博图片列表,包括原创微博和转发微博中的图片都可以使用
     */
    public static void fillWeiBoImgList(BaseViewHolder holder,HomeJB.StatusesBean item,int type) {
        if(type==HomeJB.StatusesBean.ORIGINAL) {
            if(item.getPic_urls()!=null&&!item.getPic_urls().isEmpty()&&item.getBmiddle_pic()!=null){
//                List<ImageInfo> imageInfo = handleImage(item,HomeJB.StatusesBean.ORIGINAL);
                String bmiddle_pic = item.getBmiddle_pic();
                int lastIndexOf = bmiddle_pic.lastIndexOf("/");
                String bmiddle_pic_add = bmiddle_pic.substring(0, lastIndexOf+1);
                ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                for (Object o : item.getPic_urls()) {
                    ImageInfo info = new ImageInfo();
                    String picUrl = ((HomeJB.StatusesBean.PicUrlsBeanX) o).getThumbnail_pic();
//                    Log.e("hello", " 小图片地址="+picUrl);
                    info.setThumbnailUrl(picUrl);
                    int lastIndexOf1 = picUrl.lastIndexOf("/");
                    String substring = picUrl.substring(lastIndexOf1 + 1);
                    info.setBigImageUrl(bmiddle_pic_add+substring);
//                    Log.e("hello", " 大图片地址="+bmiddle_pic_add+substring);
                    imageInfo.add(info);
                }
//                holder.setAdapter(R.id.recycler_image, (Adapter) new NineGridViewClickAdapter(MyApplication.getGlobalContext(), imageInfo));
                ((NineGridView)holder.getView(R.id.nine_grid_view_image)).setAdapter(new NineGridViewClickAdapter(MyApplication.getGlobalContext(),imageInfo));
            }else {
                holder.setGone(R.id.nine_grid_view_image, true);
            }
        }
        if(type==HomeJB.StatusesBean.RETWEET){
            if(item.getRetweeted_status().getPic_urls()!=null&&!item.getRetweeted_status().getPic_urls().isEmpty()&&item.getRetweeted_status().getBmiddle_pic()!=null)
            {
//                List<ImageInfo> imageInfo = handleImage(item,HomeJB.StatusesBean.RETWEET);
                String bmiddle_pic = item.getRetweeted_status().getBmiddle_pic();
                int lastIndexOf = bmiddle_pic.lastIndexOf("/");
                String bmiddle_pic_add = bmiddle_pic.substring(0, lastIndexOf+1);
                ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                for (Object o : item.getRetweeted_status().getPic_urls()) {
                    ImageInfo info = new ImageInfo();
                    String picUrl = ((HomeJB.StatusesBean.RetweetedStatusBean.PicUrlsBean) o).getThumbnail_pic();
                    Log.e("hello", " 小图片地址="+picUrl);
                    info.setThumbnailUrl(picUrl);
                    int lastIndexOf1 = picUrl.lastIndexOf("/");
                    String substring = picUrl.substring(lastIndexOf1 + 1);
                    info.setBigImageUrl(bmiddle_pic_add+substring);
                    Log.e("hello", " 大图片地址="+bmiddle_pic_add+substring);
                    imageInfo.add(info);
                }
//                holder.setAdapter(R.id.retweet_imageList, (Adapter) new NineGridViewClickAdapter(MyApplication.getGlobalContext(), imageInfo));
                ((NineGridView)holder.getView(R.id.nine_grid_retweet_imageList)).setAdapter(new NineGridViewClickAdapter(MyApplication.getGlobalContext(),imageInfo));
            }else {
                holder.setGone(R.id.nine_grid_retweet_imageList, true);
            }
        }
    }

    /**
     * 填充转发微博文字内容
     */
    public static void fillRetweetContent(BaseViewHolder holder,HomeJB.StatusesBean item) {
        if (item.getRetweeted_status()!= null) {
            StringBuffer retweetcontent_buffer = new StringBuffer();
            retweetcontent_buffer.setLength(0);
            retweetcontent_buffer.append("@");
            retweetcontent_buffer.append(item.getRetweeted_status().getUser().getName()+ " :  ");
            retweetcontent_buffer.append(item.getRetweeted_status().getText());
            holder.setText(R.id.origin_nameAndcontent, retweetcontent_buffer);
        } else {
            holder.setText(R.id.origin_nameAndcontent, "抱歉，此微博已被作者删除。查看帮助：#网页链接#");
        }
    }

    /**
     * 给控件添加监听器
     */
    public static void addListener(BaseViewHolder holder,int type) {
        holder.addOnClickListener(R.id.im_head)
                .addOnClickListener(R.id.tv_name)
                .addOnClickListener(R.id.bt_more)
                .addOnClickListener(R.id.ll_forward)
                .addOnClickListener(R.id.ll_comment)
                .addOnClickListener(R.id.ll_like);
        if (type == HomeJB.StatusesBean.RETWEET) {
            holder.addOnClickListener(R.id.retweetStatus_layout);
        }
    }

}
