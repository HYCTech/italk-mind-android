package com.lw.italk.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.activity.AddNetFriendActivity;
import com.lw.italk.activity.FriendVerificationActivity;
import com.lw.italk.activity.SearchNetContactActivity;
import com.lw.italk.activity.SendChatActivity;
import com.lw.italk.framework.base.ViewHolder;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.PingYinUtil;
import com.lw.italk.greendao.model.PinyinComparator;
import com.lw.italk.gson.user.QueryUser;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.QueryUserRequest;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.ItalkLog;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AddLocalContactAdapter extends BaseAdapter implements SectionIndexer, Response {
	private Context mContext;
	private List<Contact> UserInfos;// 好友信息

	public AddLocalContactAdapter(Context mContext, List<Contact> UserInfos) {
		this.mContext = mContext;
		this.UserInfos = UserInfos;
		// 排序(实现了中英文混排)
		Collections.sort(UserInfos, new PinyinComparator());
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
		final Contact user = UserInfos.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.add_local_contact_item, null);

		}
		ImageView ivAvatar = ViewHolder.get(convertView,
				R.id.contactitem_avatar_iv);
		TextView tvCatalog = ViewHolder.get(convertView,
				R.id.contactitem_catalog);
		TextView tvNick = ViewHolder.get(convertView, R.id.contactitem_nick);
		TextView tvAccept = ViewHolder.get(convertView, R.id.txt_add);

		boolean temp = false;
		for(int i = 0; i < GloableParams.contactInfos.size(); i++){
			if(GloableParams.contactInfos.get(i).getUid().equals(user.getUid())){
				temp = true;
				break;
			}
		}

		if(temp){
			tvAccept.setText("已添加");
			tvAccept.setBackgroundResource(R.drawable.btn_bg_gray1);
			tvAccept.setTextColor(Color.BLACK);
			tvAccept.setEnabled(false);
		}else{
			tvAccept.setText("添加");
			tvAccept.setTextColor(Color.WHITE);
			tvAccept.setBackgroundResource(R.drawable.btn_bg_green);
			tvAccept.setEnabled(true);
		}

        tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(user.getUid())){
                    Toast.makeText(mContext, "该用户号码为空", Toast.LENGTH_SHORT).show();
                }else{
                    getQueryFriendInfo(user.getUid());
                }
            }
        });
		String catalog = "";
		String nickName = user.getRemark();
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
			Contact Nextuser = UserInfos.get(position - 1);
//			String lastCatalog = PingYinUtil.converterToFirstSpell(
//					Nextuser.getNickname()).substring(0, 1);
			String lastCatalog = "";
			if (TextUtils.isEmpty(Nextuser.getRemark())) {
				lastCatalog = "#";
			} else {
				lastCatalog = PingYinUtil.converterToFirstSpell(Nextuser.getRemark())
						.substring(0, 1).toUpperCase();
			}
			if (catalog.equals(lastCatalog)) {
				tvCatalog.setVisibility(View.GONE);
			} else {
				tvCatalog.setVisibility(View.VISIBLE);
				tvCatalog.setText(catalog);
			}
		}

		ivAvatar.setImageResource(R.mipmap.head);
		tvNick.setText(user.getRemark());
		return convertView;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < UserInfos.size(); i++) {
			Contact user = UserInfos.get(i);
			String l = PingYinUtil.converterToFirstSpell(user.getRemark())
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

	private void getQueryFriendInfo(String number) {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("tokenId", LWUserManager.getInstance().getToken());
		map.put("account", number);
		HttpUtils.doPostFormMap(mContext, Request.Path.USER_QUERYUSER, map, true, Request.Code.USER_QUERYUSER, this);
	}

	@Override
	public void next(Object o, int requestCode) {
		switch (requestCode) {
			case Request.Code.USER_QUERYUSER:
				QueryUser items = ((QueryUser) o);
				if(items.getUserExists() == 0){//用户是否存在
					Toast.makeText(mContext, "该用户不存在", Toast.LENGTH_SHORT).show();
				}else{
					Intent intent = null;
					if(LWDBManager.getInstance().queryFriendItem(items.getUserid()) == null){
						intent = new Intent(mContext, AddNetFriendActivity.class);
					}else{
						intent = new Intent(mContext, SendChatActivity.class);
					}
					System.out.println("---------AddLocalContactAdapter------->" );
					intent.putExtra(Constants.NAME, items.getNickname());
					intent.putExtra(Constants.HEADURL, items.getImage());
					intent.putExtra(Constants.PHONE, items.getUserid());
					intent.putExtra(Constants.User_ID, items.getUserid());
					mContext.startActivity(intent);
				}

				break;
			default:
				break;
		}
	}

	@Override
	public void error(ResponseErrorException t, int requestCode) {
		Toast.makeText(mContext, "errorCode："+t.getErrorCode()+","+t.getMessage(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public Type getTypeToken(int requestCode) {
		Type type = null;
		switch (requestCode) {
			case Request.Code.USER_QUERYUSER:
				type = new TypeToken<BaseResponse<QueryUser>>() {
				}.getType();
				break;
			default:
				break;
		}
		return type;
	}
}
