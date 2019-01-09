package com.lw.italk.framework.common;

import android.widget.Toast;

import com.lw.italk.App;
import com.lw.italk.greendao.model.GroupMember;
import com.lw.italk.greendao.model.RequestTime;

import java.util.List;

/**
 * Created by 喜明 on 2018/9/1.
 */

public class LWGroupMemberManager {

    private static LWGroupMemberManager instance = null;
    
    LWGroupMemberManager() {
    }

    public static synchronized LWGroupMemberManager getInstance() {
        if (instance == null) {
            instance = new LWGroupMemberManager();
        }
        return instance;
    }

    public void addGroupMember(List<GroupMember> groupMemberList, String groupid) {
        LWDBManager.getInstance().insertOrReplaceGroupMember(groupMemberList, groupid);
    }

    public List<GroupMember> getAllGroupMember(String group_id){
        return LWDBManager.getInstance().getAllGroupMember(group_id);
    }

    public List<GroupMember> getLimitGroupMemberList(String group_id, int limit) {
        return LWDBManager.getInstance().getLimitGroupMemberList(group_id, limit);
    }

    public long getNowTime(String userid) {
        RequestTime requestTime = LWDBManager.getInstance().getNowTime(userid);
        if (requestTime == null) {

            return 0;
        }
        return requestTime.getGroupmembertime();
    }

    public RequestTime getRequestTime(String userid) {
        RequestTime requestTime = LWDBManager.getInstance().getNowTime(userid);
        return requestTime;
    }

    public void updateRequest(RequestTime requestTime) {
        LWDBManager.getInstance().insertOrReplaceRequestTime(requestTime);
    }

    public GroupMember queryGroupMember(String user_id,String group_id){
        return LWDBManager.getInstance().queryGroupMember(user_id,group_id);
    }

    public void deleteGroupMember(String user_id,String group_id){
        GroupMember entity = LWDBManager.getInstance().queryGroupMember(user_id,group_id);
        if(entity != null){
            LWDBManager.getInstance().deleteGroupMember(entity);
        }else{
            Toast.makeText(App.getInstance(),"数据操作失败！",Toast.LENGTH_SHORT).show();
        }
    }

    public  void deleteGroupMemberList(List<GroupMember> memberList){
        for (GroupMember member :  memberList){
            deleteGroupMember(member.getUserid(),member.getGroupid());
        }
    }
}
