package com.benben.bb.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.benben.bb.MyApplication;
import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.activity.EmployActivity;
import com.benben.bb.activity.MainFragmentActivity;
import com.benben.bb.activity.MyWalletActivity;
import com.benben.bb.activity.PersonQrActivity;
import com.benben.bb.activity.RecruitDetailActivity;
import com.benben.bb.activity.SearchActivity;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.HomeBannerResponse;
import com.benben.bb.okhttp3.response.SearchResultResponse;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.BannerView;
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
    @Bind(R.id.home_search_main_layout)
    LinearLayout llMain;
    @Bind(R.id.home_search_child_layout)
    LinearLayout llChild;
//    @Bind(R.id.search_bar_location)
//    TextView locationTv;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    //状态栏的高度
    private int statusHeight;
    private static final String ARG = "arg";
    private BannerView bannerView;
    private LinearLayout bannerLayout;
    private List<HomeBannerResponse.BannerInfo> bannerList;
    private GradientDrawable mainDrawable;
    private GradientDrawable childDrawable;

    public HomeFragment() {
        LogUtil.d("HomeFragment non-parameter constructor");
    }

    public static HomeFragment newInstance(String arg) {
        LogUtil.d("HomeFragment newInstance");
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
        mainDrawable = new GradientDrawable();
        mainDrawable.setColor(Color.parseColor("#003b9cff"));
        llMain.setBackground(mainDrawable);
        childDrawable = new GradientDrawable();
        childDrawable.setColor(Color.parseColor("#00FFFFFF"));
//        childDrawable.setStroke(Utils.dip2px(getActivity(), 1), Color.parseColor("#FFFFFF"));
        childDrawable.setCornerRadius(Utils.dip2px(getActivity(), 5));
        llChild.setBackground(childDrawable);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtil.d("HomeFragment onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        statusHeight = Utils.getStatusHeight(getActivity());
        getView().findViewById(R.id.home_search_child_layout).setOnClickListener(this);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.home_hot_employ_head, null, false);
//        header.findViewById(R.id.search_bar_layout2).setOnClickListener(this);
        bannerLayout = (LinearLayout) header.findViewById(R.id.bannerlayout);
        header.findViewById(R.id.home_employ_change).setOnClickListener(this);
        header.findViewById(R.id.home_rights_layout).setOnClickListener(this);
        header.findViewById(R.id.home_employ_layout).setOnClickListener(this);
        header.findViewById(R.id.home_share_layout).setOnClickListener(this);
        header.findViewById(R.id.home_wallet_layout).setOnClickListener(this);
//        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        recyclerSwipeLayout.addHeaderView(header);
        bannerView = new BannerView(getActivity());
        bannerLayout.addView(bannerView);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(MyApplication.ADDRESS_CITY_ACTION);
        getActivity().registerReceiver(cityReceiver, mFilter);
        freshUI();
        initBanner();
        recyclerSwipeLayout.addOnScrollListener(onScrollListener);
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        //监听滑动状态的改变
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        @Override
        //用于监听ListView屏幕滚动
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int[] ints = new int[2];
            bannerLayout.getLocationOnScreen(ints);
            /**
             * mImage距离屏幕顶部的距离(图片顶部在屏幕最上面，向上滑动为负数，所以取反)
             * 如果不隐藏状态栏，需要加上状态栏的高度；隐藏状态栏就不用加了；
             */
            if (ints[1] == 0) {
                return;
            }
            int scrollY = -ints[1] + statusHeight;
            //mImage这个view的高度
            int imageHeight = bannerLayout.getHeight();
            if (bannerLayout != null && imageHeight > 0) {
                //如果“图片”没有向上滑动，设置为全透明
                if (scrollY < 0) {
                    mainDrawable.setColor(Color.parseColor("#003b9cff"));
                    llMain.setBackground(mainDrawable);
                    childDrawable.setColor(Color.parseColor("#00FFFFFF"));
                    llChild.setBackground(childDrawable);
                } else {
                    //“图片”已经滑动，而且还没有全部滑出屏幕，根据滑出高度的比例设置透明度的比例
                    if (scrollY < imageHeight) {
                        int progress = (int) (new Float(scrollY) / new Float(imageHeight) * 255);//255
                        mainDrawable.setColor(Color.parseColor("#" + decimalToHex(progress) + "3b9cff"));
                        llMain.setBackground(mainDrawable);
                        childDrawable.setColor(Color.parseColor("#" + decimalToHex(progress) + "FFFFFF"));
                        llChild.setBackground(childDrawable);
                    } else {
                        //“图片”全部滑出屏幕的时候，设为完全不透明
                        mainDrawable.setColor(Color.parseColor("#ff3b9cff"));
                        llMain.setBackground(mainDrawable);
                        childDrawable.setColor(Color.parseColor("#FFFFFFFF"));
                        llChild.setBackground(childDrawable);
                    }
                }
            }
        }
    };

    private String decimalToHex(int decimal) {
        String hex = "";
        if (decimal == 0) {
            return "00";
        }
        while (decimal != 0) {
            int hexValue = decimal % 16;
            hex = toHexChar(hexValue) + hex;
            decimal = decimal / 16;
        }
        if (hex.length() == 1) {
            return "0" + hex;
        }
        return hex;
    }

    //将0~15的十进制数转换成0~F的十六进制数
    private char toHexChar(int hexValue) {
        if (hexValue <= 9 && hexValue >= 0)
            return (char) (hexValue + '0');
        else
            return (char) (hexValue - 10 + 'A');
    }

    private void initBanner() {
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
                        bannerList = hbr.getData();
                        if (bannerList == null || bannerList.isEmpty()) {
                            return;
                        }
                        List<String> bannerData = new ArrayList<String>();
                        for (HomeBannerResponse.BannerInfo banner : bannerList) {
                            bannerData.add(banner.getPic());
                        }
                        bannerView.setList(bannerData);
                        bannerView.setOnBannerItemClickListener(new BannerView.OnBannerItemClickListener() {
                            @Override
                            public void onClick(int position) {
                                if (Utils.isFastDoubleClick()) {
                                    return;
                                }
                                try {
                                    HomeBannerResponse.BannerInfo bi = bannerList.get(position);
                                    String href = bi.getHref();
                                    String positionName = bi.getPositionName();
                                    String positionId = href.split("=")[1];
                                    if (TextUtils.isEmpty(href) || TextUtils.isEmpty(positionId) /*|| TextUtils.isEmpty(positionName)*/) {
                                        return;
                                    }
                                    Intent detail = new Intent();
                                    detail.setClass(getActivity(), RecruitDetailActivity.class);
                                    detail.putExtra("positionId", positionId);
                                    detail.putExtra("positionName", positionName);
                                    startActivity(detail);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
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
            baseViewHolder.setText(R.id.home_employ_ltd, ri.getCompanyName());
            String region = ri.getRegion();
            if (region.contains(".")) {
                String[] regions = region.split("\\.");
                if (regions.length == 3) {
                    baseViewHolder.setText(R.id.home_employ_addr, regions[1] + "." + regions[2]);
                } else {
                    baseViewHolder.setText(R.id.home_employ_addr, regions[0] + "." + regions[1]);
                }
            } else {
                baseViewHolder.setText(R.id.home_employ_addr, ri.getRegion());
            }
            baseViewHolder.setText(R.id.home_employ_price, "¥" + ri.getFocusSalary());
            switch (ri.getAdvSort()) {//advSort (1限时招聘2优质企业3犇犇推荐4可实习生5小时兼职)
                case 1:
                    baseViewHolder.setText(R.id.home_employ_tag, "限时招聘");
                    baseViewHolder.setBackgroundColor(R.id.home_employ_tag, Color.parseColor("#fd7979"));
                    break;
                case 2:
                    baseViewHolder.setText(R.id.home_employ_tag, "优质企业");
                    baseViewHolder.setBackgroundColor(R.id.home_employ_tag, Color.parseColor("#94db46"));
                    break;
                case 3:
                default:
                    baseViewHolder.setText(R.id.home_employ_tag, "犇犇推荐");
                    baseViewHolder.setBackgroundColor(R.id.home_employ_tag, R.color.bluetheme);
                    break;
                case 4:
                    baseViewHolder.setText(R.id.home_employ_tag, "可实习生");
                    baseViewHolder.setBackgroundColor(R.id.home_employ_tag, Color.parseColor("#ffa23c"));
                    break;
                case 5:
                    baseViewHolder.setText(R.id.home_employ_tag, "小时兼职");
                    baseViewHolder.setBackgroundColor(R.id.home_employ_tag, Color.parseColor("#50e3c2"));
                    break;
            }
            String img = ri.getHouseImg();
            if (!TextUtils.isEmpty(img) && img.contains(",")) {
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
        }
    };

    @Override
    public void onClick(View v) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.home_employ_change:
                if (totalPage == 1) {
                    ToastUtil.showText("暂无更新");
                    return;
                }
                if (pageNo < totalPage) {
                    recyclerSwipeLayout.setNewData(new ArrayList<SearchResultResponse.RecruitInfo>());
                    pageNo++;
                    requestEmployList();
                } else {
                    pageNo = 1;
                    requestEmployList();
                }
//                ((MainFragmentActivity) getActivity()).changeFragment(1);
                break;

            case R.id.home_search_child_layout:
                Intent search = new Intent();
                search.setClass(getActivity(), SearchActivity.class);
                startActivity(search);
                break;
            case R.id.home_rights_layout:
                ((MainFragmentActivity) getActivity()).changeFragment(3);
                break;
            case R.id.home_employ_layout:
                Intent employ = new Intent();
                employ.setClass(getActivity(), EmployActivity.class);
                startActivity(employ);
                break;
            case R.id.home_wallet_layout:
                Intent wallet = new Intent();
                wallet.setClass(getActivity(), MyWalletActivity.class);
                startActivity(wallet);
                break;
            case R.id.home_share_layout:
                Intent qr = new Intent();
                qr.setClass(getActivity(), PersonQrActivity.class);
                startActivity(qr);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            Utils.updateUserInfomation();
        }
    }

    private void requestEmployList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        OkHttpUtils.getAsyn(NetWorkConfig.INDEX_HOME_ADV, params, SearchResultResponse.class, new HttpCallback() {
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
                                ToastUtil.showText("当前无数据");
//                                recyclerSwipeLayout.setEmpty();
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
//                locationTv.setText(intent.getStringExtra("city"));
            }
        }
    };

    public void freshUI() {
//        initBanner();
        requestEmployList();
    }
}