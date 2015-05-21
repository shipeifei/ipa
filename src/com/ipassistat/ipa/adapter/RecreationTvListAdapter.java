/**
 * 
 */
package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.bean.response.entity.VideoList;
import com.ipassistat.ipa.util.GlobalUtil;

/**
 * @author wrj 视频列表适配
 * @author lis
 */
public class RecreationTvListAdapter extends PaginationAdapter<VideoList> {
	/** 获取的数据集 */
	private VideoList mVideoList;

	public RecreationTvListAdapter(Context context, List<VideoList> videoLists) {
		super(context, videoLists);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		mVideoList = getItem(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = getInflater().inflate(R.layout.adapter_wonderfultidbits_item, null);
			holder.mphoto = (ImageView) convertView.findViewById(R.id.iv_media_id);
			holder.mchannename = (TextView) convertView.findViewById(R.id.tv_media_name);
			holder.mplayhour = (TextView) convertView.findViewById(R.id.tv_play_hour);
			holder.mplaytime = (TextView) convertView.findViewById(R.id.tv_play_time);
			holder.mplaytime.setVisibility(View.GONE);
			holder.mupdatetime = (TextView) convertView.findViewById(R.id.tv_update_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String imageUrl = "";
		if (mVideoList.getPicInfos() != null && mVideoList.getPicInfos().size() > 0) {
			imageUrl = mVideoList.getPicInfos().get(0).getSmallPicInfo().getPicUrl();
		}

		new AQuery(holder.mphoto).image(imageUrl, true, true,
				GlobalUtil.displayMetrics.widthPixels, drawable.default_goods_img);

		holder.mchannename.setText(mVideoList.getRecreation_name());
		holder.mplaytime.setText("第" + "1" + mVideoList.getRecreation_updatesum() + "次播放");
		holder.mupdatetime.setText(mVideoList.getRecreation_updatetime() + "更新");
		if (mVideoList.getPlaying_time() != null && !"".equals(mVideoList.getPlaying_time())) {
			holder.mplayhour.setText(mVideoList.getPlaying_time());
			holder.mplayhour.setVisibility(View.VISIBLE);
		} else {
			holder.mplayhour.setVisibility(View.GONE);
		}
		return convertView;

	}

	class ViewHolder {
		public TextView mchannename;
		ImageView mphoto;
		public TextView mplayhour;
		public TextView mplaytime;
		public TextView mupdatetime;
	}
}
