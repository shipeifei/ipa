package com.ipassistat.ipa.ui.activity;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.adapter.OrderListAdapter;
import com.ipassistat.ipa.bean.response.OrderListResponse;
import com.ipassistat.ipa.bean.response.entity.ApiSellerOrderListResult;
import com.ipassistat.ipa.business.OrderModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.TitleBar;
import com.ipassistat.ipa.view.PaginationListView.PaginationListener;
import com.ipassistat.ipa.view.TitleBar.TitleBarButton;
import com.umeng.analytics.MobclickAgent;

/**
 * 订单列表
 * 
 * @author renheng
 * 
 */

public class OrderListActivity extends BaseActivity implements OnClickListener, PaginationListener {

	private OrderListAdapter mAdapter;

	private TitleBar mTitlebar;
	private PaginationListView mListView; // 订单列表的listview
	private Context mContext;
	private OrderModule mModule;
	private View mNoNet;

	private int mPage = 1; // 页数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		mContext = OrderListActivity.this;
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1034"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
		refreshOfficialList();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1034"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}
	
	private void init() {
		initTitleBar();
		mListView = (PaginationListView) findViewById(R.id.listview);
		mListView.setOnPaginationListener(this);
		mNoNet=findViewById(R.id.no_net);
		mListView.setDividerHeight(15);
		// 设置页面为空的布局
		mListView.setEmptyRes(drawable.error_image, drawable.no_order_imgv);
	}

	private void initTitleBar() {
		String name = "我的订单";
		mTitlebar = (TitleBar) findViewById(R.id.titlebar);
		mTitlebar.setTitleText(name);
		mTitlebar.setButtonClickListener(TitleBarButton.leftImgv, this);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		
		if (url.equals(ConfigInfo.API_ORDER_LIST)) {
			mNoNet.setVisibility(View.GONE);
			OrderListResponse resp = (OrderListResponse) object;
			if (resp != null) {
				List<ApiSellerOrderListResult> ordersList = resp.getSellerOrderList();
				View view = findViewById(R.id.no_order); // 无订单页面

				if (mAdapter == null) {
					mAdapter = new OrderListAdapter(OrderListActivity.this, ordersList);
					mListView.setAdapter(mAdapter);
				} else if (ordersList != null && ordersList.size() != 0) {
					mAdapter.loadNextPage(ordersList);
					this.mPage++;
				}
				
				if(resp.getNowPage() >= resp.getCountPage()) {
					mListView.noMorePage();
				} else {
					mListView.mayHaveNextPage();
				}
				
			} else {
				return;
			}
		}
		if (url.equals(ConfigInfo.API_GOODS_RECEIVE_CONFIRM)) {
			ToastUtil.showToast(this, "确认收货成功");
			refreshOfficialList();
		}
		if (url.equals(ConfigInfo.API_ORDER_REMOVE)) {
			ToastUtil.showToast(mContext, "删除订单成功");
			refreshOfficialList();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv:
			MobclickAgent.onEvent(mContext, "1101");
			finish();
			break;

		default:
			break;
		}
	}

	/*
	 * 获取订单列表
	 */
	private void getOrderList(String nextPage, String buyer_code, String order_status) {
		mModule = new OrderModule(this);
		mModule.postOrderList(mContext, nextPage, buyer_code);
	}


	/**
	 * 刷新列表
	 */
	public void refreshOfficialList() {
		mAdapter = null;
		mPage = 1;
		getOrderList(String.valueOf(mPage), "1", "");
	}

	@Override
	public void onRefresh() {
		refreshOfficialList();
	}

	@Override
	public void onRequestNextPage(int page) {
		getOrderList(String.valueOf(this.mPage + 1), "1", "");
	}

	@Override
	public void onFinish() {
		super.onFinish();
		mListView.onRefreshComplete();
	}
	
	@Override
	public void onNoNet() {
		super.onNoNet();
		mNoNet.setVisibility(View.VISIBLE);
		mNoNet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refreshOfficialList();
			}
		});
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
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
