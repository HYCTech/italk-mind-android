package com.lw.italk.adapter.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.lw.italk.R;
import com.lw.italk.gson.friend.DeptNodeItem;
import com.unnamed.b.atv.model.TreeNode;


/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class ProfileHolder extends TreeNode.BaseNodeViewHolder<DeptNodeItem> {


    public ProfileHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, DeptNodeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_profile_node, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        if (value.isDept()){
            tvValue.setText(value.getDeptName());
        }else{
            tvValue.setText(value.getUsername());
        }

        final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
        iconView.setIconText(context.getResources().getString(R.string.ic_keyboard_arrow_down));

        return view;
    }

    @Override
    public void toggle(boolean active) {
    }

    @Override
    public int getContainerStyle() {
        return com.unnamed.b.atv.R.style.TreeNodeStyle;
    }
}
