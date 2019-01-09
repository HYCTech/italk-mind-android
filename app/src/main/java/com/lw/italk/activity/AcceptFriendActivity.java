package com.lw.italk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.gson.friend.VerifyFriend;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.AcceptFriendRequest;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.GlideCircleTransform;
import com.lw.italk.framework.base.BaseActivity;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

/**
 *
 * Created by 喜明 on 2018/8/17.
 */

public class AcceptFriendActivity extends BaseActivity implements Response{

    @BindView(R.id.left_bar_item)
    TextView mLeftBarItem;
    @BindView(R.id.center_bar_item)
    TextView mCenterBarItem;
    @BindView(R.id.title_bar_left)
    LinearLayout titleBarLeft;
    @BindView(R.id.right_title_bar)
    TextView mRightTitleBar;
    @BindView(R.id.title_bar_right)
    RelativeLayout titleBarRight;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.remark)
    TextView remark;
    @BindView(R.id.arrow)
    ImageView arrow;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.tv_user_number)
    TextView tvUserNumber;
    @BindView(R.id.user_number)
    TextView mTvUserNumber;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.init)
    RelativeLayout init;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.complete)
    Button complete;
    @BindView(R.id.nick_name)
    TextView mTvNickName;

    private String mHeadUrl = "";
    private String mNickName = "";
    private String mPhoneNumber = "";
    private String mUserId = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mHeadUrl = getIntent().getStringExtra(Constants.HEADURL);
        mNickName = getIntent().getStringExtra(Constants.NAME);
        mUserId = getIntent().getStringExtra(Constants.User_ID);
        mPhoneNumber = getIntent().getStringExtra(Constants.PHONE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_accept_friend;
    }

    @Override
    protected void initView() {
        Glide.with(this).load(mHeadUrl)
                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                .transform(new GlideCircleTransform(App.getInstance()))
                .into(logo);
        mTvNickName.setText(mNickName);
        mTvUserNumber.setText(mPhoneNumber);
    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("通讯录");
        mCenterBarItem.setText("详细资料");
        mRightTitleBar.setVisibility(View.GONE);
        Eyes.translucentStatusBar(this, true);
    }

    private void toSetRemarkInfo(){
        Intent intent = new Intent(AcceptFriendActivity.this, SetRemarkActivity.class);
        intent.putExtra(Constants.NAME, mNickName);
        intent.putExtra(Constants.HEADURL, mHeadUrl);
        intent.putExtra(Constants.PHONE, mPhoneNumber);
        intent.putExtra(Constants.User_ID, mUserId);
        startActivity(intent);
    }

    @OnClick({R.id.left_bar_item, R.id.remark, R.id.logo, R.id.complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.remark:
                toSetRemarkInfo();
                break;
            case R.id.logo:
                break;
            case R.id.complete:
                acceptFriend();
                break;
        }
    }

    private void acceptFriend() {
        AcceptFriendRequest request = new AcceptFriendRequest();
        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
        request.setTo_account(mUserId);
        HttpUtils.doPost(this, Request.Path.FRIEND_VERIFY_FRIEND, request, true, Request.Code.FRIEND_VERIFY_FRIEND, this);
    }


    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.FRIEND_VERIFY_FRIEND:
                VerifyFriend item = (VerifyFriend)o;
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {

    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.FRIEND_VERIFY_FRIEND:
                type = new TypeToken<BaseResponse<VerifyFriend>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }
}
