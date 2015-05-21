package com.ipassistat.ipa.ui.gesture;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * 滑动关闭的手势
 * 
 * @author LiuYuHang
 * @date 2014年10月28日
 */
public class SlideCloseGesture extends SimpleOnGestureListener {
	private int verticalMinDistance = 80;// 滑动关闭的距离
	private int minVelocity = 0;

	private CustomGestureListener mListener;

	public interface CustomGestureListener {
		/**
		 * 手势滑动关闭监听
		 *
		 * @author LiuYuHang
		 * @date 2014年10月28日
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
		// Log.i(getClass().getName(),
		// "onScroll-----" + getActionName(e2.getAction()) + ",(" +
		// e1.getX() + "," + e1.getY() + ") ,("
		// + e2.getX() + "," + e2.getY() + ")");
		// LogUtil.outLogDetail("onScroll-----" + e2.getAction() + ",(" +
		// e1.getX() + "," + e1.getY() + ") ,(" + e2.getX() + "," +
		// e2.getY() + ")");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// LogUtil.outLogDetail("onFling-----" + e2.getAction() + ",(" +
		// e1.getX() + "," + e1.getY() + ") ,(" + e2.getX() + "," +
		// e2.getY() + ")");

//		 LogUtil.outLogDetail("x:" + e1.getX());
		// LogUtil.outLogDetail("水平滑动:" + (e2.getX() - e1.getX()));
		// LogUtil.outLogDetail("垂直滑动:" + Math.abs((e2.getY() -
		// e1.getY())));

		if (e1.getX() <= 50 && e2.getX() - e1.getX() > verticalMinDistance && Math.abs(e2.getY() - e1.getY()) < 150 && Math.abs(velocityX) > minVelocity) {
			// finish();
			mListener.onSlideClose();
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// Log.i(getClass().getName(), "onShowPress-----" +
		// getActionName(e.getAction()));
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// Log.i(getClass().getName(), "onDown-----" +
		// getActionName(e.getAction()));
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// Log.i(getClass().getName(), "onDoubleTap-----" +
		// getActionName(e.getAction()));
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// Log.i(getClass().getName(), "onDoubleTapEvent-----" +
		// getActionName(e.getAction()));
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// Log.i(getClass().getName(), "onSingleTapConfirmed-----" +
		// getActionName(e.getAction()));
		return false;
	}
}
