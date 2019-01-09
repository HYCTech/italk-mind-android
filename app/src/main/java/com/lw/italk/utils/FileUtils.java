package com.lw.italk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.lw.italk.http.FileDownloadCallback;
import com.lw.italk.http.RetrofitCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by pyzhou on 15-6-1.
 */
public class FileUtils {

    public static String SAVE_DOWN_APK_ID = "down_apk";
    public static String SAVE_NEED_DOWN_APK = "is_need_apk";
    public static final String TEMP_SHOP_PHOTO = "shopphoto.jpg";

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     * 
     * @param filePath
     *            文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = file.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件夹
     * 
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {

                if (!flist[i].isHidden()) {
                    size = size + flist[i].length();
                }
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     * 
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static void deleteDir(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                file.delete();
            } else {
                deleteDir(file.getAbsolutePath());
            }
        }
    }

    //
    public static File bitmapToFile(Bitmap bitmap) {
        File avaterFile = new File(Environment.getExternalStorageDirectory(), TEMP_SHOP_PHOTO);//设置文件名称

        if(avaterFile.exists()){
            avaterFile.delete();
        }
        try {
            avaterFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(avaterFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return avaterFile;
    }

    /**
     * 图片的缩放方法
     * 
     * bitmap 源图片资源 
     * maxSize 图片允许最大空间 单位：KB 
     */
    public static Bitmap getZoomImage(Bitmap bitmap, double maxSize) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }
        
        //单位：从Byte 转为 KB
        double currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        //判断bitmap占用的空间是否大于大于最大允许空间，如果大于则压缩，小于不压缩
        while (currentSize > maxSize) {
            //计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / maxSize;
            //开始压缩：将宽度和高度压缩到对应的平方根倍
            //1.保持新的宽度和高度，与bitmap原来的宽高率一致
            //2.压缩后达到了最大大小对应的新bitmap。显示效果最好
            bitmap = getZoomImage(bitmap,
                    bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight()
                            / Math.sqrt(multiple));
            currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        }

        return bitmap;
    }

    /**
     * 图片的缩放方法
     * 
     * orgBitmap 源图片资源 
     * newWidth 缩放后的宽度 
     * newHeight 缩放后的高度
     */
    public static Bitmap getZoomImage(Bitmap orgBitmap, double newWidth,
            double newHeight) {
        if (orgBitmap == null) {
            return null;
        }
        if (orgBitmap.isRecycled()) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }
        // 获取图片的宽和高
        float width = orgBitmap.getWidth();
        float height = orgBitmap.getHeight();

        Matrix matrix = new Matrix();
        // 计算宽和高的缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * bitmap 转换byte 数组
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 判断相对应的APP是否存在
     * 
     * @param context
     * @param packageName
     *            (包名)(若想判断QQ，则改为com.tencent.mobileqq，若想判断微信，则改为com.tencent.mm)
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        // 获取手机系统的所有APP包名，然后进行一一比较
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    public static void openFile(File var0, Activity var1) {
        Intent var2 = new Intent();
        var2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        var2.setAction("android.intent.action.VIEW");
        String var3 = getMIMEType(var0);
        var2.setDataAndType(Uri.fromFile(var0), var3);

        try {
            var1.startActivity(var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(var1, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }

    }

    public static String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    public static boolean isSubString(String a,String b){
        if (a.length()<b.length()) {
            return false;
        }else{
            int ptr=0;
            int count=0;//这里是统计相同字符，
            for(int i=0;i<b.length();i++){
                for(int j=ptr;j<a.length();j++){
                    if (a.charAt(j)==b.charAt(i)) {
                        ptr=j+1;
                        count++;
                    }
                }
            }
            return count == b.length()?true:false;
        }
    }


    public static File createFile(String filename,boolean isThumb){

        String path = PathUtil.getInstance().getFilePath();
        String downPath = "";
        if (isThumb) {
            downPath = path + "/local/" + System.currentTimeMillis() + filename;
        } else {
            downPath = path + "/" + System.currentTimeMillis() + "_" + filename;
        }
        File file = new File(downPath);
        Log.e("123qwe", "path:" + path + ",filename:" + filename);
        try {
//            if (!file.exists()){
//                file.mkdirs();
//                file.createNewFile();
//            }
            if (isThumb) {
                File dirFile = new File(path);
                if (!dirFile.exists()){
                    dirFile.mkdirs();
                }
            }else {
                File dirFile1 = new File(path + "/local/");
                if (!dirFile1.exists()){
                    dirFile1.mkdirs();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return file;
    }

    public static void writeFile2Disk(Response<ResponseBody> response, File file, FileDownloadCallback<ResponseBody> httpCallBack){

        long currentLength = 0;
        OutputStream os =null;
        if (response.body() == null){
            if (httpCallBack != null){
                httpCallBack.onFailure(null,new Throwable("no body"));
            }
            return;
        }
        InputStream is = response.body().byteStream();
        long totalLength =response.body().contentLength();

        try {

            os = new FileOutputStream(file);

            int len ;

            byte [] buff = new byte[1024];

            while((len=is.read(buff))!=-1){

                os.write(buff,0,len);
                currentLength+=len;
                Log.d("vivi","当前进度:"+currentLength);
                httpCallBack.onLoading(currentLength,totalLength);
            }
            httpCallBack.onSuccess(null,response,file.getAbsolutePath());
            // httpCallBack.onLoading(currentLength,totalLength,true);

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(os!=null){
                try {
                    os.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
