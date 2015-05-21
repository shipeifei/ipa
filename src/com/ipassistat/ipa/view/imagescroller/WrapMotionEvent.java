/*
 * WrapMotionEvent.java [V 1.0.0]
 * classes : com.ichsy.mboss.view.WrapMotionEvent
 * 时培飞 Create at 2014-11-24 上午9:59:30
 */
package com.ipassistat.ipa.view.imagescroller;

import android.view.MotionEvent;



/**
 * com.ichsy.mboss.view.WrapMotionEvent
 * @author 时培飞 
 * Create at 2014-11-24 上午9:59:30
 */
public class WrapMotionEvent {
	protected MotionEvent event;

	protected WrapMotionEvent(MotionEvent event) {
		this.event = event;
	}

	static public WrapMotionEvent wrap(MotionEvent event) {
		try {
			return new EclairMotionEvent(event);
		} catch (VerifyError e) {
			return new WrapMotionEvent(event);
		}
	}

	public int getAction() {
		return event.getAction();
	}

	public float getX() {
		return event.getX();
	}

	public float getX(int pointerIndex) {
		verifyPointerIndex(pointerIndex);
		return getX();
	}

	public float getY() {
		return event.getY();
	}

	public float getY(int pointerIndex) {
		verifyPointerIndex(pointerIndex);
		return getY();
	}

	public int getPointerCount() {
		return 1;
	}

	public int getPointerId(int pointerIndex) {
		verifyPointerIndex(pointerIndex);
		return 0;
	}

	private void verifyPointerIndex(int pointerIndex) {
		if (pointerIndex > 0) {
			throw new IllegalArgumentException(
					"Invalid pointer index for Donut/Cupcake");
		}
	}

}