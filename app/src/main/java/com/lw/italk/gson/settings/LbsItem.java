package com.lw.italk.gson.settings;

/**
 * Created by lxm on 2018/8/23.
 */
public class LbsItem {

    private String ip;
    private String pot;
    private int weight;
    private long timestamp;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPot() {
        return pot;
    }

    public void setPot(String pot) {
        this.pot = pot;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
