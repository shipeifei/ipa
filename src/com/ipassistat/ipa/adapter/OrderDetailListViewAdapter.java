package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ipassistat.ipa.R;

/**
 * 订单详情里的商品明细的adapter,同OrderItemListViewAdapter
 * @author renheng
 *
 */
public class OrderDetailListViewAdapter extends BaseAdapter {

	private List list;
	private Context context;
	
	public OrderDetailListViewAdapter(Context context, List list) {
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.item_order_good, null);

		return convertView;
	}

}
