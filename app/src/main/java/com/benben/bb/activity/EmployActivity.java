package com.benben.bb.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.adapter.SearchGridViewAdapter;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.SearchResultResponse;
import com.benben.bb.okhttp3.widget.loading.LoadingView;
import com.benben.bb.utils.DialogUtil;
import com.benben.bb.utils.ListViewUtils;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-21.
 * email:shoxgov@126.com
 */

public class EmployActivity extends BaseActivity {
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    //    @Bind(R.id.search_key_edit)
//    EditText keyEdit;
    //    @Bind(R.id.search_bar_condition_layout)
//    LinearLayout conditionLayout;
//    @Bind(R.id.search_bar_model_grid)
//    GridView modelGrid;
//    @Bind(R.id.search_bar_wages_grid)
//    GridView wagesGrid;
    GridView modelGrid;
    GridView wagesGrid;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private int oldUpdown = 0;
    private SearchGridViewAdapter modelAdapter, wagesAdapter;
    /**
     * 保存升降序，用于请求下一页
     */
    private int oldAdvSort = 0;// (1限时招聘2优质企业3犇犇推荐4可实习生5小时兼职)
    private int oldSort = 0;

    public interface GridViewItemOnClick {
        void onItemClick(int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employ);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        recyclerSwipeLayout.createAdapter(R.layout.list_home_hot_employ);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        View headView = LayoutInflater.from(this).inflate(R.layout.employ_search_head, null, false);
        modelGrid = (GridView) headView.findViewById(R.id.search_bar_model_grid);
        wagesGrid = (GridView) headView.findViewById(R.id.search_bar_wages_grid);
        modelAdapter = new SearchGridViewAdapter(this, new MyOnItemClickListener(1));
        wagesAdapter = new SearchGridViewAdapter(this, new MyOnItemClickListener(2));
        modelGrid.setAdapter(modelAdapter);
        wagesGrid.setAdapter(wagesAdapter);
//        modelGrid.setOnItemClickListener(new MyOnItemClickListener(1));
//        wagesGrid.setOnItemClickListener(new MyOnItemClickListener(2));
        List<String> modelDate = new ArrayList<>();
        modelDate.add("全部");
        modelDate.add("限时招聘");
        modelDate.add("优质企业");
        modelDate.add("可实习");
        modelDate.add("小时兼职");
        modelDate.add("犇犇推荐");
        modelAdapter.setData(modelDate);
        List<String> wagesDate = new ArrayList<>();
        wagesDate.add("默认");
        wagesDate.add("由高到低");
        wagesDate.add("由低到高");
        wagesAdapter.setData(wagesDate);
//        ListViewUtils.calGridViewWidthAndHeigh(modelGrid, 3, 10);
//        ListViewUtils.calGridViewWidthAndHeigh(wagesGrid, 3, 10);
        ListViewUtils.setGridViewHeightBasedOnChildren(modelGrid);
        ListViewUtils.setGridViewHeightBasedOnChildren(wagesGrid);
        recyclerSwipeLayout.addHeaderView(headView);
//        String[] data = getResources().getStringArray(R.array.search_type_updown);
//        list = Arrays.asList(data);
//        mSpinerPopWindow = new SpinerPopWindow<String>(this, list, itemClickListener);
//        mSpinerPopWindow.setOnDismissListener(dismissListener);
        requestEmployList(0, 0);
    }

    public class MyOnItemClickListener implements GridViewItemOnClick {
        private int type = 0;

