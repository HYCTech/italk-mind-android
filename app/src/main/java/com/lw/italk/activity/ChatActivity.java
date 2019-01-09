package com.lw.italk.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.adapter.MessageAdapter;
import com.lw.italk.framework.common.LWChatManager;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWGroupMemberManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.framework.common.MsgClearListen;
import com.lw.italk.framework.common.MsgUpdateListen;
import com.lw.italk.greendao.model.Conversation;
import com.lw.italk.greendao.model.RequestTime;
import com.lw.italk.gson.group.MemberList;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.ItalkLog;
import com.lw.italk.utils.PathUtil;
import com.lw.italk.utils.SmileUtils;
import com.lw.italk.utils.Utils;
import com.lw.italk.adapter.ExpressionAdapter;
import com.lw.italk.adapter.ExpressionPagerAdapter;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWError;
import com.lw.italk.framework.common.VoicePlayClickListener;
import com.lw.italk.framework.common.VoiceRecorder;
import com.lw.italk.view.ExpandGridView;
import com.lw.italk.view.PasteEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import flyn.Eyes;

//聊天页面
public class ChatActivity extends BaseActivity implements OnClickListener,Response {

    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;

//	public static final int CHATTYPE_SINGLE = 1;
//	public static final int CHATTYPE_GROUP = 2;

    public static final int Handler_Refresh_LOADING = 1;
	public static final int Handler_Refresh_TCP = 2;
    public static final int Handler_Refresh_CLEAR = 3;

    public static final String COPY_IMAGE = "EASEMOBIMG";
    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;
    private ListView listView;
    private PasteEditText mEditTextContent;
    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    private LinearLayout emojiIconContainer;
    private LinearLayout btnContainer;
    // private ImageView locationImgview;
    private View more;
    private int position;
    private ClipboardManager clipboard;
    private ViewPager expressionViewpager;
    private InputMethodManager manager;
    private List<String> reslist;
    private Drawable[] micImages;
    private int chatType;
    private Conversation conversation;
    private NewMessageBroadcastReceiver receiver;
    public static ChatActivity activityInstance = null;
    // 给谁发送消息
    private String Name;
    private String toChatUsername;
    private VoiceRecorder voiceRecorder;
    private MessageAdapter adapter;
    private File cameraFile;
    static int resendPos;

    private ImageView[] indicationPoint;//指示点控件
    private LinearLayout indicationLayout;

    //	private GroupListener groupListener;
    private TextView txt_title;
    private TextView txt_left;
    private ImageView iv_emoticons_normal, img_right;
    private ImageView iv_emoticons_checked;
    private RelativeLayout edittext_layout;
    private ProgressBar loadmorePB;
    private boolean isloading;
    private final int pagesize = 20;
    private boolean haveMoreData = true;
    private Button btnMore;
    public String playMsgId;
    private AnimationDrawable animationDrawable;

