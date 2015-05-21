package com.ipassistat.ipa.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.view.ViewFiller;
import com.lidroid.xutils.BitmapUtils;

public class SearchProductAdapter extends PaginationAdapter<SaleProduct> {
	static String TAG = SearchProductAdapter.class.getSimpleName();
//	private ImageWorker mImageWorker;
	private BitmapUtils mBitmapUtils;

	// private LayoutInflater mInflater;
	// private List<SaleProduct> mData;

	// public SearchProductAdapter(Context context, List<SaleProduct> list) {
	// mInflater = LayoutInflater.from(context);
	// mData = list;
	// }

	public SearchProductAdapter(Context context, List<SaleProduct> data) {
		super(context, data);

//		mImageWorker = ImageWorker.newInstance(getContext());
//		mImageWorker.addParams(TAG, ImageCacheParams.getDefaultParams(getContext(), drawable.goods_default_img));
		
		mBitmapUtils = BitmapOptionsFactory.getInstance(getContext());

//		mBitmapUtils = new BitmapUtils(getContext());
//		mBitmapUtils.configDefaultLoadFailedImage(drawable.goods_default_img);
//		mBitmapUtils.configDefaultLoadingImage(drawable.goods_default_img);
	}

	// @Override
	// public int getCount() {
	// return mData == null ? 0 : mData.size();
	// }
	//
	// @Override
	// public Object getItem(int arg0) {
	// return mData.get(arg0);
	// }
	//
	// @Override
	// public long getItemId(int arg0) {
	// return 0;
	// }

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			// convertView =
			// mInflater.inflate(R.layout.activity_sistergroup_product_info,
			// null);
			// holder.labelView = (TextView)
			// convertView.findViewById(id.textview_title);
			// holder.buyCountView = (TextView)
			// convertView.findViewById(id.textview_buycount);
			// holder.priceView = (TextView)
			// convertView.findViewById(id.textview_price);
			// holder.olderPriceView = (TextView)
			// convertView.findViewById(id.textview_oldprice);

			convertView = getInflater().inflate(R.layout.adapter_goods_listview, null);
			// holder.labelView = (TextView)
			// convertView.findViewById(id.goods_name_show);
			// holder.buyCountView = (TextView)
			// convertView.findViewById(id.goods_sale_num_show);
			// holder.priceView = (TextView)
			// convertView.findViewById(id.goods_price_show);
			// holder.olderPriceView = (TextView)
			// convertView.findViewById(id.goods_old_price_show);

			convertView.setTag(holder);;
		} else {
			holder = (Holder) convertView.getTag();
		}

		ViewFiller.fillProductView(getItem(position), convertView, mBitmapUtils);
		// ProductVo item = mData.get(position);
		//
		// holder.labelView.setText(item.title);
		//
		// holder.priceView.setText("￥" + item.sell_price);
		// holder.olderPriceView.setText("￥" + item.market_price);
		//
		// holder.buyCountView.setText(item.buy_count + "人购买");

		return convertView;
	}

	private class Holder {
		// TextView labelView, buyCountView, priceView, olderPriceView;
	}
}
