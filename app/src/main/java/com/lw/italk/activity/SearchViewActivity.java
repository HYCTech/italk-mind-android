package com.lw.italk.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.adapter.AddGroupChatAdapter;
import com.lw.italk.adapter.AddLocalContactAdapter;
import com.lw.italk.adapter.BlackFriendListAdapter;
import com.lw.italk.adapter.ContactAdapter;
import com.lw.italk.adapter.MyGroupAdpter;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.BlackList;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.PingYinUtil;
import com.lw.italk.gson.friend.MoveFromBlackFriend;
import com.lw.italk.gson.group.GroupItem;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.RemoveBlackListRequest;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.FileUtils;
import com.lw.italk.utils.ItalkLog;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lxm on 2018/8/30.
 */
public class SearchViewActivity extends BaseActivity {

    private static final int EVENT_SEARCH_CONTACT = 1;
    private static final int EVENT_SEARCH_GROUP = 2;
    private static final int EVENT_SEARCH_BLACK_LIST = 3;
    private static final int EVENT_SEARCH_LOCAL_CONTACT = 4;
    private static final int EVENT_SEARCH_ADD_GROUP_CHAT = 5;

    @BindView(R.id.search_back)
    ImageView mSearchBack;
    @BindView(R.id.search_text)
    EditText mSearchText;
    @BindView(R.id.lvContact)
    ListView mListView;

    private int mSearchType;
    private BaseAdapter mAdapter;
    private UIHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSearchType = getIntent().getIntExtra(Constants.SEARCH_TYPE, 0);
        super.onCreate(savedInstanceState);
        mHandler = new UIHandler(this);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mSearchText.setFocusable(true);
//        mSearchText.setFocusableInTouchMode(true);
//        mSearchText.requestFocus();
//        InputMethodManager inputManager =
//                (InputMethodManager)mSearchText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.showSoftInput(mSearchText, 0);
//    }

    @Override
    protected int setContentView() {
        return R.layout.activity_search_view;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        switch (mSearchType) {
            case Constants.SEARCH_CONTACT:
            case Constants.SEARCH_ADD_GROUP_CHAT:
                doContactSearch();
                break;
            case Constants.SEARCH_GROUP:
                doGroupSearch();
                break;
            case Constants.SEARCH_BLACK_LIST:
                doBlackListSearch();
                break;
            case Constants.SEARCH_LOCAL_CONTACT:
                doLocalContactList();
                break;
            default:
                break;
        }
    }


    private void doContactSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSearchText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        List<Contact> mDate = new ArrayList<Contact>();
                        for (int i = 0; i < GloableParams.contactInfos.size(); i++) {
                            if(FileUtils.isSubString(PingYinUtil.getPingYin(GloableParams.contactInfos.get(i).getUsername()),PingYinUtil.getPingYin(s + ""))
                                    || FileUtils.isSubString(PingYinUtil.getPingYin(GloableParams.contactInfos.get(i).getRemark()),PingYinUtil.getPingYin(s + "")) ){
                                mDate.add(GloableParams.contactInfos.get(i));
                            }
                            ItalkLog.e(PingYinUtil.getPingYin(GloableParams.contactInfos.get(i).getUsername()) + "-----------" + PingYinUtil.getPingYin(s + ""));
                        }
                        Message msg = mHandler.obtainMessage();

                        if(mSearchType == Constants.SEARCH_CONTACT){
                            msg.what = EVENT_SEARCH_CONTACT;
                        }else if(mSearchType == Constants.SEARCH_ADD_GROUP_CHAT){
                            msg.what = EVENT_SEARCH_ADD_GROUP_CHAT;
                        }

