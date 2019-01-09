package com.lw.italk.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.lw.italk.R;
import com.lw.italk.framework.common.LWConversationManager;
import com.lw.italk.utils.Constants;

/**
 * Created by Administrator on 2018/9/11 0011.
 */


public class LocalVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    public static final String VIDEO_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video);
        String path = getIntent().getStringExtra("remotepath");

        String localpath = getIntent().getStringExtra("localpath");
        if (localpath != null){
            path = localpath;
        }
        //本地的视频  需要在手机SD卡根目录添加一个 fl1234.mp4 视频
//        String videoUrl1 = Environment.getExternalStorageDirectory().getPath()+"/fl1234.mp4" ;
        videoView = (VideoView) this.findViewById(R.id.videoView);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);

        //网络视频
//        String videoUrl2 = VIDEO_URL ;
        if (!TextUtils.isEmpty(path)) {
            Uri uri = Uri.parse(path);
            //设置视频控制器
//            videoView.setMediaController(new MediaController(this));

            //播放完成回调
            videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());

            //设置视频路径
            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                }
            });
            //开始播放视频
            videoView.start();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying() && videoView.canPause()) {
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying()) {
            videoView.resume();
        }
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
//            Toast.makeText( LocalVideoActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}
