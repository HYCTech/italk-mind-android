/**  
* Copyright © 2010 - 2018 All Rights Reserved Powered by italkmind
* @Title: LinkTools.java
* @version V1.0  
*/

package com.italkmind.client.util;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.italkmind.client.protocol.ProtocolBodyParse;
import com.italkmind.client.protocol.ProtocolBodyParseHelper;
import com.italkmind.client.vo.protocol.ItalkMindMessage;
import com.italkmind.client.vo.protocol.body.HeaderMessage;
import com.italkmind.client.vo.protocol.body.HeaderMessage.ItalkMindHeader;


/**
 * @ClassName: LinkTools
 * @Description: 通用工具类
 * @author fern
 * @date 2018年8月23日
 *
 */

public abstract class ClientTools {
    private static final int MSG_CHECK_CODE = 'I' << 24 | 'M' << 16;
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F' };

    public static ProtocolBodyParse fetchProtocolBodyParse(HeaderMessage.ItalkMindHeader header) {
        ProtocolBodyParse parse = ProtocolBodyParseHelper.fetchProtocolBodyParse(header.getCmdType())/*.get()*/;
//        return ProtocolBodyParseHelper.fetchProtocolBodyParse(header.getCmdType())
//                .orElseThrow(() -> new RuntimeException("该协议尚未支持" + header.getCmdType()));
        if(parse == null){
            throw new RuntimeException("该协议尚未支持" + header.getCmdType());
        }
        return parse;
    }

    public static long getId() {
        return System.nanoTime();
    }
    
    public static ItalkMindMessage fetchRawMessage(long messageId, int cmdType) {
        ItalkMindHeader.Builder builder = ItalkMindHeader.newBuilder();
        builder.setCheckCode(MSG_CHECK_CODE);
        builder.setMessageId(messageId);
        builder.setCmdType(cmdType);
        ItalkMindMessage message = new ItalkMindMessage();
        message.setHeader(builder.build());
        return message;
    }

    public static String readContentFromPost(String url, @NonNull Map<String, ?> params) throws IOException {
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(url);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        // 设置是否向connection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true
        connection.setDoOutput(true);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // 默认是 GET方式
        connection.setRequestMethod("POST");
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        // 设置本次连接是否自动重定向
        connection.setInstanceFollowRedirects(true);
        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
        // 要注意的是connection.getOutputStream会隐含的进行connect。
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
        StringBuilder content = new StringBuilder(64);
        boolean firstTag = true;
        for (Map.Entry<String, ?> param : params.entrySet()) {
            if (!firstTag) {
                content.append("&");
            } else {
                firstTag = false;
            }
            content.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue().toString(), "UTF-8"));
        }
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
        out.writeBytes(content.toString());
        // 流用完记得关
        out.flush();
        out.close();
        // 获取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder(64);
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();
        // 该干的都干完了,记得把连接断了
        connection.disconnect();
        return result.toString();
    }
    
    public static void msgLog(String msg) {
        StringBuilder msgBuilder = new StringBuilder(128);
        msgBuilder.append(Thread.currentThread().getName()).append(" - ").append(System.currentTimeMillis()).append(" - ").append(msg);
        System.out.println(msgBuilder.toString());
    }

    public static String hashMd5(String str) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            return bytesToHex(md5.digest(str.getBytes(Charset.forName("UTF-8"))));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("该异常不应当抛出", e);
        }
    }
    
    public static String bytesToHex(@NonNull byte[] byteArray) {
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = HEX_DIGITS[b >>> 4 & 0xf];
            resultCharArray[index++] = HEX_DIGITS[b & 0xf];
        }
        return new String(resultCharArray);
    }
    
    private ClientTools() {
    }
}
