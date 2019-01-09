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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.gson.friend.AddFriend;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.GlideCircleTransform;
import com.lw.italk.utils.Utils;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

/**
 * Created by lxm on 2018/8/28.
 */
public class AddNetFriendActivity extends BaseActivity implements Response{

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
    @BindView(R.id.star_mark)
    ImageView mStarMark;

    private String mHeadUrl = "";
    private String mNickName = "";
    private String mPhoneNumber = "";
    private String mUserID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mHeadUrl = getIntent().getStringExtra(Constants.HEADURL);
        mNickName = getIntent().getStringExtra(Constants.NAME);
        mPhoneNumber = getIntent().getStringExtra(Constants.PHONE);
        mUserID = getIntent().getStringExtra(Constants.User_ID);
        Eyes.translucentStatusBar(this, true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_add_net_friend;
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
    protected void onDestroy() {
        super.onDestroy();
        LWJNIManager.getInstance().unregisterMsgSendUpdateListen();
    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("通讯录");
        mCenterBarItem.setText("详细资料");
        mRightTitleBar.setText("");
        mRightTitleBar.setBackground(getResources().getDrawable(R.mipmap.icon_more));

       /* HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("uid", mUserID);
        HttpUtils.doPostFormMap(this, Request.Path.USER_QUERYUSER, map, true, Request.Code.USER_QUERYUSER, this);*/
    }

    private void toSettingRemark(){
        Intent intent = new Intent(AddNetFriendActivity.this , SetRemarkActivity.class);
        intent.putExtra(Constants.NAME, mNickName);
        intent.putExtra(Constants.HEADURL, mHeadUrl);
        intent.putExtra(Constants.PHONE, mPhoneNumber);
        intent.putExtra(Constants.User_ID, mUserID);
        startActivity(intent);
    }

    @OnClick({R.id.left_bar_item, R.id.remark, R.id.logo, R.id.complete, R.id.right_title_bar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.right_title_bar:
                toSettingRemark();
                break;
            case R.id.remark:
                toSettingRemark();
                break;
            case R.id.logo:
                break;
            case R.id.complete:
                addFriend();
                break;
        }
    }

    private void addFriend() {
        Intent intent = new Intent(AddNetFriendActivity.this,FriendVerificationActivity.class);
        intent.putExtra(Constants.User_ID, mUserID);
        intent.putExtra(Constants.NAME, mNickName);
        intent.putExtra(Constants.HEADURL, mHeadUrl);
        startActivity(intent);
        LWJNIManager.getInstance().sendFriendRq(mPhoneNumber, "");
//        Utils.showLongToast(AddNetFriendActivity.this, "发送好友申请成功!");
        finish();
//        LWJNIManager.getInstance().registerMsgSendListen(new MsgSendRq() {
//
//            @Override
//            public void msgSendResponseSuceess(long localid) {
//                if (localid == localid) {
//                    AddNetFriendActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Utils.showLongToast(AddNetFriendActivity.this, "发送好友申请成功!");
//                            finish();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void msgSendResponseError(long localid) {
//                Utils.showLongToast(AddNetFriendActivity.this, "发送好友申请失败!");
//            }
//        });

//        AddFriendRequest request = new AddFriendRequest();
//        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUserid());
//        request.setTo_account(mPhoneNumber);
//        HttpUtils.doPost(this, Request.Path.FRIEND_ADDFRIEND, request, true, Request.Code.FRIEND_ADDFRIEND, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.FRIEND_ADDFRIEND:
                AddFriend item = (AddFriend)o;
                Utils.showLongToast(this, "发送好友申请成功!");
                finish();
                break;
            case Request.Code.USER_QUERYUSER:
                Contact items = ((Contact) o);
                if(items == null){//用户是否存在
                }else{
//                    if(== null){
                    Contact contact = LWDBManager.getInstance().queryFriendItem(items.getUid());
                    contact.setMobile(items.getMobile());
                    contact.setEmail(items.getEmail());
                    LWDBManager.getInstance().updateContact(contact);
                    mTvUserNumber.setText(items.getMobile());
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.FRIEND_ADDFRIEND:
                type = new TypeToken<BaseResponse<AddFriend>>() {
                }.getType();
                break;
            case Request.Code.USER_QUERYUSER:
                type = new TypeToken<BaseResponse<Contact>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }
}
