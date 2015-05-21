package com.ipassistat.ipa.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.OrderDetailResponse;
import com.ipassistat.ipa.business.OrderModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 提交成功activity
 * @author renheng
 *
 */
public class OrderSubmitSuccessActivity extends BaseActivity implements OnClickListener{
	
	private TextView mContinue; //继续购物
	private TextView mLookOrder;  //查看订单
	private TextView mOrderNum; //订单号
	private TextView mOrderPrice; //订单金额
	private TextView mPayType; //支付方式
	private TitleBar mBar; 
	
	private String mOrderCodeStr; //订单号
	private String mOrderMoneyStr; //订单金额
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_order_submit_success);
			init();
			initListener();
			Intent intent=getIntent();
			mOrderCodeStr = intent.getStringExtra(IntentKey.ORDER_SUCCESS_ORDER_CODE);
			
			OrderModule module= new OrderModule(this);
			module.postOrderDetail(this, mOrderCodeStr);
			
			mOrderNum.setText(mOrderCodeStr);
			
		}
		
		@Override
		protected void onResume() {
			super.onResume();
			MobclickAgent.onPageStart("1033"); //统计页面
		    MobclickAgent.onResume(this);          //统计时长
		}

		@Override
		protected void onPause() {
			super.onPause();
			MobclickAgent.onPageEnd("1033"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
		    MobclickAgent.onPause(this);
		}
		
		private void init(){
			initTitleBar();
			mContinue=(TextView) findViewById(R.id.continu);
			mLookOrder=(TextView) findViewById(R.id.look);
			mOrderNum=(TextView) findViewById(R.id.order_num);
			mOrderPrice=(TextView) findViewById(R.id.order_price);
			mPayType=(TextView) findViewById(R.id.pay_type);
		}
		
		private void initTitleBar(){
			mBar=(TitleBar) findViewById(R.id.titlebar);
			mBar.setTitleText("提交成功");
			mBar.setButtonClickListener(TitleBarButton.leftImgv, this);
		}
		
		private void initListener(){
			mContinue.setOnClickListener(this);
			mLookOrder.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			switch (v.getId()) {
			case R.id.continu:
				MobclickAgent.onEvent(this, "1095");
				intent.setClass(this, GoodsListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				break;
			case R.id.look :
				MobclickAgent.onEvent(this, "1096");
				IntentUtil.startOrderDetailActivity(this, mOrderCodeStr, Constant.ORDER_SUBMIT_SUCCESS);
				finish();
				break;
			case R.id.left_imgv :
				finish();
				break;

			default:
				break;
			}
		}
		
		@Override
		public void onMessageSucessCalledBack(String url, Object object) {
			super.onMessageSucessCalledBack(url, object);
			if(url.equals(ConfigInfo.API_ORDER_DETAIL)){
				OrderDetailResponse resp=(OrderDetailResponse) object;
				mOrderPrice .setText(StringUtil.getCharPrice(resp.getDue_money()+""));
				if(resp.getPay_type().equals(Constant.PAY_WEIXIN)){
					mPayType.setText("微信支付");
				}else if(resp.getPay_type().equals(Constant.PAY_ZHIFUBAO)){
					mPayType.setText("支付宝");
				}
			}
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
