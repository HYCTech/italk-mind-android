package com.lw.italk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.kyleduo.switchbutton.SwitchButton;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.gson.friend.AddToBlackList;
import com.lw.italk.gson.friend.DeleteFriend;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.AddToBlackListRequest;
import com.lw.italk.http.model.DeleteFriendRequest;
import com.lw.italk.utils.Constants;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flyn.Eyes;

/**
 * 设置朋友资料界面
 * Created by 喜明 on 2018/8/14.
 */

public class SettingFriendInfoActivity extends BaseActivity implements Response, CompoundButton.OnCheckedChangeListener {
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
    @BindView(R.id.setting_remark)
    TextView mSettingRemark;
    @BindView(R.id.setting_star_mark)
    RelativeLayout mSettingStarMark;
    @BindView(R.id.send_friend_card)
    TextView mSendFriendCard;
    @BindView(R.id.add_black_list)
    RelativeLayout mAddBlackList;
    @BindView(R.id.delete)
    TextView mDelete;
    @BindView(R.id.add_to_black_list)
    SwitchButton mAddToBlackList;

    private String mHeadUrl = "";
    private String mNickName = "";
    private String mPhoneNumber = "";
    private String mUserID = "";

    @Override
    protected int setContentView() {
        return R.layout.activity_setting_friend_info;
    }

    @Override
    protected void initView() {
        mAddToBlackList.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("详细资料");
        mCenterBarItem.setText("资料设置");
        mRightTitleBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mHeadUrl = getIntent().getStringExtra(Constants.HEADURL);
        mNickName = getIntent().getStringExtra(Constants.NAME);
        mPhoneNumber = getIntent().getStringExtra(Constants.PHONE);
        mUserID = getIntent().getStringExtra(Constants.User_ID);
        System.out.println( "SettingFriendInfoActivity------>mHeadUrl:"+mHeadUrl+"--->mHeadUrl:"+mNickName+"--->mPhoneNumber"+mPhoneNumber+"---mUserID:"+mUserID);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Eyes.translucentStatusBar(this, true);
    }

    private void toSetRemarkInfo(){
        Intent intent = new Intent(SettingFriendInfoActivity.this, SetRemarkActivity.class);
        intent.putExtra(Constants.NAME, mNickName);
        intent.putExtra(Constants.HEADURL, mHeadUrl);
        intent.putExtra(Constants.PHONE, mPhoneNumber);
        intent.putExtra(Constants.User_ID, mUserID);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.left_bar_item, R.id.setting_remark, R.id.setting_star_mark, R.id.send_friend_card, R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:

                Intent intent = new Intent(SettingFriendInfoActivity.this, SendChatActivity.class);
                intent.putExtra(Constants.NAME,mNickName );
                intent.putExtra(Constants.HEADURL, mHeadUrl);
                intent.putExtra(Constants.PHONE, mPhoneNumber);
                intent.putExtra(Constants.User_ID, mUserID);
                startActivity(intent);
                finish();
                break;
            case R.id.setting_remark:
            case R.id.send_friend_card:
                toSetRemarkInfo();
                break;
            case R.id.setting_star_mark:
                break;
            case R.id.delete:
                deleteFriend();
                break;
            default:
                break;
        }
    }

    private void addToBlackList(){
        AddToBlackListRequest request = new AddToBlackListRequest();
        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
        request.setTo_account(mPhoneNumber);
        HttpUtils.doPost(this, Request.Path.FRIEND_ADDTOBLACKLIST, request, true, Request.Code.FRIEND_ADDTOBLACKLIST, this);
    }

    private void deleteFriend() {
//        DeleteFriendRequest request = new DeleteFriendRequest();
////        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
////        request.setTo_account(mPhoneNumber);
        Map<String, String> request = new HashMap<String, String>();
        request.put("fuid", mUserID);
        request.put("tokenId", LWUserManager.getInstance().getToken());//性别
        HttpUtils.doPostFormMap(this, Request.Path.FRIEND_DELETEFRIEND, request, true, Request.Code.FRIEND_DELETEFRIEND, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.FRIEND_DELETEFRIEND:
//                DeleteFriend items = (DeleteFriend) o;
                LWFriendManager.getInstance().deleteFriendItem(mUserID);
                Toast.makeText(this, "删除好友成功！", Toast.LENGTH_SHORT).show();
                LWConversationManager.getInstance().deleteByFid(mUserID);
                LWConversationManager.getInstance().deletConversationById(mUserID);
                Intent intent = new Intent(SettingFriendInfoActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case Request.Code.FRIEND_ADDTOBLACKLIST:
                LWFriendManager.getInstance().deleteFriendItem(mPhoneNumber);
                Toast.makeText(this, "加入黑名单成功！", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(SettingFriendInfoActivity.this, MainActivity.class);
                startActivity(intent1);
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
            case Request.Code.FRIEND_DELETEFRIEND:
                type = new TypeToken<BaseResponse<Object>>() {
                }.getType();
                break;
            case Request.Code.FRIEND_ADDTOBLACKLIST:
                type = new TypeToken<BaseResponse<AddToBlackList>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.add_to_black_list:
                addToBlackList();
                break;
            default:
                break;
        }
    }
}
