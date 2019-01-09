package com.lw.italk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.greendao.model.GroupInfo;
import com.lw.italk.greendao.model.User;
import com.lw.italk.gson.friend.AddFriend;
import com.lw.italk.gson.friend.RemarkInfo;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.GlideCircleTransform;
import com.lw.italk.utils.Utils;
import com.yixia.camera.util.Log;

import java.lang.reflect.Type;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import flyn.Eyes;

/**
 * 点击查看朋友界面
 * Created by 喜明 on 2018/8/17.
 */

public class SendChatActivity extends BaseActivity implements Response {

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
    private String mMarkNme = "";
    private String mUserID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mHeadUrl = getIntent().getStringExtra(Constants.HEADURL);
        mNickName = getIntent().getStringExtra(Constants.NAME);
        mPhoneNumber = getIntent().getStringExtra(Constants.PHONE);
        mUserID = getIntent().getStringExtra(Constants.User_ID);
        Eyes.translucentStatusBar(this, true);
        System.out.println("SendChatActivity----->mHeadUrl:" + mHeadUrl + "--->mHeadUrl:" + mNickName + "--->mPhoneNumber" + mPhoneNumber + "---mUserID:" + mUserID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_send_chat;
    }

    @Override
    protected void initView() {
        Glide.with(this).load(mHeadUrl)
                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                .transform(new GlideCircleTransform(App.getInstance()))
                .into(logo);
        //mTvUserNumber.setText(mPhoneNumber);
    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("---------------->onResume");
    /*    String nickName = LWFriendManager.getInstance().queryFriendItem(mUserID).getRemark();
        String phone = LWFriendManager.getInstance().queryFriendItem(mUserID).getMobile();
        if (!TextUtils.isEmpty(nickName)) {
            mTvNickName.setText(nickName);
        } else {
            mTvUserNumber.setText(mNickName);
        }

        if (!TextUtils.isEmpty(phone)) {
            mTvNickName.setText(phone);
        } else {
            mTvUserNumber.setText(mPhoneNumber);
        }
*/

    }

    @Override
    protected void initData() {
        System.out.println("---------------->initData");
        mLeftBarItem.setText("通讯录");
        mCenterBarItem.setText("详细资料");
        mRightTitleBar.setText("");
        mRightTitleBar.setBackground(getResources().getDrawable(R.mipmap.icon_more));

       /* HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("uid", mUserID);
        HttpUtils.doPostFormMap(this, Request.Path.USER_QUERYUSER, map, true, Request.Code.USER_QUERYUSER, this);
*/
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("uid", mUserID);
        HttpUtils.doPostFormMap(this, Request.Path.FRIEND_REMARKINFO, map, true, Request.Code.FRIEND_REMARKINFO, this);

    }

    private void toSetFriendInfo() {
        System.out.println("---------------->toSetFriendInfo");
        Intent intent = new Intent(SendChatActivity.this, SettingFriendInfoActivity.class);
        intent.putExtra(Constants.NAME,mNickName );
        intent.putExtra(Constants.HEADURL, mHeadUrl);
        intent.putExtra(Constants.PHONE, mPhoneNumber);
        intent.putExtra(Constants.User_ID, mUserID);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.left_bar_item, R.id.remark, R.id.logo, R.id.complete, R.id.right_title_bar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.right_title_bar:
            case R.id.remark:
                toSetFriendInfo();
                break;
            case R.id.logo:
                break;
            case R.id.complete:
                Conversation conversation = new Conversation();
                conversation.setIsGroup(false);
                conversation.setLocalid(mUserID);
                conversation.setImgurl(mHeadUrl);
                conversation.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
                conversation.setUsername(mNickName);
                LWConversationManager.getInstance().insertOrReplaceGroupChat(conversation);
                Intent intent = new Intent(SendChatActivity.this, ChatActivity.class);
                intent.putExtra(Constants.NAME, mNickName);// 设置昵称
                intent.putExtra(Constants.TYPE, LWConversationManager.CHATTYPE_SINGLE);
                intent.putExtra(Constants.User_ID, mUserID);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.USER_QUERYUSER:
                Contact items = ((Contact) o);
                if (items == null) {//用户是否存在
                } else {
                    String uid=items.getUid();
                    String username=items.getUsername();
                    String mobile=items.getMobile();
                    String avatar=items.getAvatar();
                    Contact contact = LWDBManager.getInstance().queryFriendItem(uid);
                    if(mNickName==""){
                        contact.setEmail(username);
                        mTvNickName.setText(username);
                        mNickName = getIntent().getStringExtra(Constants.NAME);
                        mNickName = username;
                    }
                    if(mPhoneNumber==""){
                        contact.setMobile(mobile);
                        mTvUserNumber.setText(mobile);
                        mPhoneNumber = mobile;
                    }

                    LWDBManager.getInstance().updateContact(contact);

                    Glide.with(this).load(avatar)
                            .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                            .transform(new GlideCircleTransform(App.getInstance()))
                            .into(logo);

                    mHeadUrl = avatar;

                }

                break;
            case Request.Code.FRIEND_REMARKINFO:
                RemarkInfo remarkInfo = (RemarkInfo) o;

                String remark = remarkInfo.getRemark();
                String mobile = remarkInfo.getMobile();
                mTvNickName.setText(remark);
                //mLabel.setText(items.getLabel());
                mTvUserNumber.setText(mobile);
                mNickName = remark;
                mPhoneNumber = mobile;


                HashMap<String, String> map = new HashMap<String, String>();
                map.put("tokenId", LWUserManager.getInstance().getToken());
                map.put("uid", mUserID);
                HttpUtils.doPostFormMap(this, Request.Path.USER_QUERYUSER, map, true, Request.Code.USER_QUERYUSER, this);


                break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        switch ((int) t.getErrorCode()) {
            case 80012:
                mPhoneNumber = getIntent().getStringExtra(Constants.PHONE);
                mTvNickName.setText(mNickName);

                Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.FRIEND_REMARKINFO:
                type = new TypeToken<BaseResponse<RemarkInfo>>() {
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
