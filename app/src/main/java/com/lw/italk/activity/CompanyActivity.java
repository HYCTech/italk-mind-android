package com.lw.italk.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lw.italk.GloableParams;
import com.lw.italk.R;
import com.lw.italk.adapter.holder.ArrowExpandSelectableHeaderHolder;
import com.lw.italk.adapter.holder.HeaderHolder;
import com.lw.italk.adapter.holder.IconTreeItemHolder;
import com.lw.italk.adapter.holder.PlaceHolderHolder;
import com.lw.italk.adapter.holder.ProfileHolder;
import com.lw.italk.adapter.holder.SelectableHeaderHolder;
import com.lw.italk.adapter.holder.SelectableItemHolder;
import com.lw.italk.framework.base.BaseActivity;
import com.lw.italk.framework.common.LWUserManager;
import com.lw.italk.gson.friend.CompanyDeptList;
import com.lw.italk.gson.friend.DeptInfo;
import com.lw.italk.gson.friend.DeptNodeItem;
import com.lw.italk.gson.friend.MemberInfo;
import com.lw.italk.http.BaseResponse;
import com.lw.italk.http.HttpUtils;
import com.lw.italk.http.Request;
import com.lw.italk.http.Response;
import com.lw.italk.http.ResponseErrorException;
import com.lw.italk.utils.Utils;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.yixia.camera.util.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


public class CompanyActivity extends BaseActivity implements Response {

    private TextView txt_title, txt_left;
    @BindView(R.id.tree_layout)
    LinearLayout treeViewLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int setContentView() {
        return R.layout.activity_company;
    }

    @Override
    protected void initView() {
        txt_left = (TextView) findViewById(R.id.left_bar_item);
        txt_left.setText("通讯录");
        txt_left.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.center_bar_item);
        txt_title.setText(GloableParams.companyInfo.getCompanyName());
        findViewById(R.id.right_title_bar).setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
        
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("tokenId", LWUserManager.getInstance().getToken());
        HttpUtils.doPostFormMap(this, Request.Path.FRIEND_DEPTLIST, map, true, Request.Code.FRIEND_DEPTLIST, this);
    }


    private void changeCompanyList(CompanyDeptList list){

        DeptNodeItem parentItem = new DeptNodeItem();
        parentItem.setDept(false);
        parentItem.setUsername(GloableParams.companyInfo.getCompanyName());
        parentItem.setAvatar(GloableParams.companyInfo.getLogo());
        parentItem.setUid(String.valueOf(GloableParams.companyInfo.getCompanyId()));

        TreeNode root = TreeNode.root();

        TreeNode parent = new TreeNode(parentItem);

        root.addChild(parent);

        AndroidTreeView tView = new AndroidTreeView(this, root);
        //设置树形视图开启默认动画
        tView.setDefaultAnimation(true);
        //设置树形视图默认的样式
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided/*com.unnamed.b.atv.R.style.TreeNodeStyle*/);

        tView.setDefaultViewHolder(HeaderHolder.class);

        treeViewLayout.addView(tView.getView());

        List<DeptNodeItem> originTree = changeToList(list);
        toTree(originTree,parent);
        tView.setDefaultNodeClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                node.getLevel();
            }
        });
    }

    private  List<DeptNodeItem> changeToList(CompanyDeptList list){
        List<DeptNodeItem> treeList = new ArrayList<DeptNodeItem>();
        for (DeptInfo dept:list.getDeptInfo()){
            DeptNodeItem nodeItem = new DeptNodeItem();
            nodeItem.setDept(true);
            nodeItem.setDeptName(dept.getDeptName());
            nodeItem.setDeptId(dept.getDeptId());
            nodeItem.setParentId(dept.getParentId());
            treeList.add(nodeItem);
        }

        for (MemberInfo member:list.getMembers()){
            DeptNodeItem nodeItem = new DeptNodeItem();
            nodeItem.setDept(false);
            nodeItem.setUid(member.getUid());
            nodeItem.setUsername(member.getUsername());
            nodeItem.setParentId(member.getDeptId());
            nodeItem.setAvatar(member.getAvatar());
            nodeItem.setRemark(member.getRemark());
            treeList.add(nodeItem);
        }
        return treeList;
    }

    private  void toTree(List<DeptNodeItem> list,TreeNode parent) {
        List<TreeNode> treeList = new ArrayList<TreeNode>();
        for (DeptNodeItem tree : list) {
            if(tree.getParentId() == 0){
                TreeNode node = new TreeNode(tree);
                parent.addChild(node);
                treeList.add(node);
            }
        }
        for (DeptNodeItem tree : list) {
            toTreeChildren(treeList,tree);
        }
        return;
    }

    private  void toTreeChildren(List<TreeNode> treeList, DeptNodeItem tree) {
        for (TreeNode node : treeList) {
            DeptNodeItem nodeValue = (DeptNodeItem) node.getValue();
            if (nodeValue.isDept()){
                if(tree.getParentId() == nodeValue.getDeptId()){
                    if(nodeValue.getChildren() == null){
                        nodeValue.setChildren(new ArrayList<DeptNodeItem>());
                    }
                    nodeValue.getChildren().add(tree);

                    TreeNode childNode = new TreeNode(tree);
                    node.addChild(childNode);
                }
                if(node.getChildren() != null){
                    toTreeChildren(node.getChildren(),tree);
                }
            }
        }
    }

    @Override
    public void next(Object o, int requestCode) {
        switch (requestCode) {
            case Request.Code.FRIEND_DEPTLIST:
                CompanyDeptList list = (CompanyDeptList)o;
                changeCompanyList(list);
                break;
            default:
                break;
        }
    }

    @Override
    public void error(ResponseErrorException t, int requestCode) {
        Toast.makeText(this,t.getMessage(),Toast.LENGTH_LONG);
    }

    @Override
    public Type getTypeToken(int requestCode) {
        Type type = null;
        switch (requestCode) {
            case Request.Code.FRIEND_DEPTLIST:
                type = new TypeToken<BaseResponse<CompanyDeptList>>() {
                }.getType();
                break;
            default:
                break;
        }
        return type;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_bar_item:
                Utils.finish(CompanyActivity.this);
                break;
            default:
                break;
        }
    }
}
