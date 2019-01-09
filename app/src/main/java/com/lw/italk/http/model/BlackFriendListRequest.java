package com.lw.italk.http.model;

/**
 * Created by 喜明 on 2018/8/25.
 */

public class BlackFriendListRequest {
    private String from_account;
    private long timestamp;
    private int page;
    private int pagecount;

    public String getFrom_account() {
        return from_account;
    }

    public void setFrom_account(String from_account) {
        this.from_account = from_account;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagecount() {
        return pagecount;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }
}
