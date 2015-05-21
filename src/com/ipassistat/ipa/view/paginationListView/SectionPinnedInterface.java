package com.ipassistat.ipa.view.paginationListView;

import android.view.View;

/***
 * 
 * com.ipassistat.ipa.view.paginationListView.SectionPinnedInterface
 * @author 时培飞 
 * Create at 2015-4-21 上午9:26:44
 */
public interface SectionPinnedInterface {

	public int getPinnedHeaderState(int position);

	public void configurePinnedHeader(View headView, int position, int alpha);
	
}
