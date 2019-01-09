package com.lw.italk.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.lw.italk.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 喜明 on 2018/9/4.
 */

public class EditCenterDialog {

    private Activity context;
    private String dialogTitle;
    private String dialogTip;

    public EditCenterDialog(String title, String tip) {
        dialogTitle = title;
        dialogTip = tip;
    }

    public void showIpConfigDialog(final Activity ontext) {
        context= ontext;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Light_FullScreenDialogAct);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        //解决无法弹出软键盘的bug
        alertDialog.setView(LayoutInflater.from(context).inflate(R.layout.dialog_center, null));

        alertDialog.show();

        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_center, null);
        //输入ip地址的EditText框
        final EditText etIpAddress = (EditText) inflate.findViewById(R.id.et_ip_address);
        //自动获取焦点并弹出软键盘
        showSoftInput(etIpAddress);

        TextView mTitle = (TextView)inflate.findViewById(R.id.dialog_title);
        mTitle.setText(dialogTitle);
        TextView mTip = (TextView)inflate.findViewById(R.id.dialog_tip);
        mTip.setText(dialogTip);
        //确定按钮
        inflate.findViewById(R.id.tv_confirm_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = etIpAddress.getText().toString().trim();
                onButtonClickListener.onConfirmBtnClick(alertDialog, ip);
            }
        });
        //取消按钮
        inflate.findViewById(R.id.tv_cancel_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickListener.onCancelBtnClick(alertDialog);
            }
        });


        //为AlertDialog设置View
        alertDialog.getWindow().setContentView(inflate);

        //监听Dialog Dismiss时隐藏软键盘
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //隐藏软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    /**
     * 自动获取焦点并弹出软键盘
     *
     * @param etIpAddress
     */
    private void showSoftInput(final EditText etIpAddress) {
        //自动获取焦点
        etIpAddress.setFocusable(true);
        etIpAddress.setFocusableInTouchMode(true);
        etIpAddress.requestFocus();
        //200毫秒后弹出软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputManager = (InputMethodManager) etIpAddress.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(etIpAddress, 0);
                    }
                });
            }
        }, 200);
    }


    /**
     * 对外暴露确定取消按钮点击接口
     */
    private OnButtonClickListener onButtonClickListener;

    public interface OnButtonClickListener {
        void onConfirmBtnClick(AlertDialog dialog, String ip);

        void onCancelBtnClick(AlertDialog dialog);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}

