package com.benben.bb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.activity.RecruitDetailActivity;
import com.benben.bb.activity.SearchActivity;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.bean.UserData;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.SearchResultResponse;
import com.benben.bb.utils.DateUtils;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.SpinerPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class EmployFragment extends BaseFragment {
    private static final String ARG = "arg";
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.home_employ_updown)
    TextView updownSp;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private List<String> barUpdown;
    /**
     * 保存升降序，用于请求下一页
     */
    private int oldSort = 0;
    private SpinerPopWindow<String> mSpinerPopWindow;

    public EmployFragment() {
        LogUtil.d("EmployFragment non-parameter constructor");
    }

    public static EmployFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        EmployFragment fragment = new EmployFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        String[] data = getResources().getStringArray(R.array.search_type_updown);
        barUpdown = Arrays.asList(data);
        mSpinerPopWindow = new SpinerPopWindow<String>(getActivity(), barUpdown, itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
        if (savedInstanceState != null) {
            oldSort = savedInstanceState.getInt("oldSort", 0);
        }
        freshUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("oldSort", oldSort);
    }

    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            //setTextImage(R.drawable.icon_down);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            updownSp.setText(barUpdown.get(position));
            if (oldSort == position) {
                return;
            }
            reset();
            oldSort = position;
            requestEmployList(position);
        }
    };

    private void reset() {
        pageNo = 1;
        totalPage = -1;
        recyclerSwipeLayout.setNewData(new ArrayList<SearchResultResponse.RecruitInfo>());
    }

    private void requestEmployList(int sort) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("positionName", "");
        params.put("sort", sort + "");//0升序1降序默认0
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

    @Override
    protected int provieResourceID() {
        return R.layout.fragment_employ;
    }

    @Override
    protected void init() {
    }

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
                requestEmployList(oldSort);
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final SearchResultResponse.RecruitInfo ri = (SearchResultResponse.RecruitInfo) itemModel;
            baseViewHolder.setText(R.id.employ_job_name, ri.getPositionName());
            baseViewHolder.setText(R.id.employ_job_addr, "工作地点：" + ri.getRegion());
            baseViewHolder.setText(R.id.employ_job_welfare, ri.getWelfare().replace(",", "|"));


            if (UserData.getUserData().getIsAgent() > 0 && UserData.getUserData().getIsAgent() < 88) {
                baseViewHolder.setVisible(R.id.employ_job_broker_layout, true);
                baseViewHolder.setText(R.id.employ_job_broker_price, ri.getCommision() + "元/小时");
                baseViewHolder.setText(R.id.employ_job_price, ri.getSalary() + "元/小时");
            } else if (UserData.getUserData().getIsCompany() > 0 && UserData.getUserData().getIsCompany() < 88) {
                baseViewHolder.setVisible(R.id.employ_job_broker_layout, false);
                baseViewHolder.setText(R.id.employ_job_price, ri.getSalary() + "元/小时");
            } else {//普通用户
                baseViewHolder.setVisible(R.id.employ_job_broker_layout, false);
                baseViewHolder.setText(R.id.employ_job_price, ri.getFocusSalary() + "元/小时");
            }
            baseViewHolder.setText(R.id.employ_job_count, Html.fromHtml("报名：<font color=#FD7979>" + ri.getEnrollNum() + "/" + ri.getHiringCount() + "</font>(还剩<font color=#FD7979>" + DateUtils.dateDiffToday(ri.getEndTime()) + "</font>天)"));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.home_employ_qzone, R.id.home_employ_category, R.id.home_employ_updown, R.id.search_bar_layout})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.search_bar_layout:
                Intent search = new Intent();
                search.setClass(getActivity(), SearchActivity.class);
                startActivity(search);
                break;
            case R.id.home_employ_updown:
                mSpinerPopWindow.setWidth(updownSp.getWidth());
                mSpinerPopWindow.showAsDropDown(updownSp);
                break;
            case R.id.home_employ_qzone:
                ToastUtil.showText("暂不支持地区选择");
                break;
            case R.id.home_employ_category:
                ToastUtil.showText("暂不支持行业分类");
                break;
        }
    }

    public void freshUI() {
        requestEmployList(oldSort);
    }
}