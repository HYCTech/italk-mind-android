package com.lw.italk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kyleduo.switchbutton.SwitchButton;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.framework.base.ViewHolder;
import com.lw.italk.greendao.model.BlackList;
import com.lw.italk.greendao.model.BlackListComparator;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.PingYinUtil;
import com.lw.italk.greendao.model.PinyinComparator;
import com.lw.italk.greendao.model.User;
import com.lw.italk.utils.GlideCircleTransform;

import java.util.Collections;
import java.util.List;

public class BlackFriendListAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private List<BlackList> UserInfos;// 好友信息
	private OnCheckBoxClickListener mListener;

	public BlackFriendListAdapter(Context mContext, List<BlackList> UserInfos, OnCheckBoxClickListener listener) {
		this.mContext = mContext;
		this.UserInfos = UserInfos;
		this.mListener = listener;
		// 排序(实现了中英文混排)
		Collections.sort(UserInfos, new BlackListComparator());
	}

	public void setParam(List<BlackList> UserInfos){
		this.UserInfos = UserInfos;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return UserInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return UserInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final BlackList user = UserInfos.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.black_friend_item, null);

		}
		ImageView ivAvatar = ViewHolder.get(convertView,
				R.id.contactitem_avatar_iv);
		TextView tvCatalog = ViewHolder.get(convertView,
				R.id.contactitem_catalog);
		TextView tvNick = ViewHolder.get(convertView, R.id.contactitem_nick);
		SwitchButton tvAccept = ViewHolder.get(convertView, R.id.check_box);
		tvAccept.setChecked(true);

		String catalog = PingYinUtil.converterToFirstSpell(user.getNickname())
				.substring(0, 1);
		if (position == 0) {
			tvCatalog.setVisibility(View.VISIBLE);
			tvCatalog.setText(catalog);
		} else {
			BlackList Nextuser = UserInfos.get(position - 1);
			String lastCatalog = PingYinUtil.converterToFirstSpell(
					Nextuser.getNickname()).substring(0, 1);
			if (catalog.equals(lastCatalog)) {
				tvCatalog.setVisibility(View.GONE);
			} else {
				tvCatalog.setVisibility(View.VISIBLE);
				tvCatalog.setText(catalog);
			}
		}
		Glide.with(mContext).load(user.getImage())
				.placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
				.transform(new GlideCircleTransform(App.getInstance()))
				.into(ivAvatar);
//		ivAvatar.setImageResource(R.mipmap.head);
		tvNick.setText(user.getNickname());

		tvAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked){
					mListener.onCheckBoxClilk(user.getUserid());
				}

			}
		});
		return convertView;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < UserInfos.size(); i++) {
			BlackList user = UserInfos.get(i);
			String l = PingYinUtil.converterToFirstSpell(user.getNickname())
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

	public interface OnCheckBoxClickListener{
		void onCheckBoxClilk(String remove_id);
	}
}
