package com.lw.italk.adapter.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.johnkil.print.PrintView;
import com.lw.italk.App;
import com.lw.italk.R;
import com.lw.italk.gson.friend.DeptNodeItem;
import com.lw.italk.utils.GlideCircleTransform;
import com.unnamed.b.atv.model.TreeNode;


/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class HeaderHolder extends TreeNode.BaseNodeViewHolder<DeptNodeItem> {

    private PrintView arrowView;
    private Context context;

    public HeaderHolder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View createNodeView(TreeNode node, DeptNodeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_header_node, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.node_value);
        final ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        if (value.isDept()){
            tvValue.setText(value.getDeptName());
            iconView.setVisibility(View.GONE);
        }else{
            tvValue.setText(value.getUsername());
            iconView.setVisibility(View.VISIBLE);
            Glide.with(this.context).load(value.getAvatar())
                    .placeholder(R.drawable.default_img) //设置占位图，在加载之前显示
                    .transform(new GlideCircleTransform(App.getInstance()))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(iconView);
        }


        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
        if (node.isLeaf()) {
            arrowView.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }


}
