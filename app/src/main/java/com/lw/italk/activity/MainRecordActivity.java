package com.lw.italk.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lw.italk.R;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.view.camara.FocusSurfaceView;
import com.lw.italk.view.camara.MyVideoView;
import com.lw.italk.view.camara.RecordedButton;
import com.lw.italk.view.camara.TypeButton;
import com.yixia.camera.MediaRecorderBase;
import com.yixia.camera.MediaRecorderNative;
import com.yixia.camera.VCamera;
import com.yixia.camera.model.MediaObject;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by wangzhongyu on 18/9/12
 */
public class MainRecordActivity extends Activity implements View.OnClickListener{
    private static final int REQUEST_KEY = 100;
    private static final int HANDLER_RECORD = 200;
    private static final int HANDLER_EDIT_VIDEO = 201;

    private MediaRecorderNative mMediaRecorder;
    private MediaObject mMediaObject;
    private FocusSurfaceView sv_ffmpeg;
    private RecordedButton rb_start;
    private RelativeLayout rl_bottom;
    private RelativeLayout rl_bottom2;
    private TypeButton iv_back;
    private TextView tv_hint;
    private TextView textView;
    private MyVideoView vv_play;
    private ImageView mSwitchCamera;
    private ImageView mPhoto;
    private boolean mIsTakePic = false;
    private long mProcess;
    private String mUrl;


