package com.ipassistat.ipa.bean.response.entity;

/**
 * 赠品列表 (家有接口)
 * @author renheng
 *
 */
public class ApiOrderDonationDetailsResult {
	private String pictureUrl; //	 String	图片链接	
	private String detailsName; //	 String	名称	
	private String productCode; //	 String	商品编号	
	private String detailsNumber; //	 String	数量	
	private int resultCode; //	 int	返回标记	
	private String resultMessage; //	 String	返回消息
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getDetailsName() {
		return detailsName;
	}
	public void setDetailsName(String detailsName) {
		this.detailsName = detailsName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getDetailsNumber() {
		return detailsNumber;
	}
	public void setDetailsNumber(String detailsNumber) {
		this.detailsNumber = detailsNumber;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	
}
