package com.lw.italk.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;
import com.italkmind.client.vo.api.AvatarInfo;
import com.lw.italk.App;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.activity.ChangeNickNameActivity;
import com.lw.italk.activity.QRCodeActivity;
import com.lw.italk.activity.UserSettingActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.UserInfo;
import com.lw.italk.gson.user.SetProfile;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.RetrofitCallback;
import com.lw.italk.http.ThirdLibs;
import com.lw.italk.utils.CameraUtil;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.utils.FileUtils;
import com.lw.italk.utils.GlideCircleTransform;
import com.lw.italk.utils.ItalkLog;
import com.lw.italk.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 喜明 on 2018/8/12.
 */

public class MyCenterFragment extends Fragment implements Response
{

    Unbinder unbinder;
    @BindView(R.id.fm_head_img)
    ImageView mFmHeadImg;
    @BindView(R.id.fm_nick_name)
    TextView mFmNickName;
    @BindView(R.id.fm_setting_nickname)
    TextView mFmSettingNickname;
    @BindView(R.id.fm_setting_qrcode)
    TextView mFmSettingQrcode;
    private final int ACT_GALLERY = 0;
    private final int ACT_CROP = 2;
    @BindView(R.id.fm_setting_gender)
    TextView mSettingGender;
    @BindView(R.id.tv_setting_gender)
    TextView mGender;
    @BindView(R.id.fm_setting)
    TextView fmSetting;
    @BindView(R.id.tv_setting_nickname)
    TextView mSettingNickname;
    @BindView(R.id.gender_img)
    ImageView mGenderImg;


    private static final int REQUEST_UPLOAD_FILE_CODE = 20000;
    private Uri picUri;

