package com.ipassistat.ipa.util.share.ichsy;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.util.share.ichsy.adapter.ShareAdapter;

/**
 * 分享View的工厂类
 * 
 * @author LiuYuHang
 * @date 2014年9月22日
 *
 */
public class ShareViewFactory {

	/**
	 * 分享组件的默认View
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 *
	 * @param context
	 * @param controller
	 * @return
	 */
	public static View getDefaultView(final ShareHandler handler, final ShareController controller) {
		View root = View.inflate(handler.mContext, layout.activity_share_view, null);

		if (controller != null) {
			GridView shareView = (GridView) root.findViewById(id.shareview);

			ShareAdapter adapter = new ShareAdapter(handler.mContext, controller.mPlatforms);
			shareView.setAdapter(adapter);
			shareView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					if (handler.mShareWindow != null)
						handler.mShareWindow.dismiss();
					if (controller.shareListener == null)
						throw new NullPointerException("调用" + controller.getClass().getSimpleName() + ".setShareListener()添加监听");
					controller.shareListener.onShare(controller.mPlatforms.get(position));
				}
			});
		}
		return root;
	}

}
