package com.lw.italk.gson.friend;

/**
 * Created by lxm on 2018/8/23.
 */
public class MoveFromBlackFriend {

    private int del_blacklist_count;
    private String[] del_blacklist_items;
    private int add_friendlist_count;
    private String[] add_friendlist_items;

    public int getDel_blacklist_count() {
        return del_blacklist_count;
    }

    public void setDel_blacklist_count(int del_blacklist_count) {
        this.del_blacklist_count = del_blacklist_count;
    }

    public String[] getDel_blacklist_items() {
        return del_blacklist_items;
    }

    public void setDel_blacklist_items(String[] del_blacklist_items) {
        this.del_blacklist_items = del_blacklist_items;
    }

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
}
