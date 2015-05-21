package com.ipassistat.ipa.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.ipassistat.ipa.R;

/**
 * 一个半透明显示中间圆形的图片
 * 
 * @author maoyn
 * */

public class CircleClipImageView extends View {
	Bitmap mComposedBitmap;
	Bitmap mOriginalBitmap;
	Canvas mComposedCanvas;
	Canvas mOriginalCanvas;
	Paint mPaint;
	int mHeight;
	int mWidth;
	float mR; // 圆的半径
	float mDefalutR = 50;
	float mRX; // 圆心的横坐标
	float mRY; // 圆心的纵坐标
	private TypedArray mTa;

	public CircleClipImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTa = context.obtainStyledAttributes(attrs, R.styleable.clipCircle);
		initDimems();
	}

	@Override
	public void draw(Canvas canvas) {
		initDimems();
		if (mOriginalBitmap != null) {
			mOriginalBitmap.recycle();
		}
		if (mComposedBitmap != null) {
			mComposedBitmap.recycle();
		}
		mOriginalBitmap = Bitmap.createBitmap(mWidth, mHeight,
				Bitmap.Config.ARGB_8888);
		mComposedBitmap = Bitmap.createBitmap(mWidth, mHeight,
				Bitmap.Config.ARGB_8888);
		mComposedCanvas = new Canvas(mComposedBitmap);
		mOriginalCanvas = new Canvas(mOriginalBitmap);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);
		// originalCanvas.scale(2, 2);
		// super.draw(originalCanvas);
		mComposedCanvas.drawARGB(115, 0, 0, 0);
		mComposedCanvas.drawCircle(mRX, mRY, mR + 5, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
		mComposedCanvas.drawCircle(mRX, mRY, mR, mPaint);

		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
		mOriginalCanvas.drawBitmap(mComposedBitmap, 0, 0, new Paint()); // 画一个图片
		canvas.drawBitmap(mComposedBitmap, 0, 0, new Paint());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		initDimems();
	}

	/**
	 * 返回圆形的半径
	 * 
	 * @return 圆形的半径
	 */
	public float getR() {
		if (mR == 0) {
			mR = mDefalutR;
		}
		return mR;
	}

	/**
	 * 返回圆形圆心的横坐标
	 * 
	 * @return
	 */
	public float getRx() {
		return mRX;
	}

	/**
	 * 返回圆心的纵坐标
	 * 
	 * @return
	 */
	public float getRy() {
		return mRY;
	}

	private void initDimems() {
		mR = mTa.getDimension(R.styleable.clipCircle_rLength, mDefalutR);
		mWidth = getWidth();
		mHeight = getHeight();
		mRX = mWidth / 2;
		mRY = mHeight / 2;
	}

}