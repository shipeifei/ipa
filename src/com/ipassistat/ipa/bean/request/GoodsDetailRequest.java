package com.ipassistat.ipa.bean.request;
/**
 * 商品详情应用输入参数
 * @author wr
 *
 */
public class GoodsDetailRequest extends BaseRequest{
	private String sku_code;//sku编码
	private String width;//屏幕宽度

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	
}
