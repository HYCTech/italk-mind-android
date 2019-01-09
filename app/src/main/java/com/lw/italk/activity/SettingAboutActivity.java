package com.lw.italk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lw.italk.R;
import com.lw.italk.framework.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flyn.Eyes;

/**
 * Created by 喜明 on 2018/8/14.
 */

public class SettingAboutActivity extends BaseActivity {
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
    @BindView(R.id.upgrade_version)
    TextView mUpgradeVersion;
    @BindView(R.id.upgrade_log)
    TextView mUpgradeLog;

    @Override
    protected int setContentView() {
        return R.layout.activity_setting_about;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("设置");
        mCenterBarItem.setText("关于");
        mRightTitleBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Eyes.translucentStatusBar(this, true);
    }

    @OnClick({R.id.left_bar_item, R.id.upgrade_version, R.id.upgrade_log})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.upgrade_version:
                break;
            case R.id.upgrade_log:
                break;
        }
    }
}
