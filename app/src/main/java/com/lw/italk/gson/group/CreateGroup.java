package com.lw.italk.gson.group;

/**
 * Created by lxm on 2018/8/23.
 */
public class CreateGroup {
    private String groupid;
    private int add_groupmemberlist_count;
    private String[] add_groupmemberlist_items;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public int getAdd_groupmemberlist_count() {
        return add_groupmemberlist_count;
    }

    public void setAdd_groupmemberlist_count(int add_groupmemberlist_count) {
        this.add_groupmemberlist_count = add_groupmemberlist_count;
    }

    public String[] getAdd_groupmemberlist_items() {
        return add_groupmemberlist_items;
    }

    public void setAdd_groupmemberlist_items(String[] add_groupmemberlist_items) {
        this.add_groupmemberlist_items = add_groupmemberlist_items;
    }
}
