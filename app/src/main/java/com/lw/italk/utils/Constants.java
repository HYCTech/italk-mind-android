package com.lw.italk.utils;

import android.Manifest;

public interface Constants {
	String LoginState = "LoginState";
	String User_ID = "User_ID";
	String PWD = "PWD";
	String NAME = "NAME";
	String MARK_NAME = "MARK_NAME";
	String PHONE = "PHONE";
	String HEADURL = "HEADURL";
    String STAR = "star";
    String GROUP_ID = "GROUP_ID";
    String TYPE = "TYPE";
	String SEARCH_TYPE = "SEARCH_TYPE";
	String ADD_MEMBER = "ADD_MEMBER";//在好友详情中点击添加好友发起群聊
	String SEX="SEX";
	String REGISTER_TYPE = "REGISTER_TYPE";

    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

	int SEARCH_CONTACT = 0;
	int SEARCH_GROUP = 1;
	int SEARCH_BLACK_LIST = 2;
	int SEARCH_LOCAL_CONTACT = 3;
    int SEARCH_ADD_GROUP_CHAT = 4;
	int ADD_GROUP_MEMBER = 5;
	int ADD_CONTACT_MEMBER = 6;

	int DEL_GROUP_MEMBER = 7;
}
