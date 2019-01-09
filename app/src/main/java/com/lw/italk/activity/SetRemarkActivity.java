package com.lw.italk.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.gson.friend.RemarkInfo;
import com.lw.italk.gson.friend.UpdateFriend;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.utils.CameraUtil;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.FileUtils;
import com.lw.italk.utils.Utils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户详细资料—设置备注和标签
 * Created by 喜明 on 2018/8/18.
 */

public class SetRemarkActivity extends BaseActivity implements Response {
    @BindView(R.id.left_bar_item)
    TextView mLeftBarItem;
    @BindView(R.id.center_bar_item)
    TextView mCenterBarItem;
    @BindView(R.id.title_bar_left)
    LinearLayout titleBarLeft;
    @BindView(R.id.right_title_bar)
    TextView mRightTitleBar;
    @BindView(R.id.title_bar_right)
    RelativeLayout titleBarRight;
    @BindView(R.id.et_remark_name)
    EditText mRemarkName;
    @BindView(R.id.et_label)
    EditText mLabel;
    @BindView(R.id.et_phonenumber)
    EditText mPhonenumber;
    @BindView(R.id.et_descrpition)
    EditText mDescrpition;

    private String mHeadUrl = "";
    private String mNickName = "";
    private String mPhoneNumber = "";
    private String mUserID = "";

    private final int ACT_GALLERY = 0;
    private final int ACT_CAMERA = 1;
    private final int ACT_CROP = 2;
    private final int ACT_PERMISSION = 3;
    @BindView(R.id.select_picture)
    ImageView mSelectPicture;
    private Context context = null;

