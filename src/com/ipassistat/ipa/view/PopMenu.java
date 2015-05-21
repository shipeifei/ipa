package com.ipassistat.ipa.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ipassistat.ipa.R.layout;

/**
 * 从底部弹出的菜单
 * 
 * @author LiuYuHang
 * @date 2014年9月24日
 *
 */
public class PopMenu implements OnClickListener {

	public interface OnItemClickListener {
		public void onItemClick(View v, int position);
	}

	private Context mContext;
	private List<PopMenuItem> mMenuList;
	private LinearLayout mRootView;
	private PopAction mAction;

	public PopMenu(Context context) {
		mContext = context;
		mMenuList = new ArrayList<PopMenuItem>();

		mRootView = (LinearLayout) View.inflate(context, layout.activity_menu_photopick, null);
		mRootView.setOrientation(LinearLayout.VERTICAL);

		mAction = new PopAction(context, mRootView);
	}

	/**
	 * 绘制UI
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月24日
	 *
	 */
	private void requestLayout() {
		for (int i = 0; i < mMenuList.size(); i++) {
			final int position = i;
			
			PopMenuItem item = mMenuList.get(i);
			Button button = new Button(mContext);
			button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			button.setText(item.getTitle());

			Integer color = item.getColor();
			if (color != null) button.setTextColor(color);

			final OnItemClickListener listener = item.getListener();
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onItemClick(v, position);
					dismiss();
				}
			});

			mRootView.addView(button);
		}
	}

	public void addItem(PopMenuItem menu) {
		mMenuList.add(menu);
	}

	public void show(View v) {
		requestLayout();
		mAction.popFromBottom(v);
	}

	public void dismiss() {
		mAction.dismiss();
	}

	@Override
	public void onClick(View arg0) {
		mAction.dismiss();
	}

}
