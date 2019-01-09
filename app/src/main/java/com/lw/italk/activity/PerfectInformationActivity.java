package com.lw.italk.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.OssManager;
import com.lw.italk.gson.user.SetProfile;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.utils.FileUtils;
import com.lw.italk.utils.GlideCircleTransform;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by lxm on 2018/8/11.
 */
public class PerfectInformationActivity extends BaseActivity implements View.OnClickListener, Response{
    private static final int PHOTO_TAKE = 1;// 点击拍照回调标识
    private static final int PHOTO_PICK = 2;// 点击选择相册回调标识
    private static final int PHOTO_CLIP = 3;// 点击裁剪图片回调标识

    private EditText mNickName;
    private TextView mGender;
    private TextView mBirthday;
    private ImageView mLogo;
    private ImageView mArrow;
    private ImageView mSelectBirthday;
    private RelativeLayout mGenderLayout;
    private RadioGroup mRadioGroup;
    private RadioButton mMale;
    private RadioButton mFemale;
    private Bitmap mHeadBitmap;
    private Button mComplete;

    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_perfect_infomation;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    private void init() {
        mNickName = (EditText) findViewById(R.id.et_nick_name);
        mGender = (TextView) findViewById(R.id.gender);
        mGender.setOnClickListener(this);
        mBirthday = (TextView) findViewById(R.id.birthday);
        mBirthday.setOnClickListener(this);
        mLogo = (ImageView) findViewById(R.id.logo);
        mLogo.setOnClickListener(this);
        mArrow = (ImageView) findViewById(R.id.arrow);
        mArrow.setOnClickListener(this);
        mSelectBirthday = (ImageView) findViewById(R.id.iv_birthday);
        mSelectBirthday.setOnClickListener(this);
        mGenderLayout = (RelativeLayout) findViewById(R.id.select_gender);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mMale = (RadioButton) findViewById(R.id.btnMan);
        mFemale = (RadioButton) findViewById(R.id.btnWoman);
        mComplete = (Button) findViewById(R.id.complete);
        mComplete.setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mGenderLayout.setVisibility(View.GONE);
                mBirthday.setEnabled(true);
                if (mMale.getId() == i) {
                    mGender.setText("男");
                }
                if (mFemale.getId() == i) {
                    mGender.setText("女");
                }
            }
        });

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.birthday:
            case R.id.iv_birthday:
                new DatePickerDialog(PerfectInformationActivity.this, onDateSetListener, mYear, mMonth, mDay).show();
                break;
            case R.id.gender:
            case R.id.arrow:
                mBirthday.setEnabled(false);
                mGenderLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.logo:
                setHeadImage();
                break;
            case R.id.complete:
                if(!TextUtils.isEmpty(mNickName.getText().toString())){
                    changeUserInfo();
                }else{
                    Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            }
            mBirthday.setText(days);
        }
    };

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
                Photograph();
            }
        });
        TextView mEmptyChat = (TextView) inflate.findViewById(R.id.empty_chat);
        mEmptyChat.setTextColor(Color.RED);
        mEmptyChat.setText("从相册中选择");
        mEmptyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                selectAlbumPhoto();
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
        if(dialogWindow == null){
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
//        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    // 点击拍照
    private void Photograph() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(),
                FileUtils.TEMP_SHOP_PHOTO)));
        startActivityForResult(intent, PHOTO_TAKE);
    }

    // 点击选择相册
    private void selectAlbumPhoto() {
        // 相册中选择
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_PICK);
    }

    // 调用系统中自带的图片剪裁
    private void photoClip(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }

    // 回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_TAKE:
                switch (resultCode) {
                    case -1:// -1表示拍照成功
                        File file = new File(Environment.getExternalStorageDirectory()
                                + "/" + FileUtils.TEMP_SHOP_PHOTO);
                        if (file.exists()) {
                            photoClip(Uri.fromFile(file));
                        }
                        break;
                    default:
                        break;
                }
                break;
            case PHOTO_PICK:
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case PHOTO_CLIP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        mHeadBitmap = FileUtils.getZoomImage(photo, 5 * 100);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        mHeadBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytes = baos.toByteArray();

                        Glide.with(this).load(bytes)
                                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                                .transform(new GlideCircleTransform(App.getInstance()))
                                .into(mLogo);
                        String path = CommonUtils.savePicture(photo, "aite_headimg.jpg");// 保存图片
                        OssManager.getInstance().beginupload("aite_headimg.jpg", path, new OssManager.ProgressCallback() {
                            @Override
                            public void onProgressCallback(double progress) {
                            }

                            @Override
                            public void onSuccessCallback(String filename, String path, String result) {
                                if (!result.startsWith("http://") && !result.startsWith("https://")) {
                                    LWDBManager.getInstance().getUserInfo().setAvatar("https://"+result);
                                } else {
                                    LWDBManager.getInstance().getUserInfo().setAvatar(result);
                                }

                            }

                            @Override
                            public void onErrorCallback(String filename, String path, String error) {
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }

    private void changeUserInfo()  {
        Map<String, Object> requestList = new HashMap<String, Object>();
        requestList.put("userid", LWDBManager.getInstance().getUserInfo().getUid());
        Map<String, Object> requestItem = new HashMap<String, Object>();
        requestItem.put("nickname", mNickName.getText().toString());
        requestItem.put("image", LWDBManager.getInstance().getUserInfo().getAvatar());
        requestList.put("items", requestItem);
        HttpUtils.doPost(this, Request.Path.USER_SETPROFILE, requestList, true, Request.Code.USER_SETPROFILE, this);
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.USER_SETPROFILE:
                //保存修改信息到DB中
                LWDBManager.getInstance().getUserInfo().setUsername(mNickName.getText().toString());
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());

                Intent intent = new Intent(PerfectInformationActivity.this,MainActivity.class);
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
            case Request.Code.USER_SETPROFILE:
                type = new TypeToken<BaseResponse<SetProfile>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHeadBitmap != null) {
            mHeadBitmap.recycle();
        }
    }

}
