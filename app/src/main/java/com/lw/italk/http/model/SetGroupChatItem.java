package com.lw.italk.http.model;

/**
 * Created by 喜明 on 2018/9/1.
 */

public class SetGroupChatItem {
    private String groupnickname;
    private boolean msgsettop;
    private boolean disturb;
    private boolean shownick;

    public String getGroupnickname() {
        return groupnickname;
    }

    public void setGroupnickname(String groupnickname) {
        this.groupnickname = groupnickname;
    }

    public boolean isMsgsettop() {
        return msgsettop;
    }

    public void setMsgsettop(boolean msgsettop) {
        this.msgsettop = msgsettop;
    }

    public boolean isDisturb() {
        return disturb;
    }

    public void setDisturb(boolean disturb) {
        this.disturb = disturb;
    }

    public boolean isShownick() {
        return shownick;
    }

    public void setShownick(boolean shownick) {
        this.shownick = shownick;
    }
}
