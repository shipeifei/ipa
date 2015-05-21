package com.ipassistat.ipa.business;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.ipassistat.ipa.util.ImageUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

/**
 * 图片裁剪的工具类，可以把过大的图片做裁剪和圆角处理
 * 
 * @author LiuYuHang
 * @date 2014年11月9日
 */
public class CropImageListener extends DefaultBitmapLoadCallBack<ImageView> {
	public class ImageOption {
		public boolean crop;
		public boolean circel;
	}

	private boolean isCircel = false;
	private Context context;

	public CropImageListener() {}

	public CropImageListener(Context context, boolean circel) {
		this.context = context;
		isCircel = circel;
	}

	@Override
	public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
		if (isCircel) {
			Bitmap finalBitmap = ImageUtil.getRoundedCornerBitmap(ViewUtil.getCropImage(arg2, arg0.getMeasuredWidth(), arg0.getMeasuredHeight()), ViewUtil.dip2px(context, 2));
			arg0.setImageBitmap(finalBitmap);
		} else {
			Bitmap finalBitmap = ViewUtil.getCropImage(arg2, arg0.getMeasuredWidth(), arg0.getMeasuredHeight());
			arg0.setImageBitmap(finalBitmap);
		}
	}

	@Override
	public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
		arg0.setImageDrawable(arg2);
	}

}