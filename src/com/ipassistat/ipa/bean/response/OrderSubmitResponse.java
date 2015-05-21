package com.ipassistat.ipa.bean.response;

import com.ipassistat.ipa.bean.response.entity.MicroMessagePayment;

/**
 * 提交订单
 * @author renheng
 *
 */
public class OrderSubmitResponse extends BaseResponse{
	
	private String pay_url; //	 String	支付链接	支付链接
	private String sign_detail; //	 String	支付宝返回的sign信息	支付宝返回的sign信息
	private String order_code; //	 String	订单编号	订单编号	
	private MicroMessagePayment micoPayment; //微信支付返回参数 
	private int flag; //	 int	是否零元支付	1代表零元支付，0代表正常支付
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getPay_url() {
		return pay_url;
	}
	public void setPay_url(String pay_url) {
		this.pay_url = pay_url;
	}
	public String getSign_detail() {
		return sign_detail;
	}
	public void setSign_detail(String sign_detail) {
		this.sign_detail = sign_detail;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public MicroMessagePayment getMicoPayment() {
		return micoPayment;
	}
	public void setMicoPayment(MicroMessagePayment micoPayment) {
		this.micoPayment = micoPayment;
	}

	
}
