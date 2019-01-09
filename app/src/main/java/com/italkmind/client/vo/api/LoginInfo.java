/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: AbstractProtocolBodyParse.java
* @version V1.0  
*/
package com.italkmind.client.vo.api;


import com.lw.italk.greendao.model.UserInfo;

/**
* @ClassName: LoginInfo
* @Description: TODO(这里用一句话描述这个类的作用)
* @author fern
* @date 2018年9月7日
*
*/

public class LoginInfo {
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    private String tokenId;
    private LinkServerInfo linkInfo;
    private String fileAuthId;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public LinkServerInfo getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(LinkServerInfo linkInfo) {
        this.linkInfo = linkInfo;
    }

    public String getFileAuthId() {
        return fileAuthId;
    }

    public void setFileAuthId(String fileAuthId) {
        this.fileAuthId = fileAuthId;
    }


    public static class LinkServerInfo {
        private String majorLinkIp;
        private int majorLinkPort;
        private String secondaryLinkIp;
        private int secondaryPort;
        private String authId;

        public String getMajorLinkIp() {
            return majorLinkIp;
        }

        public void setMajorLinkIp(String majorLinkIp) {
            this.majorLinkIp = majorLinkIp;
        }

        public int getMajorLinkPort() {
            return majorLinkPort;
        }

        public void setMajorLinkPort(int majorLinkPort) {
            this.majorLinkPort = majorLinkPort;
        }

        public String getSecondaryLinkIp() {
            return secondaryLinkIp;
        }

        public void setSecondaryLinkIp(String secondaryLinkIp) {
            this.secondaryLinkIp = secondaryLinkIp;
        }

        public int getSecondaryPort() {
            return secondaryPort;
        }

        public void setSecondaryPort(int secondaryPort) {
            this.secondaryPort = secondaryPort;
        }

        public String getAuthId() {
            return authId;
        }

        public void setAuthId(String authId) {
            this.authId = authId;
        }
    }
}
