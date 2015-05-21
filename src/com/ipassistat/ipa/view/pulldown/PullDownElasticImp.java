package com.ipassistat.ipa.view.pulldown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ipassistat.ipa.R;

/**
 * @author wangxiao1@cjsc.com.cn
 * @date 2013-7-10 Ĭ�������ؼ�����ʵ��
 */
public class PullDownElasticImp implements IPullDownElastic {
	private View refreshView;
	private ImageView arrowImageView;
	/**刷新头部的高度*/
	private int headContentHeight;
	private ProgressBar progressBar;
	private TextView tipsTextview;
	/**下拉显示时间*/
	private TextView lastUpdatedTextView;

	private Context mContext;

	public PullDownElasticImp(Context context) {
		mContext = context;
		init();
	}

	private void init() {
		// 刷新视图顶端的的view
		refreshView = LayoutInflater.from(mContext).inflate(
				R.layout.refresh_top_item, null);

		// 指示器view
		arrowImageView = (ImageView) refreshView
				.findViewById(R.id.head_arrowImageView);
		// 刷新bar
		progressBar = (ProgressBar) refreshView
				.findViewById(R.id.head_progressBar);
		// 下拉显示text
		tipsTextview = (TextView) refreshView.findViewById(R.id.refresh_hint);
		// 下来显示时间
		lastUpdatedTextView = (TextView) refreshView
				.findViewById(R.id.refresh_time);

		headContentHeight = dip2px(mContext, 50);
	}

	/**
	 * dp转换为px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * @return
	 * 
	 */
	@Override
	public View getElasticLayout() {
		return refreshView;
	}

	/**
	 * @return
	 * 
	 */
	@Override
	public int getElasticHeight() {
		return headContentHeight;
	}

	/**
	 * @param show
	 * 
	 */
	@Override
	public void showArrow(int visibility) {
		arrowImageView.setVisibility(visibility);
	}

	/**
	 * @param animation
	 * 
	 */
	@Override
	public void startAnimation(Animation animation) {
		arrowImageView.startAnimation(animation);
	}

	/**
     * 
     * 
     */
	@Override
	public void clearAnimation() {
		arrowImageView.clearAnimation();
	}

	/**
	 * @param show
	 * 
	 */
	@Override
	public void showProgressBar(int visibility) {
		progressBar.setVisibility(visibility);
	}

	/**
	 * @param tips
	 * 
	 */
	@Override
	public void setTips(String tips) {
		tipsTextview.setText(tips);
	}

	/**
	 * @param show
	 * 
	 */
	@Override
	public void showLastUpdate(int visibility) {
		lastUpdatedTextView.setVisibility(visibility);
	}

	/**
	 * @param text
	 * 
	 */
	public void setLastUpdateText(String text) {
		lastUpdatedTextView.setText(text);
	}

	/**
	 * @param state
	 * @param isBack
	 * 
	 */
	@Override
	public void changeElasticState(int state, boolean isBack) {
		

	}

}
