package com.lw.italk.entity;

/**
 * Created by Administrator on 2018/8/18 0018.
 */

public abstract class FileMessageBody extends MessageBody {
    public transient LWCallBack downloadCallback = null;
    public transient boolean downloaded = false;
    String fileName = null;
    String localUrl = null;
    String remoteUrl = null;
    String secret = null;

    public FileMessageBody() {
    }

    public void setDownloadCallback(LWCallBack var1) {
        if(this.downloaded) {
            var1.onProgress(100, (String)null);
            var1.onSuccess();
        } else {
            this.downloadCallback = var1;
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String var1) {
        this.fileName = var1;
    }

    public String getLocalUrl() {
        return this.localUrl;
    }

    public void setLocalUrl(String var1) {
        this.localUrl = var1;
    }

    public String getRemoteUrl() {
        return this.remoteUrl;
    }

    public void setRemoteUrl(String var1) {
        this.remoteUrl = var1;
    }

    public void setSecret(String var1) {
        this.secret = var1;
    }

    public String getSecret() {
        return this.secret;
    }
}