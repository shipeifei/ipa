package com.ipassistat.ipa.ui.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.OrderDetailResponse;
import com.ipassistat.ipa.bean.response.OrderRemoveResponse;
import com.ipassistat.ipa.bean.response.entity.ApiOrderSellerDetailsResult;
import com.ipassistat.ipa.bean.response.entity.MicroMessagePayment;
import com.ipassistat.ipa.business.OrderController;
import com.ipassistat.ipa.business.OrderModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.FormatUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.util.pay.PayUitl;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单详情
 * 
 * @author renheng
 * 
 */
public class OrderDetailActivity extends BaseActivity implements
		OnClickListener {


	private TextView mUserName;
	private TextView mUserPhone;
	private TextView mUserAddress;
	private TextView mPayType;
	private TextView mSendType;
	private TextView mTotalCount;
	private TextView mTotalPrice;
	private TextView mOrderNum;  //订单号
	private TextView mOrderNumTime;  //订单号生成时间
	private TextView mBtnChat;
	private TextView mBtnPay;
	private TitleBar mTitbar;
	private TextView mOrderPrice;  //订单金额（含运费）
	private TextView mOrderOperation; //订单操作
	private TextView mRemark,mRemarkName;
	private View mNoNet;
	
	private LinearLayout mContainer; 
	private LayoutInflater mInflater;
	private Context mContext;
	private ProgressHub mHub;
	private Intent mIntent;
	private String mPayTypeFlag; //使用哪种支付方式的标识
	
	private String mOrderCodeStr;
	private String mAliPaySign;
	private String mPageFlag=null; //从哪跳过来的页面标识
	
	private OrderController mOrderController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		init();
		initListener();
		mOrderController = new OrderController();
		mIntent=getIntent();
		mOrderCodeStr = mIntent.getStringExtra(IntentKey.ORDER_DETAIL);
		mPageFlag = mIntent.getStringExtra(IntentKey.INTENT_ORDERDETAIL_FLAG_KEY);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1036"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
		mHub=ProgressHub.getInstance(this);
		mHub.show("正在读取数据");
		getOrderDetail();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1036"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}
	
	private void init() {
		initTitleBar();
		mContext = OrderDetailActivity.this;
		mUserName = (TextView) findViewById(R.id.user_name);
		mUserAddress = (TextView) findViewById(R.id.user_address);
		mUserPhone = (TextView) findViewById(R.id.user_phone);
		mPayType = (TextView) findViewById(R.id.pay_type);
		mSendType = (TextView) findViewById(R.id.send_type);
		mTotalCount = (TextView) findViewById(R.id.total_count);
		mTotalPrice = (TextView) findViewById(R.id.total_price);
		mOrderNum = (TextView) findViewById(R.id.order_number);
		mOrderNumTime = (TextView) findViewById(R.id.order_number_time);
		mBtnChat = (TextView) findViewById(R.id.btn_chat);
		mBtnPay = (TextView) findViewById(R.id.btn_pay);
		mContainer=(LinearLayout) findViewById(R.id.container);
		mOrderPrice=(TextView) findViewById(R.id.order_price);
		mOrderOperation=(TextView) findViewById(R.id.order_operation);
		mRemarkName=(TextView) findViewById(R.id.remark);
		mRemark=(TextView) findViewById(R.id.remark_text);
		mNoNet=findViewById(R.id.no_net);
	}

	private void initListener(){
		mBtnChat.setOnClickListener(this);
		mBtnPay.setOnClickListener(this);
	}
	
	private void initTitleBar() {
		mTitbar = (TitleBar) findViewById(R.id.titlebar);
		String name = "订单详情";
		mTitbar.setTitleText(name);
		mTitbar.setButtonClickListener(TitleBarButton.leftImgv, this);
		mTitbar.setRightTextViewText("新增");
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.left_imgv:
			actionInFinish();
			finish();
			break;
		case R.id.btn_chat:	
			MobclickAgent.onEvent(this, "1105");
			IntentUtil.startSystemCallPhoneActivity(mContext, ConfigInfo.PHONE_NUM);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onNoNet() {
		super.onNoNet();
		mHub.dismiss();
		mNoNet.setVisibility(View.VISIBLE);
		mNoNet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getOrderDetail();
			}
		});
	}
	
	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		try {
			super.onMessageSucessCalledBack(url, object);
			if(ConfigInfo.API_ORDER_DETAIL.equals(url)){
				mHub.dismiss();
				mNoNet.setVisibility(View.GONE);
				OrderDetailResponse resp = (OrderDetailResponse) object;
				mPayTypeFlag = resp.getPay_type();
				mUserName.setText(resp.getConsigneeName());
				mUserAddress.setText(resp.getConsigneeAddress());
				mUserPhone.setText(resp.getConsigneeTelephone());
				String orderPriceStr=resp.getOrder_money();  //订单金额
				double freightDouble=resp.getFreight(); //运费    
				mOrderPrice.setText(StringUtil.getCharPrice(FormatUtil.getTwoNumber(resp.getDue_money())));
				String orderNumStr="订单号："+resp.getOrder_code();
				 mOrderCodeStr=resp.getOrder_code();
				mOrderNum.setText(orderNumStr);
				String orderTimeStr=getFormatTime(resp.getCreate_time()); //订单号创建时间

				mOrderNumTime.setText(orderTimeStr); 
				mTotalPrice.setText(StringUtil.getCharPrice(orderPriceStr));
				mSendType.setText(mOrderController.getSendTypeStr(freightDouble+""));
				String orderStatuStr=resp.getOrder_status(); //订单状态
				mOrderOperation.setText(StringUtil.getOrderStatuStr(orderStatuStr));
				String remarkStr = resp.getRemark();
				if(TextUtils.isEmpty(remarkStr)){
					hideRemark();
				}else{
					showRemark();
					mRemark.setText(remarkStr);
				}
				changByPayType();
				List<ApiOrderSellerDetailsResult> list = resp.getOrderSellerList();
				
				showByOrderStatu(resp.getOrder_status(), resp.getAlipaySign(), resp.getMicoPayment());
				addShopItem(list,resp.getOrder_status(),resp.getCreate_time());
			}
			if(ConfigInfo.API_ORDER_REMOVE.equals(url)){
				OrderRemoveResponse resp = (OrderRemoveResponse) object;
				if(resp.getResultCode()==1){
					Intent intent =new Intent();
					intent.setClass(OrderDetailActivity.this, OrderListActivity.class);
					startActivity(intent);
					ToastUtil.showToast(this, "删除订单成功");
					finish();
				}
			}
			if(ConfigInfo.API_GOODS_RECEIVE_CONFIRM.equals(url)){
				ToastUtil.showToast(mContext, "确认收货成功");
				getOrderDetail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加商品条目信息
	 * @param list  商品列表
	 */
	private void addShopItem(List<ApiOrderSellerDetailsResult> list, String orderStatu, final String orderTime){
		mInflater = LayoutInflater.from(this);
		mContainer.removeAllViews();
		int shopTotalCount=0;
		for(int i=0; i<list.size(); i++){
			View	view = mInflater.inflate(R.layout.item_order_good, null);
			ImageView thumb =  (ImageView) view.findViewById(R.id.thumb);
			TextView name= (TextView) view.findViewById(R.id.name);
			TextView price = (TextView) view.findViewById(R.id.price);
			TextView count = (TextView) view.findViewById(R.id.count);
			TextView comment=(TextView) view.findViewById(R.id.comment);
			
			final ApiOrderSellerDetailsResult result = list.get(i);
			name.setText(result.getProductName());
			price.setText(StringUtil.getCharPrice(FormatUtil.getTwoNumber(result.getPrice())));
			count.setText(StringUtil.getCharCount(result.getNumber()));
			shopTotalCount = shopTotalCount + Integer.valueOf(result.getNumber());
			
			BitmapOptionsFactory.getInstance(getApplicationContext()).display(thumb, result.getMainpicUrl());
			
			final String endTime=result.getEnd_time();  //结束时间
			
			int flag=mOrderController.getShowStatuByOrderCode(orderStatu); //根据订单状态得出判断码 0隐藏 1显示
			int tempFlag=flag;
			
			if(tempFlag==1){
				String isComment = result.getIfEvaluate(); //是否评价 true已评价 false为未评价
				/*
				 * 根据是否评价字段来判断评价标签是否隐藏
				 */
				if(isComment.equals("true")){
					flag=0;
				}else{
					flag=1;
				}
			}
			if(flag==0){
				comment.setVisibility(View.GONE);
			}else{
				comment.setVisibility(View.VISIBLE);
			}
			comment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(mContext, "1107");
					String sCode = result.getProductCode();
					IntentUtil.startGoodsCommentActivity(mContext, sCode, result.getMainpicUrl(), result.getProductName(), result.getProductShortName(), orderTime, mOrderCodeStr);
				}
			});
			mContainer.addView(view);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/*IntentUtil.startGoodsDetail(OrderDetailActivity.this, result.getProductCode(), result.getProductType(), endTime);
					finish();*/
				}
			});
		}
		setGoodsCount(String.valueOf(shopTotalCount)); 
	}

	/**
	 * 处理日期格式
	 * @param time
	 */
	private String getFormatTime(String time){
		return "订单号生成时间："+time;
	}
	
	/**
	 * 设置商品数量显示样式
	 * 
	 * @param str商品数量
	 */
	private void setGoodsCount(String str) {
		String count = "共 " + str + " 件商品";
		SpannableString ss = ViewUtil.getTextColorStyle(count, 2, 4,
				getResources().getColor(R.color.red_02));
		mTotalCount.setText(ss);
	}

	/**
	 * 根据订单状态来显示按钮
	 * 
	 * @param 订单状态
	 * @param 订单号
	 */
	private void showByOrderStatu(String orderStatu ,final String alipaySign, final MicroMessagePayment weixin) {
		
		if (orderStatu.equals(Constant.ORDER_STATU_NO_PAY)) {
			// 待付款
			mBtnPay.setText("付款");
			mBtnPay.setTextColor(getResources().getColor(R.color.order_pay));
			mBtnPay.setBackgroundResource(R.drawable.bg_border_yellow);
			this.mAliPaySign=alipaySign;
			mBtnPay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(OrderDetailActivity.this, "1104");
					
					if(mPayTypeFlag.equals(Constant.PAY_WEIXIN)){
							weixinPayType(weixin);
					}else if(mPayTypeFlag.equals(Constant.PAY_ZHIFUBAO)){
							aliPayType();
					}
				}
			});

		} else if (orderStatu.equals(Constant.ORDER_STATU_NO_SEND)) {
			// 已付款
			mBtnPay.setText("确认收货");
			mBtnPay.setTextColor(getResources().getColor(R.color.order_confirm_no_click));
			mBtnPay.setBackgroundResource(R.drawable.bg_border_order_confirm_no_click);
			mBtnPay.setOnClickListener(null);
			
		} else if (orderStatu.equals(Constant.ORDER_STATU_HAS_SEND)) {
			// 已发货
			mBtnPay.setText("确认收货");
			mBtnPay.setTextColor(getResources().getColor(R.color.order_confirm));
			mBtnPay.setBackgroundResource(R.drawable.bg_border_order_confirm);
			mBtnPay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(mContext, "1106");
					mOrderController.goodsReceiveConfirm(mContext, mOrderCodeStr);
				}
			});
			
		} else if (orderStatu.equals(Constant.ORDER_STATU_HAS_RECEIVE)) {
			// 已收货
			mBtnPay.setText("删除订单");
			mBtnPay.setTextColor(getResources().getColor(R.color.order_confirm));
			mBtnPay.setBackgroundResource(R.drawable.bg_border_order_confirm);
			mBtnPay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mOrderController.removeOrder(mContext, mOrderCodeStr);
				
				}
			});
		} else if (orderStatu.equals(Constant.ORDER_STATU_SUCCESS)) {
			// 交易成功
			mBtnPay.setText("删除订单");
			mBtnPay.setTextColor(getResources().getColor(R.color.order_confirm));
			mBtnPay.setBackgroundResource(R.drawable.bg_border_order_confirm);
			mBtnPay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mOrderController.removeOrder(mContext,mOrderCodeStr);
				}
			});
		} else if (orderStatu.equals(Constant.ORDER_STATU_FAIL)) {
			// 交易关闭
			mBtnPay.setText("删除订单");
			mBtnPay.setTextColor(getResources().getColor(R.color.order_confirm));
			mBtnPay.setBackgroundResource(R.drawable.bg_border_order_confirm);
			mBtnPay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mOrderController.removeOrder(mContext,mOrderCodeStr);
				}
			});
		}
	}
	/**
	 * 获取订单详情
	 */
	private void getOrderDetail(){
		OrderModule module=new OrderModule(this); 
		module.postOrderDetail(this, mOrderCodeStr);
	}
	
	@Override
	public void onNoTimeOut() {
		super.onNoTimeOut();
		mHub.dismiss();
	}
	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
		mHub.dismiss();
	}
	
	//隐藏订单备注
	private void hideRemark(){
		mRemarkName.setVisibility(View.GONE);
		mRemark.setVisibility(View.GONE);
	}
	
	//显示订单备注
	private void showRemark(){
		mRemarkName.setVisibility(View.VISIBLE);
		mRemark.setVisibility(View.VISIBLE);
	}
	
	
	private void aliPayType() {
		PayUitl.payByAli(mContext, mAliPaySign, new PayUitl.PayInterface() {

			@Override
			public void paySucess() {
				IntentUtil.startOrderSubmitSuccessActivity(mContext,
						mOrderCodeStr);
			}

			@Override
			public void payFail() {
				IntentUtil.startOrderDetailActivity(mContext, mOrderCodeStr);
			}
		});
	}
	
	private void weixinPayType(MicroMessagePayment weixin) {
		PayUitl.payByWeiXin(this, mOrderCodeStr, weixin.getPrepayid(),
				weixin.getNonceStr(), weixin.getTimeStamp(),
				weixin.getPackageValue(), weixin.getSign(), Constant.ORDER_DETAIL);
	}

	private void changByPayType(){
		if(mPayTypeFlag.equals(Constant.PAY_WEIXIN)){
			mPayType.setText("微信支付");
		}else if(mPayTypeFlag.equals(Constant.PAY_ZHIFUBAO)){
			mPayType.setText("支付宝");
		}
	}
	
	//返回的时候要调用的方法
	private void actionInFinish(){
		if(mPageFlag!=null){
			if(mPageFlag.equals(Constant.ORDER_SUBMIT_SUCCESS)||mPageFlag.equals(Constant.ORDER_CONFIRM)){
				if(OrderConfirmActivity.instanse!=null){
					OrderConfirmActivity.instanse.finish();
				}
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
	    	actionInFinish();
	    	finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}
	
}
