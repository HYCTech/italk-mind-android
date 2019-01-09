package com.lw.italk.gson.friend;

/**
 * Created by lxm on 2018/8/23.
 */
public class DeleteFriend {

    private int del_friendlist_count;
    private String[] del_friendlist_items;

    public int getDel_friendlist_count() {
        return del_friendlist_count;
    }

    public void setDel_friendlist_count(int del_friendlist_count) {
        this.del_friendlist_count = del_friendlist_count;
    }

    public String[] getDel_friendlist_items() {
        return del_friendlist_items;
    }

    public void setDel_friendlist_items(String[] del_friendlist_items) {
        this.del_friendlist_items = del_friendlist_items;
    }
}
