package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.WarnDialog;
import com.benben.bb.dialog.WheelBottomDialog;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.CompanyCategoryResponse;
import com.benben.bb.okhttp3.response.CompanyInfoResponse;
import com.benben.bb.okhttp3.response.CompanyNatureResponse;
import com.benben.bb.utils.GlideImageLoader;
import com.benben.bb.utils.ImageUtils;
import com.benben.bb.utils.PreferenceUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.RoundImageView;
import com.benben.bb.view.TitleBar;
import com.bumptech.glide.Glide;
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

/**
 * Created by wangshengyin on 2018-05-14.
 * email:shoxgov@126.com
 */

public class EnterpriseInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_IMG = 15;
    @Bind(R.id.enterprise_info_gracefull_layout)
    LinearLayout gracefullLayout;
    @Bind(R.id.company_info_name)
    TextView infoName;
    @Bind(R.id.company_info_hint)
    TextView infoHint;
    @Bind(R.id.enterprise_info_nature)
    TextView infoNature;
    @Bind(R.id.enterprise_info_size)
    TextView infoSize;
    @Bind(R.id.enterprise_info_introduction)
    EditText infoIntroduction;
    List<String> pics = new ArrayList<>();
    private List<CompanyNatureResponse.NatureInfo> natureList;
    private List<CompanyNatureResponse.ScaleInfo> scaleList;
    private List<CompanyCategoryResponse.CategoryFirst> categoryList;
    /**
     * 标记是否原来有图片
     */
    private boolean isHavePic = false;
    /**
     * 保存选择的
     */
    private CompanyNatureResponse.ScaleInfo selectScale;
    private CompanyNatureResponse.NatureInfo selectNature;
    private int companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_info);
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
        findViewById(R.id.enterprise_info_nature).setOnClickListener(this);
        findViewById(R.id.enterprise_info_size).setOnClickListener(this);
        findViewById(R.id.enterprise_info_submit).setOnClickListener(this);
        pics.add("default");
        addPicLayout(gracefullLayout, pics);
        requestCategory();
    }

    private void requestCategory() {
        OkHttpUtils.getAsyn(NetWorkConfig.GET_COMPANY_CATEGORY, CompanyCategoryResponse.class, new HttpCallback() {

            @Override
            public void onSuccess(BaseResponse baseResponse) {
                super.onSuccess(baseResponse);
                CompanyCategoryResponse ccr = (CompanyCategoryResponse) baseResponse;
                if (ccr.getCode() == 1) {
                    categoryList = ccr.getData();
                }
                requestNature();
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                requestNature();
            }
        });
    }

    private void requestNature() {
        OkHttpUtils.getAsyn(NetWorkConfig.GET_COMPANY_NATURE, CompanyNatureResponse.class, new HttpCallback() {

            @Override
            public void onSuccess(BaseResponse baseResponse) {
                super.onSuccess(baseResponse);
                CompanyNatureResponse cnr = (CompanyNatureResponse) baseResponse;
                if (cnr.getCode() == 1) {
                    scaleList = cnr.getData().getScaleList();
                    natureList = cnr.getData().getNatureList();
                }
                getCompanyInfo();
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                getCompanyInfo();
            }
        });
    }

    private void getCompanyInfo() {
        OkHttpUtils.getAsyn(NetWorkConfig.GET_COMPANY_INFO, CompanyInfoResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                super.onSuccess(baseResponse);
                CompanyInfoResponse cir = (CompanyInfoResponse) baseResponse;
                if (cir.getCode() == 1) {
                    companyId = cir.getData().getId();
                    String photo = cir.getData().getCompanyMien();
                    if (!TextUtils.isEmpty(photo)) {
                        isHavePic = true;
                        if (photo.contains(",")) {
                            String[] photos = photo.split(",");
                            for (String s : photos) {
                                pics.add(0, s);
                            }
                        } else {
                            pics.add(0, photo);
                        }
                        addPicLayout(gracefullLayout, pics);
                    } else {
                        isHavePic = false;
                    }
                    infoName.setText(cir.getData().getCompanyName());
                    String category = cir.getData().getCategoriesId() + "";
                    if (categoryList != null && !categoryList.isEmpty()) {
                        category = findCategoryName(cir.getData().getCategoriesId());
                    }
                    infoHint.setText(cir.getData().getCompanyAddr() + " | " + category);
                    String cpSize = "";
                    if (scaleList != null && !scaleList.isEmpty()) {
                        selectScale = scaleList.get(cir.getData().getCompanySize() - 1);
                        cpSize = selectScale.getName();
                    }
                    infoSize.setText(cpSize);
                    String cpNature = "";
                    if (natureList != null && !natureList.isEmpty()) {
                        selectNature = natureList.get(cir.getData().getCompanyNature() - 1);
                        cpNature = selectNature.getName();
                    }
                    infoNature.setText(cpNature);
                    if (!TextUtils.isEmpty(cir.getData().getIntroduction())) {
                        infoIntroduction.setText(cir.getData().getIntroduction() + "");
                    }
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    private void addPicLayout(LinearLayout layout, List<String> url) {
        layout.removeAllViews();
        if (url == null || url.isEmpty()) {
            return;
        }
        int width = Utils.dip2px(this, 260);
        int height = Utils.dip2px(this, 140);
        int span = Utils.dip2px(this, 10);
        for (int i = 0; i < url.size(); i++) {
            View imgLayout = LayoutInflater.from(this).inflate(R.layout.enterprise_grace_image, null, false);
            RoundImageView iv = imgLayout.findViewById(R.id.gracefull_img);
            ImageView clear = imgLayout.findViewById(R.id.gracefull_img_clear);
            clear.setTag(url.get(i));
            if (url.get(i).equals("default")) {
                iv.setImageResource(R.mipmap.enterprise_certify_bg);
                clear.setVisibility(View.GONE);
                clear.setOnClickListener(null);
            } else {
                Glide.with(this).load(url.get(i)).error(R.mipmap.default_image).into(iv);
                clear.setVisibility(View.VISIBLE);
                clear.setOnClickListener(this);
            }
            iv.setOnClickListener(new ImgOnClickLisener(url.get(i), "图片"));
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
            param.setMargins(span, 0, span, 0);
            param.gravity = Gravity.CENTER;
            imgLayout.setLayoutParams(param);
            layout.addView(imgLayout);
        }
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.gracefull_img_clear:
                if (isHavePic) {
                    WarnDialog warnDialog = new WarnDialog(EnterpriseInfoActivity.this, "中途删除会清除所有旧图片，确定删除？", new DialogCallBack() {
                        @Override
                        public void OkDown(Object obj) {
                            isHavePic = false;
                            pics.clear();
                            pics.add("default");
                            addPicLayout(gracefullLayout, pics);
                        }

                        @Override
                        public void CancleDown() {

                        }
                    });
                    warnDialog.show();
                } else {
                    pics.remove(v.getTag().toString());
                    addPicLayout(gracefullLayout, pics);
                }
                break;

            case R.id.enterprise_info_nature:
                if (natureList == null) {
                    return;
                }
                final String[] nature = new String[natureList.size()];
                int i = 0;
                for (CompanyNatureResponse.NatureInfo ni : natureList) {
                    nature[i] = ni.getName();
                    i++;
                }
                WheelBottomDialog sexDla = new WheelBottomDialog(this, nature, new DialogCallBack() {
                    @Override
                    public void OkDown(Object score) {
                        int position = (int) score;
                        selectNature = natureList.get(position);
                        infoNature.setText(selectNature.getName());
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                sexDla.show();
                break;

            case R.id.enterprise_info_size:
                if (scaleList == null) {
                    return;
                }
                final String[] scale = new String[scaleList.size()];
                int ii = 0;
                for (CompanyNatureResponse.ScaleInfo si : scaleList) {
                    scale[ii] = si.getName();
                    ii++;
                }
                WheelBottomDialog scaleDla = new WheelBottomDialog(this, scale, new DialogCallBack() {
                    @Override
                    public void OkDown(Object score) {
                        int position = (int) score;
                        selectScale = scaleList.get(position);
                        infoSize.setText(selectScale.getName());
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                scaleDla.show();
                break;

            case R.id.enterprise_info_submit:
                String tempIntr = infoIntroduction.getText().toString();
                if (TextUtils.isEmpty(tempIntr)) {
                    ToastUtil.showText("请完善简介");
                    return;
                }
                if (selectNature == null || selectScale == null) {
                    ToastUtil.showText("请选择企业属性或规模");
                    return;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("companyNature", selectNature.getId() + "");
                params.put("companySize", selectScale.getId() + "");
                params.put("introduction", tempIntr);
                params.put("id", companyId + "");
                String filekey = "file";
                List<File> files = new ArrayList<>();
                if (pics != null && !pics.isEmpty()) {
                    for (String p : pics) {
                        if (!p.equals("default") && !p.startsWith("http")) {
                            files.add(new File(p));
                        }
                    }
                }
                if (files.isEmpty()) {
                    files = null;
                    filekey = "";
                }
                OkHttpUtils.postAsynFiles(NetWorkConfig.GET_COMPANY_INFO_SAVE, filekey, files, params, BaseResponse.class, new HttpCallback() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        ToastUtil.showText(baseResponse.getMessage());
                        if (baseResponse.getCode() == 1) {
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

    class ImgOnClickLisener implements View.OnClickListener {

        String url;
        String filename;

        public ImgOnClickLisener(String url, String filename) {
            this.url = url;
            this.filename = filename;
        }

        @Override
        public void onClick(View v) {
            if (url.equals("default")) {
                if (isHavePic) {
                    WarnDialog warnDialog = new WarnDialog(EnterpriseInfoActivity.this, "中途修改会清除所有旧图片，确定删除？", new DialogCallBack() {
                        @Override
                        public void OkDown(Object obj) {
                            isHavePic = false;
                            pics.clear();
                            pics.add("default");
                            addPicLayout(gracefullLayout, pics);
                            ImagePicker imagePicker = ImagePicker.getInstance();
                            imagePicker.setImageLoader(new GlideImageLoader());
                            imagePicker.setMultiMode(false);   //多选
                            imagePicker.setShowCamera(true);  //显示拍照按钮
                            imagePicker.setCrop(true);       //不进行裁剪
                            imagePicker.setOutPutX(1000);
                            imagePicker.setOutPutY(600);
                            imagePicker.setFocusWidth(1000);
                            imagePicker.setFocusHeight(600);
                            Intent intent = new Intent(EnterpriseInfoActivity.this, ImageGridActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_IMG);
                        }

                        @Override
                        public void CancleDown() {

                        }
                    });
                    warnDialog.show();
                } else {
                    ImagePicker imagePicker = ImagePicker.getInstance();
                    imagePicker.setImageLoader(new GlideImageLoader());
                    imagePicker.setMultiMode(false);   //多选
                    imagePicker.setShowCamera(true);  //显示拍照按钮
                    imagePicker.setCrop(true);       //不进行裁剪
                    imagePicker.setOutPutX(1000);
                    imagePicker.setOutPutY(600);
                    imagePicker.setFocusWidth(1000);
                    imagePicker.setFocusHeight(600);
                    Intent intent = new Intent(EnterpriseInfoActivity.this, ImageGridActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_IMG);
                }
            }
//            Intent intent = new Intent();
//            intent.setClass(EnterpriseInfoActivity.this, ImageViewActivity.class);
//            intent.putExtra("url", url);
//            intent.putExtra("filename", filename);
//            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMG && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (items != null && items.size() > 0) {
                try {
                    pics.add(pics.size() - 1, items.get(0).path);
                    addPicLayout(gracefullLayout, pics);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String findCategoryName(int id) {
        for (CompanyCategoryResponse.CategoryFirst cf : categoryList) {
            List<CompanyCategoryResponse.CategorySecond> categorySecond = cf.getCompanyCategory();
            if (categorySecond == null || categorySecond.isEmpty()) {
                continue;
            }
            for (CompanyCategoryResponse.CategorySecond cs : categorySecond) {
                if (cs.getId() == id) {
                    return cs.getName();
                }
            }
        }
        return "";
    }
}
