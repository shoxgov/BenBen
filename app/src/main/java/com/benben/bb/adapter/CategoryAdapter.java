package com.benben.bb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.okhttp3.response.CompanyCategoryResponse;

import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private boolean isSub;
    private List<CompanyCategoryResponse.CategoryFirst> paredate;
    private List<String> data = new ArrayList<>();
    private int selectItem = 0;

    public CategoryAdapter(Context context, boolean isSub) {
        this.context = context;
        this.isSub = isSub;
    }

    public void setData(List<CompanyCategoryResponse.CategoryFirst> paredate) {
        this.paredate = paredate;
    }

    public int getSelectItem(){
        return selectItem;
    }

    public void setSelectItem(int item) {
        if (paredate == null || paredate.isEmpty()) {
            return;
        }
        selectItem = item;
        data.clear();
        if (isSub) {
            List<CompanyCategoryResponse.CategorySecond> temp = paredate.get(item).getCompanyCategory();
            for (CompanyCategoryResponse.CategorySecond csm : temp) {
                data.add(csm.getName());
            }
        } else {
            for (CompanyCategoryResponse.CategoryFirst cpm : paredate) {
                data.add(cpm.getName());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
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
                    R.layout.adapter_category, null, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.category_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(data.get(position));
        if (!isSub) {
            if (position == selectItem) {
                convertView.setBackgroundResource(R.color.mainbg);
            } else {
                convertView.setBackgroundResource(R.color.white);
            }
        } else {

        }
        return convertView;
    }

    public class ViewHolder {
        public TextView name;
    }


}
