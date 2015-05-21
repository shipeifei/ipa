package com.ipassistat.ipa.ui.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.local.GoodsEntity;
import com.ipassistat.ipa.bean.request.OrderConfirmRequest;
import com.ipassistat.ipa.bean.request.entity.BillInfo;
import com.ipassistat.ipa.bean.request.entity.GoodsInfoForAdd;
import com.ipassistat.ipa.bean.request.entity.PurchaseGoods;
import com.ipassistat.ipa.bean.response.OrderConfirmResponse;
import com.ipassistat.ipa.bean.response.OrderPrePayResponse;
import com.ipassistat.ipa.bean.response.OrderSubmitResponse;
import com.ipassistat.ipa.bean.response.entity.BeautyAddress;
import com.ipassistat.ipa.bean.response.entity.GoodsInfoForConfirm;
import com.ipassistat.ipa.bean.response.entity.MicroMessagePayment;
import com.ipassistat.ipa.business.AppInfoModule;
import com.ipassistat.ipa.business.OrderController;
import com.ipassistat.ipa.business.OrderModule;
import com.ipassistat.ipa.business.ProductStateManager;
import com.ipassistat.ipa.business.ShopingCartObserver;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.constant.IntentFlag;
import com.ipassistat.ipa.constant.IntentKey;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.FormatUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.NetUtil;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.pay.PayUitl;
import com.ipassistat.ipa.view.AlertDialog;
import com.ipassistat.ipa.view.MyScrollView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.MyScrollView.OnScrollListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单确认页面'
 * 
 * @author renheng
 * 
 */
