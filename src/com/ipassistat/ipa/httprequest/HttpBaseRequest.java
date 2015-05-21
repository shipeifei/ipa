package com.ipassistat.ipa.httprequest;


/***
 * 
 * com.ipassistat.ipa.dao.BaseDao
 * @author 时培飞 
 * Create at 2015-4-24 下午4:56:26
 */
public class HttpBaseRequest implements HttpRequestLisenter {
	
	public HttpRequestLisenter businessCallBack;
	

	public HttpBaseRequest(HttpRequestLisenter serviceCallBack) {
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
