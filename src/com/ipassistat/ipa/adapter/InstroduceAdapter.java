package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.adapter.viewholder.InstroduceViewHolder;
import com.ipassistat.ipa.bean.request.entity.ContactPerson;
import com.ipassistat.ipa.util.StringUtil;


public class InstroduceAdapter extends BaseAdapter {

	List<ContactPerson> lv;
	Context context;
	LayoutInflater inflater;
	public InstroduceAdapter(List<ContactPerson>lv, Context context,LayoutInflater inflater) {
		// TODO Auto-generated constructor stub
		this.lv = lv;
		this.context = context;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		
		return lv.size();
	}

	@Override
	public Object getItem(int position) {
		
		InstroduceViewHolder holder;
		View convertView = LayoutInflater.from(context).inflate(R.layout.item_instroduce,null);
		holder = new InstroduceViewHolder();

		
		holder.cb = (ImageView) convertView.findViewById(R.id.iv_item_instroduce_checkbox);
		holder.name = (TextView) convertView.findViewById(R.id.tv_item_instroduce_name);
		holder.num = (TextView) convertView.findViewById(R.id.tv_item_instroduce_num);
		holder.status = (TextView) convertView.findViewById(R.id.tv_item_instroduce_status);
		convertView.setTag(holder);

		return holder;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		InstroduceViewHolder holder;
		if (convertView == null) {
			convertView = inflater.from(context).inflate(R.layout.item_instroduce,null);
			holder = new InstroduceViewHolder();
			holder.cb = (ImageView) convertView.findViewById(R.id.iv_item_instroduce_checkbox);
			holder.name = (TextView) convertView.findViewById(R.id.tv_item_instroduce_name);
			holder.num = (TextView) convertView.findViewById(R.id.tv_item_instroduce_num);
			holder.status = (TextView) convertView.findViewById(R.id.tv_item_instroduce_status);
			holder.background = (RelativeLayout) convertView.findViewById(R.id.rl_item_instroduce);
			convertView.setTag(holder);

		} else {
			holder = (InstroduceViewHolder) convertView.getTag();
			holder.name.setVisibility(View.VISIBLE);
			holder.status.setVisibility(View.INVISIBLE);
		}

	/*	
	 * holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				if (isChecked) {
					lv.get(index).setSelected(true);
				} else {
					lv.get(index).setSelected(false);
				}
			}
		});
	*/
		//设置复选框图片的选中状
		if(lv.get(position).isSelected())
		{
			holder.cb.setBackgroundResource(R.drawable.selected);
			convertView.setBackgroundColor(0xeaeaea);
		}else{
			holder.cb.setBackgroundResource(R.drawable.not);
			convertView.setBackgroundColor(Color.WHITE);
		}
		
		holder.name.setText(lv.get(position).getName().trim());
		holder.num.setText(lv.get(position).getPhoneNum());
		if(StringUtil.isPhoneNo(lv.get(position).getName().trim()))
			{
			holder.name.setVisibility(View.GONE);
			};
		//设置是否推荐的文本框的显隐状态̬
		if (lv.get(position).isHavesend()) {
			holder.status.setVisibility(View.VISIBLE);
			if (lv.get(position).isSuccess()) {
				
				holder.status.setTextColor(context.getResources().getColor(R.color.success));
				holder.status.setText("已发送!");
			}else{
				holder.status.setText("发送失败!");
				holder.status.setTextColor(context.getResources().getColor(R.color.failure));
			}
		} else {
			holder.status.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

}
