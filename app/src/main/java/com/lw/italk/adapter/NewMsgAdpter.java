package com.lw.italk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.gson.group.GroupItem;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.utils.DateUtils;
import com.lw.italk.utils.GlideCircleTransform;
import com.lw.italk.utils.SmileUtils;
import com.lw.italk.framework.base.ViewHolder;
import com.lw.italk.view.dialog.WarnTipDialog;
import com.lw.italk.view.swipe.SwipeLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class NewMsgAdpter extends BaseAdapter {
    protected Context context;
    private List<Conversation> conversationList;
    private WarnTipDialog Tipdialog;
    private int deleteID;
    private String ChatID;
    //	private NetClient netClient;
    private String userid;
    private Hashtable<String, String> ChatRecord = new Hashtable<String, String>();


//	public PublicMsgInfo PublicMsg = null;

    public NewMsgAdpter(Context ctx, List<Conversation> objects) {
        context = ctx;
        conversationList = objects;
//		netClient = new NetClient(ctx);
//		userid = UserUtils.getUserID(context);
    }

    public void setData(List<Conversation> data) {
        conversationList = data;
    }

//	public void setPublicMsg(PublicMsgInfo Msg) {
//		PublicMsg = Msg;
//	}
//
//	public PublicMsgInfo getPublicMsg() {
//		return PublicMsg;
//	}

    public Hashtable<String, String> getChatRecord() {
        return ChatRecord;
    }

    @Override
    public int getCount() {
        if (null == conversationList) {
            return 0;
        }
        return conversationList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_item_msg, parent, false);
        }
        ImageView img_avar = ViewHolder.get(convertView,
                R.id.contactitem_avatar_iv);
        TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
        TextView txt_state = ViewHolder.get(convertView, R.id.txt_state);
        TextView txt_del = ViewHolder.get(convertView, R.id.txt_del);
        TextView txt_content = ViewHolder.get(convertView, R.id.txt_content);
        TextView txt_time = ViewHolder.get(convertView, R.id.txt_time);
        TextView unreadLabel = ViewHolder.get(convertView,
                R.id.unread_msg_number);
        SwipeLayout swipe = ViewHolder.get(convertView, R.id.swipe);
//		if (PublicMsg != null && position == 0) {
//			txt_name.setText(R.string.official_accounts);
//			img_avar.setImageResource(R.drawable.icon_public);
//			txt_time.setText(PublicMsg.getTime());
//			txt_content.setText(PublicMsg.getContent());
//			unreadLabel.setText("3");
//			unreadLabel.setVisibility(View.VISIBLE);
//			swipe.setSwipeEnabled(false);
//		} else {
        swipe.setSwipeEnabled(true);
        // 获取与此用户/群组的会话
        final Conversation conversation = conversationList.get(position);
        // 获取用户username或者群组groupid
        ChatID = conversation.getUsername();
        txt_del.setTag(ChatID);
        if (conversation.getIsGroup()) {
//				img_avar.setImageResource(R.drawable.defult_group);
//				GroupItem info = LWFriendManager.getInstance().getGroupById(conversation.getLocalid() + "");
            txt_name.setText(!TextUtils.isEmpty(conversation.getUsername()) ? conversation.getUsername() : "群组");
//				if (!TextUtils.isEmpty(conversation.getUsername())) {
//					txt_name.setText(info.getName());
//				} else {
////					 initGroupInfo(img_avar, txt_name);// 获取群组信息
//					txt_name.setText("群组");
//				}
        } else {
            txt_name.setText(!TextUtils.isEmpty(conversation.getUsername()) ? conversation.getUsername() : "好友");
//				Contact user = LWFriendManager.getInstance().queryFriendItem(conversation.getUserid());
//				if (user != null) {
//					txt_name.setText(user.getNickname());
//				} else {
//					txt_name.setText("好友");
////					UserUtils.initUserInfo(context, ChatID, img_avar, txt_name);// 获取用户信息
//				}
        }
        if (!TextUtils.isEmpty(conversation.getImgurl())) {
            Glide.with(context).load(conversation.getImgurl())
                    .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                    .transform(new GlideCircleTransform(App.getInstance()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_avar);
        } else {
            img_avar.setImageResource(R.drawable.default_img);
        }

        Log.e("123qwe", "conversation.getUnreadMsgCount():" + conversation.getUnreadMsgCount());
        Log.e("123qwe", "conversation.getUnreadMsgCount() position:" + position);
        Log.e("123qwe", "conversation.getUnreadMsgCount() getLocalid:" + conversation.getLocalid());
        Log.e("123qwe", "conversation.getMsgCount():" + conversation.getMsgCount());
        if (conversation.getUnreadMsgCount() > 0) {
            // 显示与此用户的消息未读数
            unreadLabel.setText(String.valueOf(conversation
                    .getUnreadMsgCount()));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
        if (conversation.getMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            MsgItem lastMessage = conversation.getLastMessage();
            txt_content.setText(
                    SmileUtils.getSmiledText(context,
                            getMessageDigest(lastMessage, context)),
                    BufferType.SPANNABLE);
            //设置时间
            SimpleDateFormat formatter = new SimpleDateFormat("MM日dd日 HH:mm");
            String dateString = formatter.format(lastMessage.getTimestamp());
            txt_time.setText(dateString);

            if (lastMessage.getStatus() == LWConversationManager.SUCCESS) {
                txt_state.setText("送达");
                // txt_state.setBackgroundResource(R.drawable.btn_bg_orgen);
            } else if (lastMessage.getStatus() == LWConversationManager.FAIL) {
                txt_state.setText("失败");
                // txt_state.setBackgroundResource(R.drawable.btn_bg_red);
            } else if (lastMessage.getStatus() == LWConversationManager.RECEIVE) {
                txt_state.setText("已读");
                txt_state.setBackgroundResource(R.drawable.btn_bg_blue);
            }
        } else {
            txt_content.setText("");
            txt_time.setText("");
        }

        txt_del.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteID = position;
                Tipdialog = new WarnTipDialog((Activity) context,
                        "您确定要删除该聊天吗？");
                Tipdialog.setBtnOkLinstener(onclick);
                Tipdialog.show();
            }
        });
