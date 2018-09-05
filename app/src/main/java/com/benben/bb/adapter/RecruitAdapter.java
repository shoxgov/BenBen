package com.benben.bb.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.activity.RecruitDetailActivity;
import com.benben.bb.bean.RecruitDetailInfo;
import com.benben.bb.view.RoundImageView;
import com.bumptech.glide.Glide;

import java.util.List;

public class RecruitAdapter extends BaseAdapter {

    private Context context;
    private String companyName = "";
    private List<RecruitDetailInfo> data;

    public RecruitAdapter(Context context) {
        this.context = context;
    }

    public RecruitAdapter(Context context, String companyName) {
        this.context = context;
        this.companyName = companyName;
    }

    public void setData(List<RecruitDetailInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public RecruitDetailInfo getItem(int position) {
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
                    R.layout.list_home_hot_employ, null, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.home_employ_name);
            viewHolder.tag = (TextView) convertView.findViewById(R.id.home_employ_tag);
            viewHolder.ltdName = (TextView) convertView.findViewById(R.id.home_employ_ltd);
            viewHolder.salary = (TextView) convertView.findViewById(R.id.home_employ_price);
            viewHolder.region = (TextView) convertView.findViewById(R.id.home_employ_addr);
            viewHolder.img = (RoundImageView) convertView.findViewById(R.id.home_employ_logo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tag.setVisibility(View.GONE);
        final RecruitDetailInfo ri = data.get(position);
        viewHolder.name.setText(ri.getPositionName());
        viewHolder.ltdName.setText(companyName);
        String region = ri.getRegion();
        if (region.contains(".")) {
            String[] regions = region.split("\\.");
            if (regions.length == 3) {
                viewHolder.region.setText(regions[1] + "." + regions[2]);
            } else {
                viewHolder.region.setText(regions[0] + "." + regions[1]);
            }
        } else {
            viewHolder.region.setText(ri.getRegion());
        }
        viewHolder.salary.setText("¥" + ri.getFocusSalary());
        String img = ri.getHouseImg();
        if (!TextUtils.isEmpty(img) && img.contains(",")) {
            img = img.split(",")[0];
        }
        Glide.with(context)
                .load(img)
                .placeholder(R.mipmap.default_image)
                .error(R.mipmap.default_image)
                .into(viewHolder.img);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent();
                detail.setClass(context, RecruitDetailActivity.class);
                detail.putExtra("positionId", ri.getId());
                detail.putExtra("positionName", ri.getPositionName());
                context.startActivity(detail);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView name;
        public TextView tag;
        public TextView ltdName;
        public TextView salary;
        public TextView region;
        public RoundImageView img;
    }


}