    private List<MsgItem> addMessages;
    private List<MsgItem> tcpUpdateMesssages;
    //	private NetClient netClient;
    @SuppressLint("HandlerLeak")
    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.arg1){
                case 1:
                    stopVoice(null);
                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            List<MsgItem> messages = conversation.getMessages();
            switch(msg.what) {
                case Handler_Refresh_LOADING:
                    if (messages != null && addMessages != null && addMessages.size() >0) {
                        messages.addAll(0,addMessages);
                        // 刷新ui
                        adapter.refresh();
                        scrollMyListViewToBottom(addMessages.size());
                    }
                    loadmorePB.setVisibility(View.GONE);
                    isloading = false;
                    break;
                case Handler_Refresh_TCP:
                    if (tcpUpdateMesssages != null && messages != null && adapter != null && listView != null) {
                        boolean isadd = false;
                        for (int i=tcpUpdateMesssages.size()-1 ;i >=0; i--){
                            int findIndex = -1;
                            for (int j=0;j<messages.size();j++){
                                if(tcpUpdateMesssages.get(i).getMsgid() == messages.get(j).getMsgid()){
                                    findIndex = j;
                                    break;
                                }
                            }
                            if (findIndex != -1){
                                messages.remove(findIndex);
                                messages.add(findIndex,tcpUpdateMesssages.get(i));
                            }else {
                                messages.add(tcpUpdateMesssages.get(i));
                            }
                        }
                        adapter.refresh();
                    }
                    break;
                case Handler_Refresh_CLEAR:
                    messages.clear();
                    adapter.refresh();
                    break;

            }
        }
    };

    private void refreshAdd(){
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        adapter.refresh();
    }
    // private EMGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_chat);
        //initView();
        setUpView();
        if(chatType == LWConversationManager.CHATTYPE_GROUP){
            getMemberList();
        }
        setListener();
        Eyes.translucentStatusBar(this, true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Utils.finish(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * initView
     */
    protected void initView() {
//		netClient = new NetClient(this);
        recordingContainer = findViewById(R.id.view_talk);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_left = (TextView) findViewById(R.id.txt_left);
        txt_left.setVisibility(View.VISIBLE);
        txt_left.setOnClickListener(this);
        img_right = (ImageView) findViewById(R.id.img_right);
        micImage = (ImageView) findViewById(R.id.mic_image);
        animationDrawable = (AnimationDrawable) micImage.getBackground();
        animationDrawable.setOneShot(false);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        listView = (ListView) findViewById(R.id.list);
        mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        // locationImgview = (ImageView) findViewById(R.id.btn_location);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
        btnMore = (Button) findViewById(R.id.btn_more);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        more = findViewById(R.id.more);
        mEditTextContent.setBackgroundResource(R.drawable.input_bar_bg_normal);

        indicationLayout = (LinearLayout) findViewById(R.id.indication_layout);
        // 表情list
        reslist = getExpressionRes(146);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        View gv4 = getGridChildView(4);
        View gv5 = getGridChildView(5);
        View gv6 = getGridChildView(6);
        View gv7 = getGridChildView(7);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        views.add(gv4);
        views.add(gv5);
        views.add(gv6);
        views.add(gv7);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
        initGoodsViewPager(views.size());

        edittext_layout.requestFocus();
        voiceRecorder = new VoiceRecorder(micImageHandler);
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEditTextContent
                            .setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    mEditTextContent
                            .setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        mEditTextContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                scrollMyListViewToBottom(adapter.getCount() - 1);
                mEditTextContent
                        .setBackgroundResource(R.drawable.input_bar_bg_active);
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void initData() {

    }

    private void setUpView() {
        activityInstance = this;
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
        // position = getIntent().getIntExtra("position", -1);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        // 判断单聊还是群聊
        chatType = getIntent().getIntExtra(Constants.TYPE, LWConversationManager.CHATTYPE_SINGLE);
        Name = getIntent().getStringExtra(Constants.NAME);
        img_right.setVisibility(View.VISIBLE);
        if (chatType == LWConversationManager.CHATTYPE_SINGLE) { // 单聊
            toChatUsername = getIntent().getStringExtra(Constants.User_ID);
            img_right.setImageResource(R.mipmap.icon_more);
            if (TextUtils.isEmpty(Name)) {
                initUserInfo();
            } else {
                txt_title.setText(Name);
            }
        } else {
            // 群聊
            if (TextUtils.isEmpty(Name)) {
                initGroupInfo();
            } else {
                txt_title.setText(Name);
            }
//			findViewById(R.id.view_location_video).setVisibility(View.GONE);
            toChatUsername = getIntent().getStringExtra(Constants.GROUP_ID);
            img_right.setImageResource(R.mipmap.icon_more);
        }
        Log.e("123qwe", "toChatUsername:" + toChatUsername);
        conversation = LWConversationManager.getInstance().getmConversById(
                toChatUsername, 0, 15);
        if (conversation.getMessages().size() <15 ){
            haveMoreData = false;
        }
        // 把此会话的未读数置为0
//		conversation.resetUnreadMsgCount();
        if (conversation != null) {
            LWConversationManager.getInstance().updataRead(conversation.getLocalid());
            conversation.setUnreadMsgCount(0);
        }
        Glide.get(this).clearMemory();
        adapter = new MessageAdapter(this, toChatUsername, chatType);
        adapter.setConversation(conversation);
        listView.setStackFromBottom(true);
        // 显示消息
        listView.setAdapter(adapter);
		listView.setOnScrollListener(new ListScrollListener());
//        int count = listView.getCount();
//        if (count > 0) {
//            listView.setSelection(count);
//        }
//        listView.getFirstVisiblePosition();
//        listView.getLastVisiblePosition();
        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });
        // 注册接收消息广播
//		receiver = new NewMessageBroadcastReceiver();
//		IntentFilter intentFilter = new IntentFilter(EMChatManager
//				.getInstance().getNewMessageBroadcastAction());
//		// 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
//		intentFilter.setPriority(5);
//		registerReceiver(receiver, intentFilter);

//		// 注册一个ack回执消息的BroadcastReceiver
//		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
//				.getInstance().getAckMessageBroadcastAction());
//		ackMessageIntentFilter.setPriority(5);
//		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

//		// 注册一个消息送达的BroadcastReceiver
//		IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(
//				EMChatManager.getInstance()
//						.getDeliveryAckMessageBroadcastAction());
//		deliveryAckMessageIntentFilter.setPriority(5);
//		registerReceiver(deliveryAckMessageReceiver,
//				deliveryAckMessageIntentFilter);

//		// 监听当前会话的群聊解散被T事件
//		groupListener = new GroupListener();
//		EMGroupManager.getInstance().addGroupChangeListener(groupListener);

        // show forward message if the message is not null
        String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
        if (forward_msg_id != null) {
            // 显示发送要转发的消息
            forwardMessage(forward_msg_id,toChatUsername);
        }

    }

    protected void setListener() {
        findViewById(R.id.img_back).setVisibility(View.VISIBLE);
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.view_camera).setOnClickListener(this);
		findViewById(R.id.view_file).setOnClickListener(this);
        findViewById(R.id.view_video).setOnClickListener(this);
        findViewById(R.id.view_photo).setOnClickListener(this);
        findViewById(R.id.view_read_burn).setOnClickListener(this);
//		findViewById(R.id.view_location).setOnClickListener(this);
        findViewById(R.id.view_audio).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
        img_right.setOnClickListener(this);

        LWJNIManager.getInstance().registerMsgUpdateListen(new MsgUpdateListen() {
            @Override
            public void updateMsgs(List<MsgItem> msgItems) {
                if (msgItems == null || msgItems.size() == 0){
                    return;
                }
                if (!msgItems.get(0).getFid().equals(toChatUsername)){
                    return;
                }
//                conversation = LWConversationManager.getInstance().getmConversById(toChatUsername);
                tcpUpdateMesssages = msgItems;
                Collections.sort(tcpUpdateMesssages, new Comparator<MsgItem>() {
                    public int compare(MsgItem o1, MsgItem o2) {
                        if (o1.getTimestamp() > o2.getTimestamp()) {
                            return 1;
                        } else if (o1.getTimestamp() == o2.getTimestamp()) {
                            return 0;
                        }
                        return -1;
                    }
                });
                refreshHandler.sendEmptyMessage(Handler_Refresh_TCP);
            }
        });
        LWChatManager.getInstance().registerClearListener(new MsgClearListen() {
            @Override
            public void clear(String conversationID) {
                if (toChatUsername != null && toChatUsername.equalsIgnoreCase(conversationID)){
                    refreshHandler.sendEmptyMessage(Handler_Refresh_CLEAR);
                }
            }
        });
    }

    /**
     * onActivityResult
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("123qwe", "onA ctivityResult requestCode:" + requestCode + ",resultCode:" + resultCode);
//        if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
//            Log.e("123qwe", "REQUEST_CODE_LOCAL");
//            if (data != null) {
//                List<MediaDataBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
//                for (MediaDataBean mediaDataBean : resultList) {
//                    Log.e("123qwe", "REQUEST_CODE_LOCAL mediaDataBean: " +mediaDataBean.getImagePath());
//                }
//                Uri selectedImage = data.getData();
//                if (selectedImage != null) {
//                    sendPicByUri(selectedImage);
//                }
//            }
//        }
        btnContainer.setVisibility(View.GONE);
        listView.setSelection(listView.getCount());
        if (resultCode == RESULT_CODE_EXIT_GROUP) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case RESULT_CODE_COPY: // 复制消息
                    MsgItem copyMsg = ((MsgItem) adapter.getItem(data
                            .getIntExtra("position", -1)));
                    // clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
                    // ((TextMessageBody) copyMsg.getBody()).getMessage()));
                    clipboard.setText(copyMsg.getText());
                    break;
                case RESULT_CODE_DELETE: // 删除消息
                    MsgItem deleteMsg = (MsgItem) adapter.getItem(data
                            .getIntExtra("position", -1));
                    conversation.messages.remove(deleteMsg.getMsgid());
                    adapter.refresh();
                    listView.setSelection(data.getIntExtra("position",
                            adapter.getCount()) - 1);
                    break;

                case RESULT_CODE_FORWARD: // 转发消息
                    // EMMessage forwardMsg = (EMMessage) adapter.getItem(data
                    // .getIntExtra("position", 0));
                    // Intent intent = new Intent(this,
                    // ForwardMessageActivity.class);
                    // intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
                    // startActivity(intent);

                    break;

                default:
                    break;
            }
        }
        if (resultCode == RESULT_OK) { // 清空消息
            if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
                // 清空会话
//				EMChatManager.getInstance().clearConversation(toChatUsername);
                adapter.refresh();
            } else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists())
                    sendPicture(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频

                int duration = data.getIntExtra("dur", 0);
                String videoPath = data.getStringExtra("path");
                Log.e("123qwe", "videoPath:" + videoPath);
                if (!TextUtils.isEmpty(videoPath) && (videoPath.endsWith(".jpg") || videoPath.endsWith(".png"))) {
                    sendPicture(videoPath);
                    return;
                }
                File file = new File(PathUtil.getInstance().getImagePath(),
                        "thvideo" + System.currentTimeMillis());
                Bitmap bitmap = null;
                FileOutputStream fos = null;
                try {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                    if (bitmap == null) {
                        Log.d("chatactivity",
                                "problem load video thumbnail bitmap,use default icon");
                        bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.app_panel_video_icon);
                    }
                    fos = new FileOutputStream(file);

                    bitmap.compress(CompressFormat.JPEG, 100, fos);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fos = null;
                    }
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                }
                sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                Log.e("123qwe", "REQUEST_CODE_LOCAL");
                if (data != null) {
//                    List<MediaDataBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
//                    for (MediaDataBean mediaDataBean : resultList) {
//                        Log.e("123qwe", "REQUEST_CODE_LOCAL mediaDataBean: " +mediaDataBean.getImagePath());
//                    }
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        sendFile(uri);
                    }
                }

            } else if (requestCode == REQUEST_CODE_MAP) { // 地图
//				double latitude = data.getDoubleExtra("latitude", 0);
//				double longitude = data.getDoubleExtra("longitude", 0);
//				String locationAddress = data.getStringExtra("address");
//				if (locationAddress != null && !locationAddress.equals("")) {
//					more(more);
//					sendLocationMsg(latitude, longitude, "", locationAddress);
//				} else {
//					String st = getResources().getString(
//							R.string.unable_to_get_loaction);
//					Toast.makeText(this, st, 0).show();
//				}
                // 重发消息
            } else if (requestCode == REQUEST_CODE_TEXT
                    || requestCode == REQUEST_CODE_VOICE
                    || requestCode == REQUEST_CODE_PICTURE
                    || requestCode == REQUEST_CODE_LOCATION
                    || requestCode == REQUEST_CODE_VIDEO
                    || requestCode == REQUEST_CODE_FILE) {
                resendMessage();
            } else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
                // 粘贴
                if (!TextUtils.isEmpty(clipboard.getText())) {
                    String pasteText = clipboard.getText().toString();
                    if (pasteText.startsWith(COPY_IMAGE)) {
                        // 把图片前缀去掉，还原成正常的path
                        sendPicture(pasteText.replace(COPY_IMAGE, ""));
                    }

                }
            } else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
                MsgItem deleteMsg = (MsgItem) adapter.getItem(data
                        .getIntExtra("position", -1));
//				addUserToBlacklist(deleteMsg.getFrom());
            } else if (conversation.getMsgCount() > 0) {
                adapter.refresh();
                setResult(RESULT_OK);
            } else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
//                adapter.refresh();
                refreshAdd();//直接滚动到底部
            }
        }
    }

    /**
     * 消息图标点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
//        hideKeyboard();
        int id = view.getId();
        switch (id) {
            case R.id.txt_left:
            case R.id.img_back:
                Utils.finish(ChatActivity.this);
                break;
            case R.id.img_right:
                if (chatType == LWConversationManager.CHATTYPE_SINGLE) { // 单聊
                    Intent intent = new Intent(ChatActivity.this, ContactChatDetailActivity.class);
                    intent.putExtra(Constants.GROUP_ID, toChatUsername);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ChatActivity.this, GroupChatDetailActivity.class);
                    intent.putExtra(Constants.GROUP_ID, toChatUsername);
                    startActivity(intent);
                }
                break;
            case R.id.view_camera:
                selectVideoFromCamera();// 点击照相图标
                break;

		case R.id.view_file:
			// 发送文件
			selectFileFromLocal();
			break;
            case R.id.view_video:
                // 视频通话
//			if (!EMChatManager.getInstance().isConnected())
//				Toast.makeText(this, Constants.NET_ERROR, 0).show();
//			else
//				startActivity(new Intent(this, VideoCallActivity.class)
//						.putExtra("username", toChatUsername).putExtra(
//								"isComingCall", false));
                break;
            case R.id.view_photo:
                selectPicFromLocal(); // 点击图片图标
                break;
            case R.id.view_read_burn:
                break;
//		case R.id.view_location:
//			// TODO 位置
//			startActivityForResult(new Intent(this, BaiduMapActivity.class),
//					REQUEST_CODE_MAP);
//			break;
//		case R.id.view_audio:
//			// 语音通话
//			if (!EMChatManager.getInstance().isConnected())
//				Toast.makeText(this, Constants.NET_ERROR, 0).show();
//			else
//				startActivity(new Intent(ChatActivity.this,
//						VoiceCallActivity.class).putExtra("username",
//						toChatUsername).putExtra("isComingCall", false));
//			break;
            case R.id.iv_emoticons_normal:
                // 点击显示表情框
                more.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.INVISIBLE);
                iv_emoticons_checked.setVisibility(View.VISIBLE);
                btnContainer.setVisibility(View.GONE);
                emojiIconContainer.setVisibility(View.VISIBLE);
                hideKeyboard();
                break;
            case R.id.iv_emoticons_checked:// 点击隐藏表情框
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                btnContainer.setVisibility(View.VISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                break;
            case R.id.btn_send:
                // 点击发送按钮(发文字和表情)
                String s = mEditTextContent.getText().toString();
                sendText(s);
                break;
            default:
                break;
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_chat;
    }

    /**
     * 照相获取图片
     */
    public void selectPicFromCamera() {
        if (!Utils.isExitsSdcard()) {
            String st = getResources().getString(
                    R.string.sd_card_does_not_exist);
            Toast.makeText(getApplicationContext(), st, 0).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), "Walk"
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                        MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    public void selectVideoFromCamera() {
        if (!Utils.isExitsSdcard()) {
            String st = getResources().getString(
                    R.string.sd_card_does_not_exist);
            Toast.makeText(getApplicationContext(), st, 0).show();
            return;
        }

//        cameraFile = new File(PathUtil.getInstance().getVideoPath(), "Walk"
//                + System.currentTimeMillis() + ".mp4");
//        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(this, MainRecordActivity.class), REQUEST_CODE_SELECT_VIDEO);
//        startActivityForResult(
//                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
//                        MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
//                REQUEST_CODE_CAMERA);
    }

    /**
     * 选择文件
     */
    private void selectFileFromLocal() {
        Intent intent = null;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showToast(this,"本机无SD卡");
            return;
        }
        //获取文件下载路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() ;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //调用系统文件管理器打开指定路径目录
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);


        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);

