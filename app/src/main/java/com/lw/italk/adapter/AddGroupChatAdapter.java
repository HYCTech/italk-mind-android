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
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.framework.base.ViewHolder;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.PingYinUtil;
import com.lw.italk.greendao.model.PinyinComparator;
import com.lw.italk.utils.GlideCircleTransform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 喜明 on 2018/9/2.
 */

public class AddGroupChatAdapter extends BaseAdapter implements SectionIndexer {
    private Context mContext;
    private Bitmap[] bitmaps;
    private boolean[] isCheckedArray;
    private List<Contact> list = new ArrayList<Contact>();

    @SuppressWarnings("unchecked")
    public AddGroupChatAdapter(Context mContext, List<Contact> users) {
        this.mContext = mContext;
        this.list = users;
        bitmaps = new Bitmap[list.size()];
        isCheckedArray = new boolean[list.size()];
        // 排序(实现了中英文混排)
        Collections.sort(list, new PinyinComparator());
    }

    public boolean[] getCheckedArray(){
        return isCheckedArray;
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
                    R.layout.add_group_item, null);
        }
        ImageView ivAvatar = ViewHolder.get(convertView,
                R.id.contactitem_avatar_iv);
        TextView tvCatalog = ViewHolder.get(convertView,
                R.id.contactitem_catalog);
        TextView tvNick = ViewHolder
                .get(convertView, R.id.contactitem_nick);
        final CheckBox checkBox = ViewHolder
                .get(convertView, R.id.checkbox);
        checkBox.setVisibility(View.VISIBLE);
        String catalog = PingYinUtil.converterToFirstSpell(
                user.getRemark()).substring(0, 1).toUpperCase();
        if (position == 0) {
            tvCatalog.setVisibility(View.VISIBLE);
            tvCatalog.setText(catalog);
        } else {
            Contact Nextuser = list.get(position - 1);
            String lastCatalog = PingYinUtil.converterToFirstSpell(
                    Nextuser.getRemark()).substring(0, 1).toUpperCase();
            if (catalog.equals(lastCatalog)) {
                tvCatalog.setVisibility(View.GONE);
            } else {
                tvCatalog.setVisibility(View.VISIBLE);
                tvCatalog.setText(catalog);
            }
        }
        Glide.with(mContext).load(user.getAvatar())
                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                .transform(new GlideCircleTransform(App.getInstance()))
                .into(ivAvatar);
//			ivAvatar.setImageResource(R.mipmap.head);
        tvNick.setText(user.getRemark());

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

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < list.size(); i++) {
            Contact user = list.get(i);
            String l = PingYinUtil
                    .converterToFirstSpell(user.getRemark()).substring(0, 1);
            char firstChar = l.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}