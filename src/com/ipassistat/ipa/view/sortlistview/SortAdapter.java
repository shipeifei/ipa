package com.ipassistat.ipa.view.sortlistview;

import java.util.List;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.view.sortlistview.SortListView.OnSortListViewClickListener;
import com.ipassistat.ipa.view.sortlistview.SortListView.OnSortListViewLongClickListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.TextView;

@SuppressLint({ "DefaultLocale", "InflateParams" })
public class SortAdapter<T> extends BaseAdapter implements SectionIndexer{
	private List<SortModel<T>> list = null;
	private Context mContext;
	private OnSortListViewClickListener<T> mListViewClickListener;
	private OnSortListViewLongClickListener<T> mListViewLongClickListener;
	
	public SortAdapter(Context mContext) {
		this.mContext = mContext;
	}
	
	public void setOnSortListViewClickListener(OnSortListViewClickListener<T> listener){
		this.mListViewClickListener = listener;
	}
	
	public void setOnSortListViewLongClickListener(OnSortListViewLongClickListener<T> listener){
		this.mListViewLongClickListener = listener;
	}
	
	public void loadData(List<SortModel<T>> list){
		this.list = list;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel<T>> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel<T> mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_city_sort, null);
			viewHolder.tvTitle = (Button) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.line = view.findViewById(R.id.line);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		//根据position获取分类的首字母的Char ascii值ֵ
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
			viewHolder.line.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
			viewHolder.line.setVisibility(View.GONE);
		}
	
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		viewHolder.tvTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					if(mListViewClickListener!=null) 
						mListViewClickListener.onClick(position, mContent.getT());
			}
		});
		viewHolder.tvTitle.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if(mListViewLongClickListener!=null) 
					mListViewLongClickListener.onLongClick(position, mContent.getT());
				return false;
			}
		});
		return view;
	}

	final static class ViewHolder {
		TextView tvLetter;
		Button tvTitle;
		View line;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
	

	@Override
	public Object[] getSections() {
		return null;
	}
	
	
}