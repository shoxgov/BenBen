package com.benben.bb.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.SearchResultResponse;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.SpinerPopWindow;
import com.bumptech.glide.Glide;
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
 * Created by wangshengyin on 2018-05-21.
 * email:shoxgov@126.com
 */

public class SearchActivity extends BaseActivity {
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.search_key_edit)
    EditText keyEdit;
    @Bind(R.id.search_bar_updown)
    TextView updownSp;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    /**
     * 保存上一次搜索关键词
     */
    private String oldKey = "";
    private int oldUpdown = 0;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        recyclerSwipeLayout.createAdapter(R.layout.list_home_hot_employ);
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        String[] data = getResources().getStringArray(R.array.search_type_updown);
        list = Arrays.asList(data);
        mSpinerPopWindow = new SpinerPopWindow<String>(this, list, itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
    }

    /**
     * 监听popupwindow取消
     */
    private OnDismissListener dismissListener = new OnDismissListener() {
        @Override
        public void onDismiss() {
            //setTextImage(R.drawable.icon_down);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            updownSp.setText(list.get(position));
            reset();
            oldUpdown = position;
            requestSearchKey(oldKey, position);
//            Toast.makeText(MainActivity.this, "点击了:" + list.get(position),Toast.LENGTH_LONG).show();
        }
    };

    private void requestSearchKey(String key, int sort) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNum", "" + pageNo);
        params.put("pageSize", "" + pageSize);
        params.put("positionName", key);
        params.put("sort", sort + "");//0升序1降序默认0
        OkHttpUtils.getAsyn(NetWorkConfig.INDEX_RECRUIT_SEARCH, params, SearchResultResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                SearchResultResponse srr = (SearchResultResponse) br;
                if (srr.getCode() == 1) {
                    pageNo = srr.getData().getPageNum();
                    totalPage = srr.getData().getPages();
                    if (totalPage == 0) {
                        recyclerSwipeLayout.setEmpty();
                        return;
                    }
                    recyclerSwipeLayout.openLoadMore(totalPage);
                    recyclerSwipeLayout.addData(srr.getData().getList());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    private void reset() {
        pageNo = 1;
        totalPage = -1;
        recyclerSwipeLayout.clear();
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
                requestSearchKey(oldKey, oldUpdown);
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
            String img = ri.getCompanyMien();
            if (!TextUtils.isEmpty(img) && img.contains(",")) {
                img = img.split(",")[0];
            }
            Glide.with(SearchActivity.this)
                    .load(img)
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into((ImageView) baseViewHolder.getView(R.id.home_employ_logo));
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent();
                    detail.setClass(SearchActivity.this, RecruitDetailActivity.class);
                    detail.putExtra("positionId", ri.getId());
                    detail.putExtra("positionName", ri.getPositionName());
                    startActivity(detail);
                }
            });
        }
    };

    @OnClick({R.id.search_ok, R.id.search_bar_updown, R.id.search_bar_qzone, R.id.search_bar_category})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.search_ok:
                String temp = keyEdit.getText().toString();
                if (TextUtils.isEmpty(temp)) {
                    ToastUtil.showText("请输入职位");
                    return;
                }
                if (temp.equals(oldKey)) {
                    return;
                }
                oldKey = temp;
                pageNo = 1;
                pageSize = 10;
                totalPage = -1;
                recyclerSwipeLayout.clear();
                requestSearchKey(temp, 0);
                Utils.closeInputMethod(this);
                break;

            case R.id.search_bar_updown:
                mSpinerPopWindow.setWidth(updownSp.getWidth());
                mSpinerPopWindow.showAsDropDown(updownSp);
//                setTextImage(R.drawable.icon_up);
                break;
            case R.id.search_bar_qzone:
                ToastUtil.showText("暂不支持地区选择");
                break;
            case R.id.search_bar_category:
                ToastUtil.showText("暂不支持行业分类");
                break;
        }
    }
}
