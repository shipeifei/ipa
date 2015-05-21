/*
 * EclairMotionEvent.java [V 1.0.0]
 * classes : com.ichsy.mboss.view.EclairMotionEvent
 * 时培飞 Create at 2014-11-24 上午10:00:38
 */
package com.ipassistat.ipa.view.imagescroller;

import android.view.MotionEvent;

/**
 * com.ichsy.mboss.view.EclairMotionEvent
 * @author 时培飞 
 * 用于图片缩放
 * Create at 2014-11-24 上午10:00:38
 */
public class EclairMotionEvent extends WrapMotionEvent {

	protected EclairMotionEvent(MotionEvent event) {
		super(event);
	}

	public float getX(int pointerIndex) {
		return event.getX(pointerIndex);
	}

	public float getY(int pointerIndex) {
		return event.getY(pointerIndex);
	}

	public int getPointerCount() {
		return event.getPointerCount();
	}

	public int getPointerId(int pointerIndex) {
		return event.getPointerId(pointerIndex);
	}
}