                        msg.obj = mDate;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }).start();
    }

    private void doGroupSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSearchText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        List<GroupItem> mDate = new ArrayList<GroupItem>();
                        for (int i = 0; i < GloableParams.ListGroupInfos.size(); i++) {
                            if(FileUtils.isSubString(PingYinUtil.getPingYin(GloableParams.ListGroupInfos.get(i).getName()) , PingYinUtil.getPingYin(s + ""))){
                                mDate.add(GloableParams.ListGroupInfos.get(i));
                            }
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = EVENT_SEARCH_GROUP;
                        msg.obj = mDate;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }).start();
    }

    private void doBlackListSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSearchText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        List<BlackList> mDate = new ArrayList<BlackList>();
                        for (int i = 0; i < GloableParams.blackLists.size(); i++) {
                            if(FileUtils.isSubString(PingYinUtil.getPingYin(GloableParams.blackLists.get(i).getNickname()) , PingYinUtil.getPingYin(s + ""))){
                                mDate.add(GloableParams.blackLists.get(i));
                            }
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = EVENT_SEARCH_BLACK_LIST;
                        msg.obj = mDate;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }).start();
    }

    private void doLocalContactList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSearchText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        List<Contact> mDate = new ArrayList<Contact>();
                        for (int i = 0; i < GloableParams.LocalContactInfos.size(); i++) {
                            if(FileUtils.isSubString(PingYinUtil.getPingYin(GloableParams.LocalContactInfos.get(i).getUsername()) , PingYinUtil.getPingYin(s + ""))){
                                mDate.add(GloableParams.LocalContactInfos.get(i));
                            }
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = EVENT_SEARCH_LOCAL_CONTACT;
                        msg.obj = mDate;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }).start();
    }

    @OnClick({R.id.search_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.search_text)
    public void onClick() {
    }

    private static class UIHandler extends Handler {
        private WeakReference<SearchViewActivity> ref;

        public UIHandler(SearchViewActivity ref) {
            this.ref = new WeakReference<SearchViewActivity>(ref);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (ref.get() == null) {
                return;
            }

            final SearchViewActivity activity = ref.get();

            switch (msg.what) {
                case EVENT_SEARCH_CONTACT:
                    if (msg.obj == null) {
                        return;
                    }

                    activity.mAdapter = new ContactAdapter(activity, (List<Contact>) msg.obj);
                    break;
                case EVENT_SEARCH_GROUP:
                    if (msg.obj == null) {
                        return;
                    }

                    activity.mAdapter = new MyGroupAdpter(activity, (List<GroupItem>) msg.obj);
                    break;
                case EVENT_SEARCH_BLACK_LIST:
                    if (msg.obj == null) {
                        return;
                    }

                    activity.mAdapter = new BlackFriendListAdapter(activity,
                            LWFriendManager.getInstance().getAllBlackList(LWUserManager.getInstance().getUserInfo().getUid()), new BlackFriendListAdapter.OnCheckBoxClickListener() {
                        @Override
                        public void onCheckBoxClilk(String remove_id) {
                            RemoveBlackListRequest request = new RemoveBlackListRequest();
                            request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
                            request.setTo_account(remove_id);
                            HttpUtils.doPost(activity, Request.Path.FRIEND_MOVEFROMBLACKLIST, request, true, Request.Code.FRIEND_MOVEFROMBLACKLIST, activity.mResponse);
                        }
                    });
                    break;
                case EVENT_SEARCH_LOCAL_CONTACT:
                    if (msg.obj == null) {
                        return;
                    }

                    activity.mAdapter = new AddLocalContactAdapter(activity, (List<Contact>) msg.obj);
                    break;
                case EVENT_SEARCH_ADD_GROUP_CHAT:
                    if (msg.obj == null) {
                        return;
                    }

                    activity.mAdapter = new AddGroupChatAdapter(activity, (List<Contact>) msg.obj);
                    break;
                default:
                    break;
            }

            activity.mListView.setAdapter(activity.mAdapter);
        }
    }

    private Response mResponse = new Response(){
        @Override
        public void next(Object o, int requestCode) {
            switch (requestCode) {
                case Request.Code.FRIEND_MOVEFROMBLACKLIST:
                    MoveFromBlackFriend items = (MoveFromBlackFriend)o;
                    LWFriendManager.getInstance().deleteBlackItem(items.getDel_blacklist_items()[0]);
                    GloableParams.blackLists = LWFriendManager.getInstance().getAllBlackList(LWUserManager.getInstance().getUserInfo().getUid());
                    mSearchText.setText("");
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
                case Request.Code.FRIEND_MOVEFROMBLACKLIST:
                    type = new TypeToken<BaseResponse<MoveFromBlackFriend>>() {
                    }.getType();
                    break;
                default:
                    break;
            }
            return type;
        }
    };

}
