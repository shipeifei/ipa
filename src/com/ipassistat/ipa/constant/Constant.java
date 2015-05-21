package com.ipassistat.ipa.constant;

/**
 * 全局不可变常量
 * 
 * @Package com.ichsy.mboss.constant
 * 
 * @File Constant.java
 * 
 * @author LiuYuHang Date: 2015年2月3日
 *
 *         Modifier： Modified Date： Modify：
 * 
 *         Copyright @ 2015 Corpration CHSY
 *
 */
public class Constant {

	public static String DEFAULT_DEBUG_TAG = "znms";
	
	public static String PERSON_MAN = "4497465100010002"; //男 
	public static String PERSON_WOMAN= "4497465100010003";//女
	
	/*
	 * 订单类型
	 */
	public static String ORDER_TYPE_TRY = "449715200003"; // 试用订单
	public static String ORDER_TYPE_TIME = "449715200004"; // 闪购订单
	public static String ORDER_TYPE_NORMAL = "449715200005";// 普通订单

	/*
	 * 订单来源
	 */
	public static String ORDER_SOURCE = "449715190002"; // 安卓订单

	/*
	 * 快递方式
	 */
	public static String KUAI_DI = "449715210001"; // 快递
	public static String POST_OFFICE = "449715210002"; // 邮局

	/*
	 * 支付方式
	 */
	// public static String PAY_ONLINE = "449716200001"; // 支付宝支付
	public static String PAY_ZHIFUBAO = "449716200001"; // 支付宝支付
	public static String PAY_OFFLINE = "449716200002"; // 货到付款
	public static String PAY_WEIXIN = "449716200004"; // 微信支付
	public static String PAY_JIFEN = "449716200003"; // 积分支付

	/*
	 * 订单状态
	 */
	public static String ORDER_STATU_NO_PAY = "4497153900010001"; // 下单成功-未付款
	public static String ORDER_STATU_NO_SEND = "4497153900010002"; // 下单成功-未发货(已付款)
	public static String ORDER_STATU_HAS_SEND = "4497153900010003"; // 已发货
	public static String ORDER_STATU_HAS_RECEIVE = "4497153900010004"; // 已收货
	public static String ORDER_STATU_SUCCESS = "4497153900010005"; // 交易成功
	public static String ORDER_STATU_FAIL = "4497153900010006"; // 交易关闭

	/*
	 * 商品类型
	 */
	public static String GOODS_TYPE_NORMAL = "0"; // 普通商品
	public static String GOODS_TYPE_LIMIT = "1"; // 限购商品
	public static String GOODS_TYPE_FREE = "2"; // 免费试用商品
	public static String GOODS_TYPE_POST = "3"; // 付邮试用商品
	/**
	 * 我的试用的状态
	 */
	public static final String TRYPROCESSING_FREE = "449746890002"; // 审核中
	public static final String TRYSUCCESS_FREE = "449746890003"; // 申请成功
	public static final String TRYFAIL_FREE = "449746890004"; // 申请失败

	public static final String TRYSUCCESS_POSTAGE = "449746890006"; // 申请成功
	public static final String TRYFAIL_POSTAGE = "449746890004"; // already end

	/**
	 * 删除全部收藏
	 */
	public static final String DELETEALL = "1";
	/**
	 * 删除单条收藏
	 */
	public static final String DELETEONE = "0";

	/**
	 * 试用类型：免费
	 */
	public static final String TRIAL_FREE = "449746930003";
	/**
	 * 试用类型：付邮
	 */
	public static final String TRIAL_POSTAGE = "449746930002";

	/**
	 * 未分享
	 */
	public static final String UNSHARED = "0";

	/**
	 * 已分享
	 */
	public static final String SHARED = "1";

	/**
	 * 有默认收货地址
	 */
	public static final String HASDEFAULTADDRESS = "1";

	/**
	 * 没有默认收货地址
	 */
	public static final String HASNOTDEFAULTADDRESS = "0";

	/**
	 * 商品收藏状态
	 */
	public static final String FAVSTATUS_TRUE = "1";// 已收藏过
	public static final String FAVSTATUS_FALSE = "0";// 未收藏过

	/**
	 * 商品库存状态
	 */
	public static final String STOCK_LACK = "0";// 库存不足
	public static final String STOCK_FULL = "1";// 库存充足

	/**
	 * 商品状态
	 */
	public static final String GOODS_WAIT_PUTAWAY = "4497153900060001";// 待上架
	public static final String GOODS_HAVE_PUTAWAY = "4497153900060002";// 已上架
	public static final String GOODS_MERCHANT_SOLD_OUT = "4497153900060003";// 商家下架
	public static final String GOODS_PLATFORM_SOLD_OUT = "4497153900060004";// 平台强制下架

	public static boolean IS_OPERATION_FLAG = false; // 当前页面是否有操作的标识

	/**
	 * 商品有效状态
	 */
	public static final String GOODS_VALID = "1";// 有效
	public static final String GOODS_IN_VALID = "0";// 无效

	/**
	 * 传到微信支付回调页面的页面标识
	 */
	public static final String ORDER_CONFIRM = "ORDER_CONFIRM"; // 订单确认页
	public static final String ORDER_DETAIL = "ORDER_DETAIL"; // 订单详情页
	public static final String ORDER_LIST = "ORDER_LIST"; // 订单列表页

	public static final String ORDER_SUBMIT_SUCCESS = "ORDER_SUBMIT_SUCCESS";// 提交订单成功页

}
