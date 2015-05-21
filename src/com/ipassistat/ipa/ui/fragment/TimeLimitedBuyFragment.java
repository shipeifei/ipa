package com.ipassistat.ipa.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.TimeLimitAdapter;
import com.ipassistat.ipa.bean.local.GoodsEntity;
import com.ipassistat.ipa.bean.response.TimeLimitGoodsBuyListResponse;
import com.ipassistat.ipa.bean.response.entity.TimedScareBuying;
import com.ipassistat.ipa.business.GoodsModule;
import com.ipassistat.ipa.business.HmlShoppingCartController;
import com.ipassistat.ipa.business.ShopingCartObserver;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;

import com.ipassistat.ipa.util.DateUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.PaginationListView.PaginationListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * @Discription： 限时抢购页面加载，刷新数据利用封装好的 PaginationListView
 * @package： com.ichsy.mboss.fragment.TimeLimitedBuyFragment
 * @author： MaoYaNan
 * @date：2014-9-26 下午1:23:52
 */
public class TimeLimitedBuyFragment extends BaseFragment implements OnItemClickListener, PaginationListener, ShopingCartObserver<GoodsEntity> {
	private TitleBar mTitlebar;
	private PaginationListView mTimelimitList;
	private List<TimedScareBuying> mDatas;
	private Activity mActivity;
	private TimeLimitAdapter mAdapter;
	private GoodsModule mModel;
	private TimeLimitGoodsBuyListResponse mResponse;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_time_lim_buy, container, false);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1037"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1037"); //
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		HmlShoppingCartController.instance(mActivity.getApplicationContext()).unRegisterListener(this);// 反注册
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// 处理页面保存数据
		mActivity = getActivity();
		// 获得调用参数

		initViews(view);
		registerListeners();
		initDatas();
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (ConfigInfo.API_TIMESCAREDBUY.endsWith(url)) {
			mResponse = (TimeLimitGoodsBuyListResponse) object;
			mDatas = mResponse.getProducts(); // get
			int page = (Integer) mResponse.getTag(); // 获得页数的标记
			if (mDatas != null && mDatas.size() > 0) {
				mDatas = DateUtil.setEndTime(TimedScareBuying.class, "endTime", "systemTime", mDatas);
			}
			if (mAdapter == null || page == 0) {
				mAdapter = new TimeLimitAdapter(mActivity, mDatas);
				mTimelimitList.setAdapter(mAdapter); // 填充数据
			} else {
				mAdapter.loadNextPage(mDatas);
			}
			mTimelimitList.delegatePageCheck(mResponse.getPaged());
		} else {
			LogUtil.outLogDetail("url don't equals with requestUrl");
		}
	}

	/**
	 * 初始化界面基本数据
	 */
	private void initDatas() {

		// 初始化基本数据
		mTitlebar.setTitleText(getString(R.string.time_limited_buy));
		mModel = new GoodsModule(this);
		onRefresh(); // 第一次请求网络数据
	}

	public void registerListeners() {

		mTitlebar.setVisibility(TitleBarButton.leftImgv, View.GONE);

		mTitlebar.setButtonClickListener(TitleBarButton.rightImgv, new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*MobclickAgent.onEvent(mActivity, "1172");
				Intent intentshoppingcart = new Intent(mActivity, ShoppingCartActivity.class);
				startActivity(intentshoppingcart);*/
			}
		});

		mTimelimitList.setEmptyViewLisenter(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTimelimitList.showLoadingView(true);
				onRefresh();
			}
		});

		mTimelimitList.setOnItemClickListener(this);
		// 监听
		mTimelimitList.setOnPaginationListener(this);
		HmlShoppingCartController.instance(mActivity.getApplicationContext()).registerListener(this);// 注册购物车监听
	}

	/**
	 * 初始化组件
	 * 
	 * @param view
	 */
	private void initViews(View view) {
		mTitlebar = (TitleBar) view.findViewById(R.id.titleBar);
		Drawable shoppingcartdrawable = mActivity.getResources().getDrawable(R.drawable.button_goodsdetail_shoppingcart); // 购物车按钮图片
		mTitlebar.setImageBackGround(TitleBarButton.rightImgv, shoppingcartdrawable);// 设置购物车按钮图片
		mTitlebar.setVisibility(TitleBarButton.shoppingcart_num_Textview, View.VISIBLE);// 设置购物车商品数量文本显示
		mTimelimitList = (PaginationListView) view.findViewById(R.id.timelimit);
		mTimelimitList.setDividerHeight(0);
		mTimelimitList.setEmptyRes(R.drawable.error_image, R.drawable.error_empty_timelimit);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//MobclickAgent.onEvent(mActivity, "1108");
		//IntentUtil.startGoodsDetail(mActivity, mAdapter.getItem(position - 1).getSku_code(), Constant.GOODS_TYPE_LIMIT);
	}

	@Override
	public void onRefresh() {
		mModel.postTimeScaredBuyGoods(mActivity, 0, getResources().getDrawable(R.drawable.default_goodsdetail_img).getMinimumWidth());
	}

	@Override
	public void onRequestNextPage(int page) {
		if (mResponse != null && mResponse.getPaged().getMore() == ConfigInfo.PAGE_RESULT_MORE) {
			mModel.postTimeScaredBuyGoods(mActivity, page + 1, getResources().getDrawable(R.drawable.default_goodsdetail_img).getMinimumWidth());
		}
	}

	@Override
	public void onNoNet() {
		super.onNoNet();
		mTimelimitList.noNet(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initDatas();
			}
		});
	}

	@Override
	public void onFinish() {
		super.onFinish();
		mTimelimitList.onRefreshComplete();// 刷新结束
	}

	/**
	 * 购物车回调
	 */
	@Override
	public void onShopCartCountChanged(List<GoodsEntity> productArray, int totalCount, int changeCount) {
		if (totalCount > 99) {
			Drawable drawable = mActivity.getResources().getDrawable(R.drawable.bg_shoppingcart_longnum);
			mTitlebar.setImageBackGround(TitleBarButton.shoppingcart_num_Textview, drawable);
			mTitlebar.setshoppingcartNumTextViewText("99+");// 购物车数量大于99显示99+

		} else if (totalCount > 9) {
			Drawable drawable = mActivity.getResources().getDrawable(R.drawable.bg_shoppingcart_longnum);
			mTitlebar.setImageBackGround(TitleBarButton.shoppingcart_num_Textview, drawable);
			mTitlebar.setshoppingcartNumTextViewText(totalCount + "");// 没有大于99显示当前数量
		} else {
			Drawable drawable = mActivity.getResources().getDrawable(R.drawable.bg_shoppingcart_num);
			mTitlebar.setImageBackGround(TitleBarButton.shoppingcart_num_Textview, drawable);
			mTitlebar.setshoppingcartNumTextViewText(totalCount + "");// 没有大于99显示当前数量
		}
	}

	@Override
	public void onWarning(String message) {

	}

	@Override
	public void onError(int code, String message) {

	}
}
