package com.zhang.netty_lib.netty;

import android.util.Log;

import com.italkmind.client.protocol.ProtocolBodyParseHelper;
import com.italkmind.client.util.ClientTools;
import com.italkmind.client.vo.protocol.ItalkMindMessage;
import com.italkmind.client.vo.protocol.body.SystemMessage;
import com.lw.italk.utils.ItalkLog;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by 张俨 on 2018/1/10.
 */

public class NettyClientHandler extends SimpleChannelInboundHandler<ItalkMindMessage> {
    private static final String TAG = NettyClientHandler.class.getName();
    private NettyListener listener;

    public NettyClientHandler(NettyListener listener) {
        this.listener = listener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyClient.getInstance().setConnectStatus(true);
        if (listener != null){
            listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_SUCCESS);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyClient.getInstance().setConnectStatus(false);
        if (listener != null){
            listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_CLOSED);
        }
        NettyClient.getInstance().reconnect();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ItalkMindMessage byteBuf) throws Exception {
        Log.e(TAG, "thread == " + Thread.currentThread().getName());
        Log.e(TAG, "来自服务器的消息 ====》" + byteBuf.getHeader().getCmdType());
        if (byteBuf.getHeader().getCmdType() == 2) {
            SystemMessage.ConnectAuthAck ack = (SystemMessage.ConnectAuthAck) byteBuf.getBody();
            if(ack.getAuthType() == 1){
                heartBeat(channelHandlerContext,byteBuf);
            }else{
                if (listener != null){
                    listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_AUTH_FAIL);
                }
                ItalkLog.d("认证失败");
            }

        }else if(byteBuf.getHeader().getCmdType() == 4){
            //心跳回复，body为空
        }else {
            if (listener != null){
                listener.onMessageResponse(byteBuf);
            }
        }
    }

    private void heartBeat(ChannelHandlerContext ctx, ItalkMindMessage msg) {
        ctx.executor().scheduleWithFixedDelay(() -> {
            ctx.writeAndFlush(ClientTools.fetchRawMessage(ClientTools.getId(),
                    ProtocolBodyParseHelper.HEART_BEAT_REQ.getCode()));
        }, 0, 15, TimeUnit.SECONDS);
    }

}
