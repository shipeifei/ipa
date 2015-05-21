package com.ipassistat.ipa.business;

import java.util.List;

import android.content.Context;

import com.ipassistat.ipa.bean.local.RequestOptions;
import com.ipassistat.ipa.bean.request.GoodsReceiveConfirmRequest;
import com.ipassistat.ipa.bean.request.OrderConfirmRequest;
import com.ipassistat.ipa.bean.request.OrderDetailRequest;
import com.ipassistat.ipa.bean.request.OrderListRequest;
import com.ipassistat.ipa.bean.request.OrderPrePayRequest;
import com.ipassistat.ipa.bean.request.OrderRemoveRequest;
import com.ipassistat.ipa.bean.request.OrderSubmitRequest;
import com.ipassistat.ipa.bean.request.entity.BillInfo;
import com.ipassistat.ipa.bean.request.entity.GoodsInfoForAdd;
import com.ipassistat.ipa.bean.request.entity.PurchaseGoods;
import com.ipassistat.ipa.bean.response.GoodsReceiveConfirmResponse;
import com.ipassistat.ipa.bean.response.OrderConfirmResponse;
import com.ipassistat.ipa.bean.response.OrderDetailResponse;
import com.ipassistat.ipa.bean.response.OrderListResponse;
import com.ipassistat.ipa.bean.response.OrderPrePayResponse;
import com.ipassistat.ipa.bean.response.OrderRemoveResponse;
import com.ipassistat.ipa.bean.response.OrderSubmitResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.util.NetUtil;

/**
 * 订单模块
 * 
 * @author renheng
 * 
 */
public class OrderModule extends BaseModule {

	public OrderModule(BusinessInterface dataCallBack) {
		super(dataCallBack);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取订单列表
	 * 
	 * @param context
	 * @param nextPage
	 *            下一页
	 * @param buyer_code
	 *            买家编号
	 * @param order_status
	 *            订单状态(非必填)
	 */
	public void postOrderList(Context context, String nextPage,
			String buyer_code) {

		OrderListRequest req = new OrderListRequest();
		req.setNextPage(nextPage);
		req.setBuyer_code(buyer_code);
		req.setBrowserUrl(NetUtil.getPhoneIp());

		getDataByPost(context, ConfigInfo.API_ORDER_LIST, req,
				OrderListResponse.class);

	}

	/**
	 * 请求预结算接口数据
	 * 
	 * @param context
	 * @param list
	 *            商品信息
	 * @param order_money
	 *            订单总价
	 */
	public void postOrderSettlement(Context context, List<PurchaseGoods> list,
			String order_money) {
		OrderPrePayRequest request = new OrderPrePayRequest();
		request.setPurchaseGoods(list);
		request.setOrder_money(order_money);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = false;
		getDataByPost(context, ConfigInfo.API_ORDER_SETTLEMENT, request,requestOptions,
				OrderPrePayResponse.class);

	}

	/**
	 * 提交订单接口
	 * 
	 * @param context
	 * @param check_pay_money
	 *            应付款
	 * @param goods
	 *            商品列表
	 * @param buyer_address_code
	 *            收货人地址第三级 区编号
	 * @param buyer_name
	 *            收货人姓名
	 * @param buyer_mobile
	 *            收货人手机号
	 * @param pay_type
	 *            支付方式
	 * @param buyer_address
	 *            收货人地址
	 * @param app_vision
	 *            app版本信息
	 * @param order_type
	 *            订单类型
	 * @param remark
	 *            订单备注
	 */
	public void postOrderSubmit(Context context, double check_pay_money,
			List<GoodsInfoForAdd> goods, String buyer_address_code,
			String buyer_name, String buyer_mobile, String pay_type,
			String buyer_address, String app_vision, String order_type, String remark, String ip) {

		OrderSubmitRequest req = new OrderSubmitRequest();
		req.setCheck_pay_money(check_pay_money);
		req.setGoods(goods);
		req.setBuyer_address_code(buyer_address_code);
		req.setBuyer_name(buyer_name);
		req.setBuyer_mobile(buyer_mobile);
		req.setPay_type(pay_type);
		req.setBuyer_address(buyer_address);
		req.setApp_vision(app_vision);
		req.setOrder_type(order_type);
		req.setRemark(remark);
		req.setBrowserUrl(ip);

		// 无发票信息，传空
		BillInfo info = new BillInfo();
		info.setBill_detail("");
		info.setBill_title("");
		info.setBill_Type("");
		req.setBillInfo(info);
		
		RequestOptions options = new RequestOptions();
		options.errorToast=false;

		getDataByPost(context, ConfigInfo.API_ORDER_SUBMIT, req, options,
				OrderSubmitResponse.class);
	}

	/**
	 * 获取订单详情
	 * 
	 * @param context
	 * @param order_code
	 *            订单编号
	 */
	public void postOrderDetail(Context context, String order_code) {
		OrderDetailRequest req = new OrderDetailRequest();
		req.setOrder_code(order_code);
		req.setBuyer_code("1"); // 非空 任何值
		req.setBrowserUrl(NetUtil.getPhoneIp());
		getDataByPost(context, ConfigInfo.API_ORDER_DETAIL, req,
				OrderDetailResponse.class);
	}

	/**
	 * 删除订单
	 * 
	 * @param context
	 * @param order_code
	 *            订单编号
	 */
	public void postOrderRemove(Context context, String order_code) {
		OrderRemoveRequest req = new OrderRemoveRequest();
		req.setOrder_code(order_code);
		getDataByPost(context, ConfigInfo.API_ORDER_REMOVE, req,
				OrderRemoveResponse.class);
	}

	/**
	 * 确认收货
	 * 
	 * @param context
	 * @param order_code
	 *            订单编号
	 */
	public void postGoodsReceiveConfirm(Context context, String order_code) {
		GoodsReceiveConfirmRequest req = new GoodsReceiveConfirmRequest();
		req.setOrderCode(order_code);
		getDataByPost(context, ConfigInfo.API_GOODS_RECEIVE_CONFIRM, req,
				GoodsReceiveConfirmResponse.class);
	}

	/**
	 * 请求预结算接口数据
	 * 
	 * @param context
	 * @param list
	 *            商品信息
	 * @param order_money
	 *            订单总价
	 */
	public void postOrderSettlement(Context context, List<PurchaseGoods> list,
			String order_money, String type) {
		OrderPrePayRequest request = new OrderPrePayRequest();
		request.setPurchaseGoods(list);
		request.setOrder_money(order_money);
		request.setType(type);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = false;
		getDataByPost(context, ConfigInfo.API_ORDER_SETTLEMENT, request,requestOptions,
				OrderPrePayResponse.class); // 不弹出其它信息

	}
	
	/**
	 * 请求确认订单数据
	 * @param context
	 * @param goods 商品列表
	 * @param order_type 订单类型
	 */
	public void postOrderConfirm(Context context, List<GoodsInfoForAdd> goods, String order_type){
		OrderConfirmRequest req = new OrderConfirmRequest();
		req.setGoods(goods);
		req.setOrder_type(order_type);
		getDataByPost(context, ConfigInfo.API_ORDER_CONFIRM, req, OrderConfirmResponse.class);
	}
	
	
	
}
