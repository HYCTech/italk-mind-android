package com.lw.italk.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.kyleduo.switchbutton.SwitchButton;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.adapter.GroupMemberDetailAdapter;
import com.lw.italk.entity.LWConversation;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWChatManager;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWGroupMemberManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.greendao.model.RequestTime;
import com.lw.italk.gson.group.AddGroupList;
import com.lw.italk.gson.group.GroupChatSetting;
import com.lw.italk.gson.group.GroupItem;
import com.lw.italk.gson.group.GroupProfile;
import com.lw.italk.gson.group.MemberList;
import com.lw.italk.gson.group.MoveGroupList;
import com.lw.italk.gson.group.SetGroupProfile;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.AddOrMoveGroupListRequest;
import com.lw.italk.http.model.GetGroupChatRequest;
import com.lw.italk.http.model.GetGroupMemberList;
import com.lw.italk.http.model.SetGroupChatItem;
import com.lw.italk.http.model.SetGroupChatRequest;
import com.lw.italk.http.model.SetGroupProfileItem;
import com.lw.italk.http.model.SetGroupProfileRequest;
import com.lw.italk.utils.Constants;
import com.lw.italk.view.dialog.EditCenterDialog;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群聊详情页
 * Created by 喜明 on 2018/9/1.
 */

