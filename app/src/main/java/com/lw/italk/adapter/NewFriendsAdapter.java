package com.lw.italk.adapter;

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
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.gson.friend.GetAddFrienditem;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.GlideCircleTransform;
import com.lw.italk.activity.AcceptFriendActivity;
import com.lw.italk.framework.base.ViewHolder;

import java.util.List;


public class NewFriendsAdapter extends BaseAdapter {
	protected Context mContext;
	private List<GetAddFrienditem> mData;
	private OnButtonClickListener mListener;

	public NewFriendsAdapter(Context ctx, OnButtonClickListener listener) {
        mContext = ctx;
		this.mListener = listener;
	}

	public void setParam(List<GetAddFrienditem> items){
		mData = items;
        notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData == null? 0 : mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.layout_item_newfriend, parent, false);
		}
		ImageView img_avar = ViewHolder.get(convertView, R.id.img_photo);
		TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
		final TextView txt_add = ViewHolder.get(convertView, R.id.txt_add);
		//final Contact user = GloableParams.contactInfos.get(position);
		GetAddFrienditem addFriend = mData.get(position);
		txt_name.setText(mData.get(position).getNickname());
        Glide.with(mContext).load(mData.get(position).getAvatar())
                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                .transform(new GlideCircleTransform(App.getInstance()))
                .into(img_avar);

        if (addFriend.getStatus() == 1){
			txt_add.setText("待验证");
			txt_add.setEnabled(false);
			txt_add.setTextColor(mContext.getResources().getColor(
					R.color.blue));
			txt_add.setBackgroundResource(R.drawable.btn_bg_gray1);
		}else if(addFriend.getStatus() == 2){
			txt_add.setTextColor(mContext.getResources().getColor(
					R.color.black1));
			txt_add.setBackgroundResource(R.drawable.btn_bg_gray1);
			txt_add.setText("已添加");
			txt_add.setEnabled(false);
		}else if(addFriend.getStatus() == 3){
			txt_add.setEnabled(true);
			txt_add.setText("接收");
			txt_add.setBackgroundResource(R.drawable.btn_bg_green);
			txt_add.setTextColor(mContext.getResources().getColor(
					R.color.white));
		}
		else if(addFriend.getStatus() == 4){
			txt_add.setText("被拒绝");
			txt_add.setEnabled(false);
			txt_add.setTextColor(mContext.getResources().getColor(
					R.color.black1));
			txt_add.setBackgroundResource(R.drawable.btn_bg_gray1);
		}
		txt_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_add.setTextColor(mContext.getResources().getColor(
						R.color.black1));
				txt_add.setBackgroundResource(R.drawable.btn_bg_gray1);
				txt_add.setText("已添加");
				mListener.onButtonClilk(mData.get(position));
//				try {
//					EMContactManager.getInstance().addContact(
//							user.getTelephone(), "请求添加你为朋友");
//				} catch (EaseMobException e) {
//					e.printStackTrace();
//				}
			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (addFriend.getStatus() == 3){
//					Intent intent = new Intent(mContext, AcceptFriendActivity.class);
//					intent.putExtra(Constants.NAME, addFriend.getNickname());
//					intent.putExtra(Constants.HEADURL, addFriend.getAvatar());
////					intent.putExtra(Constants.PHONE, addFriend.);
//					intent.putExtra(Constants.User_ID, addFriend.getUserid());
//					mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	public interface OnButtonClickListener{
		void onButtonClilk(GetAddFrienditem frienditem);
	}
}
