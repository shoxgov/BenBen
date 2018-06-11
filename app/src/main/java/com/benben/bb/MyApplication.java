package com.benben.bb;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.wxlib.util.SysUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.benben.bb.bean.AccountInfoData;
import com.benben.bb.bean.UserData;
import com.benben.bb.okhttp3.http.Https;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.PreferenceUtil;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.tencent.bugly.Bugly;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    public final static String ADDRESS_CITY_ACTION = "com.benben.bb.action.city";
    /**
     * 阿里百川客服
     * 测试账号 ltx001，ltx002，ltx003  pwd：123456
     * 如果您已经有了百川appkey，请将appkey请替换成您自己的百川appkey
     * 您在淘宝开放平台成功创建老同学应用，您的Appkey 和 Appsecret如下：
     App Key： 24614751
     App Secret： 4b311f0102f3c11d891ecd378156c8bd
     云旺客服设置
     已开通云旺客服账号:老同学平台
     */
    public final static String YW_APP_KEY = "24614751";
    public final static String YW_APP_COMM_PWD = "zbb123.0admin";
    public static Context mContext;
    //全局保存用户信息
    public static AccountInfoData userData = new AccountInfoData();
    private static List<Activity> activityList = new ArrayList<>();
    public static int screenWidthPixels = 400;
    public static int screenHeightPixels = 800;
    /**
     * 高德地图定位
     */
//声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    //private AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    /**
     * 保存当前城市名
     */
    public static String currentCityName = "";

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化方法的调用必须放在Application onCreate方法中
        //必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
        SysUtil.setApplication(this);
        if(SysUtil.isTCMSServiceProcess(this)){
            return;
        }
//第一个参数是Application Context
//这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
        if(SysUtil.isMainProcess()){
            YWAPI.init(this, YW_APP_KEY);
        }
        //end alipay baichuang
        mContext = getApplicationContext();
        //Bugly升级
//        Beta.canShowUpgradeActs.add(MainFragmentActivity.class);
//        Beta.canShowUpgradeActs.add(MainStudentsFragmentActivity.class);
        Bugly.init(getApplicationContext(), "b3c8fc1de5", true);
        //友盟 统计分析  用了Bugly 屏敝
        //MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, "58b644c22ae85b5645001992", "laotongxue-Android", MobclickAgent.EScenarioType.E_UM_NORMAL, true));
        //友盟分享
        // 开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
        //各个平台的配置，建议放在全局Application或者程序入口
        PlatformConfig.setWeixin("wx6ab0128193e89c8e", "a3a2dfd2c643125f2c10565c34fd3d8d");//微信后台注册签名：cf571acd59eb69c09ce7dd690e3d7ca5
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("1541150674", "3c48508f14a4f92f84c667538ffc9384", "http://sns.whalecloud.com/sina2/callback");
//        Config.REDIRECT_URL="http://sns.whalecloud.com/sina2/callback";
        PlatformConfig.setQQZone("1106115687", "WhTiVpf3zSggWslI");
        //---end 友盟
        initOkHttp();
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * @Description 初始化OkHttp
     */
    private void initOkHttp() {
        File cache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));
        Https.SSLParams sslParams = Https.getSslSocketFactory(null, null, null);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)//连接超时(单位:秒)
                .writeTimeout(20, TimeUnit.SECONDS)//写入超时(单位:秒)
                .readTimeout(20, TimeUnit.SECONDS)//读取超时(单位:秒)
                .pingInterval(20, TimeUnit.SECONDS) //websocket轮训间隔(单位:秒)
                .cache(new Cache(cache.getAbsoluteFile(), cacheSize))//设置缓存
                .cookieJar(cookieJar)//Cookies持久化
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)//https配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        }
    }

    public void startBD() {
        //启动定位
        mLocationClient.startLocation();
    }

    public void stopBD() {
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    //http://lbs.amap.com/api/android-location-sdk/guide/android-location/getlocation
    public void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2500);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    StringBuilder location = new StringBuilder("");
                    location.append("\nLocationType=" + amapLocation.getLocationType());//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    location.append("\nLatitude=" + amapLocation.getLatitude());//获取纬度
                    location.append("\nLongitude=" + amapLocation.getLongitude());//获取经度
                    location.append("\nAccuracy=" + amapLocation.getAccuracy());//获取精度信息
                    location.append("\nAddress=" + amapLocation.getAddress());//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    location.append("\nCountry=" + amapLocation.getCountry());//国家信息
                    location.append("\nProvince=" + amapLocation.getProvince());//省信息
                    location.append("\nCity=" + amapLocation.getCity());//城市信息
                    location.append("\nDistrict=" + amapLocation.getDistrict());//城区信息
                    location.append("\nStreet=" + amapLocation.getStreet());//街道信息
                    location.append("\nStreetNum=" + amapLocation.getStreetNum());//街道门牌号信息
                    location.append("\nCityCode=" + amapLocation.getCityCode());//城市编码
                    location.append("\nAdCode=" + amapLocation.getAdCode());//地区编码
                    location.append("\nAoiName=" + amapLocation.getAoiName());//获取当前定位点的AOI信息
                    location.append("\nBuildingId=" + amapLocation.getBuildingId());//获取当前室内定位的建筑物Id
                    location.append("\nFloor=" + amapLocation.getFloor());//获取当前室内定位的楼层
                    //获取定位时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);
                    location.append("\nDate=" + date);
                    Log.d("GAODEDITU","amapLocation:" + location.toString());
                    if (!TextUtils.isEmpty(amapLocation.getCity())) {
                        Intent intent = new Intent();
                        //设置Action
                        intent.setAction(ADDRESS_CITY_ACTION);
                        //携带数据
                        currentCityName = amapLocation.getCity();
                        intent.putExtra("city", currentCityName);
                        //发送广播
                        sendBroadcast(intent);
                    }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogUtil.d("location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    /**
     * @Description 添加Activity到activityList，在onCreate()中调用
     */
    public static void addActivity(Activity activity) {
        if (activityList != null && activityList.size() > 0) {
            if (!activityList.contains(activity)) {
                activityList.add(activity);
            }
        } else {
            activityList.add(activity);
        }
    }

    /**
     * @Description 结束Activity到activityList，在onDestroy()中调用
     */
    public static void finishActivity(Activity activity) {
        if (activity != null && activityList != null && activityList.size() > 0) {
            activityList.remove(activity);
        }
    }

    /**
     * @Description 结束所有Activity
     */
    public static void finishAllActivity() {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (null != activity) {
                    activity.finish();
                }
            }
        }
        activityList.clear();
    }

}
