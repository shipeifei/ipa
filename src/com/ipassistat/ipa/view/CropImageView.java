package com.ipassistat.ipa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 会截取图片的一部分
 * 
 * @author LiuYuHang
 * @date 2014年10月23日
 */
public class CropImageView extends ImageView {

	public CropImageView(Context context) {
		super(context);
	}

	public CropImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CropImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

//		Bitmap bitmap = ViewUtil.getCropImage(getContext(), getDrawable(), getWidth(), getHeight());
//		canvas.drawBitmap(bitmap, 0, 0, null);
	}

}
