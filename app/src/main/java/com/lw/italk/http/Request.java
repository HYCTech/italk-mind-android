package com.lw.italk.http;

/**
 * Created by cts on 2017/7/6.
 */
public interface Request {

    interface Path {
        String USER_LOGIN = "/profile/login";//"v1/user/login";
        String USER_IMAGE_VERIFY = "/profile/captcha";//图片验证码
        String USER_LOGINSMS = "/profile/sendCode";//发送验证码
        String USER_SETPROFILE = "/profile/setAccount";//注册，修改密码

        String GROUP_ADDMEMBER = "/group/addMember";

        String USER_LOGOUT = "/user/logout";//"/v1/user/logout";
        String USER_ALLPROFILE = "/user/userInfo";
        String USER_EDITINFO = "/user/editInfo";
        String USER_QUERYUSER = "/user/userInfo";
        String USER_SYSTEMTIME = "/v1/user/systemtime";

        String FRIEND_ADDFRIEND = "/friend/addFriend";
        String FRIEND_UPDATEFRIEND = "/friend/editRemark";//修改好友备注名
        String FRIEND_DELETEFRIEND = "/friend/delFriend";
        String FRIEND_VERIFY_FRIEND = "/friend/ackFollow";//还有请求应答
        String FRIEND_FRIENDLIST = "friend/friendList";
        String FRIEND_ADDTOBLACKLIST = "v1/friend/addtoblacklist";
        String FRIEND_BLACKLIST = "v1/friend/blacklist";
        String FRIEND_REMARKINFO = "/friend/fetchRemark";//获取好友的备注信息
        String FRIEND_MOVEFROMBLACKLIST = "v1/friend/movefromblacklist";
        String FRIEND_GETADDFRIENDLIST = "v1/friend/getaddfriendlist";

        String FRIEND_DEPTLIST = "/friend/deptList";

        String GROUP_CREATEGROUP = "v1/group/creategroup";
        String GROUP_GROUPLIST = "/group/fetchGroupCollectInfo";
        String GROUP_GROUPPROFILE = "/group/fetchBaseInfo";
        String GROUP_SETGROUPPROFILE = "/group/changeSetting";
        String GROUP_GROUPCHATSETTING = "v1/group/groupchatsetting";
        String GROUP_SETGROUPCHAT = "v1/group/setgroupchat";
        String GROUP_MEMBERLIST = "/group/fetchMembers";
        //        String GROUP_ADDORDELETEMEMBER = "v1/group/addordeletemember";
        String GROUP_DISBANDGROUP = "v1/group/disbandgroup";
        String GROUP_EXITGROUP = "/group/delMember";
        String GROUP_GROUPBLACKLIST = "v1/group/groupblacklist";
        String GROUP_TRANSFERGROUP = "v1/group/transfergroup";
        String GROUP_BANNEDTOSPEAK = "v1/group/bannedtospeak";

        String GROUP_ADDTOGROUPLIST = "/group/saveGroupCollect";
        String GROUP_MOVEFROMGROUPLIST = "/group/delGroupCollect";

        String MSG_MSGLIST = "/message/fetchUserMsg";//"v1/msg/msglist";
        String MSG_GETMSGSTATUS = "v1/msg/getmsgstatus";
        String MSG_REPORTRECEIVEDMSG = "v1/msg/reportreceivedmsg";
        String MSG_REPORTREADMSG = "v1/msg/reportreadmsg";

        String SETTINGS_LBSLIST = "v1/settings/lbslist";
        String SETTINGS_ADDRESSLIST = "v1/settings/addresslist";

    }

    interface Code {
        int USER_LOGIN = 0;
        int USER_LOGINSMS = 1;
        int USER_LOGOUT = 2;
        int USER_ALLPROFILE = 3;
        int USER_SETPROFILE = 4;
        int USER_QUERYUSER = 5;
        int USER_SYSTEMTIME = 6;


        int USER_IMAGE_VERIFY = 7;

        int FRIEND_ADDFRIEND = 7;
        int FRIEND_UPDATEFRIEND = 8;
        int FRIEND_DELETEFRIEND = 9;
        int FRIEND_VERIFY_FRIEND = 10;
        int FRIEND_FRIENDLIST = 11;
        int FRIEND_ADDTOBLACKLIST = 12;
        int FRIEND_BLACKLIST = 13;
        int FRIEND_REMARKINFO = 14;
        int FRIEND_MOVEFROMBLACKLIST = 15;
        int FRIEND_GETADDFRIENDLIST = 16;

        int GROUP_CREATEGROUP = 17;
        int GROUP_GROUPLIST = 18;
        int GROUP_GROUPPROFILE = 19;
        int GROUP_SETGROUPPROFILE = 20;
        int GROUP_GROUPCHATSETTING = 21;
        int GROUP_SETGROUPCHAT = 22;
        int GROUP_MEMBERLIST = 23;
        //        int GROUP_ADDORDELETEMEMBER = 24;
        int GROUP_DISBANDGROUP = 25;
        int GROUP_EXITGROUP = 26;
        int GROUP_GROUPBLACKLIST = 27;
        int GROUP_TRANSFERGROUP = 28;
        int GROUP_BANNEDTOSPEAK = 29;
        int GROUP_ADDMEMBER = 30;
        int GROUP_ADDTOGROUPLIST = 31;
        int GROUP_MOVEFROMGROUPLIST = 32;
        //        int GROUP_ADDTOGROUPLIST = 33;

        int MSG_MSGLIST = 34;
        int MSG_GETMSGSTATUS = 35;
        int MSG_REPORTRECEIVEDMSG = 36;
        int MSG_REPORTREADMSG = 37;

        int SETTINGS_LBSLIST = 38;
        int SETTINGS_ADDRESSLIST = 39;

        int change_notifymsg = 80;
        int change_notifyvoice = 81;
        int change_notifydetail = 82;
        int change_msgsound = 83;
        int change_msgshock = 84;
        int change_allowtype = 85;
        int change_recommendfriend = 86;
        int change_freeze = 87;
        int change_vip = 88;

        int OSS_AUTHENTICATION = 100;

        int FRIEND_DEPTLIST = 101;
        int USER_EDITINFO = 102;
    }
}
