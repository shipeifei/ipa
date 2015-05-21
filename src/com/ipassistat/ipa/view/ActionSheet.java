package com.ipassistat.ipa.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * UIActionSheet 仿 ios ActionSheet 效果，并在其基础上进行了扩展
 * 
 * @author maoyn
 * 
 */
public class ActionSheet extends Fragment implements OnClickListener {

	private static final String ARG_CANCEL_BUTTON_TITLE = "cancel_button_title";
	private static final String ARG_OTHER_BUTTON_TITLES = "other_button_titles";
	private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
	private static final int BG_VIEW_ID = 10;
	private static final int TRANSLATE_DURATION = 200;
	private static final int ALPHA_DURATION = 300;

	private boolean mDismissed = true;
	private ActionSheetListener mListener;
	private View mView;
	private LinearLayout mPanel;
	private int mPanelLayoutId;
	private ViewGroup mGroup;
	private View mBg;
	private boolean isCancel = true;

	/**
	 * 显示fragment
	 * 
	 * @param manager
	 * @param tag
	 */
	public void show(FragmentManager manager, String tag) {
		if (!mDismissed) {
			return;
		}
		mDismissed = false;
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(this, tag);
		ft.addToBackStack(null);
		ft.commit();
	}

	/**
	 * ActionSheet消失
	 */
	public void dismiss() {
		if (mDismissed) {
			return;
		}
		mDismissed = true;
		getFragmentManager().popBackStack();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(this);
		ft.commit();
	}

	/***
	 * 创建fragment时调用
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			View focusView = getActivity().getCurrentFocus();
			if (focusView != null) {
				imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
			}
		}

		mView = createView();
		mGroup = (ViewGroup) getActivity().getWindow().getDecorView(); // 必须得有
		mGroup.addView(mView);
		mBg.startAnimation(createAlphaInAnimation());
		mPanel.startAnimation(createTranslationInAnimation());
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * @return fragment 要显示的界面
	 */
	private View createView() {
		FrameLayout parent = new FrameLayout(getActivity());
		parent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mBg = new View(getActivity());
		mBg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
		mBg.setId(BG_VIEW_ID);
		mBg.setOnClickListener(this);

		createPannel(mPanelLayoutId, parent);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM;
		mPanel.setLayoutParams(params);
		// mPanel.setOrientation(LinearLayout.VERTICAL);

		parent.addView(mBg);
		parent.addView(mPanel);
		return parent;
	}

	/**
	 * 生成Panel布局
	 * 
	 * @param id
	 * @param parent
	 */
	private void createPannel(int id, ViewGroup parent) {
		mPanel = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
				getActivity().getResources().getLayout(id), parent, false);
		mListener.onSetPanelListener(mPanel); // 给没Panel设置监听
	}

	/***
	 * fragment消失时显示的界面
	 */
	@Override
	public void onDestroyView() {
		mPanel.startAnimation(createTranslationOutAnimation());
		mBg.startAnimation(createAlphaOutAnimation());
		mView.postDelayed(new Runnable() {
			@Override
			public void run() {
				mGroup.removeView(mView);
			}
		}, ALPHA_DURATION);
		if (mListener != null) {
			mListener.onDismiss(this, isCancel);
		}
		super.onDestroyView();
	}

	/**
	 * 点击透明背景时是否是可消失的
	 * 
	 * @return
	 */
	private boolean getCancelableOnTouchOutside() {
		return getArguments().getBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE);
	}

	/**
	 * @param listener
	 */
	public void setActionSheetListener(ActionSheetListener listener) {
		mListener = listener;
	}

	/**
	 * 设置 panel布局
	 * 
	 * @param mPanelLayoutId2
	 */
	public void setPanelLayoutId(int mPanelLayoutId2) {
		mPanelLayoutId = mPanelLayoutId2;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == BG_VIEW_ID && !getCancelableOnTouchOutside()) {
			return;
		}
		dismiss();
	}

	/**
	 * ��̬��
	 * 
	 * @param context
	 * @param fragmentManager
	 * @return
	 */
	public static Builder createBuilder(Context context,
			FragmentManager fragmentManager) {
		return new Builder(context, fragmentManager);
	}

	/**
	 * 
	 * @author maoyn
	 * 
	 */
	public static class Builder {

		private Context mContext;
		private FragmentManager mFragmentManager;
		private String mCancelButtonTitle;
		private String[] mOtherButtonTitles;
		private String mTag = "actionSheet";
		private boolean mCancelableOnTouchOutside;
		private ActionSheetListener mListener;
		private int mPanelLayoutId;

		public Builder(Context context, FragmentManager fragmentManager) {
			mContext = context;
			mFragmentManager = fragmentManager;
		}

		/**
		 * 设置标签
		 * 
		 * @param tag
		 * @return
		 */
		public Builder setTag(String tag) {
			mTag = tag;
			return this;
		}

		/**
		 * 设置监听
		 * 
		 * @param listener
		 * @return
		 */
		public Builder setListener(ActionSheetListener listener) {
			this.mListener = listener;
			return this;
		}

		/**
		 * 设置panel的布局
		 * 
		 * @param listener
		 * @return
		 */
		public Builder setPanelLayout(int id) {
			this.mPanelLayoutId = id;
			return this;
		}

		/**
		 * 设置弹框的消失
		 * 
		 * @param cancelable
		 * @return
		 */
		public Builder setCancelableOnTouchOutside(boolean cancelable) {
			mCancelableOnTouchOutside = cancelable;
			return this;
		}

		/**
		 * 传递参数
		 * 
		 * @return
		 */
		public Bundle prepareArguments() {
			Bundle bundle = new Bundle();
			bundle.putString(ARG_CANCEL_BUTTON_TITLE, mCancelButtonTitle);
			bundle.putStringArray(ARG_OTHER_BUTTON_TITLES, mOtherButtonTitles);
			bundle.putBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE,
					mCancelableOnTouchOutside);
			return bundle;
		}

		/**
		 * 显示
		 * 
		 * @return
		 */
		public ActionSheet show() {
			ActionSheet actionSheet = (ActionSheet) Fragment.instantiate(
					mContext, ActionSheet.class.getName(), prepareArguments());
			actionSheet.setActionSheetListener(mListener);
			actionSheet.setPanelLayoutId(mPanelLayoutId);
			actionSheet.show(mFragmentManager, mTag);
			return actionSheet;
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	/**
	 * 进入和退出时的动画
	 */
	public static interface ActionSheetListener {

		void onDismiss(ActionSheet actionSheet, boolean isCancel);

		void onSetPanelListener(LinearLayout panel);
	}

	/**
	 * 
	 * @return
	 */
	private Animation createTranslationInAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				1, type, 0);
		an.setDuration(TRANSLATE_DURATION);
		return an;
	}

	/**
	 * 
	 * @return
	 */
	private Animation createAlphaInAnimation() {
		AlphaAnimation an = new AlphaAnimation(0, 1);
		an.setDuration(ALPHA_DURATION);
		return an;
	}

	/**
	 * 
	 * @return
	 */
	private Animation createTranslationOutAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				0, type, 1);
		an.setDuration(TRANSLATE_DURATION);
		an.setFillAfter(true);
		return an;
	}

	/**
	 * 
	 * @return
	 */
	private Animation createAlphaOutAnimation() {
		AlphaAnimation an = new AlphaAnimation(1, 0);
		an.setDuration(ALPHA_DURATION);
		an.setFillAfter(true);
		return an;
	}

}