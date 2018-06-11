package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.ThreeWheelViewDialogs;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.utils.DialogUtil;
import com.benben.bb.utils.GlideImageLoader;
import com.benben.bb.utils.ImageUtils;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.TitleBar;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2017-12-25.
 * email:shoxgov@126.com
 */

public class EnterpriseCertifyActivity extends BaseActivity {

    private static final int HAND_CATEGORY = 11;
    private static final int REQUEST_CODE_FRONT = 1;
    private static final int REQUEST_CODE_BACK = 2;
    @Bind(R.id.titlelayout)
    TitleBar titleBar;
    @Bind(R.id.enterprise_certify_1)
    ImageView picFront;
    @Bind(R.id.enterprise_certify_2)
    ImageView picBack;
    @Bind(R.id.enterprise_certify_category_name)
    TextView categoryNameTv;
    @Bind(R.id.enterprise_certify_addr_name)
    TextView addrNameTv;
    @Bind(R.id.enterprise_certify_name)
    EditText nameEdit;
    @Bind(R.id.enterprise_certify_submit)
    Button submit;
    @Bind(R.id.enterprise_certify_realname)
    TextView realname;
    @Bind(R.id.enterprise_certify_realname_cardid)
    TextView cardId;

    /**
     * 企业认证  状态码  0 未认证
     * 99 申请中
     * 88 认证失败
     * 1 已认证
     * 2 3 4 5 6（等后期扩展使用）
     */
    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_certify);
        ButterKnife.bind(this);
        status = getIntent().getIntExtra("status", 0);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void init() {
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        realname.setText(UserData.getUserData().getTrueName());
        if (!TextUtils.isEmpty(UserData.getUserData().getIdentityCard())) {
            try {
                cardId.setText(UserData.getUserData().getIdentityCard().replace(UserData.getUserData().getIdentityCard().substring(4, 8), "****"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        picBack.setTag("");
        picFront.setTag("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FRONT && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (items != null && items.size() > 0) {
                try {
                    picFront.setTag(items.get(0).path);
                    picFront.setImageBitmap(ImageUtils.decodeBitmap(items.get(0).path));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_CODE_BACK && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (items != null && items.size() > 0) {
                try {
                    picBack.setTag(items.get(0).path);
                    picBack.setImageBitmap(ImageUtils.decodeBitmap(items.get(0).path));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == HAND_CATEGORY && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            //显示选中的内容
            categoryNameTv.setText(bundle.getString("result"));
            categoryNameTv.setTag(bundle.getInt("id")+"");
        }
    }

    @OnClick({R.id.enterprise_certify_1, R.id.enterprise_certify_2, R.id.enterprise_certify_category, R.id.enterprise_certify_addr, R.id.enterprise_certify_submit})
    public void onViewClicked(View view) {
        if(Utils.isFastDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.enterprise_certify_1:
                ImagePicker imagePicker = ImagePicker.getInstance();
                imagePicker.setImageLoader(new GlideImageLoader());
                imagePicker.setMultiMode(false);   //多选
                imagePicker.setShowCamera(true);  //显示拍照按钮
                imagePicker.setCrop(true);       //不进行裁剪
                imagePicker.setOutPutX(1000);
                imagePicker.setOutPutY(600);
                imagePicker.setFocusWidth(1000);
                imagePicker.setFocusHeight(600);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FRONT);
                break;
            case R.id.enterprise_certify_2:
                ImagePicker imagePicker2 = ImagePicker.getInstance();
                imagePicker2.setImageLoader(new GlideImageLoader());
                imagePicker2.setMultiMode(false);   //多选
                imagePicker2.setShowCamera(true);  //显示拍照按钮
                imagePicker2.setCrop(true);       //不进行裁剪
                imagePicker2.setOutPutX(1000);
                imagePicker2.setOutPutY(600);
                imagePicker2.setFocusWidth(1000);
                imagePicker2.setFocusHeight(600);
                Intent intent2 = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent2, REQUEST_CODE_BACK);
                break;

            case R.id.enterprise_certify_category:
                Intent category = new Intent();
                category.setClass(this, EnterpriseCategoryActivity.class);
                startActivityForResult(category, HAND_CATEGORY);
                break;

            case R.id.enterprise_certify_addr:
                ThreeWheelViewDialogs addressDla = new ThreeWheelViewDialogs(this, new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {
                        try {
                            if (!TextUtils.isEmpty(obj.toString())) {
                                addrNameTv.setText(obj.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                addressDla.show();
                break;
            case R.id.enterprise_certify_submit:
                String frontPath = picFront.getTag().toString();
                String backPath = picBack.getTag().toString();
                if (TextUtils.isEmpty(frontPath) && TextUtils.isEmpty(backPath)) {
                    ToastUtil.showText("请选择图片");
                    return;
                }
                String categoryName = categoryNameTv.getText().toString();
                if (TextUtils.isEmpty(categoryName)) {
                    ToastUtil.showText("请选择分类");
                    return;
                }
                String addr = addrNameTv.getText().toString();
                if (TextUtils.isEmpty(addr)) {
                    ToastUtil.showText("请输入区域");
                    return;
                }
                String name = nameEdit.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showText("请输入企业名");
                    return;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("categoryName", categoryNameTv.getTag().toString());
                params.put("companyName", name);
                params.put("companyAddr", addr);
                List<File> files = new ArrayList<>();
                if (!TextUtils.isEmpty(frontPath)) {
                    files.add(new File(frontPath));
                }
                if (!TextUtils.isEmpty(backPath)) {
                    files.add(new File(backPath));
                }
                DialogUtil.showDialogLoading(EnterpriseCertifyActivity.this,"");
                OkHttpUtils.postAsynFiles(NetWorkConfig.COMPANY_CERTIFY, "file", files, params, new HttpCallback() {

                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        super.onSuccess(baseResponse);
                        if (baseResponse.getCode() == 1) {
                            ToastUtil.showText(baseResponse.getMessage());
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            ToastUtil.showText(baseResponse.getMessage());
                        }
                        DialogUtil.hideDialogLoading();
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        ToastUtil.showText(message);
                    }
                });
                break;
        }
    }
}
