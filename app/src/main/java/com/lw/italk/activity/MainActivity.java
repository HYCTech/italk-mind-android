package com.lw.italk.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.google.gson.reflect.TypeToken;
import com.lw.italk.App;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.fragment.Fragment_Friends;
import com.lw.italk.fragment.Fragment_Msg;
import com.lw.italk.fragment.MyCenterFragment;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.greendao.model.UserInfo;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.UserInfoRequest;
import com.lw.italk.utils.Utils;
import com.lw.italk.view.StatusBarUtil;
import com.lw.italk.view.dialog.ActionItem;
import com.lw.italk.view.dialog.TitlePopup;
import com.lw.italk.zxing.CaptureActivity;
import com.zhang.netty_lib.NettyService;

import java.lang.reflect.Type;
import java.util.Timer;
import java.util.TimerTask;

import flyn.Eyes;

public class MainActivity extends FragmentActivity implements OnClickListener ,Response {
	private TextView txt_title;
	private ImageView img_right;
//	private WarnTipDialog Tipdialog;
	private NewMessageBroadcastReceiver msgReceiver;
	protected static final String TAG = "MainActivity";
	private TitlePopup titlePopup;
	private TextView unreaMsgdLabel;// 未读消息textview
	private TextView unreadAddressLable;// 未读通讯录textview
//	private TextView unreadFindLable;// 发现
	private Fragment[] fragments;
	public Fragment_Msg homefragment;
	private MyCenterFragment myCenterFragment;
	private Fragment_Friends contactlistfragment;
//	private Fragment_Dicover findfragment;
//	private Fragment_Profile profilefragment;
	private ImageView[] imagebuttons;
	private TextView[] textviews;
	private String connectMsg = "";;
	private int index;
	private int currentTabIndex;// 当前fragment的index
	//加载中
	private ProgressDialog progressDialog;
	private ServiceConnection connection;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		App.getInstance2().addActivity(this);
        getUserInfo();
		findViewById();
		initViews();
		initTabView();
		// initVersion();
		setOnListener();
		initPopWindow();
		initReceiver();
		onBindService();
		Eyes.translucentStatusBar(this, true);
	}
	public void onBindService() {
		if (connection != null) {
			unbindService(connection);
		}
		connection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
//				nettyService = ((NettyService.NettyBinder) service).getService();
//				myService.setOnServiceProgressChangedListener(ServiceActivity.this);
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
//				HolaPrint.pr("service disconnected.");
			}
		};
		Intent i = new Intent(this, NettyService.class);
		bindService(i, connection, BIND_AUTO_CREATE);

	}

	private void initTabView() {
		unreaMsgdLabel = (TextView) findViewById(R.id.unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
//		unreadFindLable = (TextView) findViewById(R.id.unread_find_number);
		homefragment = new Fragment_Msg();
		myCenterFragment = new MyCenterFragment();
		contactlistfragment = new Fragment_Friends();
//		findfragment = new Fragment_Dicover();
//		profilefragment = new Fragment_Profile();
		fragments = new Fragment[] {homefragment, contactlistfragment, myCenterFragment};
		imagebuttons = new ImageView[4];
		imagebuttons[0] = (ImageView) findViewById(R.id.ib_weixin);
		imagebuttons[1] = (ImageView) findViewById(R.id.ib_contact_list);
//		imagebuttons[2] = (ImageView) findViewById(R.id.ib_find);
		imagebuttons[2] = (ImageView) findViewById(R.id.ib_profile);

		imagebuttons[0].setSelected(true);
		textviews = new TextView[4];
		textviews[0] = (TextView) findViewById(R.id.tv_weixin);
		textviews[1] = (TextView) findViewById(R.id.tv_contact_list);
//		textviews[2] = (TextView) findViewById(R.id.tv_find);
		textviews[2] = (TextView) findViewById(R.id.tv_profile);
		textviews[0].setTextColor(0xFF000000);
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, homefragment)
                .add(R.id.fragment_container, contactlistfragment)
				.add(R.id.fragment_container, myCenterFragment)
//				.add(R.id.fragment_container, profilefragment)
//				.add(R.id.fragment_container, findfragment)
				.hide(contactlistfragment)
				.hide(myCenterFragment)
				.show(homefragment).commit();
	}

	public void onTabClicked(View view) {
        img_right.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.re_weixin:
                img_right.setVisibility(View.VISIBLE);
                index = 0;
                if (homefragment != null) {
                    homefragment.refresh();
                }
                txt_title.setText(R.string.msg);
				img_right.setImageResource(R.drawable.icon_add);
                break;
            case R.id.re_contact_list:
                index = 1;
                txt_title.setText(R.string.contacts);
//                img_right.setVisibility(View.VISIBLE);
//                img_right.setImageResource(R.drawable.icon_me_setting);
                break;
//		case R.id.re_find:
//			index = 2;
//			txt_title.setText(R.string.discover);
//			break;
            case R.id.re_profile:
                index = 2;
                txt_title.setText(R.string.me);
				img_right.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        if (currentTabIndex != index) {
            Log.e("123qwe", "currentTabIndex:" + currentTabIndex + ",index:" + index);
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        imagebuttons[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        imagebuttons[index].setSelected(true);
        textviews[currentTabIndex].setTextColor(0xFF999999);
        textviews[index].setTextColor(0xFF000000);
        currentTabIndex = index;
    }

	private void initPopWindow() {
//		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, R.string.menu_groupchat,
				R.drawable.icon_menu_group));
		titlePopup.addAction(new ActionItem(this, R.string.menu_addfriend,
				R.drawable.icon_menu_addfriend));
		titlePopup.addAction(new ActionItem(this, R.string.menu_qrcode,
				R.drawable.icon_de_saoyisao));
	}

	private TitlePopup.OnItemOnClickListener onitemClick = new TitlePopup.OnItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			// mLoadingDialog.show();
			switch (position) {
			case 0:// 发起群聊
				Utils.start_Activity(MainActivity.this, AddGroupChatActivity.class);
				break;
			case 1:// 添加朋友
				Utils.start_Activity(MainActivity.this, SearchNetContactActivity.class);
				break;
			case 2:// 扫一扫
				Utils.start_Activity(MainActivity.this, CaptureActivity.class);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unbindService(connection);
		super.onDestroy();
	}

	private void findViewById() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		img_right = (ImageView) findViewById(R.id.img_right);
	}

	private void initViews() {
		// 设置消息页面为初始页面
		img_right.setVisibility(View.GONE);
		txt_title.setText(R.string.msg);
		img_right.setImageResource(R.drawable.icon_add);
	}

	private void setOnListener() {
		img_right.setOnClickListener(this);

	}

	private int keyBackClickCount = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (keyBackClickCount++) {
			case 0:
				Toast.makeText(this, "再次按返回键退出", Toast.LENGTH_SHORT).show();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						keyBackClickCount = 0;
					}
				}, 3000);
				break;
			case 1:
