package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.response.entity.Activity;
import com.ipassistat.ipa.util.BitmapOptionsFactory;

/**
 * 官方活动页面的适配器
 * 
 * @author renheng
 * 
 */
public class OfficialAdapter extends PaginationAdapter<Activity> {

	public OfficialAdapter(Context context, List<Activity> data) {
		super(context, data);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = getInflater().inflate(R.layout.adapter_official, parent, false);
			holder.official_image = (ImageView) convertView.findViewById(R.id.official_image);
			holder.official_title = (TextView) convertView.findViewById(R.id.official_title);
			holder.official_time = (TextView) convertView.findViewById(R.id.official_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Activity activity = getItem(position);
		holder.official_title.setText(activity.getName());
		holder.official_time.setText(getTime(activity.getStart_time(), activity.getEnd_time()));
		BitmapOptionsFactory.getInstance(getContext()).display(holder.official_image,
				activity.getPhoto());
		return convertView;
	}

	class ViewHolder {
		ImageView official_image;
		TextView official_title;
		TextView official_time;
	}

	private String getTime(String startTime, String endTime) {
		String str = "活动日期: " + formatTime(startTime) + "至" + formatTime(endTime);
		return str;
	}

	private String formatTime(String time) {
		String str = time.substring(0, 10);
		return str;
	}
}
