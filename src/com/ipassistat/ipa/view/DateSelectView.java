package com.ipassistat.ipa.view;

import java.util.Calendar;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.view.wheelview.OnWheelChangedListener;
import com.ipassistat.ipa.view.wheelview.WheelView;
import com.ipassistat.ipa.view.wheelview.adapters.ArrayWheelAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


public class DateSelectView extends LinearLayout implements OnWheelChangedListener,OnClickListener{
	private Button mConfirmBtn,mCancelBtn;//确认按钮
	private WheelView mYearWheel;//年份滚轮
	private WheelView mMonthWheel;//月份滚轮
	private WheelView mDayWheel;//日期滚轮
	private String[] mYearData; // 年份数组
	private String[] mMonthData;//月份数组
	private String[] mDayData_thirtyone; // 日期31号数组
	private String[] mDayData_thirty;//日期30号数组
	private String[] mDayData_twintynine;//日期29号数组
	private String[] mDayData_twintyeight;//日期28号数组
	
	private String YearDate,MonthDate,DayDate;//年份数值，月份数值，日期数值
	private OnDateSelectListener listener;//自定义的一个接口
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayWheelAdapter<String> dayThirthone_adapter;
	private ArrayWheelAdapter<String> dayThirty_adapter;
	private ArrayWheelAdapter<String> dayTwintynine_adapter;
	private ArrayWheelAdapter<String> dayTwintyeight_adapter;
	private int targetYear=2020, startYear = 1940;
	private int mShowDefaultYear=1990, mShowDefaultYearPosition=0; //默认显示的年份及该年份在数组中的position
	public DateSelectView(Context context,OnDateSelectListener listener) {
		super(context);
		initView(context);
		registerListener(listener);
	}

	public DateSelectView(Context context, AttributeSet attrs, int defStyle, OnDateSelectListener listener) {
		super(context, attrs, defStyle);
		initView(context);
		registerListener(listener);
	}

