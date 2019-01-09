package com.lw.italk.gson.friend;


import java.util.List;

public class CompanyDeptList {

    private List<DeptInfo> deptInfo;
    private List<MemberInfo> members;

    public List<DeptInfo> getDeptInfo() {
        return deptInfo;
    }

    public void setDeptInfo(List<DeptInfo> deptInfo) {
        this.deptInfo = deptInfo;
    }

    public List<MemberInfo> getMembers() {
        return members;
    }

    public void setMembers(List<MemberInfo> members) {
        this.members = members;
    }
}
