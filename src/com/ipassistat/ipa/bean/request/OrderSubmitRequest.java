package com.ipassistat.ipa.bean.request;

import java.util.List;

import com.ipassistat.ipa.bean.request.entity.BillInfo;
import com.ipassistat.ipa.bean.request.entity.GoodsInfoForAdd;

/**
 * 提交订单
 * 
 * @author renheng
 * 
 */
public class OrderSubmitRequest extends BaseRequest {

	private double check_pay_money; //	 double	是	8888.88	应付款	应付款
	private List<GoodsInfoForAdd>goods; //	 GoodsInfoForAdd[]	是		商品列表	不可为空
	private String buyer_address_code; //	 String	是		收货人地址编号	收货人地址所在地区选择的第三级编号
	private String buyer_name;//	 String	是		收货人姓名	收货人姓名
	private String	buyer_mobile; //	 String	是	13333100204	收货人手机号	手机号

	private String pay_type; //	 String	是	449716200001:在线支付,449716200002:货到付款	支付方式	支付方式
	private String buyer_address; //	 String	是		收货人地址	收货人地址
	private String app_vision; //	 String	是	1.0.0	app版本信息	app版本信息
	private BillInfo   billInfo; //	 BillInfo	是		发票信息	发票信息
	private String order_type; //	 String	是	449715200003	订单类型	449715200003试用订单、449715200004闪购订单、449715200005普通订单
	private String remark; //订单备注  字段名等接口出过之后再改
	private String browserUrl; //浏览器的IP地址
	
	public String getBrowserUrl() {
		return browserUrl;
	}
	public void setBrowserUrl(String browserUrl) {
		this.browserUrl = browserUrl;
	}
	public double getCheck_pay_money() {
		return check_pay_money;
	}
	public void setCheck_pay_money(double check_pay_money) {
		this.check_pay_money = check_pay_money;
	}
	public List<GoodsInfoForAdd> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodsInfoForAdd> goods) {
		this.goods = goods;
	}
	public String getBuyer_address_code() {
		return buyer_address_code;
	}
	public void setBuyer_address_code(String buyer_address_code) {
		this.buyer_address_code = buyer_address_code;
	}
	public String getBuyer_name() {
		return buyer_name;
	}
	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}
	public String getBuyer_mobile() {
		return buyer_mobile;
	}
	public void setBuyer_mobile(String buyer_mobile) {
		this.buyer_mobile = buyer_mobile;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getBuyer_address() {
		return buyer_address;
	}
	public void setBuyer_address(String buyer_address) {
		this.buyer_address = buyer_address;
	}
	public String getApp_vision() {
		return app_vision;
	}
	public void setApp_vision(String app_vision) {
		this.app_vision = app_vision;
	}
	public BillInfo getBillInfo() {
		return billInfo;
	}
	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	

}