	public DateSelectView(Context context, AttributeSet attrs, OnDateSelectListener listener) {
		super(context, attrs);
		initView(context);
		registerListener(listener);
	}

	
	private void initView(Context context) {
		this.mContext = context;
		mInflater=LayoutInflater.from(context);
		mInflater.inflate(R.layout.item_date_select, this);
		mYearWheel = (WheelView) findViewById(R.id.date_year_wheel);//年份滚轮
		mMonthWheel = (WheelView) findViewById(R.id.date_month_wheel);//月份滚轮
		mDayWheel = (WheelView) findViewById(R.id.date_day_wheel);//日期滚轮
		mConfirmBtn = (Button) findViewById(R.id.confirm_btn);//确认按钮
		mCancelBtn = (Button) findViewById(R.id.cancel_btn);//取消按钮
		initDate();//初始化数据
	}
	private void initDate() {
	//	mYearData = new String[]{"2015年","2016年","2017年","2018年","2019年","2020年","2021年","2022年","2023年","2024年","2025年"};//年份数组
//		Calendar cal = Calendar.getInstance();
//		cal.get(Calendar.YEAR);
		
		//循环得到年份数组
		int span = targetYear - startYear+1;
		mYearData = new String[span];
		int tempYear=1940;
		for(int i=0; i<span; i++){
			if(tempYear == mShowDefaultYear){
				mShowDefaultYearPosition = i;
			}
			mYearData[i] = String.valueOf(tempYear)+"年";
			tempYear=tempYear+1;
		}
		
		mMonthData = new String[]{"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};//月份数组
		mDayData_thirtyone = new String[]{"1号","2号","3号","4号","5号","6号","7号","8号","9号","10号","11号","12号","13号","14号","15号","16号","17号","18号","19号","20号","21号","22号","23号","24号","25号","26号","27号","28号","29号","30号","31号"};//31天日期数组
		mDayData_thirty = new String[]{"1号","2号","3号","4号","5号","6号","7号","8号","9号","10号","11号","12号","13号","14号","15号","16号","17号","18号","19号","20号","21号","22号","23号","24号","25号","26号","27号","28号","29号","30号"};//30天日期数组
		mDayData_twintynine = new String[]{"1号","2号","3号","4号","5号","6号","7号","8号","9号","10号","11号","12号","13号","14号","15号","16号","17号","18号","19号","20号","21号","22号","23号","24号","25号","26号","27号","28号","29号"};//29天日期数组
		mDayData_twintyeight = new String[]{"1号","2号","3号","4号","5号","6号","7号","8号","9号","10号","11号","12号","13号","14号","15号","16号","17号","18号","19号","20号","21号","22号","23号","24号","25号","26号","27号","28号"};//28天日期数组
		ArrayWheelAdapter<String> year_adapter = new ArrayWheelAdapter<String>(mContext,mYearData);
		mYearWheel.setViewAdapter(year_adapter);
		mYearWheel.setVisibleItems(5);//滚轮显示五个
		mYearWheel.setCurrentItem(mShowDefaultYearPosition);//从第一个开始显示，默认为"0"
		ArrayWheelAdapter<String> month_adapter = new ArrayWheelAdapter<String>(mContext,mMonthData);
		mMonthWheel.setViewAdapter(month_adapter);
		mMonthWheel.setVisibleItems(5);//滚轮显示五个
		mMonthWheel.setCurrentItem(0);//从第一个开始显示，默认为"0"
		dayThirthone_adapter = new ArrayWheelAdapter<String>(mContext,mDayData_thirtyone);
		dayThirty_adapter = new ArrayWheelAdapter<String>(mContext,mDayData_thirty);
		dayTwintynine_adapter = new ArrayWheelAdapter<String>(mContext,mDayData_twintynine);
		dayTwintyeight_adapter = new ArrayWheelAdapter<String>(mContext,mDayData_twintyeight);
		mDayWheel.setViewAdapter(dayThirthone_adapter);
		mDayWheel.setVisibleItems(5);//滚轮显示五个
		mDayWheel.setCurrentItem(0);//从第一个开始显示，默认为"0"
		YearDate = mShowDefaultYear+"";
		MonthDate = "01";
		DayDate = "01";
	
	}

	private void registerListener(OnDateSelectListener listener) {
		if(listener!=null){
			this.listener = listener;
			mConfirmBtn.setOnClickListener(this);
			mCancelBtn.setOnClickListener(this);
			mYearWheel.addChangingListener(this);
			mMonthWheel.addChangingListener(this);
			mDayWheel.addChangingListener(this);
		}
	
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_btn:
			if(listener != null){
				listener.getDateByConfirm(YearDate, MonthDate, DayDate, true);
			}
			break;
		case R.id.cancel_btn:
			if(listener != null){
				listener.cancel();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if(wheel == mYearWheel){
			//YearDate = (newValue+2015)+"";
			String YearDateStr = mYearData[mYearWheel.getCurrentItem()];
			YearDate = YearDateStr.substring(0, 4);
			int year = Integer.valueOf(YearDate);
			if((year%4==0&&year%100!=0)||(year%400==0)){  //表示闰年
				if(MonthDate.equals("02")){
					mDayWheel.setViewAdapter(dayTwintynine_adapter);
					mDayWheel.setVisibleItems(5);//滚轮显示五个
					mDayWheel.setCurrentItem(0);//从第一个开始显示，默认为"0"
				}
			}else{  //非闰年
				if(MonthDate.equals("02")){
					mDayWheel.setViewAdapter(dayTwintyeight_adapter);
					mDayWheel.setVisibleItems(5);//滚轮显示五个
					mDayWheel.setCurrentItem(0);//从第一个开始显示，默认为"0"
				}
			}
			if(MonthDate.equals("01") || MonthDate.equals("03") || MonthDate.equals("05") || MonthDate.equals("07") || MonthDate.equals("08") || MonthDate.equals("10") || MonthDate.equals("12")){
				mDayWheel.setViewAdapter(dayThirthone_adapter);
				mDayWheel.setVisibleItems(5);//滚轮显示五个
				mDayWheel.setCurrentItem(0);//从第一个开始显示，默认为"0"
			}else if(MonthDate.equals("04") || MonthDate.equals("06") || MonthDate.equals("09") || MonthDate.equals("1")){
				mDayWheel.setViewAdapter(dayThirty_adapter);
				mDayWheel.setVisibleItems(5);//滚轮显示五个
				mDayWheel.setCurrentItem(0);//从第一个开始显示，默认为"0"
			}
		}else if(wheel == mMonthWheel){
			if(newValue == 1){
				if(YearDate.equals("2016") || YearDate.equals("2020") || YearDate.equals("2024")){
					mDayWheel.setViewAdapter(dayTwintynine_adapter);
					mDayWheel.setVisibleItems(5);//滚轮显示五个
					mDayWheel.setCurrentItem(0);//从第一个开始显示，默认为"0"
				}else{
					mDayWheel.setViewAdapter(dayTwintyeight_adapter);
					mDayWheel.setVisibleItems(5);//滚轮显示五个
					mDayWheel.setCurrentItem(0);//从第一个开始显示，默认为"0"
				}
			}else if(newValue == 0 || newValue == 2 || newValue ==4 || newValue ==6 || newValue == 7 || newValue==9 || newValue==11){
				mDayWheel.setViewAdapter(dayThirthone_adapter);
				mDayWheel.setVisibleItems(5);//滚轮显示五个
				mDayWheel.setCurrentItem(0);//从第一个开始显示，默认为"0"
			}else {
				mDayWheel.setViewAdapter(dayThirty_adapter);
				mDayWheel.setVisibleItems(5);//滚轮显示五个
				mDayWheel.setCurrentItem(0);//从第一个开始显示，默认为"0"
			}
			MonthDate = (newValue+1) > 9?(newValue+1)+"" : "0" + (newValue+1);
		}else if(wheel == mDayWheel){
			DayDate = (newValue+1) > 9?(newValue+1)+"" : "0" + (newValue+1);
		}
//		if(listener != null){
//			listener.getDateByConfirm(YearDate, MonthDate, DayDate, false);
//		}
	}

	
	/**
	 * 自定义接口
	 * @author WanRui
	 */
	public interface OnDateSelectListener {

		/**
		 * 得到年月日的值
		 * @param YearDate 年份滚轮的值
		 * @param MonthDate 月份滚轮的值
		 * @param DayDate 日期滚轮的值
		 * @param isColse 是否需要关闭弹框
		 */
		void getDateByConfirm(String YearDate, String MonthDate, String DayDate,Boolean isClosed);
		
		void cancel();

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return true;
	}
}