//				EMChatManager.getInstance().logout();// 退出聊天
				App.getInstance2().exit();
				break;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_right:
			if (index == 0) {
				titlePopup.show(findViewById(R.id.img_right));
			} else {
//				Utils.start_Activity(MainActivity.this, PublicActivity.class,
//						new BasicNameValuePair(Constants.NAME, "添加朋友"));
			}
			break;
		default:
			break;
		}
	}

	private void initVersion() {
//		// TODO 检查版本更新
//		String versionInfo = Utils.getValue(this, Constants.VersionInfo);
//		if (!TextUtils.isEmpty(versionInfo)) {
//			Tipdialog = new WarnTipDialog(this,
//					"发现新版本：  1、更新啊阿三达到阿德阿   2、斯顿阿斯顿撒旦？");
//			Tipdialog.setBtnOkLinstener(onclick);
//			Tipdialog.show();
//		}
	}

	private DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
//			Utils.showLongToast(MainActivity.this, "正在下载...");// TODO
//			Tipdialog.dismiss();
		}
	};

	private void initReceiver() {
		LWJNIManager.getInstance();
//		Intent intent = new Intent(this, UpdateService.class);
//		startService(intent);
//		registerReceiver(new MyBroadcastReceiver(), new IntentFilter(
//				"com.juns.wechat.Brodcast"));
//		// 注册一个接收消息的BroadcastReceiver
//		msgReceiver = new NewMessageBroadcastReceiver();
//		IntentFilter intentFilter = new IntentFilter(EMChatManager
//				.getInstance().getNewMessageBroadcastAction());
//		intentFilter.setPriority(3);
//		registerReceiver(msgReceiver, intentFilter);
//
//		// 注册一个ack回执消息的BroadcastReceiver
//		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
//				.getInstance().getAckMessageBroadcastAction());
//		ackMessageIntentFilter.setPriority(3);
//		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
//
//		// 注册一个透传消息的BroadcastReceiver
//		IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager
//				.getInstance().getCmdMessageBroadcastAction());
//		cmdMessageIntentFilter.setPriority(3);
//		registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);
//		// setContactListener监听联系人的变化等
//		// EMContactManager.getInstance().setContactListener(
//		// new MyContactListener());
//		// 注册一个监听连接状态的listener
//		// EMChatManager.getInstance().addConnectionListener(
//		// new MyConnectionListener());
//		// // 注册群聊相关的listener
//		EMGroupManager.getInstance().addGroupChangeListener(
//				new MyGroupChangeListener());
//		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
//		EMChat.getInstance().setAppInited();
	}


	private void getUserInfo() {
//		UserInfoRequest request = new UserInfoRequest();
//		request.setUid(GloableParams.CurUser.getUid());
//		HttpUtils.doPost(this, Request.Path.USER_ALLPROFILE, request, false, Request.Code.USER_ALLPROFILE, this);
	}

	@Override
	public void next(Object o, int requestCode) {
		switch (requestCode) {
			case Request.Code.USER_ALLPROFILE:
				UserInfo items = ((UserInfo) o);
				items.setIscurrent(true);
				LWDBManager.getInstance().insertOrReplace(items);
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
			case Request.Code.USER_ALLPROFILE:
				type = new TypeToken<BaseResponse<UserInfo>>() {
				}.getType();
				break;
			default:
				break;
		}
		return type;
	}

	// 自己联系人 群组数据返回监听
	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
