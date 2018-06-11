package com.benben.bb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.benben.bb.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangshengyin on 2017-05-10.
 * email:shoxgov@126.com
 */

public class WalletTradeExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private Map<String, List<String>> dataset = new HashMap<>();
    private String[] parentList;

    public WalletTradeExpandableListAdapter(Context context) {
        this.context = context;
    }

    public void setData(Map<String, List<String>> dataset, String[] parentList) {
        this.dataset = dataset;
        this.parentList = parentList;
        notifyDataSetChanged();
    }

    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return dataset.get(parentList[parentPos]).get(childPos);
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return dataset.size();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return dataset.get(parentList[parentPos]).size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return dataset.get(parentList[parentPos]);
    }

    //  获得某个父项的id
    @Override
    public long getGroupId(int parentPos) {
        return parentPos;
    }

    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int parentPos, int childPos) {
        return childPos;
    }

    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(int parentPos, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expanadapter_wallet_trade_parentitem, null);
        }
        TextView parentTitle = (TextView) convertView.findViewById(R.id.wallet_trade_parent_title);
        TextView parentContent = (TextView) convertView.findViewById(R.id.wallet_trade_parent_content);
        return convertView;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.expanadapter_wallet_trade_childitem, null);
        }
        TextView title = (TextView) view.findViewById(R.id.wallet_trade_child_title);
        TextView date = (TextView) view.findViewById(R.id.wallet_trade_child_date);
        TextView count = (TextView) view.findViewById(R.id.wallet_trade_child_count);
        return view;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    //如果返回false 子分割线childdivider不会显示
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
