package com.lw.italk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lw.italk.R;
import com.lw.italk.framework.base.ViewHolder;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.PinyinComparator;
import com.lw.italk.greendao.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 喜明 on 2018/8/19.
 */

public class SelectChatGroupAdapter extends BaseAdapter {
    private Context mContext;
    private boolean[] isCheckedArray;
    private Bitmap[] bitmaps;
    private boolean isCheck;//能否多选
    private List<Contact> list = new ArrayList<Contact>();

    @SuppressWarnings("unchecked")
    public SelectChatGroupAdapter(Context mContext, List<Contact> users, boolean isCheck) {
        this.mContext = mContext;
        this.list = users;
        this.isCheck = isCheck;
        bitmaps = new Bitmap[list.size()];
        isCheckedArray = new boolean[list.size()];
        // 排序(实现了中英文混排)
        Collections.sort(list, new PinyinComparator());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        final Contact user = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.select_group_item, null);
        }
        ImageView ivAvatar = ViewHolder.get(convertView,
                R.id.contactitem_avatar_iv);
        TextView tvNick = ViewHolder
                .get(convertView, R.id.contactitem_nick);
        TextView tvMemberCount = ViewHolder
                .get(convertView, R.id.member_count);
        final CheckBox checkBox = ViewHolder
                .get(convertView, R.id.checkbox);

        if (isCheck){
            checkBox.setVisibility(View.VISIBLE);
        }else{
            checkBox.setVisibility(View.GONE);
        }


        ivAvatar.setImageResource(R.mipmap.head);
        tvNick.setText(user.getUsername());

        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    isCheckedArray[position] = isChecked;
                    notifyDataSetChanged();
                }
            });
            checkBox.setChecked(isCheckedArray[position]);

        }

        return convertView;
    }
}