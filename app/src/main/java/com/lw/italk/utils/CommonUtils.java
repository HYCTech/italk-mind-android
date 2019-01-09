package com.lw.italk.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lw.italk.App;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by lfei on 15-4-14.
 */
public class CommonUtils {
    public static final String PASSWORD_FILE = "pws_file";
    public static final String KEYPAIR = "keypair";
    public static final long SIZE_500_M = 500 * 1024 * 1024;
    final static int MIN_PASS_LENG = 6;
    final static int MAX_PASS_LENG = 12;
    private static final String TAG = "CommonUtils";
    private static final Method sApplyMethod = findApplyMethod();
    private static long lastClickTime;

    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //参数排序
    public static String sortParms(Map<String, String> params) {
        Map<String, String> treeMap = new TreeMap<String, String>();
        treeMap.putAll(params);
        Set<String> keySet = treeMap.keySet();
        StringBuffer sb = new StringBuffer();
        for (String key : keySet) {
            try {
                sb.append(key).append("=").append(URLEncoder.encode(treeMap.get(key), "UTF-8"));
            } catch (Exception e) {

            }
        }
        return sb.toString();
    }

    public static boolean isEmail(String email) {

        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

        Pattern p = Pattern.compile(str);

        Matcher m = p.matcher(email);

        return m.matches();
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {

        boolean isValid = false;

        String expression = "((^(13|14|15|17|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";

        CharSequence inputStr = phoneNumber;

        Pattern pattern = Pattern.compile(expression);

        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            isValid = true;
        }

        return isValid;
    }

    public static boolean checkConsigneeName(String str) {
        String namePat = "^[a-zA-Z\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(namePat);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean checkAddress(String str) {
        String namePat = "^[0-9a-zA-Z-#\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(namePat);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean checkUserName(String str) {
        String namePat = "[a-zA-Z0-9_]{4,}";
        Pattern pattern = Pattern.compile(namePat);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static void writeToFile(File file, String content) {

        RandomAccessFile raf = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(content.getBytes("UTF-8"));
            raf.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public static String getMerchandiseName(String name) {
        String regex = "\\([^\\(]+\\)|\\（[^\\（]+\\）";
        return name.replaceAll(regex, "");
    }

    public static String getMerchandiseFeature(String name) {
        String regex = "\\([^\\(]+\\)|\\（[^\\（]+\\）";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(name);
        if (m.find()) {
            return m.group(0).replace("(", "").replace(")", "").replace("（", "").replace("）", "");
        }
        return name;
    }

    public static String getDigestSign(String content) {
        if (content == null) {
            throw new UnsupportedOperationException();
        }
        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-1");
            md.update(content.getBytes());
            return bytes2Hex(md.digest());
        } catch (Exception e) {

        }
        return null;
    }

    public static RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }

    public static String getProductName(String name) {
        String regex = "\\([^\\(]+\\)|\\（[^\\（]+\\）";
        return name.replaceAll(regex, "");
    }

    public static String getProductFeature(String name) {
        String regex = "\\([^\\(]+\\)|\\（[^\\（]+\\）";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(name);
        if (m.find()) {
            return m.group(0).replace("(", "").replace(")", "").replace("（", "").replace("）", "");
        }
        return name;
    }

