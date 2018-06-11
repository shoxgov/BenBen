package com.benben.bb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.benben.bb.R;

public class BottomSelectAdapter extends BaseAdapter {

    private Context context;
    private String[] data;

    public BottomSelectAdapter(Context context) {
        this.context = context;
    }

    public void setData(String[] data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.dialog_select_item, null, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.dialog_select_item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String si = data[position];
        viewHolder.title.setText(si);
        return convertView;
    }


    public class ViewHolder {
        public TextView title;
    }


}