//			// Bundle bundle = intent.getExtras();
//			homefragment.refresh();
//			contactlistfragment.refresh();
		}
	}


	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
//			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
//
//			String from = intent.getStringExtra("from");
//			// 消息id
//			String msgId = intent.getStringExtra("msgid");
//			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
//			if (ChatActivity.activityInstance != null) {
//				if (message.getChatType() == ChatType.GroupChat) {
//					if (message.getTo().equals(
//							ChatActivity.activityInstance.getToChatUsername()))
//						return;
//				} else {
//					if (from.equals(ChatActivity.activityInstance
//							.getToChatUsername()))
//						return;
//				}
//			}
//
//			// 注销广播接收者，否则在ChatActivity中会收到这个广播
//			abortBroadcast();
//			// 刷新bottom bar消息未读数
//			updateUnreadLabel();
//			if (currentTabIndex == 0) {
//				// 当前页面如果为聊天历史页面，刷新此页面
//				if (homefragment != null) {
//					homefragment.refresh();
//				}
//			}
		}
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
//			abortBroadcast();
//			// 刷新bottom bar消息未读数
//			updateUnreadLabel();
//			String msgid = intent.getStringExtra("msgid");
//			String from = intent.getStringExtra("from");
//
//			EMConversation conversation = EMChatManager.getInstance()
//					.getConversation(from);
//			if (conversation != null) {
//				// 把message设为已读
//				EMMessage msg = conversation.getMessage(msgid);
//
//				if (msg != null) {
//
//					if (ChatActivity.activityInstance != null) {
//						if (msg.getChatType() == ChatType.Chat) {
//							if (from.equals(ChatActivity.activityInstance
//									.getToChatUsername()))
//								return;
//						}
//					}
//
//					msg.isAcked = true;
//				}
//			}
		}
	};

