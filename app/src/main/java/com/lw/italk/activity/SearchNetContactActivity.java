package com.lw.italk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.gson.user.QueryUser;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.QueryUserRequest;
import com.lw.italk.utils.Constants;

import java.lang.reflect.Type;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flyn.Eyes;

/**
 * Created by 喜明 on 2018/8/19.
 */

public class SearchNetContactActivity extends BaseActivity implements Response {
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
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_search)
    TextView mSearch;
	@BindView(R.id.ll_null_result)
    LinearLayout mNullResult;

    @Override
    protected int setContentView() {
        return R.layout.activity_search_net_contact;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("新的朋友");
        mCenterBarItem.setText("搜索");
        mRightTitleBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Eyes.translucentStatusBar(this, true);
    }

    @OnClick({R.id.left_bar_item, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.tv_search:
                getQueryFriendInfo();
                break;
            default:
                break;
        }
    }

    private void getQueryFriendInfo() {
        if(etSearch.getText().length() != 11){
            Toast.makeText(this, "输入的号码不正确!", Toast.LENGTH_SHORT).show();
            return;
        }
//        QueryUserRequest request = new QueryUserRequest();
//        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
//        request.setTo_account(etSearch.getText().toString());
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("account", etSearch.getText().toString());
        HttpUtils.doPostFormMap(this, Request.Path.USER_QUERYUSER, map, true, Request.Code.USER_QUERYUSER, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.USER_QUERYUSER:
                Contact items = ((Contact) o);
                if(items == null){//用户是否存在
                    mNullResult.setVisibility(View.VISIBLE);
                }if(items.getUid().equals(LWUserManager.getInstance().getUserInfo().getUid())){
                Toast.makeText(this, "你不能添加自己到通讯录", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = null;
                    if(LWDBManager.getInstance().queryFriendItem(items.getUid()) == null){
                        intent = new Intent(SearchNetContactActivity.this, AddNetFriendActivity.class);
                    }else{
                        intent = new Intent(SearchNetContactActivity.this, SendChatActivity.class);
                    }
                    intent.putExtra(Constants.NAME, items.getUsername());
                    intent.putExtra(Constants.HEADURL, items.getAvatar());
                    intent.putExtra(Constants.PHONE, etSearch.getText().toString());
                    intent.putExtra(Constants.User_ID, items.getUid());
                    startActivity(intent);
                    mNullResult.setVisibility(View.GONE);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        mNullResult.setVisibility(View.VISIBLE);
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
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