    @Override
    protected int setContentView() {
        return R.layout.activity_setting_remark;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("取消");
        mCenterBarItem.setText("设置备注及标签");
        mRightTitleBar.setText("完成");

        getFriendInfo();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mHeadUrl = getIntent().getStringExtra(Constants.HEADURL);
        mNickName = getIntent().getStringExtra(Constants.NAME);
        mPhoneNumber = getIntent().getStringExtra(Constants.PHONE);
        mUserID = getIntent().getStringExtra(Constants.User_ID);
        System.out.println( "SetRemarkActivity------>mHeadUrl:"+mHeadUrl+"--->mNickName:"+mNickName+"--->mPhoneNumber"+mPhoneNumber+"---mUserID:"+mUserID);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        switch (requestCode) {
            case ACT_PERMISSION:
                if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    Intent intent = CameraUtil.openCamera();
                    startActivityForResult(intent, ACT_CAMERA);
                } else {
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACT_CAMERA:
                switch (resultCode) {
                    case -1:// -1表示拍照成功
                        File file = new File(Environment.getExternalStorageDirectory()
                                + "/" + FileUtils.TEMP_SHOP_PHOTO);
                        if (file.exists()) {
                            startCrop(Uri.fromFile(file));
                        }
                        break;
                    default:
                        break;
                }
                break;
            case ACT_GALLERY:
                if (data != null) {
                    startCrop(data.getData());
                }
                break;
            case ACT_CROP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        Bitmap mHeadBitmap = FileUtils.getZoomImage(photo, 5 * 100);
                        mSelectPicture.setImageBitmap(mHeadBitmap);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startGallery() {
        Intent intent = CameraUtil.openGallery();
        startActivityForResult(intent, ACT_GALLERY);
    }

    private void startCamera() {
        //request the camera permission dynamic above android 6.0
        boolean permission = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (permission) {
            Intent intent = CameraUtil.openCamera();
            startActivityForResult(intent, ACT_CAMERA);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, ACT_PERMISSION);
        }

    }

    private void startCrop(Uri inUri) {
        Intent intent = CameraUtil.cropPicture(inUri);
        startActivityForResult(intent, ACT_CROP);
    }

    // 弹窗选择头像图片
    private void setHeadImage() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_bottom, null);
        //初始化控件
        TextView mTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        mTitle.setTextColor(Color.RED);
        mTitle.setTextSize(18);
        mTitle.setText("拍照");
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startCamera();
            }
        });
        TextView mEmptyChat = (TextView) inflate.findViewById(R.id.empty_chat);
        mEmptyChat.setTextColor(Color.RED);
        mEmptyChat.setText("从相册中选择");
        mEmptyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startGallery();
            }
        });
        TextView mCencel = (TextView) inflate.findViewById(R.id.cancel);
        mCencel.setText("取消");
        mCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    @OnClick({R.id.left_bar_item, R.id.right_title_bar, R.id.select_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                //Utils.finish(SetRemarkActivity.this);


                Intent intent = new Intent(SetRemarkActivity.this, SendChatActivity.class);
                intent.putExtra(Constants.NAME,mNickName );
                intent.putExtra(Constants.HEADURL, mHeadUrl);
                intent.putExtra(Constants.PHONE, mPhoneNumber);
                intent.putExtra(Constants.User_ID, mUserID);
                startActivity(intent);
                finish();
                break;
            case R.id.right_title_bar:
                setFriendInfo();
                break;
            case R.id.select_picture:
                startGallery();
                break;
            default:
                break;
        }
    }

    /**
     * @方法名: getFriendInfo
     * @参数：
     * @返回值：
     * @描述:  获取好友备注详情
     * @作者： CHENHUI
     * @创建日期 2018/11/20 10:12
     */
    private void getFriendInfo() {
        HashMap<String,String> map = new HashMap<String,String>();
        String uid=mUserID;
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("uid", uid);
        HttpUtils.doPostFormMap(this, Request.Path.FRIEND_REMARKINFO, map, true, Request.Code.FRIEND_REMARKINFO, this);

    }
    /**
     * @方法名: setFriendInfo
     * @参数：
     * @返回值：
     * @描述:  保存修改备注详情
     * @作者： CHENHUI
     * @创建日期 2018/11/20 10:19
     */
    private void setFriendInfo() {


        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        map.put("uid", mUserID);
        map.put("remark", mRemarkName.getText().toString());
        map.put("mobile",mPhonenumber.getText().toString());
        map.put("desc",mDescrpition.getText().toString());
        HttpUtils.doPostFormMap(this, Request.Path.FRIEND_UPDATEFRIEND, map, true, Request.Code.FRIEND_UPDATEFRIEND, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.FRIEND_REMARKINFO://获取备注信息
                RemarkInfo items = (RemarkInfo)o;

                mRemarkName.setText(items.getRemark());
                //mLabel.setText(items.getLabel());
                mPhonenumber.setText(items.getMobile());
                mDescrpition.setText(items.getDesc());
                break;
            case Request.Code.FRIEND_UPDATEFRIEND://设置备注信息
                Toast.makeText(this, "修改备注信息成功！", Toast.LENGTH_SHORT).show();
                String remarkName=mRemarkName.getText().toString();
                String phoneNumber=mPhonenumber.getText().toString();
                if(!TextUtils.isEmpty(remarkName)){
                    LWFriendManager.getInstance().queryFriendItem(mUserID).setRemark(remarkName);
                    LWConversationManager.getInstance().updateConversationUserName(mUserID,remarkName);
                }
                if(!TextUtils.isEmpty(phoneNumber)){
                    LWFriendManager.getInstance().queryFriendItem(mUserID).setMobile(phoneNumber);
                }
                Intent intent = new Intent(SetRemarkActivity.this, SendChatActivity.class);
                intent.putExtra(Constants.NAME, remarkName);
                intent.putExtra(Constants.HEADURL, mHeadUrl);
                intent.putExtra(Constants.PHONE, phoneNumber);
                intent.putExtra(Constants.User_ID, mUserID);
                startActivity(intent);
                finish();


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
            case Request.Code.FRIEND_REMARKINFO:
                type = new TypeToken<BaseResponse<RemarkInfo>>() {
                }.getType();
                break;
            case Request.Code.FRIEND_UPDATEFRIEND:
                type = new TypeToken<BaseResponse<UpdateFriend>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }
}
