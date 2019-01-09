package com.lw.italk;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;

import com.lw.italk.framework.common.LWDBManager;

import com.lw.italk.utils.CommonUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class App extends Application {

	private static Context _context;
	public static String VIDEO_PATH =  "/sdcard/WeiXinRecordedDemo/";

	@Override
	public void onCreate() {
		super.onCreate();
		CrashReport.initCrashReport(getApplicationContext(), "264fee0704", true);
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
//		builder.detectFileUriExposure();
		_context = getApplicationContext();
		LWDBManager.getInstance();
//		FileDownloader.setup(_context);
		// CrashHandler crashHandler = CrashHandler.getInstance();// 全局异常捕捉
		// crashHandler.init(_context);
		initCamara();
		GloableParams.versionName = CommonUtils.getVersionName(_context);

		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

			}

			@Override
			public void onActivityStarted(Activity activity) {

			}

			@Override
			public void onActivityResumed(Activity activity) {
				MyActivityManager.getInstance().setCurrentActivity(activity);
			}

			@Override
			public void onActivityPaused(Activity activity) {

			}

			@Override
			public void onActivityStopped(Activity activity) {

			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

			}

			@Override
			public void onActivityDestroyed(Activity activity) {

			}
		});


	}

	private void initCamara() {
//		VIDEO_PATH += String.valueOf(System.currentTimeMillis());
//		File file = new File(VIDEO_PATH);
//		if(!file.exists()) file.mkdirs();
//
//		//设置视频缓存路径
//		VCamera.setVideoCachePath(VIDEO_PATH);
//
//		// 开启log输出,ffmpeg输出到logcat
//		VCamera.setDebugMode(true);
//
//		// 初始化拍摄SDK，必须
//		VCamera.initialize(this);
	}


	private void initEMChat() {

		// IntentFilter callFilter = new
		// IntentFilter(EMChatManager.getInstance()
		// .getIncomingCallBroadcastAction());
		// registerReceiver(new CallReceiver(), callFilter);
	}

	private class CallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 拨打方username

		}
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		try {
			deleteCacheDirFile(getHJYCacheDir(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.gc();
	}

	public static Context getInstance() {
		return _context;
	}

	// 运用list来保存们每一个activity是关键
	private List<Activity> mList = new LinkedList<Activity>();
	private static App instance;

	// 构造方法
	// 实例化一次
	public synchronized static App getInstance2() {
		if (null == instance) {
			instance = new App();
		}
		return instance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	// 关闭每一个list内的activity
	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public static String getHJYCacheDir() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().toString()
					+ "/Health/Cache";
		else
			return "/System/com.juns.Walk/Walk/Cache";
	}

	public static String getHJYDownLoadDir() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().toString()
					+ "/Walk/Download";
		else {
			return "/System/com.Juns.Walk/Walk/Download";
		}
	}

	public static void deleteCacheDirFile(String filePath,
			boolean deleteThisPath) throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteCacheDirFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {// 如果是文件，删除
					file.delete();
				} else {// 目录
					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
						file.delete();
					}
				}
			}
		}
	}
}
