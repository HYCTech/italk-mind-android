package com.lw.italk.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kyleduo.switchbutton.SwitchButton;
import com.lw.italk.App;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWChatManager;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.GlideCircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 喜明 on 2018/9/1.
 */

public class ContactChatDetailActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
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
    @BindView(R.id.user_head_img)
    ImageView contactHeadImg;
    @BindView(R.id.add_to_friend)
    ImageView addToFriend;
    @BindView(R.id.chat_to_top)
    SwitchButton chatToTop;
    @BindView(R.id.add_friend_verification)
    RelativeLayout addFriendVerification;
    @BindView(R.id.message_free)
    SwitchButton messageFree;
    @BindView(R.id.clear_local_chat)
    TextView clearLocalChat;

    private String mGroupId = "";
    private Contact mDate;//单聊的好友
    private Conversation mConversation;

    @Override
    protected int setContentView() {
        return R.layout.activity_contact_chat_detail;
    }

    @Override
    protected void initView() {
        mLeftBarItem.setText("返回");
        mCenterBarItem.setText("聊天信息");
        mRightTitleBar.setVisibility(View.GONE);
        chatToTop.setOnCheckedChangeListener(this);
        messageFree.setOnCheckedChangeListener(this);
        mDate = LWFriendManager.getInstance().queryFriendItem(mGroupId);
    }

    @Override
    protected void initData() {
        if(mDate != null){
            Glide.with(this).load(mDate.getAvatar())
                    .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                    .transform(new GlideCircleTransform(App.getInstance()))
                    .into(contactHeadImg);
        }
        mConversation = LWConversationManager.getInstance().getConByLocalId(mGroupId);
        chatToTop.setChecked(mConversation.getIsGroup());
        messageFree.setChecked(mConversation.getDisturb());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGroupId = getIntent().getStringExtra(Constants.GROUP_ID);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.left_bar_item, R.id.user_head_img, R.id.add_to_friend, R.id.clear_local_chat})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.user_head_img:
                if(mDate != null){
                    System.out.println("---------ContactChatDetailActivity------->" );
                    intent.setClass(this, SendChatActivity.class);
                    intent.putExtra(Constants.NAME, mDate.getRemark());
                    intent.putExtra(Constants.HEADURL, mDate.getAvatar());
                    intent.putExtra(Constants.PHONE, mDate.getUid());
                    intent.putExtra(Constants.User_ID, mDate.getUid());
                    startActivity(intent);
                }

                break;
            case R.id.add_to_friend:
                String[] allGroupId = {mGroupId};
                intent.setClass(this, AddGroupChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray(Constants.ADD_MEMBER, allGroupId);
                //在好友详情中点击添加好友发起群聊，需要将现有的好友ID传过去
                intent.putExtras(bundle);
                intent.putExtra(Constants.TYPE, Constants.ADD_CONTACT_MEMBER);
                startActivity(intent);
                break;
            case R.id.clear_local_chat:
                emptyChatRecord();
                break;
        }
    }

    private void emptyChatRecord() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_bottom, null);
        //初始化控件
        TextView mTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        mTitle.setText("将清空聊天记录");
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
        if(dialogWindow == null){
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
//        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.chat_to_top:
                mConversation.setMsgsettop(isChecked);
                break;
            case R.id.message_free:
                mConversation.setDisturb(isChecked);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LWConversationManager.getInstance().insertOrReplaceGroupChat(mConversation);
    }
}
