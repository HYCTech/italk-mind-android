package com.lw.italk.framework.common;

import android.content.Context;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;

import com.lw.italk.utils.PathUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Administrator on 2018/8/19 0019.
 */

public class VoiceRecorder {
    MediaRecorder recorder;
    static final String PREFIX = "voice";
    static final String EXTENSION = ".amr";
    private boolean isRecording = false;
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;
    private Handler handler;

    public VoiceRecorder(Handler var1) {
        this.handler = var1;
    }

    public String startRecording(String var1, String var2, Context var3) {
        this.file = null;

        try {
            if(this.recorder != null) {
                this.recorder.release();
                this.recorder = null;
            }

            this.recorder = new MediaRecorder();
            this.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            this.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            this.recorder.setAudioChannels(AudioFormat.CHANNEL_CONFIGURATION_MONO);
            this.recorder.setAudioSamplingRate(44100);
            this.recorder.setAudioEncodingBitRate(96000);
            this.voiceFileName = this.getVoiceFileName(var2);
            this.voiceFilePath = this.getVoiceFilePath();
            this.file = new File(this.voiceFilePath);
            this.recorder.setOutputFile(this.file.getAbsolutePath());
            this.recorder.prepare();
            this.isRecording = true;
            this.recorder.start();
        } catch (IOException var5) {
            Log.e("voice", "prepare() failed");
        }

        (new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        if(VoiceRecorder.this.isRecording) {
                            Message var1 = new Message();
                            var1.arg1 = 0;
                            var1.what = VoiceRecorder.this.recorder.getMaxAmplitude() * 13 / 32767;
                            VoiceRecorder.this.handler.sendMessage(var1);

                            long diff = (new Date()).getTime() - startTime;
                            if (diff > 60 * 1000){
                                Message var2 = new Message();
                                var1.arg1 = 1;
                                VoiceRecorder.this.handler.sendMessage(var2);
                            }
                            SystemClock.sleep(100L);
                            continue;
                        }
                    } catch (Exception var2) {
                        Log.e("voice", var2.toString());
                    }

                    return;
                }
            }
        })).start();
        this.startTime = (new Date()).getTime();
        Log.d("voice", "start voice recording to file:" + this.file.getAbsolutePath());
        return this.file == null?null:this.file.getAbsolutePath();
    }

    public void discardRecording() {
        if(this.recorder != null) {
            try {
                this.recorder.stop();
                this.recorder.release();
                this.recorder = null;
                if(this.file != null && this.file.exists() && !this.file.isDirectory()) {
                    this.file.delete();
                }
            } catch (IllegalStateException var2) {
                ;
            } catch (RuntimeException var3) {
                ;
            }

            this.isRecording = false;
        }

    }

    public int stopRecoding() {
        if(this.recorder != null) {
            this.isRecording = false;
            try{
                this.recorder.stop();
                this.recorder.release();
                this.recorder = null;
            }catch (Exception e){
                this.recorder.release();
                this.recorder = null;
            }
            if(this.file != null && this.file.exists() && this.file.isFile() && this.file.length() == 0L) {
                this.file.delete();
                return -1011;
            } else {
                int var1 = (int)((new Date()).getTime() - this.startTime) / 1000;
                Log.d("voice", "voice recording finished. seconds:" + var1 + " file length:" + this.file.length());
                return var1;
            }
        } else {
            return 0;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if(this.recorder != null) {
            this.recorder.release();
        }

    }

    public String getVoiceFileName(String var1) {
        Time var2 = new Time();
        var2.setToNow();
        return var1 + var2.toString().substring(0, 15) + ".amr";
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    public String getVoiceFilePath() {
        return PathUtil.getInstance().getVoicePath() + "/" + this.voiceFileName;
    }
}
