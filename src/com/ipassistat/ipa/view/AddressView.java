package com.ipassistat.ipa.view;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.local.NativeAddressModel;
import com.ipassistat.ipa.bean.local.NativeAreaModel;
import com.ipassistat.ipa.bean.local.NativeCityModel;
import com.ipassistat.ipa.bean.local.NativeProvincesModel;
import com.ipassistat.ipa.util.FileUtis;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.wheelview.OnWheelChangedListener;
import com.ipassistat.ipa.view.wheelview.WheelView;
import com.ipassistat.ipa.view.wheelview.adapters.ArrayWheelAdapter;

/**
 * 
 * @author RenHeng
 * 
 */
public class AddressView extends LinearLayout implements OnWheelChangedListener,OnClickListener{

	private String TAG="AAA";
	private OnAddressCheckedListener listener;
	private Context context;
	private LayoutInflater mInflater;
	private List<NativeProvincesModel> mProvList;
	private List<NativeCityModel> mCityList;
	private List<NativeAreaModel> mAreaList;
	
	private WheelView mCityWheel;
	private WheelView mProvinceWheel;
	private WheelView mAreaWheel;
	//View view;
	
	private String[] mProvinceData; // 省份名字
	private String[] mCityData; // 城市名字
	private String[] mAreaData; // 城区名字
	private int mItemCount = 5;
	private int mDefaultItem=0; //wheelview默认显示第一条item
	
	private String mCurrentProv; // 当前选择的省份
	private String mCurrentCity; // 当前选择的城市
	private String mCurrentArea; // 当前选择的城区
	private String mAreaCode; // 当前选择的城区编码
	private String mIsDefaultStr; // 是否默认字符串
	
	private Button mSaveBtn;
	
	
	public AddressView(Context context, OnAddressCheckedListener listener) {
		super(context);
		init(context, listener);
	}
	

	public AddressView(Context context, AttributeSet attrs, int defStyle, OnAddressCheckedListener listener) {
		super(context, attrs, defStyle);
		init(context, listener);
	}

	public AddressView(Context context, AttributeSet attrs, OnAddressCheckedListener listener) {
		super(context, attrs);
		init(context, listener);
	}

	public interface OnAddressCheckedListener {

		/**
		 * 检查格式成功
		 */
		void checkedSuccess();  
		
		/**
		 * 
		 * @param provName 省份名字
		 * @param cityName   城市名字
		 * @param areaName  城区名字
		 * @param areaCode   区id
		 */
		void getCurrentDataByConfirm(String provName, String cityName, String areaName, String areaCode);
		
		
	}

	public void checkAddress(String name, String city, String district,
			String phone, String postCode) {

		if ("".equals(name.trim())) {
			ToastUtil.showToast(context, "收件人不能为空");
		} else if ("".equals(phone.trim())) {
			ToastUtil.showToast(context, "手机号不能为空");
		} else if ("".equals(postCode.trim())) {
			ToastUtil.showToast(context, "邮政编码不能为空");
		} else if ("".equals(city.trim())) {
			ToastUtil.showToast(context, "省市区不能为空");
		} else if ("".equals(district.trim())) {
			ToastUtil.showToast(context, "详细地址不能为空");
		} else if (!StringUtil.isPhoneNo(phone.trim())) {
			ToastUtil.showToast(context, "请输入正确的手机号");
		} else if (!StringUtil.isPostCode(postCode.trim())) {
			ToastUtil.showToast(context, "请输入正确的邮政编码");
		} else {
			listener.checkedSuccess();
		}
	}
	