        public MyOnItemClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onItemClick(int position) {
            switch (type) {
                case 1:
                    modelAdapter.setSelectItem(position);
                    // (1限时招聘2优质企业3犇犇推荐4可实习生5小时兼职)
                    switch (position) {
                        case 0:
                            if (oldAdvSort == 0) {
                                return;
                            }
                            reset();
                            oldAdvSort = 0;
                            requestEmployList(oldAdvSort, oldSort);
                            break;
                        case 1:
                            if (oldAdvSort == 1) {
                                return;
                            }
                            reset();
                            oldAdvSort = 1;
                            requestEmployList(oldAdvSort, oldSort);
                            break;
                        case 2:
                            if (oldAdvSort == 2) {
                                return;
                            }
                            reset();
                            oldAdvSort = 2;
                            requestEmployList(oldAdvSort, oldSort);
                            break;
                        case 3:
                            if (oldAdvSort == 4) {
                                return;
                            }
                            reset();
                            oldAdvSort = 4;
                            requestEmployList(oldAdvSort, oldSort);
                            break;
                        case 4:
                            if (oldAdvSort == 5) {
                                return;
                            }
                            reset();
                            oldAdvSort = 5;
                            requestEmployList(oldAdvSort, oldSort);
                            break;
                        case 5:
                            if (oldAdvSort == 3) {
                                return;
                            }
                            reset();
                            oldAdvSort = 3;
                            requestEmployList(oldAdvSort, oldSort);
                            break;
                    }
                    break;
                case 2:
                    wagesAdapter.setSelectItem(position);
                    //0升序1降序默认0
                    switch (position) {
                        case 0:
                            if (oldSort == 0) {
                                return;
                            }
                            reset();
                            oldSort = 0;
                            requestEmployList(oldAdvSort, oldSort);
                            break;
                        case 1:
                            if (oldSort == 1) {
                                return;
                            }
                            reset();
                            oldSort = 1;
                            requestEmployList(oldAdvSort, oldSort);
                            break;
                        case 2:
                            if (oldSort == 0) {
                                return;
                            }
                            reset();
                            oldSort = 0;
                            requestEmployList(oldAdvSort, oldSort);
                            break;
                    }
                    break;
            }
        }
    }

    private void reset() {
        pageNo = 1;
        totalPage = -1;
        recyclerSwipeLayout.clear();
    }

    private void requestEmployList(int advSort, int sort) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("positionName", "");
//        params.put("region", "");
        if (advSort > 0) {
            params.put("advSort", "" + advSort);// (1限时招聘2优质企业3犇犇推荐4可实习生5小时兼职)
        }
        params.put("sort", sort + "");//0升序1降序默认0
        DialogUtil.showDialogLoading(this);
        OkHttpUtils.getAsyn(NetWorkConfig.INDEX_RECRUIT_SEARCH, params, SearchResultResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    DialogUtil.hideDialogLoading();
                    SearchResultResponse crr = (SearchResultResponse) resultDesc;
                    if (crr.getCode() == 1) {
                        pageNo = crr.getData().getPageNum();
                        totalPage = crr.getData().getPages();
                        List<SearchResultResponse.RecruitInfo> temp = crr.getData().getList();
                        if (temp == null || temp.isEmpty()) {
                            if (totalPage == 0) {
                                ToastUtil.showText("暂无数据");
                                recyclerSwipeLayout.clear();
//                                recyclerSwipeLayout.setEmpty();
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
                DialogUtil.hideDialogLoading();
                ToastUtil.showText(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
                requestEmployList(oldAdvSort, oldSort);
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };

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
            Glide.with(EmployActivity.this)
                    .load(img)
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into((ImageView) baseViewHolder.getView(R.id.home_employ_logo));
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent();
                    detail.setClass(EmployActivity.this, RecruitDetailActivity.class);
                    detail.putExtra("positionId", ri.getId());
                    detail.putExtra("positionName", ri.getPositionName());
                    startActivity(detail);
                }
            });
        }
    };

    @OnClick({R.id.search_bar_layout, R.id.search_back})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.search_bar_layout:
                Intent search = new Intent();
                search.setClass(EmployActivity.this, SearchActivity.class);
                startActivity(search);
                break;

//            case R.id.search_bar_updown:
//                mSpinerPopWindow.setWidth(updownSp.getWidth());
//                mSpinerPopWindow.showAsDropDown(updownSp);
//                setTextImage(R.drawable.icon_up);
//                break;

            case R.id.search_back:
                finish();
                break;
        }
    }
}
