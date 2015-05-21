package com.ipassistat.ipa.dao;


/***
 * 
 * com.ipassistat.ipa.dao.BaseDao
 * @author 时培飞 
 * Create at 2015-4-24 下午4:56:26
 */
public class BaseDao implements BusinessInterface {
	
	public BusinessInterface businessCallBack;
	

	public BaseDao(BusinessInterface serviceCallBack) {
		super();
		this.businessCallBack = serviceCallBack;
	}

	@Override
	public void onMessageSucessCalledBack(String url,Object object) {
		
		if (null == object) {
			businessCallBack.onError(url, "");
			businessCallBack = null;
			return;
		}
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		
		if (null == object) {
			businessCallBack.onError(url, "error ******");
			businessCallBack = null;
			return;
		}
	}

	@Override
	public void onError(String ur, String result) {
		
		
	}

	@Override
	public void onNoNet() {
		
		
	}

	@Override
	public void onNoTimeOut() {
		
		
	}

	@Override
	public void onFinish() {
		
		
	}

	
}
