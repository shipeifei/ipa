/*
 * ImageShowViewPager.java [V 1.0.0]
 * classes : com.ichsy.mboss.view.ImageShowViewPager
 * 时培飞 Create at 2014-11-24 上午9:56:31
 */
package com.ipassistat.ipa.view.imagescroller;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * com.ichsy.mboss.view.ImageShowViewPager
 * @author 时培飞 
 * 姐妹圈帖子详情--图片轮播自定义viewpaper
 * Create at 2014-11-24 上午9:56:31
 */
public class ImageShowViewPager extends ViewPager {
	PointF last;
	public TouchImageView mCurrentView;

	public ImageShowViewPager(Context context) {
		super(context);
	}

	public ImageShowViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private float[] handleMotionEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			last = new PointF(event.getX(0), event.getY(0));
			break;
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_UP:
			PointF curr = new PointF(event.getX(0), event.getY(0));
			return new float[] { curr.x - last.x, curr.y - last.y };

		}
		return null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
			super.onTouchEvent(event);
		}

		float[] difference = handleMotionEvent(event);

		if (mCurrentView.pagerCanScroll()) {
			return super.onTouchEvent(event);
		} else {
			if (difference != null && mCurrentView.onRightSide
					&& difference[0] < 0) // move
											// right
			{
				return super.onTouchEvent(event);
			}
			if (difference != null && mCurrentView.onLeftSide
					&& difference[0] > 0) // move
											// left
			{
				return super.onTouchEvent(event);
			}
			if (difference == null
					&& (mCurrentView.onLeftSide || mCurrentView.onRightSide)) {
				return super.onTouchEvent(event);
			}
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
			super.onInterceptTouchEvent(event);
		}

		float[] difference = handleMotionEvent(event);

		if (mCurrentView.pagerCanScroll()) {
			return super.onInterceptTouchEvent(event);
		} else {
			if (difference != null && mCurrentView.onRightSide
					&& difference[0] < 0) // move
											// right
			{
				return super.onInterceptTouchEvent(event);
			}
			if (difference != null && mCurrentView.onLeftSide
					&& difference[0] > 0) // move
											// left
			{
				return super.onInterceptTouchEvent(event);
			}
			if (difference == null
					&& (mCurrentView.onLeftSide || mCurrentView.onRightSide)) {
				return super.onInterceptTouchEvent(event);
			}
		}
		return false;
	}
}
