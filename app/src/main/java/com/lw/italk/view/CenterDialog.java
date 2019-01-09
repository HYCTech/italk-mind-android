package com.lw.italk.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lw.italk.R;

/**
 * Created by 喜明 on 2018/8/19.
 */

public class CenterDialog extends Dialog {
    private String title;
    private String message;
    private String header;
    private View.OnClickListener onNegateClickListener;
    private View.OnClickListener onPositiveClickListener;
    private String state;//0、警告;1、成功;2、失败;
    private int topShow;//0、隐藏1、显示

    public CenterDialog(Context context) {
        super(context);
    }

    /**
     * @param context 上下文
     * @param theme   给dialog设置的主题
     */
    public CenterDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.center_dialog);
        //设置dialog的大小
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth() - 200; //设置dialog的宽度为当前手机屏幕的宽度-100
        getWindow().setAttributes(p);


        RelativeLayout llTop = (RelativeLayout) findViewById(R.id.callback_dialog_ll_top);
        llTop.setVisibility(topShow);
        ImageView stateImg = (ImageView) findViewById(R.id.callback_dialog_img_state);
        if (!TextUtils.isEmpty(state)) {
            stateImg.setVisibility(View.VISIBLE);
            stateImg.setImageResource(state.equals("0") ? R.mipmap.head : state.equals("1") ? R.mipmap.head : R.mipmap.head);
        } else {
            stateImg.setVisibility(View.GONE);
        }
        TextView textTitle = (TextView) findViewById(R.id.callback_dialog_tv_title);
        if (!TextUtils.isEmpty(title)) {
            textTitle.setVisibility(View.VISIBLE);
            textTitle.setText(title);
        } else {
            textTitle.setVisibility(View.GONE);
        }
        TextView textMsg = (TextView) findViewById(R.id.callback_dialog_tv_msg);
        if (!TextUtils.isEmpty(message)) {
            textMsg.setVisibility(View.VISIBLE);
            textMsg.setText(message);
        }
        TextView divider = (TextView) findViewById(R.id.callback_dialog_tv_dividers);
        TextView negate = (TextView) findViewById(R.id.callback_dialog_tv_negate);
        if (onNegateClickListener != null) {
            negate.setVisibility(View.VISIBLE);
            negate.setOnClickListener(onNegateClickListener);
        } else {
            divider.setVisibility(View.GONE);
            negate.setVisibility(View.GONE);
        }
        TextView positive = (TextView) findViewById(R.id.callback_dialog_tv_positive);
        if (onPositiveClickListener != null) {
            positive.setVisibility(View.VISIBLE);
            positive.setOnClickListener(onPositiveClickListener);
        } else {
            divider.setVisibility(View.GONE);
            positive.setVisibility(View.GONE);
        }

    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMsg(String message) {
        this.message = message;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTopShow(int topShow) {
        this.topShow = topShow;
    }

    /**
     * 确定按钮
     */
    public void setOnPositiveListener(View.OnClickListener onPositiveClickListener) {
        this.onPositiveClickListener = onPositiveClickListener;
    }

    /**
     * 取消按钮
     */
    public void setOnNegateListener(View.OnClickListener onNegateClickListener) {
        this.onNegateClickListener = onNegateClickListener;
    }
}
