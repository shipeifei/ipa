package com.ipassistat.ipa.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ipassistat.ipa.R;

/**
 * 我的试用适配器
 * 
 * @author maoyn
 * 
 */
public class PersonalInfoChooseAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<String> mDatas = new ArrayList<String>();
	private int mCheckedId;

	public PersonalInfoChooseAdapter(Context context, List<String> datas,
			int checkedId) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
		mCheckedId = checkedId;
	}

	public void setSelectedItem(int position) {
		mCheckedId = position;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.adapter_personal_info_cho,
					parent, false);
			holder.personal_info_item = (TextView) convertView
					.findViewById(R.id.personal_info_item);
			holder.choose = (Button) convertView.findViewById(R.id.choose);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.personal_info_item.setText(mDatas.get(position));
		if (position == mCheckedId) {
			holder.choose.setVisibility(View.VISIBLE);
			
		} else {
			holder.choose.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		TextView personal_info_item;
		Button choose;
	}

}
