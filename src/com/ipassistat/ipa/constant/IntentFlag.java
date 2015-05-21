package com.ipassistat.ipa.constant;

public class IntentFlag {

	/**
	 * 编辑收货地址
	 */
	public static final String ADDRESS_EDIT = "ADDRESS_EDIT";

	/**
	 * 官方活动连接
	 */
	public static final String OFFICIAL_URL = "OFFICIAL_URL";
	public static final String WEBVIEW_BUNDLE_UMCODE = "webview_um_code";
	public static final String OFFICIAL_BACK = "OFFICIAL_BACK";
	/**
	 * 分享实体类
	 */
	public static final String SHAREINFO_ENTITY = "SHAREINFO_ENTITY";
	/**
	 * 官方活动标题
	 */
	public static final String OFFICIAL_TITLE = "OFFICIAL_TITLE";
	/**
	 * 跳转确认订单页面标识
	 */
	public static final String ORDER_CONFIRM_FLAG = "ORDER_CONFIRM_FLAG";
	/**
	 * 立即购买
	 */
	public static final String BUY_NOW = "BUY_NOW";
	/**
	 * 付邮试用生成订单
	 */
	public static final String POSTAGETRYOUT = "POSTAGETRYOUT";
	/**
	 * 去结算
	 */
	public static final String BUY_SHOPPING_CART = "BUY_SHOPPING_CART";

	/**
	 * 选择地址返回
	 */
	public static final String ADDRESS_SELECT_RESULT = "ADDRESS_SELECT";

	/**
	 * 选择地址请求key
	 */
	public static final String ADDRESS_SELECT_REQUEST_KEY = "ADDRESS_SELECT_REQUEST";

	/**
	 * 选择地址请求value
	 */
	public static final String ADDRESS_SELECT_REQUEST_VALUE = "ADDRESS_SELECT_REQUEST_VALUE";
	/**
	 * 普通商品详情
	 */
	public static final String GOODS_DETAIL_NORMAL = "GOODS_DETAIL_NORMAL";
	/**
	 * 限时抢购商品详情
	 */
	public static final String GOODS_DETAIL_TIMELIMIT = "GOODS_DETAIL_TIMELIMIT";
	/**
	 * 试用中心商品详情-免邮
	 */
	public static final String GOODS_DETAIL_TRY_FREE = "GOODS_DETAIL_TRY";
	/**
	 * 试用中心商品详情-付邮
	 */
	public static final String GOODS_DETAIL_TRY_POSTAGE = "GOODS_DETAIL_TRY_POSTAGE";
	/**
	 * 头像长传路径
	 */
	public static final String PHOTO_URL = "PHOTO_URL";

	public static final String INTENT_FROM = "INTENT_FROM";

	/**
	 * 验证码类型对象
	 * 
	 * @author lxc
	 * 
	 */
	public enum IdentifyCodeType {
		forgetPwd, resetPwd, register
	}

	/**
	 * 跳到登录页的类型。
	 * 
	 * @author Administrator
	 * 
	 */
	public enum LoginType {
		resetPwd, // 设置页面的密码修改 跳到登录
		goToTinyCommunity// 点击进入微公社，如果没有登录，必须先跳转到登录，然后登录成功后跳转到微公社页面。
	}

	/**
	 * 请求默认收货地址
	 */
	public static final String TRAOUTINFO = "TRAOUTINFO";

	/**
	 * 跳转到TrialInfoActivity的标识
	 */
	public static final String TRIAL_INFO_FLAG_FROM_ACTIVITY = "TRIAL_INFO_FLAG_FROM_ACTIVITY";

	/**
	 * AddresReceiveActivity类的标识
	 */
	public static final String ADDRESS_RECEIVE_ACTIVITY = "ADDRESS_RECEIVE_ACTIVITY";

	/**
	 * 判断用户在当前页面是否有操作的标识
	 */
	public static final String IS_OPERATION_FLAG = "IS_OPERATION_FLAG";

	/**
	 * 收获地址列表跳转到新增地址是否有收获地址的标识
	 */
	public static final String HAS_DEFAULT_ADDR_FLAG = "HAS_DEFAULT_ADDR_FLAG"; // 有默认收获地址
	public static final String NO_DEFAULT_ADDR_FLAG = "NO_DEFAULT_ADDR_FLAG"; // 没有默认收货地址
	/**
	 * 引导页跳到注册页的标识
	 */
	public static final String INTENT_FROM_GUIDE = "INTENT_FROM_GUIDE";

	/**
	 * 支付方式setResult返回的intent标记
	 */
	public static final String PAY_TYPE = "PAY_TYPE";
	public static final String PAY_TYPE_CONTENT = "PAY_TYPE_CONTENT";
	public static final String PAY_TYPE_CHOICE = "PAY_TYPE_CHOICE";

}
