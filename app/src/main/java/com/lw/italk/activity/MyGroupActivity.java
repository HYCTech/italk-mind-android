package com.lw.italk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.RequestTime;
import com.lw.italk.gson.group.GroupItem;
import com.lw.italk.gson.group.GroupList;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.GetGroupListRequest;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.Utils;
import com.lw.italk.adapter.MyGroupAdpter;
import com.lw.italk.framework.base.BaseActivity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 喜明 on 2018/8/18.
 */

public class MyGroupActivity extends BaseActivity implements Response {
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
    @BindView(R.id.txt_search)
    TextView mSearch;
    @BindView(R.id.listview)
    ListView mListview;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_group;
    }

    @Override
    protected void initView() {
        mLeftBarItem.setText("消息");
        mCenterBarItem.setText("群聊");
        mRightTitleBar.setText("发起群聊");
    }

    @Override
    protected void initData() {
        showProgressDialog("正在获取群列表");
        new Thread(new Runnable() {
            @Override
            public void run() {
                GloableParams.ListGroupInfos = LWFriendManager.getInstance().getAllGroupList(LWUserManager.getInstance().getUserInfo().getUid());
                if (GloableParams.ListGroupInfos != null && GloableParams.ListGroupInfos.size() > 0) {
                    myHandler.sendEmptyMessage(1);
                }
            }
        }).start();
        long time = LWFriendManager.getInstance().getNowTime(LWUserManager.getInstance().getUserInfo().getUid());
        getGroupList(time, 0, LWUserManager.getInstance().getUserInfo().getUid());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.left_bar_item, R.id.txt_search, R.id.right_title_bar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.txt_search:
                Intent intent = new Intent(MyGroupActivity.this,SearchViewActivity.class );
                intent.putExtra(Constants.SEARCH_TYPE, Constants.SEARCH_GROUP);
                startActivity(intent);
                break;
            case R.id.right_title_bar:
                Utils.start_Activity(MyGroupActivity.this, AddGroupChatActivity.class);
                break;
            default:
                break;
        }
    }

    private void getGroupList(long nowTime, int page, String uid) {
//        GetGroupListRequest request = new GetGroupListRequest();
//        request.setTimestamp(0);
//        request.setPage(0);
//        request.setFrom_account(LWUserManager.getInstance().getUserInfo().getUid());
//        request.setPagecount(10);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        HttpUtils.doPostFormMap(this, Request.Path.GROUP_GROUPLIST, map, false, Request.Code.GROUP_GROUPLIST, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.GROUP_GROUPLIST:
                GroupList data = (GroupList) o;
                if (data.getInfo() == null || data.getInfo().size() == 0) {
                    myHandler.sendEmptyMessage(0);
                    return;
                }
                LWFriendManager.getInstance().clearAllGroup();
                LWFriendManager.getInstance().addGroupList(data.getInfo());
                GloableParams.ListGroupInfos = LWFriendManager.getInstance().getAllGroupList(LWUserManager.getInstance().getUserInfo().getUid());
//                RequestTime requestTime = LWFriendManager.getInstance().getRequestTime(LWFriendManager.USERID);
//                if (requestTime == null) {
//                    requestTime = new RequestTime();
//                    requestTime.setUserid(LWFriendManager.USERID);
//                }
//                requestTime.setGrouplisttime(data.getTimestampnow());
//                LWFriendManager.getInstance().updateRequest(requestTime);
                myHandler.sendEmptyMessage(2);
                List<GroupItem> items = ((GroupList) o).getInfo();
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
            case Request.Code.GROUP_GROUPLIST:
                type = new TypeToken<BaseResponse<GroupList>>() {
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
                    mListview.setAdapter(new MyGroupAdpter(MyGroupActivity.this, GloableParams.ListGroupInfos));
                    break;
                case 1:
                    if (GloableParams.ListGroupInfos != null) {
                        mListview.setAdapter(new MyGroupAdpter(MyGroupActivity.this,
                                GloableParams.ListGroupInfos));
                    }
                    break;
                case 2:
                    dismissProgressDialog();
                    if (GloableParams.ListGroupInfos != null) {
                        mListview.setAdapter(new MyGroupAdpter(MyGroupActivity.this,
                                GloableParams.ListGroupInfos));
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
