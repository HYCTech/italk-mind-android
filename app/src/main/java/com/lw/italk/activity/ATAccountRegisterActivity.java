package com.lw.italk.activity;

/**
 * Created by 喜明 on 2018/8/12.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.italkmind.client.util.ClientTools;
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
import com.lw.italk.http.FileDownloadCallback;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.ThirdLibs;
import com.lw.italk.http.model.UserInfoRequest;
import com.lw.italk.utils.Constants;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 注册和忘记密码
 */
public class ATAccountRegisterActivity extends BaseActivity implements Response {
    @BindView(R.id.logo)
    TextView logo;
    @BindView(R.id.phone_tx)
    TextView phoneTx;
    @BindView(R.id.phone_number)
    EditText mPhoneNumber;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    @BindView(R.id.verification_code)
    EditText mVerificationCode;
    @BindView(R.id.init)
    RelativeLayout init;
    @BindView(R.id.iv_verify_code)
    ImageView verifyCodeImage;

    @BindView(R.id.image_verify_code)
    EditText imageVerifyET;

    @BindView(R.id.et_password_code)
    EditText passwordET;

    @BindView(R.id.tv_register)
    TextView titleTextView;

    @BindView(R.id.btn_register)
    Button registerBtn;

    private String mToken;
    private final static int LOGIN_MODE_VERIFICATION_CODE = 0;
    private final static int LOGIN_MODE_PSWD = 1;
    private int mLoginMode = LOGIN_MODE_VERIFICATION_CODE;
    private CountdownThread thread;

