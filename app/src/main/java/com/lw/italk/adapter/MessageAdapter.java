package com.lw.italk.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.italkmind.client.vo.api.MindFileInfo;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.activity.AccountLoginActivity;
import com.lw.italk.activity.AddNetFriendActivity;
import com.lw.italk.activity.LocalVideoActivity;
import com.lw.italk.activity.SendChatActivity;
import com.lw.italk.activity.ShowUserInfoActivity;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWDownloadManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWGroupMemberManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.framework.common.OssManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.greendao.model.GroupMember;
import com.lw.italk.greendao.model.UserInfo;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.ErrorCode;
import com.lw.italk.http.FileTransferManager;
import com.lw.italk.http.RetrofitCallback;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.DateUtils;
import com.lw.italk.utils.FileUtils;
import com.lw.italk.utils.GlideCircleTransform;
import com.lw.italk.utils.SmileUtils;
import com.lw.italk.activity.ChatActivity;
import com.lw.italk.activity.ShowBigImage;
import com.lw.italk.entity.LWMessage;
import com.lw.italk.framework.common.VoicePlayClickListener;
import com.lw.italk.utils.TextFormater;


import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MessageAdapter extends BaseAdapter {

    private final static String TAG = "MessageAdapter";

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 12;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 13;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 14;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 15;

    public static final String IMAGE_DIR = "chat/image/";
    public static final String VOICE_DIR = "chat/audio/";
    public static final String VIDEO_DIR = "chat/video";

    private String username;
    private LayoutInflater inflater;
    private Activity activity;

    // reference to conversation object in chatsdk
    private Conversation conversation;

    private Context context;
    public int offser = 0;
    public int limit = 0;
    public int originValue;//初始值
    public int increment = 10;//刷新历史记录的增量
    public int degree = 0;//刷新测试

    private Map<String, Timer> timers = new Hashtable<String, Timer>();

    public Map<String, FileSendCallback> fileSendCallBackMap = new HashMap<>();

    public MessageAdapter(Context context, String username, int chatType) {
        this.username = username;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (Activity) context;
//        this.conversation = LWConversationManager.getInstance().getmConversById(
//                username, 0, 15);
//        if (conversation != null) {
//            originValue = conversation.getMessages().size();
//            limit = originValue;
//            Collections.sort(conversation.getMessages(), new Comparator<MsgItem>() {
//                public int compare(MsgItem o1, MsgItem o2) {
//                    if (o1.getTimestamp() > o2.getTimestamp()) {
//                        return 1;
//                    } else if (o1.getTimestamp() == o2.getTimestamp()) {
//                        return 0;
//                    }
//                    return -1;
//                }
//            });
//            LWConversationManager.getInstance().updataRead(conversation.getLocalid());
//        }
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    /**
     * 获取item数
     */
    public int getCount() {
        if (conversation == null) {
            return 0;
        }
        int size = conversation.getMsgCount();
        Log.e("123qwe", "msg size:" + size);
        return size;
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        notifyDataSetChanged();
        if (conversation != null) {
            LWConversationManager.getInstance().updataRead(conversation.getLocalid());
        }
    }

    public MsgItem getItem(int position) {
//        Log.e("123qwe", "msg position:" + position);
        return conversation.messages.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取item类型
     */
    public int getItemViewType(int position) {
        MsgItem message = conversation.messages.get(position);
        if (message.getBussinesstype() == LWConversationManager.TXT) {
//			if (message.getBooleanAttribute(
//					Constants.MESSAGE_ATTR_IS_VOICE_CALL, false))
//				return message.direct == LWConversationManager.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL
//						: MESSAGE_TYPE_SENT_VOICE_CALL;
//			else if (message.getBooleanAttribute(
//					Constants.MESSAGE_ATTR_IS_VIDEO_CALL, false))
//				return message.getDesc() == LWConversationManager.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL
//						: MESSAGE_TYPE_SENT_VIDEO_CALL;
            return message.getDirect() == LWConversationManager.DIRECT_RECEIVE ? MESSAGE_TYPE_RECV_TXT
                    : MESSAGE_TYPE_SENT_TXT;
        }
        if (message.getBussinesstype() == LWConversationManager.IMAGE) {
            return message.getDirect() == LWConversationManager.DIRECT_RECEIVE ? MESSAGE_TYPE_RECV_IMAGE
                    : MESSAGE_TYPE_SENT_IMAGE;

        }
//		if (message.getType() == LWConversationManager.LOCATION) {
//			return message.direct == LWConversationManager.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION
//					: MESSAGE_TYPE_SENT_LOCATION;
//		}
        if (message.getBussinesstype() == LWConversationManager.VOICE) {
            return message.getDirect() == LWConversationManager.DIRECT_RECEIVE ? MESSAGE_TYPE_RECV_VOICE
                    : MESSAGE_TYPE_SENT_VOICE;
        }
        if (message.getBussinesstype() == LWConversationManager.VIDEO) {
            return message.getDirect() == LWConversationManager.DIRECT_RECEIVE ? MESSAGE_TYPE_RECV_VIDEO
                    : MESSAGE_TYPE_SENT_VIDEO;
        }
        if (message.getBussinesstype() == LWConversationManager.FILE) {
            return message.getDirect() == LWConversationManager.DIRECT_RECEIVE ? MESSAGE_TYPE_RECV_FILE
                    : MESSAGE_TYPE_SENT_FILE;
        }

        return -1;// invalid
    }

    public int getViewTypeCount() {
        return 16;
    }

    private View createViewByMessage(MsgItem message, int position) {
        switch (message.getBussinesstype()) {
//		case LOCATION:
//			return message.direct == LWConversationManager.Direct.RECEIVE ? inflater
//					.inflate(R.layout.row_received_location, null) : inflater
//					.inflate(R.layout.row_sent_location, null);
            case LWConversationManager.IMAGE:
                return message.getDirect() == LWConversationManager.DIRECT_RECEIVE ? inflater
                        .inflate(R.layout.row_received_picture, null) : inflater
                        .inflate(R.layout.row_sent_picture, null);

            case LWConversationManager.VOICE:
                return message.getDirect() == LWConversationManager.DIRECT_RECEIVE ? inflater
                        .inflate(R.layout.row_received_voice, null) : inflater
                        .inflate(R.layout.row_sent_voice, null);
            case LWConversationManager.VIDEO:
                return message.getDirect() == LWConversationManager.DIRECT_RECEIVE ? inflater
                        .inflate(R.layout.row_received_video, null) : inflater
                        .inflate(R.layout.row_sent_video, null);
            case LWConversationManager.FILE:
                return message.getDirect() == LWConversationManager.DIRECT_RECEIVE ? inflater
                        .inflate(R.layout.row_received_file, null) : inflater
                        .inflate(R.layout.row_sent_file, null);
            case LWConversationManager.ADD_FRIEND_RESPONSE:
                return inflater
                        .inflate(R.layout.row_system_message, null);
            default:
//			// 语音通话
//			if (message.getBooleanAttribute(
//					Constants.MESSAGE_ATTR_IS_VOICE_CALL, false))
//				return message.direct == LWConversationManager.Direct.RECEIVE ? inflater
//						.inflate(R.layout.row_received_voice_call, null)
//						: inflater.inflate(R.layout.row_sent_voice_call, null);
//			// 视频通话
//			else if (message.getBooleanAttribute(
//					Constants.MESSAGE_ATTR_IS_VIDEO_CALL, false))
//				return message.direct == LWConversationManager.Direct.RECEIVE ? inflater
//						.inflate(R.layout.row_received_video_call, null)
//						: inflater.inflate(R.layout.row_sent_video_call, null);
                return message.getDirect() == LWConversationManager.DIRECT_RECEIVE ? inflater
                        .inflate(R.layout.row_received_message, null) : inflater
                        .inflate(R.layout.row_sent_message, null);
        }
    }

    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MsgItem message = getItem(position);
        int chatType = message.getChattype();
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = fechConvertView(message,holder,position);
        } else {
            holder = (ViewHolder) convertView.getTag();
            if (holder.bussinesstype != message.getBussinesstype() || message.getDirect() != holder.direct){
                convertView = fechConvertView(message,holder,position);
            }
        }

        try {
            //设置头像和昵称
            if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) {//判断布局方向
                if (message.getChattype() == LWConversationManager.CHATTYPE_GROUP) {//判断是单聊还是群聊
                    holder.tv_userId.setVisibility(View.VISIBLE);
                    final GroupMember temp = LWGroupMemberManager.getInstance().queryGroupMember(message.getUserid(),username);
                    Glide.with(context).load(temp.getImage())
                            .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                            .transform(new GlideCircleTransform(App.getInstance()))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(holder.head_iv);
                    if(temp.getGroupnickname() != null && temp.getGroupnickname().length() > 0){
                        holder.tv_userId.setText(temp.getGroupnickname());
                    }else {
                        holder.tv_userId.setText(temp.getNickname());
                    }
                    holder.head_iv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;
                            if (LWDBManager.getInstance().queryFriendItem(message.getUserid()) == null) {
                                intent = new Intent(context, AddNetFriendActivity.class);
                            } else {
                                intent = new Intent(context, SendChatActivity.class);
                            }
                            System.out.println("---------MessageAdapter---1---->" );
                            intent.putExtra(Constants.NAME, temp.getNickname());
                            intent.putExtra(Constants.HEADURL, temp.getImage());
                            intent.putExtra(Constants.PHONE, temp.getUserid());
                            intent.putExtra(Constants.User_ID, temp.getUserid());
                            context.startActivity(intent);
                        }
                    });
                } else {
                    holder.tv_userId.setVisibility(View.GONE);
                    final Contact temp = LWFriendManager.getInstance().queryFriendItem(message.getFid());
                    if (temp.getRemark().length() > 0){
                        holder.tv_userId.setText(temp.getRemark());//设置备注
                    }else {
                        holder.tv_userId.setText(temp.getUsername());//设置昵称
                    }
                    Glide.with(context).load(temp.getAvatar())
                            .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                            .transform(new GlideCircleTransform(App.getInstance()))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(holder.head_iv);

                    holder.head_iv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;
                            if (LWDBManager.getInstance().queryFriendItem(message.getUserid()) == null) {
                                intent = new Intent(context, AddNetFriendActivity.class);
                            } else {
                                intent = new Intent(context, SendChatActivity.class);
                            }
                            System.out.println("---------MessageAdapter---2---->" );
                            intent.putExtra(Constants.NAME, temp.getUsername());
                            intent.putExtra(Constants.HEADURL, temp.getAvatar());
                            intent.putExtra(Constants.PHONE, temp.getMobile());
                            intent.putExtra(Constants.User_ID, temp.getUid());
                            context.startActivity(intent);
                        }
                    });
                }
            } else {
                holder.tv_userId.setVisibility(View.GONE);
                Glide.with(context).load(LWUserManager.getInstance().getUserInfo().getAvatar())
                        .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                        .transform(new GlideCircleTransform(App.getInstance()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.head_iv);

                holder.head_iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        System.out.println("---------queryFriendItem------->"+ LWDBManager.getInstance().queryFriendItem(message.getUserid()));
                        System.out.println("--------->message.getUserid()------>"+message.getUserid());
                        System.out.println("--------->getUserInfo.getUserid()------>"+LWUserManager.getInstance().getUserInfo().getUid());
                        UserInfo userInfo=LWUserManager.getInstance().getUserInfo();
                        if(userInfo!=null){
                            String uid=userInfo.getUid();
                            String userName=LWUserManager.getInstance().getUserInfo().getUsername();
                            String avatar=LWUserManager.getInstance().getUserInfo().getAvatar();
                            if (LWDBManager.getInstance().queryFriendItem(message.getUserid()) == null && !uid.equals(message.getUserid())) {
                                intent = new Intent(context, AddNetFriendActivity.class);
                            } else if(uid.equals(message.getUserid())){
                                intent = new Intent(context, ShowUserInfoActivity.class);
                            }else {
                                 intent = new Intent(context, SendChatActivity.class);
                            }
                            System.out.println("---------MessageAdapter---3---->" );
                            intent.putExtra(Constants.NAME, userName);
                            intent.putExtra(Constants.HEADURL,avatar );
                            intent.putExtra(Constants.PHONE, uid);
                            intent.putExtra(Constants.User_ID, uid);
                            context.startActivity(intent);
                        }

                    }
                });
            }

        } catch (Exception e) {
        }

        // 群聊时，显示接收的消息的发送人的名称
        if (chatType == LWConversationManager.CHATTYPE_GROUP
                && message.getStatus() == LWConversationManager.RECEIVE) {
            // juns 好友名字
            // User user = GloableParams.Users.get(message.getFrom());
//             holder.tv_userId.setText(user.getUserName());
        }
        // 如果是发送的消息并且不是群聊消息，显示已读textview
        if (message.getDirect() == LWConversationManager.DIRECT_SEND
                && chatType != LWConversationManager.CHATTYPE_GROUP) {
            holder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
            holder.tv_delivered = (TextView) convertView
                    .findViewById(R.id.tv_delivered);
            if (holder.tv_ack != null) {
                if (message.getStatus() == LWConversationManager.RECEIVE) {
                    if (holder.tv_delivered != null) {
                        holder.tv_delivered.setVisibility(View.INVISIBLE);
                    }
                    holder.tv_ack.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_ack.setVisibility(View.INVISIBLE);

                    // check and display msg delivered ack status
                    if (holder.tv_delivered != null) {
                        if (message.getBurn()) {
                            holder.tv_delivered.setVisibility(View.VISIBLE);
                        } else {
                            holder.tv_delivered.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        } else {
            // 如果是文本或者地图消息并且不是group messgae，显示的时候给对方发送已读回执
            if ((message.getBussinesstype() == LWConversationManager.TXT /*|| message.getChattype() == LWConversationManager.LOCATION*/)
                    && /*!message.isAcked && */chatType != LWConversationManager.CHATTYPE_GROUP) {
                // 不是语音通话记录
//				if (!message.getBooleanAttribute(
//						Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
//					try {
////						EMChatManager.getInstance().ackMessageRead(
////								message.getFrom(), message.getMsgId());
//						// 发送已读回执
//						message.isAcked = true;
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
            }
        }

        Log.e("123qwe", "message.getBussinesstype():" + message.getBussinesstype());
        switch (message.getBussinesstype()) {
            // 根据消息type显示item
            case LWConversationManager.IMAGE: // 图片
                handleImageMessage(message, holder, position, convertView);
                break;
            case LWConversationManager.TXT: // 文本
//			if (message.getBooleanAttribute(
//					Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)
//					|| message.getBooleanAttribute(
//							Constants.MESSAGE_ATTR_IS_VIDEO_CALL, false))
//				// 音视频通话
//				handleCallMessage(message, holder, position);
//			else
                handleTextMessage(message, holder, position);
                break;
//		case LOCATION: // 位置
//			handleLocationMessage(message, holder, position, convertView);
//			break;
            case LWConversationManager.VOICE: // 语音
                handleVoiceMessage(message, holder, position, convertView);
                break;
            case LWConversationManager.VIDEO: // 视频
                handleVideoMessage(message, holder, position, convertView);
                break;
            case LWConversationManager.FILE: // 一般文件
                handleFileMessage(message, holder, position, convertView);
                break;
            case LWConversationManager.ADD_FRIEND_RESPONSE:
                if(holder.tv != null){
                    holder.tv.setText(message.getText());
                }
                break;
            default:
                // not supported
        }

        if (message.getDirect() == LWConversationManager.DIRECT_SEND) {
            View statusView = convertView.findViewById(R.id.msg_status);
            if (statusView != null){
                // 重发按钮点击事件
                statusView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // 显示重发消息的自定义alertdialog
//						Intent intent = new Intent(activity, AlertDialog.class);
//						intent.putExtra("msg",
//								activity.getString(R.string.confirm_resend));
//						intent.putExtra("title",
//								activity.getString(R.string.resend));
//						intent.putExtra("cancel", true);
//						intent.putExtra("position", position);
//						if (message.getBussinesstype() == LWConversationManager.TXT)
//							activity.startActivityForResult(intent,
//									ChatActivity.REQUEST_CODE_TEXT);
//						else if (message.getBussinesstype() == LWConversationManager.VOICE)
//							activity.startActivityForResult(intent,
//									ChatActivity.REQUEST_CODE_VOICE);
//						else if (message.getBussinesstype() == LWConversationManager.IMAGE)
//							activity.startActivityForResult(intent,
//									ChatActivity.REQUEST_CODE_PICTURE);
////						else if (message.getType() == LWConversationManager.Type.LOCATION)
////							activity.startActivityForResult(intent,
////									ChatActivity.REQUEST_CODE_LOCATION);
//						else if (message.getBussinesstype() ==LWConversationManager.FILE)
//							activity.startActivityForResult(intent,
//									ChatActivity.REQUEST_CODE_FILE);
////						else if (message.getType() == LWConversationManager.Type.VIDEO)
////							activity.startActivityForResult(intent,
////									ChatActivity.REQUEST_CODE_VIDEO);

                    }
                });
            }
        } else {
//				final String st = context.getResources().getString(
//						R.string.Into_the_blacklist);
            // 长按头像，移入黑名单
//				holder.head_iv.setOnLongClickListener(new OnLongClickListener() {
//
//					@Override
//					public boolean onLongClick(View v) {
//						Intent intent = new Intent(activity, AlertDialog.class);
//						intent.putExtra("msg", st);
//						intent.putExtra("cancel", true);
//						intent.putExtra("position", position);
//						activity.startActivityForResult(intent,
//								ChatActivity.REQUEST_CODE_ADD_TO_BLACKLIST);
//						return true;
//					}
//				});
        }
//		}

        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);


        if (position == 0) {
            String timeStr = DateUtils.getTimestampString(new Date(message
                    .getTimestamp()));
            timestamp.setText(timeStr);
            timestamp.setVisibility(View.VISIBLE);
        } else {
            // 两条消息时间离得如果稍长，显示时间
            if (DateUtils.isCloseEnough(message.getTimestamp(), conversation
                    .messages.get(position - 1).getTimestamp())) {
                timestamp.setVisibility(View.GONE);
            } else {
                String timeStr = DateUtils.getTimestampString(new Date(message
                        .getTimestamp()));
                timestamp.setText(timeStr);
                timestamp.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    private View fechConvertView(MsgItem message,ViewHolder holder, final int position){

        View convertView = createViewByMessage(message, position);
        holder.bussinesstype = message.getBussinesstype();
        holder.direct = message.getDirect();
        if (message.getBussinesstype() == LWConversationManager.ADD_FRIEND_RESPONSE){
            holder.tv = (TextView) convertView
                    .findViewById(R.id.system_text);
        }else if (message.getBussinesstype() == LWConversationManager.IMAGE) {
            try {
                holder.iv = ((ImageView) convertView
                        .findViewById(R.id.iv_sendPicture));
                holder.head_iv = (ImageView) convertView
                        .findViewById(R.id.iv_userhead);
                holder.tv = (TextView) convertView
                        .findViewById(R.id.percentage);
                holder.pb = (ProgressBar) convertView
                        .findViewById(R.id.progressBar);
                holder.staus_iv = (ImageView) convertView
                        .findViewById(R.id.msg_status);
                holder.tv_userId = (TextView) convertView
                        .findViewById(R.id.tv_userid);
            } catch (Exception e) {
            }

        } else if (message.getBussinesstype() == LWConversationManager.TXT) {

            try {
                holder.pb = (ProgressBar) convertView
                        .findViewById(R.id.pb_sending);
                holder.staus_iv = (ImageView) convertView
                        .findViewById(R.id.msg_status);
                holder.head_iv = (ImageView) convertView
                        .findViewById(R.id.iv_userhead);
                // 这里是文字内容
                holder.tv = (TextView) convertView
                        .findViewById(R.id.tv_chatcontent);
                holder.mark = (TextView) convertView
                        .findViewById(R.id.tv_mark);
                holder.tv_userId = (TextView) convertView
                        .findViewById(R.id.tv_userid);
            } catch (Exception e) {
            }

//				// 语音通话及视频通话
//				if (message.getBooleanAttribute(
//						Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)
//						|| message.getBooleanAttribute(
//								Constants.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
////					holder.iv = (ImageView) convertView
////							.findViewById(R.id.iv_call_icon);
//					holder.tv = (TextView) convertView
//							.findViewById(R.id.tv_chatcontent);
//				}

        } else if (message.getBussinesstype() == LWConversationManager.VOICE) {
            try {
                holder.iv = ((ImageView) convertView
                        .findViewById(R.id.iv_voice));
                holder.head_iv = (ImageView) convertView
                        .findViewById(R.id.iv_userhead);
                holder.mark = (TextView) convertView
                        .findViewById(R.id.tv_mark);

                holder.tv = (TextView) convertView
                        .findViewById(R.id.tv_length);
                holder.pb = (ProgressBar) convertView
                        .findViewById(R.id.pb_sending);
                holder.staus_iv = (ImageView) convertView
                        .findViewById(R.id.msg_status);
                holder.tv_userId = (TextView) convertView
                        .findViewById(R.id.tv_userid);
                holder.iv_read_status = (ImageView) convertView
                        .findViewById(R.id.iv_unread_voice);
            } catch (Exception e) {
            }
        }
//			else if (message.getChattype() == LWConversationManager.Type.LOCATION) {
//				try {
//					holder.head_iv = (ImageView) convertView
//							.findViewById(R.id.iv_userhead);
////					holder.tv = (TextView) convertView
////							.findViewById(R.id.tv_location);
//					holder.pb = (ProgressBar) convertView
//							.findViewById(R.id.pb_sending);
//					holder.staus_iv = (ImageView) convertView
//							.findViewById(R.id.msg_status);
//					holder.tv_userId = (TextView) convertView
//							.findViewById(R.id.tv_userid);
//				} catch (Exception e) {
//				}
//			}
        else if (message.getBussinesstype() == LWConversationManager.VIDEO) {
            try {
                holder.iv = ((ImageView) convertView
                        .findViewById(R.id.chatting_content_iv));
                holder.head_iv = (ImageView) convertView
                        .findViewById(R.id.iv_userhead);
                holder.tv = (TextView) convertView
                        .findViewById(R.id.percentage);
                holder.pb = (ProgressBar) convertView
                        .findViewById(R.id.progressBar);
                holder.staus_iv = (ImageView) convertView
                        .findViewById(R.id.msg_status);
                holder.size = (TextView) convertView
                        .findViewById(R.id.chatting_size_iv);
                holder.timeLength = (TextView) convertView
                        .findViewById(R.id.chatting_length_iv);
                holder.playBtn = (ImageView) convertView
                        .findViewById(R.id.chatting_status_btn);
                holder.container_status_btn = (LinearLayout) convertView
                        .findViewById(R.id.container_status_btn);
                holder.tv_userId = (TextView) convertView
                        .findViewById(R.id.tv_userid);

            } catch (Exception e) {
            }
        } else if (message.getBussinesstype() == LWConversationManager.FILE) {
            try {
                holder.head_iv = (ImageView) convertView
                        .findViewById(R.id.iv_userhead);
                holder.tv_file_name = (TextView) convertView
                        .findViewById(R.id.tv_file_name);
                holder.tv_file_size = (TextView) convertView
                        .findViewById(R.id.tv_file_size);
                holder.pb = (ProgressBar) convertView
                        .findViewById(R.id.pb_sending);
                holder.staus_iv = (ImageView) convertView
                        .findViewById(R.id.msg_status);
                holder.tv_file_download_state = (TextView) convertView
                        .findViewById(R.id.tv_file_state);
                holder.ll_container = (LinearLayout) convertView
                        .findViewById(R.id.ll_file_container);
                // 这里是进度值
                holder.tv = (TextView) convertView
                        .findViewById(R.id.percentage);
            } catch (Exception e) {
            }
            try {
                holder.tv_userId = (TextView) convertView
                        .findViewById(R.id.tv_userid);
            } catch (Exception e) {
            }

        }

        convertView.setTag(holder);
        return convertView;
    }

    /**
     * 文本消息
     *
     * @param message
     * @param holder
     * @param position
     */
    private void handleTextMessage(MsgItem message, ViewHolder holder,
                                   final int position) {
//		TextMessageBody txtBody = (TextMessageBody) message.getBody();
        Spannable span = SmileUtils
                .getSmiledText(context, message.getText());
        // 设置内容
        holder.tv.setText(span, BufferType.SPANNABLE);
//        CountdownThread thread = new CountdownThread(6, 1, holder.mark, 1);
//        thread.start();
        // 设置长按事件监听
//		holder.tv.setOnLongClickListener(new OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				activity.startActivityForResult((new Intent(activity,
//						ContextMenu.class)).putExtra("position", position)
//						.putExtra("type", LWConversationManager.Type.TXT.ordinal()),
//						ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//				return true;
//			}
//		});

        if (message.getDirect() == LWConversationManager.DIRECT_SEND) {

            switch (message.getStatus()) {
                case LWConversationManager.SEND:
                case LWConversationManager.RECEIVE:
                case LWConversationManager.READ:
                case LWConversationManager.SUCCESS:
                    if (holder.pb != null){
                        holder.pb.setVisibility(View.GONE);
                    }
                    if (holder.staus_iv != null){
                        holder.staus_iv.setVisibility(View.GONE);
                    }
                    break;
                case LWConversationManager.FAIL: // 发送失败
                    if (holder.pb != null){
                        holder.pb.setVisibility(View.GONE);
                    }
                    if (holder.staus_iv != null){
                        holder.staus_iv.setVisibility(View.VISIBLE);
                    }
                    break;
                case LWConversationManager.INPROGRESS: // 发送中
                    if (holder.pb != null){
                        holder.pb.setVisibility(View.VISIBLE);
                    }
                    if (holder.staus_iv != null){
                        holder.staus_iv.setVisibility(View.GONE);
                    }
                    break;
                default:
                    // 发送消息
//                    sendMsgInBackground(message, holder);
            }
        }
    }

    /**
     * 音视频通话记录
     *
     * @param message
     * @param holder
     * @param position
     */
//	private void handleCallMessage(LWMessage message, ViewHolder holder,
//                                   final int position) {
//		TextMessageBody txtBody = (TextMessageBody) message.getBody();
//		holder.tv.setText(txtBody.getMessage());
//
//	}

    /**
     * 图片消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleImageMessage(final MsgItem message,
                                    final ViewHolder holder, final int position, View convertView) {
        Log.e("123qwe", "handleImageMessage");
        holder.pb.setTag(position);
//		holder.iv.setOnLongClickListener(new OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				activity.startActivityForResult((new Intent(activity,
//								ContextMenu.class)).putExtra("position", position)
//								.putExtra("type", LWConversationManager.Type.IMAGE.ordinal()),
//						ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//				return true;
//			}
//		});

        // 接收方向的消息
        if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) {

            Log.e("123qwe", "message.getStatus():" + message.getStatus());
            // "it is receive msg";
            if (message.getStatus() == LWConversationManager.INPROGRESS) {
                // "!!!! back receive";
                holder.iv.setImageResource(R.drawable.default_image);
                showDownloadImageProgress(message, holder);
                // downloadImage(message, holder);
            } else {
                // "!!!! not back receive, show image directly");
                holder.pb.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                holder.iv.setImageResource(R.drawable.default_image);
//				ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
//				if (imgBody.getLocalUrl() != null) {
                Log.e("123qwe", "message.getUrl():" + message.getUrl());
                if (message.getUrl() != null) {

                    // String filePath = imgBody.getLocalUrl();
                    String remotePath = message.getUrl();
//					String filePath = Utils.getImagePath(remotePath);
                    String thumbRemoteUrl = message.getThumburl();
//					String thumbnailPath = Utils
//							.getThumbnailImagePath(thumbRemoteUrl);
//					showImageView(thumbnailPath, holder.iv, filePath,
//							message.getUrl(), message);
                    showImageView(thumbRemoteUrl, holder.iv, remotePath,
                            message.getUrl(), message);

                }
            }
            return;
        }

        // 发送的消息
        // process send message
        // send pic, show the pic directly
//		ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
//		String filePath = imgBody.getLocalUrl();
        String filePath = message.getUrl();
        Log.e("123qwe", "handleImageMessage send img filePath:" + filePath);
        if (filePath != null && new File(filePath).exists()) {
            Log.e("123qwe", "handleImageMessage send img remote null:");
            showImageView(filePath,
                    holder.iv, filePath, null, message);
        } else {
            showImageView(filePath,
                    holder.iv, filePath, IMAGE_DIR, message);
        }
        Log.e("123qwe", "handleImageMessage message.getStatus():" + message.getStatus());
        switch (message.getStatus()) {
            case LWConversationManager.SEND:
            case LWConversationManager.RECEIVE:
            case LWConversationManager.READ:
            case LWConversationManager.SUCCESS:
                holder.pb.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.GONE);
                break;
            case LWConversationManager.FAIL:
                holder.pb.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.VISIBLE);
                break;
            case LWConversationManager.INPROGRESS:
                holder.staus_iv.setVisibility(View.GONE);
                holder.pb.setVisibility(View.VISIBLE);
                holder.tv.setVisibility(View.VISIBLE);
                if (fileSendCallBackMap.containsKey(String.valueOf(message.getLocalid()))){
                    FileSendCallback callback = fileSendCallBackMap.get(String.valueOf(message.getLocalid()));
                    if (callback != null){
                        callback.sendMessage = message;
                        callback.viewHolder = holder;
                    }
                }
                break;
            default:
                sendPictureMessage(message, holder);
        }
    }

    /**
     * 视频消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleVideoMessage(final MsgItem message,
                                    final ViewHolder holder, final int position, View convertView) {

        // final File image=new File(PathUtil.getInstance().getVideoPath(),
        // videoBody.getFileName());
        String localThumb = message.getLocalthumburl();
        Log.e("123qwe", "handleVideoMessage send img filePath:" + localThumb);
//		holder.iv.setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				activity.startActivityForResult(new Intent(activity,
//						ContextMenu.class).putExtra("position", position)
//						.putExtra("type", EMMessage.Type.VIDEO.ordinal()),
//						ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//				return true;
//			}
//		});

//		if (localThumb != null) {

        showVideoThumbView(localThumb, holder.iv,
                message.getThumburl(), message);
//		}
        int second = message.getSecond();
        if (second > 0) {
            String time = DateUtils.toTimeBySecond(message.getSecond());
            holder.timeLength.setText(time);
        }
        holder.playBtn.setImageResource(R.drawable.app_panel_video_icon);

        if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) {
            if (message.getSize() > 0) {
                String size = TextFormater.getDataSize(message.getSize());
                if (message.getSecond() < 10){
                    holder.timeLength.setText("0:0"+message.getSecond());
                }else {
                    holder.timeLength.setText("0:"+message.getSecond());
                }
            }
        } else {
            if (message.getLocalpath() != null
                    && new File(message.getLocalpath()).exists()) {
                File file = new File(message.getLocalpath());
                long time = 0;
                try{
                    MediaPlayer meidaPlayer = new MediaPlayer();
                    meidaPlayer.setDataSource(file.getPath());
                    meidaPlayer.prepare();
                    time = meidaPlayer.getDuration();//获得了视频的时长（以毫秒为单位）

                }catch(Exception e){
                }
                String size = TextFormater.getDataSize(new File(message.getLocalpath()).length());//文件大小
                if (Math.round(time/1000) < 10){
                    holder.timeLength.setText("0:0"+Math.round(time/1000));
                }else {
                    holder.timeLength.setText("0:"+Math.round(time/1000));
                }
            }
        }

        if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) {

            // System.err.println("it is receive msg");
            if (message.getStatus() == LWConversationManager.INPROGRESS) {
                // System.err.println("!!!! back receive");
                holder.iv.setImageResource(R.drawable.default_image);
                showDownloadImageProgress(message, holder);

            } else {
                // System.err.println("!!!! not back receive, show image directly");
                holder.iv.setImageResource(R.drawable.default_image);
                if (localThumb != null) {
                    showVideoThumbView(localThumb, holder.iv,
                            message.getThumburl(), message);
                }

            }

            return;
        }
        holder.pb.setTag(position);

        // until here ,deal with send video msg
        switch (message.getStatus()) {
            case LWConversationManager.SEND:
            case LWConversationManager.RECEIVE:
            case LWConversationManager.READ:
            case LWConversationManager.SUCCESS:
                holder.pb.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                break;
            case LWConversationManager.FAIL:
                holder.pb.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.VISIBLE);
                break;
            case LWConversationManager.INPROGRESS:
                if (fileSendCallBackMap.containsKey(String.valueOf(message.getLocalid()))){
                    FileSendCallback callback = fileSendCallBackMap.get(String.valueOf(message.getLocalid()));
                    callback.sendMessage = message;
                    callback.viewHolder = holder;
                }
                break;
            default:
                sendVideoMessage(message, holder);

        }

    }

    /**
     * 语音消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleVoiceMessage(final MsgItem message,
                                    final ViewHolder holder, final int position, View convertView) {
//		VoiceMessageBody voiceBody = (VoiceMessageBody) message.getBody();
        holder.tv.setText(message.getSecond() + "\"");
        if (holder.iv != null){
            holder.iv.setOnClickListener(new VoicePlayClickListener(message,
                    holder.iv, holder.iv_read_status,holder.mark, this, activity, username));
        }
//		holder.iv.setOnLongClickListener(new OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				activity.startActivityForResult((new Intent(activity,
//								ContextMenu.class)).putExtra("position", position)
//								.putExtra("type", LWMessage.Type.VOICE.ordinal()),
//						ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//				return true;
//			}
//		});
        LayoutParams lp = holder.iv.getLayoutParams();
        RelativeLayout.LayoutParams lpMark = (RelativeLayout.LayoutParams)holder.mark.getLayoutParams();
        int temp = 162;
        if (Math.round(message.getSecond())< 11){
            lp.width = (int) (temp * (1+Math.round(message.getSecond())*0.1));
        }else {
            lp.width = (int) (temp * (2+ Math.round(message.getSecond())*0.02));
        }
        switch (Math.round(message.getSecond())) {
            case 1:
                if(message.getDirect() == LWConversationManager.DIRECT_SEND){
                    lpMark.rightMargin = 300;
                }else{
                    lpMark.leftMargin = 300;
                }
                break;
            case 2:
                if(message.getDirect() == LWConversationManager.DIRECT_SEND){
                    lpMark.rightMargin = 310+(int)(temp * 0.2);
                }else{
                    lpMark.leftMargin = 310+(int)(temp * 0.2);
                }
                break;
            case 3:
                if(message.getDirect() == LWConversationManager.DIRECT_SEND){
                    lpMark.rightMargin = 310+(int)(temp * 0.3);
                }else{
                    lpMark.leftMargin = 310+(int)(temp * 0.3);
                }
                break;
            case 4:
                if(message.getDirect() == LWConversationManager.DIRECT_SEND){
                    lpMark.rightMargin = 310+(int)(temp * 0.4);
                }else{
                    lpMark.leftMargin = 310+(int)(temp * 0.4);
                }
                break;
            case 5:
                if(message.getDirect() == LWConversationManager.DIRECT_SEND){
                    lpMark.rightMargin = 310+(int)(temp * 0.5);
                }else{
                    lpMark.leftMargin = 310+(int)(temp * 0.5);
                }
                break;
            case 6:
                if(message.getDirect() == LWConversationManager.DIRECT_SEND){
                    lpMark.rightMargin = 310+(int)(temp * 0.6);
                }else{
                    lpMark.leftMargin = 310+(int)(temp * 0.6);
                }
                break;
            case 7:
                if(message.getDirect() == LWConversationManager.DIRECT_SEND){
                    lpMark.rightMargin = 310+(int)(temp * 0.7);
                }else{
                    lpMark.leftMargin = 310+(int)(temp * 0.7);
                }
                break;
            case 8:
                if(message.getDirect() == LWConversationManager.DIRECT_SEND){
                    lpMark.rightMargin = 310+(int)(temp * 0.8);
                }else{
                    lpMark.leftMargin = 310+(int)(temp * 0.8);
                }
                break;
            case 9:
                if(message.getDirect() == LWConversationManager.DIRECT_SEND){
                    lpMark.rightMargin = 310+(int)(temp * 0.9);
                }else{
                    lpMark.leftMargin = 310+(int)(temp * 0.9);
                }
                break;
            default:
                if(message.getDirect() == LWConversationManager.DIRECT_SEND){
                    lpMark.rightMargin = 310+(int)(temp);
                }else{
                    lpMark.leftMargin = 310+(int)(temp);
                }
                break;
        }
        holder.mark.setLayoutParams(lpMark);
        holder.iv.setLayoutParams(lp);
        if (((ChatActivity) activity).playMsgId != null
                && ((ChatActivity) activity).playMsgId.equals(message
                .getMsgid()) && VoicePlayClickListener.isPlaying) {
            AnimationDrawable voiceAnimation;
//			if (message.direct == LWMessage.Direct.RECEIVE) {
            if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) {
                holder.iv.setImageResource(R.drawable.voice_from_icon);
            } else {
                holder.iv.setImageResource(R.drawable.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) holder.iv.getDrawable();
            voiceAnimation.start();
        } else {
//			if (message.direct == LWMessage.Direct.RECEIVE) {
            if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) {

                holder.iv.setImageResource(R.drawable.chatfrom_voice_playing);
            } else {
                holder.iv.setImageResource(R.drawable.chatto_voice_playing);
            }
        }

//		if (message.direct == LWMessage.Direct.RECEIVE) {
        if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) {
            Log.e("123qwe", "message.getIslisten():" + message.getIslisten());
            if (message.getIslisten()) {
//				// 隐藏语音未听标志
                holder.iv_read_status.setVisibility(View.GONE);
			} else {
				holder.iv_read_status.setVisibility(View.VISIBLE);
			}
            System.err.println("it is receive msg");
            if (message.getStatus() == LWConversationManager.INPROGRESS) {
                holder.pb.setVisibility(View.VISIBLE);
                System.err.println("!!!! back receive");
            } else {
                holder.pb.setVisibility(View.INVISIBLE);

            }
            return;
        }

        // until here, deal with send voice msg
        switch (message.getStatus()) {
            case LWConversationManager.SEND:
            case LWConversationManager.RECEIVE:
            case LWConversationManager.READ:
            case LWConversationManager.SUCCESS:
                if (holder.pb != null){
                    holder.pb.setVisibility(View.GONE);
                }
                if (holder.staus_iv != null){
                    holder.staus_iv.setVisibility(View.GONE);
                }
                break;
            case LWConversationManager.FAIL:
                if (holder.pb != null){
                    holder.pb.setVisibility(View.GONE);
                }
                if (holder.staus_iv != null){
                    holder.staus_iv.setVisibility(View.VISIBLE);
                }
                break;
            case LWConversationManager.INPROGRESS:
                if (holder.pb != null){
                    holder.pb.setVisibility(View.VISIBLE);
                }
                if (holder.staus_iv != null){
                    holder.staus_iv.setVisibility(View.GONE);
                }
                break;
            default:
                if (holder.staus_iv != null){
                    holder.staus_iv.setVisibility(View.GONE);
                }
                if (holder.pb != null){
                    holder.pb.setVisibility(View.VISIBLE);
                }                String[] tmp = message.getUrl().split("/");
                String name = System.currentTimeMillis() + "";
                if (tmp != null && tmp.length > 0) {
                    name = name + "_" + tmp[tmp.length - 1];
                } else {
                    name = name + ".amr";
                }
                message.setStatus(LWConversationManager.INPROGRESS);
                File file = new File(message.getUrl());
                FileTransferManager.getInstance().uploadFile(file,message.getChattype(),Long.valueOf(message.getFid()),4, new RetrofitCallback<ResponseBody>() {
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("123qwe", "onFailure call:" + t.getMessage()+t.getStackTrace());
                        t.printStackTrace();
                        fileSendFail(message);
                    }


                    @Override
                    public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String body = response.body().string();
                            BaseResponse<MindFileInfo> fileRes = MindFileInfo.jsonToMind(body);
                            if (fileRes.getCode() == ErrorCode.SUCCESS) {
                                MindFileInfo fileInfo = fileRes.getData();
                                message.setUrl(fileInfo.getRawPath());
                                LWJNIManager.getInstance().sendMsgItem(message);
                            }else if (fileRes.getCode() == ErrorCode.FILE_NOT_LOGIN){
                                LWDBManager.getInstance().getUserInfo().setIscurrent(false);//账号为退出状态
                                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                                Intent intent = new Intent(activity, AccountLoginActivity.class);
                                //跳转后关闭activity之前的所有activity（原理是清理activity堆栈）
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                                activity.finish();
                                fileSendFail(message);
                            }else {
                                fileSendFail(message);
                            }

                        }catch (Exception e) {
                            e.printStackTrace();
                            fileSendFail(message);
                        }

                    }

                    @Override
                    public void onLoading(long total, long progress) {
                        final int pecent = (int) progress;
                        Log.e("123qwe", "onProgressCallback progress:" + progress +"total = "+total);
                        //此处进行进度更新
                    }
                });
                break;
        }
    }

    /**
     * 文件消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleFileMessage(final MsgItem message,
                                   final ViewHolder holder, int position, View convertView) {
//		final NormalFileMessageBody fileMessageBody = (NormalFileMessageBody) message
//				.getBody();
		final String filePath = message.getLocalpath();
		holder.tv_file_name.setText(message.getFilename());
		holder.tv_file_size.setText(TextFormater.getDataSize(message.getSize()));
		holder.ll_container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
                File file = null;
			    if (filePath != null){
			        file = new File(filePath);
                }
				if (file != null && file.exists()) {
					// 文件存在，直接打开
					FileUtils.openFile(file, (Activity) context);
				} else {
					// 下载
					// TODO 下载文件
                    LWDownloadManager.getInstance().startDownload(message.getMsgid()+"",message.getFid(), message.getUrl(), new OssManager.ProgressCallback() {
                        @Override
                        public void onProgressCallback(double progress) {
                        }

                        @Override
                        public void onSuccessCallback(String filename, String path, final String result) {
                            if (TextUtils.isEmpty(result)) {
                                return;
                            }
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        message.setLocalpath(result);
                                        File file = new File(result);
                                        FileUtils.openFile(file, (Activity) context);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onErrorCallback(String filename, String path, String error) {

                        }
                    }, false);
				}
				if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE
						/*&& !message.isAcked*/) {
//					try {
//						EMChatManager.getInstance().ackMessageRead(
//								message.getFrom(), message.getMsgId());
//						message.isAcked = true;
//					} catch (EaseMobException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}
		});
		String st1 = context.getResources().getString(R.string.Have_downloaded);
		String st2 = context.getResources()
				.getString(R.string.Did_not_download);
		if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) { // 接收的消息
			System.err.println("it is receive msg");
			if (filePath == null){
                holder.tv_file_download_state.setText(st2);
                return;
            }
			File file = new File(filePath);
			if (file != null && file.exists()) {
				holder.tv_file_download_state.setText(st1);
			} else {
				holder.tv_file_download_state.setText(st2);
			}
			return;
		}

		// until here, deal with send voice msg
        switch (message.getStatus()) {
            case LWConversationManager.SEND:
            case LWConversationManager.RECEIVE:
            case LWConversationManager.READ:
            case LWConversationManager.SUCCESS:
                holder.pb.setVisibility(View.INVISIBLE);
                holder.tv.setVisibility(View.INVISIBLE);
                holder.staus_iv.setVisibility(View.INVISIBLE);
                break;
            case LWConversationManager.FAIL:
                holder.pb.setVisibility(View.INVISIBLE);
                holder.tv.setVisibility(View.INVISIBLE);
                holder.staus_iv.setVisibility(View.VISIBLE);
                break;
            case LWConversationManager.INPROGRESS:
                if (fileSendCallBackMap.containsKey(String.valueOf(message.getLocalid()))){
                    FileSendCallback callback = fileSendCallBackMap.get(String.valueOf(message.getLocalid()));
                    callback.sendMessage = message;
                    callback.viewHolder = holder;
                }
                break;
		default:
			// 发送消息
			sendMsgInBackground(message, holder);
		}

    }

    /**
     * 处理位置消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleLocationMessage(final LWMessage message,
                                       final ViewHolder holder, final int position, View convertView) {
//		TextView locationView = ((TextView) convertView
//				.findViewById(R.id.tv_location));
//		LocationMessageBody locBody = (LocationMessageBody) message.getBody();
//		locationView.setText(locBody.getAddress());
//		LatLng loc = new LatLng(locBody.getLatitude(), locBody.getLongitude());
//		locationView.setOnClickListener(new MapClickListener(loc, locBody
//				.getAddress()));
//		locationView.setOnLongClickListener(new OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				activity.startActivityForResult((new Intent(activity,
//						ContextMenu.class)).putExtra("position", position)
//						.putExtra("type", EMMessage.Type.LOCATION.ordinal()),
//						ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//				return false;
//			}
//		});
//
//		if (message.direct == EMMessage.Direct.RECEIVE) {
//			return;
//		}
//		// deal with send message
//		switch (message.status) {
//		case SUCCESS:
//			holder.pb.setVisibility(View.GONE);
//			holder.staus_iv.setVisibility(View.GONE);
//			break;
//		case FAIL:
//			holder.pb.setVisibility(View.GONE);
//			holder.staus_iv.setVisibility(View.VISIBLE);
//			break;
//		case INPROGRESS:
//			holder.pb.setVisibility(View.VISIBLE);
//			break;
//		default:
//			sendMsgInBackground(message, holder);
//		}
    }

    /**
     * 发送消息
     *
     * @param message
     * @param holder
     */
    public void sendMsgInBackground(final MsgItem message,
                                    final ViewHolder holder) {
        holder.staus_iv.setVisibility(View.GONE);
        holder.pb.setVisibility(View.VISIBLE);

        final long start = System.currentTimeMillis();

        File file = new File(message.getUrl());
        long filelength = file.length();
        long file50M = 51*1024*1024;
        if (filelength > file50M){
            message.setStatus(LWConversationManager.FAIL);
            LWConversationManager.getInstance().updateMsg(message);
            this.notifyDataSetChanged();
            Toast.makeText(activity,"文件大于10M",Toast.LENGTH_LONG);
            return;
        }
        message.setStatus(LWConversationManager.INPROGRESS);
        FileSendCallback fileCallback = new FileSendCallback(activity,this);
        fileCallback.sendMessage = message;
        fileCallback.viewHolder = holder;
        fileSendCallBackMap.put(String.valueOf(message.getLocalid()), fileCallback);
        FileTransferManager.getInstance().uploadFile(file,message.getChattype(),Long.valueOf(message.getFid()),6, fileCallback);

    }

    /*
     * chat sdk will automatic download thumbnail image for the image message we
     * need to register callback show the download progress
     */
    private void showDownloadImageProgress(final MsgItem message,
                                           final ViewHolder holder) {
//		System.err.println("!!! show download image progress");
//		// final ImageMessageBody msgbody = (ImageMessageBody)
//		// message.getBody();
//		final FileMessageBody msgbody = (FileMessageBody) message.getBody();
//		if (holder.pb != null)
//			holder.pb.setVisibility(View.VISIBLE);
//		if (holder.tv != null)
//			holder.tv.setVisibility(View.VISIBLE);
//
//		msgbody.setDownloadCallback(new LWCallBack() {
//
//			@Override
//			public void onSuccess() {
//				activity.runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						// message.setBackReceive(false);
//						if (message.getType() == LWMessage.Type.IMAGE) {
//							holder.pb.setVisibility(View.GONE);
//							holder.tv.setVisibility(View.GONE);
//						}
//						notifyDataSetChanged();
//					}
//				});
//			}
//
//			@Override
//			public void onError(int code, String message) {
//
//			}
//
//			@Override
//			public void onProgress(final int progress, String status) {
//				if (message.getType() == LWMessage.Type.IMAGE) {
//					activity.runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							holder.tv.setText(progress + "%");
//
//						}
//					});
//				}
//
//			}
//
//		});
    }

    /*
     * send message with new sdk
     */
    private void sendVideoMessage(final MsgItem message,
                                  final ViewHolder holder) {
        try {
            // before send, update ui
            holder.staus_iv.setVisibility(View.GONE);
            holder.pb.setVisibility(View.VISIBLE);
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setText("0%");
            String[] tmp = message.getUrl().split("/");
            String name = "";
            if (tmp != null && tmp.length > 0) {
                name = tmp[tmp.length - 1];
            } else {
                name = System.currentTimeMillis() + ".mp4";
            }
            Log.e("TAG", "sendVideoMessage :" + name);
            File file = new File(message.getUrl());
            long filelength = file.length();
            long file50M = 51*1024*1024;
            if (filelength > file50M){
                message.setStatus(LWConversationManager.FAIL);
                LWConversationManager.getInstance().updateMsg(message);
                this.notifyDataSetChanged();
                Toast.makeText(activity,"图片文件大于10M",Toast.LENGTH_LONG);
                return;
            }
            message.setStatus(LWConversationManager.INPROGRESS);
            FileSendCallback videoCallback = new FileSendCallback(activity,this);
            videoCallback.sendMessage = message;
            videoCallback.viewHolder = holder;
            fileSendCallBackMap.put(String.valueOf(message.getLocalid()), videoCallback);
            FileTransferManager.getInstance().uploadFile(file,message.getChattype(),Long.valueOf(message.getFid()),3, videoCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * send message with new sdk
     */
    private void sendPictureMessage(final MsgItem message,
                                    final ViewHolder holder) {
        try {
//			String to = message.getTo();
//
            // before send, update ui
            holder.staus_iv.setVisibility(View.GONE);
            holder.pb.setVisibility(View.VISIBLE);
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setText("0%");
            String[] tmp = message.getUrl().split("/");
            String name = System.currentTimeMillis() + "";
            if (tmp != null && tmp.length > 0) {
                name = name + "_" + tmp[tmp.length - 1];
            } else {
                name = name + ".jpg";
            }
            Log.e("TAG", "sendPictureMessage :" + name);

            File file = new File(message.getUrl());
            long filelength = file.length();
            long file50M = 51*1024*1024;
            if (filelength > file50M){
                message.setStatus(LWConversationManager.FAIL);
                LWConversationManager.getInstance().updateMsg(message);
                this.notifyDataSetChanged();
                Toast.makeText(activity,"图片文件大于10M",Toast.LENGTH_LONG);
                return;
            }
            message.setStatus(LWConversationManager.INPROGRESS);
            FileSendCallback picCallback = new FileSendCallback(activity,this);
            picCallback.sendMessage = message;
            picCallback.viewHolder = holder;
            fileSendCallBackMap.put(String.valueOf(message.getLocalid()), picCallback);
            FileTransferManager.getInstance().uploadFile(file,message.getChattype(),Long.valueOf(message.getFid()),2, picCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新ui上消息发送状态
     *
     * @param message
     * @param holder
     */
    private void updateSendedView(final LWMessage message,
                                  final ViewHolder holder) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // send success
                if (message.getType() == LWMessage.Type.VIDEO) {
                    holder.tv.setVisibility(View.GONE);
                }
                if (message.status == LWMessage.Status.SUCCESS) {
                    // if (message.getType() == EMMessage.Type.FILE) {
                    // holder.pb.setVisibility(View.INVISIBLE);
                    // holder.staus_iv.setVisibility(View.INVISIBLE);
                    // } else {
                    // holder.pb.setVisibility(View.GONE);
                    // holder.staus_iv.setVisibility(View.GONE);
                    // }

                } else if (message.status == LWMessage.Status.FAIL) {
                    // if (message.getType() == EMMessage.Type.FILE) {
                    // holder.pb.setVisibility(View.INVISIBLE);
                    // } else {
                    // holder.pb.setVisibility(View.GONE);
                    // }
                    // holder.staus_iv.setVisibility(View.VISIBLE);
                    Toast.makeText(
                            activity,
                            activity.getString(R.string.send_fail)
                                    + activity
                                    .getString(R.string.connect_failuer_toast),
                            Toast.LENGTH_SHORT).show();
                }

                notifyDataSetChanged();
            }
        });
    }

    private void startDownImage(final ImageView iv, final MsgItem message) {
        iv.setImageResource(R.drawable.default_image);
        Log.e("123qwe", "message.getFilename():" + message.getFilename());
        LWDownloadManager.getInstance().startDownload(message.getMsgid()+"" , message.getFid(), message.getUrl(), new OssManager.ProgressCallback() {
            @Override
            public void onProgressCallback(double progress) {

            }

            @Override
            public void onSuccessCallback(String filename, String path, final String result) {
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File file = new File(result);
                            if (file.exists()) {
                                if (!((Activity) context).isFinishing()) {
                                    Glide.with(context).load(file)
                                            .into(iv);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onErrorCallback(String filename, String path, String error) {

            }
        }, true);
    }

    /**
     * load image into image view
     *
     * @param thumbernailPath
     * @param iv
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath,
                                  final ImageView iv, final String localFullSizePath,
                                  String remoteDir, final MsgItem message) {
        // String imagename =
        // localFullSizePath.substring(localFullSizePath.lastIndexOf("/") + 1,
        // localFullSizePath.length());
        // final String remote = remoteDir != null ? remoteDir+imagename :
        // imagename;
        final String remote = remoteDir;
        Log.e("123qwe", "local = " + localFullSizePath + " thumbernailPath: " + thumbernailPath);
        // first check if the thumbnail image already loaded into cache
//		Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
//        Log.e("123qwe", "bitmap = " + bitmap );
//		if (bitmap != null) {
        // thumbnail image is already loaded, reuse the drawable
//			iv.setImageBitmap(bitmap);
        Log.e("123qwe", "message.getLocalthumburl() = " + message.getLocalthumburl());
        if (!TextUtils.isEmpty(message.getLocalthumburl())) {
            File file = new File(message.getLocalthumburl());
            if (file.exists()) {
                Glide.with(context).load(message.getLocalthumburl())
                        .into(iv);
            } else {
                startDownImage(iv, message);
            }

        } else {
            startDownImage(iv, message);
        }

        iv.setClickable(true);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShowBigImage.class);
                Log.e("123qwe", "message.getLocalurl():" + message.getLocalurl());
                if (!TextUtils.isEmpty(message.getLocalpath())) {
                    File file = new File(message.getLocalpath());
                    if (file.exists()) {
                        Uri uri = Uri.fromFile(file);
                        intent.putExtra("uri", uri);
//						System.err
//								.println("here need to check why download everytime");
                    } else {
                        // The local full size pic does not exist yet.
                        // ShowBigImage needs to download it from the server
                        // first
                        // intent.putExtra("", message.get);
//						ImageMessageBody body = (ImageMessageBody) message
//								.getBody();
//						intent.putExtra("secret", body.getSecret());
                        intent.putExtra("remotepath", localFullSizePath);
                        intent.putExtra("msgID", message.getMsgid()+"");
                        intent.putExtra("fid", message.getFid());
                    }
                } else {
                    intent.putExtra("remotepath", localFullSizePath);
                    intent.putExtra("msgID", message.getMsgid()+"");
                    intent.putExtra("fid", message.getFid());
                }


                if (message != null
                        && message.getDirect() == LWConversationManager.DIRECT_RECEIVE
                            /*&& !message.isAcked*/
                        && message.getChattype() != LWConversationManager.GROUP_CREAT) {
                    try {
//							EMChatManager.getInstance().ackMessageRead(
//									message.getFrom(), message.getMsgId());
//							message.isAcked = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                activity.startActivity(intent);
            }
        });
        return true;
//		} else {
//
////			new LoadImageTask().execute(thumbernailPath, localFullSizePath,
////					remote, message.getChatType(), iv, activity, message);
//			return true;
//		}

    }

    /**
     * 展示视频缩略图
     *
     * @param localThumb   本地缩略图路径
     * @param iv
     * @param thumbnailUrl 远程缩略图路径
     * @param message
     */
    private void showVideoThumbView(String localThumb, ImageView iv,
                                    String thumbnailUrl, final MsgItem message) {
        // first check if the thumbnail image already loaded into cache
//		Uri uri = Uri.parse(localThumb);
//		Bitmap bitmap = ImageCache.getInstance().get(localThumb);
//		if (uri != null) {
//			// thumbnail image is already loaded, reuse the drawable
//			iv.setImageURI(uri);
//			iv.setClickable(true);
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//					VideoMessageBody videoBody = (VideoMessageBody) message
//							.getBody();
                Log.e("123qwe", "video view is on click");

                if (message.getDirect() == LWConversationManager.DIRECT_SEND &&
                        message.getLocalpath() != null) {
                    Intent intent = new Intent(activity,
                            LocalVideoActivity.class);
                    intent.putExtra("localpath", message.getLocalpath());
//					 intent.putExtra("secret", videoBody.getSecret());
                    intent.putExtra("remotepath", message.getUrl());
                    activity.startActivity(intent);
                }else {
                    LWDownloadManager.getInstance().startDownload(message.getMsgid()+"",message.getFid(), message.getUrl(), new OssManager.ProgressCallback() {
                        @Override
                        public void onProgressCallback(double progress) {

                        }

                        @Override
                        public void onSuccessCallback(String filename, String path, final String result) {
                            if (TextUtils.isEmpty(result)) {
                                return;
                            }
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        message.setLocalpath(result);
                                        Intent intent = new Intent(activity,
                                                LocalVideoActivity.class);
                                        intent.putExtra("localpath", message.getLocalpath());
                                        intent.putExtra("remotepath", message.getUrl());
                                        activity.startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }

                        @Override
                        public void onErrorCallback(String filename, String path, String error) {

                        }
                    }, false);
                }

            }
        });

