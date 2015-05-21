package com.ipassistat.ipa.httprequest;

/***
 * 网络请求接口
 * com.ipassistat.ipa.dao.BusinessInterface
 * @author 时培飞 
 * Create at 2015-4-24 下午4:56:04
 */
public interface HttpRequestLisenter {

	public void onMessageSucessCalledBack(String url,Object object);
	
	public void onMessageFailedCalledBack(String url,Object object);
	
	public void onError(String ur,String result);
	
	/**
	 * 无网状态
	 * @param ur
	 * @param result
	 */
	public void onNoNet();
	
	/**
	 * 网络超时回调，（只用来处理逻辑显示，不用进行提示）
	 * @param ur
	 * @param result
	 */
	public void onNoTimeOut();
	
	/**
	 * 结束方法，无论请求网络成功还是失败，都会调用此方法。
	 */
	public void onFinish();
}
