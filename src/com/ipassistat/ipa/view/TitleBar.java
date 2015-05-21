package com.ipassistat.ipa.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;

/**
 * 标题栏。
 * 
 * @author lxc
 * 
 */
public class TitleBar extends LinearLayout {
	// private Button leftButton,rightButton;
	private TextView titleTextView, rightTextView, shoppingcart_num_Textview;
	private Context mContext;
	private ImageView leftImgv, rightImgv, shareImgv;
	private boolean isSetListener;

	public static enum TitleBarButton {
		/* leftButton,rightButton, */leftImgv, rightImgv, shareImgv, rightTextView, shoppingcart_num_Textview;
	}

	public TitleBar(Context context) {
		super(context);
		this.mContext = context;
	}

	public TitleBar(Context context, AttributeSet attr) {
		super(context, attr);
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.title_bar, this);
		// leftButton = (Button) findViewById(R.id.left);
		// rightButton = (Button) findViewById(R.id.right);
		titleTextView = (TextView) findViewById(R.id.title);
		leftImgv = (ImageView) findViewById(R.id.left_imgv);
		rightImgv = (ImageView) findViewById(R.id.right_imgv);
		shareImgv = (ImageView) findViewById(R.id.share_imgv);
		rightTextView = (TextView) findViewById(R.id.right_textview);
		shoppingcart_num_Textview = (TextView) findViewById(R.id.shoppingcart_num_show);
		
		leftImgv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (!isSetListener) {
					((Activity) mContext).finish();
				}
			}
		});
	}

	/**
	 * 设置左右按钮的显示和隐藏状态，其中btn的值为TitleBarButton类中的一个，state为View.gone等值。
	 * 
	 * @param btn
	 * @param state
	 */
	public void setVisibility(TitleBarButton btn, int state) {
		if (btn == TitleBarButton.leftImgv) {
			leftImgv.setVisibility(state);
			isSetListener = true;
		} else if (btn == TitleBarButton.rightImgv) {
			rightImgv.setVisibility(state);
		} else if (btn == TitleBarButton.shareImgv) {
			shareImgv.setVisibility(state);
		} else if (btn == TitleBarButton.shoppingcart_num_Textview) {
			shoppingcart_num_Textview.setVisibility(state);
		} else {
			rightTextView.setVisibility(state);
		}
	}

	/**
	 * 设置组件的背景
	 * 
	 * @param btn
	 * @param drawable
	 */
	public void setImageBackGround(TitleBarButton btn, Drawable drawable) {
		if (btn == TitleBarButton.rightTextView) {
			rightTextView.setBackgroundDrawable(drawable);
		} else if (btn == TitleBarButton.leftImgv) {
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			leftImgv.setImageDrawable(drawable);
		} else if (btn == TitleBarButton.rightImgv) {
			rightImgv.setImageDrawable(drawable);
		} else if (btn == TitleBarButton.shareImgv) {
			shareImgv.setImageDrawable(drawable);
		} else if (btn == TitleBarButton.shoppingcart_num_Textview) {
			shoppingcart_num_Textview.setBackgroundDrawable(drawable);
		}
	}

	/**
	 * 设置标题内容
	 * 
	 * @param title
	 */
	public void setTitleText(String title) {
		titleTextView.setText(title);
	}

	/**
	 * 设置右边textview 内容
	 * 
	 * @param text
	 */
	public void setRightTextViewText(String text) {
		rightTextView.setText(text);
	}

	/**
	 * 设置购物车商品数量内容
	 * 
	 * @param text
	 */
	public void setshoppingcartNumTextViewText(String text) {
		shoppingcart_num_Textview.setText(text);
	}

	/**
	 * 给左右按钮设置监听事件
	 * 
	 * @param v
	 * @param l
	 */
	public void setButtonClickListener(TitleBarButton btn, View.OnClickListener l) {
		if (btn == TitleBarButton.leftImgv) {
			leftImgv.setOnClickListener(l);
		} else if (btn == TitleBarButton.rightImgv) {
			rightImgv.setOnClickListener(l);
		} else if (btn == TitleBarButton.shareImgv) {
			shareImgv.setOnClickListener(l);
		} else {
			rightTextView.setOnClickListener(l);
		}
	}

	@Deprecated
	public void setButtonClickListener(View v, View.OnClickListener l) {
		v.setOnClickListener(l);
	}

	@Deprecated
	public View getLeftButton() {
		return leftImgv;
	}

	@Deprecated
	public View getRightButton() {
		return rightImgv;
	}

	@Deprecated
	public View getShareButton() {
		return shareImgv;
	}
	
	

}
