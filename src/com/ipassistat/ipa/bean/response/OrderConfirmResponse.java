package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.GoodsInfoForConfirm;

/**
 * 确认订单
 * @author renheng
 *
 */
public class OrderConfirmResponse extends BaseResponse{
	
	private double sent_money; //	 double	运费	运费
	private double pay_money; //	 double	实付款	实付款
	private String pay_type; //	 String	仓储地区所支持的支付方式	仓储地区所支持的支付方式 449716200001:在线支付,449716200002:货到付款
	private List<GoodsInfoForConfirm> resultGoodsInfo; //	 GoodsInfoForConfirm[]	商品列表	商品列表
	private double sub_money; //	 double	满减金额	满减金额
	private double cash_back; //	 double	返现	返现
	private double cost_money; //	 double	商品总金额	商品总金额
	private double first_cheap; //	 double	首单优惠	首单优惠
	private double phone_money; //	 double	手机下单减少金额	手机下单减少金额
	public double getSent_money() {
		return sent_money;
	}
	public void setSent_money(double sent_money) {
		this.sent_money = sent_money;
	}
	public double getPay_money() {
		return pay_money;
	}
	public void setPay_money(double pay_money) {
		this.pay_money = pay_money;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public List<GoodsInfoForConfirm> getResultGoodsInfo() {
		return resultGoodsInfo;
	}
	public void setResultGoodsInfo(List<GoodsInfoForConfirm> resultGoodsInfo) {
		this.resultGoodsInfo = resultGoodsInfo;
	}
	public double getSub_money() {
		return sub_money;
	}
	public void setSub_money(double sub_money) {
		this.sub_money = sub_money;
	}
	public double getCash_back() {
		return cash_back;
	}
	public void setCash_back(double cash_back) {
		this.cash_back = cash_back;
	}
	public double getCost_money() {
		return cost_money;
	}
	public void setCost_money(double cost_money) {
		this.cost_money = cost_money;
	}
	public double getFirst_cheap() {
		return first_cheap;
	}
	public void setFirst_cheap(double first_cheap) {
		this.first_cheap = first_cheap;
	}
	public double getPhone_money() {
		return phone_money;
	}
	public void setPhone_money(double phone_money) {
		this.phone_money = phone_money;
	}
	
	
	
}
