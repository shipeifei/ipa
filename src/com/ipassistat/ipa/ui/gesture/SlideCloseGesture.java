package com.ipassistat.ipa.ui.gesture;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/***
 * 滑动关闭的手势
 * @author shipeifei
 *
 */
public class SlideCloseGesture extends SimpleOnGestureListener {
	private int verticalMinDistance = 80;// 滑动关闭的距离
	private int minVelocity = 0;

	private CustomGestureListener mListener;

	public interface CustomGestureListener {
		/***
		 * 手势滑动关闭监听
		 * create at 2015-6-1
		 * author 时培飞
		 */
		void onSlideClose();
	}

	public SlideCloseGesture(CustomGestureListener listener) {
		mListener = listener;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// Log.i(getClass().getName(), "onSingleTapUp-----" +
		// getActionName(e.getAction()));
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// Log.i(getClass().getName(), "onLongPress-----" +
		// getActionName(e.getAction()));
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		

		if (e1.getX() <= 50 && e2.getX() - e1.getX() > verticalMinDistance && Math.abs(e2.getY() - e1.getY()) < 150 && Math.abs(velocityX) > minVelocity) {
			// finish();
			mListener.onSlideClose();
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onDown(MotionEvent e) {
		
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		
		return false;
	}
}
