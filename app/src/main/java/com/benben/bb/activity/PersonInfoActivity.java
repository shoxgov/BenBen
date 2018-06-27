package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.benben.bb.MyApplication;
import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.ThreeWheelViewDialogs;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.utils.GlideImageLoader;
import com.benben.bb.utils.ImageUtils;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
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
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-16.
 * email:shoxgov@126.com
 */

public class PersonInfoActivity extends BaseActivity {

    private static final int PERSON_REF_NICKNAME = 11;
    private static final int PERSON_REF_SEX = 12;
    private static final int PERSON_REF_AGE = 13;
    private static final int PERSON_REF_TEL = 14;
    private static final int PERSON_REF_NATION = 15;
    private static final int PERSON_REF_SIGNATURE = 16;
    private static final int PERSON_REF_EDU = 17;
    private static final int REQUEST_CODE_PHOTO = 19;

    @Bind(R.id.my_person_photo)
    RoundImageView photoImg;
    @Bind(R.id.my_person_nickname)
    TextView nicknameTv;
    @Bind(R.id.my_person_id)
    TextView idTv;
    @Bind(R.id.my_person_sex)
    TextView sexTv;
    @Bind(R.id.my_person_age)
    TextView ageTv;
    @Bind(R.id.my_person_tel)
    TextView telTv;
    @Bind(R.id.my_person_edu)
    TextView eduTv;
    @Bind(R.id.my_person_addr)
    TextView addrTv;
    @Bind(R.id.my_person_nation)
    TextView nationTv;
    @Bind(R.id.my_person_signature)
    TextView signatureTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        TitleBar titleLayout = (TitleBar) findViewById(R.id.titlelayout);
        titleLayout.setTitleBarListener(new TitleBarListener() {

            @Override
            public void rightClick() {
            }

            @Override
            public void leftClick() {
                finish();
            }
        });

