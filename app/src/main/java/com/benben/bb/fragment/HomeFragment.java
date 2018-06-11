package com.benben.bb.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.bb.MyApplication;
import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.activity.BrokerActivity;
import com.benben.bb.activity.EnterpriseCertifyActivity;
import com.benben.bb.activity.MainFragmentActivity;
import com.benben.bb.activity.MyEnterpriseActivity;
import com.benben.bb.activity.RealNameCertifyActivity;
import com.benben.bb.activity.RecruitDetailActivity;
import com.benben.bb.activity.SearchActivity;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.BecomeBrokerDialog;
import com.benben.bb.dialog.BecomeBrokerRunningDialog;
import com.benben.bb.dialog.RealnameCertifyDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.HomeBannerResponse;
import com.benben.bb.okhttp3.response.SearchResultResponse;
import com.benben.bb.utils.DateUtils;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.BannerView;
import com.benben.bb.view.CustumViewGroup;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;


/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.search_bar_location)
    TextView locationTv;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 4;

    private static final String ARG = "arg";
    private BannerView bannerView;
    private LinearLayout bannerLayout;
    private LinearLayout brokerLayout, enterpriseLayout;

    public HomeFragment() {
        LogUtil.d("HomeFragment non-parameter constructor");
    }

    public static HomeFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provieResourceID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.search_bar_layout2).setOnClickListener(this);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.home_hot_employ_head, null, false);
        bannerLayout = (LinearLayout) header.findViewById(R.id.bannerlayout);
        brokerLayout = (LinearLayout) header.findViewById(R.id.home_broker_layout);
        enterpriseLayout = (LinearLayout) header.findViewById(R.id.home_enterprise_layout);
        header.findViewById(R.id.home_employ_more).setOnClickListener(this);
        brokerLayout.setOnClickListener(this);
        enterpriseLayout.setOnClickListener(this);
        recyclerSwipeLayout.addHeaderView(header);
