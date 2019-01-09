package com.lw.italk.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWDBManager;
import com.lw.italk.greendao.model.UserInfo;
import com.lw.italk.utils.GlideCircleTransform;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 喜明 on 2018/8/12.
 */

public class  QRCodeActivity extends BaseActivity {

    @BindView(R.id.left_bar_item)
    TextView mLeftBarItem;
    @BindView(R.id.center_bar_item)
    TextView mCenterBarItem;
    @BindView(R.id.title_bar_left)
    LinearLayout titleBarLeft;
    @BindView(R.id.right_title_bar)
    TextView mRightTitleBar;
    @BindView(R.id.title_bar_right)
    RelativeLayout titleBarRight;
    @BindView(R.id.header_img)
    ImageView mHeaderImg;
    @BindView(R.id.tv_nick_name)
    TextView mNickName;
    @BindView(R.id.er_wei_ma)
    ImageView mErWeiMa;
    @BindView(R.id.user_info)
    RelativeLayout mUserInfo;
    private Handler mHandler = new Handler();

    @Override
    protected int setContentView() {
        return R.layout.activity_qr_code;
    }

    @Override
    protected void initView() {
        mLeftBarItem.setText("我");
        mCenterBarItem.setText("二维码");
        mRightTitleBar.setText("保存图片");
    }

    @Override
    protected void initData() {
        UserInfo userInfo=LWDBManager.getInstance().getUserInfo();
        Bitmap qrcode = generateQRCode(userInfo.getUid());
        String avater=userInfo.getAvatar();

        mErWeiMa.setImageBitmap(qrcode);
        mNickName.setText(userInfo.getUsername());
        Glide.with(this).load(avater)
                .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                .error(R.drawable.default_img)
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .transform(new GlideCircleTransform(App.getInstance()))
                .into(mHeaderImg);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initData();
    }

    //生成二维码
    private Bitmap generateQRCode(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE,
                    500, 500);
            return bitMatrix2Bitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }

    public void savePicture(Bitmap bm, String fileName) {//将布局保存为图片

        if (bm == null) {
            Toast.makeText(this, "savePicture null !", Toast.LENGTH_SHORT).show();
            return;
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
            Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //通知相册更新
        MediaStore.Images.Media.insertImage(this.getContentResolver(),
                bm, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        this.sendBroadcast(intent);
    }

    @OnClick({R.id.left_bar_item, R.id.right_title_bar, R.id.header_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.right_title_bar:
                mUserInfo.setDrawingCacheEnabled(true);
                mUserInfo.buildDrawingCache();

                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        final Bitmap bmp = mUserInfo.getDrawingCache(); // 获取图片

                        savePicture(bmp, "aite_qrcode.jpg");// 保存图片
                        mUserInfo.destroyDrawingCache(); // 保存过后释放资源
                    }
                }, 500);

                break;
            case R.id.header_img:
                break;
        }
    }
}
