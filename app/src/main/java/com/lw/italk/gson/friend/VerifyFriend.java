package com.lw.italk.gson.friend;

/**
 * Created by lxm on 2018/8/23.
 */
public class VerifyFriend {

    private int add_friendlist_count;
    private String[] add_friendlist_items;
    private int del_addfriendlist_count;
    private String[] del_addfriendlist_items;

    public int getAdd_friendlist_count() {
        return add_friendlist_count;
    }

    public void setAdd_friendlist_count(int add_friendlist_count) {
        this.add_friendlist_count = add_friendlist_count;
    }

    public String[] getAdd_friendlist_items() {
        return add_friendlist_items;
    }

    public void setAdd_friendlist_items(String[] add_friendlist_items) {
        this.add_friendlist_items = add_friendlist_items;
    }

    public int getDel_addfriendlist_count() {
        return del_addfriendlist_count;
    }

    public void setDel_addfriendlist_count(int del_addfriendlist_count) {
        this.del_addfriendlist_count = del_addfriendlist_count;
    }

    public String[] getDel_addfriendlist_items() {
        return del_addfriendlist_items;
    }

    public void setDel_addfriendlist_items(String[] del_addfriendlist_items) {
        this.del_addfriendlist_items = del_addfriendlist_items;
    }
}
