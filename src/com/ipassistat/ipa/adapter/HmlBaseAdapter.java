package com.ipassistat.ipa.adapter;

import com.ipassistat.ipa.dao.BusinessInterface;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * hml baseadapter 
 * @author Administrator
 *
 */
public class HmlBaseAdapter extends BaseAdapter implements BusinessInterface{

	@Override
	public int getCount() {
		
		return 0;
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return null;
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		
		
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		
		
	}

	@Override
	public void onError(String ur, String result) {
		
		
	}

	@Override
	public void onNoNet() {
		
		
	}

	@Override
	public void onNoTimeOut() {
		
		
	}

	@Override
	public void onFinish() {
		
		
	}

	
}
