package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.dialog.ThreeWheelViewDialogs;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyInfoResponse;
import com.benben.bb.utils.GlideImageLoader;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
 * Created by wangshengyin on 2018-05-14.
 * email:shoxgov@126.com
 */

public class EnterpriseInfoEditActivity extends BaseActivity {
    private static final int REQUEST_CODE_IMG = 15;
    @Bind(R.id.enterprise_info_edit_ground)
    FrameLayout gracefullLayout;
    @Bind(R.id.enterprise_cover_add)
    View coverView;
    @Bind(R.id.enterprise_info_edit_ground_change)
    TextView groundChange;
    @Bind(R.id.enterprise_info_submit)
    Button saveBtn;
    @Bind(R.id.enterprise_info_edit_certify_tag)
    ImageView certifyTag;
    @Bind(R.id.enterprise_info_edit_name)
    TextView infoName;
    @Bind(R.id.enterprise_info_edit_addr)
    TextView addrNameTv;
    @Bind(R.id.enterprise_info_edit_introduction)
    EditText infoIntroduction;
    private String companyPic = "https://img14.360buyimg.com/n0/jfs/t22849/110/1423883954/360356/ec4d867f/5b600398N57b5675a.jpg";
    private String companyRegion;
    /**
     * 保存选择的
     */
    private int companyId;
    private String companyName, type;
    private boolean edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_info_edit);
        companyName = getIntent().getStringExtra("companyName");
        companyId = getIntent().getIntExtra("companyId", 0);
        type = getIntent().getStringExtra("type");
        edit = getIntent().getBooleanExtra("edit", false);
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
        groundChange.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(companyName)) {
            infoName.setText(companyName);
        }
        if (!edit) {
            saveBtn.setVisibility(View.GONE);
            infoIntroduction.setEnabled(false);
        }
        if (companyId > 0) {
            getCompanyInfo();
        }
        if (!TextUtils.isEmpty(type) && type.equals("add")) {
            saveBtn.setText("新增");
        }
    }

    private void getCompanyInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("companyId", companyId + "");
        OkHttpUtils.getAsyn(NetWorkConfig.GET_COMPANY_INFO, params, CompanyInfoResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                super.onSuccess(baseResponse);
                CompanyInfoResponse cir = (CompanyInfoResponse) baseResponse;
                if (cir.getCode() == 1) {
                    companyId = cir.getData().getCompanyInfo().getId();
                    companyRegion = cir.getData().getCompanyInfo().getCompanyRegion();
                    infoName.setText(cir.getData().getCompanyInfo().getCompanyName());
                    String photo = cir.getData().getCompanyInfo().getCompanyMien();
                    if (!TextUtils.isEmpty(photo)) {
                        if (photo.contains(",")) {
                            String[] photos = photo.split(",");
                            loadCompanyPic(photos[0]);
                        } else {
                            loadCompanyPic(photo);
                        }
                    } else {
                        loadCompanyPic(companyPic);
                    }
                    if (!TextUtils.isEmpty(companyRegion)) {
                        addrNameTv.setText(companyRegion.replace(".", ""));
                    }
                    if (!TextUtils.isEmpty(cir.getData().getCompanyInfo().getIntroduction())) {
                        infoIntroduction.setText(cir.getData().getCompanyInfo().getIntroduction() + "");
                    }
                    if (cir.getData().getCompanyInfo().getAuditStatus() == 1) {
                        certifyTag.setVisibility(View.VISIBLE);
                    } else {
                        certifyTag.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }


    @OnClick({R.id.enterprise_info_edit_addr_change, R.id.enterprise_info_edit_ground_change, R.id.enterprise_info_submit, R.id.enterprise_cover_add})
    public void onViewClicked(View v) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.enterprise_info_edit_addr_change:
                ThreeWheelViewDialogs addressDla = new ThreeWheelViewDialogs(this, new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {
                        try {
                            if (!TextUtils.isEmpty(obj.toString())) {
                                companyRegion = obj.toString();
                                addrNameTv.setText(companyRegion.replace(".", ""));
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
            case R.id.enterprise_cover_add:
            case R.id.enterprise_info_edit_ground_change:
                ImagePicker imagePicker = ImagePicker.getInstance();
                imagePicker.setImageLoader(new GlideImageLoader());
                imagePicker.setMultiMode(false);   //多选
                imagePicker.setShowCamera(true);  //显示拍照按钮
                imagePicker.setCrop(true);       //不进行裁剪
                imagePicker.setOutPutX(1000);
                imagePicker.setOutPutY(600);
                imagePicker.setFocusWidth(1000);
                imagePicker.setFocusHeight(600);
                Intent intent = new Intent(EnterpriseInfoEditActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_IMG);
                break;

            case R.id.enterprise_info_submit:
                String tempIntr = infoIntroduction.getText().toString();
                if (TextUtils.isEmpty(tempIntr)) {
                    ToastUtil.showText("请完善简介");
                    return;
                }
                Map<String, String> params = new HashMap<String, String>();
//                params.put("companyNature", selectNature.getId() + "");
//                params.put("companySize", selectScale.getId() + "");
                params.put("introduction", tempIntr);
                params.put("companyRegion", companyRegion);
                if (companyId > 0) {
                    params.put("id", companyId + "");
                }
                String filekey = "file";
                List<File> files = new ArrayList<>();
                if (!TextUtils.isEmpty(companyPic) && !companyPic.startsWith("http")) {
                    files.add(new File(companyPic));
                }
                String url;
                if (!TextUtils.isEmpty(type) && type.equals("add")) {
                    url = NetWorkConfig.SAVE_COOPERATE_ENTERPRISE_INFO;
                } else {
                    url = NetWorkConfig.GET_COMPANY_INFO_SAVE;
                }
                OkHttpUtils.postAsynFiles(url, filekey, files, params, BaseResponse.class, new HttpCallback() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        ToastUtil.showText(baseResponse.getMessage());
                        if (baseResponse.getCode() == 1) {
                            setResult(RESULT_OK);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMG && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (items != null && items.size() > 0) {
                try {
                    companyPic = items.get(0).path;
                    loadCompanyPic(companyPic);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void loadCompanyPic(String path) {
        Glide.with(this)
                .load(path)
                .placeholder(R.mipmap.default_image)
                .error(R.mipmap.default_image)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        gracefullLayout.setBackgroundDrawable(resource);
                    }
                });
        coverView.setVisibility(View.GONE);
        groundChange.setVisibility(View.VISIBLE);
    }

}
