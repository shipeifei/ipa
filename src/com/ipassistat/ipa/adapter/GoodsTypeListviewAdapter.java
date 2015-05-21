package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.entity.ProductCategory;
/**
 * 全部商品选择分类适配器
 * @author WanRui
 */
public class GoodsTypeListviewAdapter extends BaseAdapter{
	private List<ProductCategory> typeList;
	private LayoutInflater inflater;
	private Context mContext;
	
	private int mSelectPosition=-1;

	public GoodsTypeListviewAdapter(Context context, List<ProductCategory> list) {
		this.typeList = list;
		this.mContext = context;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return typeList.size();
	}

	@Override
	public Object getItem(int position) {
		return typeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_goods_type_listview, null);
			viewHolder.mTypeShow = (TextView) convertView.findViewById(R.id.goods_type_show);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(mSelectPosition==position){
			viewHolder.mTypeShow.setTextColor(mContext.getResources().getColor(R.color.global_main_text_color));
		}else{
			viewHolder.mTypeShow.setTextColor(mContext.getResources().getColor(R.color.goods_type_unclick));
		}
		ProductCategory goodstype = (ProductCategory) typeList.get(position);
		String typename = goodstype.getName();
		viewHolder.mTypeShow.setText(typename);		
		return convertView;
	}
	class ViewHolder{
		private TextView mTypeShow;
	}
	
	public void setSelectPosition(int position){
		this.mSelectPosition = position;
		notifyDataSetInvalidated();
	}
}
