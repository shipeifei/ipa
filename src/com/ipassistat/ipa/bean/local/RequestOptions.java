package com.ipassistat.ipa.bean.local;

public class RequestOptions {

	/**
	 * 是否需要超时的toast
	 */
	public boolean timeOutToast = true;
	/**
	 * 是否需要code不等于200的toast
	 */
	public boolean errorToast = true;
	/**
	 * 是否需要无网的toast
	 */
	public boolean noNetToast = true;

	/**
	 * 超时时间
	 */
	public int timeOut;
	
	/**
	 * 是否是带缓存的网络请求
	 */
	private boolean isCacheQuestOption;

	public boolean isCacheQuestOption() {
		return isCacheQuestOption;
	}

	public void setCacheQuestOption(boolean isCacheQuestOption) {
		this.isCacheQuestOption = isCacheQuestOption;
	}
	
}
