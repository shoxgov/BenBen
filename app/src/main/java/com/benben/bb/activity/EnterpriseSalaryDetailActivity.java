package com.benben.bb.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.bean.EnterpriseSalaryWorker;
import com.benben.bb.dialog.EnterpriseSalarySuccessDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyEmployeeEntryResponse;
import com.benben.bb.okhttp3.response.EnterpriseSalarySuccessResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.TitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-18.
 * email:shoxgov@126.com
 */

public class EnterpriseSalaryDetailActivity extends BaseActivity {
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.enterprise_salary_detail_date)
    TextView dateTv;
    @Bind(R.id.enterprise_salary_detail_positionname)
    TextView positionnameTv;
    @Bind(R.id.enterprise_salary_detail_base)
    TextView baseTv;
    @Bind(R.id.enterprise_salary_detail_commission)
    TextView commissionTv;
    @Bind(R.id.enterprise_salary_detail_total)
    TextView totalTv;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    /**
     * 该职位的ID
     */
    private int positionId;
    private int companyId;
    private float salary;
    private float commision;
    private float totalWages;
    private String positionName;
    private HashMap<Integer, EnterpriseSalaryWorker> workMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_detail);
        ButterKnife.bind(this);
        positionId = getIntent().getIntExtra("id", 0);
        companyId = getIntent().getIntExtra("companyId", 0);
        salary = getIntent().getFloatExtra("salary", 0);
        commision = getIntent().getFloatExtra("commision", 0);
        positionName = getIntent().getStringExtra("positionName");
        init();
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
                Intent history = new Intent();
                history.setClass(EnterpriseSalaryDetailActivity.this, EnterpriseSalaryHistoryActivity.class);
                history.putExtra("positionId", positionId);
                startActivity(history);

            }
        });
        positionnameTv.setText(positionName);
        baseTv.setText("基本薪资：" + salary + "元/小时");
        commissionTv.setText("就业顾问佣金：" + commision + "/小时");
        recyclerSwipeLayout.createAdapter(R.layout.list_salary_detail_item);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        requestEmployPosition();
    }

    private void requestEmployPosition() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("positionId", "" + positionId);//职位ID
        params.put("entryStatus", "1");//状态默认0,0未入职1已入职
        OkHttpUtils.getAsyn(NetWorkConfig.COMPANY_EMPLOYEE_DETAIL_LIST, params, CompanyEmployeeEntryResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    CompanyEmployeeEntryResponse mar = (CompanyEmployeeEntryResponse) resultDesc;
                    if (mar.getCode() == 1) {
                        pageNo = mar.getData().getPageNum();
                        totalPage = mar.getData().getPages();
                        if (totalPage == 0) {
                            recyclerSwipeLayout.setEmpty();
                            return;
                        }
                        List<CompanyEmployeeEntryResponse.EmployeeEntryInfo> temp = mar.getData().getList();
                        recyclerSwipeLayout.openLoadMore(totalPage);
                        recyclerSwipeLayout.addData(temp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
            }
        });
    }

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
                requestEmployPosition();
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };


    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            CompanyEmployeeEntryResponse.EmployeeEntryInfo eei = (CompanyEmployeeEntryResponse.EmployeeEntryInfo) itemModel;
            baseViewHolder.setText(R.id.enterprise_salary_detail_item_username, eei.getTrueName() + "    " + eei.getUserName());
            EditText workHourEdit = (EditText) baseViewHolder.getView(R.id.enterprise_salary_detail_item_workhour);
            EditText deduckEdit = (EditText) baseViewHolder.getView(R.id.enterprise_salary_detail_item_deduck);
            EditText awardEdit = (EditText) baseViewHolder.getView(R.id.enterprise_salary_detail_item_award);
            EditText whyEdit = (EditText) baseViewHolder.getView(R.id.enterprise_salary_detail_item_why);
