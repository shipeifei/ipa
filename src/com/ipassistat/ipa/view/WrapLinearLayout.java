package com.ipassistat.ipa.view;

import java.util.Hashtable;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 自动换行的LinearLayout，未对margin做处理
 * 
 * @author sargent
 * @date 2014年9月11日
 */
public class WrapLinearLayout extends LinearLayout {
	private final int CHILD_MAGIN = 10;

	int mLeft, mRight, mTop, mBottom;

	private Hashtable<View, Position> map = new Hashtable<View, Position>();

	public WrapLinearLayout(Context context) {
		super(context);
	}

	public WrapLinearLayout(Context context, int horizontalSpacing,
			int verticalSpacing) {
		super(context);
	}

	public WrapLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int mWidth = MeasureSpec.getSize(widthMeasureSpec);
		int mCount = getChildCount();
		int mX = 0;
		int mY = 0;
		mLeft = 0;
		mRight = 0;
		mTop = 0;
		mBottom = 0;

		int j = 0;

		View lastview = null;
		for (int i = 0; i < mCount; i++) {
			final View child = getChildAt(i);

			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

			LinearLayout.LayoutParams params = (LayoutParams) child
					.getLayoutParams();

			// 此处增加onlayout中的换行判断，用于计算所需的高度
			int childw = child.getMeasuredWidth() + params.leftMargin
					+ params.rightMargin;
			int childh = child.getMeasuredHeight() + params.topMargin
					+ params.bottomMargin;
			mX += childw; // 将每次子控件宽度进行统计叠加，如果大于设定的高度则需要换行，高度即Top坐标也需重新设置

			Position position = new Position();
			mLeft = getPosition(i - j, i);
			mRight = mLeft + child.getMeasuredWidth() + params.leftMargin
					+ params.rightMargin;
			if (mX >= mWidth) {
				mX = childw;
				mY += childh;
				j = i;
				mLeft = 0;
				mRight = mLeft + child.getMeasuredWidth() + params.leftMargin
						+ params.rightMargin;
				mTop = mY + CHILD_MAGIN;
				// PS：如果发现高度还是有问题就得自己再细调了
			}
			mBottom = mTop + child.getMeasuredHeight() + params.topMargin
					+ params.bottomMargin;
			mY = mTop; // 每次的高度必须记录 否则控件会叠加到一起
			position.left = mLeft;
			position.top = mTop;
			position.right = mRight;
			position.bottom = mBottom;
			map.put(child, position);
		}
		setMeasuredDimension(mWidth, mBottom);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(1, 1); // default of 1px spacing
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		

		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			Position pos = map.get(child);
			if (pos != null) {
				child.layout(pos.left, pos.top, pos.right, pos.bottom);
			} else {
				Log.i("MyLayout", "error");
			}
		}
	}

	private class Position {
		int left, top, right, bottom;
	}

	public int getPosition(int IndexInRow, int childIndex) {
		if (IndexInRow > 0) {
			return getPosition(IndexInRow - 1, childIndex - 1)
					+ getChildAt(childIndex - 1).getMeasuredWidth() + 8;
		}
		return getPaddingLeft();
	}
}