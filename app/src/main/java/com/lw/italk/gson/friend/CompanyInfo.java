package com.lw.italk.gson.friend;

public class CompanyInfo {
    private long companyId;
    private String companyName;
    private String logo;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public CompanyInfo(long companyId, String companyName, String logo) {

        this.companyId = companyId;
        this.companyName = companyName;
        this.logo = logo;
    }
}
