package com.example.zhong.lasagna.postweibo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhong.lasagna.R;

import org.w3c.dom.Comment;

public class PostActivity extends AppCompatActivity {
//    private UsersAPI mUsersAPI;
    private Context mContext;
    private TextView mCancal;
    private TextView mUserName;
    private TextView mSendButton;
    private TextView publicbutton;
    private ImageView picture;
    private ImageView mentionbutton;
    private ImageView trendbutton;
    private ImageView emoticonbutton;
    private ImageView more_button;
    private EditText mEditText;
    private TextView mLimitTextView;
    private TextView mInputType;
    private LinearLayout mRepostlayout;
    private ImageView repostImg;
    private TextView repostName;
    private TextView repostContent;
    private RecyclerView mRecyclerView;
    private ImageView mBlankspace;
    private LinearLayout mIdea_linearLayout;

//    private ArrayList<AlbumFolderInfo> mFolderList = new ArrayList<AlbumFolderInfo>();
//    private ArrayList<ImageInfo> mSelectImgList = new ArrayList<ImageInfo>();
//    private Status mStatus;
    private Comment mComment;
    private String mIdeaType;

    /**
     * 最多输入140个字符
     */
    private static final int TEXT_LIMIT = 140;

    /**
     * 在只剩下10个字可以输入的时候，做提醒
     */
    private static final int TEXT_REMIND = 10;
    private boolean mStartAlumbAcitivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);
//        mUsersAPI = new UsersAPI(mContext, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
        mInputType = findViewById(R.id.inputType);
        mCancal = findViewById(R.id.idea_cancal);
//        mUserName =  findViewById(R.id.idea_username);
        mSendButton =  findViewById(R.id.idea_send);
        publicbutton =  findViewById(R.id.publicbutton);
        picture = findViewById(R.id.picture);
        mentionbutton = findViewById(R.id.mentionbutton);
        trendbutton = findViewById(R.id.trendbutton);
        emoticonbutton = findViewById(R.id.emoticonbutton);
        more_button = findViewById(R.id.more_button);
        mEditText = findViewById(R.id.idea_content);
        mLimitTextView = findViewById(R.id.limitTextView);
//        mRepostlayout = findViewById(R.id.repost_layout);
//        repostImg = findViewById(R.id.repost_img);
//        repostName = findViewById(R.id.repost_name);
//        repostContent = findViewById(R.id.repost_content);
        mRecyclerView = findViewById(R.id.ImgList);
        mBlankspace = findViewById(R.id.blankspace);
        mIdea_linearLayout = findViewById(R.id.idea_linearLayout);

//        mIdeaType = getIntent().getStringExtra("ideaType");
//        mStatus = getIntent().getParcelableExtra("status");
//        mComment = getIntent().getParcelableExtra("comment");
        mInputType.setText(mIdeaType);

//        refreshUserName();
//        initContent();
//        setUpListener();
//        mEditText.setTag(false);
    }
}
