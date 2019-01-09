package com.lw.italk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.common.ConversationUpdateListen;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.greendao.model.RequestTime;
import com.lw.italk.gson.group.GroupItem;
import com.lw.italk.gson.msg.MsgInfoItemGroup;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.gson.msg.MsgList;
import com.lw.italk.gson.msg.MsgStatusList;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.MsgIndex;
import com.lw.italk.http.model.MsgListRequest;
import com.lw.italk.http.model.MsgRequestParam;
import com.lw.italk.utils.Constants;
import com.lw.italk.activity.ChatActivity;
import com.lw.italk.activity.MainActivity;
import com.lw.italk.adapter.NewMsgAdpter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

//消息
public class Fragment_Msg extends Fragment implements OnClickListener,
        OnItemClickListener, Response{
    private Activity mActivity;
    private View layout, layout_head;;
    public RelativeLayout errorItem;
    public TextView errorText;
    private ListView lvContact;
    private NewMsgAdpter adpter;
    //	private List<Conversation> conversationList = new ArrayList<Conversation>();
    private MainActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (layout == null) {
            mActivity = this.getActivity();
            parentActivity = (MainActivity) getActivity();
            layout = mActivity.getLayoutInflater().inflate(R.layout.fragment_msg,
                    null);
            lvContact = (ListView) layout.findViewById(R.id.listview);
            errorItem = (RelativeLayout) layout
                    .findViewById(R.id.rl_error_item);
            errorText = (TextView) errorItem
                    .findViewById(R.id.tv_connect_errormsg);
            setOnListener();
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        layout.findViewById(R.id.txt_nochat).setVisibility(View.GONE);
        adpter = new NewMsgAdpter(getActivity(), GloableParams.sConversations);
        lvContact.setAdapter(adpter);
        LWJNIManager.getInstance().registerConversationUpdateListen(new ConversationUpdateListen() {
            @Override
            public void updateMsg() {
                Log.e("123qwe", "ConversationUpdateListen updateMsg");
                refresh();
            }
        });
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LWJNIManager.getInstance().unregisterConversationUpdateListen();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    /**
     * 刷新页面
     */
    public void refresh() {
//		conversationList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GloableParams.sConversations = LWConversationManager.getInstance().getAllConversations(LWUserManager.getInstance().getUserInfo().getUid());
                Log.e("123qwe", "GloableParams.sConversations");
                if (GloableParams.sConversations != null && GloableParams.sConversations.size() > 0) {
                    Log.e("123qwe", "GloableParams.sConversations:" + GloableParams.sConversations.size());
                    sortConversationByLastChatTime(GloableParams.sConversations);
                    myHandler.sendEmptyMessage(1);
                }
            }
        }).start();
        RequestTime time = LWFriendManager.getInstance().getRequestTime(LWUserManager.getInstance().getUserInfo().getUid());
        List<MsgIndex> indexs = null;
        if (time != null){
            MsgIndex index = new MsgIndex();
            index.setMsgId(time.getMsglisttime());
            index.setSendId(time.getLastmsg_sendId());
            index.setSendType(time.getLastmsg_sendType());
            indexs = new ArrayList<>();
            indexs.add(index);
        }
