package com.lw.italk.gson.group;

import java.util.List;

/**
 * Created by lxm on 2018/8/23.
 */
public class GroupList {
//    private long timestampnow;
//    private int totalnum;
    private List<GroupItem> info;

    public List<GroupItem> getInfo() {
        return info;
    }

    public void setInfo(List<GroupItem> info) {
        this.info = info;
    }
}
