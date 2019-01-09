package com.lw.italk.task;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.lw.italk.utils.ImageCache;
import com.lw.italk.utils.ImageUtils;
import com.lw.italk.entity.LWMessage;

import java.io.File;

public class LoadImageTask extends AsyncTask<Object, Void, Bitmap> {
	private ImageView iv = null;
	String localFullSizePath = null;
	String thumbnailPath = null;
	String remotePath = null;
	LWMessage message = null;
	LWMessage.ChatType chatType;
	Activity activity;

	@Override
	protected Bitmap doInBackground(Object... args) {
		thumbnailPath = (String) args[0];
		localFullSizePath = (String) args[1];
		remotePath = (String) args[2];
		chatType = (LWMessage.ChatType) args[3];
		iv = (ImageView) args[4];
		// if(args[2] != null) {
		activity = (Activity) args[5];
		// }
		message = (LWMessage) args[6];
		File file = new File(thumbnailPath);
		if (file.exists()) {
			return ImageUtils.decodeScaleImage(thumbnailPath, 160, 160);
		} else {
			if (message.direct == LWMessage.Direct.SEND) {
				return ImageUtils.decodeScaleImage(localFullSizePath, 160, 160);
			} else {
				return null;
			}
		}

	}

	protected void onPostExecute(Bitmap image) {
		if (image != null) {
			iv.setImageBitmap(image);
			ImageCache.getInstance().put(thumbnailPath, image);
			iv.setClickable(true);
			iv.setTag(thumbnailPath);
			iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (thumbnailPath != null) {
						// TODO 查看大图

						// Intent intent = new Intent(activity,
						// BigPhotoActivity.class);
						// File file = new File(localFullSizePath);
						// if (file.exists()) {
						// Uri uri = Uri.fromFile(file);
						// intent.putExtra("uri", uri);
						// } else {
						// intent.putExtra("remotepath", remotePath);
						// }
						// activity.startActivity(intent);
					}
				}
			});
		} else {
			if (message.status == LWMessage.Status.FAIL) {
//				if (CommonUtils.isNetWorkConnected(activity)) {
//					new Thread(new Runnable() {
//
//						@Override
//						public void run() {
//							EMChatManager.getInstance().asyncFetchMessage(
//									message);
//						}
//					}).start();
//				}
			}

		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
}