//        getMsgListRequest(indexs/*time, 0, LWFriendManager.USERID*/);
        GloableParams.sConversations = LWConversationManager.getInstance().getAllConversations(LWUserManager.getInstance().getUserInfo().getUid());
        if (GloableParams.sConversations != null && GloableParams.sConversations.size() > 0) {
            sortConversationByLastChatTime(GloableParams.sConversations);
            myHandler.sendEmptyMessage(1);
        }
        myHandler.sendEmptyMessage(2);
    }

    /**
     * 根据最后一条消息的时间排序
     *
     */
    private void sortConversationByLastChatTime(
            List<Conversation> conversationList) {
        if (conversationList == null || conversationList.size() == 0) {
            return;
        }
		Collections.sort(conversationList, new Comparator<Conversation>() {
			@Override
			public int compare(final Conversation con1,
					final Conversation con2) {

                MsgItem con2LastMessage = con2.getLastMessage();
                MsgItem con1LastMessage = con1.getLastMessage();
                if (con2LastMessage == null || con1LastMessage == null){
                    return 0;
                }
				if (con2LastMessage.getTimestamp() == con1LastMessage
						.getTimestamp()) {
					return 0;
				} else if (con2LastMessage.getTimestamp() > con1LastMessage
						.getTimestamp()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
    }

    private void setOnListener() {
        lvContact.setOnItemClickListener(this);
        errorItem.setOnClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
//		if (adpter.PublicMsg != null && position == 0) {
//			// 打开订阅号列表页面
//			Utils.start_Activity(getActivity(), PublishMsgListActivity.class);
//		} else {
//        ((MainActivity) getActivity()).updateUnreadLabel(6);
        Conversation conversation = GloableParams.sConversations.get(position);
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        Hashtable<String, String> ChatRecord = adpter.getChatRecord();
        if (ChatRecord != null) {
            if (conversation.getIsGroup()) {
                GroupItem info = LWFriendManager.getInstance().getGroupById(conversation
                        .getLocalid());
                if (info != null) {
                    intent.putExtra(Constants.TYPE,
                            LWConversationManager.CHATTYPE_GROUP);
                    intent.putExtra(Constants.GROUP_ID, conversation.getLocalid());
                    intent.putExtra(Constants.NAME, info.getName());// 设置标题
                    getActivity().startActivity(intent);
                } else {
                    intent.putExtra(Constants.TYPE,
                            LWConversationManager.CHATTYPE_GROUP);
                    intent.putExtra(Constants.GROUP_ID, conversation.getLocalid());
                    intent.putExtra(Constants.NAME, GloableParams.sConversations.get(position).getUsername());// 设置标题
                    getActivity().startActivity(intent);
                }
            } else {
                Contact user = LWFriendManager.getInstance().queryFriendItem(conversation.getLocalid());
                if (user != null) {
                    intent.putExtra(Constants.NAME, user.getRemark());// 设置昵称
                    intent.putExtra(Constants.TYPE,
                            LWConversationManager.CHATTYPE_SINGLE);
                    intent.putExtra(Constants.User_ID,
                            conversation.getLocalid());
                    getActivity().startActivity(intent);
                } else {
                    intent.putExtra(Constants.NAME, "好友");
                    intent.putExtra(Constants.TYPE,
                            LWConversationManager.CHATTYPE_SINGLE);
                    intent.putExtra(Constants.User_ID,
                            conversation.getLocalid());
                    getActivity().startActivity(intent);
                }
            }
        }
//
//		}
    }

    @Override
    public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.rl_error_item:
//			NetUtil.openSetNetWork(getActivity());
//			break;
//		default:
//			break;
//		}
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GloableParams.sConversations = LWConversationManager.getInstance().getAllConversations(LWUserManager.getInstance().getUserInfo().getUid());
                    Log.e("123qwe", "GloableParams.sConversations");
                    if (GloableParams.sConversations != null && GloableParams.sConversations.size() > 0) {
                        Log.e("123qwe", "GloableParams.sConversations:" + GloableParams.sConversations.size());
                        sortConversationByLastChatTime(GloableParams.sConversations);
                        myHandler.sendEmptyMessage(1);
                    }
                }
            }).start();
            RequestTime time = LWFriendManager.getInstance().getRequestTime(LWUserManager.getInstance().getUserInfo().getUid());
            List<MsgIndex> indexs = null;
            if (time != null){
                MsgIndex index = new MsgIndex();
                index.setMsgId(time.getMsglisttime());
                index.setSendId(time.getLastmsg_sendId());
                index.setSendType(time.getLastmsg_sendType());
                indexs = new ArrayList<>();
                indexs.add(index);
            }
//            getMsgListRequest(indexs/*time, 0, LWFriendManager.USERID*/);
        }
    }

    private void getMsgListRequest(List<MsgIndex> indexs) {
        MsgListRequest request = new MsgListRequest();
//        request.setTimestamp(nowTime);
//        request.setPage(page);
//        request.setFrom_account(uid);
//        request.setPagecount(100);
        request.setTokenId(LWUserManager.getInstance().getToken());
        HttpUtils.doPost(mActivity, Request.Path.MSG_MSGLIST, request, false, Request.Code.MSG_MSGLIST, this);
    }


    @Override
    public void next(Object o, int requestCode) {
        int i = 0;
        switch (requestCode) {
            case Request.Code.MSG_MSGLIST:
                MsgList msg = (MsgList)o;
                if (msg == null || msg.getItems().size() == 0) {
                    Log.e("123qwe", "msg is null");
                    myHandler.sendEmptyMessage(0);
                    return;
                }
                long msgid = 0;
                ArrayList<MsgIndex> msgIndexs = new ArrayList<>();
                MsgIndex index = null;
                for (MsgInfoItemGroup group : msg.getItems()) {
                    for (MsgItem msgitem : group.getMsgInfoItems()) {
                        msgitem.setMsgContent(msgitem.getMsgContent());
                        msgitem.setUserid(String.valueOf(group.getSendId()));
                        msgitem.setChattype(group.getSendType());
                        if (msgitem.getMsgid() > msgid) {
                            msgid = msgitem.getMsgid();
                            index = new MsgIndex();
                            index.setMsgId(msgitem.getMsgid());
                            index.setSendId(group.getSendId());
                            index.setSendType(group.getSendType());
                        }
                    }
                    LWConversationManager.getInstance().insertOrReplaceUnreadMsg(group.getMsgInfoItems());
                }
                if (index != null){
                    msgIndexs.add(index);
                }

//        for (Contact contact1 : data.getItems()) {
//            Log.e("123qwe", "contact1:" + contact1.getUserid() + ",nick:" + contact1.getNickname());
//        }
                GloableParams.sConversations = LWConversationManager.getInstance().getAllConversations(LWUserManager.getInstance().getUserInfo().getUid());
                if (GloableParams.sConversations != null && GloableParams.sConversations.size() > 0) {
                    sortConversationByLastChatTime(GloableParams.sConversations);
                    myHandler.sendEmptyMessage(1);
                }
//        for (Contact contact : GloableParams.contactInfos) {
//            Log.e("123qwe", "contact:" + contact.getUserid() + ",nick:" + contact.getNickname());
//        }
                RequestTime requestTime = LWFriendManager.getInstance().getRequestTime(LWUserManager.getInstance().getUserInfo().getUid());
                if (requestTime == null) {
                    requestTime = new RequestTime();
                    requestTime.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
                }
                if (msgid != 0) {
                    requestTime.setMsglisttime(msgid);
                    if (index != null){
                        requestTime.setLastmsg_sendId(index.getSendId());
                        requestTime.setLastmsg_sendType(index.getSendType());
                    }
                }
                LWFriendManager.getInstance().updateRequest(requestTime);
                myHandler.sendEmptyMessage(2);
//                if (msgIndexs.size() >0 ){
//                    getMsgListRequest(msgIndexs);
//                }
                break;
            case Request.Code.MSG_GETMSGSTATUS:
                MsgStatusList msgStatus = (MsgStatusList)o;
                break;
            case Request.Code.MSG_REPORTRECEIVEDMSG:
                break;
            case Request.Code.MSG_REPORTREADMSG:
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
            case Request.Code.MSG_MSGLIST:
                type = new TypeToken<BaseResponse<MsgList>>() {
                }.getType();
                break;
            case Request.Code.MSG_GETMSGSTATUS:
                type = new TypeToken<BaseResponse<MsgStatusList>>() {
                }.getType();
                break;
            case Request.Code.MSG_REPORTRECEIVEDMSG:
                type = new TypeToken<BaseResponse<MsgStatusList>>() {
                }.getType();
                break;
            case Request.Code.MSG_REPORTREADMSG:
                type = new TypeToken<BaseResponse<MsgStatusList>>() {
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
//                    ((MainActivity) mContext).dismissProgressDialog();
                    break;
                case 1:
                    int count = 0;
//                    ((BaseActivity) mContext).dismissProgressDialog();
                    if (GloableParams.sConversations != null) {
                        adpter.setData(GloableParams.sConversations);
                        adpter.notifyDataSetChanged();
                    }
                    for(int i =0; i < GloableParams.sConversations.size(); i++){
                        count += GloableParams.sConversations.get(i).getUnreadMsgCount();
                    }
                    ((MainActivity) getActivity()).updateUnreadLabel(count);
                    break;
                case 2:
                    int count1 = 0;
//                    ((MainActivity) mContext).dismissProgressDialog();
                    if (GloableParams.sConversations != null) {
                        adpter.setData(GloableParams.sConversations);
                        adpter.notifyDataSetChanged();
                    }
                    for(int i =0; i < GloableParams.sConversations.size(); i++){
                        count1 += GloableParams.sConversations.get(i).getUnreadMsgCount();
                    }
                    ((MainActivity) getActivity()).updateUnreadLabel(count1);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
