package com.ipassistat.ipa.util.share.ichsy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.util.share.ichsy.model.SharePlatform;

/**
 * 分享UI的adapter
 * 
 * @author LiuYuHang
 * @date 2014年9月22日
 *
 */
public class ShareAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<SharePlatform> mPlatforms;

	public ShareAdapter(Context context, List<SharePlatform> platforms) {
		mInflater = LayoutInflater.from(context);
		mPlatforms = platforms;

	}

	@Override
	public int getCount() {
		return mPlatforms == null ? 0 : mPlatforms.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(layout.adapter_item_share, null);
			holder.iconView = (ImageView) convertView.findViewById(id.imageview_icon);
			holder.contentView = (TextView) convertView.findViewById(id.textview_content);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		SharePlatform item = mPlatforms.get(position);
		holder.iconView.setImageResource(item.icon);
		holder.contentView.setText(item.title);

		return convertView;
	}

	private class Holder {
		ImageView iconView;
		TextView contentView;
	}
}
