package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.GoCertifyDialog;
import com.benben.bb.dialog.SignupSuccessDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.RecruitDetailResponse;
import com.benben.bb.stickLayout.StickLayout;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.BannerView;
import com.benben.bb.view.CustumViewGroup;
import com.benben.bb.view.MyTabIndicator;
import com.benben.bb.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-16.
 * email:shoxgov@126.com
 */

public class RecruitDetailActivity extends BaseActivity {

    @Bind(R.id.recruit_detail_welfare_layout)
    LinearLayout welfLayout;
    @Bind(R.id.recruit_detail_commission_layout)
    LinearLayout commissionLayout;
    @Bind(R.id.mTabIndicator)
    MyTabIndicator tabIndicator;
    @Bind(R.id.sl)
    StickLayout mSl;
    @Bind(R.id.tv2)
    View mTv2;
    @Bind(R.id.tv3)
    View mTv3;
    @Bind(R.id.tv4)
    View mTv4;
    @Bind(R.id.tv5)
    View mTv5;
    @Bind(R.id.bannerlayout)
    LinearLayout bannerlayout;
    @Bind(R.id.recruit_detail_position_name)
    TextView positionNameTv;
    @Bind(R.id.recruit_detail_position_price)
    TextView positionPriceTv;
    @Bind(R.id.recruit_detail_addr)
    TextView addrTv;
    @Bind(R.id.recruit_detail_ltd_name)
    TextView ltdNameTv;
    @Bind(R.id.recruit_detail_endtime)
    TextView endtimeTv;
    @Bind(R.id.recruit_detail_position_commission)
    TextView commissionTv;
    @Bind(R.id.recruit_detail_self_signup)
    TextView selfSignup;
    @Bind(R.id.recruit_detail_other_signup)
    TextView otherSignup;
    @Bind(R.id.recruit_detail_salary_edit)
    EditText baseSalaryEdit;
    @Bind(R.id.recruit_detail_dining_edit)
    EditText baseDiningEdit;
    @Bind(R.id.recruit_detail_demand_edit)
    EditText baseDemandEdit;
    private int currentPosition = -1;
    private BannerView bannerView;
    private LinearLayout bannerLayout;
    /**
     * 职位
     */
    private int positionId;
    private String positionName;
    private int companyId;
    /**
     * hiringCount-招聘人数
     * enrollNum - 已招人数
     */
    private int hiringCount, enrollNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_detail);
        positionId = getIntent().getIntExtra("positionId", 0);
        positionName = getIntent().getStringExtra("positionName");
        ButterKnife.bind(this);
        initViews();
        if (UserData.getUserData().getIsAgent() > 0 && UserData.getUserData().getIsAgent() < 88) {
            selfSignup.setText("自己报名");
            otherSignup.setVisibility(View.VISIBLE);
            commissionLayout.setVisibility(View.VISIBLE);
        }
        init();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
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

        initTabIndicator();
