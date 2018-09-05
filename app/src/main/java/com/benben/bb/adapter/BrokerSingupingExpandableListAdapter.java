package com.benben.bb.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.activity.UserInfoActivity;
import com.benben.bb.okhttp3.response.BrokerEnrollSignupPositionResponse;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.RoundImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshengyin on 2017-05-10.
 * email:shoxgov@126.com
 */

public class BrokerSingupingExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private List<BrokerEnrollSignupPositionResponse.SignData> data = new ArrayList<>();

    public BrokerSingupingExpandableListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<BrokerEnrollSignupPositionResponse.SignData> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return data.get(parentPos).getUserlist().get(childPos);
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return data.get(parentPos).getUserlist() == null ? 0 : data.get(parentPos).getUserlist().size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return data.get(parentPos);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.expanadapter_broker_signuping_parentitem, null);
        }
        TextView parentPosition = (TextView) convertView.findViewById(R.id.broker_signuping_parent_position);
        TextView parentPositionSalary = (TextView) convertView.findViewById(R.id.broker_signuping_parent_position_salary);
        TextView parentCompany = (TextView) convertView.findViewById(R.id.broker_signuping_parent_company);
        TextView parentPositionAddr = (TextView) convertView.findViewById(R.id.broker_signuping_parent_position_addr);
        BrokerEnrollSignupPositionResponse.SignData parentData = data.get(parentPos);
        parentPosition.setText(parentData.getPositionName());
        parentPositionSalary.setText(parentData.getFocusSalary() + "元");
        parentCompany.setText(parentData.getCompanyName());
        String region = parentData.getRegion();
        if (!TextUtils.isEmpty(region) && region.contains(".")) {
            String[] regions = region.split("\\.");
            if (regions.length == 3) {
                parentPositionAddr.setText(regions[1] + "." + regions[2]);
            } else {
                parentPositionAddr.setText(regions[0] + "." + regions[1]);
            }
        } else {
            parentPositionAddr.setText(parentData.getRegion() + "");
        }
        return convertView;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.expanadapter_broker_signuping_childitem, null);
        }
        RoundImageView img = (RoundImageView) view.findViewById(R.id.item_img);
        TextView name = (TextView) view.findViewById(R.id.item_name);
        TextView sex = (TextView) view.findViewById(R.id.item_sex);
        TextView year = (TextView) view.findViewById(R.id.item_year);
        TextView edu = (TextView) view.findViewById(R.id.item_edu);
        final BrokerEnrollSignupPositionResponse.SignupPositionInfo childData = data.get(parentPos).getUserlist().get(childPos);
        if (!TextUtils.isEmpty(childData.getAvatar())) {
            Glide.with(context)
                    .load(childData.getAvatar())
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into(img);
        }
        if (TextUtils.isEmpty(childData.getTrueName())) {
            name.setText("未实名");
        } else {
            name.setText(childData.getTrueName());
        }
        if (childData.getSex() == 1) {
            sex.setText("女");
        } else {
            sex.setText("男");
        }
        year.setText(childData.getAge() + "");
        edu.setText(childData.getEducation());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                Intent person = new Intent();
                person.setClass(context, UserInfoActivity.class);
                person.putExtra("userId", childData.getUserId() + "");
                context.startActivity(person);
            }
        });
        return view;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    //如果返回false 子分割线childdivider不会显示
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}
