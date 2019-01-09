package com.lw.italk.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.lw.italk.App;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.activity.CompanyActivity;
import com.lw.italk.activity.SearchViewActivity;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWJNIManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.framework.common.MsgSendRq;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.greendao.model.RequestTime;
import com.lw.italk.greendao.model.UserInfo;
import com.lw.italk.gson.friend.CompanyDeptList;
import com.lw.italk.gson.friend.FriendList;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.model.FriendListRequest;
import com.lw.italk.http.model.UserInfoRequest;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.GlideCircleTransform;
import com.lw.italk.utils.Utils;
import com.lw.italk.activity.MyGroupActivity;
import com.lw.italk.activity.NewFriendsListActivity;
import com.lw.italk.adapter.ContactAdapter;
import com.lw.italk.view.SideBar;
import com.yixia.camera.util.Log;

import java.lang.reflect.Type;
import java.util.HashMap;


//通讯录

public class Fragment_Friends extends Fragment implements OnClickListener,
        OnItemClickListener , Response{
    private Activity mContext;
    private View layout, layout_head;
    private ListView lvContact;
    private SideBar indexBar;
    private TextView mDialogText;
    private WindowManager mWindowManager;
    private LinearLayout mSearchBar;
    boolean isInit = false;
    private ContactAdapter mContactAdapter;
    private FriendList data = null;

    private LinearLayout companyLayout = null;
    private View companyLine = null;
    private ImageView companyImage = null;
    private TextView companyText = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (layout == null) {
            mContext = this.getActivity();
            layout = mContext.getLayoutInflater().inflate(R.layout.fragment_friends,
                    null);
            mWindowManager = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);
            initViews();
            initData();
            setOnListener();
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        LWJNIManager.getInstance().registerMsgSendListen(new MsgSendRq() {
            @Override
            public void msgSendResponseSuceess(String userid) {
                refresh();
            }
        });
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInit) {
            refresh();
        } else {
            isInit = true;
        }

    }

    private void initViews() {
        lvContact = (ListView) layout.findViewById(R.id.lvContact);

        mDialogText = (TextView) LayoutInflater.from(getActivity()).inflate(
                R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        indexBar = (SideBar) layout.findViewById(R.id.sideBar);
        indexBar.setListView(lvContact);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        indexBar.setTextView(mDialogText);
        layout_head = mContext.getLayoutInflater().inflate(
                R.layout.layout_head_friend, null);
        lvContact.addHeaderView(layout_head);
        mContactAdapter = new ContactAdapter(getActivity(),
                GloableParams.contactInfos);
        lvContact.setAdapter(mContactAdapter);

        companyLayout = (LinearLayout) layout_head.findViewById(R.id.layout_company);
        companyLine = layout_head.findViewById(R.id.company_line);
        companyImage = (ImageView) layout_head.findViewById(R.id.company_image);
        companyText = (TextView) layout_head.findViewById(R.id.company_text);
    }

    @Override
    public void onDestroy() {
        //移除防止内存泄漏
        mWindowManager.removeViewImmediate(mDialogText);
        LWJNIManager.getInstance().unregisterMsgSendUpdateListen();
        super.onDestroy();
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        initData();
    }

    private void initData() {
//        MainActivity activity = (MainActivity) mContext;
//        activity.showProgressDialog("正在同步好友列表");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    GloableParams.contactInfos = LWFriendManager.getInstance().getAllContact(LWUserManager.getInstance().getUserInfo().getUid());
                    if (GloableParams.contactInfos != null && GloableParams.contactInfos.size() > 0) {
                        myHandler.sendEmptyMessage(1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext,"Fragment_Friends_initData:"+e.getLocalizedMessage(),Toast.LENGTH_SHORT);
                }
            }
        }).start();

        long time = LWFriendManager.getInstance().getNowTime(LWUserManager.getInstance().getUserInfo().getUid());
        requestDate(time, 0, LWUserManager.getInstance().getToken());