    //最大录制时间
    private int maxDuration = 10500;
    //本次段落是否录制完成
    private boolean isRecordedOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mainrecord);

        sv_ffmpeg = (FocusSurfaceView) findViewById(R.id.sv_ffmpeg);
        rb_start = (RecordedButton) findViewById(R.id.rb_start);
        vv_play = (MyVideoView) findViewById(R.id.vv_play);
        TypeButton iv_finish = (TypeButton) findViewById(R.id.iv_finish);
        iv_back = (TypeButton) findViewById(R.id.iv_back);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        rl_bottom2 = (RelativeLayout) findViewById(R.id.rl_bottom2);
        TypeButton iv_next = (TypeButton) findViewById(R.id.iv_next);
        TypeButton iv_close = (TypeButton) findViewById(R.id.iv_close);
        mSwitchCamera = (ImageView) findViewById(R.id.image_switch);
        mPhoto = (ImageView) findViewById(R.id.image_photo);

        initMediaRecorder();

        sv_ffmpeg.setTouchFocus(mMediaRecorder);

        rb_start.setMax(maxDuration);

        rb_start.setOnGestureListener(new RecordedButton.OnGestureListener() {
            @Override
            public void onLongClick() {
                mProcess = 0;
                isRecordedOver = false;
                mIsTakePic = false;
                mMediaRecorder.startVideoRecord();
                rb_start.setSplit();
                myHandler.sendEmptyMessageDelayed(HANDLER_RECORD, 100);
            }
            @Override
            public void onClick() {
                Log.e("123qwe", "rb_start onClick");
                mIsTakePic = true;
                mMediaRecorder.takePicture(new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        Log.e("123qwe", "onPictureTaken");
//                        rb_start.closeButton();
                        changeButton(false);
                        rb_start.closeButton();
                        rb_start.setVisibility(View.GONE);
                        rl_bottom2.setVisibility(View.VISIBLE);
//                        mPhoto.setVisibility(View.VISIBLE);
//                        YuvImage image = new YuvImage(data, ImageFormat.NV21, MediaRecorderBase.VIDEO_WIDTH, MediaRecorderBase.VIDEO_HEIGHT, null);
//                        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
//                        image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 70, outputSteam); // 将NV21格式图片，以质量70压缩成Jpeg，并得到JPEG数据流
//                        byte[] jpegData = outputSteam.toByteArray();                                                //从outputSteam得到byte数据

//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inSampleSize = 1;
//                        BitmapFactory.decodeStream()
//                        Bitmap bmp = BitmapFactory.decodeByteArray(jpegData, null, options);

                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        mUrl = CommonUtils.savePicture(bitmap, System.currentTimeMillis() + ".jpg");
//                        mPhoto.setImageBitmap(bitmap);
                    }
                });
            }
            @Override
            public void onLift() {
                Log.e("123qwe", "onLift");
                isRecordedOver = true;
                mIsTakePic = false;
                rb_start.closeButton();
                if(rb_start.getCurrentPro()<1500){
                    Toast.makeText(MainRecordActivity.this, "视频太短，请重新录制", Toast.LENGTH_SHORT).show();
                    mMediaRecorder.stopPreview();
                    mMediaRecorder.startPreview();
                    initMediaRecorderState();
                }else {
                    mMediaRecorder.stopVideoRecord(new MediaRecorderBase.StopRecordCallback() {
                        @Override
                        public void recordResult(String url) {
                            mUrl = url;
                            videoFinish();
                        }
                    });
                }
//                isRecordedOver = true;
//                mMediaRecorder.stopRecord();
//                changeButton(mMediaObject.getMediaParts().size() > 0);
            }
            @Override
            public void onOver() {
                Log.e("123qwe", "onOver");
                isRecordedOver = true;
                mIsTakePic = false;
                rb_start.closeButton();
                mMediaRecorder.stopVideoRecord(new MediaRecorderBase.StopRecordCallback() {
                    @Override
                    public void recordResult(String url) {
                        Log.e("123qwe", "recordResult url:" + url);
                        mUrl = url;
                        videoFinish();
                    }
                });
            }
        });

        iv_back.setOnClickListener(this);
        iv_finish.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        mSwitchCamera.setOnClickListener(this);
    }

    private void changeButton(boolean flag){

        if(flag){
            tv_hint.setVisibility(View.VISIBLE);
            rl_bottom.setVisibility(View.VISIBLE);
            mSwitchCamera.setVisibility(View.VISIBLE);
        }else{
            tv_hint.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);
            mSwitchCamera.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化视频拍摄状态
     */
    private void initMediaRecorderState(){

        vv_play.setVisibility(View.GONE);
        vv_play.pause();

        rb_start.setVisibility(View.VISIBLE);
        rl_bottom2.setVisibility(View.GONE);
        changeButton(false);
        tv_hint.setVisibility(View.VISIBLE);

//        LinkedList<MediaObject.MediaPart> list = new LinkedList<>();
//        list.addAll(mMediaObject.getMediaParts());

//        for (MediaObject.MediaPart part : list){
//            mMediaObject.removePart(part, true);
//        }

        mProcess = 0;
        rb_start.setProgress(0);
        rb_start.cleanSplit();
        mPhoto.setVisibility(View.GONE);
    }

    private void videoFinish() {
        changeButton(false);
        rb_start.setVisibility(View.GONE);

//        textView = showProgressDialog();

        myHandler.sendEmptyMessage(HANDLER_EDIT_VIDEO);
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_RECORD://拍摄视频的handler
                    if(!isRecordedOver){
                        if(rl_bottom.getVisibility() == View.VISIBLE) {
                            changeButton(false);
                        }
                        mProcess += 30;
                        rb_start.setProgress(mProcess);
                        myHandler.sendEmptyMessageDelayed(HANDLER_RECORD, 30);
                    }
                    break;
                case HANDLER_EDIT_VIDEO://合成视频的handler
//                    int progress = UtilityAdapter.FilterParserAction("", UtilityAdapter.PARSERACTION_PROGRESS);
//                    if(textView != null) textView.setText("视频编译中 "+mProcess+"%");
//                    if (progress == 100) {
                    mMediaRecorder.stopPreview();
                    syntVideo();
//                    } else if (progress == -1) {
//                        closeProgressDialog();
//                        Toast.makeText(getApplicationContext(), "视频合成失败", Toast.LENGTH_SHORT).show();
//                    } else {
//                        sendEmptyMessageDelayed(HANDLER_EDIT_VIDEO, 30);
//                    }
                    break;
            }
        }
    };

    /**
     * 合成视频
     */
    private void syntVideo(){

        //ffmpeg -i "concat:ts0.ts|ts1.ts|ts2.ts|ts3.ts" -c copy -bsf:a aac_adtstoasc out2.mp4
//        StringBuilder sb = new StringBuilder("ffmpeg");
//        sb.append(" -i");
//        String concat="concat:";
//        for (MediaObject.MediaPart part : mMediaObject.getMediaParts()){
//            concat+=part.mediaPath;
//            concat += "|";
//        }
//        concat = concat.substring(0, concat.length()-1);
//        sb.append(" "+concat);
//        sb.append(" -c");
//        sb.append(" copy");
//        sb.append(" -bsf:a");
//        sb.append(" aac_adtstoasc");
//        sb.append(" -y");
//        String output = MyApplication.VIDEO_PATH+"/finish.mp4";
//        sb.append(" "+output);
//
//        int i = UtilityAdapter.FFmpegRun("", sb.toString());
//        closeProgressDialog();
        File videoFile = new File(mUrl);
        Log.e("123qwe", "mUrl:" + mUrl);
        if(videoFile != null){
            rl_bottom2.setVisibility(View.VISIBLE);
            vv_play.setVisibility(View.VISIBLE);
            String providerPath = MainRecordActivity.this.getPackageName() + ".fileProvider";
            Log.e("123qwe", "providerPath:" + providerPath);
            Log.e("123qwe", "providerPath1:" + getExternalFilesDir(""));
            Uri contentUri = FileProvider.getUriForFile(MainRecordActivity.this, providerPath, videoFile);
//            Uri uri = FileUtils.getImageContentUri(MainActivity.this, mUrl);
            vv_play.setVideoPath(contentUri);
            Log.e("123qwe", "contentUri:" + contentUri.getPath());
            vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    vv_play.setLooping(true);
                    vv_play.start();
                }
            });
            if(vv_play.isPrepared()){
                vv_play.setLooping(true);
                vv_play.start();
            }
        }else{
            Toast.makeText(getApplicationContext(), "视频合成失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化录制对象
     */
    private void initMediaRecorder() {

        mMediaRecorder = new MediaRecorderNative();
        String key = String.valueOf(System.currentTimeMillis());
        //设置缓存文件夹
        mMediaObject = mMediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath());
        //设置视频预览源
        mMediaRecorder.setSurfaceHolder(sv_ffmpeg.getHolder());
        //准备
        mMediaRecorder.prepare();
        //滤波器相关
//        UtilityAdapter.freeFilterParser();
//        UtilityAdapter.initFilterParser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaRecorder.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaRecorder.stopPreview();
    }

    @Override
    public void onBackPressed() {
        if(rb_start.getSplitCount() == 0) {
            super.onBackPressed();
        }else{
            initMediaRecorderState();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_KEY){
                initMediaRecorderState();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                if(rb_start.isDeleteMode()){//判断是否要删除视频段落
                    MediaObject.MediaPart lastPart = mMediaObject.getPart(mMediaObject.getMediaParts().size() - 1);
                    mMediaObject.removePart(lastPart, true);
                    rb_start.setProgress(mMediaObject.getDuration());
                    rb_start.deleteSplit();
                    changeButton(mMediaObject.getMediaParts().size() > 0);
//                    iv_back.setImageResource(R.mipmap.video_delete);
                }else if(mMediaObject.getMediaParts().size() > 0){
                    rb_start.setDeleteMode(true);
//                    iv_back.setImageResource(R.mipmap.video_delete_click);
                }
                break;
            case R.id.iv_finish:
                videoFinish();
                break;
            case R.id.iv_next:
//                rb_start.setDeleteMode(false);
                Log.e("123qwe", "iv_next click mIsTakePic:" + mUrl);
                Intent intent = new Intent();
                intent.putExtra("path", mUrl);
                MainRecordActivity.this.setResult(RESULT_OK, intent);
                finish();
//                startActivityForResult(intent, REQUEST_KEY);
                break;
            case R.id.iv_close:
                Log.e("123qwe", "iv_back click mIsTakePic:" + mIsTakePic);
//                if (mPhoto.getVisibility() == View.VISIBLE) {
//                    Log.e("123qwe", "iv_back click GONE");
//                    mPhoto.setVisibility(View.GONE);
//                }
//                if (mIsTakePic) {
                    mMediaRecorder.startPreview();
//                }
                initMediaRecorderState();
                break;
            case R.id.image_switch:
                mMediaRecorder.switchCamera();
                break;
        }
    }
}
