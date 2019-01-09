package com.lw.italk.framework.common;

import android.text.TextUtils;
import android.util.Log;

import com.lw.italk.App;
import com.lw.italk.entity.LWContact;
import com.lw.italk.greendao.BlackListDao;
import com.lw.italk.greendao.ContactDao;
import com.lw.italk.greendao.ConversationDao;
import com.lw.italk.greendao.DaoMaster;
import com.lw.italk.greendao.DaoSession;
import com.lw.italk.greendao.GetAddFrienditemDao;
import com.lw.italk.greendao.GroupItemDao;
import com.lw.italk.greendao.GroupMemberDao;
import com.lw.italk.greendao.MsgItemDao;
import com.lw.italk.greendao.RequestTimeDao;
import com.lw.italk.greendao.UserInfoDao;
import com.lw.italk.greendao.model.BlackList;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.greendao.model.GroupMember;
import com.lw.italk.greendao.model.RequestTime;
import com.lw.italk.greendao.model.UserInfo;
import com.lw.italk.gson.friend.GetAddFrienditem;
import com.lw.italk.gson.group.GroupItem;
import com.lw.italk.gson.msg.MsgItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class LWDBManager {
    private static final String TAG = "Session";
    public LWContact currentUser = null;
    private String lastLoginUser = null;
    private static LWDBManager instance = null;
    private DaoSession mDaoSession;
    private UserInfoDao mUserInfoDao;
    private ContactDao mContactDao;
    private RequestTimeDao mRequestTimeDao;
    private BlackListDao mBlackListDao;
    private GroupItemDao mGroupItemDao;
    private MsgItemDao mMsgItemDao;
    private ConversationDao mConversationDao;
    private GroupMemberDao mGroupMemberDao;
    private GetAddFrienditemDao mGetAddFrienditemDao;

    LWDBManager() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(App.getInstance(), "italk.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = daoMaster.newSession();
        mUserInfoDao = mDaoSession.getUserInfoDao();
        mContactDao = mDaoSession.getContactDao();
        mRequestTimeDao = mDaoSession.getRequestTimeDao();
        mBlackListDao = mDaoSession.getBlackListDao();
        mGroupItemDao = mDaoSession.getGroupItemDao();
        mMsgItemDao = mDaoSession.getMsgItemDao();
        mConversationDao = mDaoSession.getConversationDao();
        mGroupMemberDao = mDaoSession.getGroupMemberDao();
        mGetAddFrienditemDao = mDaoSession.getGetAddFrienditemDao();
    }

    public static synchronized LWDBManager getInstance() {
        if (instance == null) {
            instance = new LWDBManager();
        }
        return instance;
    }

    public UserInfo getUserInfo() {
        return mUserInfoDao.queryBuilder().where(UserInfoDao.Properties.Iscurrent.eq(true)).build().unique();
    }

    public void insertOrReplace(UserInfo userInfo) {
        mUserInfoDao.insertOrReplace(userInfo);
    }

    public void updateUserInfo(UserInfo userInfo) {
        mUserInfoDao.update(userInfo);
    }

    public void insertOrReplaceContact(final List<Contact> contactList) {
//        mDaoSession.startAsyncSession().runInTx(new Runnable() {
//            @Override
//            public void run() {
//        for (Contact contact : contactList) {
//            contact.setJid(LWFriendManager.USERID);
//            mContactDao.insertOrReplaceInTx(contact);
//        }
//
        for(Contact item : contactList){
            Log.e("123qwe", "add contact:" + item.getUid());
            item.setJid(LWUserManager.getInstance().getUserInfo().getUid());
            if(TextUtils.isEmpty(item.getRemark())){
                item.setRemark(item.getUsername());
            }
            mContactDao.insertOrReplaceInTx(item);
            //LWDownloadManager.getInstance().downloadContactHead(item.getAvatar());
        }
//            }
//        });
    }

    public void addContact(Contact contact) {
//        mDaoSession.startAsyncSession().runInTx(new Runnable() {
//            @Override
//            public void run() {
//        for (Contact contact : contactList) {
        contact.setJid(LWUserManager.getInstance().getUserInfo().getUid());
        mContactDao.insertOrReplaceInTx(contact);
//        }
//            }
//        });
    }
    public List<Contact> getAllContact(String userId) {
        return mContactDao.queryBuilder().where(ContactDao.Properties.Jid.eq(userId)).build().list();

    }

    public void insertOrReplaceBlackList(final List<BlackList> blackLists) {
        for (BlackList blackList : blackLists) {
            blackList.setJid(LWUserManager.getInstance().getUserInfo().getUid());
            mBlackListDao.insertOrReplaceInTx(blackList);
        }
    }
    public List<Conversation> getmAllConvers(String userId) {
        Log.e("123qwe", "getmAllConvers  userId:" + userId);
        return mConversationDao.queryBuilder().where(ConversationDao.Properties.Userid.eq(userId)).orderDesc(ConversationDao.Properties.Timestamp).build().list();
    }

    public void deletConversationById(String fid) {
        mConversationDao.deleteByKey(fid);
    }
    public void deletConversation(Conversation conversation) {
        mConversationDao.delete(conversation);
    }
    public void deletConversationAll() {
        mConversationDao.deleteAll();
    }
    public Conversation getmConversById(String localid) {
        return mConversationDao.queryBuilder().where(ConversationDao.Properties.Localid.eq(localid)).build().unique();
    }
    public void insertOrReplaceConversation(final Conversation conversation) {
        Log.e("123qwe", "insertOrReplaceConversation :");
        mConversationDao.insertOrReplaceInTx(conversation);
    }

    /**
     * 修改了好友备注聊天界面昵称数据更改
     * @param localID 用户ID
     */
    public void updateConversationBylocalId(String localID,String userName){
        String sql = "update "+ConversationDao.TABLENAME +" set  USERNAME = '"+userName + "'  where LOCALID" +  " = " + localID;
        mDaoSession.getDatabase().execSQL(sql);
    }

    public void updateContactByuid(String uid,String userName){
        String sql = "update "+ContactDao.TABLENAME +" set  USERNAME = '"+userName + "'  where UID" +  " = " + uid;
        mDaoSession.getDatabase().execSQL(sql);
    }

    public void updateConversionReadToZero(String localID) {
        String sql = "update "+ConversationDao.TABLENAME +" set  UNREAD_MSG_COUNT = 0 "  + " where LOCALID" +  " = " + localID;
        mDaoSession.getDatabase().execSQL(sql);

        String sql2 = "update "+MsgItemDao.TABLENAME +" set UNREAD = 0 " + " where FID" +  " = " + localID;
        mDaoSession.getDatabase().execSQL(sql2);
    }

    public void deleteByFid(String fid) {
        String sql = "delete from " + MsgItemDao.TABLENAME + " where FID" +  " = " + fid;
        mDaoSession.getDatabase().execSQL(sql);
    }

    public void deleteConvByLocalId(String fid) {
        String sql = "delete from " + ConversationDao.TABLENAME + " where LOCALID" +  " = " + fid;
        mDaoSession.getDatabase().execSQL(sql);
    }

    /**
     * 根据uid删除msgitem表内容
     * @param uid
     */
    public void deleteMsgItemByUserId(String uid) {
        String sql = "delete from " + MsgItemDao.TABLENAME  + " where USERID" +  " = " + uid;
        mDaoSession.getDatabase().execSQL(sql);
    }
    /**
     * 根据uid删除Conversionb表内容
     * @param uid
     */
    public void deleteConversionByUserId(String uid) {
        String sql = "delete from " + ConversationDao.TABLENAME  + " where USERID" +  " = " + uid;
        mDaoSession.getDatabase().execSQL(sql);
    }


    /**
     * 根据uid删除msgitem表内容
     * @param fid
     */
    public void deleteMsgItemByFid(String fid) {
        String sql = "delete from " + MsgItemDao.TABLENAME  + " where FID" +  " = " + fid;
        mDaoSession.getDatabase().execSQL(sql);
    }
    /**
     * 根据localid删除Conversionb表内容
     * @param localid
     */
    public void deleteConversaTionByLocalid(String localid,boolean isGroup) {
        String sql ="";
        if(isGroup){
            sql = "delete from " + ConversationDao.TABLENAME  + " where LOCALID" +  " = " + localid+" AND IS_GROUP = 1";
        }else{
            sql = "delete from " + ConversationDao.TABLENAME  + " where LOCALID" +  " = " + localid +" AND IS_GROUP = 0";
        }

        mDaoSession.getDatabase().execSQL(sql);
    }


    public void deleteMsg(MsgItem msgItem) {
        mMsgItemDao.delete(msgItem);
    }

    public void updateMsg(MsgItem msgItem) {
        List<MsgItem> msgItemList = LWConversationManager.getInstance().getMsgByLocalId(msgItem.getLocalid());
        MsgItem msg = null;
        for (MsgItem item : msgItemList) {
            if (item.getDirect() == msgItem.getDirect()) {
                msg = item;
                break;
            }
        }
        if (msg != null){
            msgItem.setId(msg.getId());
            mMsgItemDao.update(msgItem);
        }
    }

    public MsgItem getMsgByMsgId(String msgid ,String fid) {
        List<MsgItem> list = mMsgItemDao.queryBuilder().where(MsgItemDao.Properties.Msgid.eq(msgid), MsgItemDao.Properties.Fid.eq(fid)).build().list();
        if (list != null && list.size() > 0){
            return list.get(0);
        }else {
            return null;
        }
    }

    public MsgItem getMsgByDownId(long downid) {
        return mMsgItemDao.queryBuilder().where(MsgItemDao.Properties.Downid.eq(downid)).build().unique();
    }

    public List<MsgItem> getMsgByFileName(String filename) {
        return mMsgItemDao.queryBuilder().where(MsgItemDao.Properties.Filename.eq(filename)).build().list();
    }

    public List<MsgItem> getMsgByLocalId(long localid) {
        return mMsgItemDao.queryBuilder().where(MsgItemDao.Properties.Localid.eq(localid)).build().list();
    }

    public List<MsgItem> getMsgByStatus(int status, int status1) {
        return mMsgItemDao.queryBuilder().whereOr(MsgItemDao.Properties.Status.eq(status), MsgItemDao.Properties.Status.eq(status1)).build().list();
    }

    public List<MsgItem> getMsgByFid(String fid, int chatType) {
        Log.e("123qwe", "getMsgByFid fid:" + fid + ",chatType:" + chatType);
        return mMsgItemDao.queryBuilder().where(MsgItemDao.Properties.Fid.eq(fid), MsgItemDao.Properties.Chattype.eq(chatType)).orderAsc(MsgItemDao.Properties.Timestamp).build().list();
    }

    public List<MsgItem> getMsgByFid(String fid, int chatType, int offser, int limit) {
        return mMsgItemDao.queryBuilder().where(MsgItemDao.Properties.Fid.eq(fid), MsgItemDao.Properties.Chattype.eq(chatType)).orderDesc(MsgItemDao.Properties.Timestamp).offset(offser).limit(limit).build().list();
    }

    public List<MsgItem> getMsgByUserId(String userid) {
        return mMsgItemDao.queryBuilder().where(MsgItemDao.Properties.Userid.eq(userid), MsgItemDao.Properties.Chattype.eq(LWConversationManager.CHATTYPE_SINGLE)).orderAsc(MsgItemDao.Properties.Timestamp).build().list();
    }
    public int getMsgAllUnreadCount(String userId, String fid) {
        List<MsgItem> msgItems = mMsgItemDao.queryBuilder().where(MsgItemDao.Properties.Userid.eq(userId),
                MsgItemDao.Properties.Unread.eq(true),
                MsgItemDao.Properties.Fid.eq(fid)).build().list();
        if (msgItems == null) {
            return 0;
        }
        return msgItems.size();
    }

    public int getMsgUnreadCountByFid(String userId) {
        List<MsgItem> msgItems = mMsgItemDao.queryBuilder().where(MsgItemDao.Properties.Fid.eq(userId),
                MsgItemDao.Properties.Unread.eq(true)).build().list();
        if (msgItems == null) {
            return 0;
        }
        return msgItems.size();
    }

    public int getMsgUnreadCountByuserId(String userId) {
        List<MsgItem> msgItems = mMsgItemDao.queryBuilder().where(MsgItemDao.Properties.Userid.eq(userId),
                MsgItemDao.Properties.Unread.eq(true), MsgItemDao.Properties.Chattype.eq(LWConversationManager.CHATTYPE_SINGLE)).build().list();
        if (msgItems == null) {
            return 0;
        }
        return msgItems.size();
    }

    public void addMsg(MsgItem msgItem) {
        MsgItem msg = getMsgByMsgId(msgItem.getMsgid() + "",msgItem.getFid());
        if (msg != null) {
            mMsgItemDao.delete(msg);
        }
        if (msgItem.getDirect() == LWConversationManager.DIRECT_SEND) {
            msgItem.setUnread(false);
        } else {
            msgItem.setUnread(true);
        }
        if (msgItem.getChattype() == LWConversationManager.CHATTYPE_SINGLE) {
            String fid = msgItem.getFid();
            String userid = msgItem.getUserid();
//            msgItem.setFid(userid);
            msgItem.setFid(fid);
        }
//        msgItem.setDirect(LWConversationManager.DIRECT_RECEIVE);
        Log.e("123qwe", "addMsg msgItem get direct:" + msgItem.getDirect() + ",status:" + msgItem.getStatus());
        mMsgItemDao.insertOrReplace(msgItem);
        String id;
//        if (msgItem.getChattype() == LWConversationManager.CHATTYPE_SINGLE) {
//            id = msgItem.getUserid();
//        } else {
        id = msgItem.getFid();
//        }
        if (null == getmConversById(id)) {
            Conversation conversation = new Conversation();
            conversation.setLocalid(id);
            Log.e("123qwe", "addMsg insertOrReplaceUnreadMsg msgItem.getUserid():" + msgItem.getUserid());
            conversation.setIsGroup(msgItem.getChattype() == LWConversationManager.CHATTYPE_GROUP);
            conversation.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
            conversation.setTimestamp(msgItem.getTimestamp());
            if (!msgItem.getIsgroup()) {
                Contact contact = queryFriendItem(msgItem.getFid());
                if (contact != null) {
                    conversation.setUsername(contact.getUsername());
                    conversation.setImgurl(contact.getAvatar());
                    Log.e("123qwe", "insertOrReplaceUnreadMsg contact.getNickname():" + contact.getUsername() + ",contact.getImage():" + contact.getAvatar());
                }
            } else {
                GroupItem groupItem = getGroupById(msgItem.getLocalid() + "");
                if (groupItem != null) {
                    conversation.setUsername(groupItem.getName());
                    conversation.setImgurl("");
                }
            }
            insertOrReplaceConversation(conversation);
        }
    }


    public void insertOrReplaceUnreadMsg(final List<MsgItem> msgItemList) {
        Log.e("123qwe", "insertOrReplaceUnreadMsg msgItemList:" + msgItemList.size());
        for (MsgItem msgItem : msgItemList) {
            msgItem.setTimestamp(msgItem.getTimestamp() * 1000);
            if ( msgItem.getUserid() != null && msgItem.getUserid().equals(LWUserManager.getInstance().getUserInfo().getUid())){
                continue;
            }

            int type = msgItem.getBussinesstype();
            Log.e("123qwe", "LWJNIManager readcallback type:" + type);
            msgItem.setStatus(LWConversationManager.RECEIVE);
            LWJNIManager.getInstance().sendMsgStatus(msgItem);
            switch (type) {
                case LWConversationManager.VOICE:
                case LWConversationManager.TXT:
                case LWConversationManager.IMAGE:
                case LWConversationManager.VIDEO:
                case LWConversationManager.FILE:
                case LWConversationManager.ADD_FRIEND_RESPONSE:

                    msgItem.setUnread(true);
                    msgItem.setUid(LWUserManager.getInstance().getUserInfo().getUid());
                    msgItem.setDirect(LWConversationManager.DIRECT_RECEIVE);
//                    msgItem.setTimestamp(msgItem.getTimestamp() * 1000);
                    if (msgItem.getChattype() == LWConversationManager.CHATTYPE_SINGLE) {
                        String fid = msgItem.getFid();
                        String userid = msgItem.getUserid();
                        msgItem.setFid(userid);
                        msgItem.setUserid(fid);
                    }
                    MsgItem msg = getMsgByMsgId(msgItem.getMsgid() + "",msgItem.getFid());
                    if (msg != null) {
                        mMsgItemDao.delete(msg);
                    }
                    mMsgItemDao.insertOrReplaceInTx(msgItem);
//            LWJNIManager.getInstance().sendMsgStatus(msgItem);
                    if (msgItem.getBussinesstype() == LWConversationManager.VOICE) {
                        LWDownloadManager.getInstance().startDownload(msgItem.getMsgid()+"",msgItem.getFid(), msgItem.getUrl());
                    }
                    String id;
//            if (msgItem.getChattype() == LWConversationManager.CHATTYPE_SINGLE) {
//                id = msgItem.getUserid();
//            } else {
                    id = msgItem.getFid();
//            }
                    if (null == getmConversById(id)) {
                        Conversation conversation = new Conversation();
                        conversation.setLocalid(id);
                        Log.e("123qwe", "insertOrReplaceUnreadMsg msgItem.getUserid():" + msgItem.getUserid());
                        conversation.setIsGroup(msgItem.getChattype() == LWConversationManager.CHATTYPE_GROUP);
                        conversation.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
                        conversation.setTimestamp(msgItem.getTimestamp());
                        if (!msgItem.getIsgroup()) {
                            Contact contact = queryFriendItem(msgItem.getFid());
                            if (contact != null) {
                                conversation.setUsername(contact.getUsername());
                                conversation.setImgurl(contact.getAvatar());
                                Log.e("123qwe", "insertOrReplaceUnreadMsg contact.getNickname():" + contact.getUsername());
                            }else {
                                GetAddFrienditem addFrienditem = LWDBManager.getInstance().selectAddFriends(id);
                                if (addFrienditem != null){
                                    conversation.setUsername(addFrienditem.getNickname());
                                    conversation.setImgurl(addFrienditem.getAvatar());
                                }
                            }
                        } else {
                            GroupItem groupItem = getGroupById(msgItem.getLocalid() + "");
                            if (groupItem != null) {
                                conversation.setUsername(groupItem.getName());
                                conversation.setImgurl("");
                            }
                        }
                        insertOrReplaceConversation(conversation);
                    }
                    break;
                case LWConversationManager.GROUP_MENBER_QUIT:
                    break;
                case LWConversationManager.GROUP_MENBER_ADD:
                    break;
                case LWConversationManager.GROUP_CREAT:
                    break;
                default:
                    break;
            }

        }
    }

    public List<BlackList> getAllBlackList(String userId) {
        return mBlackListDao.queryBuilder().where(BlackListDao.Properties.Jid.eq(userId)).build().list();
    }
    //查询好友列表中的好友
    public Contact queryFriendItem(String user_id){
        return mContactDao.queryBuilder().where(ContactDao.Properties.Uid.eq(user_id)).build().unique();
    }
    //查询好友列表中的好友
    public List<Contact> queryFriendItemByUrl(String url){
        return mContactDao.queryBuilder().where(ContactDao.Properties.Avatar.eq(url)).build().list();
    }

    //查询好友列表中的好友
    public void updateContact(Contact contact){
        mContactDao.update(contact);
    }
    //查询黑名单列表中的好友
    public BlackList queryBlackListFriendItem(String user_id){
        return mBlackListDao.queryBuilder().where(BlackListDao.Properties.Userid.eq(user_id)).build().unique();
    }

    //删除好友列表中的好友
    public void deleteFriendItem(Contact entity){
        mContactDao.delete(entity);
    }

    //删除黑名单列表中的好友
    public void deleteBlackItem(BlackList entity) {
        mBlackListDao.delete(entity);
    }

    public void insertOrReplaceGroupList(final List<GroupItem> groupItemList) {
        for (GroupItem groupItem : groupItemList) {
            groupItem.setJid(LWUserManager.getInstance().getUserInfo().getUid());
            mGroupItemDao.insertOrReplaceInTx(groupItem);
        }
    }

    public void insertOrReplaceGroup(GroupItem groupItem) {
        mGroupItemDao.insertOrReplaceInTx(groupItem);
    }

    public List<GroupItem> getAllGroupList(String userId) {
        return mGroupItemDao.queryBuilder().where(GroupItemDao.Properties.Jid.eq(userId)).build().list();
    }

    public GroupItem getGroupById(String groupid) {
        return mGroupItemDao.queryBuilder().where(GroupItemDao.Properties.Groupid.eq(groupid)).build().unique();
    }

    public void deleteGroup(GroupItem groupItem){
        mGroupItemDao.delete(groupItem);
    }

    public void clearGroup() {
        mGroupItemDao.deleteAll();
    }

    public RequestTime getNowTime(String userId) {
        return mRequestTimeDao.queryBuilder().where(RequestTimeDao.Properties.Userid.eq(userId)).build().unique();
    }

    public void insertOrReplaceRequestTime(RequestTime requestTime) {
        mRequestTimeDao.insertOrReplace(requestTime);
    }

    public void insertOrReplaceGroupMember(List<GroupMember> groupMemberList, String groupid){
        for (GroupMember groupMember : groupMemberList) {
            groupMember.setGroupid(groupid);
            GroupMember member = queryGroupMember(groupMember.getUserid(), groupid);
            if (member == null){
                mGroupMemberDao.insertOrReplaceInTx(groupMember);
            }
        }
    }

//    public List<GroupMember> getGroupMemberList(int start, int end) {
////        return mGroupMemberDao.queryBuilder().where(GroupItemDao.Properties.Jid.eq(userId)).build().list();
//    }

    public GroupMember queryGroupMember(String user_id,String group_id) {
        return mGroupMemberDao.queryBuilder().where(GroupMemberDao.Properties.Userid.eq(user_id),GroupMemberDao.Properties.Groupid.eq(group_id)).build().unique();
    }

    public void deleteGroupMember(GroupMember entity) {
        mGroupMemberDao.delete(entity);
    }

    public List<GroupMember> getAllGroupMember(String group_id) {
        return mGroupMemberDao.queryBuilder().where(GroupMemberDao.Properties.Groupid.eq(group_id)).build().list();
    }

    public List<GroupMember> getLimitGroupMemberList( String group_id, int limit) {
        return mGroupMemberDao.queryBuilder().where(GroupMemberDao.Properties.Groupid.eq(group_id)).limit(limit).build().list();
    }

    public void insertOrReplaceGroupChat(Conversation mConversation) {
        mConversationDao.insertOrReplaceInTx(mConversation);
    }

    public GetAddFrienditem selectAddFriends(String userid) {
        if (userid == null){
            return null;
        }
        mGetAddFrienditemDao.detachAll();//清除缓存
        List<GetAddFrienditem> list1 = mGetAddFrienditemDao.queryBuilder().where(GetAddFrienditemDao.Properties.Userid.eq(userid)).build().list();
        if (list1 == null || list1.size() == 0){
            return null;
        }
        return  list1.get(0);
    }

    public List selectAllAddFriends() {
        mGetAddFrienditemDao.detachAll();//清除缓存
        List list1 = mGetAddFrienditemDao.queryBuilder().build().list();
        return list1 == null ? new ArrayList() : list1;
    }

    public void addFriendItem(GetAddFrienditem item){
        GetAddFrienditem list = selectAddFriends(item.getUserid());
        if (list != null){
            mGetAddFrienditemDao.delete(list);
        }
        mGetAddFrienditemDao.insertOrReplace(item);
    }

    public GetAddFrienditem updateAddFriendStatus(GetAddFrienditem item) {
        GetAddFrienditem select = selectAddFriends(item.getUserid());
        if (select == null){
            return null;
        }
        select.setStatus(item.getStatus());
        mGetAddFrienditemDao.update(select);
        return select;
    }
}