	private NativeAddressModel getAddrData() {
		try {
			String str= FileUtis.convertStreamToString(context.getAssets().open(
					"area.json"));
			Gson gson=new Gson();
			NativeAddressModel model=gson.fromJson(str, NativeAddressModel.class);
			return model;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void init(Context context, OnAddressCheckedListener listener){
		this.context=context;
		this.listener=listener;
		mInflater=LayoutInflater.from(context);
		mInflater.inflate(R.layout.item_address_select, this);
		mCityWheel = (WheelView)findViewById(R.id.city);
		mProvinceWheel = (WheelView)findViewById(R.id.province);
		mAreaWheel = (WheelView)findViewById(R.id.area);
		mSaveBtn=(Button) findViewById(R.id.confirm);
		mProvinceWheel.addChangingListener(this);
		mCityWheel.addChangingListener(this);
		mAreaWheel.addChangingListener(this);
		mSaveBtn.setOnClickListener(this);
		initData();
	}
	
	/*
	 * 初始化数据
	 */
	private void initData(){
		NativeAddressModel m1=getAddrData();
		mProvList = m1.list;
		
		NativeProvincesModel m2 =mProvList.get(mDefaultItem);
		mCurrentProv=m2.provinceName;  //默认的省份名字
		mCityList = m2.cityList;
		
		NativeCityModel  m3= mCityList.get(mDefaultItem);
		mCurrentCity = m3.cityName;
		mAreaList = m3.districtList;
		
		NativeAreaModel m4 = mAreaList.get(mDefaultItem);
		mCurrentArea = m4.district;
		mAreaCode = m4.districtID;
		
		showProvView(mProvList);
		showCityViewByProv(mCityList);
		showAreaViewByCity(mAreaList);
		
	}

	/*
	 * 显示省份的wheelview
	 */
	private void showProvView(List<NativeProvincesModel> list) {

		mProvinceData = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			mProvinceData[i] = list.get(i).provinceName;
		}
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(context,
				mProvinceData);
		mProvinceWheel.setViewAdapter(adapter);
		mProvinceWheel.setVisibleItems(mItemCount);
		mProvinceWheel.setCurrentItem(mDefaultItem);
	}
	
	/*
	 * 根据省份显示城市的wheelview
	 */
	private void showCityViewByProv(List<NativeCityModel> list) {
		mCityData = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			mCityData[i] = list.get(i).cityName;
		}
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(context,
				mCityData);
		mCityWheel.setViewAdapter(adapter);
		mCityWheel.setVisibleItems(mItemCount);
		mCityWheel.setCurrentItem(mDefaultItem);

	}
	
	/*
	 * 根据城市显示城区的wheelview
	 */
	private void showAreaViewByCity(List<NativeAreaModel> list) {
		mAreaData = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			mAreaData[i] = list.get(i).district;
		}
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(context,
				mAreaData);
		mAreaWheel.setViewAdapter(adapter);
		mAreaWheel.setVisibleItems(mItemCount);
		mAreaWheel.setCurrentItem(mDefaultItem);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		
		if (wheel == mProvinceWheel) {
			int currentItem = mProvinceWheel.getCurrentItem();
			String currentId = mProvList.get(currentItem).provinceID;
			mCurrentProv = mProvList.get(currentItem).provinceName;
			Log.d(TAG, currentId);
			getCitysDataByProvId(currentId);
		} else if (wheel == mCityWheel) {
			int currentItem = mCityWheel.getCurrentItem();
			String currentId = mCityList.get(currentItem).cityID;
			mCurrentCity = mCityList.get(currentItem).cityName;
			getAreasDataByCityId(currentId);
		} else if (wheel == mAreaWheel) {
			int currentItem = mAreaWheel.getCurrentItem();
			mCurrentArea = mAreaList.get(currentItem).district;
			mAreaCode = mAreaList.get(currentItem).districtID;
		}
	}
	
	/*
	 * 根据省份ID获取城市数据
	 */
	private void getCitysDataByProvId(String provinceID) {

		NativeProvincesModel selectProvinces = null;

		for (NativeProvincesModel provinces : mProvList) {
			if (provinces.provinceID.equals(provinceID)) {
				selectProvinces = provinces;
				break;
			}
		}
		mCityList = selectProvinces.cityList;
		showCityViewByProv(mCityList);
		// 显示默认市区的城区
		getAreasDataByCityId(mCityList.get(mDefaultItem).cityID);
		mCurrentCity = mCityList.get(mDefaultItem).cityName;
	}

	/*
	 * 根据城市ID获取城区数据
	 */
	private void getAreasDataByCityId(String cityId) {

		NativeCityModel selectCity = null;

		for (NativeCityModel city : mCityList) {
			if (city.cityID.equals(cityId)) {
				selectCity = city;
				break;
			}
		}
		mAreaList = selectCity.districtList;
		showAreaViewByCity(mAreaList);
		mCurrentArea = mAreaList.get(mDefaultItem).district;
		mAreaCode = mAreaList.get(mDefaultItem).districtID;
	}

	@Override
	public void onClick(View v) {
		
		if(listener!=null){
			listener.getCurrentDataByConfirm(mCurrentProv, mCurrentCity, mCurrentArea, mAreaCode);
		}
	}
	
}
