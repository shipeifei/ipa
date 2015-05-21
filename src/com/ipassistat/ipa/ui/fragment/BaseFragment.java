package com.ipassistat.ipa.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.util.http.HttpContext;
import com.ipassistat.ipa.util.http.RequestListener;

/**
 * 所有fragment的父类
 * 
 * @author lxc
 *
 */
public class BaseFragment extends Fragment implements BusinessInterface, RequestListener {
	private boolean isFragmentFirstVisible = true;
	private boolean isVisible;

	private boolean isCreate = false;
//	protected FragmentManager mFm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
//		mFm = getFragmentManager();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (isFragmentFirstVisible && isVisible) {
			onLazyInit();
		}
		isFragmentFirstVisible = false;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		this.isVisible = isVisibleToUser;
		if (isVisibleToUser) {
			if (!isFragmentFirstVisible) {
				onLazyInit();
			}
		}
	}
	
	private void onLazyInit() {
		if (!isCreate) {
			onLazyCreate();
			onLazyResume();
			isCreate = true;
		} else {
			onLazyResume();
		}
	}

	/**
	 * fragment的懒加载生命周期，进入当前fragment才会调用，类似activity的onCreate
	 *
	 * @author LiuYuHang
	 * @date 2015年1月15日
	 */
	protected void onLazyCreate() {}

	/**
	 * fragment的懒加载生命周期，进入当前fragment才会调用，类似activity的onResume
	 *
	 * @author LiuYuHang
	 * @date 2015年1月15日
	 */
	protected void onLazyResume() {}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		// ((BaseActivity) getActivity()).dismissProgressDialog();
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		// ((BaseActivity) getActivity()).dismissProgressDialog();

	}

	@Override
	public void onError(String ur, String result) {
		// ((BaseActivity) getActivity()).dismissProgressDialog();

	}

	@Override
	public String toString() {
		
		return getClass().getName();
	}

	@Override
	public void onNoNet() {
		// ((BaseActivity) getActivity()).dismissProgressDialog();

	}

	@Override
	public void onNoTimeOut() {
		// ((BaseActivity) getActivity()).dismissProgressDialog();
	}

	@Override
	public void onFinish() {
		

	}

	@Override
	public void onHttpRequestBegin(String url) {
		

	}

	@Override
	public void onHttpRequestSuccess(String url, HttpContext httpContext) {
		

	}

	@Override
	public void onHttpRequestFailed(String url, HttpContext httpContext) {
		

	}

	@Override
	public void onHttpRequestComplete(boolean success, String url, HttpContext httpContext) {
		

	}

	@Override
	public void onHttpRequestCancel(String url, HttpContext httpContext) {
		
		
	}

}
