package com.lw.italk.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.framework.common.LWDownloadManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.utils.Constants;
import com.lw.italk.activity.SendChatActivity;
import com.lw.italk.framework.base.ViewHolder;
import com.lw.italk.greendao.model.PingYinUtil;
import com.lw.italk.greendao.model.PinyinComparator;
import com.lw.italk.greendao.model.User;
import com.lw.italk.utils.GlideCircleTransform;

import java.util.Collections;
import java.util.List;

public class ContactAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private List<Contact> mContactList;// 好友信息

	public ContactAdapter(Context mContext, List<Contact> contactList) {
		this.mContext = mContext;
		this.mContactList = contactList;
		// 排序(实现了中英文混排)
		Collections.sort(mContactList, new PinyinComparator());
	}

	public void setData(List<Contact> contactList) {
		mContactList = contactList;
		// 排序(实现了中英文混排)
		Collections.sort(mContactList, new PinyinComparator());
	}

	@Override
	public int getCount() {
		if (mContactList == null) {
			return 0;
		}
		return mContactList.size();
	}

	@Override
	public Object getItem(int position) {
		return mContactList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Contact contact = mContactList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.contact_item, null);

		}
		ImageView ivAvatar = ViewHolder.get(convertView,
				R.id.contactitem_avatar_iv);
		TextView tvCatalog = ViewHolder.get(convertView,
				R.id.contactitem_catalog);
		TextView tvNick = ViewHolder.get(convertView, R.id.contactitem_nick);
        String catalog = "";
		String nickName = contact.getRemark();

		if (TextUtils.isEmpty(nickName)) {
            nickName = "匿名";
            catalog = "#";
        } else {
            catalog = PingYinUtil.converterToFirstSpell(nickName)
                    .substring(0, 1).toUpperCase();
        }
		if (position == 0) {
			tvCatalog.setVisibility(View.VISIBLE);
			tvCatalog.setText(catalog);
		} else {
			Contact nextContact = mContactList.get(position - 1);
            String lastCatalog = "";
            if (TextUtils.isEmpty(nextContact.getRemark())) {
                lastCatalog = "#";
            } else {
                lastCatalog = PingYinUtil.converterToFirstSpell(nextContact.getRemark())
                        .substring(0, 1).toUpperCase();
            }
//			String lastCatalog = PingYinUtil.converterToFirstSpell(
//					nextContact.getNickname()).substring(0, 1);
			if (catalog.equals(lastCatalog)) {
				tvCatalog.setVisibility(View.GONE);
			} else {
				tvCatalog.setVisibility(View.VISIBLE);
				tvCatalog.setText(catalog);
			}
		}
		Log.e("123qwe", "contact.getLocalimage():" + contact.getLocalimage());
//		if ( !TextUtils.isEmpty(contact.getLocalimage())) {
//            Glide.with(mContext).load(contact.getLocalimage())
//                    .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
//                    .transform(new GlideCircleTransform(App.getInstance()))
//                    .into(ivAvatar);
//		} else {
            Glide.with(mContext).load(contact.getAvatar())
                    .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                    .transform(new GlideCircleTransform(App.getInstance()))
                    .into(ivAvatar);
//		}
		tvNick.setText(nickName);

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("---------ContactAdapter------->" );
				Intent intent = new Intent(mContext, SendChatActivity.class);
				intent.putExtra(Constants.NAME, mContactList.get(position).getRemark());
				intent.putExtra(Constants.HEADURL, mContactList.get(position).getAvatar());
				intent.putExtra(Constants.PHONE, mContactList.get(position).getUid());
				intent.putExtra(Constants.User_ID, mContactList.get(position).getUid());
                mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < mContactList.size(); i++) {
			Contact contact = mContactList.get(i);
			String l = PingYinUtil.converterToFirstSpell(contact.getUsername())
					.substring(0, 1);
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
