package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.SettingAdapter;
import com.benben.bb.bean.SettingItem;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyCategoryResponse;
import com.benben.bb.okhttp3.response.CompanyInfoResponse;
import com.benben.bb.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-11.
 * email:shoxgov@126.com
 */

public class MyEnterpriseActivity extends BaseActivity {

    @Bind(R.id.setting_list)
    ListView list;
    @Bind(R.id.myenterprise_name)
    TextView nameTv;
//    @Bind(R.id.myenterprise_category)
//    TextView categoryTv;
//    @Bind(R.id.myenterprise_truename)
//    TextView truenameTv;
//    @Bind(R.id.myenterprise_idno)
//    TextView idnoTv;
    private SettingAdapter adapter;
    private List<CompanyCategoryResponse.CategoryFirst> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myenterprise);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void init() {
        TitleBar titleBar = (TitleBar) findViewById(R.id.titlelayout);
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
//        truenameTv.setText(UserData.getUserData().getTrueName());
//        if (!TextUtils.isEmpty(UserData.getUserData().getIdentityCard())) {
//            try {
//                idnoTv.setText(UserData.getUserData().getIdentityCard().replace(UserData.getUserData().getIdentityCard().substring(4, 8), "****"));
//            }catch (Exception e){
//
//            }
//        }
        adapter = new SettingAdapter(this);
        list.setAdapter(adapter);
        List<SettingItem> data = new ArrayList<>();
        data.add(new SettingItem(R.mipmap.myenterprise_info, "企业信息", ""));
        data.add(new SettingItem(R.mipmap.myenterprise_recruit, "招聘管理", ""));
        data.add(new SettingItem(R.mipmap.myenterprise_salary, "工资核算", ""));
        data.add(new SettingItem(R.mipmap.myenterprise_employee, "应聘管理", ""));
        adapter.setData(data);
        list.setOnItemClickListener(onItemClickListener);
        getCompanyInfo();
    }

    private void requestCategory() {
        OkHttpUtils.getAsyn(NetWorkConfig.GET_COMPANY_CATEGORY, CompanyCategoryResponse.class, new HttpCallback() {

            @Override
            public void onSuccess(BaseResponse baseResponse) {
                super.onSuccess(baseResponse);
                CompanyCategoryResponse ccr = (CompanyCategoryResponse) baseResponse;
                if (ccr.getCode() == 1) {
                    categoryList = ccr.getData();
                }
                getCompanyInfo();
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                getCompanyInfo();
            }
        });
    }

    private void getCompanyInfo() {
        OkHttpUtils.getAsyn(NetWorkConfig.GET_COMPANY_INFO, CompanyInfoResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                super.onSuccess(baseResponse);
                CompanyInfoResponse cir = (CompanyInfoResponse) baseResponse;
                if (cir.getCode() == 1) {
                    nameTv.setText(cir.getData().getCompanyName());
//                    String category = cir.getData().getCategoriesId() + "";
//                    if (categoryList != null && !categoryList.isEmpty()) {
//                        category = findCategoryName(cir.getData().getCategoriesId());
//                    }
//                    categoryTv.setText(cir.getData().getCompanyRegion());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    private String findCategoryName(int id) {
        for (CompanyCategoryResponse.CategoryFirst cf : categoryList) {
            List<CompanyCategoryResponse.CategorySecond> categorySecond = cf.getCompanyCategory();
            if (categorySecond == null || categorySecond.isEmpty()) {
                continue;
            }
            for (CompanyCategoryResponse.CategorySecond cs : categorySecond) {
                if (cs.getId() == id) {
                    return cs.getName();
                }
            }
        }
        return "";
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    Intent info = new Intent();
                    info.setClass(MyEnterpriseActivity.this, EnterpriseInfoActivity.class);
                    startActivity(info);
                    break;
                case 1:
                    Intent recruit = new Intent();
                    recruit.setClass(MyEnterpriseActivity.this, EnterpriseRecruitActivity.class);
                    startActivity(recruit);
                    break;
                case 2:
                    Intent salary = new Intent();
                    salary.setClass(MyEnterpriseActivity.this, EnterpriseSalaryActivity.class);
                    startActivity(salary);
                    break;
                case 3:
                    Intent setting = new Intent();
                    setting.setClass(MyEnterpriseActivity.this, EnterpriseEmployeeActivity.class);
                    startActivity(setting);
                    break;
            }
        }
    };
}