public class OrderConfirmActivity extends BaseActivity implements
OnClickListener ,ShopingCartObserver<GoodsEntity>, OnScrollListener{

	private TitleBar mTitleBar;// 标题栏
	private Context mContext;
	private ProgressHub mHub;

	private MyScrollView mScrollView;
	private RelativeLayout mAddressLayout; // 收货地址信息和图标的父容器
	private TextView mCountTv; // 合计的商品数量
	private TextView mTotalPrice; // 商品合计的价格
	private TextView mPayPrice; // 应该支付的价格
	private TextView mPay; // 去支付按钮
	private RelativeLayout mAddrFrame; // 收货地址信息的容器
	private TextView mComment; // 评论
	private TextView mWarn; // 订单提示
	private TextView mSendType; // 配送方式
	private TextView mPayType; //支付方式
	private RelativeLayout mLayoutPayType;
	private LinearLayout mShopContainer; // 放置商品信息的容器
	private FrameLayout mItemGoodFrameLayout; //商品条目的framelayout
	private EditText mRemark; //订单备注

	// item_add_subtract里的控件
	private ImageView mAdd;
	private ImageView mSubtract;
	private EditText mCountEt;
	private int mCount = 1;
	private LayoutInflater inflater;

	private String mOrderType; // 订单类型
	private String sPriceStr; // 商品价格
	private List<GoodsInfoForAdd> mGoods; // 商品列表
	private double mPayPriceDou; // 应付款
	private String mFreightStr; // 运费
	private String mOrderMoneyStr; // 订单预结算接口传过来的订单金额
	private String mAreaCode; // 默认区编码

	private static String mActivityFlag=""; // 从哪个Activity跳转过来的标识

	private final String FLAG_BUY_BY_NOW = "FLAG_BUY_BY_NOW"; // 从立即购买跳转过来的普通商品
	private final String FLAG_BUY_BY_CART = "FLAG_BUY_BY_CART"; // 从购物车跳转过来
	private final String FLAG_BUY_BY_TIMELIMIT = "FLAG_BUY_BY_TIMELIMIT"; // 从立即购买跳转过来的限时抢购
	private final String FLAG_BUY_BY_TRY = "FLAG_BUY_BY_TRY"; // 从使用跳转过来
	private Intent mIntent;

	private String mSkuCodeByFree; // 通过试用页面跳转过来的SkuCode
	//private String mPayTypeFlag=Constant.PAY_WEIXIN; //支付方式的标记
	private String mPayTypeFlag = Constant.PAY_ZHIFUBAO; //1.0.0暂时不支持微信支付，默认为支付宝
	private int mPayPos=0; 
	private BeautyAddress mAddr;
	
	private String mActivityResultFlag;  //如果不为空，则是从onActivityForResult取的地址信息 
	private TextView mUserName,mUserPhone,mUserAddr;
	private View mAddressView,mImportInfoView;
	
	public static  OrderConfirmActivity instanse= null;
	
	private UserModule mUserModule;
	private OrderController mOrderController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirm);
		instanse=this;
		init();
		initListener();
		// 判断从哪个Activity跳转过来
		mIntent = getIntent();
		getDefaultAddress();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1026"); //统计页面
		MobclickAgent.onResume(this);          //统计时长
		if (mActivityFlag.equals(FLAG_BUY_BY_NOW)) {
			mCountEt.setText(mCount+"");
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(mActivityResultFlag==null){
			getDefaultAddress();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1026"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//HmlShoppingCartController.instance(getApplicationContext()).unRegisterListener(this);
	}

	/**
	 * 初始化
	 */
	private void init() {
		mContext = this;
		mUserModule = new UserModule(this);
		mOrderController = new OrderController();
		inflater = LayoutInflater.from(this);
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);// 标题栏
		String title = "确认订单";
		mTitleBar.setTitleText(title);// 设置文字标题
		mTitleBar.setVisibility(TitleBarButton.rightImgv, View.GONE);// 设置右边按钮隐藏
		mTitleBar.setButtonClickListener(TitleBarButton.leftImgv, this);

		mScrollView=(MyScrollView) findViewById(R.id.scroll);
		mCountTv = (TextView) findViewById(R.id.total_count);
		mTotalPrice = (TextView) findViewById(R.id.total_price);
		mPay = (TextView) findViewById(R.id.pay);
		mPayType=(TextView) findViewById(R.id.pay_type_text);
		mLayoutPayType=(RelativeLayout) findViewById(R.id.paymentmethod);
		mSendType = (TextView) findViewById(R.id.icon_send_type);
		mPayPrice = (TextView) findViewById(R.id.pay_price);
		mAddressLayout = (RelativeLayout) findViewById(R.id.address);
		mAddrFrame = (RelativeLayout) findViewById(R.id.frame_addr);
		mWarn = (TextView) findViewById(R.id.order_warn);
		mRemark=(EditText) findViewById(R.id.et_remark);
		mComment = (TextView) findViewById(R.id.comment);
		mComment.setVisibility(View.GONE); // 将评价按钮隐藏
		mShopContainer = (LinearLayout) findViewById(R.id.goodsDetail);
		mItemGoodFrameLayout=(FrameLayout) findViewById(R.id.good_detail_framelayout);
		mHub = ProgressHub.getInstance(this);
		updatePayTypeUi();
	}

	private void initListener() {
		mScrollView.setOnScrollListener(this);
		mAddressLayout.setOnClickListener(this);
		mPay.setOnClickListener(this);
		//mLayoutPayType.setOnClickListener(this); 暂时去掉微信支付
		//HmlShoppingCartController.instance(getApplicationContext()).registerListener(this);
	}


	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		try {
			if (ConfigInfo.API_ORDER_CONFIRM.equals(url)) { // 调取确认订单接口成功
				OrderConfirmResponse resp = (OrderConfirmResponse) object;
				mSendType.setText(mOrderController.getSendTypeStr(String.valueOf(resp
						.getSent_money()))); // 运费
				mPayPrice.setText(StringUtil.getCharPrice(String.valueOf(FormatUtil.getTwoNumber(resp
						.getPay_money())))); // 实付款
				mPayPriceDou = resp.getPay_money(); // 提交订单所需要的实付款参数
				mTotalPrice.setText(StringUtil.getCharPrice(String.valueOf(FormatUtil.getTwoNumber(resp
						.getCost_money())))); // 商品总金额
				List<GoodsInfoForConfirm> list = resp.getResultGoodsInfo(); // 商品列表
				mGoods = new ArrayList<GoodsInfoForAdd>(); // 提交订单所要用到的商品列表
				PurchaseGoods good = null;
				GoodsInfoForAdd goodAdd = null; // 提交订单用到的商品对象
				ArrayList<PurchaseGoods> purchrGoods = new ArrayList<PurchaseGoods>(); // 设置购物车所要用到的商品列表
				for (int i = 0; i < list.size(); i++) {
					good = new PurchaseGoods();
					GoodsInfoForConfirm goodInfo = list.get(i);
					good.setSku_code(goodInfo.getSku_code());
					good.setGoods_name(goodInfo.getSku_name());
					good.setPic_url(goodInfo.getPic_url());
					good.setProduct_code(goodInfo.getProduct_code());
					good.setPrice(String.valueOf(goodInfo.getSku_price()));
					good.setOrder_count(String.valueOf(goodInfo.getSku_num()));

					purchrGoods.add(good);
					goodAdd = new GoodsInfoForAdd();
					goodAdd.setArea_code(mAreaCode);
					goodAdd.setProduct_code(goodInfo.getProduct_code());
					goodAdd.setSku_num(goodInfo.getSku_num());
					goodAdd.setSku_code(goodInfo.getSku_code());
					mGoods.add(goodAdd);
				}

				if (mActivityFlag.equals(FLAG_BUY_BY_NOW)) {
					setGoodsInfoByNormal(good);
				} else if (mActivityFlag.equals(FLAG_BUY_BY_TIMELIMIT)) {
					setGoodsInfoByTimeLimit(good);
				} else if (mActivityFlag.equals(FLAG_BUY_BY_TRY)) {
					setGoodsInfoByFree(good);
				} else {
					setGoodsInfoByCart(purchrGoods);
				}

			}

			if (ConfigInfo.API_ORDER_SUBMIT.equals(url)) {

				mHub.dismiss();
				OrderSubmitResponse resp = (OrderSubmitResponse) object;
				int resultCode = resp.getResultCode();

				if (resultCode == 1) { // 如果返回1，则成功，调取支付宝开始支付

					final String orderCode = resp.getOrder_code();

					if (mSkuCodeByFree != null) {
						ProductStateManager
						.notifyProductStateHasChanged(mSkuCodeByFree); // 改变订单状态
					}
					
					if(resp.getFlag()==0){
						if(mPayTypeFlag.equals(Constant.PAY_ZHIFUBAO)){
							alipayType(resp.getPay_url(), orderCode);
						}else if(mPayTypeFlag.equals(Constant.PAY_WEIXIN)){
							weixinPayType(orderCode, resp.getMicoPayment());
						}
					}else{
						//此时代表订单金额为0，直接跳转到订单提交成功页
						IntentUtil.startOrderSubmitSuccessActivity(mContext, orderCode);
						finish();
					}

					// 订单提交成功后同步购物车
					String str = mIntent.getStringExtra(IntentFlag.ORDER_CONFIRM_FLAG);
					if(str != null && str.equals(IntentFlag.BUY_SHOPPING_CART)){
						for (int i = 0; i < mGoods.size(); i++) {
							//HmlShoppingCartController.instance(getApplicationContext()).removeFromCart(mGoods.get(i).getSku_code());
						}
					}
				} else if (resultCode == 916401133) { // 应付款数值有变化!
					double newPirceDou = Double.parseDouble(resp.getResultMessage());
					showDialogWithPriceChange(newPirceDou);

				} else if (resultCode == 916401131) { // 商品下架或库存不足
					ToastUtil.showToast(this, "商品库存不足或已下架！");
					finish();
				} else{
					ToastUtil.showToast(this, "提交订单失败");
				} 
			}
		} catch (Exception e) {
			LogUtil.outLogDetail("orderconfirm" + e.getMessage());
		}

	}

	private void showDialogWithPriceChange(double newPirceDou){
		String dialogMess = null; // 弹窗提示语
		if (newPirceDou > mPayPriceDou) {
			double balance = newPirceDou - mPayPriceDou;
			BigDecimal b1 = new BigDecimal(balance);
			BigDecimal b2 = b1
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			String str = String.valueOf(b2);
			dialogMess = "商品总价增加" + str + "元,是否重新结算!";
		} else {
			double balance = mPayPriceDou - newPirceDou;
			BigDecimal b1 = new BigDecimal(balance);
			BigDecimal b2 = b1
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			String str = String.valueOf(b2);
			dialogMess = "商品总价减少" + str + "元,是否重新结算!";
		}
		
		AlertDialog dialog = new AlertDialog(this);
		dialog.setTitle(dialogMess);
		dialog.setPositiveButton("取消",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				OrderConfirmActivity.this.finish();
			}
		});
		dialog.setNegativeButton("重新结算",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				OrderConfirmRequest req = new OrderConfirmRequest();
				req.setGoods(mGoods);
				req.setOrder_type(mOrderType);
				refresh(mGoods, mOrderType);
			}
		});
		dialog.show();
	}
	
	/**
	 * 点击事件
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv:
			// 返回
			MobclickAgent.onEvent(mContext, "1088");
			finish();
			break;
		case R.id.address:
			MobclickAgent.onEvent(mContext, "1085");
			// 点击填写收货地址跳转到收货地址页面
			Intent intentaddress = new Intent(mContext,
					AddressReceiveActivity.class);
			intentaddress.putExtra(IntentFlag.ADDRESS_SELECT_REQUEST_KEY, IntentFlag.ORDER_CONFIRM_FLAG);
			startActivityForResult(intentaddress, IntentKey.REQUEST_CODE_ORDER_CONFIRM);
			break;
		case R.id.pay:
			MobclickAgent.onEvent(mContext, "1087");
			submitOrder();
			break;
		case R.id.icon_add:
			if (mCount < 99) {
				mCount++;
				mCountEt.setText(String.valueOf(mCount));
			}
			break;
		case R.id.icon_subtract:
			if (mCount > 1) {
				mCount--;
				mCountEt.setText(String.valueOf(mCount));
			}
			break;
		case R.id.paymentmethod:
			IntentUtil.startPayTypeChoice(this, mPayPos, 0);
			break;
		default:
			break;
		}
	}


	private void addTextWatcher() {
		mAdd.setOnClickListener(OrderConfirmActivity.this);
		mSubtract.setOnClickListener(null);

		mAdd.setBackgroundResource(R.drawable.button_shoppingcart_plus_on);
		mSubtract.setBackgroundResource(R.drawable.button_shoppingcart_reduction_off);

		mCountEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!"".equals(s.toString().trim())) {
					int c = Integer.valueOf(s.toString());
					mCount = c;
					mAdd.setOnClickListener(OrderConfirmActivity.this);
					mSubtract.setOnClickListener(OrderConfirmActivity.this);
					if (c > 99) {
						mCountEt.setText(String.valueOf(99));
						ToastUtil.showToast(mContext, "亲亲，没办法买更多了哟 ~");
						mAdd.setBackgroundResource(R.drawable.button_shoppingcart_plus_off);
						mSubtract
						.setBackgroundResource(R.drawable.button_shoppingcart_reduction_on);
						mAdd.setOnClickListener(null);
						mSubtract.setOnClickListener(OrderConfirmActivity.this);
					} else if (c < 1) {
						LogUtil.outLogDetail(c);
						mCount = 1;
						mCountEt.setText(String.valueOf(mCount));
						ToastUtil.showToast(mContext, "亲亲，至少买一件哦~");
						mAdd.setBackgroundResource(R.drawable.button_shoppingcart_plus_on);
						mSubtract
						.setBackgroundResource(R.drawable.button_shoppingcart_reduction_off);
						mAdd.setOnClickListener(OrderConfirmActivity.this);
						mSubtract.setOnClickListener(null);
					} else if (c == 1) {
						mAdd.setBackgroundResource(R.drawable.button_shoppingcart_plus_on);
						mSubtract
						.setBackgroundResource(R.drawable.button_shoppingcart_reduction_off);
						mAdd.setOnClickListener(OrderConfirmActivity.this);
						mSubtract.setOnClickListener(null);
						setTotalInfo(s.toString());
					} else if (c == 99) {
						mAdd.setBackgroundResource(R.drawable.button_shoppingcart_plus_off);
						mSubtract
						.setBackgroundResource(R.drawable.button_shoppingcart_reduction_on);
						mAdd.setOnClickListener(null);
						mSubtract.setOnClickListener(OrderConfirmActivity.this);
						setTotalInfo(s.toString());
					} else {
						mAdd.setBackgroundResource(R.drawable.button_shoppingcart_plus_on);
						mSubtract
						.setBackgroundResource(R.drawable.button_shoppingcart_reduction_on);
						mAdd.setOnClickListener(OrderConfirmActivity.this);
						mSubtract.setOnClickListener(OrderConfirmActivity.this);

						setTotalInfo(s.toString());
					}

				}
			}
		});
	}

	/**
	 * 从收货地址列表取出默认收货地址
	 */
	private void getDefaultAddress() {
		mUserModule = new UserModule(this);
		BeautyAddress addr = mUserModule.getUserDefaultAddres(this);
		if(addr==null){
			mPay.setOnClickListener(null);
			mPay.setBackgroundResource(R.drawable.bg_orderconfirm_pay_no);
			showAddressUiByDefault();
			getDataByIntent();
		}else{
			showAddrUiByBeautyAddr(addr);
		}
	}

	private void showAddrUiByBeautyAddr(BeautyAddress addr){
		if(addr!=null){
			mAddr =addr;
			mAddressView = inflater.inflate(R.layout.item_address_receive, null);
			mImportInfoView = inflater.inflate(R.layout.tv_user_info, null);
			mUserName = (TextView) mAddressView.findViewById(R.id.user_name);
			mUserPhone = (TextView) mAddressView.findViewById(R.id.user_phone);
			mUserAddr = (TextView) mAddressView.findViewById(R.id.user_address);
			mUserAddr.setMaxLines(2);
			mUserAddr.setEllipsize(TruncateAt.END);
			
			mUserName.setText(mAddr.getName());
			mUserPhone.setText(mAddr.getMobile());
			mUserAddr.setText(mAddr.getProvinces() + mAddr.getStreet());
			mAddrFrame.removeAllViews();
			mAddrFrame.addView(mAddressView);
			mPay.setBackgroundResource(R.drawable.bg_orderconfirm_pay);
			mPay.setOnClickListener(this);
			mAreaCode = mAddr.getAreaCode(); // 得到默认区编码
		}
		getDataByIntent();
	}

	/**
	 * 将地址信息改为默认地址信息
	 */
	private void showAddressUiByDefault(){
		mAddrFrame.removeAllViews();
		mAddressView = inflater.inflate(R.layout.item_address_receive, null);
		mImportInfoView = inflater.inflate(R.layout.tv_user_info, null);
		mAddrFrame.addView(mImportInfoView);
	}
	
	/**
	 * 设置以立即购买方式传入的普通商品信息
	 * 
	 * @param goodsCount
	 *            商品数量
	 */
	private void setGoodsInfoByNormal(PurchaseGoods goods) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.item_order_good_edit, null);
		mItemGoodFrameLayout.removeAllViews();
		mItemGoodFrameLayout.addView(view);

		mAdd = (ImageView) view.findViewById(R.id.icon_add);
		mSubtract = (ImageView)view.findViewById(R.id.icon_subtract);
		mCountEt = (EditText)view.findViewById(R.id.goods_count);

		addTextWatcher();

		TextView name = (TextView) view.findViewById(R.id.name); // 商品名字
		TextView price = (TextView) view.findViewById(R.id.price); // 商品价格
		ImageView image = (ImageView) view.findViewById(R.id.thumb); // 商品图片
		sPriceStr = goods.getPrice(); // 商品单价，全局可用

		name.setText(goods.getGoods_name());
		price.setText(StringUtil.getCharPrice(sPriceStr));
		
		BitmapOptionsFactory.getInstance(getApplicationContext()).display(image, goods.getPic_url());

		mCountTv.setText(mCountEt.getEditableText().toString());

	}

	/**
	 * 设置以立即购买方式传入的限时抢购商品信息
	 */
	private void setGoodsInfoByTimeLimit(PurchaseGoods goods) {
		View view = findViewById(R.id.view_goods_detail);
		TextView name = (TextView) view.findViewById(R.id.name); // 商品名字
		TextView price = (TextView) view.findViewById(R.id.price); // 商品价格
		ImageView image = (ImageView) view.findViewById(R.id.thumb); // 商品图片
		TextView count = (TextView) view.findViewById(R.id.count); // 商品数量

		name.setText(goods.getGoods_name());
		price.setText(goods.getPrice());
		BitmapOptionsFactory.getInstance(getApplicationContext()).display(image, goods.getPic_url());

		String c = goods.getOrder_count();
		count.setText(getCharCount(c));
		mCountTv.setText(c);
		mTotalPrice.setText(StringUtil.getCharPrice(mOrderMoneyStr));
		mPayPriceDou = Double.parseDouble(mOrderMoneyStr)
				+ Double.parseDouble(mFreightStr);
		mPayPrice.setText(StringUtil.getCharPrice(String.valueOf(FormatUtil.getTwoNumber(mPayPriceDou))));
	}

	/**
	 * 设置由申请付邮试用页面传入的商品信息
	 * 
	 * @param goods
	 */
	private void setGoodsInfoByFree(PurchaseGoods goods) {
		View view = findViewById(R.id.view_goods_detail);
		TextView name = (TextView) view.findViewById(R.id.name); // 商品名字
		TextView price = (TextView) view.findViewById(R.id.price); // 商品价格
		ImageView image = (ImageView) view.findViewById(R.id.thumb); // 商品图片
		TextView count = (TextView) view.findViewById(R.id.count); // 商品数量

		name.setText(goods.getGoods_name());
		price.setText("￥0");

		BitmapOptionsFactory.getInstance(getApplicationContext()).display(image, goods.getPic_url());
		
		String c = goods.getOrder_count();
		count.setText(getCharCount(c));
		mCountTv.setText(c);
		mPayPriceDou = Double.valueOf(mFreightStr);
		mTotalPrice.setText("￥0");
		mPayPrice.setText(StringUtil.getCharPrice(String.valueOf(FormatUtil.getTwoNumber(mPayPriceDou))));
	}

	/**
	 * 设置由购物车页面传入的商品信息
	 * 
	 * @param goods
	 */
	private void setGoodsInfoByCart(ArrayList<PurchaseGoods> list) {
		mShopContainer.removeAllViews();
		int orderTotalCount = 0;
		double tPrice = 0; // 订单价格
		for (int i = 0; i < list.size(); i++) {
			View view = inflater.inflate(R.layout.item_order_good, null);
			TextView name = (TextView) view.findViewById(R.id.name); // 商品名字
			TextView price = (TextView) view.findViewById(R.id.price); // 商品价格
			ImageView image = (ImageView) view.findViewById(R.id.thumb); // 商品图片
			TextView count = (TextView) view.findViewById(R.id.count); // 商品数量
			TextView comment = (TextView) view.findViewById(R.id.comment);
			comment.setVisibility(View.GONE);

			PurchaseGoods goods = list.get(i);
			name.setText(goods.getGoods_name());
			price.setText(StringUtil.getCharPrice(FormatUtil.getTwoNumber(goods.getPrice())));
			BitmapOptionsFactory.getInstance(getApplicationContext()).display(image, goods.getPic_url());
			String c = goods.getOrder_count();
			count.setText(getCharCount(c));
			String singlePrice = goods.getPrice(); // 商品单价
			double singlePriceTotal = Double.parseDouble(singlePrice)
					* Integer.parseInt(c); // 每个商品单价*数量=商品的总价
			tPrice = singlePriceTotal + tPrice;

			orderTotalCount = Integer.valueOf(c) + orderTotalCount; // 得到合计商品数量

			android.widget.LinearLayout.LayoutParams p = new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			p.setMargins(60, 0, 0, 0);
			mShopContainer.addView(view, p);
		}
		mCountTv.setText(String.valueOf(orderTotalCount));
		mTotalPrice.setText(StringUtil.getCharPrice(FormatUtil.getTwoNumber(tPrice)));
		mPayPriceDou = Double.valueOf(tPrice) + Double.valueOf(mFreightStr); // 得到要支付的金额
		mPayPrice.setText(StringUtil.getCharPrice(FormatUtil
				.getTwoNumber(mPayPriceDou)));

	}

	/**
	 * 可编辑的商品页面 对合计信息的处理
	 * 
	 * @param count
	 *            合计的商品数量
	 */
	private void setTotalInfo(String count) {
		mCountTv.setText(count); // 更改合计数量
		BigDecimal bPrice = new BigDecimal(sPriceStr);
		BigDecimal b2 = new BigDecimal(Integer.valueOf(count));
		double str = bPrice.multiply(b2).doubleValue();
		mTotalPrice.setText(StringUtil.getCharPrice(String.valueOf(FormatUtil.getTwoNumber(str)))); // 设置订单里商品的合计价格

		mPayPriceDou = Double.valueOf(str) + Double.valueOf(mFreightStr); // 要提交的应付款（含运费）
		double dFreifht = Double.valueOf(mFreightStr); // 转化为double类型的运费
		double dStr = Double.valueOf(str); // 转化为double类型的商品总价（商品单价*商品数量）
		double dPayPrice = dFreifht + dStr; // 运费+商品总价=要支付的价格
		mPayPrice.setText(StringUtil.getCharPrice(String.valueOf(FormatUtil.getTwoNumber(dPayPrice))));// 设置要支付的合计价格
		setMGoodsCount(count);
	}


	/**
	 * 将数量改为 x 1 的形式
	 */
	private String getCharCount(String str) {
		return "x" + " " + str;
	}

	/**
	 * 提交订单
	 */
	private void submitOrder() {
		mHub.showWithNoTouch("正在提交订单");
		try {
			
			if (mAddr == null) {
				ToastUtil.showToast(mContext, "收货地址为空");
				mHub.dismiss();
			} else {
				String userName = mAddr.getName();// 用户名
				String areaCode = mAddr.getAreaCode(); // 区编码
				String userMobile = mAddr.getMobile(); // 用户电话
				String userAddr = mAddr.getProvinces() + mAddr.getStreet(); // 用户地址
				AppInfoModule appModule = new AppInfoModule(this);
				String appVersion = appModule.getAppVersionName(this); // app版本信息
				//String payType = Constant.PAY_ONLINE; // 在线支付的支付方式
				BillInfo billInfo = new BillInfo(); // 发票信息
				billInfo.setBill_detail("");
				billInfo.setBill_title("");
				billInfo.setBill_Type("");
				String remark=mRemark.getText().toString();

				OrderModule orderModule = new OrderModule(this);
				orderModule.postOrderSubmit(this, mPayPriceDou, mGoods,
						areaCode, userName, userMobile, mPayTypeFlag, userAddr,
						appVersion, mOrderType, remark, NetUtil.getPhoneIp());
			}
		} catch (Exception e) {
			LogUtil.outLogDetail("submitOrder()" + e.getMessage());
		}
	}


	/**
	 * 调取确认订单接口，刷新当前页面
	 */
	private void refresh(List<GoodsInfoForAdd> goods, String orderType) {
		OrderModule module = new OrderModule(this);
		module.postOrderConfirm(this, goods, orderType);
	}

	/**
	 * 更改List<GoodsInfoForAdd> mGoods 集合里的商品数量 用于提交订单
	 * 
	 * @param count
	 */
	private void setMGoodsCount(String countStr) {
		int c = Integer.parseInt(countStr);
		for (int i = 0; i < mGoods.size(); i++) {
			GoodsInfoForAdd goodInfo = mGoods.get(i);
			goodInfo.setSku_num(c);
		}
	}

	/**
	 * 获取从上个页面intent过来的信息
	 */
	private void getDataByIntent() {
		String str = mIntent.getStringExtra(IntentFlag.ORDER_CONFIRM_FLAG);
		// 获取订单提示
		OrderPrePayResponse resp = (OrderPrePayResponse) mIntent
				.getSerializableExtra(IntentKey.BUY_NOW_PREPAY); // 订单预结算接口返回字段
		String mess = resp.getPrompt();
		mFreightStr = resp.getPostage();
		mOrderMoneyStr = resp.getOrder_money();

		mWarn.setText(mess);
		mSendType.setText(mOrderController.getSendTypeStr(mFreightStr));

		mGoods = new ArrayList<GoodsInfoForAdd>();
		if (str.equals(IntentFlag.BUY_NOW)) {
			// 以立即购买方式跳转过来的商品
			PurchaseGoods goods = (PurchaseGoods) mIntent
					.getParcelableExtra(IntentKey.BUY_NOW_GOODS);
			String type = goods.getProduct_type();
			GoodsInfoForAdd goodsInfo = new GoodsInfoForAdd();
			goodsInfo.setSku_code(goods.getSku_code());
			goodsInfo.setProduct_code(goods.getProduct_code());
			goodsInfo.setSku_num(Integer.valueOf(goods.getOrder_count()));
			goodsInfo.setArea_code(mAreaCode); // 无用参数，任意填写
			mGoods.add(goodsInfo);

			if (type.equals(Constant.GOODS_TYPE_NORMAL)) {
				// 普通商品类型
				mActivityFlag = FLAG_BUY_BY_NOW;
				mOrderType = Constant.ORDER_TYPE_NORMAL;

				setGoodsInfoByNormal(goods);
				setTotalInfo("1"); // 编辑框的默认数量是1
			} else if (type.equals(Constant.GOODS_TYPE_LIMIT)) {
				// 限购商品类型
				mActivityFlag = FLAG_BUY_BY_TIMELIMIT;
				mOrderType = Constant.ORDER_TYPE_TIME;

				setGoodsInfoByTimeLimit(goods);
			}
		} else if (str.equals(IntentFlag.BUY_SHOPPING_CART)) {
			// 从购物车跳转过来
			mActivityFlag = FLAG_BUY_BY_CART;
			ArrayList<PurchaseGoods> list = mIntent
					.getParcelableArrayListExtra(IntentKey.SHOPPINGCART_GOODS);
			boolean isHasLimitType=false; //true表示购物车里含有限时抢购商品，false表示没有

			for (int i = 0; i < list.size(); i++) {
				PurchaseGoods goods = list.get(i);
				String type = goods.getProduct_type();
				if(type.equals(Constant.GOODS_TYPE_LIMIT)){
					isHasLimitType=true;
				}
				GoodsInfoForAdd goodsInfo = new GoodsInfoForAdd();
				goodsInfo.setSku_code(goods.getSku_code());
				goodsInfo.setProduct_code(goods.getProduct_code());
				goodsInfo.setSku_num(Integer.valueOf(goods.getOrder_count()));
				goodsInfo.setArea_code(mAreaCode); // 无用参数，任意填写
				mGoods.add(goodsInfo);
			}

			if(isHasLimitType){   //根据购物车是否含有限时抢购商品来判断订单类型
				mOrderType=Constant.ORDER_TYPE_TIME;         
			}else{
				mOrderType = Constant.ORDER_TYPE_NORMAL;
			}

			setGoodsInfoByCart(list);
		} else if (str.equals(IntentFlag.POSTAGETRYOUT)) {
			// 试用商品类型
			mActivityFlag = FLAG_BUY_BY_TRY;
			mOrderType = Constant.ORDER_TYPE_TRY;
			PurchaseGoods goods = (PurchaseGoods) mIntent
					.getParcelableExtra(IntentKey.BUY_NOW_GOODS);
			mSkuCodeByFree = goods.getSku_code(); // 取出skuCode判断商品状态

			GoodsInfoForAdd goodsInfo = new GoodsInfoForAdd();
			goodsInfo.setSku_code(goods.getSku_code());
			goodsInfo.setProduct_code(goods.getProduct_code());
			goodsInfo.setSku_num(Integer.valueOf(goods.getOrder_count()));
			goodsInfo.setArea_code(mAreaCode); // 无用参数，任意填写
			mGoods.add(goodsInfo);

			setGoodsInfoByFree(goods);
		}
	}

	@Override
	public void onNoNet() {
		super.onNoNet();
		mHub.dismiss();
	}

	@Override
	public void onNoTimeOut() {
		super.onNoTimeOut();
		mHub.dismiss();
	}

	/**
	 * 购物车回调
	 */
	@Override
	public void onShopCartCountChanged(List<GoodsEntity> productArray,
			int totalCount, int changeCount) {
		
	}

	@Override
	public void onWarning(String message) {
		
	}

	@Override
	public void onError(int code, String message) {
		
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		super.onActivityResult(reqCode, resCode, data);
		if(resCode==RESULT_OK){
			if(reqCode==0){
				int code=data.getIntExtra(IntentFlag.PAY_TYPE, 0);
				switch (code) {  
				case 0:
					mPayTypeFlag=Constant.PAY_WEIXIN;
					updatePayTypeUi();
					break;
				case 1:
					mPayTypeFlag=Constant.PAY_ZHIFUBAO;
					updatePayTypeUi();
					break;
				default:
					break;
				}
			}
			if(reqCode==IntentKey.REQUEST_CODE_ORDER_CONFIRM){
				mActivityResultFlag="yes";
				BeautyAddress addr=(BeautyAddress) data.getSerializableExtra(IntentKey.ADDRRECE_TO_ORDERCON__KEY);
				showAddrUiByBeautyAddr(addr);
			}
			
		}
		
	}
	
	private void updatePayTypeUi(){
		if(mPayTypeFlag.equals(Constant.PAY_WEIXIN)){
			mPayType.setText("微信支付");
			mPayPos=0;
		}else{
			mPayType.setText("支付宝");
			mPayPos=1;
		}
	}
	
	//支付宝支付方式
	private void alipayType(String payUrl, final String orderCode){
		PayUitl.payByAli(this, payUrl,
				new PayUitl.PayInterface() {

			@Override
			public void paySucess() {
				IntentUtil.startOrderSubmitSuccessActivity(
						OrderConfirmActivity.this,
						orderCode);
				finish();
			}

			@Override
			public void payFail() {
				IntentUtil.startOrderDetailActivity(
						OrderConfirmActivity.this,
						orderCode,Constant.ORDER_CONFIRM);
				finish();
			}
		});
	}
	
	//微信支付
	private void weixinPayType(String orderCode, MicroMessagePayment weixin){
		 PayUitl.payByWeiXin(this, orderCode, weixin.getPrepayid(), weixin.getNonceStr(), weixin.getTimeStamp(), weixin.getPackageValue(), weixin.getSign(),Constant.ORDER_CONFIRM);
	}
	
	@Override
	public void onScroll(int scrollY) {
		mRemark.clearFocus();
		if(mCountEt!=null){
			mCountEt.clearFocus();
		}
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.hideSoftInputFromWindow(mRemark.getWindowToken(), 0);
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
