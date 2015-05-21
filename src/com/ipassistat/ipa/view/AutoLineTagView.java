package com.ipassistat.ipa.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ipassistat.ipa.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

/**
 * 自动换行的tagView
 * @author renheng
 *
 */
@SuppressLint("InflateParams")
public class AutoLineTagView extends LinearLayout {

	private LinearLayout layout;
	@SuppressWarnings("unused")
	private CheckedTextView tag;
	private LayoutInflater inflater;
	private List<String> list;
	private Activity activity;
	private int maxLayout = 10; // 最大的horizontalLayout 个数
	private int blankWidth = getResources().getDimensionPixelSize(
			R.dimen.tag_margin_left); // 空白宽度
	private boolean hasClick = true; // 是否给tagview添加onclickListener false 无 ,
										// true 有
	//private boolean isCheckTag = false;

	private int tagLine = 0; // 设置tag标签的行数, 0为自动换行

	private int tagBg = R.drawable.bg_border_tag; // 设置tagview的背景
	
	private LinkedList<String> tagText=new LinkedList<String>(); //所有tagview的text，以逗号分隔

	/*
	 * 当tag选中或取消的回调接口
	 */
	public interface OnTagCheckedListener{
		/**
		 *  当tag选中或取消的回调函数， list为选中的textview的值的结合
		 * @param list
		 */
		public void TagChecked(LinkedList<String> list);
	};
	
	private OnTagCheckedListener onTagCheckedListener;
	
	private enum WidthHeight {
		width, height;
	}

