package com.lw.italk.framework.common;

import com.lw.italk.gson.msg.MsgItem;

import java.util.List;

/**
 * Created by Administrator on 2018/9/2 0002.
 */

public interface MsgUpdateListen {
        public void updateMsgs(List<MsgItem> msgItem);
}

