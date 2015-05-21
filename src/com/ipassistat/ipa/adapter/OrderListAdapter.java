package com.ipassistat.ipa.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.entity.ApiSellerListResult;
import com.ipassistat.ipa.bean.response.entity.ApiSellerOrderListResult;
import com.ipassistat.ipa.bean.response.entity.MicroMessagePayment;
import com.ipassistat.ipa.business.CropImageListener;
import com.ipassistat.ipa.business.OrderController;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.pay.PayUitl;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单列表的adapter
 * @author renheng
 *
 */

public class OrderListAdapter extends PaginationAdapter<ApiSellerOrderListResult> implements OnClickListener{


	private List<ApiSellerOrderListResult> mList;  
	private Context mContext;
	private LayoutInflater mInflater;
	
	
	private ImageView mThumb;  //缩略图
	private TextView mName;  //商品名称
	private TextView mPrice; //商品单价
	private TextView mCount;  //商品数量
	private Activity mActivity;
	
	private OrderController mOrderController;
	
	public OrderListAdapter(Context context, List<ApiSellerOrderListResult> data) {
		super(context, data);
		mContext=context;
		mOrderController = new OrderController();
		mActivity=(Activity) context;
		mList=data;
		mInflater=LayoutInflater.from(context);
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ApiSellerOrderListResult  orderResult = mList.get(position);
		String payType;
		payType = orderResult.getPayType();
		String orderStatuStr=orderResult.getOrder_status();  //订单状态
		int statu =mOrderController.getShowStatuByOrderCode(orderStatuStr);
		int tempStatu=statu; 
		ViewHolder holder=null;
//		if(convertView==null){
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.adapter_order_list,null);
			holder.orderNum=(TextView) convertView.findViewById(R.id.order_number);
			holder.orderState=(TextView) convertView.findViewById(R.id.order_state);
			holder.orderItemTotalPrice=(TextView) convertView.findViewById(R.id.order_item_total_price);
			holder.orderConfirm=(TextView) convertView.findViewById(R.id.order_confirm);
			holder.listview=(ListView) convertView.findViewById(R.id.listview);
			holder.container=(LinearLayout) convertView.findViewById(R.id.container);
			//添加订单的商品信息
			List<ApiSellerListResult> list = getList(position);
			for(int i=0; i<list.size();i++){
				ApiSellerListResult good = list.get(i);
				String isComment = good.getIfEvaluate();
				
				if(tempStatu==1){
					/*
					 * 根据是否评价字段来判断评价标签是否隐藏
					 */
					statu=mOrderController.getShowStatuByIsComm(isComment);
					Log.d("BBB", "statu="+String.valueOf(statu));
				}
				addItem(holder.container,i, list, position,statu,orderResult.getOrder_code());
			}
			convertView.setTag(holder);
//		}else{
//			holder=(ViewHolder) convertView.getTag();
//		}
		
		holder.orderItemTotalPrice.setText(StringUtil.getCharPrice(orderResult.getDue_money()));
		String orderCodeStr="订单号："+orderResult.getOrder_code();
		holder.orderNum.setText(orderCodeStr);
		holder.orderState.setText(StringUtil.getOrderStatuStr(orderStatuStr));
		
