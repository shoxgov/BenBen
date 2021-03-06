package com.benben.bb.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.activity.UserInfoActivity;
import com.benben.bb.okhttp3.response.MyResourceResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BrokerGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private boolean iscbVisible = false;
    private List<MyResourceResponse.EntryPositionInfo> mList;
    private String selectedTag = "";

    public BrokerGridViewAdapter(Context mContext, boolean iscbVisible) {
        super();
        this.iscbVisible = iscbVisible;
        this.mContext = mContext;
    }

    public void setData(List<MyResourceResponse.EntryPositionInfo> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void addData(List<MyResourceResponse.EntryPositionInfo> mList) {
        if (this.mList == null) {
            this.mList = new ArrayList<MyResourceResponse.EntryPositionInfo>();
        }
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    public List<MyResourceResponse.EntryPositionInfo> getData() {
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
                    R.layout.gridview_broker_myres_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.item_img);
            holder.iccb = (CheckBox) convertView.findViewById(R.id.item_img_cb);
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            holder.hint = (TextView) convertView.findViewById(R.id.item_hint);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MyResourceResponse.EntryPositionInfo epi = mList.get(position);
        Glide.with(mContext)
                .load(epi.getAvatar())
                .error(R.mipmap.default_image)
                .into(holder.icon);
        if(TextUtils.isEmpty(epi.getTrueName())){
            holder.name.setText("未实名认证");
        }else {
            holder.name.setText(epi.getTrueName());
        }
        holder.hint.setText(epi.getUserName());
        if (iscbVisible) {
            holder.iccb.setVisibility(View.VISIBLE);
            if (selectedTag.contains("#" + epi.getUserId() + "@")) {
                holder.iccb.setChecked(true);
            } else {
                holder.iccb.setChecked(false);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedTag.contains("#" + epi.getUserId() + "@")) {
                        selectedTag = selectedTag.replace("#" + epi.getUserId() + "@", "");
                    } else {
                        selectedTag = selectedTag.concat("#" + epi.getUserId() + "@");
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userInfo = new Intent();
                    userInfo.setClass(mContext, UserInfoActivity.class);
                    userInfo.putExtra("userId", epi.getUserId() + "");
                    mContext.startActivity(userInfo);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView name;
        public TextView hint;
        public ImageView icon;
        public CheckBox iccb;
    }
}