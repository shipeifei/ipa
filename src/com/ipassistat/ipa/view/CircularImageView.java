package com.ipassistat.ipa.view;

import java.lang.ref.SoftReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.ImageUtil;
/***
 * 
 * @author shipeifei
 *
 */
public class CircularImageView extends ImageView {
	private final int DEFAULT_INNER_BORDER = 0;
	private final int DEFAULT_INNER_COLOR = Color.WHITE;
	private final int DEFAULT_OUTER_BORDER = 0;
	private final int DEFAULT_OUTER_COLOR = Color.YELLOW;
	private float mBorderInnerWidth;
	private int mBorderInnerColor;
	private float mBorderOuterWidth;
	private int mBorderOuterColor;
	private int mViewWidth = 100; // 图片的大小尺寸
	private int mViewHeight = 100;
	private SoftReference<Bitmap> mImage;
	private Paint mPaint;
	private Paint mPaintOuterBorder;
	private BitmapShader mShader;
	private TypedArray mTa;
	private Context mContext;
	private Paint mPaintInnerBorder;
	//private BitmapUtils mBitmapUtil;
//	private ImageLoader mImageLoader;

	public CircularImageView(Context context) {
		super(context);
		mContext = context;
		setup();
	}

	public CircularImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mTa = context.obtainStyledAttributes(attrs, R.styleable.circleImage);
		initDimems();
		setup();
	}

	public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mTa = context.obtainStyledAttributes(attrs, R.styleable.circleImage);
		initDimems();
		setup();
	}

	/**
	 * @discretion: 加载来自网络的图片
	 * @author: MaoYaNan
	 * @date: 2014-11-7 下午2:32:02
	 * @param url
	 */
	public void setImageUrl(String url) {
		//mBitmapUtil.display(this, url);
		BitmapOptionsFactory.getInstance(getContext()).display(this, url, BitmapOptionsFactory.getOptionConfig(getContext(), drawable.personal_photo));
//		mImageLoader.displayImage(url, this,BitmapOptionsFactory.getImageOption(drawable.personal_photo));
	}

	private void initDimems() {
		mBorderInnerWidth = mTa.getDimension(
				R.styleable.circleImage_InnerWidth, DEFAULT_INNER_BORDER);
		mBorderOuterWidth = mTa.getDimension(
				R.styleable.circleImage_OuterWidth, DEFAULT_OUTER_BORDER);
		mBorderInnerColor = mTa.getColor(
				R.styleable.circleImage_InnerColor, DEFAULT_INNER_COLOR);
		mBorderOuterColor = mTa.getColor(
				R.styleable.circleImage_OuterColor, DEFAULT_OUTER_COLOR);
	}

	private void setup() {
		// init paint
		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		mPaintOuterBorder = new Paint();
		mPaintOuterBorder.setColor(mBorderOuterColor);
		mPaintOuterBorder.setAntiAlias(true);

		mPaintInnerBorder = new Paint();
		mPaintInnerBorder.setColor(mBorderInnerColor);
		mPaintInnerBorder.setAntiAlias(true);

//		mImageLoader = ImageLoader.getInstance();
	}

	// public void setBorderWidth(int borderWidth) {
	// this.borderOuterWidth = borderWidth;
	// this.invalidate();
	// }
	//
	// public void setBorderColor(int borderColor) {
	// if (paintOuterBorder != null)
	// paintOuterBorder.setColor(borderColor);
	//
	// this.invalidate();
	// }

	private void loadBitmap() {
		Drawable drawable = this.getDrawable();

		if (drawable != null) {
			mImage = new SoftReference<Bitmap>(
					ImageUtil.drawableToBitmap(drawable));
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		// load the bitmap
		loadBitmap();
		// init shader
		if (mImage != null && mImage.get() != null) {
			mShader = new BitmapShader(Bitmap.createScaledBitmap(mImage.get(),
					canvas.getWidth(), canvas.getHeight(), false),
					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP); // 将一张图片给的颜色布局给这个着色器。
			mPaint.setShader(mShader); // 设置着色器
			int circleCenter = mViewWidth / 2;

			// circleCenter is the x or y of the view's center
			// radius is the radius in pixels of the cirle to be drawn
			// paint contains the shader that will texture the shape
			canvas.drawCircle(circleCenter, circleCenter, circleCenter,
					mPaintOuterBorder); // 画圆形图片
			canvas.drawCircle(circleCenter, circleCenter, circleCenter
					- mBorderOuterWidth, mPaintInnerBorder);
			canvas.drawCircle(circleCenter, circleCenter, circleCenter
					- mBorderOuterWidth - mBorderInnerWidth, mPaint); // 画背景图片
		}
		mShader = null;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = measureWidth(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

		setMeasuredDimension(width, height);
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			result = mViewWidth;
		}
		mViewWidth = specSize;

		return result;
	}

	private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpecHeight);
		int specSize = MeasureSpec.getSize(measureSpecHeight);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = mViewHeight;
		}
		return result;
	}
}