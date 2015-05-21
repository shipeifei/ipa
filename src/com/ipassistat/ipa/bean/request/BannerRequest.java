package com.ipassistat.ipa.bean.request;
/**
 * 广告列表
 * @author lxc
 *
 */
public class BannerRequest extends BaseRequest{

	/**
	 * Col140721100002 栏目ID;
	 */
	private String column_code;

	public String getColumn_code() {
		return column_code;
	}

	public void setColumn_code(String column_code) {
		this.column_code = column_code;
	}	 
	
	
}