//        mSl.setStickView(findViewById(R.id.tv2)); //设置粘性控件
//        mSl.setStickView(findViewById(R.id.tv3));
//        mSl.canScrollToEndViewTop(true);      //设置是否开启最后控件滑动到顶部
        //设置滑动改变监听（一滑动就会有回调）
        mSl.setOnScrollChangeListener(new StickLayout.OnScrollChangeListener() {
            @Override
            public void onScrollChange(StickLayout v, View currentView, int position, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //直到当前控件改变在做事情
//                LogUtil.d("setOnScrollChangeListener position="+position);
                if (currentPosition != position && position <= 5) {
                    currentPosition = position;
                    switch (currentPosition) {
                        case 0:
                        case 2:
                            tabIndicator.selectItem(0);
                            break;
                        case 3:
                            tabIndicator.selectItem(1);
                            break;
                        case 4:
                            tabIndicator.selectItem(2);
                            break;
                        case 5:
                            tabIndicator.selectItem(3);
                            break;
                    }
                }
            }
        });
        requestDetail();
    }

    private void requestDetail() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", positionId + "");//id	职位ID
        OkHttpUtils.getAsyn(NetWorkConfig.INDEX_RECRUIT_DETAIL, params, RecruitDetailResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                RecruitDetailResponse rdi = (RecruitDetailResponse) br;
                if (rdi.getData() == null) {
                    return;
                }
                String houseImg = rdi.getData().getPositionInfo().getCompanyMien();
                List<String> bannerData = new ArrayList<String>();
                if (!TextUtils.isEmpty(houseImg) && houseImg.contains(",")) {
                    bannerData.add(houseImg.split(",")[0]);
                } else {
                    bannerData.add(houseImg);
                }
                hiringCount = rdi.getData().getPositionInfo().getHiringCount();
                enrollNum = rdi.getData().getPositionInfo().getEnrollNum();
                companyId = rdi.getData().getPositionInfo().getCompanyId();
                bannerView.setList(bannerData);
                positionName = rdi.getData().getPositionInfo().getPositionName();
                positionNameTv.setText(rdi.getData().getPositionInfo().getPositionName());
                positionPriceTv.setText(rdi.getData().getPositionInfo().getFocusSalary() + "元");
                initWelfare(rdi.getData().getPositionInfo().getWelfare());
                addrTv.setText(rdi.getData().getPositionInfo().getRegion());
                String updateDate = rdi.getData().getPositionInfo().getCreateDate();
                if (updateDate.contains(" ")) {
                    updateDate = updateDate.split(" ")[0];
                }
                endtimeTv.setText("更新时间：" + updateDate);
                String modeCash = "";
                switch (rdi.getData().getPositionInfo().getSettlement()) {//结算方式（0工时1一次性2月结）
                    case 0:
                        modeCash = rdi.getData().getPositionInfo().getCommision() + "元/小时";
                        break;
                    case 1:
                        modeCash = rdi.getData().getPositionInfo().getCommision() + "元";
                        break;
                    case 2:
                        modeCash = rdi.getData().getPositionInfo().getCommision() + "元/月";
                        break;
                }
                if (!TextUtils.isEmpty(rdi.getData().getPositionInfo().getCommisionDetails())) {
                    modeCash += "<br><font color=#9b9b9b>佣金说明：" + rdi.getData().getPositionInfo().getCommisionDetails() + "</font>";
                }
                commissionTv.setText(Html.fromHtml(modeCash));
                baseSalaryEdit.setText(rdi.getData().getPositionInfo().getSupplement());
                baseDiningEdit.setText(rdi.getData().getPositionInfo().getStaffCanteen());
                baseDemandEdit.setText(rdi.getData().getPositionInfo().getJobDemand());
                ltdNameTv.setText(rdi.getData().getCompanyName());
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    private void initTabIndicator() {
        //初始化数据
        final List<String> titles = new ArrayList<>();
        titles.add("薪资待遇");
        titles.add("食宿情况");
        titles.add("入职条件");
        titles.add("公司简介");
        tabIndicator.setTitles(titles);
        //上面是有ViewPager的情况。如果不想与Viewpager绑定则可以调用：
        tabIndicator.setTabSelectedListener(new MyTabIndicator.TabSelectedListener() {
            @Override
            public void tabClicked(int position) {
                switch (position) {
                    case 0:
                        mSl.scrollToView(mTv2);
                        break;
                    case 1:
                        mSl.scrollToView(mTv3);
                        break;
                    case 2:
                        mSl.scrollToView(mTv4);
                        break;
                    case 3:
                        mSl.scrollToView(mTv5);
                        break;
                }
            }
        });
//        tabIndicator.setClicked(0);
    }

    private void initWelfare(String welfare) {
        if (TextUtils.isEmpty(welfare)) {
            return;
        }
        CustumViewGroup custumViewGroup = new CustumViewGroup(this);
        if (welfare.contains(",")) {
            String[] welfares = welfare.split(",");
            for (int i = 0; i < welfares.length; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.tag_layout, null);
                TextView tag = (TextView) view.findViewById(R.id.tag_layout_text);
                tag.setText(welfares[i]);
                custumViewGroup.addView(view);
            }
        } else {
            View view = LayoutInflater.from(this).inflate(R.layout.tag_layout, null);
            TextView tag = (TextView) view.findViewById(R.id.tag_layout_text);
            tag.setText(welfare);
            custumViewGroup.addView(view);
        }
        welfLayout.removeAllViews();
        welfLayout.addView(custumViewGroup);
    }

    private void initViews() {
        bannerLayout = (LinearLayout) findViewById(R.id.bannerlayout);
        bannerView = new BannerView(this);
        bannerLayout.addView(bannerView);
    }

    public void scrollTo2(View view) {
        //滑动到指定子控件
        mSl.scrollToView(mTv2);
    }

    public void scrollTo3(View view) {
        mSl.scrollToView(mTv3);
    }

    public void scrollTo4(View view) {
        mSl.scrollToView(mTv4);
    }

    public void scrollTo6(View view) {
        mSl.scrollToView(mTv5);
    }

    @OnClick({R.id.recruit_detail_self_signup, R.id.recruit_detail_other_signup, R.id.tv5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recruit_detail_self_signup:
                if (UserData.getUserData().getValidateStatus() != 1) {//validateStatus 0未认证1已通过2认证失败3认证中
                    GoCertifyDialog dialog = new GoCertifyDialog(this, new DialogCallBack() {
                        @Override
                        public void OkDown(Object obj) {
                            Intent realname = new Intent();
                            realname.setClass(RecruitDetailActivity.this, RealNameCertifyActivity.class);
                            realname.putExtra("status", UserData.getUserData().getValidateStatus());
                            startActivity(realname);
                        }

                        @Override
                        public void CancleDown() {

                        }
                    });
                    dialog.show();
                    return;
                }
                if (UserData.getUserData().getIsCompany() == 1) {
                    ToastUtil.showText("企业用户不能报名");
                    return;
                }
                String info = "您是否已了解该职位的<font color=red>入职条件</font>，<br>点击<font color=#3b9cff>[确定]</font>将报名该职位，否则请<font color=#3b9cff>[取消]</font>";
                GoCertifyDialog signupDialog = new GoCertifyDialog(this, "我要报名", info, "取消", true, "确认报名", true, new DialogCallBack() {
                    @Override
                    public void OkDown(Object score) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("positionId", positionId + "");//职位ID
                        params.put("positionName", positionName);//职位
                        params.put("code", "2");//1经纪人报名2自主报名
                        params.put("userIds", UserData.getUserData().getId() + "");//用户ID逗号分隔
                        OkHttpUtils.getAsyn(NetWorkConfig.USER_SIGNUP_EVENT, params, BaseResponse.class, new HttpCallback() {
                            @Override
                            public void onSuccess(BaseResponse br) {
                                super.onSuccess(br);
                                try {
                                    if (br.getCode() == 1) {
                                        SignupSuccessDialog signupSucDialog = new SignupSuccessDialog(RecruitDetailActivity.this, new DialogCallBack() {
                                            @Override
                                            public void OkDown(Object score) {

                                            }

                                            @Override
                                            public void CancleDown() {

                                            }
                                        });
                                        signupSucDialog.show();
                                    } else if (br.getCode() == 100) {
                                        String info2 = "您已报名过该职位。<br>当前状态：<font color=red>[" + br.getMessage() + "]</font>";
                                        GoCertifyDialog signupFailDialog = new GoCertifyDialog(RecruitDetailActivity.this, "我要报名", info2, "", false, "关闭", true, new DialogCallBack() {

                                            @Override
                                            public void OkDown(Object obj) {

                                            }

                                            @Override
                                            public void CancleDown() {

                                            }
                                        });
                                        signupFailDialog.show();
                                    } else {
                                        ToastUtil.showText(br.getMessage());
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

                    @Override
                    public void CancleDown() {

                    }
                });
                signupDialog.show();
                break;
            case R.id.recruit_detail_other_signup:
                if (UserData.getUserData().getValidateStatus() != 1) {//validateStatus 0未认证1已通过2认证失败3认证中
                    ToastUtil.showText("未实名认证用户不能报名");
                    return;
                }
                if (UserData.getUserData().getIsCompany() == 1) {
                    ToastUtil.showText("企业用户不能报名");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(RecruitDetailActivity.this, BrokerSignupActivity.class);
                intent.putExtra("positionId", positionId);
                intent.putExtra("positionName", positionName);
                intent.putExtra("hiringCount", hiringCount);
                intent.putExtra("enrollNum", enrollNum);
                startActivity(intent);
                break;

            case R.id.tv5:
                if (companyId > 0) {
                    Intent detail = new Intent();
                    detail.setClass(RecruitDetailActivity.this, EnterpriseDetailActivity.class);
                    detail.putExtra("companyId", companyId);
                    startActivity(detail);
                }
                break;
        }
    }
}
