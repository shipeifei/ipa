package com.ipassistat.ipa.view;

import java.util.HashMap;

import com.ipassistat.ipa.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
/**
 * app 加载滚动条
 * @author lxc
 *
 */
public class HmlProgressDialog extends LinearLayout{

	private static HashMap<String, Integer> mHashMap = new HashMap<String, Integer>();
	
	public HmlProgressDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.loading_view, this);
	}

	public void showLoadingView(String flag,HmlProgressDialog progressDialog){
		progressDialog.setVisibility(View.VISIBLE);
		setRequestNum(flag);
	}
	
	
	public void setRequestNum(String flag){
		if (mHashMap.containsKey(flag)) {
			mHashMap.put(flag, mHashMap.get(flag)+1);
		}else {
			mHashMap.put(flag, 1);
		}
	}
	
	public void dismissLoadingView(String flag,HmlProgressDialog progressDialog){
		if (mHashMap.containsKey(flag)) {
			mHashMap.put(flag, mHashMap.get(flag)-1);
			if (mHashMap.get(flag) == 0) {
				progressDialog.setVisibility(View.GONE);
			}
		}
	}
	
	
	
}
