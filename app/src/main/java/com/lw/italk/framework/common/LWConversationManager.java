package com.lw.italk.framework.common;

import android.util.Log;

import com.lw.italk.entity.LWConversation;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.gson.msg.MsgItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class LWConversationManager {

    public static final int TXT = 0;
    public static final int LOCATION = 1;
    public static final int IMAGE = 2;
    public static final int VIDEO = 3;
    public static final int VOICE = 4;
    public static final int REVOKE = 5;//撤销
    public static final int FILE = 6;
    public static final int ADD_FRIEND_RQ = 7;
    public static final int ADD_FRIEND_RESPONSE = 8;//应答
    public static final int VERIFY_FRIEND_SUCCESS = 14;
    public static final int GROUP_CREAT = 15;
    public static final int GROUP_MENBER_QUIT = 16;
    public static final int GROUP_MENBER_ADD = 17;
    public static final int DELETE_GROUP_MENBER = 21;
    public static final int CHAT_STATUS_CHANGE = 22;


    public static final int SEND = 0;
    public static final int RECEIVE = 2;
    public static final int READ = 3;
    public static final int SUCCESS = 4;
    public static final int FAIL = 5;
    public static final int CREATE = 6;
    public static final int INPROGRESS = 7;

    public static final int DIRECT_SEND = 0;
    public static final int DIRECT_RECEIVE = 1;

    public static final int CHATTYPE_SINGLE = 1;//原来是0，1，修改为1，2,  3为公司
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_COMPANY = 3;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

//    private List<Conversation> mConversationList = null;
    private static LWConversationManager instance = new LWConversationManager();

    LWConversationManager() {
    }

    public static LWConversationManager getInstance() {
        return instance;
    }
    public List<Conversation> getAllConversations(String userId) {
            List<Conversation> conversationList = LWDBManager.getInstance().getmAllConvers(userId);
            Log.e("123qwe", "conversationList:" + conversationList.size() + ",userId:" + userId);
            for (Conversation conversation : conversationList) {
                Log.e("123qwe", "conversation:" + conversation.getLocalid());
                List<MsgItem> msgItemList;
                int unreadCount = 0;
                msgItemList = LWDBManager.getInstance().getMsgByFid(conversation.getLocalid() + "", conversation.getIsGroup()?LWConversationManager.CHATTYPE_GROUP:LWConversationManager.CHATTYPE_SINGLE);
                unreadCount = LWDBManager.getInstance().getMsgUnreadCountByFid(conversation.getLocalid() + "");

                Log.e("123qwe", "msgItemList:" + msgItemList.size());
                if (null != msgItemList) {
                    conversation.messages = msgItemList;
                    MsgItem msgItem = conversation.getLastMessage();
                    if (null != msgItem) {
                        conversation.setTimestamp(msgItem.getTimestamp());
                    }
                }
                conversation.setUnreadMsgCount(unreadCount);
            }
        return conversationList;
    }

    public void insertOrReplaceUnreadMsg(List<MsgItem> msgItemList) {
        LWDBManager.getInstance().insertOrReplaceUnreadMsg(msgItemList);
    }

    public void insertOrReplaceGroupChat(Conversation mConversation) {
        LWDBManager.getInstance().insertOrReplaceGroupChat(mConversation);
    }

    public void deleteByFid(String fid) {
        LWDBManager.getInstance().deleteByFid(fid);
    }

    public void deleteConvByFid(String fid) {
        LWDBManager.getInstance().deleteConvByLocalId(fid);
    }

    /**
     * 删除当前登入用户关联的msgitem单聊消息
     * @param uid
     */
    public void deleteMsgItemByUid(String uid) {
        LWDBManager.getInstance().deleteMsgItemByUserId(uid);
    }

    /**
     *
     * @param localID 用户ID
     * @param userName 用户昵称
     */
    public void updateConversationUserName(String localID,String userName){
        LWDBManager.getInstance().updateConversationBylocalId(localID,userName);
    }

    public void updateContactUserName(String uid,String userName){
        LWDBManager.getInstance().updateContactByuid(uid,userName);
    }


    /**
     * 删除当前登入用户关联的Conversion消息面板消息
     * @param uid
     */
    public void deleteConversionByUid(String uid) {
        LWDBManager.getInstance().deleteConversionByUserId(uid);
    }

    /**
     * 根据localid删除Conversionb表内容
     * @param localid
     */
    public void deleteConversaTionByLocalid(String localid,boolean isGroup) {
        LWDBManager.getInstance().deleteConversaTionByLocalid(localid,isGroup);
    }

    /**
     * 根据fid删除msgitem表内容
     * @param fid
     */
    public void deleteMsgItemByFid(String fid) {
        LWDBManager.getInstance().deleteMsgItemByFid(fid);
    }

    public void addMsg(MsgItem msgItem) {
        LWDBManager.getInstance().addMsg(msgItem);
    }
    public void updateMsg(MsgItem msgItem) {
        LWDBManager.getInstance().updateMsg(msgItem);
    }

    public Conversation getmConversById(String localid) {
        Conversation conversation = LWDBManager.getInstance().getmConversById(localid);
        List<MsgItem> msgItemList;
//        if (conversation.getIsGroup()) {
            msgItemList = LWDBManager.getInstance().getMsgByFid(conversation.getLocalid() + "", conversation.getIsGroup()?LWConversationManager.CHATTYPE_GROUP:LWConversationManager.CHATTYPE_SINGLE);
//        } else {
//            msgItemList = LWDBManager.getInstance().getMsgByUserId(conversation.getLocalid() + "");
//        }

        Log.e("123qwe", "getmConversById msgItemList:" + msgItemList.size());
        if (null != msgItemList) {
            conversation.messages = msgItemList;
        }
        return conversation;
    }

    public Conversation getmConversById(String localid, int offser, int limit) {
        Conversation conversation = LWDBManager.getInstance().getmConversById(localid);
        if (conversation == null){
            return null;
        }
        List<MsgItem> msgItemList;
        msgItemList = LWDBManager.getInstance().getMsgByFid(conversation.getLocalid() + "", conversation.getIsGroup()?LWConversationManager.CHATTYPE_GROUP:LWConversationManager.CHATTYPE_SINGLE, offser, limit);

        Collections.sort(msgItemList, new Comparator<MsgItem>() {
                public int compare(MsgItem o1, MsgItem o2) {
                    if (o1.getTimestamp() > o2.getTimestamp()) {
                        return 1;
                    } else if (o1.getTimestamp() == o2.getTimestamp()) {
                        return 0;
                    }
                    return -1;
                }
            });
        if (null != msgItemList) {
            if (conversation.messages == null){
                conversation.messages = msgItemList;
            }else {
                conversation.messages.clear();
                conversation.messages.addAll(msgItemList);
            }
        }
        return conversation;
    }

    public void deletConversation(Conversation conversation) {
        LWDBManager.getInstance().deletConversation(conversation);
    }

    public void deletConversationAll() {
        LWDBManager.getInstance().deletConversationAll();
    }
    public void deletConversationById(String fid) {
        LWDBManager.getInstance().deletConversationById(fid);
    }
    public Conversation getConByLocalId(String localid) {
        Conversation conversation = LWDBManager.getInstance().getmConversById(localid);
//        List<MsgItem> msgItemList;
//        if (conversation.getIsGroup()) {
//            msgItemList = LWDBManager.getInstance().getMsgByFid(conversation.getLocalid() + "");
//        } else {
//            msgItemList = LWDBManager.getInstance().getMsgByUserId(conversation.getLocalid() + "");
//        }
//
//        Log.e("123qwe", "msgItemList:" + msgItemList.size());
//        if (null != msgItemList) {
//            conversation.messages = msgItemList;
//        }
        return conversation;
    }
    public List<MsgItem> getMsgByFileName(String filename) {
        return LWDBManager.getInstance().getMsgByFileName(filename);
    }
    public MsgItem getMsgByDownId(long downid) {
        return LWDBManager.getInstance().getMsgByDownId(downid);
    }
    public List<MsgItem> getMsgByLocalId(long localid) {
        return LWDBManager.getInstance().getMsgByLocalId(localid);
    }
    public List<MsgItem> getMsgByStatus(int status, int status1) {
        return LWDBManager.getInstance().getMsgByStatus(status, status1);
    }
    public MsgItem getMsgByMsgId(String msgid, String fid) {
        return LWDBManager.getInstance().getMsgByMsgId(msgid,fid);
    }
    public void deleteMsg(MsgItem msgItem) {
        LWDBManager.getInstance().deleteMsg(msgItem);
    }

    public void updataRead(String localid) {
        LWDBManager.getInstance().updateConversionReadToZero(localid);
    }
}