	public AutoLineTagView(Context context, AttributeSet attrs, int defStyle,
			List<String> list, boolean hasClick, int tagBg) {
		super(context, attrs, defStyle);
		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.view_autoline_tag, this);
		layout = (LinearLayout) findViewById(R.id.container);
		activity = (Activity) context;
		this.list = list;
		this.hasClick=hasClick;
		if(tagBg!=0){
			this.tagBg=tagBg;
		}
	}

	public AutoLineTagView(Context context, AttributeSet attrs,
			List<String> list, boolean hasClick, int tagBg) {
		this(context, attrs, 0, list, hasClick, tagBg);
	}

	/**
	 * 构造函数
	 * @param context 上下文
	 * @param list 要显示的文字的集合
	 * @param hasClick 是否可以点击
	 * @param tagBg  每一个tag的背景
	 */
	public AutoLineTagView(Context context, List<String> list, boolean hasClick, int tagBg) {
		this(context, null, list, hasClick, tagBg);
	}



	public LinkedList<String> getTagText(){
		return tagText;
	}
	
	public void setOnTagCheckedListener(OnTagCheckedListener onTagCheckedListener){
		this.onTagCheckedListener=onTagCheckedListener;
	}
	

	/*
	 * 得到tag标签的水平的linearLayout
	 */
	private LinearLayout getHorizontalLayout() {
		LinearLayout lin = new LinearLayout(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		int left = getResources().getDimensionPixelSize(
				R.dimen.tag_layout_margin_left);
		@SuppressWarnings("unused")
		int top = getResources().getDimensionPixelSize(R.dimen.tag_margin_top);
		int right = getResources().getDimensionPixelSize(
				R.dimen.tag_layout_margin_right);
		int bottom = getResources().getDimensionPixelSize(
				R.dimen.tag_margin_bottom);
		params.setMargins(left, 0, right, bottom);
		lin.setOrientation(LinearLayout.HORIZONTAL);
		lin.setLayoutParams(params);
		return lin;
	}

	/*
	 * 得到标签view
	 */
	private CheckedTextView getTagView(String s) {
		CheckedTextView t = (CheckedTextView) inflater.inflate(R.layout.tv_tag,
				null);
		t.setBackgroundResource(tagBg);
		t.setText(s);
		return t;
	}

	public void setTagLine(int tagLine){
		this.tagLine = tagLine;
		addHorizontalLayout();
	}
	
	/*
	 * 添加水平的linearLayout
	 */
	private void addHorizontalLayout() {
		// test();
		int useWidth = 0; // 已经使用的宽度
		int screenWidth = getScreenLabel(WidthHeight.width);
	//	int screenWidth = getScreenLabel(WidthHeight.width)-39;
		LinearLayout linear = null;
		int index = 0;
		int blankWidth = getBlankViewWidth();

		List<LinearLayout> layoutList = getHorizontalLayoutList();

		@SuppressWarnings("rawtypes")
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			final String text = (String) iterator.next();
			final CheckedTextView tv = getTagView(text);
			int width = getLabel(tv, WidthHeight.width);
			useWidth = useWidth + width;
			int remainWidth = screenWidth - useWidth;


			if (isEnough(width, remainWidth)) {
				linear = layoutList.get(index);
				linear.addView(tv);
				addBlankView(linear);
				useWidth = useWidth + blankWidth;
			} else {
				//如果是0， 则获取所有，自动换行
				if (tagLine == 0) {
					layout.addView(linear);
					useWidth = 0;
					index++;
					linear = layoutList.get(index);
					linear.addView(tv);
					useWidth=getLabel(tv, WidthHeight.width);	
					addBlankView(linear);
					useWidth = useWidth + blankWidth;
				} else {
					if (index < tagLine - 1) {
						layout.addView(linear);
						useWidth = 0;
						index++;
						linear = layoutList.get(index);
						linear.addView(tv);
						useWidth=getLabel(tv, WidthHeight.width);
						addBlankView(linear);
						useWidth = useWidth + blankWidth;
					}
				}
			}
			
			if (hasClick) {

				tv.setOnClickListener(new OnClickListener() {
					boolean isCheckTag = false;
					@Override
					public void onClick(View v) {
						tv.setChecked(isCheckTag);
						String str=null;
						str=tv.getText().toString();
						if (isCheckTag == false) {
							isCheckTag = true;
							tv.setBackgroundResource(R.drawable.bg_border_tag_select);
							tv.setTextColor(Color.WHITE);
							tagText.add(str);
							onTagCheckedListener.TagChecked(tagText);
							
						} else {
							isCheckTag = false;
							tv.setBackgroundResource(R.drawable.bg_border_tag);
							tv.setTextColor(getResources().getColor(
									R.color.tag_text_normal));
							tagText.remove(str);
							onTagCheckedListener.TagChecked(tagText);
						}

					}
				});
			}
		}
		layout.addView(linear);
//		if(index<tagLine-1){
//			
//		}else{
//			layout.addView(linear);
//		}
		

	}

	private List<LinearLayout> getHorizontalLayoutList() {
		List<LinearLayout> list = new ArrayList<LinearLayout>();
		for (int i = 0; i < maxLayout; i++) {
			list.add(getHorizontalLayout());
		}
		return list;
	}

	/*
	 * 返回控件的宽度和高度
	 */
	private int getLabel(View v, WidthHeight wh) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
		if (wh == WidthHeight.width) {
			return v.getMeasuredWidth();
		} else {
			return v.getMeasuredHeight();
		}
	}

	@SuppressWarnings("unchecked")
	public void setList(@SuppressWarnings("rawtypes") List list) {
		this.list = list;
	}

	/*
	 * 返回屏幕的宽度和高度
	 */
	@SuppressWarnings("deprecation")
	private int getScreenLabel(WidthHeight wh) {
		int w = activity.getWindowManager().getDefaultDisplay().getWidth();
		int h = activity.getWindowManager().getDefaultDisplay().getHeight();
		if (wh == WidthHeight.width) {
			return w;
		} else {
			return h;
		}
	}

	/**
	 * 判断剩余的宽度是否能够放置控件
	 * 
	 * @param width
	 *            控件的宽度
	 * @param remainWidth
	 *            屏幕剩余的宽度
	 * @return true 能够放置 false 不能够放置
	 */
	private boolean isEnough(int width, int remainWidth) {
		if (width < remainWidth) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 得到tag的间距，在这里间距有空白色的view来填充
	 */
	private View getBlankView() {
		View view = new View(activity);
		int width = blankWidth;
		int height = 1;
		LayoutParams params = new LayoutParams(width, height);
		view.setLayoutParams(params);
		return view;
	}

	/*
	 * 在linearlayout中添加blankview
	 */
	private void addBlankView(LinearLayout l) {
		View blankView = getBlankView();
		l.addView(blankView);
	}

	private int getBlankViewWidth() {
		View view = getBlankView();
		return getLabel(view, WidthHeight.width);
	}

}