    public static String makeDisplayDate(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time * 1000);
        String res = df.format(date);
        return res;
    }

    /**
     * susan.gu 2014-12-03根据手机的分辨率从dp的单位转化px
     */
    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }

    /***
     * susan.gu 2014-12-03根据手机的分辨率从px转化为dp
     */
    public static int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);

    }

    /*
    * 判断wifi是否连接*/
    public static boolean isWifiConnected(Context context) throws Exception {
        ConnectivityManager connecManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConnected = false;
        if (null == connecManager) {
        } else {
            NetworkInfo info = connecManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                isWifiConnected = info.isConnected();
            }
        }
        return isWifiConnected;
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getPhoneVersion() {
        return Build.DISPLAY;
    }

    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getInstallIMEI() {
        TelephonyManager tm = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        return TextUtils.isEmpty(imei) ? "" : imei;
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static String timeStamp2Date(String timestampString, String formats) {
        long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }

    public static boolean getBooleanPref(Context context, String name, boolean def) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getBoolean(name, def);
    }

    public static void setBooleanPref(Context context, String name, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        Editor ed = prefs.edit();
        ed.putBoolean(name, value);
        ed.commit();
        apply(ed);
    }

    private static Method findApplyMethod() {
        try {
            Class cls = Editor.class;
            return cls.getMethod("apply");
        } catch (NoSuchMethodException unused) {
            // fall through
        }
        return null;
    }

    private static void apply(Editor editor) {
        if (sApplyMethod != null) {
            try {
                sApplyMethod.invoke(editor);
                return;
            } catch (InvocationTargetException unused) {
                // fall through
            } catch (IllegalAccessException unused) {
                // fall through
            }
        }
        editor.commit();
    }


    public static String getDefaultPath() {

        String path = null;

        if (checkSDCard()) {
            path = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + "nubiaShopDown" + File.separator;
        } else {
            path = App.getInstance().getCacheDir().getPath()
                    + File.separator + "Upgrade" + File.separator;
        }
        if (path == null || TextUtils.isEmpty(path)) {
        }

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
            }
        }

        return path;

    }

    private static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    @SuppressLint("NewApi")
    public static boolean isStorageEnough(long needSize) {
        long aviableSize = Environment.getExternalStorageDirectory()
                .getFreeSpace();
        long totalSize = Environment.getExternalStorageDirectory()
                .getTotalSpace();
        long limit = totalSize * 10 / 100;
        limit = Math.min(limit, SIZE_500_M);

        boolean flag = aviableSize > limit && aviableSize > needSize;
        return flag;
    }

    /**
     * 检查密码长度是否合理
     *
     * @param pass
     * @return
     */
    public static boolean checkPasswordLen(String pass) {
        //非空
        if (TextUtils.isEmpty(pass)) {
            return false;
        }

        //长度
        return !(pass.length() < MIN_PASS_LENG || pass.length() > MAX_PASS_LENG);
    }

    /**
     * 密码是否含有非法字符检查
     *
     * @param pass
     * @return
     */
    public static boolean checkPasswordCode(String pass) {

        //非空
        if (TextUtils.isEmpty(pass)) {
            return false;
        }

        //长度
        if (pass.length() < MIN_PASS_LENG || pass.length() > MAX_PASS_LENG) {
            return false;
        }

        //字符是否合法
        String regExp = "[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:\"<>?]*";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(pass);
        return m.matches();
    }

    public static void hideKb(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im.isActive()) {
            im.hideSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     *
     * @param toCheck  待检查字符串,数字合法性这里不做
     * @param max      数字最大值
     * @param dotvalue 小数点位数
     * @return
     */
    public static boolean checkPrice(String toCheck, int max, int dotvalue){

        if(toCheck.trim().length() <= 0){
            return false;
        }
        toCheck = toCheck.trim();
        if(toCheck.contains(".")){
            String[] strArr = toCheck.split("\\.");
            //判断小数点后面的位数，如果超过两位就返回false
            if(strArr.length>1 && strArr[1].length()>dotvalue){
                return false;
            }
        }

        //
        if(Double.valueOf(toCheck) > Double.valueOf(max)){
            return false;
        }

        return true;
    }

    public static String savePicture(Bitmap bm, String fileName) {//将布局保存为图片

        if (bm == null) {
            Toast.makeText(App.getInstance(), "savePicture null !", Toast.LENGTH_SHORT).show();
            return "";
        }
        File foder = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(foder, fileName);
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myCaptureFile.getAbsolutePath().toString();
    }

    public static boolean isNetworkConnected() {
//        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) App.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
//        }
        return false;
    }

    public static Map<String, String> ObjectToMapUtil(Object obj) throws NoSuchFieldException, IllegalAccessException {
        Map<String, String> reMap = new HashMap<String, String>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field subField = obj.getClass().getDeclaredField(fields[i].getName());
            subField.setAccessible(true);
            Object o = subField.get(obj);
            if(o != null){
                reMap.put(fields[i].getName(), o.toString());
            }
        }
        return reMap;
    }

    /**
     * 获取当前应用的版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verCode;
    }

    /** 获取当前应用的版本名称 */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }
}
