package com.lw.italk.framework.common;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.italk.www.italknet;
import com.italkmind.client.protocol.ProtocolBodyParseHelper;
import com.italkmind.client.util.ClientContants;
import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.protocol.ItalkMindMessage;
import com.italkmind.client.vo.protocol.body.HeaderMessage;
import com.italkmind.client.vo.protocol.body.PushMessage;
import com.italkmind.client.vo.protocol.body.SendItalkMessage;
import com.italkmind.client.vo.protocol.body.SystemMessage;
import com.lw.italk.App;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.activity.MainActivity;
import com.lw.italk.entity.Meta;
import com.lw.italk.entity.TcpResponse;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.greendao.model.GroupMembe;
import com.lw.italk.greendao.model.RequestTime;
import com.lw.italk.gson.friend.GetAddFrienditem;
import com.lw.italk.gson.msg.MsgInfoItemGroup;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.gson.msg.MsgList;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.MsgIndex;
import com.lw.italk.http.model.MsgListRequest;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.utils.ImageUtils;
import com.lw.italk.utils.ItalkLog;
import com.lw.italk.utils.Utils;
import com.zhang.netty_lib.netty.FutureListener;
import com.zhang.netty_lib.netty.NettyClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class LWJNIManager implements Response {
    private static final String TAG = "LWJNIManager";
    private static LWJNIManager instance = null;
    private static final String META_TYPE_CHATMSG = "ItalkChatMsg";
    private static final String META_TYPE_ECHOMSG = "ItalkChatEchoMsg";
    //    private static final String TOKEN = "token";
    private MsgUpdateListen mMsgUpdateListen;
    private MsgSendRq mMsgSendRq;
    private CreatGroupListen mCreatGroupListen;
    private ConversationUpdateListen mConversationUpdateListen;

    LWJNIManager() {
    }

    public void registerConversationUpdateListen(ConversationUpdateListen conversationUpdateListen) {
        mConversationUpdateListen = conversationUpdateListen;
    }

    public void unregisterConversationUpdateListen() {
        mConversationUpdateListen = null;
    }

    public void registerCreatGroupListen(CreatGroupListen msgUpdateListen) {
        mCreatGroupListen = msgUpdateListen;
    }

    public void unregisterCreatGroupListen() {
        mCreatGroupListen = null;
    }

    public void registerMsgSendListen(MsgSendRq msgUpdateListen) {
        mMsgSendRq = msgUpdateListen;
    }

    public void unregisterMsgSendUpdateListen() {
        mMsgSendRq = null;
    }

    public void registerMsgUpdateListen(MsgUpdateListen msgUpdateListen) {
        mMsgUpdateListen = msgUpdateListen;
    }

    public void unregisterMsgUpdateListen() {
        mMsgUpdateListen = null;
    }

    public MsgUpdateListen getmMsgUpdateListen() {
        return mMsgUpdateListen;
    }

    public static synchronized LWJNIManager getInstance() {
        if (instance == null) {
            instance = new LWJNIManager();
        }
        return instance;
    }

    public void receiveTCPMessage(ItalkMindMessage serverMessage){
        List<MsgItem> msgItemList = LWConversationManager.getInstance().getMsgByLocalId(serverMessage.getHeader().getMessageId());
        MsgItem msg = null;
        for (MsgItem item : msgItemList) {
            if (item.getDirect() == LWConversationManager.DIRECT_SEND) {
                msg = item;
                break;
            }
        }
         if(serverMessage.getHeader().getCmdType() == ProtocolBodyParseHelper.CODE_SEND_MESSAGE_ACK.getCode()){//消息发送成功
            if (msg == null) {
                return;
            }
            PushMessage.MsgAckMessage body = (PushMessage.MsgAckMessage)serverMessage.getBody();
            msg.setMsgid(body.getMsgId());
            msg.setStatus(LWConversationManager.SUCCESS);
            LWConversationManager.getInstance().updateMsg(msg);
            notifyUI(msg);
        }else if(serverMessage.getHeader().getCmdType() == ProtocolBodyParseHelper.ERROR_RESULT_ACK.getCode()){
            if (msg == null) {
                return;
            }
            msg.setStatus(LWConversationManager.FAIL);
            LWConversationManager.getInstance().updateMsg(msg);
            notifyUI(msg);
        }else if(serverMessage.getHeader().getCmdType() == ProtocolBodyParseHelper.CODE_NEW_MESSAGE_REQ.getCode()){
            //todo:拉取消息
             RequestTime time = LWFriendManager.getInstance().getRequestTime(LWUserManager.getInstance().getUserInfo().getUid());
             List<MsgIndex> indexs = null;
             if (time != null && time.getMsglisttime() != 0){
                 MsgIndex index = new MsgIndex();
                 index.setMsgId(time.getMsglisttime());
                 index.setSendId(time.getLastmsg_sendId());
                 index.setSendType(time.getLastmsg_sendType());
                 indexs = new ArrayList<>();
                 indexs.add(index);
             }
             getMsgListRequest(indexs);
        }

    }
    private void getMsgListRequest(List<MsgIndex> indexs) {
        MsgListRequest request = new MsgListRequest();
        request.setTokenId(LWUserManager.getInstance().getToken());
        if (indexs != null && indexs.size() >0){
            MsgIndex[] indexArray = new MsgIndex[indexs.size()];
            for(int i= 0 ;i< indexs.size();i++) {
                indexArray[i] = indexs.get(i);
            }
            request.setMsgIndex(indexArray);
        }
        HttpUtils.doPost(null, Request.Path.MSG_MSGLIST, request, false, Request.Code.MSG_MSGLIST, this);
    }
    private void notifyUI(MsgItem msgItem){
        if (mMsgUpdateListen != null) {
            List msgs = new ArrayList();
            msgs.add(msgItem);
            mMsgUpdateListen.updateMsgs(msgs);
        }
        if (mConversationUpdateListen != null) {
            mConversationUpdateListen.updateMsg();
        }
    }
    private void notifyChat(List<MsgItem> msgs){
        if (mMsgUpdateListen != null) {
            mMsgUpdateListen.updateMsgs(msgs);
        }
    }
    private void notifyConversation(){
        if (mConversationUpdateListen != null) {
            mConversationUpdateListen.updateMsg();
        }
    }

    public void readCallBack(String s) {
        Log.e("123qwe", "LWJNIManager readcallback:" + s);
        Gson gs = new Gson();
        TcpResponse tr = gs.fromJson(s, TcpResponse.class);
        if (tr != null) {
            Log.e("123qwe", "LWJNIManager readcallback tr is not null");
            Meta meta = tr.meta;
            MsgItem msgItem = tr.response;
            if (meta != null && msgItem != null) {
                Log.e("123qwe", "LWJNIManager readcallback methodname:" + meta.methodname);
                if (META_TYPE_CHATMSG.equals(meta.methodname)) {
                    int type = msgItem.getBussinesstype();
                    Log.e("123qwe", "LWJNIManager readcallback type:" + type);
                    sendMsgStatus(msgItem);
                    switch (type) {
                        case LWConversationManager.VOICE:
                            LWDownloadManager.getInstance().startDownload(msgItem.getMsgid()+"", msgItem.getFid(), msgItem.getUrl());
                            msgItem.setTimestamp(msgItem.getTimestamp() * 1000);
                            msgItem.setDirect(LWConversationManager.DIRECT_RECEIVE);
                            msgItem.setText("语音消息");
                            if (msgItem.getChattype() == LWConversationManager.CHATTYPE_SINGLE) {
                                String fid = msgItem.getFid();
                                String userid = msgItem.getUserid();
                                msgItem.setFid(userid);
                                msgItem.setUserid(fid);
                            }
                            LWConversationManager.getInstance().addMsg(msgItem);
                            beginNotify(msgItem);
                            if (mMsgUpdateListen != null) {
                                List<MsgItem> msgs = new ArrayList<MsgItem>();
                                msgs.add(msgItem);
                                mMsgUpdateListen.updateMsgs(msgs);
                            }
                            if (mConversationUpdateListen != null) {
                                mConversationUpdateListen.updateMsg();
                            }
                            break;
                        //dwonload voice

                        case LWConversationManager.IMAGE:
                        case LWConversationManager.VIDEO:
                        case LWConversationManager.FILE:
                        case LWConversationManager.TXT:
                            if (type == LWConversationManager.IMAGE ) {
                                msgItem.setText("图片消息");
                            } else  if (type == LWConversationManager.VIDEO ) {
                                msgItem.setText("视频消息");
                            } else  if (type == LWConversationManager.FILE ) {
                                msgItem.setText("文件消息");
                            } else {

                            }
                            msgItem.setDirect(LWConversationManager.DIRECT_RECEIVE);
                            msgItem.setTimestamp(msgItem.getTimestamp() * 1000);
                            if (msgItem.getChattype() == LWConversationManager.CHATTYPE_SINGLE) {
                                String fid = msgItem.getFid();
                                String userid = msgItem.getUserid();
                                msgItem.setFid(userid);
                                msgItem.setUserid(fid);
                            }
                            LWConversationManager.getInstance().addMsg(msgItem);
                            if(!LWConversationManager.getInstance().getConByLocalId(msgItem.getFid()).getDisturb()
                                    && Utils.isBackground(App.getInstance())){
                                beginNotify(msgItem);
                            }

                            if (mMsgUpdateListen != null) {
                                List<MsgItem> msgs = new ArrayList<MsgItem>();
                                msgs.add(msgItem);
                                mMsgUpdateListen.updateMsgs(msgs);
                            }
                            if (mConversationUpdateListen != null) {
                                mConversationUpdateListen.updateMsg();
                            }
                            break;
                        case LWConversationManager.GROUP_MENBER_QUIT:
                            break;
                        case LWConversationManager.GROUP_MENBER_ADD:
                            break;

                        case LWConversationManager.GROUP_CREAT:
                            if (mCreatGroupListen != null) {
                                mCreatGroupListen.msgSendResponseSuceess(msgItem);
                            }
                            break;
                        default:
                            break;


                    }
                } else if (META_TYPE_ECHOMSG.equals(meta.methodname)) {
                    List<MsgItem> msgItemList = LWConversationManager.getInstance().getMsgByLocalId(msgItem.getLocalid());
                    MsgItem msg = null;
                    for (MsgItem item : msgItemList) {
                        if (item.getDirect() == LWConversationManager.DIRECT_SEND) {
                            msg = item;
                            break;
                        }
                    }
                    if (msg == null) {
                        return;
                    }
//                    LWConversationManager.getInstance().deleteMsg(msg);
                    if (meta.actionstatus) {
                        msg.setMsgid(msgItem.getMsgid());
                        msg.setStatus(LWConversationManager.SUCCESS);
                        LWConversationManager.getInstance().updateMsg(msg);
                    } else {
                        msg.setStatus(LWConversationManager.FAIL);
                        LWConversationManager.getInstance().updateMsg(msg);
                    }
                    if (mMsgUpdateListen != null) {
                        List<MsgItem> msgs = new ArrayList<MsgItem>();
                        msgs.add(msgItem);
                        mMsgUpdateListen.updateMsgs(msgs);
                    }
                } else {

                }

            }
        }
    }

    private void beginNotify(MsgItem msgItem) {
        NotificationManager manager = (NotificationManager) App.getInstance().getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getInstance());
        String imagePath = "";
        if (msgItem.getChattype() != LWConversationManager.CHATTYPE_GROUP) {
            Contact contact = LWFriendManager.getInstance().queryFriendItem(msgItem.getFid());
            if (contact != null) {
                imagePath = contact.getLocalimage();
            }
        } else {

        }
        Bitmap loadbitmap = null;
        Log.e("123qwe", "receive ms:" + imagePath);
        if (!TextUtils.isEmpty(imagePath)) {
            loadbitmap = BitmapFactory.decodeFile(imagePath, ImageUtils.getBitmapOption(2));
        } else {
//            InputStream is = App.getInstance().getResources().openRawResource(R.drawable.default_img);
            loadbitmap = BitmapFactory.decodeResource(App.getInstance().getResources(), R.drawable.default_img);
        }

        Intent intent = new Intent(App.getInstance(),MainActivity.class);
        int request_code = 0;
        PendingIntent pendingIntent= PendingIntent.getActivity(App.getInstance(),
                request_code++, intent, 0);


        Notification notification = builder
               /*设置large icon*/
                .setLargeIcon(loadbitmap)
             /*设置small icon*/
                .setSmallIcon(R.drawable.logo)
            /*设置title*/
                .setContentTitle("您收到了"  + "新消息")
            /*设置详细文本*/
                .setContentText(msgItem.getText())
             /*设置发出通知的时间为发出通知时的系统时间*/
                .setWhen(System.currentTimeMillis())
             /*设置发出通知时在status bar进行提醒*/
                .setTicker("收到新消息")
            /*setOngoing(boolean)设为true,notification将无法通过左右滑动的方式清除 * 可用于添加常驻通知，必须调用cancle方法来清除 */
                .setOngoing(false)
             /*设置点击后通知消失*/
                .setAutoCancel(true)
             /*设置通知数量的显示类似于QQ那种，用于同志的合并*/
//                .setNumber(3)
             /*点击跳转到MainActivity*/
                .setContentIntent(pendingIntent)
                .build();;

//        manager.notify(121, notifyBuilder.build());
        Log.e("123qwe", "begin notify");
        int x = (int) (Math.random() * 100);
        manager.notify(x, notification);
    }

    public void addGroupMenber(String groupid, String memberid) {
        GroupMembe groupMembe = new GroupMembe();
//        MsgItem msgItem = new MsgItem();
        groupMembe.setLocalid(System.currentTimeMillis());
        groupMembe.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
        groupMembe.setFid(groupid);
        groupMembe.setChattype(LWConversationManager.CHATTYPE_GROUP);
        groupMembe.setBussinesstype(LWConversationManager.GROUP_MENBER_ADD);
        String[] members = new String[] {memberid};
        groupMembe.setMemberids(members);
        Gson gson = new Gson();
        String json = gson.toJson(groupMembe);
        try {
            JSONObject object = new JSONObject(json);
//            object.put("memberids", members);
            object.put("command", "ItalkChatMsg");
            LWJNIManager.getInstance().sendMsg(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteGroupMenber(String groupid, String memberid) {
        GroupMembe groupMembe = new GroupMembe();
//        MsgItem msgItem = new MsgItem();
        groupMembe.setLocalid(System.currentTimeMillis());
        groupMembe.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
        groupMembe.setFid(groupid);
        groupMembe.setChattype(LWConversationManager.CHATTYPE_GROUP);
        groupMembe.setBussinesstype(LWConversationManager.DELETE_GROUP_MENBER);
        String[] members = new String[] {memberid};
        groupMembe.setMemberids(members);
        Gson gson = new Gson();
        String json = gson.toJson(groupMembe);
        try {
            JSONObject object = new JSONObject(json);
//            object.put("memberids", members);
            object.put("command", "ItalkChatMsg");
            LWJNIManager.getInstance().sendMsg(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void quitGroupMenber(String groupid) {
        GroupMembe groupMembe = new GroupMembe();
//        MsgItem msgItem = new MsgItem();
        groupMembe.setLocalid(System.currentTimeMillis());
        groupMembe.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
        groupMembe.setFid(groupid);
        groupMembe.setChattype(LWConversationManager.CHATTYPE_GROUP);
        groupMembe.setBussinesstype(LWConversationManager.GROUP_MENBER_QUIT);
        String[] members = new String[] {LWUserManager.getInstance().getUserInfo().getUid()};
        groupMembe.setMemberids(members);
        Gson gson = new Gson();
        String json = gson.toJson(groupMembe);
        try {
            JSONObject object = new JSONObject(json);
//            object.put("memberids", members);
            object.put("command", "ItalkChatMsg");
            LWJNIManager.getInstance().sendMsg(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void creatGroup(Conversation conversation) {
        GroupMembe groupMembe = new GroupMembe();
//        MsgItem msgItem = new MsgItem();
        groupMembe.setLocalid(Long.parseLong(conversation.getLocalid()));
        groupMembe.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
        groupMembe.setChattype(LWConversationManager.CHATTYPE_GROUP);
        groupMembe.setBussinesstype(LWConversationManager.GROUP_CREAT);
        String[] members = conversation.getMembers().split(",");
        groupMembe.setMemberids(members);
//        msgItem.setChattype(LWConversationManager.CHATTYPE_SINGLE);
        Gson gson = new Gson();
        String json = gson.toJson(groupMembe);
        try {
            JSONObject object = new JSONObject(json);
//            object.put("memberids", members);
            object.put("command", "ItalkChatMsg");
            LWJNIManager.getInstance().sendMsg(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void se

    public void sendFriendVerify(String uid) {
        MsgItem msgItem = new MsgItem();
        msgItem.setFid(uid);
        msgItem.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
        msgItem.setLocalid(System.currentTimeMillis());
        msgItem.setBussinesstype(LWConversationManager.VERIFY_FRIEND_SUCCESS);
        msgItem.setChattype(LWConversationManager.CHATTYPE_SINGLE);
        Gson gson = new Gson();
        String json = gson.toJson(msgItem);
        try {
            JSONObject object = new JSONObject(json);
            object.put("command", "ItalkChatMsg");
            LWJNIManager.getInstance().sendMsg(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mMsgSendRq != null) {
            mMsgSendRq.msgSendResponseSuceess(msgItem.getUserid());
        }
    }

    public long sendFriendRq(String uid, String remark) {
        MsgItem msgItem = new MsgItem();
        msgItem.setFid(uid);
        Log.e("123qwe", "sendFriendRq userid:" + LWUserManager.getInstance().getUserInfo().getUid());
        msgItem.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
        Log.e("123qwe", "sendFriendRq msgItem:" + msgItem.getUserid());
        msgItem.setText(remark);
        msgItem.setLocalid(System.currentTimeMillis());
        msgItem.setBussinesstype(LWConversationManager.ADD_FRIEND_RQ);
        msgItem.setChattype(LWConversationManager.CHATTYPE_SINGLE);
        Gson gson = new Gson();
        String json = gson.toJson(msgItem);
        try {
            JSONObject object = new JSONObject(json);
            object.put("command", "ItalkChatMsg");
            LWJNIManager.getInstance().sendMsg(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msgItem.getLocalid();
    }

    public long sendMsgStatus(MsgItem msgItem) {
        MsgItem msg = new MsgItem();
        msg.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
        msg.setMsgid(msgItem.getMsgid());
        msg.setStatus(msgItem.getStatus());
        msg.setChattype(msgItem.getChattype());
        msg.setBussinesstype(LWConversationManager.CHAT_STATUS_CHANGE);
        msg.setFid(msgItem.getFid());
//        msgItem.setStatus(LWConversationManager.RECEIVE);
        Gson gson = new Gson();
        String json = gson.toJson(msg);
        try {
            JSONObject object = new JSONObject(json);
            object.put("command", "ItalkChatMsg");
            LWJNIManager.getInstance().sendMsg(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msgItem.getLocalid();
    }

    public void sendMsg(String msg) {
        ItalkLog.d("123qwe", "LWJNIManager msg:" + msg);
//        mItalknetobj.write(msg);

//        NettyClient.getInstance().sendMessage();
    }
    private ItalkMindMessage createMessage(MsgItem msg) {
        switch (msg.getBussinesstype()) {
            case LWConversationManager.TXT:
                ItalkMindMessage textMsg = ClientTools.fetchRawMessage(msg.getLocalid(),
                        ProtocolBodyParseHelper.TEXT_MESSAGE.getCode());

                SendItalkMessage.SendTextMessage.Builder builder1 = SendItalkMessage.SendTextMessage.newBuilder();
                builder1.setRecType(msg.getChattype());
                builder1.setRecId(Long.valueOf(msg.getFid()));
                builder1.setTextBody(msg.getText());
                textMsg.setBody(builder1.build());
                return textMsg;
            case LWConversationManager.IMAGE:
                ItalkMindMessage imgMsg = ClientTools.fetchRawMessage(msg.getLocalid(),
                        ProtocolBodyParseHelper.IMAGE_MESSAGE.getCode());

                SendItalkMessage.SendImageMessage.Builder imgBuilder = SendItalkMessage.SendImageMessage.newBuilder();
                imgBuilder.setRecType(msg.getChattype());
                imgBuilder.setRecId(Long.valueOf(msg.getFid()));
                imgBuilder.setHeight(msg.getHeight());
                imgBuilder.setWidth(msg.getWidth());
                imgBuilder.setRawRemotePath(msg.getUrl());
                imgBuilder.setThumbRemotePath(msg.getThumburl());
                imgMsg.setBody(imgBuilder.build());
                return imgMsg;
            case LWConversationManager.VIDEO:
                ItalkMindMessage videoMsg = ClientTools.fetchRawMessage(msg.getLocalid(),
                        ProtocolBodyParseHelper.VIDEO_MESSAGE.getCode());

                SendItalkMessage.SendVideoMessage.Builder videoBuilder = SendItalkMessage.SendVideoMessage.newBuilder();
                videoBuilder.setRecType(msg.getChattype());
                videoBuilder.setRecId(Long.valueOf(msg.getFid()));
                videoBuilder.setHeight(msg.getHeight());
                videoBuilder.setWidth(msg.getWidth());
                videoBuilder.setRawRemotePath(msg.getUrl());
                if (msg.getThumburl() != null){
                    videoBuilder.setThumbRemotePath(msg.getThumburl());
                }
                videoBuilder.setDuration(msg.getSecond());
                videoMsg.setBody(videoBuilder.build());
                return videoMsg;
            case LWConversationManager.VOICE:
                ItalkMindMessage voiceMsg = ClientTools.fetchRawMessage(msg.getLocalid(),
                        ProtocolBodyParseHelper.VOICE_MESSAGE.getCode());

                SendItalkMessage.SendVoiceMessage.Builder voiceBuilder = SendItalkMessage.SendVoiceMessage.newBuilder();
                voiceBuilder.setRecType(msg.getChattype());
                voiceBuilder.setRecId(Long.valueOf(msg.getFid()));
                voiceBuilder.setDuration(msg.getSecond());
                voiceBuilder.setRemotepath(msg.getUrl());
                voiceMsg.setBody(voiceBuilder.build());
                return voiceMsg;
            case LWConversationManager.FILE:
                ItalkMindMessage fileMsg = ClientTools.fetchRawMessage(msg.getLocalid(),
                        ProtocolBodyParseHelper.FILE_MESSAGE.getCode());
                SendItalkMessage.SendFileMessage.Builder fileBuilder = SendItalkMessage.SendFileMessage.newBuilder();
                fileBuilder.setRecType(msg.getChattype());
                fileBuilder.setRecId(Long.valueOf(msg.getFid()));
                fileBuilder.setRemotepath(msg.getUrl());
                fileBuilder.setSize(msg.getSize());
                fileBuilder.setFilename(msg.getFilename());
                fileMsg.setBody(fileBuilder.build());
                return fileMsg;
                default:
                    return null;
        }

    }
    public void sendMsgItem(MsgItem msg) {
        ItalkLog.d("123qwe", "LWJNIManager msg:" + msg.toString());
        ItalkMindMessage message = createMessage(msg);
        if (message == null){
            return;
        }
        NettyClient.getInstance().sendMessage(message, new FutureListener() {
            @Override
            public void success() {
                ItalkLog.e("LWJNIManager", "LWJNIManager msg:" + msg.toString());
            }

            @Override
            public void error() {
                msg.setStatus(LWConversationManager.FAIL);
                LWConversationManager.getInstance().updateMsg(msg);
                if (mMsgUpdateListen != null) {
                    List<MsgItem> msgs = new ArrayList<MsgItem>();
                    msgs.add(msg);
                    mMsgUpdateListen.updateMsgs(msgs);
                }
            }
        });
    }
    public ItalkMindMessage fetchContentCheckAckMsg() {
        HeaderMessage.ItalkMindHeader.Builder header = HeaderMessage.ItalkMindHeader.newBuilder();
        header.setCheckCode(ClientContants.MSG_CHECK_CODE);
        header.setCmdType(ProtocolBodyParseHelper.CONTENT_CHECK_REQ.getCode());
        header.setMessageId(ClientTools.getId());

        SendItalkMessage.SendTextMessage.Builder body = SendItalkMessage.SendTextMessage.newBuilder();
        body.setRecType(1);
        return new ItalkMindMessage(header.build(), body.build());
    }
    public void connectcallback(boolean connected) {
        Log.e("123qwe", "LWJNIManager connectcallback:" + connected);
        if (connected) {
            sendAuthor();
        }
    }
    /**
     * 发送认证信息
     */
    private void sendAuthor() {

        ProtocolBodyParseHelper.CONNECT_AUTH_REQ.getCode();
        ItalkMindMessage message = ClientTools.fetchRawMessage(ClientTools.getId(),
                ProtocolBodyParseHelper.CONNECT_AUTH_REQ.getCode());
        SystemMessage.ConnectAuthReq.Builder builder = SystemMessage.ConnectAuthReq.newBuilder();
        builder.setUserId(Long.valueOf(LWUserManager.getInstance().getUserInfo().getUid()));
        builder.setTokenId(LWUserManager.getInstance().getLinkServerInfo().getAuthId());
        message.setBody(builder.build());
        NettyClient.getInstance().sendMessage(message, null);
    }

    public void closeCallback() {
        List<MsgItem> msgItemList = LWConversationManager.getInstance().getMsgByStatus(LWConversationManager.SEND, LWConversationManager.CREATE);
        for (MsgItem msgItem: msgItemList) {
            msgItem.setStatus(LWConversationManager.FAIL);
            LWConversationManager.getInstance().updateMsg(msgItem);
        }
        if (mMsgUpdateListen != null) {
            mMsgUpdateListen.updateMsgs(msgItemList);
        }
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.MSG_MSGLIST:
                MsgList msg = ((MsgList)o);
                if (msg == null || msg.getItems().size() == 0) {
                    Log.e("123qwe", "msg is null");
                    return;
                }
                long msgid = 0;
                ArrayList<MsgIndex> msgIndexs = new ArrayList<>();
                MsgIndex index = null;
                for (MsgInfoItemGroup group : msg.getItems()) {
                    List<MsgItem> removeList = new ArrayList<>();
                    for (MsgItem msgitem : group.getMsgInfoItems()) {
                        msgitem.setMsgContent(msgitem.getMsgContent());
                        msgitem.setChattype(group.getSendType());
                        msgitem.setFid(String.valueOf(group.getSendId()));//接受的id
                        if (msgitem.getMsgid() > msgid) {
                            msgid = msgitem.getMsgid();
                            index = new MsgIndex();
                            index.setMsgId(msgitem.getMsgid());
                            if (group.getSendType() == LWConversationManager.CHATTYPE_SINGLE){
                                index.setSendId(Long.valueOf(msgitem.getUserid()));
                            }else if (group.getSendType() == LWConversationManager.CHATTYPE_GROUP){
                                index.setSendId(Long.valueOf(msgitem.getFid()));
                            }else if (group.getSendType() == LWConversationManager.CHATTYPE_COMPANY){
                                //todo: 公司类型消息
                                index.setSendId(Long.valueOf(msgitem.getFid()));
                            }
                            index.setSendType(group.getSendType());
                        }
                        if(msgitem.getBussinesstype() == LWConversationManager.ADD_FRIEND_RESPONSE){

                            GetAddFrienditem item = new GetAddFrienditem();
                            item.setUserid(msgitem.getMsgContent().getUid());
                            item.setStatus(2);
                            if (!msgitem.getMsgContent().isAgreed()){
                                item.setStatus(4);
                                removeList.add(msgitem);
                            }
                            GetAddFrienditem addFrienditem = LWDBManager.getInstance().updateAddFriendStatus(item);
                            msgitem.setUserid(msgitem.getMsgContent().getUid());
                            if (addFrienditem != null){
                                msgitem.setText(addFrienditem.getNickname()+"添加你为好友");
                            }else {
                                msgitem.setText("添加你为好友");
                            }
                            msgitem.setChattype(LWConversationManager.CHATTYPE_SINGLE);
                        }else if(msgitem.getBussinesstype() == LWConversationManager.ADD_FRIEND_RQ) {
                            removeList.add(msgitem);
                            GetAddFrienditem item = new GetAddFrienditem();
                            item.setAvatar(msgitem.getMsgContent().getAvatar());
                            item.setNickname(msgitem.getMsgContent().getUsername());
                            item.setRemarkInfo(msgitem.getMsgContent().getInfo());
                            item.setUserid(msgitem.getMsgContent().getUid());
                            item.setFollowAuthId(msgitem.getMsgContent().getFollowAuthId());
                            item.setStatus(3);
                            LWDBManager.getInstance().addFriendItem(item);
                        }
                    }
                    group.getMsgInfoItems().removeAll(removeList);
                    LWConversationManager.getInstance().insertOrReplaceUnreadMsg(group.getMsgInfoItems());
                    notifyChat(group.getMsgInfoItems());
                }
                if (index != null){
                    msgIndexs.add(index);
                }
                notifyConversation();
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

                if (msgIndexs.size() >0 ){
                    getMsgListRequest(msgIndexs);
                }
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        t.printStackTrace();
        ItalkLog.e(TAG,"requestcode = "+requestCode+"exception = "+t.getMessage());
    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.MSG_MSGLIST:
                type = new TypeToken<BaseResponse<MsgList>>() {
                }.getType();
                break;
        }
        return type;
    }
}
