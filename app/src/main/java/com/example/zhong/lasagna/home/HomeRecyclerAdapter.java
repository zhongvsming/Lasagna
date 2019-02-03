package com.example.zhong.lasagna.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zhong.lasagna.R;

import java.util.List;

public class HomeRecyclerAdapter  extends BaseMultiItemQuickAdapter<HomeJB.StatusesBean, BaseViewHolder>{
    public static final String USER_ID="USER_ID";
    public static final String WEIBO_ID="WEIBO_ID";
    private Context context;

        public HomeRecyclerAdapter(List data, Context context) {
            super(data);
            addItemType(HomeJB.StatusesBean.ORIGINAL, R.layout.home_recycler_original_adapter_item);
            addItemType(HomeJB.StatusesBean.RETWEET, R.layout.mainfragment_weiboitem_retweet_pictext);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder holder, HomeJB.StatusesBean item) {
            switch (holder.getItemViewType()) {
                case HomeJB.StatusesBean.ORIGINAL:
                    FillContent.fillTitleBar(holder, item);
                    FillContent.fillButtonBar(holder, item);
                    FillContent.fillWeiBoContent(holder, item, HomeJB.StatusesBean.ORIGINAL);
                    FillContent.fillWeiBoImgList(holder, item, HomeJB.StatusesBean.ORIGINAL);
                    FillContent.addListener(holder,HomeJB.StatusesBean.ORIGINAL);
                    break;

                case HomeJB.StatusesBean.RETWEET:
                    FillContent.fillTitleBar(holder, item);
                    FillContent.fillButtonBar(holder, item);
                    FillContent.fillWeiBoContent(holder, item, HomeJB.StatusesBean.RETWEET);
                    FillContent.fillRetweetContent(holder, item);
                    FillContent.fillWeiBoImgList(holder, item, HomeJB.StatusesBean.RETWEET);
                    break;
            }
        }

        public void setListener(){
            this.setOnItemClickListener((adapter, view, position) -> {
                Log.d("hello", "onItemClick: ");
                Object item = adapter.getItem(position);
                if(item instanceof HomeJB.StatusesBean){
                    Intent intent = new Intent(context, OriginDetailActivity.class);
                    intent.putExtra(WEIBO_ID, String.valueOf(((HomeJB.StatusesBean) item).getId()));
                    intent.putExtra(USER_ID, String.valueOf(((HomeJB.StatusesBean) item).getUser().getId()));
                    context.startActivity(intent);
                }
            });
            this.setOnItemChildClickListener((adapter, view, position) -> {
                Log.d("hello", "onItemChildClick: ");
                Object item = adapter.getItem(position);
                switch (view.getId())
                {
                    case R.id.im_head:
                        if(item instanceof HomeJB.StatusesBean){
                            Intent intent = new Intent(context, ProfileSwipeActivity.class);
                            intent.putExtra(USER_ID, ((HomeJB.StatusesBean) item).getUser().getId());
                            context.startActivity(intent);
                        }
                        break;
                    case R.id.tv_name:
                        if(item instanceof HomeJB.StatusesBean){
                            Intent intent = new Intent(context, ProfileSwipeActivity.class);
                            intent.putExtra(USER_ID, ((HomeJB.StatusesBean) item).getUser().getId());
                            context.startActivity(intent);
                        }
                        break;
                    case R.id.bt_more:
                        showListDialog();
                        break;
                    case R.id.ll_forward:
                        Toast.makeText(context,"接口不提供此功能", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ll_comment:
                        if(item instanceof HomeJB.StatusesBean){
                            Intent intent = new Intent(context, OriginDetailActivity.class);
                            intent.putExtra(WEIBO_ID, ((HomeJB.StatusesBean) item).getId());
                            intent.putExtra(USER_ID, ((HomeJB.StatusesBean) item).getUser().getId());
                            context.startActivity(intent);
                        }
                        break;
                    case R.id.ll_like:
                        Toast.makeText(context, "接口不提供此功能", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.retweetStatus_layout:
                        if(item instanceof HomeJB.StatusesBean){
                            Intent intent = new Intent(context, OriginDetailActivity.class);
                            intent.putExtra(WEIBO_ID, String.valueOf(((HomeJB.StatusesBean) item).getRetweeted_status().getId()));
                            intent.putExtra(USER_ID, String.valueOf(((HomeJB.StatusesBean) item).getRetweeted_status().getUser().getId()));
                            context.startActivity(intent);
                        }
                        break;
                }
            });
        }

    private void showListDialog() {
        final String[] items = { "收藏","用此卡片背景","帮上头条","取消关注" ,"屏蔽","投诉"};
//            AlertDialog listDialog = new AlertDialog.Builder(MyApplication.getGlobalContext())
//                    .setTitle("")
//                    .setItems(items, (dialogInterface, i) -> {
//                    }).create();
//            listDialog.show();
        android.app.AlertDialog listDialog = new android.app.AlertDialog.Builder(context)
                .setItems(items, (dialogInterface, i) -> {
                }).create();
        listDialog.setCanceledOnTouchOutside(true);
        listDialog.show();
    }

}
