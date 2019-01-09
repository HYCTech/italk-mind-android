package com.lw.italk.framework.common;

import android.widget.Toast;

import com.lw.italk.App;
import com.lw.italk.greendao.model.BlackList;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.RequestTime;
import com.lw.italk.gson.group.GroupItem;

import java.util.List;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class LWFriendManager {
    private static final String TAG = "Session";
    private static LWFriendManager instance = null;

    LWFriendManager() {
    }

    public static synchronized LWFriendManager getInstance() {
        if (instance == null) {
            instance = new LWFriendManager();
        }
        return instance;
    }

    public List<Contact> getAllContact(String userId) {
        return LWDBManager.getInstance().getAllContact(userId);
    }

    public void addContact(List<Contact> contactList) {
        LWDBManager.getInstance().insertOrReplaceContact(contactList);
    }
    public void addContact(Contact contact) {
        LWDBManager.getInstance().addContact(contact);
    }

    public List<BlackList> getAllBlackList(String userId) {
        return LWDBManager.getInstance().getAllBlackList(userId);
    }

    public void addBlackList(List<BlackList> blackLists) {
        LWDBManager.getInstance().insertOrReplaceBlackList(blackLists);
    }

    public void deleteFriendItem(String user_id) {//删除DB中好友
        Contact entity = LWDBManager.getInstance().queryFriendItem(user_id);
        if (entity != null) {
            LWDBManager.getInstance().deleteFriendItem(entity);
        } else {
            Toast.makeText(App.getInstance(), "数据操作失败！", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteBlackItem(String user_id) {//删除DB中黑名单
        BlackList entity = LWDBManager.getInstance().queryBlackListFriendItem(user_id);
        if (entity != null) {
            LWDBManager.getInstance().deleteBlackItem(entity);
        } else {
            Toast.makeText(App.getInstance(), "数据操作失败！", Toast.LENGTH_SHORT).show();
        }
    }

    public GroupItem getGroupById(String groupid) {
        return LWDBManager.getInstance().getGroupById(groupid);
    }

    public List<GroupItem> getAllGroupList(String userId) {
        return LWDBManager.getInstance().getAllGroupList(userId);
    }

    public void addGroupList(List<GroupItem> groupItems) {
        LWDBManager.getInstance().insertOrReplaceGroupList(groupItems);
    }

    public void clearAllGroup() {
        LWDBManager.getInstance().clearGroup();
    }
    public void addGroup(GroupItem groupItems) {
        LWDBManager.getInstance().insertOrReplaceGroup(groupItems);
    }

    public void deleteGroup(GroupItem groupItems) {
        LWDBManager.getInstance().deleteGroup(groupItems);
    }

    public long getMsgListTime(String userid) {
        RequestTime requestTime = LWDBManager.getInstance().getNowTime(userid);
        if (requestTime == null) {

            return 0;
        }
        return requestTime.getMsglisttime();
    }

    public long getNowTime(String userid) {
        RequestTime requestTime = LWDBManager.getInstance().getNowTime(userid);
        if (requestTime == null) {

            return 0;
        }
        return requestTime.getTimestampnow();
    }

    public RequestTime getRequestTime(String userid) {
        RequestTime requestTime = LWDBManager.getInstance().getNowTime(userid);
        return requestTime;
    }

    public void updateRequest(RequestTime requestTime) {
        LWDBManager.getInstance().insertOrReplaceRequestTime(requestTime);
    }

    public long getBlackListTime(String userid) {
        RequestTime requestTime = LWDBManager.getInstance().getNowTime(userid);
        if (requestTime == null) {

            return 0;
        }
        return requestTime.getBlacklisttime();
    }

    public long getGroupListTime(String userid) {
        RequestTime requestTime = LWDBManager.getInstance().getNowTime(userid);
        if (requestTime == null) {

            return 0;
        }
        return requestTime.getGrouplisttime();
    }

    public void updateContact(Contact contact){
        LWDBManager.getInstance().updateContact(contact);
    }

    public List<Contact> queryFriendItemByUrl(String url) {
        return LWDBManager.getInstance().queryFriendItemByUrl(url);
    }
    public Contact queryFriendItem(String user_id) {
        return LWDBManager.getInstance().queryFriendItem(user_id);
    }
}
