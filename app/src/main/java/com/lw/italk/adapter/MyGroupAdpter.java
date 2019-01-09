package com.lw.italk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.activity.ChatActivity;
import com.lw.italk.activity.MyGroupActivity;
import com.lw.italk.entity.LWConversation;
import com.lw.italk.framework.base.ViewHolder;
import com.lw.italk.framework.common.LWChatManager;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.PinyinComparator;
import com.lw.italk.greendao.model.User;
import com.lw.italk.gson.group.GroupItem;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.GlideCircleTransform;
import com.lw.italk.utils.Utils;

import java.util.Collections;
import java.util.List;

public class MyGroupAdpter extends BaseAdapter {
    protected Activity context;
    private List<GroupItem> grouplist;

    public MyGroupAdpter(Activity ctx, List<GroupItem> grouplist) {
        context = ctx;
        this.grouplist = grouplist;
//		Collections.sort(grouplist, new PinyinComparator());
    }

    @Override
    public int getCount() {
        return grouplist == null? 0 : grouplist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_item_mygroup, parent, false);
        }
        final GroupItem group = grouplist.get(position);
        ImageView img_avar = ViewHolder.get(convertView, R.id.img_photo);
        TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
//		img_avar.setImageResource(R.mipmap.head);
        Glide.with(context).load(group.getFaceurl())
                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                .transform(new GlideCircleTransform(App.getInstance()))
                .into(img_avar);
        if(group.getName() == null || group.getName().length() == 0) {
            txt_name.setText("群聊");
        }else {
            txt_name.setText(group.getName());
        }
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(Constants.NAME, group.getName());
                intent.putExtra(Constants.TYPE, LWConversationManager.CHATTYPE_GROUP);
                intent.putExtra(Constants.GROUP_ID, group.getGroupid());
                context.startActivity(intent);
                context.finish();
            }
        });
        return convertView;
    }
}
