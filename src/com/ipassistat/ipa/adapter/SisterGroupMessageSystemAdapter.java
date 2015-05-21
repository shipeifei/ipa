package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.array;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.bean.response.entity.SystemMessageVo;

public class SisterGroupMessageSystemAdapter extends PaginationAdapter<SystemMessageVo> {

	public SisterGroupMessageSystemAdapter(Context context, List<SystemMessageVo> data) {
		super(context, data);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = getInflater().inflate(layout.adapter_message_system, null);

			holder.readStateView = convertView.findViewById(id.view_readstate);
			holder.typeView = (TextView) convertView.findViewById(id.textview_type);

			holder.content = (TextView) convertView.findViewById(R.id.textview_content);
			holder.time = (TextView) convertView.findViewById(R.id.textview_time);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		SystemMessageVo item = getItem(position);

		String messageStr[] = getContext().getResources().getStringArray(array.sistergroup_message_type);
		if (item.message_type.equals(SystemMessageVo.MESSAGE_TYPE_ALERT)) {
			holder.typeView.setText(messageStr[0]);
		} else if (item.message_type.equals(SystemMessageVo.MESSAGE_TYPE_EDITER)) {
			holder.typeView.setText(messageStr[1]);
		} else {
			holder.typeView.setText(messageStr[2]);
		}

		holder.readStateView.setVisibility(item.is_read == SystemMessageVo.TYPE_NO_READ ? View.VISIBLE : View.INVISIBLE);

		holder.content.setText(item.message_info);
		holder.time.setText(item.send_time);

		return convertView;
	}

	private class Holder {
		View readStateView;
		TextView typeView, content, time;
	}
}
