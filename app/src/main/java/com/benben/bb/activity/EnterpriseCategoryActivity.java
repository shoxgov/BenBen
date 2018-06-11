package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.adapter.CategoryAdapter;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyCategoryResponse;
import com.benben.bb.utils.DialogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.TitleBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangshengyin on 2018-05-11.
 * email:shoxgov@126.com
 */

public class EnterpriseCategoryActivity extends BaseActivity {

    @Bind(R.id.enterprise_category_list_left)
    ListView lList;
    @Bind(R.id.enterprise_category_list_right)
    ListView rList;
//    private List<CategoryProfessModel> professionList = null;
    private CategoryAdapter leftAdapter, rightAdapter;
    private List<CompanyCategoryResponse.CategoryFirst> categoryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_category);
        ButterKnife.bind(this);
        init();
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
        leftAdapter = new CategoryAdapter(this, false);
        rightAdapter = new CategoryAdapter(this, true);
        lList.setAdapter(leftAdapter);
        rList.setAdapter(rightAdapter);
//        initProfessionDatas();
        lList.setOnItemClickListener(new MyOnItemClickListener(false));
        rList.setOnItemClickListener(new MyOnItemClickListener(true));
        requestCategory();
    }

    private void requestCategory() {
        DialogUtil.showDialogLoading(this,"");
        OkHttpUtils.getAsyn(NetWorkConfig.GET_COMPANY_CATEGORY, null, CompanyCategoryResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                CompanyCategoryResponse ccr = (CompanyCategoryResponse) br;
                if(ccr.getCode() == 1){
                    categoryDate = ccr.getData();
                    leftAdapter.setData(categoryDate);
                    rightAdapter.setData(categoryDate);
                    leftAdapter.setSelectItem(0);
                    rightAdapter.setSelectItem(0);
                }
                DialogUtil.hideDialogLoading();
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                ToastUtil.showText(message);
                DialogUtil.hideDialogLoading();
            }
        });
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        private boolean isSub;

        private MyOnItemClickListener(boolean isSub) {
            this.isSub = isSub;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (isSub) {
                CompanyCategoryResponse.CategorySecond temp = categoryDate.get(leftAdapter.getSelectItem()).getCompanyCategory().get(position);
                Intent rs = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("result", temp.getName());
                bundle.putInt("id", temp.getId());
                rs.putExtras(bundle);
                setResult(RESULT_OK, rs);
                finish();
            } else {
                leftAdapter.setSelectItem(position);
                rightAdapter.setSelectItem(position);
            }

        }
    }

    /**
     * 解析省市区的XML数据
     */
    private void initProfessionDatas() {

//        try {
//            InputStream input = getResources().openRawResource(R.raw.category_data);
//            // 创建一个解析xml的工厂对象
//            SAXParserFactory spf = SAXParserFactory.newInstance();
//            // 解析xml
//            SAXParser parser = spf.newSAXParser();
//            XmlParserCategoryHandler handler = new XmlParserCategoryHandler();
//            parser.parse(input, handler);
//            input.close();
            // 获取解析出来的数据
//            professionList = handler.getDataList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        }
    }
}
