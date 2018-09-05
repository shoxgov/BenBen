package com.benben.bb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.benben.bb.MyApplication;
import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.dialog.RealnameCertifyDialog;
import com.benben.bb.fragment.EmployFragment;
import com.benben.bb.fragment.FragmentFactory;
import com.benben.bb.fragment.HomeFragment;
import com.benben.bb.fragment.MessageFragment;
import com.benben.bb.fragment.MyFragment;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.LoginResponse;
import com.benben.bb.service.MQService;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.PreferenceUtil;
import com.benben.bb.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class MainFragmentActivity extends BaseFragmentActivity {

    @Bind(R.id.rb_home)
    TextView tvBottomNav1;
    @Bind(R.id.rb_employ)
    TextView tvBottomNav2;
    @Bind(R.id.rb_msg)
    TextView tvBottomNav3;
    @Bind(R.id.rb_my)
    TextView tvBottomNav4;
    private FragmentManager mFragmentManager;
    private Fragment homeFragment;
    private Fragment employFragment;
    private Fragment msgFragment;
    private Fragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("MainFragmentActivity onCreate");
        setContentView(R.layout.activity_main);
        PreferenceUtil.init(this);
        ButterKnife.bind(this);
        Utils.verifyStoragePermissions(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        MyApplication.screenWidthPixels = dm.widthPixels;
        MyApplication.screenHeightPixels = dm.heightPixels;

//        MyApplication app = (MyApplication) getApplication();
//        app.initLocation();
//        app.startBD();
        judeFirst();
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            homeFragment = FragmentFactory.createFragment(FragmentFactory.F1);
            selectFragment(homeFragment);
            LogUtil.d("MainFragmentActivity savedInstanceState is  null");
            startServer();
        } else {
            position = savedInstanceState.getInt("position");
            Map<String, String> params = new HashMap<String, String>();
            params.put("phone", PreferenceUtil.getString("LoginTel", ""));
            params.put("passWord", PreferenceUtil.getString("passWord", ""));
            OkHttpUtils.getAsyn(NetWorkConfig.LOGIN, params, LoginResponse.class, new HttpCallback() {
                @Override
                public void onSuccess(BaseResponse br) {
                    super.onSuccess(br);
                    LoginResponse lr = (LoginResponse) br;
                    if (lr.getCode() == 1) {
                        UserData.updateAccount(lr);
                    }
                    preventOverlap();
                }

                @Override
                public void onFailure(int code, String message) {
                    super.onFailure(code, message);
                    preventOverlap();
                }
            });
        }
    }

    private void startServer() {
        Intent server = new Intent(getBaseContext(), MQService.class);
        server.putExtra("command", MQService.START_YUNIM);
        server.putExtra("openIm", UserData.getUserData().getBenbenNum());
//        bindService(server,conn);
        startService(server);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void judeFirst() {
        PreferenceUtil.init(this);
        boolean isFirst = PreferenceUtil.getBoolean("IsFirstMain", true);
        if (!isFirst) {
            return;
        }
        if (UserData.getUserData().getValidateStatus() == 0) {//0未认证3认证中2认证失败1已通过
            PreferenceUtil.commitBoolean("IsFirstMain", false);
            RealnameCertifyDialog realnameCertifyDialog = new RealnameCertifyDialog(this, "前去完成实名认证", new DialogCallBack() {
                @Override
                public void OkDown(Object obj) {
                    Intent realname = new Intent();
                    realname.setClass(MainFragmentActivity.this, RealNameCertifyActivity.class);
                    realname.putExtra("status", "0");
                    startActivity(realname);
                }

                @Override
                public void CancleDown() {

                }
            });
            realnameCertifyDialog.show();
        }
    }

    /**
     * 防止Fragment重叠的问题，切换横竖屏或者内存不够，重走了Activity的生命周期
     * 第一次要createFragment
     * 重启以后，要通过tag来找到对应的Fragment
     * <p>
     * 重启的标准是savedInstanceState是否为null
     * 因为重启之前savedInstanceState=null
     * 重启之后savedInstanceState！=null
     */
    private void preventOverlap() {
//        如果savedInstanceState为空，证明没有发生重走Activity的生命周期的情况，这时候要创建createFragment
        LogUtil.d("MainFragmentActivity savedInstanceState is not null");
//            使用mFragmentManager通过Tag来取得，只要他add过，就给他添加了Tag
//            否则直接重写创建一个Fragment的话,会导致重叠
        homeFragment = mFragmentManager.findFragmentByTag(HomeFragment.class.getName());
        employFragment = mFragmentManager.findFragmentByTag(EmployFragment.class.getName());
        msgFragment = mFragmentManager.findFragmentByTag(MessageFragment.class.getName());
        myFragment = mFragmentManager.findFragmentByTag(MyFragment.class.getName());
//=======================以下代码，不要系统也会自动识别，上次死亡位置，但是除了死亡位置，savedInstanceState还可以传递其他数据============
//            获得上次死亡重启的位置
        LogUtil.d(position + "=======");
        switch (position) {
            case 1:
                tvBottomNav1.performClick();
//                    selectFragment(homeFragment);
                break;
            case 2:
                tvBottomNav2.performClick();
//                    selectFragment(employFragment);
                break;
            case 3:
                tvBottomNav3.performClick();
//                    selectFragment(msgFragment);
                break;
            case 4:
                tvBottomNav4.performClick();
//                    selectFragment(myFragment);
                break;
        }
        if (homeFragment != null) {
            ((HomeFragment) homeFragment).freshUI();
        }
        if (employFragment != null) {
            ((EmployFragment) employFragment).freshUI();
        }
        if (msgFragment != null) {
            ((MessageFragment) msgFragment).freshUI();
        }
        if (myFragment != null) {
            ((MyFragment) myFragment).freshUI();
        }
        startServer();
//===================以上代码需要配合onSaveInstanceState（）方法里面记录数据================
    }

    @OnClick({R.id.rb_home, R.id.rb_employ, R.id.rb_msg, R.id.rb_my})
    public void onViewClicked(View view) {
        LogUtil.d("onViewClicked");
        switch (view.getId()) {
            case R.id.rb_home:
                if (homeFragment == null) {
                    homeFragment = FragmentFactory.createFragment(FragmentFactory.F1);
                }
                selectFragment(homeFragment);
                position = 1;
                break;
            case R.id.rb_employ:
                if (employFragment == null) {
                    employFragment = FragmentFactory.createFragment(FragmentFactory.F2);
                }
                selectFragment(employFragment);
                position = 2;
                break;
            case R.id.rb_msg:
                if (msgFragment == null) {
                    msgFragment = FragmentFactory.createFragment(FragmentFactory.F3);
                }
                selectFragment(msgFragment);
                position = 3;
                break;
            case R.id.rb_my:
                if (myFragment == null) {
                    myFragment = FragmentFactory.createFragment(FragmentFactory.F4);
                }
                selectFragment(myFragment);
                position = 4;
                break;
        }
    }

    public void changeFragment(int page) {
        switch (page) {
            case 0:
                tvBottomNav1.performClick();
                break;
            case 1:
                tvBottomNav2.performClick();
                break;
            case 2:
                tvBottomNav3.performClick();
                break;
            case 3:
                tvBottomNav4.performClick();
                break;
        }
    }

    /**
     * 选择显示某一个Fragment
     *
     * @param fragment
     */
    private void selectFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAll(transaction);
        if (!fragment.isAdded()) {
            transaction.add(R.id.fl_content, fragment, fragment.getClass().getName());//tag
//            transaction.addToBackStack(null);//加入回退栈
        }
        transaction.show(fragment).commit();
    }

    private void hideAll(FragmentTransaction transaction) {
        if (homeFragment != null) {
//            必须使用同一个transaction，跟add/show时候一样
//            同一个事物进行了所有的add/hide/show，之后统一commit就行了
//            中间不能commit事物，因为一个事物只能是提交一次，重复提交会报错
//            即：每一次点击，生成一个事务，操作完后，提交这个事务
            transaction.hide(homeFragment);
        }
        if (employFragment != null) {
            transaction.hide(employFragment);
        }
        if (msgFragment != null) {
            transaction.hide(msgFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    //记录Fragment的位置
    private int position;

    /**
     * 这个方法会在activity重启前调用，用来保存一些数据
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //记录当前的position
        outState.putInt("position", position);
        LogUtil.d(position + ">>>>onSaveInstanceState");
//        ★★★★★★★★别忘了下下面的super.onSave，否则会报错
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            WarnDialog warnDialog = new WarnDialog(MainFragmentActivity.this, "确定退出应用？", new DialogCallBack() {
//                @Override
//                public void OkDown(Object obj) {
            MyApplication.finishAllActivity();
            finish();
//                    try {
//                        int id = android.os.Process.myPid();
//                        android.os.Process.killProcess(id);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void CancleDown() {
//                }
//            });
//            warnDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}