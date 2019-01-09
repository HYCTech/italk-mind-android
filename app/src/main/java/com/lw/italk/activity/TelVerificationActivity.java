package com.lw.italk.activity;

/**
 * Created by 喜明 on 2018/8/12.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.App;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.UserInfo;
import com.lw.italk.gson.friend.FriendList;
import com.lw.italk.gson.user.Login;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.UserInfoRequest;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TelVerificationActivity extends BaseActivity implements Response{
    @BindView(R.id.logo)
    TextView logo;
    @BindView(R.id.phone_area)
    TextView phoneArea;
    @BindView(R.id.phone_area_content)
    TextView phoneAreaContent;
    @BindView(R.id.phone_tx)
    TextView phoneTx;
    @BindView(R.id.phone_number)
    EditText mPhoneNumber;
    @BindView(R.id.send_verification_code)
    TextView mSendVerificationCode;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    @BindView(R.id.verification_code)
    EditText mVerificationCode;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.init)
    RelativeLayout init;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.left_bar_item)
    TextView mLeftBar;
    private CountdownThread thread;
    private String mToken;
    private final static int LOGIN_MODE_VERIFICATION_CODE = 0;
    private final static int LOGIN_MODE_PSWD = 1;
    private int mLoginMode = LOGIN_MODE_VERIFICATION_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
    @Override
    protected int setContentView() {
        return R.layout.activity_verification_tel;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    private void countdown(int paramInt) {
        thread = new CountdownThread(paramInt * 1000L, 1000L, this.mSendVerificationCode);
        thread.start();
    }

    protected void onDestroy() {
        this.mSendVerificationCode = null;
        if (this.thread != null) {
            this.thread.cancel();
        }
        super.onDestroy();
    }

    @OnClick({R.id.phone_number, R.id.send_verification_code, R.id.verification_code, R.id.login, R.id.left_bar_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_verification_code:
                if(!TextUtils.isEmpty(mPhoneNumber.getText()) && mPhoneNumber.getText().length() == 11){
                    countdown(60);//倒计时
                    mSendVerificationCode.setEnabled(false);//按钮不可点击

//                    SmsEntityRequest request = new SmsEntityRequest();
//                    request.setUserid(mPhoneNumber.getText().toString());
//                    HttpUtils.doPost(this, Request.Path.USER_LOGINSMS, request, true, Request.Code.USER_LOGINSMS, this);
                }else{
                    Toast.makeText(App.getInstance(), "请输入正确的手机号码!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.login:
//                if(!TextUtils.isEmpty(mSendVerificationCode.getText()) && mPhoneNumber.getText().length() == 11 && !TextUtils.isEmpty(mPhoneNumber.getText())){
//                   //加载框
//
//                    LoginRequest request = new LoginRequest();
//                    request.setUserid(mPhoneNumber.getText().toString());
//                    request.setVerificationcode(mVerificationCode.getText().toString());
//                    HttpUtils.doPost(this, Request.Path.USER_LOGIN, request, false, Request.Code.USER_LOGIN, this);
//                }else{
//                    Toast.makeText(App.getInstance(), "手机号码或验证码不正确!", Toast.LENGTH_SHORT).show();
//                }
                break;
            default:
                break;
        }
    }


    @Override
    public void next(Object o, int requestCode) {

        switch (requestCode) {
            case Request.Code.USER_LOGIN:
                Login login = ((Login) o);
                if (login != null) {
                    mToken = login.getToken();
                }
                //加载框取消
                UserInfoRequest request = new UserInfoRequest();
                request.setUid(mPhoneNumber.getText().toString());
                //HttpUtils.doPost(this, Request.Path.USER_ALLPROFILE, request, true, Request.Code.USER_ALLPROFILE, this);
                break;
            case Request.Code.USER_LOGINSMS:
                Toast.makeText(App.getInstance(), "短信发送成功!", Toast.LENGTH_SHORT).show();
                break;
            case Request.Code.USER_ALLPROFILE:
                UserInfo items = ((UserInfo) o);
                items.setIscurrent(true);
                items.setToken(mToken);
                //GloableParams.CurUser = items;
                LWUserManager.getInstance().setUserInfo(items);

                if(!TextUtils.isEmpty(((UserInfo) o).getUsername())){
                    Intent intent = new Intent(TelVerificationActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(TelVerificationActivity.this, PerfectInformationActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        switch ((int)t.getErrorCode()){
            case 60001:
                Toast.makeText(this, "url不正确，无法处理", Toast.LENGTH_SHORT).show();
                break;
            case 60002:
                Toast.makeText(this, "http 请求 json 解析错误，请检查 json 格式", Toast.LENGTH_SHORT).show();
                break;
            case 60003:
                Toast.makeText(this, "请求的accesstoken无效", Toast.LENGTH_SHORT).show();
                break;
            case 60004:
                Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                break;
            case 60005:
                Toast.makeText(this, "服务请求超时或 http 请求格式错误，请检查并重试", Toast.LENGTH_SHORT).show();
                break;
            case 60006:
                Toast.makeText(this, "请求频率超限，请降低请求频率", Toast.LENGTH_SHORT).show();
                break;
            case 60007:
                Toast.makeText(this, "请求参数为空", Toast.LENGTH_SHORT).show();
                break;
            case 60008:
                Toast.makeText(this, "请求参数类型不正确", Toast.LENGTH_SHORT).show();
                break;
            case 60009:
                Toast.makeText(this, "数据库操作失败", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.USER_LOGIN:
                type = new TypeToken<BaseResponse<Login>>() {
                }.getType();
                break;
            case Request.Code.USER_LOGINSMS:
                type = new TypeToken<BaseResponse<FriendList>>() {
                }.getType();
                break;
            case Request.Code.USER_ALLPROFILE:
                type = new TypeToken<BaseResponse<UserInfo>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                System.exit(0);
        }
        return false;
    }

    private class CountdownThread extends CountDownTimer
    {
        private TextView mCountDownView;

        public CountdownThread(long paramLong1, long paramLong2, TextView paramTextView)
        {
            super(paramLong1, paramLong2);
            this.mCountDownView = paramTextView;
        }

        public void onFinish()
        {
            this.mCountDownView.setEnabled(true);
            this.mCountDownView.setText("验证码");
        }

        public void onTick(long paramLong)
        {
            this.mCountDownView.setText(paramLong / 1000L + "");
        }
    }
}

