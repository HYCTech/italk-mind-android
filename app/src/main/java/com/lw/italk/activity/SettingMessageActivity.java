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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flyn.Eyes;

/**
 * Created by 喜明 on 2018/8/13.
 */

public class SettingMessageActivity extends BaseActivity implements Response, CompoundButton.OnCheckedChangeListener {
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
    @BindView(R.id.message_switch)
    SwitchButton messageSwitch;
    @BindView(R.id.message)
    RelativeLayout mMessage;
    @BindView(R.id.voice_video_switch)
    SwitchButton voiceVideoSwitch;
    @BindView(R.id.voice_video_message)
    RelativeLayout mVoiceVideoMessage;
    @BindView(R.id.message_detail_switch)
    SwitchButton messageDetailSwitch;
    @BindView(R.id.setting_message_detail)
    RelativeLayout mSettingMessageDetail;
    @BindView(R.id.sound_switch)
    SwitchButton soundSwitch;
    @BindView(R.id.message_sound)
    RelativeLayout mMessageSound;
    @BindView(R.id.shock_switch)
    SwitchButton shockSwitch;
    @BindView(R.id.message_shock)
    RelativeLayout mMessageShock;

    @Override
    protected int setContentView() {
        return R.layout.activity_setting_message;
    }

    @Override
    protected void initView() {
        messageSwitch.setChecked(LWDBManager.getInstance().getUserInfo().getNotifymsg());
        voiceVideoSwitch.setChecked(LWDBManager.getInstance().getUserInfo().getNotifyvoice());
        messageDetailSwitch.setChecked(LWDBManager.getInstance().getUserInfo().getNotifydetail());
        soundSwitch.setChecked(LWDBManager.getInstance().getUserInfo().getMsgsound());
        shockSwitch.setChecked(LWDBManager.getInstance().getUserInfo().getMsgshock());

        messageSwitch.setOnCheckedChangeListener(this);
        voiceVideoSwitch.setOnCheckedChangeListener(this);
        messageDetailSwitch.setOnCheckedChangeListener(this);
        soundSwitch.setOnCheckedChangeListener(this);
        shockSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("设置");
        mCenterBarItem.setText("新消息通知");
        mRightTitleBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Eyes.translucentStatusBar(this, true);
    }

    @OnClick({R.id.left_bar_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.message_switch:
                changeUserInfo("notifymsg", isChecked, Request.Code.change_notifymsg);
                break;
            case R.id.voice_video_switch:
                changeUserInfo("notifyvoice", isChecked, Request.Code.change_notifyvoice);
                break;
            case R.id.message_detail_switch:
                changeUserInfo("notifydetail", isChecked, Request.Code.change_notifydetail);
                break;
            case R.id.sound_switch:
                changeUserInfo("msgsound", isChecked, Request.Code.change_msgsound);
                break;
            case R.id.shock_switch:
                changeUserInfo("msgshock", isChecked, Request.Code.change_msgshock);
                break;
            default:
                break;
        }
    }

    //  key: 修改个人资料的字段， value: 修改个人资料的值，  code：修改哪个个人资料表示
    private void changeUserInfo(String key, boolean value, int code)  {
        Map<String, Object> requestList = new HashMap<String, Object>();
        requestList.put("userid", LWUserManager.getInstance().getUserInfo().getUid());
        Map<String, Object> requestItem = new HashMap<String, Object>();
        requestItem.put(key, value);
        requestList.put("items", requestItem);
        HttpUtils.doPost(this, Request.Path.USER_SETPROFILE, requestList, true, code, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.change_notifymsg:
                //保存修改信息到DB中
                LWDBManager.getInstance().getUserInfo().setNotifymsg(messageSwitch.isChecked());
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                break;
            case Request.Code.change_notifyvoice:
                LWDBManager.getInstance().getUserInfo().setNotifyvoice(voiceVideoSwitch.isChecked());
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                break;
            case Request.Code.change_notifydetail:
                LWDBManager.getInstance().getUserInfo().setNotifydetail(messageDetailSwitch.isChecked());
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                break;
            case Request.Code.change_msgsound:
                LWDBManager.getInstance().getUserInfo().setMsgsound(soundSwitch.isChecked());
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                break;
            case Request.Code.change_msgshock:
                LWDBManager.getInstance().getUserInfo().setMsgshock(shockSwitch.isChecked());
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
            case Request.Code.change_notifymsg:
            case Request.Code.change_notifyvoice:
            case Request.Code.change_notifydetail:
            case Request.Code.change_msgsound:
            case Request.Code.change_msgshock:
                type = new TypeToken<BaseResponse<SetProfile>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }

}
