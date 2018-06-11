package com.benben.bb.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.benben.bb.MyApplication;
import com.benben.bb.R;
import com.benben.bb.imp.DialogCallBack;
import com.benben.bb.view.threeWheelView.WheelThreeItemView;
import com.benben.bb.view.threeWheelView.adapter.ArrayWheelAdapter;
import com.benben.bb.view.threeWheelView.listener.OnWheelChangedListener;
import com.benben.bb.view.threeWheelView.model.CityModel;
import com.benben.bb.view.threeWheelView.model.DistrictModel;
import com.benben.bb.view.threeWheelView.model.ProvinceModel;
import com.benben.bb.view.threeWheelView.service.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ThreeWheelViewDialogs extends Dialog implements OnWheelChangedListener {

    private Context context;
    private DialogCallBack dialogcallback;
    private String cancle;
    private WheelThreeItemView mViewProvince;
    private WheelThreeItemView mViewCity;
    private WheelThreeItemView mViewDistrict;


    ///////////////////////////////
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    public ThreeWheelViewDialogs(Context context, DialogCallBack dialogcallback) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
        this.dialogcallback = dialogcallback;
    }

    public ThreeWheelViewDialogs(Context context, String cancle, DialogCallBack dialogcallback) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
        this.cancle = cancle;
        this.dialogcallback = dialogcallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bottom_three_wheels);
        setCanceledOnTouchOutside(false);
        setUpViews();
        setUpListener();
        setUpData();
        Button okBtn = (Button) findViewById(R.id.dialog_wheel_ok);
        if (!TextUtils.isEmpty(cancle)) {
            okBtn.setText(cancle);
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogcallback != null) {
                    dialogcallback.OkDown(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
                }
                dismiss();
            }
        });
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        //设置Dialog从窗体底部弹出
        window.setLayout(MyApplication.screenWidthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void setUpViews() {
        mViewProvince = (WheelThreeItemView) findViewById(R.id.id_province);
        mViewCity = (WheelThreeItemView) findViewById(R.id.id_city);
        mViewDistrict = (WheelThreeItemView) findViewById(R.id.id_district);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        //mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelThreeItemView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    @Override
    public void onBackPressed() {
        dismiss();
        super.onBackPressed();
    }


    /**
     * 解析省市区的XML数据
     */
    private void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        try {
            InputStream input = context.getResources().openRawResource(R.raw.province_data);
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

}
