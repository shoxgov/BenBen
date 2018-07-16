package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.benben.bb.R;
import com.benben.bb.dialog.WarnDialog;
import com.benben.bb.fragment.FragmentFactory;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.imp.TitleBarListener;
import com.benben.bb.utils.GlideCacheUtils;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.PreferenceUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.view.TitleBar;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-10.
 * email:shoxgov@126.com
 */

public class SettingActivity extends BaseActivity {

    @Bind(R.id.setting_clear_txt)
    TextView clearTv;
    @Bind(R.id.setting_version_code)
    TextView versionCodeTv;
    @Bind(R.id.setting_update)
    TextView versionUpdateTv;
    int versionCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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

        try {
            versionCode = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Beta.getUpgradeInfo() == null) {
            LogUtil.d("Beta.getUpgradeInfo = null");
            versionUpdateTv.setText("检查更新(当前已是最新版本)");
        } else if (versionCode < Beta.getUpgradeInfo().versionCode) {
            versionUpdateTv.setText("检查更新");
        }
        LogUtil.d("versionCode=" + versionCode);
        String versionName = null;
        try {
            versionName = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(versionName)) {
            versionCodeTv.setText("版本号: " + versionName);
        }

        clearTv.setText("清理缓存(" + GlideCacheUtils.getInstance().getCacheSize(this) + ")");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.setting_reputation, R.id.setting_clear, R.id.setting_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_reputation:
                break;
            case R.id.setting_update:
                try {
                    if (Beta.getUpgradeInfo().versionCode > versionCode) {
                        Beta.checkUpgrade();//检查版本号
                    } else {
                        ToastUtil.showText("当前已是最新版本");
                    }
                } catch (Exception e) {
                    Beta.checkUpgrade();//检查版本号
                }
                break;
            case R.id.setting_clear:
                String warn = "清除缓存将会清空所有的聊天记录和图片，您确定要清空吗？";
                WarnDialog warnDialogs = new WarnDialog(SettingActivity.this, warn, new DialogCallBack() {
                    @Override
                    public void OkDown(Object score) {
                        GlideCacheUtils.getInstance().clearImageAllCache(SettingActivity.this);
                        ToastUtil.showText("清除成功！");
                        clearTv.setText("清理缓存");
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                warnDialogs.show();
                break;
            case R.id.setting_quit:
                WarnDialog warnDialog = new WarnDialog(this, "确定退出当前账户？", new DialogCallBack() {
                    @Override
                    public void OkDown(Object obj) {
                        PreferenceUtil.removeAll();
                        FragmentFactory.clear();
                        setResult(RESULT_OK);
                        finish();
                        Intent i = new Intent();
                        i.setClass(SettingActivity.this, LoginActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void CancleDown() {

                    }
                });
                warnDialog.show();
                break;
        }
    }
}
