package com.benben.bb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.okhttp3.response.MyResSignupPositionUserResponse;
import com.bumptech.glide.Glide;

import java.util.List;

public class BrokerPositionUserGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private boolean iscbVisible = false;
    private List<MyResSignupPositionUserResponse.PositionUserInfo> mList;

    public BrokerPositionUserGridViewAdapter(Context mContext) {
        super();
        this.iscbVisible = iscbVisible;
        this.mContext = mContext;
    }

    public void setData(List<MyResSignupPositionUserResponse.PositionUserInfo> mList) {
        this.mList = mList;
        notifyDataSetChanged();
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
                    R.layout.gridview_broker_myres_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.item_img);
            holder.iccb = (CheckBox) convertView.findViewById(R.id.item_img_cb);
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            holder.hint = (TextView) convertView.findViewById(R.id.item_hint);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MyResSignupPositionUserResponse.PositionUserInfo epi = mList.get(position);
        Glide.with(mContext)
                .load(epi.getAvatar())
                .error(R.mipmap.default_image)
                .into(holder.icon);
        holder.name.setText(epi.getTrueName());
        holder.hint.setText(epi.getUserName());
        return convertView;
    }

    private class ViewHolder {
        public TextView name;
        public TextView hint;
        public ImageView icon;
        public CheckBox iccb;
    }
}