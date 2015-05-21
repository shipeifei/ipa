package com.ipassistat.ipa.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;
import com.ipassistat.ipa.util.ViewUtil;
import com.lidroid.xutils.BitmapUtils;

/**
 * 数据填充辅助类
 * 
 * @author LiuYuHang
 * @date 2014年9月24日
 *
 */
public class ViewFiller {

	/**
	 * 填充商品详情的View
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月24日
	 *
	 * @param product
	 */
	public static View fillProductView(SaleProduct product, View v, BitmapUtils bitmapUtils) {
		ImageView imageView = (ImageView) v.findViewById(id.goods_img);
		TextView labelView = (TextView) v.findViewById(id.goods_name_show);
		TextView buyCountView = (TextView) v.findViewById(id.goods_sale_num_show);
		TextView priceView = (TextView) v.findViewById(id.goods_price_show);
		TextView olderPriceView = (TextView) v.findViewById(id.goods_old_price_show);

		int lableId[] = new int[] { id.goods_tab_one, id.goods_tab_two, id.goods_tab_three };
		if (product.getLabels() != null && !product.getLabels().isEmpty()) {
			for (int i = 0; i < lableId.length; i++) {
				TextView label = (TextView) v.findViewById(lableId[i]);
				if (i >= product.getLabels().size()) break;
				String tag = product.getLabels().get(i);
				if (i > 0) tag = " · " + tag;
				label.setText(tag);
			}
		}

		// ImageLoader.getInstance().displayImage(TextUtils.isEmpty(product.getPhotos())
		// ? product.getPhoto() : product.getPhotos(), imageView, new
		// ImageLoaderCallbackFactory());

//		bitmapUtils.display(imageView, TextUtils.isEmpty(product.getPhotos()) ? product.getPhoto() : product.getPhotos());
		new AQuery(imageView).image(TextUtils.isEmpty(product.getPhotos()) ? product.getPhoto() : product.getPhotos(), true, true);

		// imageWorker.loadBitmap(imageWorkTag,
		// TextUtils.isEmpty(product.getPhotos()) ? product.getPhoto() :
		// product.getPhotos(), imageView);
		// imageView.setImageUrl(TextUtils.isEmpty(product.getPhotos()) ?
		// product.getPhoto() : product.getPhotos(),
		// drawable.goods_default_img);

		labelView.setText(TextUtils.isEmpty(product.getName()) ? product.getTitle() : product.getName());
		buyCountView.setText((TextUtils.isEmpty(product.getStock_num()) ? product.getBuy_count() : product.getStock_num()) + "人购买");

		priceView.setText("￥" + product.getSell_price());

		String maketPrice = "￥" + product.getMarket_price();
		olderPriceView.setText(ViewUtil.getTextStrikeThroughStyle(maketPrice, 1, maketPrice.length()));

		return v;
	}

}
