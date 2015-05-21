package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.util.GlobalUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.view.PopMenu.OnItemClickListener;
import com.lidroid.xutils.BitmapUtils;

/**
 * 姐妹圈-图片拾取的Adapter
 * 
 * @author LiuYuHang
 * @date 2014年9月22日
 * 
 */
public class PhotoPickerAdapter extends BaseAdapter {
	static String TAG = "PhotoPickerAdapter";
	private List<String> mBitmaps;
	private OnClickListener mAddImageListener;
	private OnItemClickListener mImageListener;
//	private Context mContext;
	// private ImageWorker mImageWorker;
//	private BitmapUtils mBitmapUtils;
	private AQuery aQuery;

	private LayoutInflater mInflater;

	private int maxNumber;

	public PhotoPickerAdapter(Context context, List<String> bitmaps, BitmapUtils bitmapUtils) {
		mInflater = LayoutInflater.from(context);
		aQuery = new AQuery(context);

//		mContext = context;
		mBitmaps = bitmaps;

//		mBitmapUtils = bitmapUtils;
		
		// mBitmapUtils = BitmapOptionsFactory.newInstanceBitmapUtils(mContext,
		// drawable.goods_default_img);

		// mImageWorker = ImageWorker.newInstance(context);
		// mImageWorker.addParams(TAG,
		// ImageCacheParams.getDefaultParams(context,
		// R.drawable.goods_default_img));
	}

	/**
	 * 设置图片最大数量
	 * 
	 * @param max
	 * @author LiuYuHang
	 * @date 2014年11月5日
	 */
	public void setMaxNumber(int max) {
		maxNumber = max;
	}

	public void notifyWithData(List<String> bitmaps) {
		this.mBitmaps = bitmaps;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mBitmaps.size() < maxNumber)
			return mBitmaps.size() + 1;
		return mBitmaps.size();
	}

	public void removeItem(int position) {
		mBitmaps.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int arg0) {
		return mBitmaps.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// CropImageView imageView;
		ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(layout.adapter_photo_picker_item, null);

			viewHolder.imageView = (ImageView) convertView.findViewById(id.imageview_icon);
			viewHolder.pulsView = convertView.findViewById(id.view_plus);

			// imageView = new CropImageView(mContext);
			// imageView.setLayoutParams(new
			// AbsListView.LayoutParams(ViewUtil.dip2px(mContext, 80),
			// ViewUtil.dip2px(mContext, 80)));
			// imageView.setScaleType(ScaleType.FIT_XY);
			// convertView = imageView;

			convertView.setTag(viewHolder);
		} else {
			// imageView = (CropImageView) convertView;
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (position < mBitmaps.size()) {
			ViewUtil.swapViewWithAnimation(viewHolder.pulsView, viewHolder.imageView, true);
			// imageView.setImageBitmap(mBitmaps.get(position).get());
			// mBitmapUtils.display(viewHolder.imageView,
			// mBitmaps.get(position));
			aQuery.id(viewHolder.imageView).image(mBitmaps.get(position), true, true, GlobalUtil.displayMetrics.widthPixels / 4, drawable.default_goods_noborder);
			// mImageWorker.loadDiskBitmap(TAG, mBitmaps.get(position),
			// imageView);

			viewHolder.imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mImageListener != null)
						mImageListener.onItemClick(v, position);
				}
			});
		} else {
			ViewUtil.swapViewWithAnimation(viewHolder.imageView, viewHolder.pulsView, true);
			viewHolder.pulsView.setOnClickListener(mAddImageListener);
			// imageView.setImageResource(drawable.shoppingcart_plus);
			// imageView.setOnClickListener(mAddImageListener);
		}
		return convertView;
	}

	private class ViewHolder {
		private ImageView imageView;
		private View pulsView;
	}

	/**
	 * 点击图片的监听
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 * 
	 * @param listener
	 */
	public void setImageListener(OnItemClickListener listener) {
		mImageListener = listener;
	}

	/**
	 * 添加图片的监听器
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月22日
	 * 
	 * @param listener
	 */
	public void setOnAddImageListener(OnClickListener listener) {
		mAddImageListener = listener;
	}
}
