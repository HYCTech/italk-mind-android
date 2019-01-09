package com.lw.italk.view;

/**
 * Created by lxm on 2018/8/13.
 * 底部弹框
 */

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lw.italk.R;

public class BottomDialog extends Dialog {

    private View mView;
    private LinearLayout mShareDialog;
    private TextView mEmptyChat;
    private TextView mCancel;
    private AnimatorSet animSet;
    private Context mContext;
    private float height;

    public BottomDialog(Context context) {
        super(context);
        init(context);
    }

    public BottomDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public BottomDialog(Context context, boolean cancelable,
                        OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mView = View.inflate(context, R.layout.dialog_bottom, null);
        setContentView(mView);

        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        mShareDialog = (LinearLayout) mView.findViewById(R.id.share_dialog);
        mShareDialog.measure(0, 0);
        height = mShareDialog.getMeasuredHeight();

        mEmptyChat = (TextView) mView.findViewById(R.id.empty_chat);
        mEmptyChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                
            }
        });

        mCancel = (TextView) mView.findViewById(R.id.cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                closeDialogAnimation();
            }
        });

        animSet = new AnimatorSet();

        ObjectAnimator translation = ObjectAnimator.ofFloat(mView,
                "translationY", getScreenSize(context)[1],
               getScreenSize(context)[1] - height - 80);

        translation.setDuration(500);
        translation.start();
    }

    private void closeDialogAnimation() {
        ObjectAnimator translation = ObjectAnimator.ofFloat(mView,
                "translationY", getScreenSize(mContext)[1]
                        - height - 80, getScreenSize(mContext)[1]);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mView, "alpha", 1f, 0f);
        animSet.play(translation).with(alpha);
        animSet.setInterpolator(new AccelerateInterpolator(1f));
        animSet.setDuration(500);
        animSet.start();
        animSet.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator arg0) {
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
            }
        });
    }

    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[] { outMetrics.widthPixels, outMetrics.heightPixels };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        closeDialogAnimation();
        return true;
    }

}