//        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        initBanner();
        requestEmployList();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(MyApplication.ADDRESS_CITY_ACTION);
        getActivity().registerReceiver(cityReceiver, mFilter);
    }

    private void initBanner() {
        bannerView = new BannerView(getActivity());
        bannerLayout.addView(bannerView);
        requestBanner();
    }

    private void requestBanner() {
        OkHttpUtils.getAsyn(NetWorkConfig.HOME_BANNER, HomeBannerResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                try {
                    HomeBannerResponse hbr = (HomeBannerResponse) br;
                    if (hbr.getCode() == 1) {
                        List<HomeBannerResponse.BannerInfo> temp = hbr.getData();
                        if (temp == null || temp.isEmpty()) {
                            return;
                        }
                        List<String> bannerData = new ArrayList<String>();
                        for (HomeBannerResponse.BannerInfo banner : temp) {
                            bannerData.add(banner.getPic());
                        }
                        bannerView.setList(bannerData);
//                        bannerView.setOnBannerItemClickListener(new BannerView.OnBannerItemClickListener() {
//                            @Override
//                            public void onClick(int position) {
//                                try {
//                                    HomeBannerResponse.BannerInfo bi = temp.get(position);
//                                    String href = bi.getHref();
//                                    Intent detail = new Intent();
//                                    detail.setClass(getActivity(), RecruitDetailActivity.class);
//                                    detail.putExtra("positionId", href.split("=")[1]);
//                                    detail.putExtra("positionName", ri.getPositionName());
//                                    startActivity(detail);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
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

//    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {
//
//        @Override
//        public void onLoadMoreRequested() {
//            if (pageNo < totalPage) {
//                pageNo++;
//                requestEmployList();
//            }
//            recyclerSwipeLayout.loadComplete();
//        }
//    };

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final SearchResultResponse.RecruitInfo ri = (SearchResultResponse.RecruitInfo) itemModel;
            baseViewHolder.setText(R.id.home_employ_name, ri.getPositionName());
            baseViewHolder.setText(R.id.home_employ_addr, "工作地点：" + ri.getRegion());
//            baseViewHolder.setText(R.id.home_employ_welfare, ri.getWelfare().replace(",", "|"));
            baseViewHolder.setText(R.id.home_employ_price, ri.getSalary() + "元/小时");
            baseViewHolder.setText(R.id.home_employ_count, Html.fromHtml("报名：<font color=#FD7979>" + ri.getEnrollNum() + "/" + ri.getHiringCount() + "</font>(还剩<font color=#FD7979>" + DateUtils.dateDiffToday(ri.getEndTime()) + "</font>天)"));
            if (UserData.getUserData().getIsAgent() > 0 && UserData.getUserData().getIsAgent() < 88) {
                baseViewHolder.setText(R.id.home_employ_broker_price, ri.getCommision() + "元/小时");
                baseViewHolder.setVisible(R.id.home_employ_broker_layout, true);
            } else {
                baseViewHolder.setVisible(R.id.home_employ_broker_layout, false);
            }
            String img = ri.getHouseImg();
            if (img.contains(",")) {
                img = img.split(",")[0];
            }
            Glide.with(getActivity())
                    .load(img)
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into((ImageView) baseViewHolder.getView(R.id.home_employ_logo));
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent();
                    detail.setClass(getActivity(), RecruitDetailActivity.class);
                    detail.putExtra("positionId", ri.getId());
                    detail.putExtra("positionName", ri.getPositionName());
                    startActivity(detail);
                }
            });
            String welfare = ri.getWelfare();
            if (TextUtils.isEmpty(welfare)) {
                return;
            }
            LinearLayout frameLayout = (LinearLayout) baseViewHolder.getView(R.id.home_employ_welfare_layout);
            CustumViewGroup custumViewGroup = new CustumViewGroup(getActivity());
            custumViewGroup.removeAllViews();
            if (welfare.contains(",")) {
                String[] welfares = welfare.split(",");
                for (int i = 0; i < welfares.length; i++) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.tag_layout, null);
                    TextView tag = (TextView) view.findViewById(R.id.tag_layout_text);
                    tag.setText(welfares[i]);
                    custumViewGroup.addView(view);
                }
            } else {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.tag_layout, null);
                TextView tag = (TextView) view.findViewById(R.id.tag_layout_text);
                tag.setText(welfare);
                custumViewGroup.addView(view);
            }
            frameLayout.removeAllViews();
            frameLayout.addView(custumViewGroup);
        }
    };

    @Override
    public void onClick(View v) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.home_employ_more:
                ((MainFragmentActivity) getActivity()).changeFragment(1);
                break;
            case R.id.home_broker_layout:
                switch (UserData.getUserData().getValidateStatus()) {//validateStatus 0未认证1已通过2认证失败3认证中
                    case 2:
                        ToastUtil.showText("上次提交的实名认证申核失败，需要重新认证");
                    case 0:
                        RealnameCertifyDialog realnameCertifyDialog = new RealnameCertifyDialog(getActivity(), "认证为就业顾问需先完成实名认证", new DialogCallBack() {
                            @Override
                            public void OkDown(Object obj) {
                                Intent realname = new Intent();
                                realname.setClass(getActivity(), RealNameCertifyActivity.class);
                                realname.putExtra("status", UserData.getUserData().getValidateStatus());
                                startActivityForResult(realname, 11);
                            }

                            @Override
                            public void CancleDown() {

                            }
                        });
                        realnameCertifyDialog.show();
                        return;
                    case 3:
                        BecomeBrokerRunningDialog dialog2 = new BecomeBrokerRunningDialog(getActivity(), "实名认证正在审核中", new DialogCallBack() {
                            @Override
                            public void OkDown(Object obj) {
                            }

                            @Override
                            public void CancleDown() {

                            }
                        });
                        dialog2.show();
                        return;
                }
                int company2 = UserData.getUserData().getIsCompany();
                if (company2 > 0 && company2 < 88) {
                    ToastUtil.showText("您已认证为企业人将不能认证就业顾问");
                    return;
                }
                final int agent = UserData.getUserData().getIsAgent();
                switch (agent) {
                    case 88://申请失败
                        ToastUtil.showText("上次提交的就业顾问认证申核失败，是否重新认证");
                    case 0://未认证
                        BecomeBrokerDialog dialog = new BecomeBrokerDialog(getActivity(), new DialogCallBack() {
                            @Override
                            public void OkDown(Object obj) {
                                OkHttpUtils.getAsyn(NetWorkConfig.BROKER_CHECK_AGENTS, BaseResponse.class, new HttpCallback() {
                                    @Override
                                    public void onSuccess(BaseResponse br) {
                                        super.onSuccess(br);
                                        if (br.getCode() == 1) {
                                            ToastUtil.showText("申请就业顾问请求已提交，申核需要1-2个工作日");
                                            Utils.updateUserInfo();
                                        } else {
                                            ToastUtil.showText(br.getMessage());
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
                        dialog.show();
                        break;
                    case 99://申请中
                        BecomeBrokerRunningDialog dialog2 = new BecomeBrokerRunningDialog(getActivity(), new DialogCallBack() {
                            @Override
                            public void OkDown(Object obj) {
                            }

                            @Override
                            public void CancleDown() {

                            }
                        });
                        dialog2.show();
                        break;
                    default://大于0 小于88 都是就业顾问
                        if (agent > 0 && agent < 88) {
                            Intent broker = new Intent();
                            broker.setClass(getActivity(), BrokerActivity.class);
                            startActivity(broker);
                        }
                        break;

                }
                break;

            case R.id.home_enterprise_layout:
                switch (UserData.getUserData().getValidateStatus()) {//validateStatus 0未认证1已通过2认证失败3认证中
                    case 2:
                        ToastUtil.showText("上次提交的实名认证申核失败，需要重新认证");
                    case 0:
                        RealnameCertifyDialog realnameCertifyDialog = new RealnameCertifyDialog(getActivity(), "认证为企业人需先完成实名认证", new DialogCallBack() {
                            @Override
                            public void OkDown(Object obj) {
                                Intent realname = new Intent();
                                realname.setClass(getActivity(), RealNameCertifyActivity.class);
                                realname.putExtra("status", UserData.getUserData().getValidateStatus());
                                startActivityForResult(realname, 11);
                            }

                            @Override
                            public void CancleDown() {

                            }
                        });
                        realnameCertifyDialog.show();
                        return;
                    case 3:
                        BecomeBrokerRunningDialog dialog2 = new BecomeBrokerRunningDialog(getActivity(), "实名认证正在审核中", new DialogCallBack() {
                            @Override
                            public void OkDown(Object obj) {

                            }

                            @Override
                            public void CancleDown() {

                            }
                        });
                        dialog2.show();
                        return;
                }
                int agent2 = UserData.getUserData().getIsAgent();
                if (agent2 > 0 && agent2 < 88) {
                    ToastUtil.showText("您已经认证为就业顾问将不能认证成为企业");
                    return;
                }
                final int company = UserData.getUserData().getIsCompany();
                switch (company) {
                    case 88://申请失败
                        ToastUtil.showText("上次提交的企业人认证申核失败，是否重新认证");
                    case 0://未认证
                        BecomeBrokerDialog dialog = new BecomeBrokerDialog(getActivity(), "是否认证为企业人", new DialogCallBack() {
                            @Override
                            public void OkDown(Object obj) {
                                Intent enterprise = new Intent();
                                enterprise.setClass(getActivity(), EnterpriseCertifyActivity.class);
                                startActivityForResult(enterprise, 11);
                            }

                            @Override
                            public void CancleDown() {

                            }
                        });
                        dialog.show();
                        break;
                    case 99://申请中
                        BecomeBrokerRunningDialog dialog2 = new BecomeBrokerRunningDialog(getActivity(), new DialogCallBack() {
                            @Override
                            public void OkDown(Object obj) {
                            }

                            @Override
                            public void CancleDown() {

                            }
                        });
                        dialog2.show();
                        break;
                    default://大于0 小于88 都是就业顾问
                        if (company > 0 && company < 88) {
                            Intent enterprise = new Intent();
                            enterprise.setClass(getActivity(), MyEnterpriseActivity.class);
                            enterprise.putExtra("status", company);
                            startActivity(enterprise);
                        }
                        break;
                }
                break;

            case R.id.search_bar_layout2:
                Intent search = new Intent();
                search.setClass(getActivity(), SearchActivity.class);
                startActivity(search);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            Utils.updateUserInfo();
        }
    }

    private void requestEmployList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("positionName", "");
        params.put("sort", "0");//0升序1降序默认0
        OkHttpUtils.getAsyn(NetWorkConfig.INDEX_RECRUIT_SEARCH, params, SearchResultResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    SearchResultResponse crr = (SearchResultResponse) resultDesc;
                    if (crr.getCode() == 1) {
                        pageNo = crr.getData().getPageNum();
                        totalPage = crr.getData().getPages();
                        List<SearchResultResponse.RecruitInfo> temp = crr.getData().getList();
                        if (temp == null || temp.isEmpty()) {
                            if (totalPage == 0) {
                                recyclerSwipeLayout.setEmpty();
                            }
                            return;
                        }
                        recyclerSwipeLayout.addData(temp);
                        recyclerSwipeLayout.loadComplete();
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

    /**
     * 导航位置更新器
     */
    private BroadcastReceiver cityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("city")) {
                locationTv.setText(intent.getStringExtra("city"));
            }
        }
    };
}