    private int type = 1;//1:为注册，2为重置密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshImageCode();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra(Constants.REGISTER_TYPE, 1);
        if (type == 2) {
            titleTextView.setText("重置密码");
            registerBtn.setText("重置密码");
        }
    }

    @Override
    protected void initData() {

    }

    /**
     * @方法名: refreshImageCode
     * @参数：
     * @返回值：
     * @描述: 刷新图形验证码
     * @创建日期 2018/11/14 11:41
     */
    private void refreshImageCode() {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        ThirdLibs.getInstanceGet().downLoadFile(uuid.toString(), Request.Path.USER_IMAGE_VERIFY, false, new FileDownloadCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response, String localPath) {
                try {
                    System.out.println(call);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //verifyCodeImage.setImageBitmap( CodeUtils.getInstance().createBitmap());
                            System.out.println("localPath:" + localPath);
                            verifyCodeImage.setImageURI(Uri.parse(localPath));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLoading(long total, long progress) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ATAccountRegisterActivity.this, "图片验证码获取失败请点击验证码刷新", Toast.LENGTH_LONG);
            }
        });
    }

    @OnClick({R.id.phone_number, R.id.tv_verification_code, R.id.btn_register, R.id.iv_verify_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_verify_code:
                //刷新图形验证码
                refreshImageCode();
                break;
            case R.id.tv_verification_code:
                //点击验证码
                String imgV = imageVerifyET.getText().toString();
                String phone = mPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(imgV)) {
                    Toast.makeText(App.getInstance(), "请输入图片验证码!", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ imgV:" + imgV + "--->phone:" + phone);

                    countdown(60);//倒计时
                    tvVerificationCode.setEnabled(false);//按钮不可点击
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("account", phone);
                    map.put("captcha", imgV);
                    map.put("type", String.valueOf(type));
                    HttpUtils.doPostFormMap(this, Request.Path.USER_LOGINSMS, map, false, Request.Code.USER_LOGINSMS, this);
                } else {
                    Toast.makeText(App.getInstance(), "请输入正确的手机号码!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_register:
                String pwd = passwordET.getText().toString();//密码
                String code = "123456";//mVerificationCode.getText().toString();//验证码
                String account = mPhoneNumber.getText().toString();//手机号

                String imgV11 = imageVerifyET.getText().toString();
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put("account", account);
                map1.put("captcha", imgV11);
                map1.put("type", String.valueOf(type));
                HttpUtils.doPostFormMap(this, Request.Path.USER_LOGINSMS, map1, false, Request.Code.USER_LOGINSMS, this);

                if (valContent(account, code, pwd)) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("account", account);
                    map.put("checkCode", code);
                    map.put("password", ClientTools.hashMd5(pwd));
                    map.put("type", String.valueOf(type));
                    //请求接口更具type 类型进行注册或重置密码操作
                    HttpUtils.doPostFormMap(this, Request.Path.USER_SETPROFILE, map, true, Request.Code.USER_SETPROFILE, this);
                }

                break;
            default:
                break;
        }
    }

    /**
     * @方法名: valContent
     * @参数：
     * @返回值：
     * @描述: 校验输入的内容是否符合规则
     * @作者： CHENHUI
     * @创建日期 2018/11/14 11:31
     */
    private static boolean valContent(String phone, String code, String pwd) {
        boolean bln = false;
        if (phone.length() < 11 || TextUtils.isEmpty(phone)) {
            Toast.makeText(App.getInstance(), "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(code)) {
            Toast.makeText(App.getInstance(), "请输入手机验证码!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(App.getInstance(), "请输入密码!", Toast.LENGTH_SHORT).show();
        } else if (pwd.length() < 6) {
            Toast.makeText(App.getInstance(), "密码不少于6位数!", Toast.LENGTH_SHORT).show();
        } else if (valPass(pwd) == false) {
            Toast.makeText(App.getInstance(), "密码必须包含大小写字母和数字，8位数以上!", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(code) && phone.length() == 11 && !TextUtils.isEmpty(phone)) {
            bln = true;
        } else {
            Toast.makeText(App.getInstance(), "手机号码或验证码不正确!", Toast.LENGTH_SHORT).show();
        }
        return bln;
    }

    /**
     * 验证输入的密码是否包含大小写和数字
     *
     * @param value
     * @return
     */
    public static boolean valPass(Object value) {
        if (null == value) {
            return false;
        } else {
            String passWord = (String) value;
            if (passWord.matches("\\w+")) {
                String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";
                return (passWord.length() >= 8 && passWord.length() <= 16 && passWord.matches(regex)) ? true : false;
            } else {
                return false;
            }
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
                HttpUtils.doPost(this, Request.Path.USER_ALLPROFILE, request, true, Request.Code.USER_ALLPROFILE, this);
                break;
            case Request.Code.USER_LOGINSMS:
                //Toast.makeText(App.getInstance(), "短信发送成功!", Toast.LENGTH_SHORT).show();
                break;
            case Request.Code.USER_ALLPROFILE:
                UserInfo items = ((UserInfo) o);
                items.setIscurrent(true);
                items.setToken(mToken);
                LWUserManager.getInstance().setUserInfo(items);

                if (!TextUtils.isEmpty(((UserInfo) o).getUsername())) {
                    Intent intent = new Intent(ATAccountRegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ATAccountRegisterActivity.this, PerfectInformationActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case Request.Code.USER_IMAGE_VERIFY:

                break;
            case Request.Code.USER_SETPROFILE:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        System.out.println("=====getData===" +t.getData() + "==getMessage=" + t.getMessage() + "==getErrorCode==" + t.getErrorCode());
        switch (requestCode) {
            case Request.Code.USER_IMAGE_VERIFY:
                Toast.makeText(this, "获取图片验证码失败，请重试", Toast.LENGTH_SHORT).show();
                break;
            case Request.Code.USER_SETPROFILE:

                refreshImageCode();

                if (type == 1) {
                    Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                } else if (type == 2) {
                    Toast.makeText(this, "重置密码失败，请重试", Toast.LENGTH_SHORT).show();
                }

                if (this.thread != null) {
                    this.thread.cancel();
                }
                this.thread.onFinish();
                break;
            case Request.Code.USER_LOGINSMS:
                refreshImageCode();
                ;
                if (1500 == t.getErrorCode()) {
                    Toast.makeText(this, "获取验证码不成功，请重试", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (this.thread != null) {
                    this.thread.cancel();
                }
                this.thread.onFinish();

                break;

            default:
                Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();

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
            case Request.Code.USER_SETPROFILE:
                type = new TypeToken<BaseResponse<Object>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }


    private void countdown(int paramInt) {
        thread = new CountdownThread(paramInt * 1000L, 1000L, this.tvVerificationCode);
        thread.start();
    }

    protected void onDestroy() {
        this.tvVerificationCode = null;
        if (this.thread != null) {
            this.thread.cancel();
        }
        super.onDestroy();
    }

    private class CountdownThread extends CountDownTimer {
        private TextView mCountDownView;

        public CountdownThread(long paramLong1, long paramLong2, TextView paramTextView) {
            super(paramLong1, paramLong2);
            this.mCountDownView = paramTextView;
        }

        public void onFinish() {
            this.mCountDownView.setEnabled(true);
            this.mCountDownView.setText("获取验证码");
        }

        public void onTick(long paramLong) {
            this.mCountDownView.setText(paramLong / 1000L + "秒");
        }
    }
}

