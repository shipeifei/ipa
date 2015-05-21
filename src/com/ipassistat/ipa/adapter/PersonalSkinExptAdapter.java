/*
 * PersonalSkinExptAdapter.java [V 1.0.0]
 * classes : com.ichsy.mboss.adapter.PersonalSkinExptAdapter
 * 时培飞 Create at 2014-11-25 下午1:37:32
 */
package com.ipassistat.ipa.adapter;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.entity.SkinHopeful;

/**
 * com.ichsy.mboss.adapter.PersonalSkinExptAdapter
 * 
 * @author 时培飞
 * @discretion:护肤需求类型列表适配器，实现多选功能 Create at 2014-11-25 下午1:37:32
 */
public class PersonalSkinExptAdapter extends BaseAdapter {
	/** 上下文 */
	private Context mContext;
	/** 布局渲染 */
	private LayoutInflater mInflater;
	/** 护肤需求数据集 */
	private ArrayList<SkinHopeful> mDatas = new ArrayList<SkinHopeful>();
	/** 用户选择的护肤需求类型 */
	private ArrayList<String> mCheckedId = new ArrayList<String>();
	/** 记录护肤类型名称 */
	public ArrayList<String> mSeleceName = new ArrayList<String>();

	public PersonalSkinExptAdapter(Context context,
			ArrayList<SkinHopeful> datas, ArrayList<String> checkedId) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
		mCheckedId = checkedId;
		initCheckedName();
	}

	/***
	 * 初始化用户选择的护肤需求名称
	 * 
	 * @author 时培飞 Create at 2014-12-24 下午2:10:33
	 */
	protected void initCheckedName() {
		if (mCheckedId != null && mCheckedId.size() > 0) {
			for (int i = 0; i < mCheckedId.size(); i++) {
				for (SkinHopeful item : mDatas) {
					if (item != null) {
						if (item.getHopeful_code().equals(
								String.valueOf(mCheckedId.get(i)))) {
							if (mSeleceName.indexOf(item.getHopeful_name()) < 0) {
								mSeleceName.add(item.getHopeful_name());
							}
							
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * @discretion:设置listview选项状态
	 * @author 时培飞 Create at 2014-11-25 下午2:50:33
	 */
	public ArrayList<String> setSelectedItem(String position, String name) {
		// 判断当前选中的选项是否已经选中，如果已经选中设置为没有选中，否则添加到选中的集合里面
		if (mCheckedId.indexOf(position) > -1) {
			// 删除选项
			mCheckedId.remove(mCheckedId.indexOf(position));
			mSeleceName.remove(mSeleceName.indexOf(name));
		} else {
			// 添加选中的选型
			mCheckedId.add(position);
			if (mSeleceName.indexOf(name) < 0) {
				mSeleceName.add(name);
			}
		}
		// 刷新listview列表
		notifyDataSetChanged();
		return mCheckedId;
	}

	@Override
	public int getCount() {
		if (mDatas != null && mDatas.size() > 0) {
			return mDatas.size();
		} else {
			return 0;
		}
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(position==0)
		{
			
		}
		ViewHolder holder = null;
		final SkinHopeful hopeful;
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
		hopeful = mDatas.get(position);

		holder.personal_info_item.setText(hopeful.getHopeful_name());
		// 选中集合里面是否已经包含了选项，已经包含设置选中图标为显示状态，否则设置为隐藏
		if (mCheckedId.indexOf(hopeful.getHopeful_code()) > -1) {
		
			holder.choose.setVisibility(View.VISIBLE);
			holder.choose.setFocusable(false);
		} else {
			holder.choose.setVisibility(View.GONE);
		}
		// 按钮选中事件
		holder.choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				setSelectedItem(hopeful.getHopeful_code(),
						hopeful.getHopeful_name());
			}
		});

		return convertView;
	}

	class ViewHolder {
		/** 选项信息 */
		TextView personal_info_item;
		/** 选项选中图标 */
		Button choose;
	}

}
