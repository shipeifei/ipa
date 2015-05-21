package com.ipassistat.ipa.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.ui.activity.MediaPhotoGridView;
import com.ipassistat.ipa.util.GlobalUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.lidroid.xutils.BitmapUtils;

public class ImageSelectViewPagerAdapter extends PagerAdapter {
	public interface SelectChangedListener {
		void onChanged(int count);
	}

	private List<String> list;
	// private List<ImageHolder> listSelectState;// 记录列表的选中状态list
	private HashMap<String, Boolean> listSelectMap;// 记录列表的选中状态map

	// private DisplayImageOptions options; // 配置图片加载及显示选项
	// private BitmapUtils mBitmapUtils;
//	private Context context;
	private LayoutInflater mInflater;
	private SelectChangedListener listener;
	
	private AQuery aQuery;

	private int mChildCount = 0;

//	private class ImageHolder {
//		private String path;
//		private boolean select;
//
//		public ImageHolder(String path, boolean select) {
//			this.path = path;
//			this.select = select;
//		}
//	}

	public ImageSelectViewPagerAdapter(Context context, BitmapUtils bitmapUtils, List<String> list, List<String> selectList, SelectChangedListener listener) {
		this.mInflater = LayoutInflater.from(context);
//		this.context = context;
		// this.mBitmapUtils = bitmapUtils;
		this.list = list;
		this.listener = listener;
		
		aQuery = new AQuery(context);

		// options = new
		// DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher)
		// // 在ImageView加载过程中显示图片
		// .showImageForEmptyUri(R.drawable.ic_launcher) // image连接地址为空时
		// .showImageOnFail(R.drawable.ic_launcher) // image加载失败
		// .cacheInMemory(true) // 加载图片时会在内存中加载缓存
		// .cacheOnDisc(true) // 加载图片时会在磁盘中加载缓存
		// // .displayer(new RoundedBitmapDisplayer(20)) //
		// // 设置用户加载图片task(这里是圆角图片显示)
		// .build();

		// listSelectState = new ArrayList<ImageHolder>();
		// for (String string : selectList) {
		// listSelectState.add(new ImageHolder(string, true));
		// }
		listSelectMap = new HashMap<String, Boolean>();
		for (String string : selectList) {
			listSelectMap.put(string, true);
		}

		// 默认全部是选中的
		listener.onChanged(list.size());
	}

	public ArrayList<String> getSelectList() {
		ArrayList<String> countArray = new ArrayList<String>();

		for (Iterator<Entry<String, Boolean>> iterator = listSelectMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Boolean> entry = iterator.next();
			if (entry.getValue() != null && entry.getValue()) {
				countArray.add(entry.getKey());
			} else {
				continue;
			}
		}

		// for (int i = 0; i < listSelectState.size(); i++) {
		// ImageHolder holder = listSelectState.get(i);
		// if (holder.select) {
		// countArray.add(holder.path);
		// }
		// }
		return countArray;
	}

	@Override
	public void notifyDataSetChanged() {
		mChildCount = getCount();
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// super.destroyItem(container, position, object);
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View root = mInflater.inflate(layout.adapter_image_browse, null);
		container.addView(root);

		ImageView imageView = (ImageView) root.findViewById(id.imageview);
		String imagePath = list.get(position);
		aQuery.id(imageView).image(imagePath, true, true, GlobalUtil.displayMetrics.widthPixels, 0);
		

		final View selectView = root.findViewById(id.view_select);
		selectView.setVisibility(isSelect(position) ? View.VISIBLE : View.GONE);
		root.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getSelectList().size() <MediaPhotoGridView.mMaxSelectNumber)
				{
					boolean nowSelect = isSelect(position);
					listSelectMap.put(getImagePathByPosition(position), !nowSelect);
					
					selectView.setVisibility(isSelect(position) ? View.VISIBLE : View.GONE);
//					notifyDataSetChanged();
					
					listener.onChanged(getSelectList().size());
				}
				else {
				     //反选
					if(isSelect(position))
					{
						boolean nowSelect = isSelect(position);
						listSelectMap.put(getImagePathByPosition(position), !nowSelect);
						
						selectView.setVisibility(isSelect(position) ? View.VISIBLE : View.GONE);
						
						listener.onChanged(getSelectList().size());
					}
					
				}
				
			}
		});

		return root;
	}

	private boolean isSelect(int position) {
		String imagePath = getImagePathByPosition(position);
		return listSelectMap.get(imagePath) != null && listSelectMap.get(imagePath);
	}

	private String getImagePathByPosition(int position) {
		return list.get(position);
	}

}
