package com.lw.italk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.adapter.BlackFriendListAdapter;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.RequestTime;
import com.lw.italk.gson.friend.BlackFriendList;
import com.lw.italk.gson.friend.MoveFromBlackFriend;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.BlackFriendListRequest;
import com.lw.italk.http.model.RemoveBlackListRequest;
import com.lw.italk.utils.Constants;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 喜明 on 2018/8/19.
 */

public class BlackFriendListActivity extends BaseActivity implements Response {
    @BindView(R.id.left_bar_item)
    TextView mLeftBarItem;
    @BindView(R.id.center_bar_item)
    TextView mCenterBarItem;
    @BindView(R.id.right_title_bar)
    TextView mRightTitleBar;
    @BindView(R.id.list)
    ListView mListview;
    @BindView(R.id.txt_search)
    TextView mSearch;

    private BlackFriendListAdapter mAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_black_friend_list;
    }

    @Override
    protected void initView() {
        mLeftBarItem.setText("隐私");
        mCenterBarItem.setText("黑名单");
        mRightTitleBar.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        mAdapter = new BlackFriendListAdapter(BlackFriendListActivity.this,
                LWFriendManager.getInstance().getAllBlackList(LWUserManager.getInstance().getUserInfo().getUid()), new BlackFriendListAdapter.OnCheckBoxClickListener() {
            @Override
            public void onCheckBoxClilk(String remove_id) {
                removeBlackFriendList(remove_id);
            }
        });
        mListview.setAdapter(mAdapter);
        showProgressDialog("正在获取黑名单");
        new Thread(new Runnable() {
            @Override
            public void run() {
                GloableParams.blackLists = LWFriendManager.getInstance().getAllBlackList(LWUserManager.getInstance().getUserInfo().getUid());
                if (GloableParams.blackLists != null && GloableParams.blackLists.size() > 0) {
                    myHandler.sendEmptyMessage(1);
                }
            }
        }).start();
        long time = LWFriendManager.getInstance().getBlackListTime(LWUserManager.getInstance().getUserInfo().getUid());
        getBlackFriendList(time, 0, LWUserManager.getInstance().getUserInfo().getUid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setParam(GloableParams.blackLists);
    }

    @OnClick({R.id.left_bar_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            default:
                break;
        }
    }

    private void getBlackFriendList(long nowTime, int page, String uid) {
        BlackFriendListRequest request = new BlackFriendListRequest();
        request.setTimestamp(nowTime);
        request.setPage(page);
        request.setFrom_account(uid);
        request.setPagecount(1000);
        HttpUtils.doPost(this, Request.Path.FRIEND_BLACKLIST, request, true, Request.Code.FRIEND_BLACKLIST, this);
    }

    private void removeBlackFriendList(String remove_id){
        RemoveBlackListRequest request = new RemoveBlackListRequest();
        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
        request.setTo_account(remove_id);
        HttpUtils.doPost(this, Request.Path.FRIEND_MOVEFROMBLACKLIST, request, true, Request.Code.FRIEND_MOVEFROMBLACKLIST, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.FRIEND_BLACKLIST:
                BlackFriendList data = (BlackFriendList) o;
                if (data == null || data.getTotalnum() == 0) {
                    myHandler.sendEmptyMessage(0);
                    return;
                }
                LWFriendManager.getInstance().addBlackList(data.getItems());
                GloableParams.blackLists = LWFriendManager.getInstance().getAllBlackList(LWUserManager.getInstance().getUserInfo().getUid());
                RequestTime requestTime = LWFriendManager.getInstance().getRequestTime(LWUserManager.getInstance().getUserInfo().getUid());
                if (requestTime == null) {
                    requestTime = new RequestTime();
                    requestTime.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
                }
                requestTime.setBlacklisttime(data.getTimestampnow());
                LWFriendManager.getInstance().updateRequest(requestTime);
                myHandler.sendEmptyMessage(2);
                break;
            case Request.Code.FRIEND_MOVEFROMBLACKLIST:
                MoveFromBlackFriend items = (MoveFromBlackFriend)o;
                LWFriendManager.getInstance().deleteBlackItem(items.getDel_blacklist_items()[0]);
                GloableParams.blackLists = LWFriendManager.getInstance().getAllBlackList(LWUserManager.getInstance().getUserInfo().getUid());
                mAdapter.setParam(GloableParams.blackLists);
                break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        myHandler.sendEmptyMessage(0);
    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.FRIEND_BLACKLIST:
                type = new TypeToken<BaseResponse<BlackFriendList>>() {
                }.getType();
                break;
            case Request.Code.FRIEND_MOVEFROMBLACKLIST:
                type = new TypeToken<BaseResponse<MoveFromBlackFriend>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }

    Handler myHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    dismissProgressDialog();
                    break;
                case 1:
//                    ((BaseActivity) mContext).dismissProgressDialog();
                    mAdapter.setParam(GloableParams.blackLists);
                    break;
                case 2:
                    dismissProgressDialog();
                    mAdapter.setParam(GloableParams.blackLists);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @OnClick(R.id.txt_search)
    public void onClick() {
        Intent intent = new Intent(BlackFriendListActivity.this, SearchViewActivity.class);
        intent.putExtra(Constants.SEARCH_TYPE, Constants.SEARCH_BLACK_LIST);
        startActivity(intent);
    }
}
