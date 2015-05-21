package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;
import com.ipassistat.ipa.view.CropImageView;

/**
 * 全部商品列表适配器
 * 
 * @author WanRui
 */
public class GoodsListviewAdapter extends PaginationAdapter<SaleProduct> {
	static String TAG = "GoodsListviewAdapter";
	private int mArrowVisibility = View.GONE; // 设置箭头是否显示
	private LayoutInflater inflater;
	// private CropImageListener mBitmapListener;
	// private ImageLoader mImageLoader;
	// private ImageLoaderCallbackFactory imageLoaderCallback;
	private AQuery aQuery;

	public GoodsListviewAdapter(Context context, List<SaleProduct> list) {
		super(context, list);
		inflater = LayoutInflater.from(context);
		// mBitmapListener = new CropImageListener();
		// mImageLoader = ImageLoader.getInstance();
		// imageLoaderCallback = new ImageLoaderCallbackFactory();
		aQuery = new AQuery(getContext());
	}

	/**
	 * 我的收藏试用的构造方法，显示右面的小箭头
	 * 
	 * @param context
	 * @param list
	 *            数据
	 * @param Visibility
	 *            右面小箭头的显示方式
	 */
	public GoodsListviewAdapter(Context context, List<SaleProduct> list, int visibility) {
		this(context, list);
		mArrowVisibility = visibility;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_goods_listview, parent, false);
			viewHolder.mGoodsImg = (CropImageView) convertView.findViewById(R.id.goods_img);// 商品图片
			viewHolder.mGoodsName = (TextView) convertView.findViewById(R.id.goods_name_show);// 商品名称
			viewHolder.mGoodsPrice = (TextView) convertView.findViewById(R.id.goods_price_show);// 商品现价
			viewHolder.mGoodsOldPrice = (TextView) convertView.findViewById(R.id.goods_old_price_show);// 商品原价
			viewHolder.mGoodsSaleNum = (TextView) convertView.findViewById(R.id.goods_sale_num_show);// 商品购买数
			viewHolder.mGoodsTabOne = (TextView) convertView.findViewById(R.id.goods_tab_one);// 商品标签一
			viewHolder.mGoodsTabTwo = (TextView) convertView.findViewById(R.id.goods_tab_two);// 商品标签二
			viewHolder.mGoodsTabThree = (TextView) convertView.findViewById(R.id.goods_tab_three);// 商品标签三
			viewHolder.mGoodsArrow = (ImageView) convertView.findViewById(R.id.goods_arrow);// 右侧小箭头
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mGoodsArrow.setVisibility(mArrowVisibility); // 设置箭头是否可见
		final SaleProduct goods = getItem(position);
		String imageurl = goods.getPhoto();
		if (imageurl != null) {
			// BitmapOptionsFactory.getInstance(getContext()).display(
			// viewHolder.mGoodsImg, imageurl,
			// BitmapOptionsFactory.getOptionConfig(getContext(),R.drawable.goods_default_noborder),
			// mBitmapListener);
			// mImageLoader.displayImage(imageurl, viewHolder.mGoodsImg,
			// BitmapOptionsFactory.getImageOption(drawable.goods_default_noborder),imageLoaderCallback);
			aQuery.id(viewHolder.mGoodsImg).image(imageurl, true, true, 0, drawable.default_goods_noborder);
		}
		viewHolder.mGoodsName.setText(goods.getTitle());// 商品名称
		viewHolder.mGoodsPrice.setText("￥" + goods.getSell_price());// 商品现价
		viewHolder.mGoodsOldPrice.setText("￥" + goods.getMarket_price());// 商品原价
		viewHolder.mGoodsOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);// 设置原件上面的横线
		viewHolder.mGoodsSaleNum.setText(goods.getBuy_count() + "人购买");// 商品购买数
		List<String> lableslist = goods.getLabels();
		if (lableslist != null && lableslist.size() == 1) {
			viewHolder.mGoodsTabOne.setText(lableslist.get(0));// 商品标签一
			viewHolder.mGoodsTabTwo.setText(null);// 商品标签二
			viewHolder.mGoodsTabThree.setText(null);// 商品标签三
		} else if (lableslist != null && lableslist.size() == 2) {
			viewHolder.mGoodsTabOne.setText(lableslist.get(0) + " · ");// 商品标签一
			viewHolder.mGoodsTabTwo.setText(lableslist.get(1));// 商品标签二
			viewHolder.mGoodsTabThree.setText(null);// 商品标签三
		} else if (lableslist != null && lableslist.size() >= 3) {
			viewHolder.mGoodsTabOne.setText(lableslist.get(0) + " · ");// 商品标签一
			viewHolder.mGoodsTabTwo.setText(lableslist.get(1) + " · ");// 商品标签二
			viewHolder.mGoodsTabThree.setText(lableslist.get(2));// 商品标签三
		}
		return convertView;
	}

	class ViewHolder {
		private CropImageView mGoodsImg;
		private ImageView mGoodsArrow;
		private TextView mGoodsName, mGoodsPrice, mGoodsOldPrice, mGoodsSaleNum, mGoodsTabOne, mGoodsTabTwo, mGoodsTabThree;
	}
}