//            TextView factgive = (TextView) baseViewHolder.getView(R.id.enterprise_salary_detail_item_factgive);
            workHourEdit.addTextChangedListener(new MyTextWatcher(baseViewHolder, eei, 1));
            deduckEdit.addTextChangedListener(new MyTextWatcher(baseViewHolder, eei, 2));
            awardEdit.addTextChangedListener(new MyTextWatcher(baseViewHolder, eei, 3));
            whyEdit.addTextChangedListener(new MyTextWatcher(baseViewHolder, eei, 4));
        }
    };

    class MyTextWatcher implements TextWatcher {
        private final int key;
        private final CompanyEmployeeEntryResponse.EmployeeEntryInfo eei;
        private final BaseViewHolder baseViewHolder;

        public MyTextWatcher(BaseViewHolder baseViewHolder, CompanyEmployeeEntryResponse.EmployeeEntryInfo eei, int key) {
            this.baseViewHolder = baseViewHolder;
            this.eei = eei;
            this.key = key;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String temp = s.toString();
            if (TextUtils.isEmpty(temp)) {
                return;
            }
            try {
                switch (key) {
                    case 1:
                    case 2:
                    case 3:
                        if (temp.endsWith(".")) {
                            return;
                        }
                        float work = Float.parseFloat(temp);
                        if (work < 0) {
                            return;
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            int userid = eei.getUserId();
            EnterpriseSalaryWorker esw;
            if (workMap.containsKey(userid)) {
                esw = workMap.get(userid);
            } else {
                esw = new EnterpriseSalaryWorker();
                esw.setUserId(userid);
                esw.setFromId(eei.getFromId());
            }
            EditText workHourEdit = (EditText) baseViewHolder.getView(R.id.enterprise_salary_detail_item_workhour);
            EditText deduckEdit = (EditText) baseViewHolder.getView(R.id.enterprise_salary_detail_item_deduck);
            EditText awardEdit = (EditText) baseViewHolder.getView(R.id.enterprise_salary_detail_item_award);
            float factWage, award, debuck, work;
            try {
                if (key == 1) {
                    work = Float.parseFloat(temp);
                } else {
                    work = Float.parseFloat(workHourEdit.getText().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                work = 0;
            }
            try {
                if (key == 2) {
                    debuck = Float.parseFloat(temp);
                } else {
                    debuck = Float.parseFloat(deduckEdit.getText().toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
                debuck = 0;
            }
            try {
                if (key == 3) {
                    award = Float.parseFloat(temp);
                } else {
                    award = Float.parseFloat(awardEdit.getText().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                award = 0;
            }
            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            switch (key) {
                case 1:
                    esw.setWorkHours(Float.parseFloat(temp));
                    factWage = salary * work - debuck + award;
                    esw.setFactMoney(factWage);
                    baseViewHolder.setText(R.id.enterprise_salary_detail_item_factgive, "实发：" + decimalFormat.format(factWage) + "元");
                    break;
                case 2:
                    factWage = salary * work - debuck + award;
                    esw.setBuckleMoney(Float.parseFloat(temp));
                    esw.setFactMoney(factWage);
                    baseViewHolder.setText(R.id.enterprise_salary_detail_item_factgive, "实发：" + decimalFormat.format(factWage) + "元");
                    break;
                case 3:
                    factWage = salary * work - debuck + award;
                    esw.setRewardMoney(Float.parseFloat(temp));
                    esw.setFactMoney(factWage);
                    baseViewHolder.setText(R.id.enterprise_salary_detail_item_factgive, "实发：" + decimalFormat.format(factWage) + "元");
                    break;
                case 4:
                    esw.setBuckleDetails(temp);
                    break;
            }
            workMap.put(userid, esw);
            updateToal();
        }
    }

    private void updateToal() {
        if (workMap.isEmpty()) {
            return;
        }
        totalWages = 0;
        Set<Integer> keySet = workMap.keySet();
        for (Integer keyS : keySet) {
            totalWages += workMap.get(keyS).getFactMoney();
        }
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        totalTv.setText("合计：" + decimalFormat.format(totalWages));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.enterprise_salary_detail_date, R.id.enterprise_salary_detail_submit})
    public void onViewClicked(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.enterprise_salary_detail_date:
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        dateTv.setText(year + "-" + (++month));      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                DatePickerDialog dlg = new DatePickerDialog(new ContextThemeWrapper(EnterpriseSalaryDetailActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar), listener, yy, mm, dd) {
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        LinearLayout mSpinners = (LinearLayout) findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
                        if (mSpinners != null) {
                            NumberPicker mMonthSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
                            NumberPicker mYearSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
                            mSpinners.removeAllViews();
                            if (mMonthSpinner != null) {
                                mSpinners.addView(mMonthSpinner);
                            }
                            if (mYearSpinner != null) {
                                mSpinners.addView(mYearSpinner);
                            }
                        }
                        View dayPickerView = findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));
                        if (dayPickerView != null) {
                            dayPickerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onDateChanged(DatePicker view, int year, int month, int day) {
                        super.onDateChanged(view, year, month, day);
                        setTitle("请选择年月");
                    }
                };
                dlg.setTitle("请选择年月");
                dlg.show();
                break;
            case R.id.enterprise_salary_detail_submit:
                final String dateTemp = dateTv.getText().toString();
                if (TextUtils.isEmpty(dateTemp)) {
                    ToastUtil.showText("请选择年月份");
                    return;
                }
                if (workMap.isEmpty()) {
                    ToastUtil.showText("请至少给一位员工发工资");
                    return;
                }
                List<EnterpriseSalaryWorker> wagesList = new ArrayList<>();
                wagesList.addAll(workMap.values());
//                Set<Integer> keySet = workMap.keySet();
//                for (Integer keyS : keySet) {
//                    wagesList.add(workMap.get(keyS));
//                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("salary", salary + "");
                params.put("commision", commision + "");
                params.put("companyId", companyId + "");
                params.put("positionId", positionId + "");
                DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                params.put("wagesSum", decimalFormat.format(totalWages));
                params.put("wagesMonth", dateTemp + "");
                final String wagesTitle = dateTemp.replace("-", "年") + "月份" + positionName;
                params.put("wagesTitle", wagesTitle);
                WagesList wageList = new WagesList();
                wageList.setWagesList(wagesList);
                params.put("wagesList", new Gson().toJson(wageList));
                OkHttpUtils.getAsyn(NetWorkConfig.COMPANY_SAVE_SALARY, params, EnterpriseSalarySuccessResponse.class, new HttpCallback() {
                    @Override
                    public void onSuccess(BaseResponse br) {
                        super.onSuccess(br);
                        ToastUtil.showText(br.getMessage());
                        EnterpriseSalarySuccessResponse esr = (EnterpriseSalarySuccessResponse) br;
                        if (esr.getCode() == 1) {
                            EnterpriseSalarySuccessDialog dialog = new EnterpriseSalarySuccessDialog(EnterpriseSalaryDetailActivity.this, wagesTitle, esr.getData().getBankType() + "        " + esr.getData().getBankCard(), new DialogCallBack() {
                                @Override
                                public void OkDown(Object obj) {
                                    finish();
                                }

                                @Override
                                public void CancleDown() {
                                }
                            });
                            dialog.show();

                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        ToastUtil.showText(message);
                    }
                });
                break;
        }
    }

    class WagesList {
        private List<EnterpriseSalaryWorker> wagesList;

        public List<EnterpriseSalaryWorker> getWagesList() {
            return wagesList;
        }

        public void setWagesList(List<EnterpriseSalaryWorker> wagesList) {
            this.wagesList = wagesList;
        }
    }
}
