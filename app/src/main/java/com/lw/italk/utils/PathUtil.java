package com.lw.italk.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.lw.italk.App;
import com.lw.italk.framework.common.LWFriendManager;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.greendao.model.UserInfo;

import java.io.File;
import java.util.UUID;

/**
 * Created by Administrator on 2018/8/19 0019.
 */

public class PathUtil {
    public static String pathPrefix;
    public static final String historyPathName = "/chat/";
    public static final String imagePathName = "/image/";
    public static final String voicePathName = "/voice/";
    public static final String filePathName = "/file/";
    public static final String videoPathName = "/video/";
    public static final String netdiskDownloadPathName = "/netdisk/";
    public static final String meetingPathName = "/meeting/";
    private static File storageDir = null;
    private static PathUtil instance = null;
    private File voicePath = null;
    private File imagePath = null;
    private File historyPath = null;
    private File videoPath = null;
    private File filePath;

    private PathUtil() {
        UserInfo userInfo=LWUserManager.getInstance().getUserInfo();
        if(userInfo!=null){
            initDirs("", userInfo.getUid(), App.getInstance());
        }else{
            UUID uuid = UUID.randomUUID();
            initDirs("", uuid.toString(), App.getInstance());
        }
    }

    public static PathUtil getInstance() {
        if(instance == null) {
            instance = new PathUtil();
        }

        return instance;
    }

    public void initDirs(String var1, String var2, Context var3) {
        String var4 = var3.getPackageName();
        pathPrefix = App.getInstance().getExternalFilesDir(null).getPath();
        this.voicePath = generateVoicePath(var1, var2, var3);
        if(!this.voicePath.exists()) {
            this.voicePath.mkdirs();
        }

        this.imagePath = generateImagePath(var1, var2, var3);
        if(!this.imagePath.exists()) {
            this.imagePath.mkdirs();
        }

        this.historyPath = generateHistoryPath(var1, var2, var3);
        if(!this.historyPath.exists()) {
            this.historyPath.mkdirs();
        }

        this.videoPath = generateVideoPath(var1, var2, var3);
        if(!this.videoPath.exists()) {
            this.videoPath.mkdirs();
        }

        this.filePath = generateFiePath(var1, var2, var3);
        if(!this.filePath.exists()) {
            this.filePath.mkdirs();
        }

    }

    public File getImagePath() {
        return this.imagePath;
    }

    public File getVoicePath() {
        return this.voicePath;
    }

//    public File getFilePath() {
//        return this.filePath;
//    }
    public String getFilePath() {
        return this.filePath.getPath();
    }

    public File getVideoPath() {
        return this.videoPath;
    }

    public File getHistoryPath() {
        return this.historyPath;
    }

    private static File getStorageDir(Context var0) {
        if(storageDir == null) {
            File var1 = Environment.getExternalStorageDirectory();
            if(var1.exists()) {
                return var1;
            }

            storageDir = var0.getFilesDir();
        }

        return storageDir;
    }

    private static File generateImagePath(String var0, String var1, Context var2) {
        String var3 = null;
        if(var0 == null) {
            var3 = pathPrefix + var1 + "/image/";
        } else {
            var3 = pathPrefix + var0 + "/" + var1 + "/image/";
        }

        return new File(getStorageDir(var2), var3);
    }

    private static File generateVoicePath(String var0, String var1, Context var2) {
        String var3 = null;
        if(var0 == null) {
            var3 = pathPrefix + var1 + "/voice/";
        } else {
            var3 = pathPrefix + var0 + "/" + var1 + "/voice/";
        }

        return new File(getStorageDir(var2), var3);
    }

    private static File generateFiePath(String var0, String var1, Context var2) {
        String var3 = null;
        if(var0 == null) {
            var3 = pathPrefix + var1 + "/file/";
        } else {
            var3 = pathPrefix + var0 + "/" + var1 + "/file/";
        }

        return new File(getStorageDir(var2), var3);
    }

    private static File generateVideoPath(String var0, String var1, Context var2) {
        String var3 = null;
        if(var0 == null) {
            var3 = pathPrefix  + "/external_files";
        } else {
            var3 = pathPrefix  + "/external_files";
        }

        return new File(getStorageDir(var2), var3);
    }

    private static File generateHistoryPath(String var0, String var1, Context var2) {
        String var3 = null;
        if(var0 == null) {
            var3 = pathPrefix + var1 + "/chat/";
        } else {
            var3 = pathPrefix + var0 + "/" + var1 + "/chat/";
        }

        return new File(getStorageDir(var2), var3);
    }

    private static File generateMessagePath(String var0, String var1, Context var2) {
        File var3 = new File(generateHistoryPath(var0, var1, var2), var1 + File.separator + "Msg.db");
        return var3;
    }

    public static File getTempPath(File var0) {
        File var1 = new File(var0.getAbsoluteFile() + ".tmp");
        return var1;
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
