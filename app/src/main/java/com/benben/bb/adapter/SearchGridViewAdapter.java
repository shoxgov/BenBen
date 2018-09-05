package com.benben.bb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.activity.EmployActivity;
import com.benben.bb.utils.Utils;

import java.util.List;

public class SearchGridViewAdapter extends BaseAdapter {
    private EmployActivity.GridViewItemOnClick listener;
    private Context mContext;
    private List<String> mList;
    private int selectItem = 0;
    private String selectedTag = "";

    public SearchGridViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public SearchGridViewAdapter(Context mContext, EmployActivity.GridViewItemOnClick listener) {
        super();
        this.mContext = mContext;
        this.listener = listener;
    }

    public void setData(List<String> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public List<String> getData() {
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
                    R.layout.gridview_search_item, null);
            holder.main = (LinearLayout) convertView.findViewById(R.id.item_main);
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String epi = mList.get(position);
        if (selectItem == position) {
            holder.main.setBackgroundResource(R.drawable.edittext_blue_corner);
            holder.name.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.main.setBackgroundResource(R.drawable.edittext_white_corner);
            holder.name.setTextColor(mContext.getResources().getColor(R.color.gray_dark));
        }
        holder.name.setText(epi);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
        return convertView;
    }

    public void setSelectItem(int position) {
        this.selectItem = position;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView name;
        public LinearLayout main;
    }
}