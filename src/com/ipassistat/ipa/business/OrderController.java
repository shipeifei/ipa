package com.ipassistat.ipa.business;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.httprequest.HttpRequestLisenter;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单数据控制类
 * 
 * Package: com.ichsy.mboss.ui.business  
 *  
 * File: OrderController.java   
 *  
 * @author: 任恒   Date: 2015-2-12  
 *
 * Modifier： Modified Date： Modify： 
 *  
 * Copyright @ 2015 Corpration Name  
 *
 */
public class OrderController {
	
	public String getSendTypeStr(String str) {
		if (Double.parseDouble(str) == 0) {
			return "快递 免邮";
		} else {
			return "快递￥" + str;
		}
	}
	
	/**
	 * 根据订单状态判断评价评价按钮是否隐藏 0隐藏 1显示 
	 * @param str
	 * @return
	 */
	public int getShowStatuByOrderCode(String str){
		if(str.equals(Constant.ORDER_STATU_NO_PAY)){
			return 0;
		}else if(str.equals(Constant.ORDER_STATU_NO_SEND)){
			return 0;
		}else if(str.equals(Constant.ORDER_STATU_HAS_SEND)){
			return 0;
		}else if(str.equals(Constant.ORDER_STATU_HAS_RECEIVE)){
			return 1;
		}else if(str.equals(Constant.ORDER_STATU_SUCCESS)){
			return 1;
		}else{
			return 0;
		}
	}
	
	/**
	 * 根据是否评价字段判断是否是否评价按钮显示状态 0 隐藏 1显示
	 * @param str
	 * @return
	 */
	public int getShowStatuByIsComm(String str){
		if(str.equals("true")){
			return 0;
		}else{
			return 1;
		}
	}
	
	/**
	 * 删除订单
	 * @param orderCode订单编号
	 */
	public  void removeOrder(final Context context, final String orderCode){
		AlertDialog.Builder builder=new Builder(context);
		builder.setMessage("亲亲，删除后将不再恢复，确认删除？");
		builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
			}
		});
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				MobclickAgent.onEvent(context, "1099");
				OrderModule module = new OrderModule((HttpRequestLisenter) context);
				module.postOrderRemove(context, orderCode); 
			}
		});
		builder.create().show();
	}
	
	/**
	 * 确认收货
	 * @param orderCode 订单号 
	 */
	public  void goodsReceiveConfirm(Context context, final String orderCode){
		final Activity activity = (Activity) context;
		AlertDialog.Builder builder=new Builder(activity);
		builder.setMessage("亲亲，为了不必要损失，请确保成功收货哦");
		builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				MobclickAgent.onEvent(activity, "1098");
				OrderModule module = new OrderModule((HttpRequestLisenter)activity);
				module.postGoodsReceiveConfirm(activity, orderCode);
			}
		});
		
		builder.create().show();
	}
}
