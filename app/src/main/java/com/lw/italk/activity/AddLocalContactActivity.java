package com.lw.italk.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.adapter.AddLocalContactAdapter;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.greendao.model.Contact;
import com.lw.italk.utils.Constants;
import com.lw.italk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flyn.Eyes;

//从通讯录添加好友
public class AddLocalContactActivity extends BaseActivity {
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
    @BindView(R.id.listview)
    ListView mlistview;
    @BindView(R.id.txt_search)
    TextView mSearch;

    private List<Contact> mLocalContactInfos = new ArrayList<Contact>();// 本地联系人信息
    private Thread mThread;
    private AddLocalContactAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Eyes.translucentStatusBar(this, true);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_add_local_contact;
    }

    @Override
    protected void initView() {
        mlistview = (ListView) findViewById(R.id.listview);
    }

    @Override
    protected void initData() {
        mLeftBarItem.setText("新的朋友");
        mCenterBarItem.setText("通讯录");
        mRightTitleBar.setVisibility(View.GONE);
        showProgressDialog("正在获取本地联系人列表");
        getLocalContacts();
    }

    private void getLocalContacts() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                getContacts();
                GloableParams.LocalContactInfos = mLocalContactInfos;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAdapter = new AddLocalContactAdapter(AddLocalContactActivity.this, mLocalContactInfos);
                        mlistview.setAdapter(mAdapter);
                        dismissProgressDialog();
                    }
                });

            }
        });
        mThread.start();
    }

    private void getContacts() {
        //联系人的Uri，也就是content://com.android.contacts/contacts
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //指定获取_id和display_name两列数据，display_name即为姓名
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Contact mContactinfo = new Contact();
                Long id = cursor.getLong(0);
                //获取姓名
                String name = cursor.getString(1);
                //指定获取NUMBER这一列数据
                String[] phoneProjection = new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };

                String tempName = "";
                if (name != null && !TextUtils.isEmpty(name)) {//去除特殊符号，例如"-"
                    for (int i = 0; i < name.length(); i++) {
                        if (Utils.isLetterDigitOrChinese(name.charAt(i)+"")) {
                            tempName += name.charAt(i);
                        }
                    }
                }
                mContactinfo.setRemark(tempName);

                //根据联系人的ID获取此人的电话号码
                Cursor phonesCusor = this.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        phoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null,
                        null);

                String num = "";
                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    do {
                        num = phonesCusor.getString(0);
                    } while (phonesCusor.moveToNext());
                }

                String temp = "";
                if (num != null && !TextUtils.isEmpty(num)) {//去除特殊符号，例如"-"
                    for (int i = 0; i < num.length(); i++) {
                        if (num.charAt(i) >= 48 && num.charAt(i) <= 57) {
                            temp += num.charAt(i);
                        }
                    }
                }

                mContactinfo.setUid(temp);

                mLocalContactInfos.add(mContactinfo);
            } while (cursor.moveToNext());
        }
    }

    @OnClick({R.id.left_bar_item, R.id.txt_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_bar_item:
                finish();
                break;
            case R.id.txt_search:
                Intent intent = new Intent(AddLocalContactActivity.this, SearchViewActivity.class );
                intent.putExtra(Constants.SEARCH_TYPE, Constants.SEARCH_LOCAL_CONTACT);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
