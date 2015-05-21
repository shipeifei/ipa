package com.ipassistat.ipa.adapter;


import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.request.entity.ContactPerson;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactPersonAdapter extends ArrayAdapter<ContactPerson> {
	private final int VIEW_TYPE = 2;
	private LayoutInflater inflater;
	public ContactPersonAdapter(Context context,LayoutInflater inflater) {
		super(context, 0);
		this.inflater=inflater;
	}

	@Override
	public boolean isEnabled(int position) {
		if (getItem(position).getItemType() == 0)// 如果是字母索引
			return false;// 表示不能点击
		return super.isEnabled(position);
	}

	@Override
	public int getItemViewType(int position) { // 重用两个行对象
		return getItem(position).getItemType();
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{

		ContactPerson person = (ContactPerson) getItem(position);
		if (convertView == null) // 如果行对象为空 分别创建行对象
		{
			if (person.getItemType() == 0) 
			{
				convertView = inflater.inflate(
						R.layout.contact_index, parent,false);

			} else if (person.getItemType() == 1)
			{
				convertView = inflater.inflate(R.layout.contact_item, parent,false);
			}
		}

		if (person.getItemType() == 0) 
		{
			initHeader(convertView, person);
		} else
		{
			initContact(convertView, person,position);
		}
		return convertView;
	}
	//快速查找
	private void initHeader(View view, ContactPerson item) 
	{
		TextView name = (TextView) view.findViewById(R.id.indexTv);
		name.setText(item.getName());
	}
	//item
	private void initContact(View view, final ContactPerson item,final int pos) {
		TextView name = (TextView) view.findViewById(R.id.itemTv);
		TextView phone = (TextView) view.findViewById(R.id.itemPhone);
		name.setText(item.getName());
		phone.setText(item.getPhoneNum());
		final ImageView iv = (ImageView) view.findViewById(R.id.tv_instroduce2fri_select);
		if(getItem(pos).isSelected())
		{
			iv.setBackgroundResource(R.drawable.selected);
			iv.setVisibility(View.VISIBLE);//0xdbdbdb
			view.setBackgroundColor(0xdbdbdb);
		}else
		{
			iv.setBackgroundResource(R.drawable.not);
			iv.setVisibility(View.VISIBLE);//0xeaeaea
			view.setBackgroundColor(Color.WHITE);
		}
	}
}
