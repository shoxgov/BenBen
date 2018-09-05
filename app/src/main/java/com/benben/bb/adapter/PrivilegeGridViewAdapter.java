package com.benben.bb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.bean.SettingItem;

import java.util.List;

public class PrivilegeGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<SettingItem> mList;
    private String selectedTag = "";

    public PrivilegeGridViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void setData(List<SettingItem> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public List<SettingItem> getData() {
        return mList;
    }

    public String getSelectedTag() {
        return selectedTag;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return this.mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return this.mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(
                    R.layout.gridview_myperson_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.name1 = (TextView) convertView.findViewById(R.id.item_name1);
            convertView.findViewById(R.id.item_name2).setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SettingItem epi = mList.get(position);
        holder.icon.setImageResource(epi.getIconResId());
        holder.name1.setText(epi.getTitle());
        return convertView;
    }

    private class ViewHolder {
        public TextView name1;
        public ImageView icon;
    }
}