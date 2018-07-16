package com.benben.bb.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.dialog.ThreeWheelViewDialogs;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyRecruitDetailResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.CustumViewGroup;
import com.benben.bb.view.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-15.
 * email:shoxgov@126.com
 */

public class EnterpriseAddRecruitActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_IMG = 15;
    //    List<String> tagSelect = new ArrayList<>();
    @Bind(R.id.tag_layout)
    LinearLayout tagLayout;
    //    @Bind(R.id.gracefull_layout)
//    LinearLayout gracefullLayout;
    @Bind(R.id.enterprise_recruit_add_position_name)
    EditText positionNameEdit;
    @Bind(R.id.enterprise_recruit_add_people_num)
    EditText peopleNumEdit;
    @Bind(R.id.enterprise_recruit_add_addr)
    TextView addrTv;
    @Bind(R.id.enterprise_recruit_add_endtime)
    TextView endtimeTv;
    @Bind(R.id.enterprise_recruit_add_salaryhour)
    EditText salaryhourEdit;
    @Bind(R.id.enterprise_recruit_add_workhour)
    EditText workhourEdit;
//    @Bind(R.id.enterprise_recruit_add_commissionhour)
//    EditText commissionhourEdit;
    @Bind(R.id.enterprise_recruit_add_totalsalary)
    EditText totalsalaryEdit;
    //    @Bind(R.id.enterprise_recruit_add_days)
//    EditText daysEdit;
//    @Bind(R.id.enterprise_recruit_add_hours)
//    EditText hoursEdit;
    //    @Bind(R.id.enterprise_recruit_add_remarks)
//    EditText remarksEdit;
    @Bind(R.id.enterprise_recruit_add_inteview)
    EditText inteviewEdit;
    @Bind(R.id.enterprise_recruit_add_tag)
    EditText tagEdit;
    @Bind(R.id.recruit_detail_commission_settle_rg)
    RadioGroup settleRg;
    @Bind(R.id.recruit_detail_commission_settle_count)
    EditText settleCountEdit;
    @Bind(R.id.enterprise_recruit_commission_supply)
    EditText commissionSupplyEdit;
    @Bind(R.id.enterprise_recruit_dining)
    EditText diningEdit;
    @Bind(R.id.enterprise_recruit_room)
    EditText roomEdit;
    @Bind(R.id.enterprise_recruit_add_age)
    EditText ageEdit;
    @Bind(R.id.enterprise_recruit_add_sex)
    EditText sexEdit;


    /**
     * 标记是否原来有图片
     */
//    private boolean isHavePic = false;
//    List<String> pics = new ArrayList<>();
    /**
     * 发布的职位修改时需要传id
     */
    private int positionId;
    private String welfFare = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_recruit_add);
        positionId = getIntent().getIntExtra("positionId", 0);
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
        //////////////////////////////////////
//        pics.add("default");
//        addPicLayout(gracefullLayout, pics);
        if (positionId > 0) {
            requestRecruitDetail();
        }
//        salaryhourEdit.addTextChangedListener(new MyTextWatcher());
//        daysEdit.addTextChangedListener(new MyTextWatcher());
//        hoursEdit.addTextChangedListener(new MyTextWatcher());
    }

    class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