		Sign sign =new Sign();
		sign.alipaySign=orderResult.getAlipaySign();
		sign.weixin = orderResult.getMicoPayment();
		setOrderConfirm(orderStatuStr, holder, orderResult.getOrder_code(), sign, payType);
		return convertView;
	}
	
	private class ViewHolder{
		TextView orderNum;
		TextView orderState;
		TextView orderItemTotalPrice;
		TextView orderConfirm;
		@SuppressWarnings("unused")
		ListView listview;
		LinearLayout container;
	}

	/**
	 * 订单中循环添加商品
	 * @param container  viewGroup
	 * @param pos 商品在当前订单中的位置
	 * @param list 每个订单的商品列表
	 * @param itemPosition 订单在订单列表的位置
	 * @param visible 评价按钮是否隐藏  0隐藏 1显示
	 */
	@SuppressLint("InflateParams")
	private void addItem(LinearLayout container, final int pos, List<ApiSellerListResult> list, final int itemPosition,int visible, final String orderCode){
		View view = mInflater.inflate(R.layout.item_order_good, null);
		
		mThumb=(ImageView) view.findViewById(R.id.thumb);
		mName=(TextView) view.findViewById(R.id.name);
		mPrice=(TextView) view.findViewById(R.id.price);
		mCount=(TextView) view.findViewById(R.id.count);

		final ApiSellerListResult res = list.get(pos);
		
		BitmapOptionsFactory.getInstance(getContext()).display(mThumb, res.getMainpic_url(), new CropImageListener());
		
		mName.setText(res.getProduct_name());
		mPrice.setText(StringUtil.getCharPrice(res.getSell_price()));
		mCount.setText(getCharString(res.getProduct_number()));
		
		
		final TextView comment=(TextView) view.findViewById(R.id.comment);
		if(visible==0){
			comment.setVisibility(View.GONE);
		}else{
			comment.setVisibility(View.VISIBLE);
		}
		
		comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, "1100");
				ApiSellerOrderListResult orderRes=	mList.get(itemPosition); //传入订单信息
				String describle=res.getProductShortName();
				IntentUtil.startGoodsCommentActivity(mContext, res.getProduct_code(),res.getMainpic_url(), res.getProduct_name(),describle , orderRes.getCreate_time(),orderCode);
			}
		});
		
		container.addView(view);
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				@SuppressWarnings("unused")
				Intent intent=new Intent();
				ApiSellerOrderListResult res = mList.get(itemPosition);
				String orderCode = res.getOrder_code(); //订单号
				IntentUtil.startOrderDetailActivity(mContext, orderCode);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_confirm:
			ToastUtil.showToast(mContext, "confirm");
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 得到每个订单的商品列表
	 */
	private List<ApiSellerListResult> getList(int position){
		ApiSellerOrderListResult res = mList.get(position);  
		List<ApiSellerListResult> apiList = res.getApiSellerList();
		return apiList;
	}
	
	
	private String getCharString(String str){
		return "数量："+str;
	}
	
	/**
	 * 根据订单状态判断确认收货按钮的逻辑
	 * @param orderStatu 订单状态码
	 * @param orderCode 当前订单号
	 */
	private void setOrderConfirm(String orderStatu, ViewHolder holder, final String orderCode, final Sign sign, final String payType) {
		if (orderStatu.equals(Constant.ORDER_STATU_NO_PAY)) {
			//待付款
			holder.orderConfirm.setBackgroundResource(R.drawable.bg_border_order_pay);
			holder.orderConfirm.setText("付款");
			holder.orderConfirm.setTextColor(mContext.getResources().getColor(R.color.order_pay));
			holder.orderConfirm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(mContext, "1097");
					if(payType.equals(Constant.PAY_ZHIFUBAO)){
						aliPay(sign.alipaySign, orderCode);
					}else if(payType.equals(Constant.PAY_WEIXIN)){
						weixinPayType(orderCode, sign.weixin);
					}
				}
			});
			
		} else if (orderStatu.equals(Constant.ORDER_STATU_NO_SEND)) {
			//已付款
			holder.orderConfirm.setBackgroundResource(R.drawable.bg_border_order_confirm_no_click);
			holder.orderConfirm.setText("确认收货");
			holder.orderConfirm.setTextColor(mContext.getResources().getColor(R.color.order_confirm_no_click));
			holder.orderConfirm.setOnClickListener(null);
			
		} else if (orderStatu.equals(Constant.ORDER_STATU_HAS_SEND)) {
			//已发货
			holder.orderConfirm.setBackgroundResource(R.drawable.bg_border_order_confirm);
			holder.orderConfirm.setText("确认收货");
			holder.orderConfirm.setTextColor(mContext.getResources().getColor(R.color.order_confirm));
			holder.orderConfirm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mOrderController.goodsReceiveConfirm(mActivity,orderCode);
				}
			});
			
		} else if (orderStatu.equals(Constant.ORDER_STATU_HAS_RECEIVE)) {
			//已收货
			holder.orderConfirm.setBackgroundResource(R.drawable.bg_border_order_confirm);
			holder.orderConfirm.setText("删除订单");
			holder.orderConfirm.setTextColor(mContext.getResources().getColor(R.color.order_confirm));
			holder.orderConfirm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mOrderController.removeOrder(mContext,orderCode);
				}
			});
		} else if (orderStatu.equals(Constant.ORDER_STATU_SUCCESS)) {
			//交易成功
			holder.orderConfirm.setBackgroundResource(R.drawable.bg_border_order_confirm);
			holder.orderConfirm.setText("删除订单");
			holder.orderConfirm.setTextColor(mContext.getResources().getColor(R.color.order_confirm));
			holder.orderConfirm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mOrderController.removeOrder(mContext,orderCode);
				}
			});
		} else if (orderStatu.equals(Constant.ORDER_STATU_FAIL)) {
			//交易关闭
			holder.orderConfirm.setBackgroundResource(R.drawable.bg_border_order_confirm);
			holder.orderConfirm.setText("删除订单");
			holder.orderConfirm.setTextColor(mContext.getResources().getColor(R.color.order_confirm));
			holder.orderConfirm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mOrderController.removeOrder(mContext,orderCode);
				}
			});
		} else {
			
		}
	}
	
	
	
	
	
	private void aliPay(String alipaySign, final String orderCode){
		PayUitl.payByAli(mContext,alipaySign,new PayUitl.PayInterface() {
			
			@Override
			public void paySucess() {
				IntentUtil.startOrderSubmitSuccessActivity(mContext, orderCode);
			}
			
			@Override
			public void payFail() {
			}
		});
	}
	
	private void weixinPayType(String orderCode, MicroMessagePayment weixin) {
		PayUitl.payByWeiXin(mContext, orderCode, weixin.getPrepayid(),
				weixin.getNonceStr(), weixin.getTimeStamp(),
				weixin.getPackageValue(), weixin.getSign(), Constant.ORDER_LIST);
	}
	
	class Sign{
		String alipaySign;
		MicroMessagePayment weixin;
	}
}
