package com.lw.italk;


import com.italkmind.client.vo.api.LoginInfo;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.BlackList;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.greendao.model.GroupInfo;
import com.lw.italk.greendao.model.User;
import com.lw.italk.greendao.model.UserInfo;
import com.lw.italk.gson.friend.CompanyInfo;
import com.lw.italk.gson.group.GroupItem;
import com.lw.italk.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GloableParams {

	// 屏幕高度 宽度
	public static int WIN_WIDTH;
	public static int WIN_HEIGHT;
	public static List<Contact> contactInfos = new ArrayList<Contact>();// 好友信息
	public static List<BlackList> blackLists = new ArrayList<BlackList>();// 黑名单信息
	public static List<GroupItem> ListGroupInfos = new ArrayList<GroupItem>();// 群聊信息
	public static List<Contact> LocalContactInfos = new ArrayList<Contact>();// 本地联系人信息
	public static List<Conversation> sConversations = new ArrayList<Conversation>();
	public static Boolean isHasPulicMsg = false;
//	public static LoginInfo.LinkServerInfo linkServerInfo;// 链接信息
	public static String versionName;
	public static CompanyInfo companyInfo;

}
