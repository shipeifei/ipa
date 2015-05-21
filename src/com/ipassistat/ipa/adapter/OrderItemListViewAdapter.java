package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipassistat.ipa.R;

/**
 * 订单列表里每个订单的listview的adapter
 * @author renheng
 *
 */
public class OrderItemListViewAdapter extends BaseAdapter {

	private Context context;
	private List list;
	private LayoutInflater inflater;
	
	public OrderItemListViewAdapter(Context context, List list) {
		this.context = context;
		this.list = list;
		 inflater = LayoutInflater.from(context);
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
	public View getView(int position, View view, ViewGroup parent) {
		
		view = inflater.inflate(R.layout.item_order_good, null);
		OrderItemViewHolder holder=null;
		if(view==null){
			holder=new OrderItemViewHolder();
			holder.thumb=(ImageView) view.findViewById(R.id.thumb);
			holder.count=(TextView) view.findViewById(R.id.count);
			holder.name=(TextView) view.findViewById(R.id.name);
			holder.price=(TextView) view.findViewById(R.id.price);
			view.setTag(holder);
		}else{
			holder=(OrderItemViewHolder) view.getTag();
		}
		return view;
	}
	
	class OrderItemViewHolder{
		ImageView thumb;
		TextView name;
		TextView count;
		TextView price;
	}

}
