package com.ipassistat.ipa.bean.response.entity;

/**
 * 发票信息(家有接口)
 * @author renheng
 *
 */
public class InvoiceInformationResult {
	private String invoiceInformationValue; //	 String	发票内容	
	private String invoiceInformationTitle; //	 String	抬头	
	private String invoiceInformationType; //	 String	发票类型
	public String getInvoiceInformationValue() {
		return invoiceInformationValue;
	}
	public void setInvoiceInformationValue(String invoiceInformationValue) {
		this.invoiceInformationValue = invoiceInformationValue;
	}
	public String getInvoiceInformationTitle() {
		return invoiceInformationTitle;
	}
	public void setInvoiceInformationTitle(String invoiceInformationTitle) {
		this.invoiceInformationTitle = invoiceInformationTitle;
	}
	public String getInvoiceInformationType() {
		return invoiceInformationType;
	}
	public void setInvoiceInformationType(String invoiceInformationType) {
		this.invoiceInformationType = invoiceInformationType;
	}

	
}
