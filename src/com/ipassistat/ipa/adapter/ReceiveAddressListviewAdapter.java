package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ipassistat.ipa.R;
/**
 * 结算中心-收货地址适配器
 * @author wr
 *
 */
public class ReceiveAddressListviewAdapter extends BaseAdapter{
	
	private List receiveAddressList;
	private Context mContext;

	public ReceiveAddressListviewAdapter(Context context, List list) {
		this.mContext = context;
		this.receiveAddressList = list;
	}

	@Override
	public int getCount() {
		return receiveAddressList.size();
	}

	@Override
	public Object getItem(int position) {
		return receiveAddressList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater.inflate(R.layout.adapter_address_receive, null);
		return convertView;
	}

}
