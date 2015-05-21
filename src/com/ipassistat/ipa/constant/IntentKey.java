package com.ipassistat.ipa.constant;

/**
 * intent key config
 * 
 * @author lxc
 * 
 */
public class IntentKey {
	/**
	 * 跳转到商品详情页面时传的商品对象key值
	 */
	public static final String INTENT_SALEPRODUCT = "INTENT_SALEPRODUCT";
	/**
	 * 商品id
	 */
	public static final String GOODS_ID = "GOODS_ID";
	/**
	 * 商品类型
	 */
	public static final String GOODS_TYPE = "GOODS_TYPE";
	/**
	 * 商品详情页面标记
	 */
	public static final String GOODS_DETAIL_FLAG = "GOODS_DETAIL_FLAG";
	/**
	 * 跳转到立即购买时传的对象的key值
	 */
	public static final String BUY_NOW_GOODS = "BUY_NOW_GOODS";
	/**
	 * 跳转到立即购买时传的对象的key值
	 */
	public static final String SHOPPINGCART_GOODS = "SHOPPINGCART_GOODS";
	/**
	 * 跳转到立即购买时传的预结算返回结果对象
	 */
	public static final String BUY_NOW_PREPAY = "BUY_NOW_PREPAY";
	/**
	 * /** 首页广告条 对应的网页地址。
	 */
	public static final String BANNER_DETAIL_URL = "BANNER_DETAIL_URL";
	/**
	 * 个人中心的个人资料是否有保存
	 */
	public static final String USERINFOSAVED = "USERINFOSAVED";
	/**
	 * 预结算的商品列表
	 */
	public static String PREORDER = "com.ichsy.mboss.preorder";
	public static String PREORDERRESPONSE = "com.ichsy.mboss.preorderresponse";

	/**
	 * 跳转到订单详情
	 */
	public static String ORDER_DETAIL = "ORDER_DETAIL";

	/**
	 * 订单详情的item的商品ID传入到商品详情
	 */
	public static String ORDER_DETAIL_SKU = "ORDER_DETAIL_SKU";

	/**
	 * 订单详情的item的商品类型传入到商品详情
	 */
	public static String ORDER_DETAIL_TYPE = "ORDER_DETAIL_TYPE";

	/**
	 * 跳转到商品评价的订单信息
	 */
	public static String GOODS_COMMENT_ORDER = "GOODS_COMMENT_ORDER";

	/**
	 * 跳转到商品评价的商品信息
	 */
	public static String GOODS_COMMENT_GOODS = "GOODS_COMMENT_GOODS";

	/**
	 * 跳转到商品评价
	 */
	public static final String GOODS_COMMENT_SKU_CODE = "GOODS_COMMENT_SKU_CODE"; // 商品SKU编号
	public static final String GOODS_COMMENT_THUMB = "GOODS_COMMENT_THUMB"; // 商品缩略图
	public static final String GOODS_COMMENT_NAME = "GOODS_COMMENT_NAME"; // 商品名称
	public static final String GOODS_COMMENT_DESCRIBE = "GOODS_COMMENT_DESCRIBE"; // 商品描述
	public static final String GOODS_COMMENT_ORDER_TIME = "GOODS_COMMENT_ORDER_TIME"; // 订单交易时间
	public static final String GOODS_COMMENT_ORDER_CODE = "GOODS_COMMENT_ORDER_CODE"; // 订单交易时间

	/**
	 * 跳转到订单提交成功页面
	 */
	public static final String ORDER_SUCCESS_ORDER_CODE = "ORDER_SUCCESS_ORDER_CODE"; // 订单号
	public static final String ORDER_SUCCESS_ORDER_MONEY = "ORDER_SUCCESS_ORDER_MONEY"; // 订单金额

	/**
	 * 跳转到试用详情
	 */
	public static final String TRYOUTINFO_ENDTIME = "TRYOUTINFO_ENDTIME";
	public static final String TRYOUTINFO_SKU_CODE = "TRYOUTINFO_SKU_CODE";
	public static final String TRYOUTINFO_WHICH = "TRYOUTINFO_WHICH";
	
	/**
	 * 收获地址列表跳转到新增地址是否有收获地址的key
	 */
	public static final String IS_HAS_DEFAULT_ADDRESS="IS_HAS_DEFAULT_ADDRESS";
	/**
	 * 引导页跳到注册页的key
	 */
	public static final String INTENT_REGISTER = "INTENT_REGISTER";
	
	/**
	 * 确认订单页startActivityForResult的请求码
	 */
	public static final int REQUEST_CODE_ORDER_CONFIRM=100;
	
	/**
	 * 地址列表页向确认订单页返回的数据的key
	 */
	public static final String ADDRRECE_TO_ORDERCON__KEY="ORDERCON_TO_ADDRRECE_KEY";
	
	/**
	 * 跳转到订单详情页的标识
	 */
	public static final String INTENT_ORDERDETAIL_FLAG_KEY="INTENT_ORDERDETAIL_FLAG_KEY";
	
	/**
	 * 跳转到视频列表页面
	 */
	public static final String RECREATION_TVLIST_TITLE = "TVLIST_TILTE_NAME";
	
	/***
	 * 选择的邮箱类别
	 */
	public static final String EMAIL_TYPE = "EMAIL_TYPE";
}
