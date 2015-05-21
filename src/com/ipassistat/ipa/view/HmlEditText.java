/**
 * 
 */
package com.ipassistat.ipa.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.ipassistat.ipa.R;

/***
 * 带有清空功能的EditText
 * com.ipassistat.ipa.view.HmlEditText
 * @author 时培飞 
 * Create at 2015-4-30 下午5:26:29
 */
public class HmlEditText extends EditText implements OnFocusChangeListener, TextWatcher {
	private Drawable mExpandDrawable;

	public HmlEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HmlEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HmlEditText(Context context) {
		super(context);
		init();
	}

	private void init() {
		mExpandDrawable = getCompoundDrawables()[2];
		if (mExpandDrawable == null) {
			mExpandDrawable = getResources().getDrawable(R.drawable.icon_delete);
		}
		mExpandDrawable.setBounds(0, 0, mExpandDrawable.getIntrinsicWidth(), mExpandDrawable.getIntrinsicHeight());
		setClearIconVisible(false);
		super.setOnFocusChangeListener(this);
		addTextChangedListener(this);
	}

	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener f) {
		this.f = f;
	}

	private OnFocusChangeListener f;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - mExpandDrawable.getIntrinsicWidth());
				if (tappedX) {
					setText("");
					event.setAction(MotionEvent.ACTION_CANCEL);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
		if (f != null) {
			f.onFocusChange(v, hasFocus);
		}
	}

	protected void setClearIconVisible(boolean visible) {
		Drawable x = visible ? mExpandDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		setClearIconVisible(s.length() > 0);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
