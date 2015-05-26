package com.ipassistat.ipa.view.sortlistview;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.request.entity.ContactPerson;
import com.ipassistat.ipa.view.sortlistview.SideBar.OnTouchingLetterChangedListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 
 * @author shipeifei
 *
 * @param <T>
 */
public class SortListView<T> extends LinearLayout {

	private ListView mListView;
	private SideBar mSideBar;
	private TextView mDialog;
	private SortAdapter<T> mAdapter;
	private Context mContext;

	private CharacterParser mCharacterParser;
	private PinyinComparator<T> mPinyinComparator;
	private List<SortModel<T>> mSourceDateList; // 填充的list

	//item的点击事件监听器
	public interface OnSortListViewClickListener<T> {

		public void onClick(int position, T t);

	}
	
	//item的长按点击事件监听器
	public interface OnSortListViewLongClickListener<T>{
		
		public void onLongClick(int position, T t);
	
	}

	public SortListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SortListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SortListView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		inflate(context, R.layout.view_sortlistview, this);
		setBackgroundResource(R.color.white);
		// 实例化汉字转拼音类
		mCharacterParser = CharacterParser.getInstance();

		mPinyinComparator = new PinyinComparator<T>();

		mSideBar = (SideBar) findViewById(R.id.sidrbar);
		mDialog = (TextView) findViewById(R.id.dialog);
		mListView = (ListView) findViewById(R.id.country_lvcountry);
		mSideBar.setTextView(mDialog);
		mAdapter = new SortAdapter<T>(mContext);
	}

	public void setSortData(ISort<T> sort) {
		mSourceDateList = filledData(sort);
		// 根据a-z进行排序源数据
		Collections.sort(mSourceDateList, mPinyinComparator);
		mAdapter.loadData(mSourceDateList);
		mListView.setAdapter(mAdapter);

		// 设置右侧触摸监听
		mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = mAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mListView.setSelection(position);
				}
			}
		});
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param data
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private List<SortModel<T>> filledData(ISort<T> iSort) {
		List<SortModel<T>> mSortList = new LinkedList<SortModel<T>>();
		SortModel<T> sortModel = null;
		List<ContactPerson> strs =(List<ContactPerson>) iSort.getSortStrings();
		List<T> ts = iSort.getSortModel();
		
		if(strs ==null) throw new NullPointerException("getSortStrings() can not return null");
		if(ts == null) throw new NullPointerException("getSortModel() can not return null");
		if(ts != null && strs.size() != ts.size()) throw new IndexOutOfBoundsException("please ensure the size of getSortStrings() and getSortModel() return the same");

		for (int i = 0; i < strs.size(); i++) {
			sortModel = new SortModel<T>();
			sortModel.setName(strs.get(i).getName());
			sortModel.setT(ts.get(i));
			
			// 汉字转换成拼音
			String pinyin = mCharacterParser.getSelling(strs.get(i).getName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;

	}

	public void setOnSortListViewClick(OnSortListViewClickListener<T> listener) {
		mAdapter.setOnSortListViewClickListener(listener);
	}
	
	public void setOnSortListViewLongClick(OnSortListViewLongClickListener<T> listener) {
		mAdapter.setOnSortListViewLongClickListener(listener);
	}
	
	public void setBackground(int r){
		this.setBackgroundResource(r);
	}

	public void updateListView(List<SortModel<T>> data){
		if(data!=null) mAdapter.updateListView(data);
		else throw new NullPointerException("please input update data");
	}
	
	public void notifyDataSetInvalidated(){
		mAdapter.notifyDataSetInvalidated();
	}
	
}
