package com.lw.italk.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWDownloadManager;
import com.lw.italk.framework.common.OssManager;
import com.lw.italk.gson.msg.MsgItem;
import com.lw.italk.task.LoadLocalBigImgTask;
import com.lw.italk.utils.GlideCircleTransform;
import com.lw.italk.utils.ImageCache;
import com.lw.italk.utils.ImageUtils;
import com.lw.italk.utils.ItalkLog;
import com.lw.italk.utils.PathUtil;
import com.lw.italk.view.TouchImageView.TouchImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;

import butterknife.BindView;

/**
 * 下载显示大图
 */
public class ShowBigImage extends BaseActivity {
    private static final int start_DownImage_onProgressCallback = 1;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.pb_load_local)
    ProgressBar loadLocalPb;
    private ProgressDialog pd;
    private int default_res = R.drawable.default_image;
    private String localFilePath;
    private Bitmap bitmap;
    private boolean isDownloaded;

    private ProgressDialog progressDialog;
    private boolean isFirst = true;
    private UIHandler mHandler;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new UIHandler(this);
        progressDialog = new ProgressDialog(this,  ProgressDialog.THEME_HOLO_LIGHT);
        default_res = getIntent().getIntExtra("default_image", R.drawable.head);
        Uri uri = getIntent().getParcelableExtra("uri");
        String remotepath = getIntent().getExtras().getString("remotepath");
        String secret = getIntent().getExtras().getString("secret");
        String msgID = getIntent().getExtras().getString("msgID");
        String fid = getIntent().getExtras().getString("fid");

        // 本地存在，直接显示本地的图片
        if (uri != null && new File(uri.getPath()).exists()) {
            image.setImageURI(uri);
//            System.err.println("showbigimage file exists. directly show it");
//            DisplayMetrics metrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            // int screenWidth = metrics.widthPixels;
//            // int screenHeight =metrics.heightPixels;
//            bitmap = ImageCache.getInstance().get(uri.getPath());
//            if (bitmap == null) {
//                LoadLocalBigImgTask task = new LoadLocalBigImgTask(this,
//                        uri.getPath(), image, loadLocalPb,
//                        ImageUtils.SCALE_IMAGE_WIDTH,
//                        ImageUtils.SCALE_IMAGE_HEIGHT);
//                if (Build.VERSION.SDK_INT > 10) {
//                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                } else {
//                    task.execute();
//                }
//            } else {
//                image.setImageBitmap(bitmap);
//            }
        } else if (remotepath != null) { // 去服务器下载图片
//			System.err.println("download remote image");
//			Map<String, String> maps = new HashMap<String, String>();
//			if (!TextUtils.isEmpty(secret)) {
//				maps.put("share-secret", secret);
//			}
//			downloadImage(remotepath, maps);
            Log.e("123qwe", "remotepath:" + remotepath);
//            Glide.with(this).load(remotepath)
//                    .placeholder(R.drawable.default_image) //设置占位图，在加载之前显示
//                    .into(image);
            startDownImage(image, msgID,fid, remotepath);
        } else {
            image.setImageResource(default_res);
        }

        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_show_big_image;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    private void startDownImage(final ImageView iv, final String msgID,String fid, final String url) {
        iv.setImageResource(R.drawable.default_image);
        showProgress(0);
        LWDownloadManager.getInstance().startDownload(msgID, fid, url, new OssManager.ProgressCallback() {
            @Override
            public void onProgressCallback(double progress) {
                Log.e("lxm1", "onProgressCallback");
                Message msg = mHandler.obtainMessage();
                msg.what = start_DownImage_onProgressCallback;
                msg.obj =  progress;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onSuccessCallback(String obj, final String file, final String result) {
                Log.e("123qwe", "Sig image how bonSuccessCallback result:" + result);
                ((Activity) ShowBigImage.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        isFirst = false;
                        ItalkLog.e("onSuccessCallback------");
                        if (TextUtils.isEmpty(result)) {
                            return;
                        }
                        File file = new File(result);
                        if (file.exists()) {
                            Uri uri = Uri.parse(result);
                            iv.setImageURI(uri);
//						System.err
//								.println("here need to check why download everytime");
                        }
                    }
                });
            }

            @Override
            public void onErrorCallback(String filename, String path, String error) {

            }
        }, false);
    }

    /**
     * 通过远程URL，确定下本地下载后的localurl
     *
     * @param remoteUrl
     * @return
     */
    public String getLocalFilePath(String remoteUrl) {
        String localPath;
        if (remoteUrl.contains("/")) {
            localPath = PathUtil.getInstance().getImagePath().getAbsolutePath()
                    + "/" + remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
        } else {
            localPath = PathUtil.getInstance().getImagePath().getAbsolutePath()
                    + "/" + remoteUrl;
        }
        return localPath;
    }

    /**
     * 下载图片
     *
     * @param remoteFilePath
     */
    private void downloadImage(final String remoteFilePath,
                               final Map<String, String> headers) {
//		String str1 = getResources().getString(R.string.Download_the_pictures);
//		pd = new ProgressDialog(this);
//		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		pd.setCanceledOnTouchOutside(false);
//		pd.setMessage(str1);
//		pd.show();
//		localFilePath = getLocalFilePath(remoteFilePath);
//		final HttpFileManager httpFileMgr = new HttpFileManager(this,
//				EMChatConfig.getInstance().getStorageUrl());
//		final CloudOperationCallback callback = new CloudOperationCallback() {
//			public void onSuccess(String resultMsg) {
//
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						DisplayMetrics metrics = new DisplayMetrics();
//						getWindowManager().getDefaultDisplay().getMetrics(
//								metrics);
//						int screenWidth = metrics.widthPixels;
//						int screenHeight = metrics.heightPixels;
//
//						bitmap = ImageUtils.decodeScaleImage(localFilePath,
//								screenWidth, screenHeight);
//						if (bitmap == null) {
//							image.setImageResource(default_res);
//						} else {
//							image.setImageBitmap(bitmap);
//							ImageCache.getInstance().put(localFilePath, bitmap);
//							isDownloaded = true;
//						}
//						if (pd != null) {
//							pd.dismiss();
//						}
//					}
//				});
//			}
//
//			public void onError(String msg) {
//				Log.e("###", "offline file transfer error:" + msg);
//				File file = new File(localFilePath);
//				if (file.exists() && file.isFile()) {
//					file.delete();
//				}
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						pd.dismiss();
//						image.setImageResource(default_res);
//					}
//				});
//			}
//
//			public void onProgress(final int progress) {
//				Log.d("ease", "Progress: " + progress);
//				final String str2 = getResources().getString(
//						R.string.Download_the_pictures_new);
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//
//						pd.setMessage(str2 + progress + "%");
//					}
//				});
//			}
//		};
//
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				httpFileMgr.downloadFile(remoteFilePath, localFilePath,
//						headers, callback);
//			}
//		}).start();
    }

    /*加载中的进度条*/
    private void showProgress(double msg) {
        progressDialog.setMessage("正在加载中："+((int)(msg))+"%");
        progressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(mHandler!= null){
                        mHandler.removeCallbacksAndMessages(null);
                    }
                    progressDialog.dismiss();
                    ShowBigImage.this.finish();
                    Log.e("lxm1","mHandler.removeCallbacksAndMessages");
                }
                return false;
            }
        });

        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.e("lxm1", "KeyEvent.KEYCODE_BACK");
                if (isDownloaded) {
                    setResult(RESULT_OK);
                }
                finish();
                return false;//拦截事件
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);

    }

    private static class UIHandler extends Handler {
        private WeakReference<ShowBigImage> ref;

        public UIHandler(ShowBigImage ref) {
            this.ref = new WeakReference<ShowBigImage>(ref);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (ref.get() == null) {
                return;
            }

            ShowBigImage activity = ref.get();

            switch (msg.what) {
                case start_DownImage_onProgressCallback:
                    Log.e("lxm1", "start_DownImage_onProgressCallback");
                    if (msg.obj == null) {
                        return;
                    }
                    if (activity.isFirst) {
                        Log.e("lxm1", "isFirst---------" + activity.isFirst + "");
                        try {
                            activity.showProgress((double)msg.obj);
                        }catch (Exception e){
                            if (activity.progressDialog != null && activity.progressDialog.isShowing()) {
                                activity.progressDialog.dismiss();
                            }
                        }

                        Log.e("lxm1", "progress----------" + (double)msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if(mHandler!= null){
            mHandler.removeCallbacksAndMessages(null);
        }
        progressDialog = null;
    }
}
