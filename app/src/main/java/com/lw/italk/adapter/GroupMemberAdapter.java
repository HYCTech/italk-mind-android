package com.lw.italk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.activity.AddNetFriendActivity;
import com.lw.italk.activity.SendChatActivity;
import com.lw.italk.framework.base.ViewHolder;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.greendao.model.GroupMember;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.GlideCircleTransform;

import java.util.List;

/**
 * Created by 喜明 on 2018/9/1.
 */

public class GroupMemberAdapter extends BaseAdapter {

    private Context mContext;
    private List<GroupMember> mDate;

    public GroupMemberAdapter(Context context, List<GroupMember> date) {
        mContext = context;
        mDate = date;
    }

    @Override
    public int getCount() {
        return mDate.size();
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
        GroupMember mGroupMember = mDate.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_contact_item, null);
        }

        ImageView ivAvatar = ViewHolder.get(convertView, R.id.img);
        TextView tvCatalog = ViewHolder.get(convertView, R.id.text);

        Glide.with(mContext).load(mGroupMember.getImage())
                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                .transform(new GlideCircleTransform(App.getInstance()))
                .into(ivAvatar);
        tvCatalog.setText(mGroupMember.getNickname());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(LWDBManager.getInstance().queryFriendItem( mDate.get(position).getUserid()) == null){
                    intent = new Intent(mContext, AddNetFriendActivity.class);
                }else{
                    intent = new Intent(mContext, SendChatActivity.class);
                }
                System.out.println("---------GroupMemberAdapter------->" );
                intent.putExtra(Constants.NAME, mDate.get(position).getNickname());
                intent.putExtra(Constants.HEADURL, mDate.get(position).getImage());
                intent.putExtra(Constants.PHONE, mDate.get(position).getUserid());
                intent.putExtra(Constants.User_ID, mDate.get(position).getUserid());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
