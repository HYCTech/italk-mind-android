package com.lw.italk.framework.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lw.italk.R;
import com.lw.italk.activity.ChatActivity;
import com.lw.italk.entity.LWMessage;
import com.lw.italk.entity.VoiceMessageBody;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.utils.CountdownThread;

import java.io.File;

/**
 * Created by Administrator on 2018/8/19 0019.
 */

public class VoicePlayClickListener implements View.OnClickListener {

    MsgItem message;
//    VoiceMessageBody voiceBody;
    ImageView voiceIconView;

    private AnimationDrawable voiceAnimation = null;
    MediaPlayer mediaPlayer = null;
    ImageView iv_read_status;
    TextView tv_mark;
    Activity activity;
    private int chatType;
    private BaseAdapter adapter;

    public static boolean isPlaying = false;
    public static VoicePlayClickListener currentPlayListener = null;

    /**
     *
     * @param message
     * @param v
     * @param iv_read_status
     * @param activity
     */
    public VoicePlayClickListener(MsgItem message, ImageView v,
                                  ImageView iv_read_status, TextView tv_mark, BaseAdapter adapter, Activity activity,
                                  String username) {
        this.message = message;
//        voiceBody = (VoiceMessageBody) message.getBody();
        this.iv_read_status = iv_read_status;
        this.tv_mark = tv_mark;
        this.adapter = adapter;
        voiceIconView = v;
        this.activity = activity;
        this.chatType = message.getChattype();
    }

    public void stopPlayVoice() {
        voiceAnimation.stop();
        if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) {
//        if (message.direct == LWMessage.Direct.RECEIVE) {

            voiceIconView.setImageResource(R.drawable.chatfrom_voice_playing);
        } else {
            voiceIconView.setImageResource(R.drawable.chatto_voice_playing);
        }
        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
        ((ChatActivity) activity).playMsgId = null;
    }

    public void playVoice(String filePath) {
        Log.e("123qwe", "playVoice filePath:" + filePath);
        if (filePath == null){
            return;
        }
        if (!(new File(filePath).exists())) {
            return;
        }
        ((ChatActivity) activity).playMsgId = message.getMsgid() + "";
        AudioManager audioManager = (AudioManager) activity
                .getSystemService(Context.AUDIO_SERVICE);
        Log.e("123qwe", "mediaPlayer start:");
        mediaPlayer = new MediaPlayer();
//        if (EMChatManager.getInstance().getChatOptions().getUseSpeaker()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        } else {
//            audioManager.setSpeakerphoneOn(true);// 关闭扬声器
//            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
//            audioManager.setMode(AudioManager.MODE_RINGTONE);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
//        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer
                    .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            mediaPlayer.release();
                            mediaPlayer = null;
                            stopPlayVoice(); // stop animation

//                            voiceIconView.setEnabled(false);
//                            CountdownThread thread = new CountdownThread(6, 1, tv_mark, 1);
//                            thread.start();
                        }

                    });
            isPlaying = true;
            currentPlayListener = this;
            mediaPlayer.start();
            showAnimation();

            // 如果是接收的消息
//            if (message.direct == LWMessage.Direct.RECEIVE) {
            if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) {

//                try {
//                    if (!message.isAcked) {
//                        message.isAcked = true;
//                        // 告知对方已读这条消息
////                        if (chatType != LWMessage.ChatType.GroupChat)
////                            EMChatManager.getInstance().ackMessageRead(
////                                    message.getFrom(), message.getMsgId());
//                    }
//                } catch (Exception e) {
//                    message.isAcked = false;
//                }
                Log.e("123qwe", "message.getIslisten():" + message.getIslisten() + ",iv_read_status.getVisibility():" + iv_read_status.getVisibility());
                if (!message.getIslisten()&& iv_read_status != null
                        && iv_read_status.getVisibility() == View.VISIBLE) {
                    // 隐藏自己未播放这条语音消息的标志
                    iv_read_status.setVisibility(View.GONE);
                    message.setIslisten(true);
                    LWConversationManager.getInstance().updateMsg(message);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // show the voice playing animation
    private void showAnimation() {
        // play voice, and start animation
//        if (message.direct == LWMessage.Direct.RECEIVE) {
        if (message.getDirect() == LWConversationManager.DIRECT_RECEIVE) {
            voiceIconView.setImageResource(R.drawable.voice_from_icon);
        } else {
            voiceIconView.setImageResource(R.drawable.voice_to_icon);
        }
        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
        voiceAnimation.start();
    }

    @Override
    public void onClick(View v) {
        String st = activity.getResources().getString(
                R.string.Is_download_voice_click_later);
        if (isPlaying) {
            if (((ChatActivity) activity).playMsgId != null
                    && ((ChatActivity) activity).playMsgId.equals(message
                    .getMsgid()+"")) {
                currentPlayListener.stopPlayVoice();
                return;
            }
            currentPlayListener.stopPlayVoice();
        }

//        if (message.direct == LWMessage.Direct.SEND) {
        if (message.getDirect() == LWConversationManager.SEND) {

            // for sent msg, we will try to play the voice file directly
            playVoice(message.getLocalpath());
        } else {
            Log.e("123qwe", "message.getStatus():" + message.getStatus());
            if (message.getStatus() == LWConversationManager.SUCCESS) {
//                File file = new File(voiceBody.getLocalUrl());
//                if (file.exists() && file.isFile())
                if (!TextUtils.isEmpty(message.getLocalpath())) {
                    playVoice(message.getLocalpath());
                } else {
                    LWDownloadManager.getInstance().startDownload(message.getMsgid()+"",message.getFid() ,message.getUrl());
                }
//                else
//                    System.err.println("file not exist");

            } else if (message.getStatus() == LWConversationManager.INPROGRESS) {
                String s = new String();

                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
            } else if (message.getStatus() == LWConversationManager.FAIL) {
                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
//                        EMChatManager.getInstance().asyncFetchMessage(message);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        adapter.notifyDataSetChanged();
                    }

                }.execute();

            } else {
                if (!TextUtils.isEmpty(message.getLocalpath())) {
                    playVoice(message.getLocalpath());
                } else {
                    LWDownloadManager.getInstance().startDownload(message.getMsgid()+"",message.getFid(), message.getUrl());
                }
            }

        }
    }
}