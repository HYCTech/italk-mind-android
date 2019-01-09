package com.lw.italk.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lw.italk.R;
import com.lw.italk.activity.AccountLoginActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.ErrorCode;
import com.lw.italk.http.RequestCode;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.http.ThirdLibs;
import com.lw.italk.utils.CommonUtils;
import com.lw.italk.utils.ItalkLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import okhttp3.ResponseBody;

/**
 * Created by cts on 2017/2/4.
 */
public class ProgressSubscriber<E> implements Subscriber<ResponseBody> {

    private static final String TAG = ProgressSubscriber.class.getSimpleName();

    /*是否弹框*/
    private boolean showProgress = false;

    private Dialog mDialog;

    private int mRequestCode;

    private Response<E> mResponse;

    private Context mContext;

    /**
     * @param context
     * @param showProgress 是否显示对话框
     */
    public ProgressSubscriber(Context context, boolean showProgress, int requestCode, Response<E> response) {
        this.showProgress = showProgress;
        this.mResponse = response;
        this.mRequestCode = requestCode;
        mContext = context;
        if (showProgress) {
            initDialog(context);
        }
    }

    /**
     * @param context
     * @param showProgress 是否显示对话框
     */
    public ProgressSubscriber(Context context, boolean showProgress, Response response) {
        this.showProgress = showProgress;
        this.mResponse = response;
        this.mRequestCode = RequestCode.EMPTY;
        this.mContext = context;
        if (showProgress) {
            initDialog(mContext);
        }
    }


    private void initDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.ll_loading_dialog);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.iv_loading);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_dialog);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        mDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        mDialog.setCancelable(false);// 不可以用“返回键”取消
        mDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
    }

    @Override
    public void onSubscribe(Subscription s) {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(ResponseBody responseBody) {

        if (mContext != null && mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        try {
            if (mResponse == null) {
                return;
            }
            String body = responseBody.string().toString();
            ItalkLog.o(body);
            String body1 = body.replaceAll("18446744073709551615","0");

            BaseResponse<E> response = ThirdLibs.getInstance().getGson().fromJson(body1, mResponse.getTypeToken(mRequestCode));

            if (response.getCode() == ErrorCode.SUCCESS) {
                mResponse.next(response.getData(), mRequestCode);
            } else if (response.getCode() == ErrorCode.TOKEN_INVALID) {
//                CommonUtils.jumpToLogin(mContext);
                LWDBManager.getInstance().getUserInfo().setIscurrent(false);//账号为退出状态
                LWDBManager.getInstance().updateUserInfo(LWDBManager.getInstance().getUserInfo());
                Intent intent = new Intent(mContext, AccountLoginActivity.class);
                //跳转后关闭activity之前的所有activity（原理是清理activity堆栈）
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                if (mContext instanceof Activity){
                    ((Activity)mContext).finish();
                }
            } else {
                mResponse.error(new ResponseErrorException(response.getData(), response.getMessage(), response.getCode()), mRequestCode);
            }

        } catch (Exception e) {
             e.printStackTrace();
            ItalkLog.o("IOException:" + e.getMessage());
        }
    }

    @Override
    public void onError(Throwable t) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        ItalkLog.o("IOException:" + t.getMessage());
        if (mResponse != null) {
            if (t instanceof ResponseErrorException) {
                mResponse.error((ResponseErrorException) t, mRequestCode);
            } else {
                mResponse.error(new ResponseErrorException(t.getMessage(), ErrorCode.UNDEFINE), mRequestCode);
            }
        }
    }

    @Override
    public void onComplete() {
    }

}
