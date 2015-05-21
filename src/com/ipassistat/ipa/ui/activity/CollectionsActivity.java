package com.ipassistat.ipa.ui.activity;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.GoodsListviewAdapter;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.CollectionResponse;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.DialogView;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.PaginationListView.PaginationListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

public class CollectionsActivity extends BaseActivity implements OnItemClickListener, PaginationListener, OnItemLongClickListener {
	
	
	private Activity mActivity;
	private String mStringTitle;
	private TitleBar mTitlebar;
	private PaginationListView mGoodsList;
	private GoodsListviewAdapter mAdapter;
	private UserModule mModel;
	private CollectionResponse mResponse;
	private boolean isClearAll = false;
	private int mSelectItem = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collections);
		mActivity = this;
		initView();
		initDatas();
		registerListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1019"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1019"); // 保证 onPageEnd 在onPause 之前调用,因为

		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (ConfigInfo.API_MYCOLLECTION.equals(url)) {
			mResponse = (CollectionResponse) object;
			int page = (Integer) mResponse.getTag();
			if (mAdapter == null || page == 0) {
				mAdapter = new GoodsListviewAdapter(this, mResponse.getProducts(), View.VISIBLE);
				mGoodsList.setAdapter(mAdapter);
			} else {
				mAdapter.loadNextPage(mResponse.getProducts());
			}
			mGoodsList.delegatePageCheck(mResponse.getPaged());
		} else if (ConfigInfo.API_FAVDELETEAPI.equals(url)) {
			BaseResponse result = (BaseResponse) object;
			if (result.getResultCode() == ConfigInfo.RESULT_OK) {
				if (isClearAll) {
					mAdapter = new GoodsListviewAdapter(this, null, View.VISIBLE);
					mGoodsList.setAdapter(mAdapter);
					ToastUtil.showToast(mActivity, getString(R.string.collection_clear_all_over));
				} else {
					List<SaleProduct> data = mAdapter.getData();
					if (data != null && data.size() >= 5) { // 数据大于五条的时候
						mAdapter.removeItem(mSelectItem);
					} else {
						onRefresh();
					}
				}
			}
		} else {
			LogUtil.outLogDetail("mApiName.equals(url) == false");
		}
		ProgressHub.getInstance(mActivity).dismiss();
	}

	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
		ProgressHub.getInstance(mActivity).dismiss();
	}

	private void initDatas() {
		// 获得调用参数
		Intent intent = getIntent();
		mStringTitle = intent.getStringExtra("title");
		if (mStringTitle == null) {
			mStringTitle = "";
		}
		mTitlebar.setTitleText(mStringTitle);
		mModel = new UserModule(this);
		onRefresh();

	}

	/**
	 * 注册监听器
	 */
	private void registerListeners() {
		mTitlebar.setButtonClickListener(TitleBarButton.leftImgv, new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mActivity, "1056");
				finish();
			}
		});
		mTitlebar.setImageBackGround(TitleBarButton.rightImgv, getResources().getDrawable(R.drawable.button_collection_delete));
		mTitlebar.setVisibility(TitleBarButton.rightImgv, View.VISIBLE);
		mTitlebar.setButtonClickListener(TitleBarButton.rightImgv, new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
					ToastUtil.showToast(mActivity, getString(R.string.collection_clear_all_nodata));
				} else {
					clearAllCollection();
				}
			}
		});
		mGoodsList.setOnItemClickListener(this);
		mGoodsList.setOnPaginationListener(this);
		mGoodsList.setOnItemLongClickListener(this);
	}

	/**
	 * @discretion: 清除所有的收藏
	 * @author: MaoYaNan
	 * @date: 2014-10-21 上午11:39:09
	 */
	private void clearAllCollection() {
		DialogView.getAlertDialogWithoutTitle(mActivity, getString(R.string.collection_dialog_clear_all_message), getString(R.string.dialog_button_sure), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				isClearAll = true;
				if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
					ToastUtil.showToast(mActivity, getString(R.string.collection_clear_all_nodata));
				} else {
					ProgressHub.getInstance(mActivity).show(getString(R.string.progress_posting));
					// mModel.deleteCollection(mActivity,
					// mAdapter.getData());
					mModel.deleteCollection(mActivity, mAdapter.getData());
				}
			}
		}, getString(R.string.dialog_button_cancle), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
	}

	/**
	 * 从 layout 获得View组件
	 */
//	private void initView() {
//		mTitlebar = (TitleBar) findViewById(R.id.titleBar);
//		mGoodsList = (PaginationListView) findViewById(R.id.collections);
//		mGoodsList.setEmptyRes(R.drawable.icon_shoucang, R.drawable.error_empty_product_collect);
//	}

	@Override
	public void onRefresh() {
		mModel.postMyCollection(mActivity, 0, getResources().getDrawable(R.drawable.default_goods_img).getMinimumWidth()); // 请求网络数据
	}

	@Override
	public void onRequestNextPage(int page) {
		if (mResponse != null && mResponse.getPaged().getMore() == ConfigInfo.PAGE_RESULT_MORE) {
			mModel.postMyCollection(mActivity, mAdapter.page + 1, getResources().getDrawable(R.drawable.default_goods_img).getMinimumWidth());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MobclickAgent.onEvent(mActivity, "1055");
		SaleProduct item = mAdapter.getItem(position - 1);
		String type = item.getProductType();
		if (type.endsWith(Constant.GOODS_TYPE_FREE) || type.equals(Constant.GOODS_TYPE_POST)) {
			type = Constant.GOODS_TYPE_NORMAL;
		}
		//IntentUtil.startGoodsDetail(mActivity, item.getId(), type);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		isClearAll = false;
		mSelectItem = position - 1;
		DialogView.getAlertDialogWithoutTitle(mActivity, getString(R.string.dialog_clear_collection_one_message), getString(R.string.dialog_button_sure), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				isClearAll = false;
				if (position - 1 >= 0) {
					ProgressHub.getInstance(mActivity).show(getResources().getString(R.string.progress_posting));
					mModel.deleteCollection(mActivity, mAdapter.getItem(position - 1));
				}
			}
		}, getString(R.string.dialog_button_cancle), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		return true;
	}

	@Override
	public void onFinish() {
		super.onFinish();
		mGoodsList.onRefreshComplete();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mTitlebar = (TitleBar) findViewById(R.id.titleBar);
		mGoodsList = (PaginationListView) findViewById(R.id.collections);
		mGoodsList.setEmptyRes(R.drawable.icon_shoucang, R.drawable.error_empty_product_collect);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}
}
