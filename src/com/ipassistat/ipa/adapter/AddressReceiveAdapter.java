package com.ipassistat.ipa.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.AddresssReceiveResponse;
import com.ipassistat.ipa.bean.response.entity.BeautyAddress;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.ui.activity.AddressEditActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 收货地址列表Adapter
 * @author renheng
 * 
 */
public class AddressReceiveAdapter extends HmlBaseAdapter implements BusinessInterface{

	private Context context;
	private List<BeautyAddress> list;
	private LayoutInflater inflater;
	private int temp = -1;
	private Activity activity;
	private AddresssReceiveResponse addrReceResp;   
	private int mDefaultPos;
	private String normalFlag="normalFlag", notifyFlag="notifyFlag"; //normalFlag代表setAdapter过来的list，notifityFlag代表notifyDataSetChanged的list
	

	public AddressReceiveAdapter(Context context, List<BeautyAddress> list,AddresssReceiveResponse addrReceResp) {
		this.context = context;
		this.list = newList(list);
		this.addrReceResp=addrReceResp;
		activity = (Activity) context;
		inflater = LayoutInflater.from(context);
	}
	
	private List<BeautyAddress> newList(List<BeautyAddress> list){
		List<BeautyAddress> newList = new ArrayList<BeautyAddress>();
		BeautyAddress addr = new BeautyAddress();
		addr.setId(normalFlag);
		newList.add(addr);
		for(int i=0; i<list.size(); i++){
			int newPos=i+1;
			newList.add(newPos, list.get(i));
		}
		return newList;
	}
	

	@Override
	public int getCount() {
		
		return list.size()-1;
	}

	@Override
	public Object getItem(int position) {
		
		return list.get(position+1);
	}

	@Override
	public long getItemId(int position) {
		
		return position+1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_address_receive, null);
			init(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		setEditClickListener(position+1,holder);  
		
		setRadioSelect(position+1,holder);
		
		BeautyAddress addr=list.get(position+1);
		holder.name.setText(addr.getName());
		holder.phone.setText(addr.getMobile());
		String addressDetail=addr.getProvinces()+addr.getStreet();
		holder.address.setText(addressDetail);
		
		//1代表是默认地址
		if("1".equals(addr.getIsdefault())){
			holder.radio.setChecked(true);
			mDefaultPos=position+1;
		}else{
			holder.radio.setChecked(false);
		}
		
		String flag=list.get(0).getId();
		if(flag.equals(normalFlag)){
			//代表通过setadapter方法调用的getview
			if(list.size()==2){
				holder.radio.setChecked(true);
				setDefaultAddresss(1);
			}
		}
		return convertView;
	}
	
	/*
	 * 设置listview中的radiobutton 单选
	 */
	private void setRadioSelect(final int position,ViewHolder holder){
		holder.radio.setId(position);
		holder.radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				if (isChecked) {
					setDefaultAddresss(position);
					mDefaultPos=position;
					
					if (temp != -1) {
						RadioButton r = (RadioButton) activity
								.findViewById(temp);
						if (r != null) {
							r.setChecked(false);
						}
					}
					temp = buttonView.getId();
				}
			}
		});

		if (position == temp) {
			holder.radio.setChecked(true);
		} else {
			holder.radio.setChecked(false);
		}
	}
	
	private void init(ViewHolder holder,View convertView){
		holder.radio =(RadioButton) convertView.findViewById(R.id.select);
		holder.edit=(Button) convertView.findViewById(R.id.edit);
		holder.address=(TextView) convertView.findViewById(R.id.user_address);
		holder.name=(TextView) convertView.findViewById(R.id.user_name);
		holder.phone=(TextView) convertView.findViewById(R.id.user_phone);
	}
	
	private class ViewHolder {
		RadioButton radio;
		Button edit;
		TextView name;
		TextView phone;
		TextView address;
	}
	
	/**
	 * 为编辑按钮设置监听
	 * @param position
	 * @param holder
	 */
	private void setEditClickListener(int position, ViewHolder holder){
		final BeautyAddress addr=list.get(position);
		holder.edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				MobclickAgent.onEvent(context, "1090");
				Intent intent=new Intent();
				intent.putExtra(IntentFlag.ADDRESS_EDIT, addr);
				intent.setClass(context, AddressEditActivity.class);
				activity.startActivity(intent);
			}
		});
	}

	/**
	 * 调取设置默认收货地址接口
	 * @param position
	 */
	private void setDefaultAddresss(int position){
		BeautyAddress addr = list.get(position);
		String id =addr.getId();
		UserModule module = new UserModule(this);
		module.postDefaultAddresss(context, id);
		module.getAddressList(context);
	}
	
	
	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		
		super.onMessageSucessCalledBack(url, object);
		if(url.equals(ConfigInfo.API_POST_SET_DEFAULT_ADDRESS)){
			Constant.IS_OPERATION_FLAG=true;
			UserModule module = new UserModule(this);
			list.get(0).setId(notifyFlag);
			for(int i=1; i<list.size();i++){
				BeautyAddress addr = list.get(i);
				String isdefault = addr.getIsdefault();
				addr.setIsdefault("0");
				if(i==mDefaultPos){
					addr.setIsdefault("1");
					module.saveUserDefaultAdd(context, addr);
				}else{
					addr.setIsdefault("0");
				}
			}
			notifyDataSetChanged();
			
		}
	}
	
}
