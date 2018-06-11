package com.benben.bb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 */
public class CustomBaseQuickAdapter<T> {

    private MainAdapter adapter;
    private QuickXCallBack callBack;

    public void openLoadAnimation(int scalein) {
        adapter.openLoadAnimation(scalein);
    }

    public void openLoadMore(int totalPage) {
        adapter.openLoadMore(totalPage);
    }

    public void loadComplete() {
        adapter.loadComplete();
    }

    public void addData(List<T> data) {
        adapter.addData(data);
    }

    public void setNewData(List<T> data) {
        adapter.setNewData(data);
    }

    public void addData(int position, T data) {
        adapter.add(position, data);
    }

    public void isFirstOnly(boolean b) {
        adapter.isFirstOnly(b);
    }

    public void setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener) {
        adapter.setOnLoadMoreListener(quickAdapterListener);
    }

    public void setEmptyView(View view) {
        adapter.setEmptyView(view);
    }


    public void setAdapter(RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }

    public void addHeaderView(View header) {
        adapter.addHeaderView(header);
    }

    public void addHeaderView(View header, int index) {
        adapter.addHeaderView(header, index);
    }

    public void addFooterView(View footer) {
        adapter.addFooterView(footer);
    }

    public void addFooterView(View footer, int index) {
        adapter.addFooterView(footer, index);
    }

    public interface QuickXCallBack {
        public void convert(BaseViewHolder baseViewHolder, Object itemModel);
    }


    public CustomBaseQuickAdapter(int resId) {
        adapter = new MainAdapter(resId, null);
    }

    public CustomBaseQuickAdapter(int resId, List<T> data) {
        adapter = new MainAdapter(resId, data);
    }

    public void setXCallBack(QuickXCallBack callBack) {
        this.callBack = callBack;
    }

    private class MainAdapter extends BaseQuickAdapter<T> {

        MainAdapter(int resId, List<T> data) {
            super(resId, data);
        }

        @Override
        protected void convert(final BaseViewHolder baseViewHolder, final Object itemModel) {
            if (callBack != null) {
                callBack.convert(baseViewHolder, itemModel);
            }
        }
    }
}
