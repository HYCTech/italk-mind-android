package com.lw.italk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.gson.friend.GetAddFriendList;
import com.lw.italk.gson.friend.GetAddFrienditem;
import com.lw.italk.gson.friend.VerifyFriend;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.AcceptFriendRequest;
import com.lw.italk.http.model.GetAddFriendListRequest;
import com.lw.italk.utils.Utils;
import com.lw.italk.adapter.NewFriendsAdapter;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.zxing.CaptureActivity;
import com.nostra13.universalimageloader.utils.L;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import flyn.Eyes;


/*
* 通讯录中 新的朋友 界面
**/
public class NewFriendsListActivity extends BaseActivity implements View.OnClickListener, Response {
	private TextView txt_title, txt_right, txt_left;
	private ImageView img_back, img_right;
	private ListView mlistview;
	private View layout_head;
	private NewFriendsAdapter mAdapter;

	@Override
	protected int setContentView() {
		return R.layout.activity_listview;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Eyes.translucentStatusBar(this, true);
	}

	@Override
	protected void initView() {
        txt_left = (TextView) findViewById(R.id.left_bar_item);
        txt_left.setText("通讯录");
        txt_left.setOnClickListener(this);
		txt_title = (TextView) findViewById(R.id.center_bar_item);
		txt_title.setText("新的朋友");
		txt_right = (TextView) findViewById(R.id.right_title_bar);
		txt_right.setText("");
		txt_right.setBackground(getResources().getDrawable(R.drawable.icon_menu_sao));
        txt_right.setOnClickListener(this);
		mlistview = (ListView) findViewById(R.id.listview);
		layout_head = getLayoutInflater().inflate(
				R.layout.layout_head_newfriend, null);
		mlistview.addHeaderView(layout_head);
        mAdapter = new NewFriendsAdapter(this, new NewFriendsAdapter.OnButtonClickListener(){

            @Override
            public void onButtonClilk(GetAddFrienditem frienditem) {
//                Contact contact = new Contact();
//                contact.setUsername(frienditem.getNickname());
//                contact.setAvatar(frienditem.getAvatar());
//                contact.setJid(LWFriendManager.USERID);
//                contact.setUid(frienditem.getUserid());
//                LWFriendManager.getInstance().addContact(contact);
//                LWJNIManager.getInstance().sendFriendVerify(frienditem.getUserid());

				HashMap<String,String> map = new HashMap<String,String>();
				map.put("tokenId", LWUserManager.getInstance().getToken());
				map.put("uid",frienditem.getUserid());
				map.put("followAuthId", frienditem.getFollowAuthId());
				map.put("isAgreed", String.valueOf(true));
				HttpUtils.doPostFormMap(NewFriendsListActivity.this, Request.Path.FRIEND_VERIFY_FRIEND, map, true, Request.Code.FRIEND_VERIFY_FRIEND, new Response<Object>() {

					@Override
					public void next(Object o, int requestCode) {
						frienditem.setStatus(2);
						List<MsgItem> msgItemList = new ArrayList<>();
						MsgItem item11 = new MsgItem();
						item11.setText("您已添加"+frienditem.getNickname()+"为好友");
						item11.setChattype(LWConversationManager.CHATTYPE_SINGLE);
						item11.setBussinesstype(LWConversationManager.ADD_FRIEND_RESPONSE);
						item11.setFid(LWUserManager.getInstance().getUserInfo().getUid());
						item11.setUserid(frienditem.getUserid());
						long time = System.currentTimeMillis()/1000;//date.getTime();
						item11.setTimestamp(time);
						item11.setLocalid(time);
						item11.setMsgid(time);
						msgItemList.add(item11);
						LWDBManager.getInstance().insertOrReplaceUnreadMsg(msgItemList);
						LWDBManager.getInstance().updateAddFriendStatus(frienditem);
					}

					@Override
					public void error(ResponseErrorException t, int requestCode) {
						Toast.makeText(NewFriendsListActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
						mAdapter.notifyDataSetChanged();
					}

					@Override
					public Type getTypeToken(int requestCode) {
						Type type = new TypeToken<BaseResponse<VerifyFriend>>() {
						}.getType();
						return type;
					}
				});
            }
        });
		mlistview.setAdapter(mAdapter);
	}

	@Override
	protected void initData() {
		layout_head.findViewById(R.id.txt_search).setOnClickListener(this);
		layout_head.findViewById(R.id.txt_tel).setOnClickListener(this);

        getAddFriendList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_bar_item:
			Utils.finish(NewFriendsListActivity.this);
			break;
		case R.id.right_title_bar:
            Utils.start_Activity(NewFriendsListActivity.this, CaptureActivity.class);
			break;
		case R.id.txt_search:
			Utils.start_Activity(this, SearchNetContactActivity.class);
			break;
		case R.id.txt_tel:
			Utils.start_Activity(this, AddLocalContactActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getAddFriendList();
	}

	private void getAddFriendList() {
//        GetAddFriendListRequest request = new GetAddFriendListRequest();
//        request.setTimestamp(0);
//        request.setPage(0);
//        request.setFrom_account(LWDBManager.getInstance().getUserInfo().getUid());
//        request.setPagecount(1000);
//        HttpUtils.doPost(this, Request.Path.FRIEND_GETADDFRIENDLIST, request, true, Request.Code.FRIEND_GETADDFRIENDLIST, this);
		List<GetAddFrienditem> items = LWDBManager.getInstance().selectAllAddFriends();
		mAdapter.setParam(items);
    }


    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.FRIEND_GETADDFRIENDLIST:
                List<GetAddFrienditem> items = ((GetAddFriendList) o).getItems();
                mAdapter.setParam(items);
                break;
			case Request.Code.FRIEND_VERIFY_FRIEND:
				VerifyFriend item = (VerifyFriend)o;
				//保存到db

				Utils.showLongToast(this, "添加好友成功!");
				break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {

    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.FRIEND_GETADDFRIENDLIST:
                type = new TypeToken<BaseResponse<GetAddFriendList>>() {
                }.getType();
                break;
			case Request.Code.FRIEND_VERIFY_FRIEND:
				type = new TypeToken<BaseResponse<VerifyFriend>>() {
				}.getType();
				break;
            default:
                break;
        }
        return type;
    }
}
