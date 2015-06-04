package com.ipassistat.ipa.ui.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.TrialOfMineAdapter;
import com.ipassistat.ipa.bean.response.MyTryOutResponse;
import com.ipassistat.ipa.bean.response.entity.TryOutGood;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.util.DateUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.PaginationListView.PaginationListener;
import com.umeng.analytics.MobclickAgent;

public class TrialContentFragment extends BaseFragment implements OnItemClickListener, PaginationListener {
	// private static final String TAG = "TrialContentFragment";
	protected FragmentManager mFrmg;
	private PaginationListView mGoodsList; // 列表数据
	private TrialOfMineAdapter mAdapter; // 适配器
	private String mTag;
	private Context mContext;
	private UserModule mModule;
	private MyTryOutResponse mResponse;

	public void onResume() {
		super.onResume();
		if (Constant.TRIAL_FREE.equals(mTag)) {
			MobclickAgent.onPageStart("1018"); // 统计页面
		} else if (Constant.TRIAL_POSTAGE.equals(mTag)) {
			MobclickAgent.onPageStart("1017"); // 统计页面
		}
	}

	public void onPause() {
		super.onPause();
		if (Constant.TRIAL_FREE.equals(mTag)) {
			MobclickAgent.onPageEnd("1018"); //
		} else if (Constant.TRIAL_POSTAGE.equals(mTag)) {
			MobclickAgent.onPageEnd("1017"); //
		}
		MobclickAgent.onPageEnd("1007"); //
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragemnt_trial_content, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mContext = getActivity();
		mGoodsList = (PaginationListView) view.findViewById(R.id.goods_lists);
		mGoodsList.setEmptyRes(R.drawable.error_image, R.drawable.error_empty_freeuse);
		initDatas();
		initListeners();
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);
		if (getActivity() == null || getActivity().isFinishing()) {
			return;
		}
		if (ConfigInfo.API_MYTRYOUTCENTERAPI.equals(url)) {
			mResponse = (MyTryOutResponse) object;
			if (mResponse.getResultCode() == ConfigInfo.RESULT_OK) {
				int page = (Integer) mResponse.getTag();
				List<TryOutGood> tryOutGoods = mResponse.getTryOutGoods();
				if (tryOutGoods != null) {
					tryOutGoods = DateUtil.setEndTime(TryOutGood.class, "time", "system_time", tryOutGoods);
				}
				if (mAdapter == null || page == 0) {
					mAdapter = new TrialOfMineAdapter(mContext, tryOutGoods, mTag);
					mGoodsList.setAdapter(mAdapter);
				} else {
					mAdapter.loadNextPage(mResponse.getTryOutGoods());
				}
				mGoodsList.delegatePageCheck(mResponse.getPaged());
			} else {
				LogUtil.outLogDetail("resquestCode != 1");
			}
		} else {
			LogUtil.outLogDetail("URL 未找到");
		}
	}

	private void initListeners() {
		mGoodsList.setOnItemClickListener(this);
		// 监听
		mGoodsList.setOnPaginationListener(this);
	}

	private void initDatas() {
		mModule = new UserModule(this);
		onRefresh();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MobclickAgent.onEvent(mContext, "1045");
		TryOutGood item = mAdapter.getItem(position - 1);
		LogUtil.outLogDetail("-------" + item.getFieldServerEnd() + "--------");
		String endTime = item.getFieldServerEnd();
		if (TextUtils.isEmpty(endTime)) {
			endTime = item.getTime();
		}
		IntentUtil.jumpToTrialInfoActivity(mContext, mTag, item.getId(), endTime);
	}

	@Override
	public void onRefresh() {
		mModule.postMyTry(mContext, mTag, 0, getResources().getDrawable(R.drawable.default_goods_img).getMinimumWidth());
	}

	@Override
	public void onRequestNextPage(int page) {
		if (mResponse != null && mResponse.getPaged().getMore() == ConfigInfo.PAGE_RESULT_MORE) {
			mModule.postMyTry(mContext, mTag, mAdapter.page + 1, getResources().getDrawable(R.drawable.default_goods_img).getMinimumWidth());
		}

	}

	@Override
	public void onFinish() {
		super.onFinish();
		mGoodsList.onRefreshComplete();
	}
}
