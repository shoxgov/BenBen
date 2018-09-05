package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.dialog.GoCertifyDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CooperateEnterpriseInfoResponse;
import com.benben.bb.okhttp3.response.SearchResultResponse;
import com.benben.bb.okhttp3.widget.loading.LoadingView;
import com.benben.bb.utils.DialogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.benben.bb.view.TitleBar;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-08-16.
 * email:shoxgov@126.com
 */

public class EnterpriseCooperationSearchActivity extends BaseActivity {
    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    @Bind(R.id.search_key_edit)
    EditText searchKeyEdit;
    @Bind(R.id.search_hint)
    TextView searchHint;
    @Bind(R.id.search_nodata_layout)
    LinearLayout nodataLayout;
    /**
     * 搜索企业的名称
     */
    private String searchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooperate_enterprise_search);
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
        searchKeyEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH /*|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)*/) {
//do something;
                    String keyContent = searchKeyEdit.getText().toString();
                    if (!TextUtils.isEmpty(keyContent)) {
                        searchKey(keyContent);
                    }
                    Utils.closeInputMethod(EnterpriseCooperationSearchActivity.this);
                    return true;
                }
                return false;
            }
        });
        recyclerSwipeLayout.setXCallBack(callBack);
    }

    private void searchKey(String key) {
        DialogUtil.showDialogLoading(this,"");
        searchName = key;
        Map<String, String> params = new HashMap<String, String>();
        params.put("companyName", key);
        OkHttpUtils.getAsyn(NetWorkConfig.SEARCH_COOPERATE_ENTERPRISE, params, CooperateEnterpriseInfoResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                searchHint.setVisibility(View.GONE);
                DialogUtil.hideDialogLoading();
                CooperateEnterpriseInfoResponse cer = (CooperateEnterpriseInfoResponse) br;
                if (cer.getCode() == 1) {
                    if (cer.getData() == null) {
                        nodataLayout.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        List<CooperateEnterpriseInfoResponse.CompanyInfo> data = new ArrayList<>();
                        data.add(cer.getData());
                        recyclerSwipeLayout.addData(data);
                        recyclerSwipeLayout.loadComplete();
                        recyclerSwipeLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            final CooperateEnterpriseInfoResponse.CompanyInfo ci = (CooperateEnterpriseInfoResponse.CompanyInfo) itemModel;
            baseViewHolder.setText(R.id.list_cooperation_search_company_name, ci.getCompanyName());
            baseViewHolder.setText(R.id.list_cooperation_search_company_addr, ci.getCompanyRegion());
            baseViewHolder.getView(R.id.list_cooperation_search_collaborate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String info = "确认与该企业合作";
                    GoCertifyDialog warnDialog = new GoCertifyDialog(EnterpriseCooperationSearchActivity.this, "新增合作确认", info, "取消", true, "确认", true, new DialogCallBack() {
                        @Override
                        public void OkDown(Object score) {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("companyId", ci.getId() + "");
                            OkHttpUtils.getAsyn(NetWorkConfig.ADD_COOPERATE_ENTERPRISE, params, BaseResponse.class, new HttpCallback() {
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
                                }
                            });
                        }

                        @Override
                        public void CancleDown() {

                        }
                    });
                    warnDialog.show();
                }
            });
        }
    };

    @OnClick(R.id.search_add_enterprise)
    public void onViewClicked() {
        Intent edit = new Intent();
        edit.setClass(this, EnterpriseInfoEditActivity.class);
        edit.putExtra("companyName", searchName);
        edit.putExtra("edit", true);
        edit.putExtra("type", "add");
        startActivityForResult(edit, 11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 11 && resultCode == RESULT_OK){
            searchKey(searchName);
        }
    }
}
