package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.utils.DialogUtil;
import com.benben.bb.utils.GlideImageLoader;
import com.benben.bb.utils.ImageUtils;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
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
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2017-12-25.
 * email:shoxgov@126.com
 */

public class RealNameCertifyActivity extends BaseActivity {

    private static final int REQUEST_CODE_FRONT = 1;
    private static final int REQUEST_CODE_BACK = 2;
    @Bind(R.id.titlelayout)
    TitleBar titleBar;
    @Bind(R.id.human_certify_realname)
    EditText nameEdit;
    @Bind(R.id.human_certify_id_num)
    EditText idNumEdit;
    @Bind(R.id.human_certify_id_front)
    ImageView idFrontImg;
    @Bind(R.id.human_certify_id_back)
    ImageView idBackImg;
    @Bind(R.id.human_certify_submit)
    Button submit;

    /**
     * validateStatus  0未认证1已通过2认证失败3认证中
     */
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realname_certify);
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
        //validateStatus  实名认证状态
        //0未认证3认证中2认证失败1已通过
        switch (status) {
            case 3://认证中
            case 2://认证失败
                if (status == 3) {
                    submit.setEnabled(false);
                    nameEdit.setEnabled(false);
                    idNumEdit.setEnabled(false);
                    idFrontImg.setEnabled(false);
                    idBackImg.setEnabled(false);
                    submit.setBackgroundResource(R.color.gray_dark);
                    submit.setText("审核中");
                } else {
                    submit.setEnabled(true);
                    nameEdit.setEnabled(true);
                    idNumEdit.setEnabled(true);
                    idFrontImg.setEnabled(true);
                    idBackImg.setEnabled(true);
                    submit.setBackgroundResource(R.color.bluetheme);
                    ToastUtil.showText("审核失败，请重新提交");
                }
                nameEdit.setText(UserData.getUserData().getTrueName());
                idNumEdit.setText(UserData.getUserData().getIdentityCard());
                if (!TextUtils.isEmpty(UserData.getUserData().getIdcardZheng())) {
                    Glide.with(this).load(UserData.getUserData().getIdcardZheng()).placeholder(R.mipmap.realname_certify_id_front).error(R.mipmap.image_error).into(idFrontImg);//旋转90度
                }
                if (!TextUtils.isEmpty(UserData.getUserData().getIdcardFan())) {
                    Glide.with(this).load(UserData.getUserData().getIdcardFan()).placeholder(R.mipmap.realname_certify_id_back).error(R.mipmap.image_error).into(idBackImg);//旋转90度
                }
                break;
            case 1:
                submit.setEnabled(false);
                nameEdit.setEnabled(false);
                idNumEdit.setEnabled(false);
                idFrontImg.setEnabled(false);
                idBackImg.setEnabled(false);
                submit.setBackgroundResource(R.color.gray_dark);
                submit.setText("审核通过");
                nameEdit.setText(UserData.getUserData().getTrueName());
                idNumEdit.setText(UserData.getUserData().getIdentityCard());
                if (!TextUtils.isEmpty(UserData.getUserData().getIdcardZheng())) {
                    Glide.with(this).load(UserData.getUserData().getIdcardZheng()).placeholder(R.mipmap.realname_certify_id_front).error(R.mipmap.image_error).into(idFrontImg);//旋转90度
                }
                if (!TextUtils.isEmpty(UserData.getUserData().getIdcardFan())) {
                    Glide.with(this).load(UserData.getUserData().getIdcardFan()).placeholder(R.mipmap.realname_certify_id_back).error(R.mipmap.image_error).into(idBackImg);//旋转90度
                }
                idFrontImg.setTag("");
                idBackImg.setTag("");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FRONT && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (items != null && items.size() > 0) {
                try {
                    idFrontImg.setTag(items.get(0).path);
                    idFrontImg.setImageBitmap(ImageUtils.decodeBitmap(items.get(0).path));
//                    Glide.with(getApplicationContext())
//                            .load(new File(items.get(0).path)).diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                            .error(R.mipmap.realname_certify_id_front)//
//                            .transform(new RotateTransformation(this, -90f))//旋转90度
//                            .into(idFrontImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtil.d("onActivityResult path=" + items.get(0).path);
            }
        } else if (requestCode == REQUEST_CODE_BACK && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (items != null && items.size() > 0) {
                try {
                    idBackImg.setTag(items.get(0).path);
                    idBackImg.setImageBitmap(ImageUtils.decodeBitmap(items.get(0).path));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick({R.id.human_certify_id_front, R.id.human_certify_id_back, R.id.human_certify_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.human_certify_id_front:
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
            case R.id.human_certify_id_back:
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
            case R.id.human_certify_submit:
                String frontPath = idFrontImg.getTag().toString();
                String backPath = idBackImg.getTag().toString();
                if (TextUtils.isEmpty(frontPath) || TextUtils.isEmpty(backPath)) {
                    ToastUtil.showText("请选择图片");
                    return;
                }
                String trueName = nameEdit.getText().toString();
                if (TextUtils.isEmpty(trueName)) {
                    ToastUtil.showText("请输入身份证姓名");
                    return;
                }
                String idNum = idNumEdit.getText().toString();
                if (TextUtils.isEmpty(idNum) || idNum.length() != 18) {//430426198605280539
                    ToastUtil.showText("请输入合法的身份证号");
                    return;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("trueName", trueName);
                params.put("identityCard", idNum);
                List<File> files = new ArrayList<>();
                files.add(new File(frontPath));
                files.add(new File(backPath));
                DialogUtil.showDialogLoading(this, "");
                OkHttpUtils.postAsynFiles(NetWorkConfig.REALNAME_CERTIFY, "file", files, params, new HttpCallback() {

                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        super.onSuccess(baseResponse);
                        if (baseResponse.getCode() == 1) {
                            ToastUtil.showText("申请成功");
                            Utils.updateUserInfomation();
//                            UserData.getUserData().setValidateStatus(3);//validateStatus 0未认证1已通过2认证失败3认证中
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
