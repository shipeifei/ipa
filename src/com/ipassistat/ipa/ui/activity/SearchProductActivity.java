package com.ipassistat.ipa.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.adapter.SearchProductAdapter;
import com.ipassistat.ipa.bean.response.ProductSearchResponse;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;
import com.ipassistat.ipa.business.SisterGroupModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.InputMethodUtil;
import com.ipassistat.ipa.util.PhotoPickerHandler;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.PhotoPickerView;
import com.umeng.analytics.MobclickAgent;

/**
 * 姐妹圈 - 编辑帖子 - 搜索商品
 * 
 * @author LiuYuHang
 * @date 2014年10月21日
 */
public class SearchProductActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	private SearchProductAdapter mAdapter;
	private PaginationListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_product);

		findListeners(new int[] { R.id.left_imgv, id.button_submit }, this);

		mListView = (PaginationListView) findViewById(R.id.listview_main);
		setTitleText("搜索产品");
		// initView();
		mListView.showLoadingView(false);
		MobclickAgent.onEvent(getApplicationContext(), "1043");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1043"); // 统计页面
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1043"); // 统计页面
	}

	/**
	 * 搜索
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 *
	 */
	private void doSearch(String keyword) {
		mListView.showLoadingView(true);
		new SisterGroupModule(this).productSearch(getApplicationContext(), keyword);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);

		if (url.equals(ConfigInfo.API_PRODUCT_SEARCH)) {

			ProductSearchResponse response = (ProductSearchResponse) object;

			mListView.setEmptyRes(drawable.error_image, drawable.error_empty_product);
			mAdapter = new SearchProductAdapter(getApplicationContext(), response.products);
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv:
			MobclickAgent.onEvent(getApplicationContext(), "1128");
			finish();
			break;
		case id.button_submit:
			MobclickAgent.onEvent(getApplicationContext(), "1127");
			EditText editText = (EditText) findViewById(id.editview_content);
			if (ViewUtil.isEdittextEmpty(editText)) {
				ToastUtil.showToast(getApplicationContext(), "请输入搜索内容");
			} else {
				InputMethodUtil.showForced(getApplicationContext(), getCurrentFocus(), false, null);
				doSearch(editText.getText().toString());
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent data = new Intent();
		data.putExtra(PhotoPickerView.INTENT_KEY_PRODUCT, (SaleProduct) mAdapter.getItem(position - 1));
		setResult(PhotoPickerHandler.RESULT_PRODUCT, data);
		finish();
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
