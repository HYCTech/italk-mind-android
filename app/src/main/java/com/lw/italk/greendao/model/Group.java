package com.lw.italk.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by wzy on 2018/8/21.
 * 群组消息
 */

@Entity
public class Group {
    private String name;
    @NotNull
    private String jid;
    @NotNull
    private String nick;
    @NotNull
    private String owner;
    private Long modifiedtime;
    private Long ispublic;
    private String desc;
    private Long members_size;
    private Long isblocked;
    private String members;
    @Generated(hash = 1775735493)
    public Group(String name, @NotNull String jid, @NotNull String nick,
            @NotNull String owner, Long modifiedtime, Long ispublic, String desc,
            Long members_size, Long isblocked, String members) {
        this.name = name;
        this.jid = jid;
        this.nick = nick;
        this.owner = owner;
        this.modifiedtime = modifiedtime;
        this.ispublic = ispublic;
        this.desc = desc;
        this.members_size = members_size;
        this.isblocked = isblocked;
        this.members = members;
    }
    @Generated(hash = 117982048)
    public Group() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getJid() {
        return this.jid;
    }
    public void setJid(String jid) {
        this.jid = jid;
    }
    public String getNick() {
        return this.nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getOwner() {
        return this.owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public Long getModifiedtime() {
        return this.modifiedtime;
    }
    public void setModifiedtime(Long modifiedtime) {
        this.modifiedtime = modifiedtime;
    }
    public Long getIspublic() {
        return this.ispublic;
    }
    public void setIspublic(Long ispublic) {
        this.ispublic = ispublic;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public Long getMembers_size() {
        return this.members_size;
    }
    public void setMembers_size(Long members_size) {
        this.members_size = members_size;
    }
    public Long getIsblocked() {
        return this.isblocked;
    }
    public void setIsblocked(Long isblocked) {
        this.isblocked = isblocked;
    }
    public String getMembers() {
        return this.members;
    }
    public void setMembers(String members) {
        this.members = members;
    }

}
