package com.ipassistat.ipa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 可以根据手指移动，可以放大缩小的ImageView
 * 
 * @author maoyn
 * */

public class GestureHandleImgeView extends ImageView {
	// Bitmap mComposedBitmap;
	// Bitmap mOriginalBitmap;
	// Canvas mComposedCanvas;
	// Canvas mOriginalCanvas;
	Paint mPaint;
	int mHeight;
	int mWidth;
	float mR; // 圆的半径
	float mRx; // 圆心的横坐标
	float mRy; // 圆心的纵坐标

	// 手势
	private float mBeforeLenght;
	private float mAfterLenght;
	// 单点移动的前后坐标值
	private float mAfterX, mAfterY;
	private float mBeforeX, mBeforeY;
	private float mScale = 0.01f;
	private Context mContext;

	public GestureHandleImgeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	// 用来设置ImageView的位置
	private void setLocation(int x, int y) {
		this.setFrame(//
				this.getLeft() + x, //
				this.getTop() + y, //
				this.getRight() + x, //
				this.getBottom() + y);//
	}

	// 用来放大缩小ImageView 因为图片是填充ImageView的，所以也就有放大缩小图片的效果
	private void setScale(float temp, int flag) {
		if (flag == 0) {// 放大图片
			this.setFrame(//
					this.getLeft() - (int) (temp * this.getWidth()),//
					this.getTop() - (int) (temp * this.getHeight()), //
					this.getRight() + (int) (temp * this.getWidth()), //
					this.getBottom() + (int) (temp * this.getHeight()));//
		} else {// 缩小图片
			this.setFrame(//
					this.getLeft() + (int) (temp * this.getWidth()),//
					this.getTop() + (int) (temp * this.getHeight()),//
					this.getRight() - (int) (temp * this.getWidth()), //
					this.getBottom() - (int) (temp * this.getHeight()));//
		}
	}

	/*
	 * 让图片跟随手指触屏的位置移动 beforeX、Y是用来保存前一位置的坐标 afterX、Y是用来保存当前位置的坐标
	 * 它们的差值就是ImageView各坐标的增加或减少值
	 */
	public void moveWithFinger(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mBeforeX = event.getX();
			mBeforeY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			mAfterX = event.getX();
			mAfterY = event.getY();
			this.setLocation((int) (mAfterX - mBeforeX),
					(int) (mAfterY - mBeforeY));
			mBeforeX = mAfterX;
			mBeforeY = mAfterY;
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
	}

	// 通过多点触屏放大或缩小图像 beforeLenght用来保存前一时间两点之间的距离 afterLenght用来保存当前时间两点之间的距离
	public void scaleWithFinger(MotionEvent event) {
		float X = event.getX(1) - event.getX(0);
		float Y = event.getY(1) - event.getY(0);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mBeforeLenght = (float) Math.sqrt((X * X) + (Y * Y));
			break;
		case MotionEvent.ACTION_MOVE:
			mAfterLenght = (float) Math.sqrt((X * X) + (Y * Y));// 得到两个点之间的长度
			float gapLenght = mAfterLenght - mBeforeLenght;
			if (gapLenght == 0) {
				break;
			}
			// 如果当前时间两点距离大于前一时间两点距离，则传0，否则传1
			if (gapLenght > 0) {
				this.setScale(mScale, 0);
			} else {
				this.setScale(mScale, 1);
			}
			mBeforeLenght = mAfterLenght;
			break;
		}
	}

}