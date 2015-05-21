package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.array;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.bean.response.entity.SisterGroupPostVo;
import com.ipassistat.ipa.business.CropImageListener;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.UpdateUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.view.CropImageView;

public class SisterGroupListViewAdapter extends PaginationAdapter<SisterGroupPostVo> {

	static String TAG = "SisterGroupListViewAdapter";
	private CropImageListener mBitmapListener;

	public SisterGroupListViewAdapter(Context context, List<SisterGroupPostVo> data) {
		super(context, data);

		mBitmapListener = new CropImageListener(getContext(), true);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.adapter_sister, parent, false);
			holder.flagGuan = (ImageView) convertView.findViewById(R.id.flag_guan);
			holder.flagJing = (ImageView) convertView.findViewById(R.id.flag_jing);
			holder.flagHuo = (ImageView) convertView.findViewById(R.id.flag_huo);
			// holder.type = (TextView) convertView.findViewById(R.id.type);
			holder.titleView = (TextView) convertView.findViewById(R.id.textview_title);
			holder.nicknameView = (TextView) convertView.findViewById(id.user_name);

			holder.contentView = (TextView) convertView.findViewById(R.id.textview_content);
			holder.lastTime = (TextView) convertView.findViewById(R.id.time_last);
			holder.readcountView = (TextView) convertView.findViewById(R.id.textview_readcount);
			holder.supports = (TextView) convertView.findViewById(R.id.support_count);

			holder.leftImageView = (CropImageView) convertView.findViewById(id.imageview_leftimage);
			holder.rightImageView = (CropImageView) convertView.findViewById(id.imageview_rightimage);

			holder.view_diviver = convertView.findViewById(id.view_divier);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		SisterGroupPostVo item = getItem(position);

		holder.view_diviver.setVisibility(position == getCount() - 1 ? View.GONE : View.VISIBLE);

		// holder.type.setText("[" + item.post_label + "]");
		holder.flagGuan.setVisibility(item.isofficial.equals(ConfigInfo.POST_TAG_OFFICIAL) ? View.VISIBLE : View.GONE);
		holder.flagJing.setVisibility(item.issessence.equals(ConfigInfo.POST_TAG_ISSESSENCE) ? View.VISIBLE : View.GONE);
		holder.flagHuo.setVisibility(item.ishot.equals(ConfigInfo.POST_TAG_HOT) ? View.VISIBLE : View.GONE);

		String type = "[" + item.post_label + "]";
		SpannableString contentText = ViewUtil.getTextColorStyle(type + item.post_title, 0, type.length(), getContext().getResources().getColor(com.ipassistat.ipa.R.color.red_01));
		holder.titleView.setText(contentText);

		holder.nicknameView.setText(" " + item.postPublisherLists.getNickname());

		int iconLeftImage = 0;
		if (item.isofficial.equals(ConfigInfo.POST_TAG_OFFICIAL)) {
			iconLeftImage = drawable.ad_guanfang_iocn;
		} else {
			iconLeftImage = drawable.renmingtubiao;
		}
		holder.nicknameView.setCompoundDrawablesWithIntrinsicBounds(iconLeftImage, 0, 0, 0);

		holder.lastTime.setText(item.publish_time);

		String actionStr[] = getContext().getResources().getStringArray(array.sistergroup_post_action);
		holder.readcountView.setText(item.post_browse + actionStr[0]);
		holder.supports.setText(item.post_count + actionStr[1]);

		holder.contentView.setText(item.post_content);

		if (TextUtils.isEmpty(item.post_img)) {
			holder.contentView.setVisibility(View.VISIBLE);
			holder.leftImageView.setVisibility(View.GONE);
			holder.rightImageView.setVisibility(View.GONE);
		} else {
			holder.contentView.setVisibility(View.GONE);
			holder.leftImageView.setVisibility(View.INVISIBLE);
			holder.rightImageView.setVisibility(View.INVISIBLE);
			String imageUrls[] = UpdateUtil.getImageUrl(item.post_img);

			CropImageView[] imageViews = new CropImageView[] { holder.leftImageView, holder.rightImageView };

			if (imageUrls.length == 0 || imageUrls.length == 1) {
				holder.leftImageView.setVisibility(View.VISIBLE);

				BitmapOptionsFactory.getInstance(getContext()).display(holder.leftImageView, item.post_img, mBitmapListener);

			} else {
				for (int i = 0; i < imageViews.length; i++) {
					imageViews[i].setVisibility(View.VISIBLE);

					BitmapOptionsFactory.getInstance(getContext()).display(imageViews[i], imageUrls[i], mBitmapListener);
				}
			}
		}
		return convertView;
	}

	private class Holder {
		ImageView flagGuan, 
		          flagJing,
		          flagHuo;
		CropImageView leftImageView,
		              rightImageView;
		TextView titleView,
				 nicknameView,
				 contentView, 
				 lastTime,
				 readcountView,
				 supports;

		View view_diviver;
	}

}
