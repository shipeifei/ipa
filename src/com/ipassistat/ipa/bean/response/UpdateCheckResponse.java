package com.ipassistat.ipa.bean.response;
/**
 * 检查升级返回的对象
 * @author lxc
 *
 */
public class UpdateCheckResponse extends BaseResponse{
	/**
	 * 升级连接	App下载链接地址
	 */
	private String appUrl;
	/**
	 * String	升级方式	参数说明：0、代表调用失败，1、代表强制升级，2、代表不强制升级，3、代表不用升级
	 */
	private String upgradeSelect;
	/**
	 * 升级版本号	用户设定升级到的版本号
	 */
	private String appVersion;
	/**
	 * 升级内容
	 */
	private String upgradeContent;
	
	

	
	public String getUpgradeContent() {
		return upgradeContent;
	}
	public void setUpgradeContent(String upgradeContent) {
		this.upgradeContent = upgradeContent;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public String getUpgradeSelect() {
		return upgradeSelect;
	}
	public void setUpgradeSelect(String upgradeSelect) {
		this.upgradeSelect = upgradeSelect;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
}