//        requestDate();

    }

    private void setOnListener() {
        lvContact.setOnItemClickListener(this);
        layout_head.findViewById(R.id.layout_addfriend)
                .setOnClickListener(this);
        layout_head.findViewById(R.id.layout_search).setOnClickListener(this);
        layout_head.findViewById(R.id.layout_group).setOnClickListener(this);
        layout_head.findViewById(R.id.layout_company).setOnClickListener(this);
//		layout_head.findViewById(R.id.layout_public).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_search:// 搜索好友及公众号
                Intent intent = new Intent(getActivity(),SearchViewActivity.class );
                intent.putExtra(Constants.SEARCH_TYPE, Constants.SEARCH_CONTACT);
                startActivity(intent);
                break;
            case R.id.layout_addfriend:// 添加好友
                Utils.start_Activity(getActivity(), NewFriendsListActivity.class);
                break;
            case R.id.layout_group:// 群聊
				Utils.start_Activity(getActivity(), MyGroupActivity.class);
                break;
            case R.id.layout_company:
                Utils.start_Activity(getActivity(), CompanyActivity.class);
                break;
//			case R.id.layout_public:// 公众号
//				Utils.start_Activity(getActivity(), PublishUserListActivity.class);
//				break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		User user = GloableParams.UserInfos.get(arg2 - 1);
//		if (user != null) {
//			Intent intent = new Intent(getActivity(), AcceptFriendActivity.class);
//			intent.putExtra(Constants.NAME, user.getUserName());
//			intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
//			intent.putExtra(Constants.User_ID, user.getTelephone());
//			getActivity().startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.push_left_in,
//					R.anim.push_left_out);
//		}

    }

    private void requestDate(long nowTime, int page, String tokenid){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId",tokenid);
        HttpUtils.doPostFormMap(mContext, Request.Path.FRIEND_FRIENDLIST, map, false, Request.Code.FRIEND_FRIENDLIST, this);

    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode){
            case Request.Code.FRIEND_FRIENDLIST:
                data = (FriendList) o;
                if(data.getFriends() != null){
                    LWFriendManager.getInstance().addContact(data.getFriends());
                }
                if (data !=null){
                    GloableParams.companyInfo = data.getCompanyInfo();
                }
//        for (Contact contact1 : data.getItems()) {
//            Log.e("123qwe", "contact1:" + contact1.getUserid() + ",nick:" + contact1.getNickname());
//        }
                GloableParams.contactInfos = LWFriendManager.getInstance().getAllContact(LWUserManager.getInstance().getUserInfo().getUid());
//        for (Contact contact : GloableParams.contactInfos) {
//            Log.e("123qwe", "contact:" + contact.getUserid() + ",nick:" + contact.getNickname());
//        }
//                RequestTime requestTime = LWFriendManager.getInstance().getRequestTime(LWFriendManager.USERID);
//                if (requestTime == null) {
//                    requestTime = new RequestTime();
//                    requestTime.setUserid(LWFriendManager.USERID);
//                }
//                requestTime.setTimestampnow(data.getTimestampnow());
//                LWFriendManager.getInstance().updateRequest(requestTime);
                myHandler.sendEmptyMessage(2);
                break;
            case Request.Code.USER_ALLPROFILE:
                Contact contact = (Contact) o;
                if (contact != null){
                    LWFriendManager.getInstance().addContact(contact);
                }
                GloableParams.contactInfos = LWFriendManager.getInstance().getAllContact(LWUserManager.getInstance().getUserInfo().getUid());
                myHandler.sendEmptyMessage(2);
                break;
        }


    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        myHandler.sendEmptyMessage(0);
        Toast.makeText(App.getInstance(), t.getMessage(), Toast.LENGTH_SHORT).show();
        switch (requestCode){
            case Request.Code.FRIEND_FRIENDLIST:
//                getfriends();
                break;
        }
//        createFriends();
    }
    private boolean isCreate = false;
    private void createFriends(){
        if (this.isCreate) return;
        this.isCreate = true;
        Contact contact = new Contact();
        contact.setUsername("1个好友");
        contact.setUid("153762082810000");
        LWFriendManager.getInstance().addContact(contact);
        Contact contact2 = new Contact();
        contact.setUsername("2个好友");
        contact.setUid("153762091810000");
        LWFriendManager.getInstance().addContact(contact);
        Contact contact3 = new Contact();
        contact.setUsername("3个好友");
        contact.setUid("153762066910000");
        LWFriendManager.getInstance().addContact(contact);
        GloableParams.contactInfos = LWFriendManager.getInstance().getAllContact(LWUserManager.getInstance().getUserInfo().getUid());
        myHandler.sendEmptyMessage(2);
    }
    private void getfriends(){
        if (this.isCreate) return;
        this.isCreate = true;
        UserInfoRequest request = new UserInfoRequest();
        request.setUid("153762066910000");
        request.setTokenId(LWUserManager.getInstance().getToken());
        request.setAccount("18695732196");
//        UserInfoRequest request1 = new UserInfoRequest();
//        request1.setUid("153762082810000");
//        request1.setTokenId(GloableParams.CurUser.getToken());
//        request1.setAccount("15280092280");海东青：153762082810000，刘亦菲：12209989784170496 宁立恒：153762091810000
        UserInfoRequest request2 = new UserInfoRequest();
        request2.setUid("153762091810000");
        request2.setTokenId(LWUserManager.getInstance().getUserInfo().getToken());
        request2.setAccount("15280082280");//小宁
        if (!LWUserManager.getInstance().getUserInfo().getUid().equals(request.getUid())){
            HttpUtils.doPostForm(this.mContext, Request.Path.USER_ALLPROFILE, request, true, Request.Code.USER_ALLPROFILE, this);
        }
//        if (!GloableParams.CurUser.getUid().equals(request1.getUid())){
//            HttpUtils.doPostForm(this.mContext, Request.Path.USER_ALLPROFILE, request1, true, Request.Code.USER_ALLPROFILE, this);
//        }
        if (!LWUserManager.getInstance().getUserInfo().getUid().equals(request2.getUid())){
            HttpUtils.doPostForm(this.mContext, Request.Path.USER_ALLPROFILE, request2, true, Request.Code.USER_ALLPROFILE, this);
        }

    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.FRIEND_FRIENDLIST:
                type = new TypeToken<BaseResponse<FriendList>>() {
                }.getType();
                break;
            case Request.Code.USER_ALLPROFILE:
                type = new TypeToken<BaseResponse<Contact>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }
    Handler myHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    ((BaseActivity) mContext).dismissProgressDialog();

                    if (GloableParams.contactInfos != null) {
                        mContactAdapter.setData(GloableParams.contactInfos);
                        mContactAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
//                    ((MainActivity) mContext).dismissProgressDialog();
                    if (GloableParams.contactInfos != null) {
                        mContactAdapter.setData(GloableParams.contactInfos);
                        mContactAdapter.notifyDataSetChanged();
                    }
                    if (data.getCompanyInfo() !=null){
                        companyLayout.setVisibility(View.VISIBLE);
                        companyLine.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(data.getCompanyInfo().getLogo())
                                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                                .transform(new GlideCircleTransform(App.getInstance()))
                                .into(companyImage);
                        companyText.setText(data.getCompanyInfo().getCompanyName());
                    }else {
                        companyLayout.setVisibility(View.GONE);
                        companyLine.setVisibility(View.GONE);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