//	/**
//	 * MyGroupChangeListener
//	 */
//	private class MyGroupChangeListener implements GroupChangeListener {
//
//		@Override
//		public void onInvitationReceived(String groupId, String groupName,
//                                         String inviter, String reason) {
//
//			// 被邀请
//			String st3 = getResources().getString(
//					R.string.Invite_you_to_join_a_group_chat);
//			User user = GloableParams.Users.get(inviter);
//			if (user != null) {
//				EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
//				msg.setChatType(ChatType.GroupChat);
//				msg.setFrom(inviter);
//				msg.setTo(groupId);
//				msg.setMsgId(UUID.randomUUID().toString());
//				msg.addBody(new TextMessageBody(user.getUserName() + st3));
//				msg.setAttribute("useravatar", user.getHeadUrl());
//				msg.setAttribute("usernick", user.getUserName());
//				// 保存邀请消息
//				EMChatManager.getInstance().saveMessage(msg);
//				// 提醒新消息
//				EMNotifier.getInstance(getApplicationContext())
//						.notifyOnNewMsg();
//			}
//			runOnUiThread(new Runnable() {
//				public void run() {
//					updateUnreadLabel();
//					// 刷新ui
//					if (currentTabIndex == 0)
//						homefragment.refresh();
//				}
//			});
//
//		}
//
//		@Override
//		public void onInvitationAccpted(String groupId, String inviter,
//                                        String reason) {
//
//		}
//
//		@Override
//		public void onInvitationDeclined(String groupId, String invitee,
//                                         String reason) {
//
//		}
//
//		@Override
//		public void onUserRemoved(String groupId, String groupName) {
//			// 提示用户被T了 刷新ui
//			runOnUiThread(new Runnable() {
//				public void run() {
//					try {
//						updateUnreadLabel();
//						if (currentTabIndex == 0)
//							homefragment.refresh();
//					} catch (Exception e) {
//						EMLog.e(TAG, "refresh exception " + e.getMessage());
//					}
//				}
//			});
//		}
//
//		@Override
//		public void onGroupDestroy(String groupId, String groupName) {
//			// 群被解散 提示用户群被解散, 刷新ui
//			runOnUiThread(new Runnable() {
//				public void run() {
//					updateUnreadLabel();
//					if (currentTabIndex == 0)
//						homefragment.refresh();
//				}
//			});
//		}
//
//		@Override
//		public void onApplicationReceived(String groupId, String groupName,
//                                          String applyer, String reason) {
//			// 用户申请加入群聊
//			InviteMessage msg = new InviteMessage();
//			msg.setFrom(applyer);
//			msg.setTime(System.currentTimeMillis());
//			msg.setGroupId(groupId);
//			msg.setGroupName(groupName);
//			msg.setReason(reason);
//			Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
//			msg.setStatus(InviteMesageStatus.BEAPPLYED);
//			// 提示有新消息
//			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
//		}
//
//		@Override
//		public void onApplicationAccept(String groupId, String groupName,
//                                        String accepter) {
//			String st4 = getResources().getString(
//					R.string.Agreed_to_your_group_chat_application);
//			// 加群申请被同意
//			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
//			msg.setChatType(ChatType.GroupChat);
//			msg.setFrom(accepter);
//			msg.setTo(groupId);
//			msg.setMsgId(UUID.randomUUID().toString());
//			msg.addBody(new TextMessageBody(accepter + st4));
//			// 保存同意消息
//			EMChatManager.getInstance().saveMessage(msg);
//			// 提醒新消息
//			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
//
//			runOnUiThread(new Runnable() {
//				public void run() {
//					updateUnreadLabel();
//					// 刷新ui
//					if (currentTabIndex == 0)
//						homefragment.refresh();
//					// if (CommonUtils.getTopActivity(MainActivity.this).equals(
//					// GroupsActivity.class.getName())) {
//					// GroupsActivity.instance.onResume();
//					// }
//				}
//			});
//		}
//
//		@Override
//		public void onApplicationDeclined(String groupId, String groupName,
//                                          String decliner, String reason) {
//			// 加群申请被拒绝，demo未实现
//		}
//
//	};

	/**
	 * 透传消息BroadcastReceiver
	 */
	private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
//			abortBroadcast();
//			// 刷新bottom bar消息未读数
//			updateUnreadLabel();
//			EMLog.d(TAG, "收到透传消息");
//			// 获取cmd message对象
//			String msgId = intent.getStringExtra("msgid");
//			EMMessage message = intent.getParcelableExtra("message");
//			// 获取消息body
//			CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
//			String action = cmdMsgBody.action;// 获取自定义action
//
//			// 获取扩展属性 此处省略
//			// message.getStringAttribute("");
//			EMLog.d(TAG,
//					String.format("透传消息：action:%s,message:%s", action,
//							message.toString()));
//			String st9 = getResources().getString(
//					R.string.receive_the_passthrough);
//			Toast.makeText(MainActivity.this, st9 + action, Toast.LENGTH_SHORT)
//					.show();
		}
	};

	/**
	 * 获取未读消息数
	 */
	public void updateUnreadLabel(int count) {
//		count = EMChatManager.getInstance().getUnreadMsgsCount();
		if (count > 0) {
			unreaMsgdLabel.setText(String.valueOf(count));
			unreaMsgdLabel.setVisibility(View.VISIBLE);
		} else {
			unreaMsgdLabel.setVisibility(View.INVISIBLE);
		}
	}
	/*自定义消息的加载进度条*/
	public void showProgressDialog(String msg) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(msg);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		try {
			progressDialog.show();
		} catch (WindowManager.BadTokenException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * 隐藏正在加载的进度条
	 *
	 */
	public void dismissProgressDialog() {
		if (null != progressDialog && progressDialog.isShowing() == true) {
			progressDialog.dismiss();
		}
	}

}