//        ImagePicker build = new ImagePicker.Builder()
//                .pickType(ImagePickType.MUTIL) //设置选取类型(拍照ONLY_CAMERA、单选SINGLE、多选MUTIL)
//                .maxNum(9) //设置最大选择数量(此选项只对多选生效，拍照和单选都是1，修改后也无效)
//                .needCamera(false) //是否需要在界面中显示相机入口(类似微信那样)
//                .cachePath(PathUtil.getInstance().getImagePath().getPath()) //自定义缓存路径(拍照和裁剪都需要用到缓存)
//                .doCrop(1, 1, 300, 300) //裁剪功能需要调用这个方法，多选模式下无效，参数：aspectX,aspectY,outputX,outputY
//                .needVideo(true)//是否显示视频 设置
//                .displayer(new GlideImagePickerDisplayer()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
//                .build();

//        build.start(this, REQUEST_CODE_LOCAL, RESULT_OK);
    }

    /**
     * 发送文本消息
     *
     * @param content message content
     *                boolean resend
     */
    private void sendText(String content) {

        if (content.length() > 0) {
            MsgItem message = new MsgItem();
            message.setBussinesstype(LWConversationManager.TXT);
            // 如果是群聊，设置chattype,默认是单聊
//			if (chatType == LWConversationManager.CHATTYPE_GROUP)
            message.setChattype(chatType);
            long time = System.currentTimeMillis();
            message.setMsgid(time);
            message.setTimestamp(time);
            message.setLocalid(time);
//            message.setStatus(LWConversationManager.CREATE);
            message.setDirect(LWConversationManager.DIRECT_SEND);
            message.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
			// 设置消息body
			message.setText(content);
			// 设置要发给谁,用户username或者群聊groupid
			Log.e("123qwe", conversation.getLocalid());
			message.setFid(conversation.getLocalid());
			// 把messgage加到conversation中
			conversation.messages.add(message);
			LWConversationManager.getInstance().addMsg(message);
			// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
//			adapter.refresh();
//			listView.setSelection(adapter.getCount() - 1);
            scrollMyListViewToBottom(adapter.getCount() - 1);
			mEditTextContent.setText("");

			LWJNIManager.getInstance().sendMsgItem(message);
//            setResult(RESULT_OK);
            refreshAdd();
        }
    }

    /**
     * 发送语音
     *
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String fileName, String length,
                           boolean isResend) {
        Log.e("123qwe", "filePath:" + filePath + ",fileName: " + fileName + ",length:" + length);
        if (!(new File(filePath).exists())) {
            return;
        }
        try {
            MsgItem message = new MsgItem();
            message.setBussinesstype(LWConversationManager.VOICE);

            // 如果是群聊，设置chattype,默认是单聊
//            if (chatType == LWConversationManager.CHATTYPE_GROUP)
//                message.setIsgroup(true);
//            message.setLocalid(System.currentTimeMillis());
            // 设置要发给谁,用户username或者群聊groupid
//            message.setFid(conversation.getUserid());
//			int len = Integer.parseInt(length);
//            message.setBurnsecond(len);
//			message.setUrl(filePath);

			message.setChattype(chatType);
			long time = System.currentTimeMillis();
			message.setMsgid(time);
			message.setTimestamp(time);
			message.setLocalid(time);
			message.setLocalpath(filePath);
            message.setStatus(LWConversationManager.CREATE);
			message.setDirect(LWConversationManager.DIRECT_SEND);
			message.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
			// 设置要发给谁,用户username或者群聊groupid
			message.setFid(conversation.getLocalid());
			message.setUrl(filePath);
			int len = Integer.parseInt(length);
			message.setSecond(len);
			// 把messgage加到conversation中
			conversation.messages.add(message);
			LWConversationManager.getInstance().addMsg(message);
//			adapter.refresh();
//			listView.setSelection(listView.getCount() - 1);
//            setResult(RESULT_OK);
            refreshAdd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPicture(final String filePath) {
        Log.e("123qwe", "sendPicture:" + filePath);
        MsgItem message = new MsgItem();
        message.setBussinesstype(LWConversationManager.IMAGE);

        // 如果是群聊，设置chattype,默认是单聊
//        if (chatType == LWConversationManager.CHATTYPE_GROUP)
            message.setChattype(chatType);
		long time = System.currentTimeMillis();
		message.setMsgid(time);
		message.setTimestamp(time);
		message.setLocalid(time);
        message.setStatus(LWConversationManager.CREATE);
		message.setDirect(LWConversationManager.DIRECT_SEND);
		message.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
        message.setLocalthumburl(filePath);
        message.setLocalurl(filePath);
        message.setLocalpath(filePath);
		// 设置要发给谁,用户username或者群聊groupid
		message.setFid(conversation.getLocalid());
		message.setUrl(filePath);
		// 把messgage加到conversation中
		conversation.messages.add(message);
		LWConversationManager.getInstance().addMsg(message);


//		ImageMessageBody body = new ImageMessageBody(new File(filePath));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
//		message.addBody(body);
//		conversation.addMessage(message);
//        conversation.messages.add(message);

//        listView.setAdapter(adapter);
//        adapter.refresh();
//        listView.setSelection(listView.getCount() - 1);
//        setResult(RESULT_OK);
        refreshAdd();
    }

    /**
     * 发送视频消息
     */
    private void sendVideo(final String filePath, final String thumbPath,
                           final int length) {
        final File videoFile = new File(filePath);
        if (!videoFile.exists()) {
            return;
        }
        try {
            MsgItem message = new MsgItem();
            message.setBussinesstype(LWConversationManager.VIDEO);
            message.setChattype(chatType);
            // 如果是群聊，设置chattype,默认是单聊
            message.setChattype(chatType);
            long time = System.currentTimeMillis();
            message.setMsgid(time);
            message.setTimestamp(time);
            message.setLocalid(time);
            message.setStatus(LWConversationManager.CREATE);
            message.setDirect(LWConversationManager.DIRECT_SEND);
            message.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
            message.setLocalthumburl(thumbPath);
            message.setLocalurl(filePath);
            message.setLocalpath(filePath);
            // 设置要发给谁,用户username或者群聊groupid
            message.setFid(conversation.getLocalid());
            message.setUrl(filePath);
            conversation.messages.add(message);
            LWConversationManager.getInstance().addMsg(message);
//			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath,
//					length, videoFile.length());
//			message.addBody(body);
//			conversation.addMessage(message);
//            listView.setAdapter(adapter);
//            adapter.refresh();
//            listView.setSelection(listView.getCount() - 1);
//            setResult(RESULT_OK);
            refreshAdd();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {
        // String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, null, null,
                null, null);
        String st8 = getResources().getString(R.string.cant_find_pictures);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendPicture(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendPicture(file.getAbsolutePath());
        }

    }

    /**
     * 发送位置信息
     *
     * @param latitude
     * @param longitude
     * @param imagePath
     * @param locationAddress
     */
    private void sendLocationMsg(double latitude, double longitude,
                                 String imagePath, String locationAddress) {

    }

    /**
     * 发送文件
     *
     * @param uri
     */
    private void sendFile(Uri uri) {
        String filePath = null;
        filePath = PathUtil.getImageAbsolutePath(this,uri);
        if (filePath == null){
            Toast.makeText(getApplicationContext(), "获取不到此文件", Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            String st7 = getResources().getString(R.string.File_does_not_exist);
            Toast.makeText(getApplicationContext(), st7, 0).show();
            return;
        }
        if (file.length() > 51 * 1024 * 1024) {
            String st6 = getResources().getString(
                    R.string.The_file_is_not_greater_than_10_m);
            Toast.makeText(getApplicationContext(), st6, 0).show();
            return;
        }

        MsgItem message = new MsgItem();
        message.setBussinesstype(LWConversationManager.FILE);

        // 如果是群聊，设置chattype,默认是单聊
//        if (chatType == LWConversationManager.CHATTYPE_GROUP)
        long time = System.currentTimeMillis();
        message.setMsgid(time);
        message.setTimestamp(time);
        message.setChattype(chatType);
        message.setLocalid(time);
        // 设置要发给谁,用户username或者群聊groupid
        message.setFid(conversation.getLocalid());
        message.setStatus(LWConversationManager.CREATE);
        message.setDirect(LWConversationManager.DIRECT_SEND);
        message.setUserid(LWUserManager.getInstance().getUserInfo().getUid());
        // create and add image message in view
        message.setLocalpath(filePath);
        message.setUrl(filePath);
        message.setSize((int) file.length());
        message.setFilename(file.getName());
        conversation.messages.add(message);
        LWConversationManager.getInstance().addMsg(message);

        refreshAdd();
    }

    /**
     * 重发消息
     */
    private void resendMessage() {
//        MsgItem msg = null;
        MsgItem msg = conversation.messages.get(resendPos);
        // msg.setBackSend(true);
        msg.setStatus(LWConversationManager.CREATE);

        adapter.refresh();
        listView.setSelection(resendPos);
    }

    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        btnMore.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        btnContainer.setVisibility(View.VISIBLE);
        emojiIconContainer.setVisibility(View.GONE);

    }

    /**
     * 显示键盘图标
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        edittext_layout.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        mEditTextContent.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mEditTextContent.getText())) {
            btnMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            btnMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 点击清空聊天记录
     *
     * @param view
     */
    public void emptyHistory(View view) {
        String st5 = getResources().getString(
                R.string.Whether_to_empty_all_chats);
        startActivityForResult(
                new Intent(this, AlertDialog.class)
                        .putExtra("titleIsCancel", true).putExtra("msg", st5)
                        .putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
    }

    /**
     * 点击进入群组详情
     *
     * @param view
     */
    public void toGroupDetails(View view) {
        // startActivityForResult(
        // (new Intent(this, GroupDeatilActivity.class).putExtra(
        // "groupId", toChatUsername)), REQUEST_CODE_GROUP_DETAIL);
    }

    /**
     * 显示或隐藏图标按钮页
     *
     * @param view
     */
    public void more(View view) {
        if (more.getVisibility() == View.GONE) {
            System.out.println("more gone");
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
        } else {
            if (emojiIconContainer.getVisibility() == View.VISIBLE) {
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 点击文字输入框
     *
     * @param v
     */
    public void editClick(View v) {
        listView.setSelection(listView.getCount() - 1);
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }

    }

    private void getMemberList() {//得到所有群成员列表
        Log.e("123qwe", "mGroupId:" + toChatUsername);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("groupId",toChatUsername);
        HttpUtils.doPostFormMap(this, Request.Path.GROUP_MEMBERLIST, map, false, Request.Code.GROUP_MEMBERLIST, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.GROUP_MEMBERLIST:
                MemberList mMemberList = (MemberList) o;
                RequestTime requestTime = LWGroupMemberManager.getInstance().getRequestTime(LWDBManager.getInstance().getUserInfo().getUid());
                if (requestTime == null) {
                    requestTime = new RequestTime();
                    requestTime.setUserid(LWDBManager.getInstance().getUserInfo().getUid());
                }
                if (mMemberList.getMembers() != null && mMemberList.getMembers().size() != 0) {
                    LWGroupMemberManager.getInstance().addGroupMember(mMemberList.getMembers(), toChatUsername);
//                    requestTime.setGroupmembertime(mMemberList.getTimestampnow() + 1);
//                    LWGroupMemberManager.getInstance().updateRequest(requestTime);
                }
                adapter.notifyDataSetChanged();
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
            case Request.Code.GROUP_MEMBERLIST:
                type = new TypeToken<BaseResponse<MemberList>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }

    /**
     * 消息广播接收者
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//			// 记得把广播给终结掉
//			abortBroadcast();
//
//			String username = intent.getStringExtra("from");
//			String msgid = intent.getStringExtra("msgid");

        }
    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

//			String msgid = intent.getStringExtra("msgid");
//			String from = intent.getStringExtra("from");
//			EMConversation conversation = EMChatManager.getInstance()
//					.getConversation(from);
//			if (conversation != null) {
//				// 把message设为已读
//				EMMessage msg = conversation.getMessage(msgid);
//				if (msg != null) {
//					msg.isAcked = true;
//				}
//			}
//			adapter.notifyDataSetChanged();

        }
    };

    /**
     * 消息送达BroadcastReceiver
     */
    private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

//			String msgid = intent.getStringExtra("msgid");
//			String from = intent.getStringExtra("from");
//			EMConversation conversation = EMChatManager.getInstance()
//					.getConversation(from);
//			if (conversation != null) {
//				// 把message设为已读
//				EMMessage msg = conversation.getMessage(msgid);
//				if (msg != null) {
//					msg.isDelivered = true;
//				}
//			}
//
//			adapter.notifyDataSetChanged();
        }
    };
    private PowerManager.WakeLock wakeLock;

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animationDrawable.start();
                    if (!Utils.isExitsSdcard()) {
                        String st4 = getResources().getString(
                                R.string.Send_voice_need_sdcard_support);
                        Toast.makeText(ChatActivity.this, st4, Toast.LENGTH_SHORT)
                                .show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        wakeLock.acquire();
                        if (VoicePlayClickListener.isPlaying)
                            VoicePlayClickListener.currentPlayListener
                                    .stopPlayVoice();
                        recordingContainer.setVisibility(View.VISIBLE);
                        recordingHint
                                .setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        voiceRecorder.startRecording(null, toChatUsername,
                                getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (voiceRecorder != null)
                            voiceRecorder.discardRecording();
                        recordingContainer.setVisibility(View.INVISIBLE);
                        Toast.makeText(ChatActivity.this, R.string.recoding_fail,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        recordingHint
                                .setText(getString(R.string.release_to_cancel));
                        recordingHint
                                .setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint
                                .setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        animationDrawable.start();
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    if (voiceRecorder.isRecording()){
                        stopVoice(event);
                    }
                    return true;
                default:
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (voiceRecorder != null)
                        voiceRecorder.discardRecording();
                    return false;
            }
        }
    }
    private void stopVoice(MotionEvent event){
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        buttonPressToSpeak.setPressed(false);
        recordingContainer.setVisibility(View.INVISIBLE);
        if (wakeLock.isHeld())
            wakeLock.release();
        if (event != null && event.getY() < 0) {
            // discard the recorded audio.
            voiceRecorder.discardRecording();

        } else {
            // stop recording and send voice file
            String st1 = getResources().getString(
                    R.string.Recording_without_permission);
            String st2 = getResources().getString(
                    R.string.The_recording_time_is_too_short);
            String st3 = getResources().getString(
                    R.string.send_failure_please);
            try {
                int length = voiceRecorder.stopRecoding();
                if (length > 0) {

                    sendVoice(voiceRecorder.getVoiceFilePath(),
                            voiceRecorder
                                    .getVoiceFileName(toChatUsername),
                            Integer.toString(length), false);
                } else if (length == LWError.INVALID_FILE) {
                    Toast.makeText(getApplicationContext(), st1,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), st2,
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ChatActivity.this, st3,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 21);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(21, 42));
        } else if (i == 3) {
            list.addAll(reslist.subList(42, 63));
        }else if (i == 4) {
            list.addAll(reslist.subList(63, 84));
        }else if (i == 5) {
            list.addAll(reslist.subList(84, 105));
        }else if (i == 6) {
            list.addAll(reslist.subList(105, 126));
        }else if (i == 7) {
            list.addAll(reslist.subList(126, 147));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression" && position != 20) { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class
                                    .forName("com.lw.italk.utils.SmileUtils");
                            Field field = clz.getField(filename);
                            mEditTextContent.append(SmileUtils.getSmiledText(
                                    ChatActivity.this, (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(mEditTextContent.getText())) {

                                int selectionStart = mEditTextContent
                                        .getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = mEditTextContent.getText()
                                            .toString();
                                    String tempStr = body.substring(0,
                                            selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i,
                                                selectionStart);
                                        if (SmileUtils.containsKey(cs
                                                .toString()))
                                            mEditTextContent.getEditableText()
                                                    .delete(i, selectionStart);
                                        else
                                            mEditTextContent.getEditableText()
                                                    .delete(selectionStart - 1,
                                                            selectionStart);
                                    } else {
                                        mEditTextContent.getEditableText()
                                                .delete(selectionStart - 1,
                                                        selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 0; x <= getSum; x++) {
            String filename = "f_static_0" + x;

            reslist.add(filename);

        }
        return reslist;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
//		EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
        LWJNIManager.getInstance().unregisterMsgUpdateListen();
        // 注销广播
        try {
            unregisterReceiver(receiver);
            receiver = null;
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(ackMessageReceiver);
            ackMessageReceiver = null;
            unregisterReceiver(deliveryAckMessageReceiver);
            deliveryAckMessageReceiver = null;
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (conversation != null) {
            txt_title.setText(conversation.getUsername());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld())
            wakeLock.release();
        if (VoicePlayClickListener.isPlaying
                && VoicePlayClickListener.currentPlayListener != null) {
            // 停止语音播放
            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }

        try {
            // 停止录音
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                recordingContainer.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 加入到黑名单
     *
     * @param username
     */
    private void addUserToBlacklist(String username) {
        String st11 = getResources().getString(
                R.string.Move_into_blacklist_success);
        String st12 = getResources().getString(
                R.string.Move_into_blacklist_failure);
//		try {
//			EMContactManager.getInstance().addUserToBlackList(username, false);
        Toast.makeText(getApplicationContext(), st11, Toast.LENGTH_SHORT).show();
//		} catch (EaseMobException e) {
//			e.printStackTrace();
//			Toast.makeText(getApplicationContext(), st12, 0).show();
//		}
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * 覆盖手机返回键
     */
    @Override
    public void onBackPressed() {
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * listview滑动监听listener
     */
    private class ListScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:
                    if (view.getFirstVisiblePosition() == 0 && !isloading
                            && haveMoreData) {
                        loadmorePB.setVisibility(View.VISIBLE);
                        try {
                            //倒序取出，整序显示
                            addMessages = LWDBManager.getInstance().getMsgByFid(conversation.getLocalid() + "", conversation.getIsGroup()?LWConversationManager.CHATTYPE_GROUP:LWConversationManager.CHATTYPE_SINGLE, adapter.getCount(), adapter.increment);

                            Collections.sort(addMessages, new Comparator<MsgItem>() {
                                public int compare(MsgItem o1, MsgItem o2) {
                                    if (o1.getTimestamp() > o2.getTimestamp()) {
                                        return 1;
                                    } else if (o1.getTimestamp() == o2.getTimestamp()) {
                                        return 0;
                                    }
                                    return -1;
                                }
                            });
                            if (addMessages.size() < adapter.increment){
                                haveMoreData = false;
                            }
                        } catch (Exception e1) {
//                            loadmorePB.setVisibility(View.GONE);
                            return;
                        }
                        refreshHandler.sendEmptyMessage(Handler_Refresh_LOADING);

                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    /**
     * 转发消息
     *
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id, String fid) {
        MsgItem forward_msg = LWConversationManager.getInstance().getMsgByMsgId(
                forward_msg_id,fid);
        int type = forward_msg.getBussinesstype();
        switch (type) {
            case LWConversationManager.TXT:
                // 获取消息内容，发送消息
                String content = forward_msg.getText();
                sendText(content);
                break;
            case LWConversationManager.IMAGE:
                // 发送图片
                String filePath = forward_msg.getUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // 不存在大图发送缩略图
                        filePath = Utils.getThumbnailImagePath(filePath);
                    }
                    sendPicture(filePath);
                }
                break;
            default:
                break;
        }
    }

    //滚动到指定位置
    private void scrollMyListViewToBottom(final int position) {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(position);
            }
        });
    }
    /**
     * 监测群组解散或者被T事件
     */
//	class GroupListener extends GroupReomveListener {
//
//		@Override
//		public void onUserRemoved(final String groupId, String groupName) {
//			runOnUiThread(new Runnable() {
//				String st13 = getResources().getString(R.string.you_are_group);
//
//				public void run() {
//					if (toChatUsername.equals(groupId)) {
//						Toast.makeText(ChatActivity.this, st13, 1).show();
//						// if (GroupDeatilActivity.instance != null)
//						// GroupDeatilActivity.instance.finish();
//						// finish();
//					}
//				}
//			});
//		}
//
//		@Override
//		public void onGroupDestroy(final String groupId, String groupName) {
//			// 群组解散正好在此页面，提示群组被解散，并finish此页面
//			runOnUiThread(new Runnable() {
//				String st14 = getResources().getString(
//						R.string.the_current_group);
//
//				public void run() {
//					if (toChatUsername.equals(groupId)) {
//						Toast.makeText(ChatActivity.this, st14, 1).show();
//						// if (GroupDeatilActivity.instance != null)
//						// GroupDeatilActivity.instance.finish();
//						// finish();
//					}
//				}
//			});
//		}
//
//	}
    public String getToChatUsername() {
        return toChatUsername;
    }

    private void initUserInfo() {
        // RequestParams params = new RequestParams();
        // String userid = UserUtils.getUserID(this);
        // params.put("user_id", userid);
        // params.put("obj_id", toChatUsername);
        // netClient.post(Constants.getUserInfoURL, params, new BaseJsonRes() {
        //
        // @Override
        // public void onMySuccess(String data) {
        // User user = JSON.parseObject(data, User.class);
        // // NetClient.getIconBitmap(img_avar, user.getFace_image());
        // txt_title.setText(user.getUserName());
        // }
        //
        // @Override
        // public void onMyFailure() {
        //
        // }
        // });
    }

    private void initGroupInfo() {
//		RequestParams params = new RequestParams();
//		String userid = UserUtils.getUserID(this);
//		params.put("user_id", userid);
//		params.put("group_id", toChatUsername);
//		netClient.post(Constants.getUserInfoURL, params, new BaseJsonRes() {
//
//			@Override
//			public void onMySuccess(String data) {
//				GroupInfo group = JSON.parseObject(data, GroupInfo.class);
//				// NetClient.getIconBitmap(img_avar, group.getGroup_name());
//				txt_title.setText(group.getGroup_name());
//			}
//
//			@Override
//			public void onMyFailure() {
//
//			}
//		});
    }

    private void initGoodsViewPager(int pagesize) {

        /**
         * 有几张图片下面就显示几个小圆点
         */
        indicationPoint = new ImageView[pagesize];
        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);//每个圆点大小
        // 设置每个小圆点距离左边的间距
        margin.setMargins(15, 0, 0, 0);

        for (int i = 0; i < pagesize; i++) {

            ImageView imageView = new ImageView(ChatActivity.this);

            // 设置每个小圆点的宽高
            imageView.setLayoutParams(new LinearLayout.LayoutParams(5, 5));
            indicationPoint[i] = imageView;

            if (i == 0) {

                // 默认选中第一张图片
                indicationPoint[i]
                        .setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                // 其他图片都设置未选中状态
                indicationPoint[i]
                        .setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            indicationLayout.addView(indicationPoint[i], margin);
        }
        // 给viewpager设置监听事件
        expressionViewpager
                .setOnPageChangeListener(new GuidePageChangeListener());
    }


    /* 头部viewpager的页面转换监听 */
    private class GuidePageChangeListener implements
            ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
            // 遍历数组让当前选中图片下的小圆点设置颜色
            for (int i = 0; i < indicationPoint.length; i++) {
                indicationPoint[arg0].setBackgroundResource(R.drawable.page_indicator_focused);
                if (arg0 != i) {
                    indicationPoint[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }
        }
    }

}
