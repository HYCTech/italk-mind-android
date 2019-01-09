package com.lw.italk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.gson.friend.AddFriend;
import com.lw.italk.gson.friend.GetAddFrienditem;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.Utils;

import java.lang.reflect.Type;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 喜明 on 2018/8/18.
 */

public class FriendVerificationActivity extends BaseActivity implements Response {
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
    @BindView(R.id.friend_verification)
    EditText mFriendVerification;

    private String mUserID = "";
    private String mUserName = "";
    private String mAvatar = "";

    @Override
    protected int setContentView() {
        return R.layout.activity_friend_verification;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("详细资料");
        mCenterBarItem.setText("朋友验证");
        mRightTitleBar.setText("发送");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserID = getIntent().getStringExtra(Constants.User_ID);
        mUserName = getIntent().getStringExtra(Constants.NAME);
        mAvatar = getIntent().getStringExtra(Constants.HEADURL);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.left_bar_item, R.id.right_title_bar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.right_title_bar:
                addFriend();
                break;
            default:
                break;
        }
    }

    private void addFriend() {

//        LWJNIManager.getInstance().sendFriendVerify(mPhoneNumber);
//        Utils.showLongToast(this, "发送好友申请成功!");
//        finish();
//        AddFriendRequest request = new AddFriendRequest();
//        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUserid());
//        request.setTo_account(mPhoneNumber);
//        request.setAddword(mFriendVerification.getText().toString());
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("uid",mUserID);
        map.put("info", mFriendVerification.getText().toString());
        HttpUtils.doPostFormMap(this, Request.Path.FRIEND_ADDFRIEND, map, true, Request.Code.FRIEND_ADDFRIEND, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.FRIEND_ADDFRIEND:
//                AddFriend item = (AddFriend)o;
                GetAddFrienditem item = new GetAddFrienditem();
                item.setAvatar(mAvatar);
                item.setNickname(mUserName);
                item.setRemarkInfo(mFriendVerification.getText().toString());
                item.setUserid(mUserID);
                item.setStatus(1);
                LWDBManager.getInstance().addFriendItem(item);
                Utils.showLongToast(this, "发送好友申请成功!");
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
            case Request.Code.FRIEND_ADDFRIEND:
                type = new TypeToken<BaseResponse<AddFriend>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }
}
