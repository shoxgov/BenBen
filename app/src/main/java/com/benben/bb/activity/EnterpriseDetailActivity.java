package com.benben.bb.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.RecruitAdapter;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyInfoResponse;
import com.benben.bb.stickLayout.StickLayout;
import com.benben.bb.utils.ListViewUtils;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.MyTabIndicator;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-14.
 * email:shoxgov@126.com
 */

public class EnterpriseDetailActivity extends BaseActivity {
    private static final int REQUEST_CODE_IMG = 15;
    @Bind(R.id.enterprise_info_edit_ground)
    FrameLayout backgroundFragment;
    @Bind(R.id.enterprise_info_edit_name)
    TextView infoName;
    @Bind(R.id.enterprise_info_edit_addr)
    TextView addrNameTv;
    @Bind(R.id.enterprise_detail_remark_rating_salry)
    RatingBar remarkRatingSalry;
    @Bind(R.id.enterprise_detail_remark_rating_welfare)
    RatingBar remarkRatingWelfare;
    @Bind(R.id.enterprise_detail_remark_rating_doorm)
    RatingBar remarkRatingDoorm;
    @Bind(R.id.enterprise_detail_critic_rating_salry)
    TextView criticRatingSalry;
    @Bind(R.id.enterprise_detail_critic_rating_welfare)
    RatingBar criticRatingWelfare;
    @Bind(R.id.enterprise_detail_critic_rating_doorm)
    RatingBar criticRatingDoorm;
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
    @Bind(R.id.company_detail_introduce_edit)
    EditText introduceEdit;
    @Bind(R.id.company_detail_employee_list)
    ListView employeeList;
    @Bind(R.id.company_detail_combination_list)
    ListView combinationList;
    @Bind(R.id.enterprise_detail_certify_tag)
    ImageView certifyTag;
    private String companyPic = "https://img14.360buyimg.com/n0/jfs/t22849/110/1423883954/360356/ec4d867f/5b600398N57b5675a.jpg";
    private String companyRegion;
    /**
     * 保存选择的
     */
    private int companyId;
    private int currentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_detail);
        ButterKnife.bind(this);
        companyId = getIntent().getIntExtra("companyId", 0);
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
        initTabIndicator();
        getCompanyInfo();
    }

    private void initTabIndicator() {
        //初始化数据
        final List<String> titles = new ArrayList<>();
        titles.add("公司简介");
        titles.add("企业直聘");
        titles.add("合作招聘");
//        titles.add("企业评分");
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
//                            tabIndicator.selectItem(3);
                            break;
                    }
                }
            }
        });
    }

    private void getCompanyInfo() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("companyId", companyId + "");//ID
        OkHttpUtils.getAsyn(NetWorkConfig.GET_COMPANY_INFO, params, CompanyInfoResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                super.onSuccess(baseResponse);
                CompanyInfoResponse cir = (CompanyInfoResponse) baseResponse;
                if (cir.getCode() == 1) {
                    companyId = cir.getData().getCompanyInfo().getId();
                    companyRegion = cir.getData().getCompanyInfo().getCompanyRegion();
                    String photo = cir.getData().getCompanyInfo().getCompanyMien();
                    if (!TextUtils.isEmpty(photo)) {
                        if(photo.contains(",")){
                            photo = photo.split(",")[0];
                        }
                        try {
                            Glide.with(EnterpriseDetailActivity.this)
                                    .load(photo)
                                    .placeholder(R.mipmap.default_image).dontAnimate()
                                    .into(new ViewTarget<View, GlideDrawable>(backgroundFragment) {
                                        //括号里为需要加载的控件
                                        @Override
                                        public void onResourceReady(GlideDrawable resource,
                                                                    GlideAnimation<? super GlideDrawable> glideAnimation) {
                                            this.view.setBackground(resource.getCurrent());
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                    infoName.setText(cir.getData().getCompanyInfo().getCompanyName());
                    if (!TextUtils.isEmpty(companyRegion)) {
                        addrNameTv.setText(companyRegion.replace(".", ""));
                    }
                    introduceEdit.setText(cir.getData().getCompanyInfo().getIntroduction());
                    if (cir.getData().getCompanyInfo().getAuditStatus() == 1) {
                        certifyTag.setVisibility(View.VISIBLE);
                    } else {
                        certifyTag.setVisibility(View.GONE);
                    }
                    RecruitAdapter adapter1 = new RecruitAdapter(EnterpriseDetailActivity.this, cir.getData().getCompanyInfo().getCompanyName());
                    employeeList.setAdapter(adapter1);
                    adapter1.setData(cir.getData().getDefaultList());
                    RecruitAdapter adapter2 = new RecruitAdapter(EnterpriseDetailActivity.this, cir.getData().getCompanyInfo().getCompanyName());
                    combinationList.setAdapter(adapter2);
                    adapter2.setData(cir.getData().getOtherList());
                    ListViewUtils.setListViewHeightBasedOnChildren(employeeList);
                    ListViewUtils.setListViewHeightBasedOnChildren(combinationList);
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    public void onViewClicked(View v) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.enterprise_info_submit:
                Map<String, String> params = new HashMap<String, String>();
//                params.put("companyNature", selectNature.getId() + "");
//                params.put("companySize", selectScale.getId() + "");
                params.put("companyRegion", companyRegion);
                params.put("id", companyId + "");
                String filekey = "file";
                List<File> files = new ArrayList<>();
                files.add(new File(companyPic));
                OkHttpUtils.postAsynFiles(NetWorkConfig.GET_COMPANY_INFO_SAVE, filekey, files, params, BaseResponse.class, new HttpCallback() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        ToastUtil.showText(baseResponse.getMessage());
                        if (baseResponse.getCode() == 1) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });
                break;
        }
    }

    @OnClick(R.id.enterprise_detail_critic_btn)
    public void onViewClicked() {
    }
}
