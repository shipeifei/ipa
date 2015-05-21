package com.ipassistat.ipa.view.pulldown;

import android.view.View;
import android.view.animation.Animation;

/**
 * @author wangxiao1@cjsc.com.cn
 * @date
 */
public interface IPullDownElastic {
	/**松开加载*/
	public final static int RELEASE_To_REFRESH = 0;
	/**下拉刷新*/
	public final static int PULL_To_REFRESH = 1;
	/***/
	public final static int REFRESHING = 2;
	/**完成刷新*/
	public final static int DONE = 3;

	public View getElasticLayout();

	public int getElasticHeight();

	public void showArrow(int visibility);

	public void startAnimation(Animation animation);

	public void clearAnimation();

	public void showProgressBar(int visibility);

	public void setTips(String tips);

	public void showLastUpdate(int visibility);

	public void setLastUpdateText(String text);

	/**
	 * 
	 * @param state
	 * @see RELEASE_To_REFRESH
	 * @param isBack
	 */
	public void changeElasticState(int state, boolean isBack);

}
