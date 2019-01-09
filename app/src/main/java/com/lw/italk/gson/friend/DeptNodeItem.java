package com.lw.italk.gson.friend;

import java.util.List;

public class DeptNodeItem {

    private String deptName;
    private long deptId;
    private long parentId;

    private boolean isDept;//是否为部门对象

    private String uid;
    private String username;
    private String remark;
    private String avatar;

    private List<DeptNodeItem> children = null;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public boolean isDept() {
        return isDept;
    }

    public void setDept(boolean dept) {
        isDept = dept;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<DeptNodeItem> getChildren() {
        return children;
    }

    public void setChildren(List<DeptNodeItem> children) {
        this.children = children;
    }
}