public class GroupChatDetailActivity extends BaseActivity implements Response, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.left_bar_item)
    TextView mLeftBarItem;
    @BindView(R.id.center_bar_item)
    TextView mCenterBarItem;
    @BindView(R.id.title_bar_left)
    LinearLayout titleBarLeft;
    @BindView(R.id.right_title_bar)
    TextView mRightTitleBar;
    @BindView(R.id.title_bar_right)
    RelativeLayout titleBarRight;
    @BindView(R.id.more_group_friend)
    TextView moreGroupFriend;
    @BindView(R.id.tv_group_name)
    TextView tvGroupName;
    @BindView(R.id.rl_group_name_item)
    RelativeLayout mGroupNameItem;
    @BindView(R.id.chat_to_top)
    SwitchButton chatToTop;
    @BindView(R.id.message_free)
    SwitchButton messageFree;
    @BindView(R.id.add_friend_verification)
    RelativeLayout addFriendVerification;
    @BindView(R.id.my_group_nike)
    TextView mGroupNike;
    @BindView(R.id.my_nike_item)
    RelativeLayout mMyNikeItem;
    @BindView(R.id.show_group_name)
    SwitchButton showGroupName;
    @BindView(R.id.clear_local_chat)
    TextView mClearLocalChat;
    @BindView(R.id.delete_layout)
    TextView mDeleteChat;
    @BindView(R.id.ll_group_chat_detail)
    LinearLayout llGroupChatDetail;
    @BindView(R.id.group_member_detail_list)
    GridView mGroupMemberDetailList;
    @BindView(R.id.save_group_chat)
    SwitchButton saveGroupChat;

    private GroupMemberDetailAdapter mAdapter;
    private String mGroupId = "";
    private GroupProfile mGroupProfile;
    private Conversation mConversation;

    @Override
    protected int setContentView() {
        return R.layout.activity_group_chat_detail;
    }

    @Override
    protected void initView() {
        mLeftBarItem.setText("返回");
        mCenterBarItem.setText("聊天信息");
        mRightTitleBar.setVisibility(View.GONE);
        chatToTop.setOnCheckedChangeListener(this);
        messageFree.setOnCheckedChangeListener(this);
        saveGroupChat.setOnCheckedChangeListener(this);
        GroupItem mGroupItem = LWFriendManager.getInstance().getGroupById(mGroupId);
        if(mGroupItem == null || TextUtils.isEmpty(mGroupItem.getGroupid())){
            saveGroupChat.setChecked(false);
        }else {
            saveGroupChat.setChecked(true);
        }
    }

    @Override
    protected void initData() {
        mConversation = LWConversationManager.getInstance().getConByLocalId(mGroupId);
        getGroupInfo();
        getGroupChatSetting();


        llGroupChatDetail.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGroupId = getIntent().getStringExtra(Constants.GROUP_ID);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_group_name_item, R.id.my_nike_item, R.id.clear_local_chat, R.id.delete_layout, R.id.more_group_friend, R.id.left_bar_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.more_group_friend:
                Intent intent = new Intent(GroupChatDetailActivity.this, GroupContactListActivity.class);
                intent.putExtra(Constants.GROUP_ID, mGroupId);
                startActivity(intent);
                break;
            case R.id.rl_group_name_item:
                if (mGroupProfile != null && mGroupProfile.getOwner_id().equals(LWDBManager.getInstance().getUserInfo().getUid())) {
                    changeGroupName();
                } else {
                    Toast.makeText(this, "不是群主，无法修改群名称", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.my_nike_item:
                changeGroupNike();
                break;
            case R.id.clear_local_chat:
                emptyChatRecord();
                break;
            case R.id.delete_layout:
                exitGroupList();

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getMemberList();
        int temp = LWGroupMemberManager.getInstance().getAllGroupMember(mGroupId).size();
        if (temp > 9) {
            temp = 9;
        }
        mAdapter = new GroupMemberDetailAdapter(this, LWGroupMemberManager.getInstance().getLimitGroupMemberList(mGroupId,temp));
        mGroupMemberDetailList.setAdapter(mAdapter);
        mAdapter.setmGroupId(mGroupId);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.chat_to_top:
                mConversation.setMsgsettop(isChecked);
                setGroupChatSetting();
                break;
            case R.id.message_free:
                mConversation.setDisturb(isChecked);
                setGroupChatSetting();
                break;
            case R.id.show_group_name:
                mConversation.setShowname(isChecked);
                break;
            case R.id.save_group_chat:
                if(isChecked){
                    addGroupList();
                }else{
                    moveGroupList();
                }
                break;
            default:
                break;
        }
    }

    private void getGroupInfo() {//获取群资料
//        GetGroupChatRequest request = new GetGroupChatRequest();
//        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
//        request.setGroupid(mGroupId);
//        String[] item = {"owner_id", "name"};
//        request.setItems(item);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("groupId",mGroupId);
        HttpUtils.doPostFormMap(this, Request.Path.GROUP_GROUPPROFILE, map, true, Request.Code.GROUP_GROUPPROFILE, this);
    }

    private void setGroupInfo(SetGroupProfileItem items) {//修改群资料
//        SetGroupProfileRequest request = new SetGroupProfileRequest();
//        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
//        request.setGroupid(mGroupId);
//        request.setItems(items);

        Map<String, String> requestItem = new HashMap<String, String>();
        requestItem.put("content", items.getName());
        requestItem.put("type", String.valueOf(1));//群名称修改
        requestItem.put("tokenId", LWUserManager.getInstance().getToken());//性别
        requestItem.put("groupId", mGroupId);
        HttpUtils.doPostFormMap(this, Request.Path.GROUP_SETGROUPPROFILE, requestItem, true, Request.Code.GROUP_SETGROUPPROFILE, this);

    }

    private void getGroupChatSetting() {//获取群设置
        GetGroupChatRequest request = new GetGroupChatRequest();
        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
        request.setGroupid(mGroupId);
        String[] item = {"groupnickname", "msgsettop", "disturb"};
        request.setItems(item);
//        HttpUtils.doPost(this, Request.Path.GROUP_GROUPCHATSETTING, request, true, Request.Code.GROUP_GROUPCHATSETTING, this);
    }

    private void setGroupChatSetting() {//修改群设置
        SetGroupChatRequest request = new SetGroupChatRequest();
        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
        request.setGroupid(mGroupId);
        SetGroupChatItem items = new SetGroupChatItem();
        items.setGroupnickname(mGroupNike.getText().toString());
        items.setDisturb(messageFree.isChecked());
        items.setMsgsettop(chatToTop.isChecked());
        items.setShownick(showGroupName.isChecked());
        request.setItems(items);
//        HttpUtils.doPost(this, Request.Path.GROUP_SETGROUPCHAT, request, false, Request.Code.GROUP_SETGROUPCHAT, this);
    }

    private void getMemberList() {//得到所有群成员列表

        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("groupId",mGroupId);
        HttpUtils.doPostFormMap(this, Request.Path.GROUP_MEMBERLIST, map, false, Request.Code.GROUP_MEMBERLIST, this);
    }

    private void addGroupList() {//保存到群通讯录
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("groupId",mGroupId);
        HttpUtils.doPostFormMap(this, Request.Path.GROUP_ADDTOGROUPLIST, map, true, Request.Code.GROUP_ADDTOGROUPLIST, this);
    }

    private void moveGroupList() {//从群通讯录中移除
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("groupId",mGroupId);
        HttpUtils.doPostFormMap(this, Request.Path.GROUP_MOVEFROMGROUPLIST, map, true, Request.Code.GROUP_MOVEFROMGROUPLIST, this);
    }


    private void exitGroupList() {//退群

        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("groupId",mGroupId);
        HttpUtils.doPostFormMap(this, Request.Path.GROUP_EXITGROUP, map, true, Request.Code.GROUP_EXITGROUP, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.GROUP_GROUPCHATSETTING:
                GroupChatSetting items = (GroupChatSetting) o;
                mGroupNike.setText(items.getGroupnickname());
                chatToTop.setChecked(items.isMsgsettop());
                messageFree.setChecked(items.isDisturb());
                break;
            case Request.Code.GROUP_SETGROUPCHAT:

                break;
            case Request.Code.GROUP_MEMBERLIST:
                MemberList mMemberList = (MemberList) o;

//                RequestTime requestTime = LWGroupMemberManager.getInstance().getRequestTime(LWDBManager.getInstance().getUserInfo().getUid());
//                if (requestTime == null) {
//                    requestTime = new RequestTime();
//                    requestTime.setUserid(LWDBManager.getInstance().getUserInfo().getUid());
//                }
                if (mMemberList.getMembers() != null && mMemberList.getMembers().size() != 0) {
                    LWGroupMemberManager.getInstance().addGroupMember(mMemberList.getMembers(), mGroupId);
//                    requestTime.setGroupmembertime(mMemberList.getTimestampnow() + 1);
//                    LWGroupMemberManager.getInstance().updateRequest(requestTime);
                }


                break;
            case Request.Code.GROUP_GROUPPROFILE:
                mGroupProfile = (GroupProfile) o;
                if (mGroupProfile.getName().length() > 0) {
                    tvGroupName.setText(mGroupProfile.getName());
                }
                if (mGroupProfile != null && mGroupProfile.getOwner_id().equals(LWDBManager.getInstance().getUserInfo().getUid())) {
                    mAdapter.setGroupOwer(true);
                    mAdapter.notifyDataSetChanged();
                }
                saveGroupChat.setCheckedImmediatelyNoEvent(mGroupProfile.isHasCollect());
                break;
            case Request.Code.GROUP_SETGROUPPROFILE:
//                mConversation.setUsername();
                GroupItem groupItem = LWFriendManager.getInstance().getGroupById(mGroupId);
                groupItem.setName(mConversation.getUsername());
                LWFriendManager.getInstance().addGroup(groupItem);
                Toast.makeText(this, "群名称修改成功", Toast.LENGTH_SHORT).show();
                break;
            case Request.Code.GROUP_ADDTOGROUPLIST:
                Toast.makeText(this, "添加到群聊成功", Toast.LENGTH_SHORT).show();
                break;
            case Request.Code.GROUP_MOVEFROMGROUPLIST:
//                LWFriendManager.getInstance().deleteGroup(LWFriendManager.getInstance().getGroupById(mGroupId));
                Toast.makeText(this, "成功从群聊中移除", Toast.LENGTH_SHORT).show();
                break;
            case Request.Code.GROUP_EXITGROUP:
                LWJNIManager.getInstance().deleteGroupMenber(mGroupId, LWUserManager.getInstance().getUserInfo().getUid());
                LWConversationManager.getInstance().deletConversationById(mGroupId);
                LWConversationManager.getInstance().deleteByFid(mGroupId);
                LWFriendManager.getInstance().deleteGroup(LWFriendManager.getInstance().getGroupById(mGroupId));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(GroupChatDetailActivity.this, MainActivity.class));
                        GroupChatDetailActivity.this.finish();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.GROUP_GROUPCHATSETTING:
                type = new TypeToken<BaseResponse<GroupChatSetting>>() {
                }.getType();
                break;
            case Request.Code.GROUP_SETGROUPCHAT:
                type = new TypeToken<BaseResponse<SetGroupProfile>>() {
                }.getType();
                break;
            case Request.Code.GROUP_MEMBERLIST:
                type = new TypeToken<BaseResponse<MemberList>>() {
                }.getType();
                break;
            case Request.Code.GROUP_GROUPPROFILE:
                type = new TypeToken<BaseResponse<GroupProfile>>() {
                }.getType();
                break;
            case Request.Code.GROUP_SETGROUPPROFILE:
                type = new TypeToken<BaseResponse<SetGroupProfile>>() {
                }.getType();
                break;
            case Request.Code.GROUP_ADDTOGROUPLIST:
                type = new TypeToken<BaseResponse<Object>>() {
                }.getType();
                break;
            case Request.Code.GROUP_MOVEFROMGROUPLIST:
                type = new TypeToken<BaseResponse<Object>>() {
                }.getType();
                break;
            case Request.Code.GROUP_EXITGROUP:
                type = new TypeToken<BaseResponse<Object>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }

    private void changeGroupNike() {//修改我在群里的昵称群昵称
        EditCenterDialog ipConfigWidget = new EditCenterDialog("我在本群的昵称", "设置你在群里的昵称，这个昵称只会在这个群内显示");
        ipConfigWidget.showIpConfigDialog(GroupChatDetailActivity.this);
        ipConfigWidget.setOnButtonClickListener(new EditCenterDialog.OnButtonClickListener() {
            @Override
            public void onConfirmBtnClick(AlertDialog dialog, String edit) {
                mGroupNike.setText(edit);
                setGroupChatSetting();
                dialog.dismiss();
            }

            @Override
            public void onCancelBtnClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        });
    }

    private void changeGroupName() {//群主修改群昵称
        EditCenterDialog ipConfigWidget = new EditCenterDialog("群聊名称", "修改群聊名称");
        ipConfigWidget.showIpConfigDialog(GroupChatDetailActivity.this);
        ipConfigWidget.setOnButtonClickListener(new EditCenterDialog.OnButtonClickListener() {
            @Override
            public void onConfirmBtnClick(AlertDialog dialog, String edit) {
                tvGroupName.setText(edit);
                SetGroupProfileItem items = new SetGroupProfileItem();
                items.setName(edit);
                setGroupInfo(items);
                mConversation.setUsername(edit);
                dialog.dismiss();
            }

            @Override
            public void onCancelBtnClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        });
    }

    private void emptyChatRecord() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_bottom, null);
        //初始化控件
        TextView mTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        mTitle.setText("将清空群聊的聊天记录");
        TextView mEmptyChat = (TextView) inflate.findViewById(R.id.empty_chat);
        mEmptyChat.setText("清空聊天记录");
        mEmptyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                LWConversationManager.getInstance().deleteByFid(mGroupId);
                LWChatManager.getInstance().notifyClearMessage(mGroupId);
            }
        });
        TextView mCencel = (TextView) inflate.findViewById(R.id.cancel);
        mCencel.setText("取消");
        mCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LWConversationManager.getInstance().insertOrReplaceGroupChat(mConversation);
    }
}
