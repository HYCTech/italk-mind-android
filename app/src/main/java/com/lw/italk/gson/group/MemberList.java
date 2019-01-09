package com.lw.italk.gson.group;

import com.lw.italk.greendao.model.GroupMember;

import java.util.List;

/**
 * Created by lxm on 2018/8/23.
 */
public class MemberList {
    private List<GroupMember> members;

    public List<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMember> members) {
        this.members = members;
    }
}
