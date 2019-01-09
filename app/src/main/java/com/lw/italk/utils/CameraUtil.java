package com.lw.italk.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
* 调用相机和相册的工具类
*/
public class CameraUtil {
    /**
     * 检测SD卡是否可用
     *
     * @return true 可用，false不可用。
     */
    public static boolean isSDExit() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 新建文件夹到手机本地
     *
     * @param fileFolder ,文件夹的路径名称
     * @return
     */
    public static boolean createDir(String fileFolder) {
        File dir = new File(fileFolder);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }

    /**
     * 新建文件到手机本地
     *
     * @param fileNameWithPath ,文件名包含路径
     * @return , true新建成功, false新建失败
     */
    public static boolean createFile(String fileNameWithPath) {
        File file = new File(fileNameWithPath);
        try {
            if (isSDExit() && file.exists()) {
                if (file.delete()) {
                    return file.createNewFile();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 新建文件到手机指定路径
     *
     * @param dirPath  ,文件的文件夹目录路径
     * @param fileName ,文件名
     * @return , true新建成功, false新建失败
     */
    public static boolean createFile(String dirPath, String fileName) {
        File file = new File(dirPath, fileName);
        try {
            if (isSDExit() && file.exists()) {
                if (file.delete()) {
                    return file.createNewFile();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 创建相机拍照图片名称
     *
     * @param fileType ,文件的类型，即扩展名，例如.jpg 、.mp4 、.mp3等
     * @return , 图片文件名,格式形式20161011_111523.jpg
     */
    public static String createFileName(String fileType) {
        String fileName = "";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        fileName = sdf.format(date) + fileType;
        return fileName;
    }

    /**
     * 保存图片的Bitmap数据到sd卡指定路径
     *
     * @param fileNameWithPath ,图片的路径
     * @param bitmap           ,图片的bitmap数据
     */
    public static void savePhotoToPath(String fileNameWithPath, Bitmap bitmap) {
        if (isSDExit()) {
            File file = new File(fileNameWithPath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                if (bitmap != null) {
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                        fos.flush();
                        fos.close();
                    }
                }
            } catch (FileNotFoundException e) {
                file.delete();
                e.printStackTrace();
            } catch (IOException e) {
                file.delete();
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param dirPath  ,文件的文件夹目录路径
     * @param fileName ,文件名
     * @return , true删除成功, false删除失败
     */
    public static boolean deleteFile(String dirPath, String fileName) {
        File file = new File(dirPath, fileName);
        if (!file.exists()) {
            return true;
        }
        return file.delete();
    }

    /**
     * 更新系统的Media库
     *
     * @param context
     */
    public static void updateSystemMedia(Context context) {
        MediaScannerConnection.scanFile(context, new String[]{
                android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
        }, null, null);
    }

    /**
     * 打开手机系统相册, method one
     *
     * @return intent, Activity调用的intent
     */
    public static Intent openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        return intent;
    }

    /**
     * 打开手机系统相册, method two
     *
     * @return intent, Activity调用的intent
     */
    public static Intent openGallery() {
        // 相册中选择
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        return intent;
    }

    /**
     * 打开手机系统相册, method two
     *
     * @return intent, Activity调用的intent
     */
    public static Intent openVideo() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }

    /**
     * 打开手机系统相机拍照
     *
     * @param   , 用于保存手机拍照后所获图片的uri
     * @return intent, Activity调用的intent
     */
    public static Intent openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(),
                FileUtils.TEMP_SHOP_PHOTO)));
        return intent;
    }

    /**
     * 打开手机系统的图片裁剪Activity
     *
     * @param    , 待裁剪图片的uri
     * @param   , 裁剪后图片保存的uri
     * @return intent , Activity调用的intent
     */
    public static Intent cropPicture(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        return intent;
    }

    public static Intent cropLongPicture(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 360);
        intent.putExtra("outputY", 400);
        intent.putExtra("return-data", true);
        return intent;
    }

}