//		} else {
        Glide.with(context).load(message.getThumburl())
                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                .into(iv);
////			new LoadVideoImageTask().execute(localThumb, thumbnailUrl, iv,
////					activity, message, this);
//		}

    }

    public static class ViewHolder {
        ImageView iv;
        TextView tv;
        ProgressBar pb;
        TextView mark;
        ImageView staus_iv;
        ImageView head_iv;
        TextView tv_userId;
        ImageView playBtn;
        TextView timeLength;
        TextView size;
        LinearLayout container_status_btn;
        LinearLayout ll_container;
        ImageView iv_read_status;
        // 显示已读回执状态
        TextView tv_ack;
        // 显示送达回执状态
        TextView tv_delivered;

        TextView tv_file_name;
        TextView tv_file_size;
        TextView tv_file_download_state;

        int bussinesstype;
        int direct;
    }

    private void fileSendFail (MsgItem message) {

        message.setStatus(LWConversationManager.FAIL);
        LWConversationManager.getInstance().updateMsg(message);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MessageAdapter.this.notifyDataSetChanged();
                Toast.makeText(activity,"文件上传异常",Toast.LENGTH_LONG).show();
            }
        });
    }

	/*
     * 点击地图消息listener
	 */
//	class MapClickListener implements View.OnClickListener {
//
//		LatLng location;
//		String address;
//
//		public MapClickListener(LatLng loc, String address) {
//			location = loc;
//			this.address = address;
//
//		}
//
//		@Override
//		public void onClick(View v) {// TODO 打开百度
//			Intent intent;
//			intent = new Intent(context, BaiduMapActivity.class);
//			intent.putExtra("latitude", location.latitude);
//			intent.putExtra("longitude", location.longitude);
//			intent.putExtra("address", address);
//			activity.startActivity(intent);
//		}
//
//	}

}