    private final int PHOTO_REQUEST_CUT = 5;
    private Uri uritempFile;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = View.inflate(getActivity(), R.layout.fragment_my_center, null);
        unbinder = ButterKnife.bind(this, view);
        showUserInfo();
        return view;
    }


    /**
     * @方法名: showUserInfo
     * @参数：
     * @返回值：
     * @描述:  显示用户基本信息
     * @作者： CHENHUI
     * @创建日期 2018/11/16 16:40
     */
    private void showUserInfo(){
        if (LWDBManager.getInstance().getUserInfo() != null)
        {
            UserInfo userInfo=LWDBManager.getInstance().getUserInfo();
            String avater=userInfo.getAvatar();//"http://47.96.126.173:8886/head-image/195/811/05/avatar26788604169568256.jpg?tag=1733633909";//"http://47.96.126.173:8886/head-image/700/649/25/avatar26786343464878080.jpg?tag=1037627444";//userInfo.getAvatar();//头像
            String userName=userInfo.getUsername();//昵称
            //ItalkLog.e("-----------显示头像-----------" + avater);

            Glide.with(this).load(avater)
                    .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                    .error(R.drawable.default_img)
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .transform(new GlideCircleTransform(App.getInstance()))
                    .into(mFmHeadImg);


            mFmNickName.setText(userName);
            mSettingNickname.setText(userName);
            if (LWDBManager.getInstance().getUserInfo().getSex() == 1)
            {
                mGender.setText("男");
                mGenderImg.setBackground(getResources().getDrawable(R.drawable.boy));
            }
            else if (LWDBManager.getInstance().getUserInfo().getSex() == 2)
            {
                mGender.setText("女");
                mGenderImg.setBackground(getResources().getDrawable(R.drawable.girl));
            }

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        showUserInfo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            showUserInfo();
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode)
        {
            case PHOTO_REQUEST_CUT:
                    //将Uri图片转换为Bitmap
                try {
                    Bitmap photo = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uritempFile));
                    System.out.println("---startCrop-PHOTO_REQUEST_CUT--"+photo);
                    Bitmap mHeadBitmap = FileUtils.getZoomImage(photo, 5 * 100);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    mHeadBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes = baos.toByteArray();

                    Glide.with(this).load(bytes)
                            .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                            .transform(new GlideCircleTransform(App.getInstance()))
                            .into(mFmHeadImg);
                    String path = CommonUtils.savePicture(photo, "aite_headimg.jpg");// 保存图片
                    Log.e("zxc", path);
                    File avatarFile = new File(path);
                    ThirdLibs.getInstance().uploadAvatar(avatarFile, LWUserManager.getInstance().getToken(), new RetrofitCallback<ResponseBody>()
                    {
                        @Override
                        public void onSuccess(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
                        {
                            try
                            {
                                String body = response.body().string();
                                AvatarInfo avatarInfo = AvatarInfo.jsonToAvatar(body);
                                LWDBManager.getInstance().getUserInfo().setAvatar(avatarInfo.getAvatar());
                                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                                System.out.println("-----avatarInfo.getAvatar()---====" + avatarInfo.getAvatar());
                                LWUserManager.getInstance().getUserInfo().setAvatar(avatarInfo.getAvatar());
                                showUserInfo();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLoading(long total, long progress)
                        {

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t)
                        {
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //TODO，将裁剪的bitmap显示在imageview控件上
                    break;

            case ACT_GALLERY:
                /*File files = new File(Environment.getExternalStorageDirectory(), "pic.jpg");
                //将图片处理成大小符合要求的文件
                Uri picUris = Uri.fromFile(files);*/
                if (data != null)
                {

                    startCrop(data.getData());
                }
                break;
            case ACT_CROP:
                if (data != null)
                {
                    Bundle extras = data.getExtras();
                    if (extras != null)
                    {
                         Bitmap photo = extras.getParcelable("data");
                        Bitmap mHeadBitmap = FileUtils.getZoomImage(photo, 5 * 100);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        mHeadBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytes = baos.toByteArray();

                        Glide.with(this).load(bytes)
                                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                                .transform(new GlideCircleTransform(App.getInstance()))
                                .into(mFmHeadImg);
                        String path = CommonUtils.savePicture(photo, "aite_headimg.jpg");// 保存图片
                        Log.e("zxc", path);
                        File avatarFile = new File(path);
                        ThirdLibs.getInstance().uploadAvatar(avatarFile, LWUserManager.getInstance().getToken(), new RetrofitCallback<ResponseBody>()
                        {
                            @Override
                            public void onSuccess(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
                            {
                                try
                                {
                                    String body = response.body().string();
                                    AvatarInfo avatarInfo = AvatarInfo.jsonToAvatar(body);
                                    LWDBManager.getInstance().getUserInfo().setAvatar(avatarInfo.getAvatar());
                                    LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                                    System.out.println("-----avatarInfo.getAvatar()---====" + avatarInfo.getAvatar());
                                    LWUserManager.getInstance().getUserInfo().setAvatar(avatarInfo.getAvatar());
                                    showUserInfo();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onLoading(long total, long progress)
                            {

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t)
                            {
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;

            case REQUEST_UPLOAD_FILE_CODE:
                //拍照后选择剪切后的图片
                if (resultCode == RESULT_OK)
                {
                    if (null != picUri)
                    {
                        File file = new File(Environment.getExternalStorageDirectory(), "pic.jpg");
                        //将图片处理成大小符合要求的文件
                        picUri = Uri.fromFile(file);
                        startCrop(picUri);
                    }
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * @方法名: setHeadImage
     * @参数：
     * @返回值：
     * @描述: 弹窗选择拍照还是从相册选择图片设置头像
     * @作者： CHENHUI
     * @创建日期 2018/11/16 15:15
     */
    private void setHeadImage()
    {
        final Dialog dialog = new Dialog(getContext(), R.style.dialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom, null);
        //初始化控件
        TextView mTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        mTitle.setTextColor(Color.RED);
        mTitle.setTextSize(18);
        mTitle.setText("拍照");
        mTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                //调用相机拍照
                picture();
            }
        });
        TextView mEmptyChat = (TextView) inflate.findViewById(R.id.empty_chat);
        mEmptyChat.setTextColor(Color.RED);
        mEmptyChat.setText("从相册中选择");
        mEmptyChat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                //选择相册图片
                startGallery();
            }
        });
        TextView mCencel = (TextView) inflate.findViewById(R.id.cancel);
        mCencel.setText("取消");
        mCencel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null)
        {
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

    /**
     * @方法名: picture
     * @参数：
     * @返回值：
     * @描述: 调用相机进行拍照选择图片
     * @作者： CHENHUI
     * @创建日期 2018/11/16 15:24
     */
    private void picture()
    {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStorageDirectory(), "pic.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            if (!photo.exists())
            {
                try
                {
                    photo.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            picUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", photo);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        }
        else
        {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
            picUri = Uri.fromFile(photo);
        }
        startActivityForResult(cameraIntent, REQUEST_UPLOAD_FILE_CODE);
    }

    /**
     * @方法名: startGallery
     * @参数：
     * @返回值：
     * @描述: 调用本地相册
     * @作者： CHENHUI
     * @创建日期 2018/11/16 15:25
     */
    private void startGallery()
    {
        Intent intent = CameraUtil.openGallery();
        startActivityForResult(intent, ACT_GALLERY);
    }

    /**
     * @方法名: startCrop
     * @参数：
     * @返回值：
     * @描述: 照片剪切
     * @作者： CHENHUI
     * @创建日期 2018/11/16 15:25
     */
    private void startCrop(Uri inUri)
    {
        //System.out.println("Build.MODEL----->"+Build.MODEL);获取机型
       /* if (Build.MODEL.contains("MI")) {*/
            cropPicture1(inUri);
        /*}else {
            Intent intent = CameraUtil.cropPicture(inUri);
            startActivityForResult(intent, ACT_CROP);
        }*/

    }

    /**
     * 剪切图片
     * @param uri
     */
    public   void cropPicture1(Uri uri) {
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

        //Uri uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        String path="file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "pic.jpg";
         uritempFile = Uri.parse(path);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        /*if (Build.MODEL.contains("MI")) {
             uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "pic.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        } else {
            intent.putExtra("return-data", true);
        }*/

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @OnClick({R.id.fm_head_img, R.id.fm_setting_nickname, R.id.fm_setting_qrcode, R.id.fm_setting_gender, R.id.fm_setting})
    public void onViewClicked(View view)
    {
        Intent intent = new Intent();
        switch (view.getId())
        {
            case R.id.fm_head_img:
                //点击头像照片
                //弹出选项框是拍照还是选择本地照片设置头像
                setHeadImage();
                break;
            case R.id.fm_setting_nickname:
                intent.setClass(getContext(), ChangeNickNameActivity.class);
                startActivity(intent);
                break;
            case R.id.fm_setting_qrcode:
                intent.setClass(getContext(), QRCodeActivity.class);
                startActivity(intent);
                break;
            case R.id.fm_setting_gender:
                selectSex();
                break;
            case R.id.fm_setting:
                Utils.start_Activity(getActivity(), UserSettingActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * @方法名: selectSex
     * @参数：
     * @返回值：
     * @描述: 选择性别
     * @作者：
     * @创建日期 2018/11/16 15:26
     */
    private void selectSex()
    {
        final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bottom, null);
        //初始化控件
        TextView mTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        mTitle.setText("男");
        mTitle.setTextSize(16);
        mTitle.setTextColor(Color.BLACK);
        mTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                mGender.setText("男");
                mGenderImg.setBackground(getResources().getDrawable(R.drawable.boy));
                changeGenderRequest();
            }
        });
        TextView mEmptyChat = (TextView) inflate.findViewById(R.id.empty_chat);
        mEmptyChat.setText("女");
        mEmptyChat.setTextSize(16);
        mEmptyChat.setTextColor(Color.BLACK);
        mEmptyChat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                mGender.setText("女");
                mGenderImg.setBackground(getResources().getDrawable(R.drawable.girl));
                changeGenderRequest();
            }
        });
        TextView mCencel = (TextView) inflate.findViewById(R.id.cancel);
        mCencel.setText("取消");
        mCencel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null)
        {
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

    /**
     * @方法名: changeGenderRequest
     * @参数：
     * @返回值：
     * @描述: 更新性别
     * @作者：
     * @创建日期 2018/11/16 15:29
     */
    private void changeGenderRequest()
    {
        Map<String, String> requestItem = new HashMap<String, String>();
        requestItem.put("infoValue", String.valueOf(mGender.getText().equals("男") ? 1 : 2));
        requestItem.put("infoKey", String.valueOf(4));//性别
        requestItem.put("tokenId", LWUserManager.getInstance().getToken());//性别
        HttpUtils.doPostFormMap(getActivity(), Request.Path.USER_EDITINFO, requestItem, false, Request.Code.USER_EDITINFO, this);
    }

    @Override
    public void next(Object o, int requestCode)
    {
        switch (requestCode)
        {
            case Request.Code.USER_EDITINFO:
                //保存修改信息到DB中
                long sex = 0;
                if (mGender.getText().equals("男"))
                {
                    sex = 1;
                }
                else if (mGender.getText().equals("女"))
                {
                    sex = 2;
                }
                LWDBManager.getInstance().getUserInfo().setSex(sex);
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                LWUserManager.getInstance().getUserInfo().setSex(sex);
                break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode)
    {
        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public Type getTypeToken(int requestCode)
    {
        Type type = null;
        switch (requestCode)
        {
            case Request.Code.USER_EDITINFO:
                type = new TypeToken<BaseResponse<SetProfile>>()
                {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }
}
