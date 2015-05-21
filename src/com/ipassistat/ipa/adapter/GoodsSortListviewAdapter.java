package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.entity.Sort;
/**
 * 全部商品选择排序方式适配器
 * @author WanRui
 */
public class GoodsSortListviewAdapter extends BaseAdapter{
	private List<Sort> sortList;
	private LayoutInflater inflater;

	public GoodsSortListviewAdapter(Context context, List<Sort> list) {
		this.sortList = list;
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return sortList.size();
	}

	@Override
	public Object getItem(int position) {
		return sortList.get(position);
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
			convertView = inflater.inflate(R.layout.adapter_goods_sort_listview, null);
			viewHolder.mSortShow = (TextView) convertView.findViewById(R.id.goods_sort_show);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Sort goodssort = (Sort) sortList.get(position);
		String sortname = goodssort.getName();
		viewHolder.mSortShow.setText(sortname);
		return convertView;
	}
	class ViewHolder{
		private TextView mSortShow;
	}

	

}