//                float pp = Float.parseFloat(s.toString());
//                if (pp <= 0) {
//                    return;
//                }
//                float salary = Float.parseFloat(salaryhourEdit.getText().toString());
//                float day = Float.parseFloat(daysEdit.getText().toString());
//                float hour = Float.parseFloat(hoursEdit.getText().toString());
//                totalsalaryEdit.setText(salary * day * hour + "");
            } catch (Exception e) {
                e.printStackTrace();
//                totalsalaryEdit.setText("0");
            }
        }
    }

    private void requestRecruitDetail() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("positionId", positionId + "");
        OkHttpUtils.getAsyn(NetWorkConfig.COMPANY_RECRUIT_DETAIL, params, CompanyRecruitDetailResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                CompanyRecruitDetailResponse crdr = (CompanyRecruitDetailResponse) br;
                if (crdr.getCode() == 1) {
                    try {
                        positionNameEdit.setText(crdr.getData().getPositionName());
                        peopleNumEdit.setText(crdr.getData().getHiringCount() + "");
                        addrTv.setText(crdr.getData().getRegion());
                        if (crdr.getData().getEndTime().contains(" ")) {
                            endtimeTv.setText(crdr.getData().getEndTime().split(" ")[0]);
                        } else {
                            endtimeTv.setText(crdr.getData().getEndTime());
                        }
                        salaryhourEdit.setText(crdr.getData().getSalary() + "");
                        workhourEdit.setText(crdr.getData().getDayworkHour() + "");
//                        commissionhourEdit.setText(crdr.getData().getCommision() + "");
                        settleCountEdit.setText(crdr.getData().getCommision() + "");
                        commissionSupplyEdit.setText(crdr.getData().getCommisionDetails());
                        ageEdit.setText(crdr.getData().getEntryAge() + "");
                        sexEdit.setText(crdr.getData().getEntrySex() + "");
                        diningEdit.setText(crdr.getData().getStaffCanteen());
                        roomEdit.setText(crdr.getData().getStaffHouse());
//                        remarksEdit.setText(crdr.getData().getSupplement());
                        inteviewEdit.setText(crdr.getData().getJobDemand());
                        totalsalaryEdit.setText(crdr.getData().getFocusSalary());
                        switch (crdr.getData().getSettlement()) {////结算方式（0工时1一次性2月结）
                            case 0:
                                ((RadioButton) settleRg.findViewById(R.id.recruit_detail_commission_settle_type_3)).setChecked(true);
                                break;
                            case 1:
                                ((RadioButton) settleRg.findViewById(R.id.recruit_detail_commission_settle_type_1)).setChecked(true);
                                break;
                            case 2:
                                ((RadioButton) settleRg.findViewById(R.id.recruit_detail_commission_settle_type_2)).setChecked(true);
                                break;
                        }
//                        daysEdit.setText(crdr.getData().getMonthworkDay() + "");
//                        hoursEdit.setText(crdr.getData().getDayworkHour() + "");
//                        String houseImg = crdr.getData().getHouseImg();
//                        if (TextUtils.isEmpty(houseImg)) {
//                            return;
//                        }
//                        if (houseImg.contains(",")) {
//                            String[] picss = houseImg.split(",");
//                            for (String p : picss) {
//                                pics.add(pics.size() - 1, p);
//                            }
//                        } else {
//                            pics.add(pics.size() - 1, houseImg);
//                        }
//                        addPicLayout(gracefullLayout, pics);
                        welfFare = crdr.getData().getWelfare();
                        initFare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
            }
        });
    }

    private void initFare() {
        CustumViewGroup custumViewGroup = new CustumViewGroup(this);
        custumViewGroup.removeAllViews();
        if (TextUtils.isEmpty(welfFare)) {
            tagLayout.removeAllViews();
            tagLayout.addView(custumViewGroup);
            return;
        }
        String[] tags;
        if (welfFare.contains(",")) {
            tags = welfFare.split(",");
        } else {
            tags = new String[]{welfFare};
        }
        for (int i = 0; i < tags.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.tag_layout_bg, null);
            TextView tag = (TextView) view.findViewById(R.id.tag_layout_cb);
            ImageView tagClear = (ImageView) view.findViewById(R.id.tag_layout_clear);
            tag.setText(tags[i]);
            tagClear.setTag(tags[i]);
            tagClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String clear = v.getTag().toString();
                    if (welfFare.contains(",")) {
                        if (welfFare.startsWith(clear)) {
                            welfFare = welfFare.replace(clear + ",", "");
                        } else {
                            welfFare = welfFare.replace("," + clear, "");
                        }
                    } else {
                        welfFare = welfFare.replace(clear, "");
                    }
                    initFare();
                }
            });
            custumViewGroup.addView(view);
        }
        tagLayout.removeAllViews();
        tagLayout.addView(custumViewGroup);
    }

    private void addPicLayout(LinearLayout layout, List<String> url) {
//        layout.removeAllViews();
//        if (url == null || url.isEmpty()) {
//            return;
//        }
//        int width = Utils.dip2px(this, 260);
//        int height = Utils.dip2px(this, 140);
//        int span = Utils.dip2px(this, 10);
//        for (int i = 0; i < url.size(); i++) {
//            View imgLayout = LayoutInflater.from(this).inflate(R.layout.enterprise_grace_image, null, false);
//            RoundImageView iv = imgLayout.findViewById(R.id.gracefull_img);
//            ImageView clear = imgLayout.findViewById(R.id.gracefull_img_clear);
//            if (url.get(i).equals("default")) {
//                iv.setImageResource(R.mipmap.enterprise_certify_bg);
//                clear.setVisibility(View.GONE);
//                clear.setOnClickListener(null);
//            } else {
//                Glide.with(this).load(url.get(i)).error(R.mipmap.default_image).into(iv);
//                clear.setVisibility(View.VISIBLE);
//                clear.setOnClickListener(this);
//            }
//            iv.setOnClickListener(new ImgOnClickLisener(url.get(i), "图片"));
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
//            param.setMargins(span, 0, span, 0);
//            param.gravity = Gravity.CENTER;
//            imgLayout.setLayoutParams(param);
//            layout.addView(imgLayout);
//        }
    }

    @OnClick({R.id.enterprise_recruit_add_tag_btn, R.id.enterprise_recruit_add_addr, R.id.enterprise_recruit_add_submit, R.id.enterprise_recruit_add_endtime})
    public void onViewClicked(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.enterprise_recruit_add_tag_btn:
                String temp = tagEdit.getText().toString();
                if (TextUtils.isEmpty(temp)) {
                    ToastUtil.showText("请输入福利");
                    return;
                }
                if (welfFare.contains(temp)) {
                    ToastUtil.showText("不能重复添加");
                    return;
                }
                if (TextUtils.isEmpty(welfFare)) {
                    welfFare = temp;
                } else {
                    welfFare += "," + temp;
                }
                tagEdit.setText("");
                initFare();
                break;

            case R.id.enterprise_recruit_add_addr:
                ThreeWheelViewDialogs addressDla = new ThreeWheelViewDialogs(this, new DialogCallBack() {
                    @Override
                    public void OkDown(final Object obj) {
                        if (!TextUtils.isEmpty(obj.toString())) {
                            addrTv.setText(obj.toString());
                        }

                    }

                    @Override
                    public void CancleDown() {
                    }
                });
                addressDla.show();
                break;
            case R.id.enterprise_recruit_add_endtime:
                // 调用时间选择器
                Calendar ca = Calendar.getInstance();
                int mYear = ca.get(Calendar.YEAR);
                int mMonth = ca.get(Calendar.MONTH);
                int mDay = ca.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(this, onDateSetListener, mYear, mMonth, mDay).show();
                break;
            case R.id.enterprise_recruit_add_submit:
                String pName = positionNameEdit.getText().toString();
                String pNum = peopleNumEdit.getText().toString();
                String addr = addrTv.getText().toString();
                String endTime = endtimeTv.getText().toString();
                String salary = salaryhourEdit.getText().toString();
                String workH = workhourEdit.getText().toString();
//                String commission = commissionhourEdit.getText().toString();
                String total = totalsalaryEdit.getText().toString();
//                String monthDay = daysEdit.getText().toString();
//                String dayHour = hoursEdit.getText().toString();
//                String remark = remarksEdit.getText().toString();
                String interview = inteviewEdit.getText().toString();
                String settleCount = settleCountEdit.getText().toString();
                String commissionSupply = commissionSupplyEdit.getText().toString();
                String age = ageEdit.getText().toString();
                String sex = sexEdit.getText().toString();
                String staffCanteen = diningEdit.getText().toString();
                String room = roomEdit.getText().toString();
                if (TextUtils.isEmpty(pName) || TextUtils.isEmpty(pNum) || TextUtils.isEmpty(addr) || TextUtils.isEmpty(endTime) || TextUtils.isEmpty(salary) ||
                        TextUtils.isEmpty(workH) /*|| TextUtils.isEmpty(commission)*/ || TextUtils.isEmpty(total) ||
                        TextUtils.isEmpty(settleCount) || TextUtils.isEmpty(commissionSupply) || TextUtils.isEmpty(age) ||
                        TextUtils.isEmpty(sex) || TextUtils.isEmpty(staffCanteen) || TextUtils.isEmpty(room)
                        ) {
                    ToastUtil.showText("请完善信息");
                    return;
                }
//                String filekey = "file";
//                List<File> files = new ArrayList<>();
//                if (pics != null && !pics.isEmpty()) {
//                    for (String p : pics) {
//                        if (!p.equals("default") && !p.startsWith("http")) {
//                            files.add(new File(p));
//                        }
//                    }
//                }
//                if (files.isEmpty()) {
//                    files = null;
//                    filekey = "";
//                }
//                StringBuffer welfare = new StringBuffer("");
//                if (tagSelect.isEmpty()) {
////                    ToastUtil.showText("请选择福利");
////                    return;
//                } else {
//                    for (String s : tagSelect) {
//                        if (TextUtils.isEmpty(welfare)) {
//                            welfare.append(s);
//                        } else {
//                            welfare.append(",").append(s);
//                        }
//                    }
//                }
                Map<String, String> params = new HashMap<String, String>();
                if (positionId > 0) {
                    params.put("id", positionId + "");
                }
                params.put("positionName", pName);
                params.put("hiringCount", pNum);
                params.put("region", addr);
                params.put("welfare", welfFare);
                params.put("endTime", endTime);
                params.put("salary", salary);
                params.put("workTime", workH);
                params.put("commision", settleCount);
                params.put("focusSalary", total);
//                params.put("monthworkDay", monthDay);
//                params.put("dayworkHour", dayHour);
//                params.put("supplement", remark);
                params.put("jobDemand", interview);
//                params.put("commisionNum", settleCount);
                params.put("commisionDetails", commissionSupply);
                params.put("entryAge", age);
                params.put("entrySex", sex);
                params.put("staffCanteen", staffCanteen);
                params.put("staffHouse", room);
                switch (settleRg.getCheckedRadioButtonId()) {////结算方式（0工时1一次性2月结）
                    case R.id.recruit_detail_commission_settle_type_1:
                        params.put("settlement", "1");
                        break;
                    case R.id.recruit_detail_commission_settle_type_2:
                        params.put("settlement", "2");
                        break;
                    case R.id.recruit_detail_commission_settle_type_3:
                        params.put("settlement", "0");
                        break;
                }
                OkHttpUtils.postAsyn(NetWorkConfig.COMPANY_RECRUIT_ADD, params, BaseResponse.class, new HttpCallback() {
                    @Override
                    public void onSuccess(BaseResponse br) {
                        super.onSuccess(br);
                        ToastUtil.showText(br.getMessage());
                        if (br.getCode() == 1) {
                            setResult(RESULT_OK);
                            finish();
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

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }
            }
            endtimeTv.setText(days);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.gracefull_img_clear:
//                if (isHavePic) {
//                    WarnDialog warnDialog = new WarnDialog(EnterpriseAddRecruitActivity.this, "中途删除会清除所有旧图片，确定删除？", new DialogCallBack() {
//                        @Override
//                        public void OkDown(Object obj) {
//                            isHavePic = false;
//                            pics.clear();
//                            pics.add("default");
//                            addPicLayout(gracefullLayout, pics);
//                        }
//
//                        @Override
//                        public void CancleDown() {
//
//                        }
//                    });
//                    warnDialog.show();
//                } else {
//                    pics.remove(v.getTag().toString());
//                    addPicLayout(gracefullLayout, pics);
//                }
//                break;
        }
    }

//    class ImgOnClickLisener implements View.OnClickListener {
//        String url;
//        String filename;
//
//        public ImgOnClickLisener(String url, String filename) {
//            this.url = url;
//            this.filename = filename;
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (isHavePic) {
//                WarnDialog warnDialog = new WarnDialog(EnterpriseAddRecruitActivity.this, "中途修改会清除所有旧图片，确定删除？", new DialogCallBack() {
//                    @Override
//                    public void OkDown(Object obj) {
//                        isHavePic = false;
//                        pics.clear();
//                        pics.add("default");
//                        addPicLayout(gracefullLayout, pics);
//                        ImagePicker imagePicker = ImagePicker.getInstance();
//                        imagePicker.setImageLoader(new GlideImageLoader());
//                        imagePicker.setMultiMode(false);   //多选
//                        imagePicker.setShowCamera(true);  //显示拍照按钮
//                        imagePicker.setCrop(true);       //不进行裁剪
//                        imagePicker.setOutPutX(1000);
//                        imagePicker.setOutPutY(600);
//                        imagePicker.setFocusWidth(1000);
//                        imagePicker.setFocusHeight(600);
//                        Intent intent = new Intent(EnterpriseAddRecruitActivity.this, ImageGridActivity.class);
//                        startActivityForResult(intent, REQUEST_CODE_IMG);
//                    }
//
//                    @Override
//                    public void CancleDown() {
//
//                    }
//                });
//                warnDialog.show();
//            } else {
//                ImagePicker imagePicker = ImagePicker.getInstance();
//                imagePicker.setImageLoader(new GlideImageLoader());
//                imagePicker.setMultiMode(false);   //多选
//                imagePicker.setShowCamera(true);  //显示拍照按钮
//                imagePicker.setCrop(true);       //不进行裁剪
//                imagePicker.setOutPutX(1000);
//                imagePicker.setOutPutY(600);
//                imagePicker.setFocusWidth(1000);
//                imagePicker.setFocusHeight(600);
//                Intent intent = new Intent(EnterpriseAddRecruitActivity.this, ImageGridActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_IMG);
//            }
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_IMG && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
//            ArrayList<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//            if (items != null && items.size() > 0) {
//                try {
//                    pics.add(pics.size() - 1, items.get(0).path);
//                    addPicLayout(gracefullLayout, pics);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
