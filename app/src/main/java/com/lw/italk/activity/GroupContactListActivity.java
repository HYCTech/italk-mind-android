package com.lw.italk.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lw.italk.R;
import com.lw.italk.adapter.GroupMemberAdapter;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWGroupMemberManager;
import com.lw.italk.greendao.model.GroupMember;
import com.lw.italk.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 所有群成员列表
 * Created by 喜明 on 2018/9/1.
 */

public class GroupContactListActivity extends BaseActivity {
    @BindView(R.id.group_contact_list)
    GridView groupContactList;
    @BindView(R.id.left_bar_item)
    TextView mLeftBarItem;
    @BindView(R.id.center_bar_item)
    TextView mCenterBarItem;
    @BindView(R.id.right_title_bar)
    TextView mRightTitleBar;
    @BindView(R.id.title_bar_right)
    RelativeLayout titleBarRight;
    @BindView(R.id.title_bar_left)
    LinearLayout titleBarLeft;

    @BindView(R.id.img_right)
    ImageView mRightImage;

    private GroupMemberAdapter adapter;
    private String mGroupId;

    private List<GroupMember> memberList;

    @Override
    protected int setContentView() {
        return R.layout.activity_group_contact_list;
    }

    @Override
    protected void initView() {
        mLeftBarItem.setText("返回");
        mRightTitleBar.setVisibility(View.GONE);
        mRightImage.setVisibility(View.VISIBLE);
        mRightImage.setImageResource(R.mipmap.icon_more);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume(){
        super.onResume();

        memberList = LWGroupMemberManager.getInstance().getAllGroupMember(mGroupId);
        adapter = new GroupMemberAdapter(this, memberList);
        groupContactList.setAdapter(adapter);
        groupContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


            }
        });
        mCenterBarItem.setText("群成员(" + memberList.size() + ")");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGroupId = getIntent().getStringExtra(Constants.GROUP_ID);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.left_bar_item,R.id.img_right})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id){
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.img_right:
                selectAddAndDelete();
                break;
        }

    }

    private void selectAddAndDelete() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_bottom, null);
        //初始化控件
        TextView mTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        mTitle.setText("添加群成员");
        mTitle.setTextSize(16);
        mTitle.setTextColor(Color.BLACK);
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                List<GroupMember> allGroup= LWGroupMemberManager.getInstance().getAllGroupMember(mGroupId);
                String[] allGroupId = new String[allGroup.size()];
                for(int i = 0 ; i < allGroup.size(); i++){
                    allGroupId[i] = allGroup.get(i).getUserid();
                }
                Intent intent1 = new Intent(GroupContactListActivity.this,AddGroupChatActivity.class );
                intent1.putExtra(Constants.TYPE, Constants.ADD_GROUP_MEMBER);
                intent1.putExtra(Constants.GROUP_ID, mGroupId);
                Bundle bundle = new Bundle();
                bundle.putStringArray(Constants.ADD_MEMBER, allGroupId);
                //在详情中点击添加好友发起群聊，需要将现有的好友ID传过去
                intent1.putExtras(bundle);
                GroupContactListActivity.this.startActivity(intent1);
            }
        });
        TextView mEmptyChat = (TextView) inflate.findViewById(R.id.empty_chat);
        mEmptyChat.setText("移除群成员");
        mEmptyChat.setTextSize(16);
        mEmptyChat.setTextColor(Color.RED);
        mEmptyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                List<GroupMember> allGroup= LWGroupMemberManager.getInstance().getAllGroupMember(mGroupId);
                String[] allGroupId = new String[allGroup.size()];
                for(int i = 0 ; i < allGroup.size(); i++){
                    allGroupId[i] = allGroup.get(i).getUserid();
                }
                Intent intent1 = new Intent(GroupContactListActivity.this,AddGroupChatActivity.class );
                intent1.putExtra(Constants.TYPE, Constants.DEL_GROUP_MEMBER);
                intent1.putExtra(Constants.GROUP_ID, mGroupId);
                Bundle bundle = new Bundle();
                bundle.putStringArray(Constants.ADD_MEMBER, allGroupId);
                //在详情中点击添加好友发起群聊，需要将现有的好友ID传过去
                intent1.putExtras(bundle);
                GroupContactListActivity.this.startActivity(intent1);
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
}
