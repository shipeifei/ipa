package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 分页使用的适配器
 * 
 * @author LiuYuHang
 * @date 2014年9月25日
 * 
 */
public abstract class PaginationAdapter<T> extends BaseAdapter {
	public Context context;
	public DataDelegate dataDelegate;
	public LayoutInflater inflater;
	private List<T> mData;
	public int page = 0;

	public interface DataDelegate {
		/**
		 * 数据发送改变的回调
		 * 
		 * @param count
		 * @author LiuYuHang
		 * @date 2014年10月11日
		 */
		void onDataChanaged(int count);

		/**
		 * 数据重置的时候回调
		 *
		 * @author LiuYuHang
		 * @date 2014年10月24日
		 */
		void onDataReset();

		/**
		 * 如果没有加载到数据的回调
		 * 
		 * @param message
		 * @author LiuYuHang
		 * @date 2014年10月11日
		 */
		public void onNetError(CharSequence message);
	}

	public PaginationAdapter() {
		resetPageNumber();
	}

	public PaginationAdapter(Context context, List<T> data) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		mData = data;
		resetPageNumber();
	}

	public void setData(List<T> data) {
		this.mData = data;
	}

	public List<T> getData() {
		return mData;
	}

	public void resetPageNumber() {
		page = 0;
	}

	/**
	 * @discretion: 删除指定位置数据
	 * @author: MaoYaNan
	 * @date: 2014-10-21 下午6:38:09
	 * @param position
	 */
	public void removeItem(int position) {
		mData.remove(position);
		notifyDataSetChanged();
	}

	/**
	 * 重新填充Adapter的数据
	 * 
	 * @param list
	 * @author LiuYuHang
	 * @date 2014年10月14日
	 */
	public void resetData(List<T> list) {
		if (this.mData != null) mData.clear();
		setData(list);
		resetPageNumber();
		notifyDataSetChanged();
		if (dataDelegate != null) dataDelegate.onDataReset();
	}

	/**
	 * 加载下一页数据，之后Page + 1 (已做判空处理)
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月25日
	 * 
	 * @param list
	 */
	public void loadNextPage(List<T> list) {
		if (list == null || list.size() == 0) return;
		page++;
		mData.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (dataDelegate != null) dataDelegate.onDataChanaged(getCount());
	}

	@Override
	public int getCount() {
		int count = mData == null ? 0 : mData.size();
		// if (dataDelegate != null)
		// dataDelegate.onDataChanaged(getCount());//会造成死循环
		return count;
	}

	public T getItem(int position) {
		return mData == null ? null : mData.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public Context getContext() {
		return context;
	}

}
