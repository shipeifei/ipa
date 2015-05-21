package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.ApiOrderSellerDetailsResult;
import com.ipassistat.ipa.bean.response.entity.InvoiceInformationResult;
import com.ipassistat.ipa.bean.response.entity.MicroMessagePayment;

/**
 * 订单详情(家有接口)
 * @author renheng
 *
 */
public class OrderDetailResponse extends BaseResponse{

	private double fullSubtraction; //	 Double	满减	
	private String consigneeName; //	 String	收货人姓名	
	private String consigneeAddress; //	 String	收货人地址	
	private InvoiceInformationResult invoiceInformation; //	 InvoiceInformationResult	发票信息	
	private String firstFavorable; //	 String	首单优惠	
	private String alipaySign; //	 String	支付宝Sign	签名过的
	private List<ApiOrderSellerDetailsResult> orderSellerList; //	 ApiOrderSellerDetailsResult[]	订单商品列表	
	private String order_code; //	 String	订单编号	
	private double due_money; //	 Double	商品总金额	
	private String order_status; //	 String	订单状态	
	private String ifFlashSales; //	 String	是否为闪购订单	0:闪购 1:非闪购
	private String consigneeTelephone; //	 String	收货人电话	
	private String alipayUrl; //	 String	支付宝移动支付链接	
	private String failureTimeReminder; //	 String	失效时间提示	
	private double telephoneSubtraction; //	 Double	手机下单减少	
	private String pay_type; //	 String	支付方式	
	private double freight; //	 Double	运费	
	private String create_time; //	 String	下单时间	
	private String order_money	; // String	订单金额
	private String remark; //订单备注(否)
    private MicroMessagePayment micoPayment; //微信支付实体
    
	public double getFullSubtraction() {
		return fullSubtraction;
	}
	public void setFullSubtraction(double fullSubtraction) {
		this.fullSubtraction = fullSubtraction;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public InvoiceInformationResult getInvoiceInformation() {
		return invoiceInformation;
	}
	public void setInvoiceInformation(InvoiceInformationResult invoiceInformation) {
		this.invoiceInformation = invoiceInformation;
	}
	public String getFirstFavorable() {
		return firstFavorable;
	}
	public void setFirstFavorable(String firstFavorable) {
		this.firstFavorable = firstFavorable;
	}
	public String getAlipaySign() {
		return alipaySign;
	}
	public void setAlipaySign(String alipaySign) {
		this.alipaySign = alipaySign;
	}
	public List<ApiOrderSellerDetailsResult> getOrderSellerList() {
		return orderSellerList;
	}
	public void setOrderSellerList(List<ApiOrderSellerDetailsResult> orderSellerList) {
		this.orderSellerList = orderSellerList;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public double getDue_money() {
		return due_money;
	}
	public void setDue_money(double due_money) {
		this.due_money = due_money;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getIfFlashSales() {
		return ifFlashSales;
	}
	public void setIfFlashSales(String ifFlashSales) {
		this.ifFlashSales = ifFlashSales;
	}
	public String getConsigneeTelephone() {
		return consigneeTelephone;
	}
	public void setConsigneeTelephone(String consigneeTelephone) {
		this.consigneeTelephone = consigneeTelephone;
	}
	public String getAlipayUrl() {
		return alipayUrl;
	}
	public void setAlipayUrl(String alipayUrl) {
		this.alipayUrl = alipayUrl;
	}
	public String getFailureTimeReminder() {
		return failureTimeReminder;
	}
	public void setFailureTimeReminder(String failureTimeReminder) {
		this.failureTimeReminder = failureTimeReminder;
	}
	public double getTelephoneSubtraction() {
		return telephoneSubtraction;
	}
	public void setTelephoneSubtraction(double telephoneSubtraction) {
		this.telephoneSubtraction = telephoneSubtraction;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public double getFreight() {
		return freight;
	}
	public void setFreight(double freight) {
		this.freight = freight;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getOrder_money() {
		return order_money;
	}
	public void setOrder_money(String order_money) {
		this.order_money = order_money;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public MicroMessagePayment getMicoPayment() {
		return micoPayment;
	}
	public void setMicoPayment(MicroMessagePayment micoPayment) {
		this.micoPayment = micoPayment;
	}
	
	
}
