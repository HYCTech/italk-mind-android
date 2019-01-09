package com.lw.italk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kyleduo.switchbutton.SwitchButton;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.gson.user.SetProfile;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.utils.Utils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flyn.Eyes;

/**
 * Created by 喜明 on 2018/8/14.
 */

public class SettingPrivacyActivity extends BaseActivity implements Response, CompoundButton.OnCheckedChangeListener {
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
    @BindView(R.id.add_friend_verification)
    RelativeLayout mAddFriendVerification;
    @BindView(R.id.black_list)
    TextView mBlackList;
    @BindView(R.id.allow_type)
    SwitchButton mAllowType;
    @BindView(R.id.recommend_friend)
    SwitchButton mRecommendFriend;

    @Override
    protected int setContentView() {
        return R.layout.activity_setting_privacy;
    }

    @Override
    protected void initView() {
        mLeftBarItem.setText("设置");
        mCenterBarItem.setText("隐私");
        mRightTitleBar.setVisibility(View.GONE);
        Eyes.translucentStatusBar(this, true);
    }

    @Override
    protected void initData() {
        if(LWDBManager.getInstance().getUserInfo().getAllowtype() == 1){
            mAllowType.setChecked(true);
        }else{
            mAllowType.setChecked(false);
        }
        mRecommendFriend.setChecked(LWDBManager.getInstance().getUserInfo().getRecommendfriend());

        mAllowType.setOnCheckedChangeListener(this);
        mRecommendFriend.setOnCheckedChangeListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.left_bar_item, R.id.black_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.black_list:
                Utils.start_Activity(SettingPrivacyActivity.this, BlackFriendListActivity.class);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.allow_type:
                long i;
                if(isChecked){
                    i = 1;
                }else{
                    i = 0;
                }
                Map<String, Object> requestList = new HashMap<String, Object>();
                requestList.put("userid", LWUserManager.getInstance().getUserInfo().getUid());
                Map<String, Object> requestItem = new HashMap<String, Object>();
                requestItem.put("allowtype", i);
                requestList.put("items", requestItem);
                HttpUtils.doPost(this, Request.Path.USER_SETPROFILE, requestList, true, Request.Code.change_allowtype, this);
                break;
            case R.id.recommend_friend:
                Map<String, Object> requestList1 = new HashMap<String, Object>();
                requestList1.put("userid", LWUserManager.getInstance().getUserInfo().getUid());
                Map<String, Object> requestItem1 = new HashMap<String, Object>();
                requestItem1.put("recommendfriend", isChecked);
                requestList1.put("items", requestItem1);
                HttpUtils.doPost(this, Request.Path.USER_SETPROFILE, requestList1, true, Request.Code.change_recommendfriend, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.change_allowtype:
                //保存修改信息到DB中
                long i;
                if(mAllowType.isChecked()){
                    i = 1;
                }else{
                    i = 0;
                }
                LWDBManager.getInstance().getUserInfo().setAllowtype(i);
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                break;
            case Request.Code.change_recommendfriend:
                LWDBManager.getInstance().getUserInfo().setRecommendfriend(mRecommendFriend.isChecked());
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
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
            case Request.Code.change_allowtype:
            case Request.Code.change_recommendfriend:
                type = new TypeToken<BaseResponse<SetProfile>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }
}
