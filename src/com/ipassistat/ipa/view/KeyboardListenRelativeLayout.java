package com.ipassistat.ipa.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 监听键盘弹出和收藏状态需要用到的自定义组件（购物车）
 * @author WanRui
 */
public class KeyboardListenRelativeLayout extends RelativeLayout {
	
	private static final String TAG = KeyboardListenRelativeLayout.class.getSimpleName();
	
	public static final byte KEYBOARD_STATE_SHOW = -3;
	public static final byte KEYBOARD_STATE_HIDE = -2;
	public static final byte KEYBOARD_STATE_INIT = -1;
	
	private boolean mHasInit = false;
	private boolean mHasKeyboard = false;
	private int mHeight;
	
	private IOnKeyboardStateChangedListener onKeyboardStateChangedListener;
	
	public KeyboardListenRelativeLayout(Context context) {
		super(context);
	}
	public KeyboardListenRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public KeyboardListenRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setOnKeyboardStateChangedListener(IOnKeyboardStateChangedListener onKeyboardStateChangedListener) {
		this.onKeyboardStateChangedListener = onKeyboardStateChangedListener;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(!mHasInit) {
			mHasInit = true;
			mHeight = b;
			if(onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_INIT);
			}
		} else {
			mHeight = mHeight < b ? b : mHeight;
		}
		
		if(mHasInit && mHeight > b) {
			mHasKeyboard = true;
			if(onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_SHOW);
			}
		}
		if(mHasInit && mHasKeyboard && mHeight == b) {
			mHasKeyboard = false;
			if(onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_HIDE);
			}
		}
	}
	
	public interface IOnKeyboardStateChangedListener {
		public void onKeyboardStateChanged(int state);
	}
}
