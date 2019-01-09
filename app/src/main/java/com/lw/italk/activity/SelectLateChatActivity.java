package com.lw.italk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.adapter.SelectChatGroupAdapter;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.view.CenterDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择一个群聊发送名片
 * Created by 喜明 on 2018/8/19.
 */

public class SelectLateChatActivity extends BaseActivity {
    @BindView(R.id.left_bar_item)
    TextView mLeftBarItem;
    @BindView(R.id.center_bar_item)
    TextView mCenterBarItem;
    @BindView(R.id.right_title_bar)
    TextView mRightTitleBar;
    @BindView(R.id.et_search)
    EditText mSearch;
    @BindView(R.id.list)
    ListView mListview;

    @Override
    protected int setContentView() {
        return R.layout.activity_select_group;
    }

    @Override
    protected void initView() {
        mLeftBarItem.setText("关闭");
        mCenterBarItem.setText("选择一个群聊");
        mRightTitleBar.setText("多选");
    }

    @Override
    protected void initData() {
        try {
            List<Contact> grouplist = GloableParams.contactInfos;
            if (grouplist != null && grouplist.size() > 0) {
                //最后参数表示能否多选
                mListview.setAdapter(new SelectChatGroupAdapter(this, grouplist, false));
            } else {
                TextView txt_nodata = (TextView) findViewById(R.id.txt_nochat);
                txt_nodata.setText("暂时没有群聊");
                txt_nodata.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showCustomDialog();
            }
        });
    }

    private void showCustomDialog() {
        final CenterDialog customDialog = new CenterDialog(this, R.style.CenterDialog);//设置自定义背景
        customDialog.setState("0");
        customDialog.setTitle("天青色等烟雨");
        customDialog.setMsg("[个人名片]");
        customDialog.setHeader("[个人名片]");
        customDialog.setOnNegateListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.setOnPositiveListener(new View.OnClickListener() {
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.left_bar_item, R.id.right_title_bar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.right_title_bar:
                break;
        }
    }
}
