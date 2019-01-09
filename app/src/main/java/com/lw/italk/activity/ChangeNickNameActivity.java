package com.lw.italk.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.App;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWConversationManager;
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
 * Created by 喜明 on 2018/8/12.
 */

public class ChangeNickNameActivity extends BaseActivity implements Response{

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
    @BindView(R.id.edit_nick_name)
    EditText editNickName;

    @Override
    protected int setContentView() {
        return R.layout.activity_change_nickname;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("取消");
        mCenterBarItem.setText("更改昵称");
        mRightTitleBar.setText("完成");
        editNickName.setText(LWDBManager.getInstance().getUserInfo().getUsername());
        editNickName.setFocusable(true);
        editNickName.setFocusableInTouchMode(true);
        editNickName.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Eyes.translucentStatusBar(this, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                if(!TextUtils.isEmpty(editNickName.getText().toString())){
                    changeUserInfo();
                }else{
                    Toast.makeText(this, "昵称不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void changeUserInfo()  {

        Map<String, String> requestItem = new HashMap<String, String>();
        requestItem.put("infoValue", editNickName.getText().toString());
        requestItem.put("infoKey", String.valueOf(1));//用户名
        requestItem.put("tokenId", LWUserManager.getInstance().getToken());//性别
        HttpUtils.doPostFormMap(this, Request.Path.USER_EDITINFO, requestItem, false, Request.Code.USER_EDITINFO, this);

    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.USER_EDITINFO:
                //保存修改信息到DB中
                String nickName=editNickName.getText().toString();
                LWDBManager.getInstance().getUserInfo().setUsername(nickName);
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                LWUserManager.getInstance().getUserInfo().setUsername(nickName);
                LWConversationManager.getInstance().updateContactUserName(LWUserManager.getInstance().getUserInfo().getUid(),nickName);
                Toast.makeText(this, "修改昵称成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        Toast.makeText(App.getInstance(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.USER_EDITINFO:
                type = new TypeToken<BaseResponse<SetProfile>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }
}