        if (UserData.getUserData().getSex() == 1) {
            sexTv.setText("女");
        } else {
            sexTv.setText("男");
        }
        nicknameTv.setText(UserData.getUserData().getNickName());
        idTv.setText(UserData.getUserData().getBenbenNum());
        ageTv.setText(UserData.getUserData().getAge());
        telTv.setText(UserData.getUserData().getUserName());
        if (!TextUtils.isEmpty(UserData.getUserData().getNation())) {
            nationTv.setText(UserData.getUserData().getNation());
        }
        if (!TextUtils.isEmpty(UserData.getUserData().getEducation())) {
            eduTv.setText(UserData.getUserData().getEducation());
        }
        if (!TextUtils.isEmpty(UserData.getUserData().getRegion())) {
            addrTv.setText(UserData.getUserData().getRegion());
        }
        if (!TextUtils.isEmpty(UserData.getUserData().getSignature())) {
            signatureTv.setText(UserData.getUserData().getSignature());
        }
        if (!TextUtils.isEmpty(UserData.getUserData().getAvatar())) {
            Glide.with(this)
                    .load(UserData.getUserData().getAvatar())
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into(photoImg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.my_person_photo_layout, R.id.my_person_nickname_layout, R.id.my_person_sex_layout, R.id.my_person_age_layout, R.id.my_person_tel, R.id.my_person_addr_layout, R.id.my_person_edu_layout, R.id.my_person_nation_layout, R.id.my_person_qr, R.id.my_person_signature_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_person_photo_layout:
                ImagePicker imagePicker = ImagePicker.getInstance();
                imagePicker.setImageLoader(new GlideImageLoader());
                imagePicker.setMultiMode(false);   //多选
                imagePicker.setShowCamera(true);  //显示拍照按钮
                imagePicker.setCrop(true);       //不进行裁剪
                imagePicker.setOutPutY(800);
                imagePicker.setOutPutX(800);
                imagePicker.setFocusWidth(600);
                imagePicker.setFocusHeight(600);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_PHOTO);
                break;
            case R.id.my_person_nickname_layout:
                Intent nick = new Intent();
                nick.setClass(this, PersonRefValueActivity.class);
                nick.putExtra("title", "昵称");
                nick.putExtra("nameTv", "昵称");
                nick.putExtra("nameValue", UserData.getUserData().getNickName());
                startActivityForResult(nick, PERSON_REF_NICKNAME);
                break;
            case R.id.my_person_sex_layout:
                Intent sex = new Intent();
                sex.setClass(this, PersonSexCheckActivity.class);
                sex.putExtra("sex", "男");
                startActivityForResult(sex, PERSON_REF_SEX);

                break;
            case R.id.my_person_age_layout:
                Intent age = new Intent();
                age.setClass(this, PersonRefValueActivity.class);
                age.putExtra("title", "年龄");
                age.putExtra("nameTv", "年龄");
                age.putExtra("nameValue", UserData.getUserData().getAge());
                startActivityForResult(age, PERSON_REF_AGE);
                break;
            case R.id.my_person_tel:
//                Intent tel = new Intent();
//                tel.setClass(this, PersonRefValueActivity.class);
//                tel.putExtra("title", "联系电话");
//                tel.putExtra("nameTv", "联系电话");
//                tel.putExtra("nameValue", "15687445299");
//                startActivityForResult(tel, PERSON_REF_TEL);
                break;

            case R.id.my_person_edu_layout:
                Intent edu = new Intent();
                edu.setClass(this, PersonEduActivity.class);
                startActivityForResult(edu, PERSON_REF_EDU);
                break;
            case R.id.my_person_addr_layout:
                ThreeWheelViewDialogs addressDla = new ThreeWheelViewDialogs(this, new DialogCallBack() {
                    @Override
                    public void OkDown(final Object obj) {
                        try {
                            if (!TextUtils.isEmpty(obj.toString())) {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", UserData.getUserData().getId() + "");
                                params.put("region", obj.toString());
                                OkHttpUtils.postAsynFile(NetWorkConfig.USER_UPDATEINFO, "", null, params, BaseResponse.class, new HttpCallback() {
                                    @Override
                                    public void onSuccess(BaseResponse baseResponse) {
                                        super.onSuccess(baseResponse);
                                        if (baseResponse.getCode() == 1) {
                                            addrTv.setText(obj.toString());
                                            UserData.getUserData().setRegion(obj.toString());
                                        }
                                        ToastUtil.showText(baseResponse.getMessage());
                                    }

                                    @Override
                                    public void onFailure(int code, String message) {
                                        super.onFailure(code, message);
                                        ToastUtil.showText(message);
                                    }
                                });
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
            case R.id.my_person_nation_layout:
                Intent nation = new Intent();
                nation.setClass(this, PersonRefValueActivity.class);
                nation.putExtra("title", "民族");
                nation.putExtra("nameTv", "民族");
                nation.putExtra("nameValue", UserData.getUserData().getNation());
                startActivityForResult(nation, PERSON_REF_NATION);
                break;
            case R.id.my_person_qr:
                Intent qr = new Intent();
                qr.setClass(this, PersonQrActivity.class);
                startActivity(qr);
                break;
            case R.id.my_person_signature_layout:
                Intent signature = new Intent();
                signature.setClass(this, PersonRefValueActivity.class);
                signature.putExtra("title", "个性签名");
                signature.putExtra("nameTv", "个性签名");
                signature.putExtra("nameValue", UserData.getUserData().getSignature());
                startActivityForResult(signature, PERSON_REF_SIGNATURE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERSON_REF_NICKNAME && resultCode == RESULT_OK) {
            nicknameTv.setText(data.getStringExtra("result"));
            setResult(RESULT_OK);
        } else if (requestCode == PERSON_REF_SEX && resultCode == RESULT_OK) {
            sexTv.setText(data.getStringExtra("result"));
        } else if (requestCode == PERSON_REF_AGE && resultCode == RESULT_OK) {
            ageTv.setText(data.getStringExtra("result"));
        } else if (requestCode == PERSON_REF_TEL && resultCode == RESULT_OK) {
            telTv.setText(data.getStringExtra("result"));
        } else if (requestCode == PERSON_REF_NATION && resultCode == RESULT_OK) {
            nationTv.setText(data.getStringExtra("result"));
        } else if (requestCode == PERSON_REF_SIGNATURE && resultCode == RESULT_OK) {
            signatureTv.setText(data.getStringExtra("result"));
            setResult(RESULT_OK);
        } else if (requestCode == PERSON_REF_EDU && resultCode == RESULT_OK) {
            eduTv.setText(data.getStringExtra("result"));
        } else if (requestCode == REQUEST_CODE_PHOTO && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            final ArrayList<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (items != null && items.size() > 0) {
                try {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", UserData.getUserData().getId() + "");
                    List<File> fileList = new ArrayList<>();
                    fileList.add(new File(items.get(0).path));
                    OkHttpUtils.postAsynFiles(NetWorkConfig.USER_UPDATEINFO, "file", fileList, params, BaseResponse.class, new HttpCallback() {
                        @Override
                        public void onSuccess(BaseResponse br) {
                            super.onSuccess(br);
                            if (br.getCode() == 1) {
                                setResult(RESULT_OK);
                            }
                            photoImg.setImageBitmap(ImageUtils.decodeBitmap(items.get(0).path));
                            ToastUtil.showText(br.getMessage());
                        }

                        @Override
                        public void onFailure(int code, String message) {
                            super.onFailure(code, message);
                            ToastUtil.showText(message);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtil.d("onActivityResult path=" + items.get(0).path);
            }
        }
    }
}
