package com.lw.italk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.italkmind.client.vo.api.GroupId;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.adapter.AddGroupChatAdapter;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWGroupMemberManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.greendao.model.GroupMember;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.Utils;
import com.lw.italk.entity.LWGroup;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.view.NineGridImageView;
import com.lw.italk.view.SideBar;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddGroupChatActivity extends BaseActivity implements
        OnClickListener, OnItemClickListener ,Response {
    private TextView mSearch;
    private TextView tv_back, txt_title, txt_right;
    ;
    private ListView listView;
    private SideBar indexBar;
    private TextView mDialogText;
    private WindowManager mWindowManager;
    private int mType;
    private String mGropuId;
    /**
     * 是否为一个新建的群组
     */
    protected boolean isCreatingNewGroup;
    /**
     * 是否为单选
     */
    private boolean isSignleChecked;
    private AddGroupChatAdapter mAdapter;
    /**
     * group中一开始就有的成员
     */
    private List<String> exitingMembers = new ArrayList<String>();
    private List<Contact> alluserList;// 好友列表
    // 可滑动的显示选中用户的View
    private LinearLayout menuLinerLayout;

    // 选中用户总数,右上角显示
    int total = 0;
    private String userId = null;
    private String groupId = null;
    private String groupname;
    // 添加的列表
    private List<String> addList = new ArrayList<String>();
    private String hxid;
    private LWGroup group;

    private String[] mContactID ; //在会话详情中点击添加好友发起群聊，需要将这个id加到群聊里
    private String GroupName = "";
    private List<String> GroupImageLink = new ArrayList<String>();
    private NineGridImageView groudIcon;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mType = getIntent().getIntExtra(Constants.TYPE, 0);
        mGropuId = getIntent().getStringExtra(Constants.GROUP_ID);
        Bundle bundle = this.getIntent().getExtras();
        if( bundle != null){
            mContactID=bundle.getStringArray(Constants.ADD_MEMBER);
        }

        super.onCreate(savedInstanceState);

    }

    @Override
    protected int setContentView() {
        return R.layout.activity_add_group_chat;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mDialogText);
    }

    @Override
    protected void initView() {
        tv_back = (TextView) findViewById(R.id.left_bar_item);
        tv_back.setText("消息");
        tv_back.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.center_bar_item);
        txt_title.setText("群聊");
        txt_right = (TextView) this.findViewById(R.id.right_title_bar);
        txt_right.setVisibility(View.VISIBLE);
        txt_right.setText("确定");
        txt_right.setOnClickListener(this);
        groudIcon = (NineGridImageView)findViewById(R.id.groudIcon);
        listView = (ListView) findViewById(R.id.list);
        mSearch = (TextView) findViewById(R.id.txt_search);
        mSearch.setOnClickListener(this);
        mDialogText = (TextView) LayoutInflater.from(this).inflate(
                R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        indexBar = (SideBar) findViewById(R.id.sideBar);
        indexBar.setListView(listView);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(mDialogText, lp);
        indexBar.setTextView(mDialogText);
        if (mType == Constants.ADD_GROUP_MEMBER) {
            txt_title.setText("添加群成员");
        }
    }
    List<Contact> contactInfos = null;
    @Override
    protected void initData() {
//		hxid = Utils.getValue(AddGroupChatActivity.this, Constants.User_ID);
//		groupId = getIntent().getStringExtra(Constants.GROUP_ID);
//		userId = getIntent().getStringExtra(Constants.User_ID);
        if (groupId != null) {
            isCreatingNewGroup = false;
//			group = EMGroupManager.getInstance().getGroup(groupId);
            if (group != null) {
                exitingMembers = group.getMembers();
                groupname = group.getGroupName();
            }
        } else if (userId != null) {
            isCreatingNewGroup = true;
            exitingMembers.add(userId);
            total = 1;
            addList.add(userId);
        } else {
            isCreatingNewGroup = true;
        }

        // 获取好友列表
//		alluserList = new ArrayList<User>();
//		for (User user : GloableParams.UserInfos) {
//			if (!user.getUserName().equals(Constant.NEW_FRIENDS_USERNAME)
//					& !user.getUserName().equals(Constant.GROUP_USERNAME))
//				alluserList.add(user);
//		}

        if(mType == Constants.ADD_GROUP_MEMBER || mType == Constants.ADD_CONTACT_MEMBER){//添加好友时，已有的好友不能出现
            contactInfos = new ArrayList<Contact>();
            for(Contact contact : GloableParams.contactInfos){
                int i = 0;
                for(i = 0; i < mContactID.length; i++){
//                    Log.e("qwe", "i --->" + i +"---j --->"+j+"---id--->"+contactInfos.get(i).getUserid()+"---jd--->"+mContactID[j]);
                    if(contact.getUid().equals(mContactID[i])){
//                        contactInfos.remove(i);
                        break;
                    }
                }
                if (i >= mContactID.length) {
                    contactInfos.add(contact);
                }
            }
            mAdapter = new AddGroupChatAdapter(AddGroupChatActivity.this,
                    contactInfos);
        }else if(mType == Constants.DEL_GROUP_MEMBER){
            List<GroupMember> allGroup= LWGroupMemberManager.getInstance().getAllGroupMember(mGropuId);
            contactInfos = new ArrayList<Contact>();
            for(int i = 0 ; i < allGroup.size(); i++){
                GroupMember member = allGroup.get(i);
                Contact contact = new Contact();
                contact.setUid(member.getUserid());
                contact.setUsername(member.getNickname());
                contact.setAvatar(member.getImage());
                if (member.getGroupnickname() != null && member.getGroupnickname().length() >0){
                    contact.setRemark(member.getGroupnickname());
                }else {
                    contact.setRemark(member.getNickname());
                }
                contactInfos.add(contact);
            }
            mAdapter = new AddGroupChatAdapter(AddGroupChatActivity.this,
                    contactInfos);
        }
        else{
            contactInfos = GloableParams.contactInfos;
            mAdapter = new AddGroupChatAdapter(AddGroupChatActivity.this,
                    contactInfos);
        }
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mAdapter.getCheckedArray()[i] = !mAdapter.getCheckedArray()[i];
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    List<GroupMember> checkList = null;
    private void addGroupMembers() {
        boolean[] isCheckedArray = mAdapter.getCheckedArray();
        String members = "";
        if (isCheckedArray != null && isCheckedArray.length != 0) {
            checkList = new ArrayList<GroupMember>();
            for (int i = 0; i < isCheckedArray.length; i++) {//所有选中的成员
                if (isCheckedArray[i]) {
                    Contact contact = contactInfos.get(i);
                    GroupMember member = new GroupMember(null,contact.getUid(),mGropuId,contact.getUsername(),contact.getAvatar(),0,contact.getRemark(),false,false,false);
                    checkList.add(member);
                    if (members.length() == 0){
                        members = contactInfos.get(i).getUid();
                    }else {
                        members = members + "," + contactInfos.get(i).getUid();
                    }
                }
            }
        }

//        LWJNIManager.getInstance().addGroupMenber(mGropuId, members);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId",LWUserManager.getInstance().getToken());
        map.put("uidGroup",members);
        map.put("groupId", mGropuId);
        HttpUtils.doPostFormMap(this, Request.Path.GROUP_ADDMEMBER, map, true, Request.Code.GROUP_ADDMEMBER, this);
//        finish();
    }

    private void delGroupMembers() {
        boolean[] isCheckedArray = mAdapter.getCheckedArray();
        String members = "";
        if (isCheckedArray != null && isCheckedArray.length != 0) {
            checkList = new ArrayList<GroupMember>();
            for (int i = 0; i < isCheckedArray.length; i++) {//所有选中的成员
                if (isCheckedArray[i]) {
                    Contact contact = contactInfos.get(i);
                    GroupMember member = new GroupMember(null,contact.getUid(),mGropuId,contact.getUsername(),contact.getAvatar(),0,contact.getRemark(),false,false,false);
                    checkList.add(member);
                    if (members.length() == 0){
                        members = contactInfos.get(i).getUid();
                    }else {
                        members = members + "," + contactInfos.get(i).getUid();
                    }
                }
            }
        }
//        LWJNIManager.getInstance().addGroupMenber(mGropuId, members);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getUserInfo().getToken());
        map.put("uidGroup",members);
        map.put("groupId", mGropuId);
        HttpUtils.doPostFormMap(this, Request.Path.GROUP_EXITGROUP, map, true, Request.Code.GROUP_EXITGROUP, this);
//        finish();
    }
    private String members="";
    private String groupLocalid = null;
    private void startGroupChat() {
        members="";
        if(LWUserManager.getInstance().getUserInfo() == null){
            Toast.makeText(this, "未登录，请退出重新登录", Toast.LENGTH_LONG).show();
            return;
        }
        boolean[] isCheckedArray = mAdapter.getCheckedArray();
        GroupName = LWUserManager.getInstance().getUserInfo().getUsername();
        members = LWUserManager.getInstance().getUserInfo().getUid();
        GroupImageLink.add(LWUserManager.getInstance().getUserInfo().getAvatar());
        if(mContactID != null && mContactID.length == 1){//从好友详情进入发起群聊时，需要将id加到群聊里
            members = members + "," + mContactID[0];
            Contact contact = LWFriendManager.getInstance().queryFriendItem(mContactID[0]);
            if (contact != null){
                GroupName = GroupName + "," + contact.getRemark();
            }
        }

        if (isCheckedArray != null && isCheckedArray.length !=0) {
            List<Contact> checkList = new ArrayList<Contact>();
            int count = 0;
            for (int i = 0; i < isCheckedArray.length; i++) {//所有选中的成员
                if (isCheckedArray[i]) {//是否选中
//                  checkList.add(GloableParams.contactInfos.get(i));
                    members = members+ ","+contactInfos.get(i).getUid();
                    if(count <= 1){//最多三个人的昵称当群昵称（除了自己，再选两个就行）
                        GroupName += ","+ contactInfos.get(i).getRemark();
                    }

                    if(count <= 7){
                        GroupImageLink.add(contactInfos.get(i).getAvatar());
                    }
                    count++;
                }
            }

            if(count == 0){
                Toast.makeText(this, "没有选中任何好友!", Toast.LENGTH_SHORT).show();
                return;
            }else if(count == 1){
                Toast.makeText(this, "最少两位好友才能创建群!", Toast.LENGTH_SHORT).show();
                return;
            }

//            NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
//                @Override
//                protected void onDisplayImage(Context context, ImageView imageView, String s) {
//                    //显示图片
//                    Glide.with(context).load(s).placeholder(R.drawable.default_img).error(R.drawable.default_img).into(imageView);
//                    //显示圆形图片
//                    //Picasso.with(context).load(s).transform(new CircleImageTransformation()).placeholder(R.mipmap.ic_holding).error(R.mipmap.ic_error).into(imageView);
//                }
//
//                @Override
//                protected ImageView generateImageView(Context context) {
//                    return super.generateImageView(context);
//                }
//            };
//            groudIcon.setAdapter(mAdapter);
//            groudIcon.setImagesData(GroupImageLink);
//            groudIcon.setDrawingCacheEnabled(true);
//            groudIcon.buildDrawingCache();

//            mHandler.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    final Bitmap bmp = groudIcon.getDrawingCache(); // 获取图片
//
//                    savePicture(bmp, "aite_qroup_head_img.jpg");// 保存图片
//                    groudIcon.destroyDrawingCache(); // 保存过后释放资源
//                }
//            }, 500);

            HashMap<String,String> map = new HashMap<String,String>();
            map.put("tokenId",LWUserManager.getInstance().getToken());
            map.put("uidGroup",members);
            map.put("groupId", String.valueOf(0));
            HttpUtils.doPostFormMap(this, Request.Path.GROUP_ADDMEMBER, map, true, Request.Code.GROUP_ADDMEMBER, this);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.left_bar_item:
                Utils.finish(AddGroupChatActivity.this);
                break;
            case R.id.right_title_bar:// 确定按钮
                if (mType == Constants.ADD_GROUP_MEMBER) {
                    addGroupMembers();
                }else if (mType == Constants.DEL_GROUP_MEMBER){
                    delGroupMembers();
                }else {
                    startGroupChat();
                }

                break;
            case R.id.txt_search:// 搜索
                Intent intent = new Intent();
                intent.setClass(AddGroupChatActivity.this, SearchViewActivity.class);
                intent.putExtra(Constants.SEARCH_TYPE, Constants.SEARCH_ADD_GROUP_CHAT);
                startActivity(intent);
                break;
            default:
                break;
        }
    }



    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        checkBox.toggle();
    }

    public void savePicture(Bitmap bm, String fileName) {//将布局保存为图片

        if (bm == null) {
            Toast.makeText(this, "savePicture null !", Toast.LENGTH_SHORT).show();
            return;
        }
        File foder = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(foder, fileName);
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
            Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.GROUP_ADDMEMBER:
                GroupId groupId = (GroupId)o;

                if (mType == Constants.ADD_GROUP_MEMBER) {
                    if (checkList != null && checkList.size() >0){
                        LWGroupMemberManager.getInstance().addGroupMember( checkList, groupId.getGid());
                    }
                    finish();
                    return;
                }else {//创建群组的情况
                    //保存数据库Conversation 能在消息面板显示群
                    Conversation mConversation = new Conversation();
                    groupLocalid = System.currentTimeMillis()+"";
                    mConversation.setLocalid(groupLocalid);
                    mConversation.setMembers(members);
                    mConversation.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
                    mConversation.setUsername(GroupName);
                    LWConversationManager.getInstance().insertOrReplaceGroupChat(mConversation);
                }

                Conversation conversation = LWConversationManager.getInstance().getConByLocalId(groupLocalid);
                if (conversation != null) {
                    LWConversationManager.getInstance().deletConversation(conversation);
                    conversation.setLocalid(groupId.getGid());
                    conversation.setIsGroup(true);
                    LWConversationManager.getInstance().insertOrReplaceGroupChat(conversation);
                    AddGroupChatActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //跳转界面
                            Intent intent = new Intent();
                            intent.setClass(AddGroupChatActivity.this, ChatActivity.class);
                            intent.putExtra(Constants.TYPE, LWConversationManager.CHATTYPE_GROUP);
                            intent.putExtra(Constants.GROUP_ID, groupId.getGid());
                            intent.putExtra(Constants.NAME, GroupName);// 设置标题
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                break;
            case Request.Code.GROUP_EXITGROUP:
                if (checkList != null && checkList.size() >0){
                    LWGroupMemberManager.getInstance().deleteGroupMemberList(checkList);
                }
                finish();
                break;
        }

    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.GROUP_ADDMEMBER:
                type = new TypeToken<BaseResponse<GroupId>>() {
                }.getType();
                break;
            case Request.Code.GROUP_EXITGROUP:
                type = new TypeToken<BaseResponse<GroupId>>() {
                }.getType();
                break;
        }
        return type;
    }
}
