package com.benben.bb.utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.benben.bb.NetWorkConfig;
import com.benben.bb.R;
import com.benben.bb.bean.UserData;
import com.benben.bb.okhttp3.http.HttpCallback;
import com.benben.bb.okhttp3.http.OkHttpUtils;
import com.benben.bb.okhttp3.response.BaseResponse;
import com.benben.bb.okhttp3.response.LoginResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 手机号码识别
     */
    public static boolean isMobliePhone(String phone) {
        String regExp = "^[1]([3|4|5|7|8|9][0-9]{1})[0-9]{8}$";//17773119860
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.find();//boolean
    }


    // 判断网络是否正常
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    // 判断网络是否连接上
    public static boolean checkNetworkInfo(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (mobile == State.CONNECTED || mobile == State.CONNECTING)
            return true;
        if (wifi == State.CONNECTED || wifi == State.CONNECTING)
            return true;
        return false;
    }

    // 判断3G网络是否正常
    public static boolean is3GNetworkConnected(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        // 检查网络连接，如果无网络可用，就不需要进行连网操作等
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return false;
        }
        // 判断网络连接类型，只有在3G或wifi里进行一些数据更新。
        int netType = info.getType();
        int netSubtype = info.getSubtype();
        if (netType == ConnectivityManager.TYPE_MOBILE
        /*
         * && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS &&
		 * !mTelephony.isNetworkRoaming()
		 */) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    /**
     * 防止按钮连续点击
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * @param ipInt
     * @return 返回类型 String
     * @throws
     * @Title: int2ip
     * @Description: TODO(将ip的整数形式转换成ip形式)
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }


    /**
     * @return 返回类型 String
     * @throws
     * @Title: getNonceStr
     * @Description: TODO(随机字符串)
     */
    public static String getNonceStr() {
        Random random = new Random();
        return getMd5(String.valueOf(random.nextInt(10000)).getBytes());
    }

    public static String getMd5(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String[] getPhoneContacts(Context context, Uri uri) {
        String[] contact = new String[2];
        // 得到ContentResolver对象
        ContentResolver cr = context.getContentResolver();
        // 取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            Cursor phone = null;
            try {
                cursor.moveToFirst();
                // 取得联系人姓名
                int nameFieldColumnIndex = cursor
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                contact[0] = cursor.getString(nameFieldColumnIndex);
                // 取得电话号码
                String ContactId = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));
                phone = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + "=" + ContactId, null, null);
                if (phone != null) {
                    phone.moveToFirst();
                    contact[1] = phone
                            .getString(phone
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (phone != null) {
                    phone.close();
                }
                cursor.close();
            }
        } else {
            return null;
        }
        return contact;
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isGPSOpen(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public static void closeInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.hideSoftInputFromWindow(EditText.getWindowToken(), 0);
    }


    /**
     * 要转换的地址或者字符串,可以是中文
     */
    public static Bitmap createQRImage(Context context, String url, String saveFileName) throws Exception {
        Bitmap bmp = null;
        try {
            // 判断URL的合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return bmp;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            // hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 设置二维码和边框的距离
            hints.put(EncodeHintType.MARGIN, 1);
            // 设置二维码的容错率,容错率等级越高,二维码的黑色密度越大
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);

            // 图像数据转换,使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, 500, 500, hints);
            int[] pixels = new int[500 * 500];
            // 下面这里按照二维码的算法,诸葛生成二维码的图片
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < 500; y++) {
                for (int x = 0; x < 500; x++) {
                    // 取出这点判断是白色还是黑色
                    if (bitMatrix.get(x, y)) {
                        pixels[y * 500 + x] = 0xff000000;
                    } else {
                        pixels[y * 500 + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式,使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, 500, 0, 0, 500, 500);
            // //给二维码加个logo
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 1;   // width，hight设为原来的十分一
            Bitmap logoBmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher, options);
            float scaleFactor = bitmap.getWidth() * 1.0f / 4 / logoBmp.getWidth();
            //
            Canvas canvas = new Canvas(bitmap);
            // 调整图片大小
            canvas.scale(scaleFactor, scaleFactor, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

            canvas.drawBitmap(logoBmp, bitmap.getWidth() / 2 - logoBmp.getWidth() / 2,
                    bitmap.getHeight() / 2 - logoBmp.getHeight() / 2, null);


            bmp = bitmap;
            // 保存二维码图到手机内存里
            if (!TextUtils.isEmpty(saveFileName)) {
                saveCode(bitmap, saveFileName);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * 保存二维码
     *
     * @param bitmap
     */
    private static void saveCode(Bitmap bitmap, String saveFileName) {
        // 创建文件夹
//        String fileName = "myShareCode.jpg";
        String path = Environment.getExternalStorageDirectory() + "/ltx/";

        File myFile = new File(path);
        if (!myFile.exists()) {
            // 必须前面的目录存在才能生成后面的目录
            myFile.mkdir();
        }
        File file = new File(path + saveFileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            // 80为压缩率，压缩20%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateUserInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", PreferenceUtil.getString("LoginTel", ""));
        params.put("logCode", PreferenceUtil.getString("LoginCode", ""));
        OkHttpUtils.getAsyn(NetWorkConfig.LOGIN, params, LoginResponse.class, new HttpCallback() {
            @Override
            public void onSuccess(BaseResponse br) {
                super.onSuccess(br);
                try {
                    LoginResponse lr = (LoginResponse) br;
                    UserData.updateAccount(lr);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 100;

    public static void verifyStoragePermissions(Activity activity) {
        int permissionWrite = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_EXTERNAL_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
