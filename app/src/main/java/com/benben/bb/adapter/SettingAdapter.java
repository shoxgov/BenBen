package com.benben.bb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.bean.SettingItem;
import com.benben.bb.bean.UserData;

import java.util.List;


public class SettingAdapter extends BaseAdapter {

    private Context context;
    private List<SettingItem> data;

    public SettingAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<SettingItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public SettingItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_setting, null, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.setting_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.setting_hint);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SettingItem si = data.get(position);
        viewHolder.title.setText(si.getTitle());
        viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(si.getIconResId()), null, null, null);
        viewHolder.content.setText(si.getContent());
        return convertView;
    }

    public class ViewHolder {
        public TextView title;
        public TextView content;
    }


}