//		}
        return convertView;
    }

    /**
     * 消息面板左划删除聊天消息
     */
    private DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Conversation conversation = conversationList.get(deleteID);

            String localid = conversation.getLocalid();
            //是否群消息
            boolean isGroup = conversation.getIsGroup();
            //删除消息面板的分组消息
            LWConversationManager.getInstance().deleteConversaTionByLocalid(localid, isGroup);
            //删除具体的聊天内容
            LWConversationManager.getInstance().deleteMsgItemByFid(localid);

            conversationList.remove(deleteID);
            notifyDataSetChanged();
            Tipdialog.dismiss();
        }
    };

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    private String getMessageDigest(MsgItem message, Context context) {
        String digest = "";
        switch (message.getBussinesstype()) {
//		case LOCATION: // 位置消息
//			if (message.direct == LWMessage.Direct.RECEIVE) {
//				digest = getStrng(context, R.string.location_recv);
//				String name = message.getFrom();
//				if (GloableParams.UserInfos != null) {
//					User user = GloableParams.Users.get(message.getFrom());
//					if (user != null && null != user.getUserName())
//						name = user.getUserName();
//				}
//				digest = String.format(digest, name);
//				return digest;
//			} else {
//				digest = getStrng(context, R.string.location_prefix);
//			}
//			break;
            case LWConversationManager.IMAGE: // 图片消息
//			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                digest = getStrng(context, R.string.picture);
                break;
            case LWConversationManager.VOICE:// 语音消息
                digest = getStrng(context, R.string.voice_msg);
                break;
            case LWConversationManager.VIDEO: // 视频消息
                digest = getStrng(context, R.string.video);
                break;
            case LWConversationManager.TXT: // 文本消息
//			if (!message.getBooleanAttribute(
//					Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
//				TextMessageBody txtBody = (TextMessageBody) message.getBody();
                digest = message.getText();
//			} else {
//				TextMessageBody txtBody = (TextMessageBody) message.getBody();
//				digest = getStrng(context, R.string.voice_call)
//						+ txtBody.getMessage();
//			}
                break;
            case LWConversationManager.FILE: // 普通文件消息
                digest = getStrng(context, R.string.file);
                break;
             case LWConversationManager.ADD_FRIEND_RESPONSE:
                digest = message.getText();
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }
        return digest;
    }

    String getStrng(Context context, int resId) {
        return context.getResources().getString(resId);
    }
}
