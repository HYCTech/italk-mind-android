package com.lw.italk.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.activity.AddGroupChatActivity;
import com.lw.italk.activity.AddNetFriendActivity;
import com.lw.italk.activity.SendChatActivity;
import com.lw.italk.framework.base.ViewHolder;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWGroupMemberManager;
import com.lw.italk.greendao.model.GroupMember;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.GlideCircleTransform;

import java.util.List;

/**
 * Created by 喜明 on 2018/9/1.
 */

public class GroupMemberDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<GroupMember> mDate;
    private String mGroupId;
    private boolean isGroupOwer = false;

    public GroupMemberDetailAdapter(Context context, List<GroupMember> date) {
        mContext = context;
        mDate = date;
    }
    public void setmGroupId(String groupId) {
        mGroupId = groupId;
    }

    public void setGroupOwer(boolean groupOwer) {
        isGroupOwer = groupOwer;
    }

    @Override
    public int getCount() {
        return mDate.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return mDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_contact_item, null);
        }

        ImageView ivAvatar = ViewHolder.get(convertView, R.id.img);
        TextView tvCatalog = ViewHolder.get(convertView, R.id.text);

        if(position == mDate.size()){
            Glide.with(mContext).load(R.drawable.add_member)
                    .transform(new GlideCircleTransform(App.getInstance()))
                    .into(ivAvatar);
        }else{
            Glide.with(mContext).load(mDate.get(position).getImage())
                    .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                    .transform(new GlideCircleTransform(App.getInstance()))
                    .into(ivAvatar);
            tvCatalog.setText(mDate.get(position).getNickname());
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mDate.size()) {
                    Intent intent = null;
                    if(LWDBManager.getInstance().queryFriendItem( mDate.get(position).getUserid()) == null){
                        intent = new Intent(mContext, AddNetFriendActivity.class);
                    }else{
                        intent = new Intent(mContext, SendChatActivity.class);
                    }
                    System.out.println("---------GroupMemberDetailAdapter------->" );
                    intent.putExtra(Constants.NAME, mDate.get(position).getNickname());
                    intent.putExtra(Constants.HEADURL, mDate.get(position).getImage());
                    intent.putExtra(Constants.PHONE, mDate.get(position).getUserid());
                    intent.putExtra(Constants.User_ID, mDate.get(position).getUserid());
                    mContext.startActivity(intent);
                } else {
                    List<GroupMember> allGroup= LWGroupMemberManager.getInstance().getAllGroupMember(mGroupId);
                    String[] allGroupId = new String[allGroup.size()];
                    for(int i = 0 ; i < allGroup.size(); i++){
                        allGroupId[i] = allGroup.get(i).getUserid();
                    }
                    Intent intent1 = new Intent(mContext,AddGroupChatActivity.class );
                    intent1.putExtra(Constants.TYPE, Constants.ADD_GROUP_MEMBER);
                    intent1.putExtra(Constants.GROUP_ID, mGroupId);
                    Bundle bundle = new Bundle();
                    bundle.putStringArray(Constants.ADD_MEMBER, allGroupId);
                    //在详情中点击添加好友发起群聊，需要将现有的好友ID传过去
                    intent1.putExtras(bundle);
                    mContext.startActivity(intent1);
                }
            }
        });

        return convertView;
    }
}
