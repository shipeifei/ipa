package com.ipassistat.ipa.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.bean.response.entity.PostsList;
import com.ipassistat.ipa.bean.response.entity.ShareInfoEntity;
import com.ipassistat.ipa.bean.response.entity.VideoChannel;
import com.ipassistat.ipa.bean.response.entity.VideoList;
import com.ipassistat.ipa.business.CropImageListener;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.GlobalUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.MathUtil;
import com.ipassistat.ipa.view.CircularImageView;
import com.ipassistat.ipa.view.CropImageView;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页内容的adapter
 * 
 * Package: com.ichsy.mboss.adapter
 * 
 * File: PopPostAdatperNew.java
 * 
 * @author: 任恒 Date: 2015-2-11
 * 
 *          Modifier： Modified Date： Modify：
 * 
 *          Copyright @ 2015 Corpration Name
 * 
 */
public class HomeAdatper extends BaseExpandableListAdapter {

	private List<VideoChannel> mDatas;
	private Context mContext;
	private LayoutInflater mInflater;
	private LayoutParams mLayoutParams;

	private AQuery mAquery;

	public HomeAdatper(Context context, List list) {
		this.mDatas = list;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		mLayoutParams = getLayoutParams(context);
	}

	private LayoutParams getLayoutParams(Context context) {
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int height = (int) (width * 0.519);
		LayoutParams layoutParams = new LayoutParams(width, height);
		return layoutParams;
	}

	@Override
	public int getGroupCount() {
		return mDatas.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		VideoChannel channel = mDatas.get(groupPosition);
		// 如果一个列表没有数据，就返回另一个列表
		return channel.getVideoList().size() == 0 ? channel.getPosts().size()
				: channel.getVideoList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mDatas.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		VideoChannel channel = mDatas.get(groupPosition);
		return channel.getVideoList().size() == 0 ? channel.getPosts().get(
				childPosition) : channel.getVideoList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.item_home_channel, null);
		convertView.setClickable(true); // 设置分组不可点击
		TextView channelName = (TextView) convertView
				.findViewById(R.id.channel_name);
		channelName.setText(mDatas.get(groupPosition).getChannel_name());
		int drawableId = 0;
		switch (MathUtil.getIndexInMovie(groupPosition)) {
		case 0:
			drawableId = R.drawable.icon_title_0;
			break;
		case 1:
			drawableId = R.drawable.icon_title_1;
			break;
		case 2:
			drawableId = R.drawable.icon_title_2;
			break;
		default:
			break;
		}
		Drawable drawable = mContext.getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		channelName.setCompoundDrawables(drawable, null, null, null);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_home, null);
			holder = new ViewHolder();
			holder.movieLayout = (LinearLayout) convertView
					.findViewById(R.id.layout_movie);
			holder.popLayout = (LinearLayout) convertView
					.findViewById(R.id.layout_pop);

			holder.moviePic = (ImageView) holder.movieLayout
					.findViewById(R.id.movie_pic);
			holder.movieName = (TextView) holder.movieLayout
					.findViewById(R.id.movie_name);
			holder.movieInfo = (TextView) holder.movieLayout
					.findViewById(R.id.movie_info);

			holder.titleTextView = (TextView) holder.popLayout
					.findViewById(R.id.title);
			holder.post_publisher = (TextView) holder.popLayout
					.findViewById(R.id.user_name);
			/*holder.scanTextView = (TextView) holder.popLayout
					.findViewById(R.id.scan_tv);
			holder.replyTextView = (TextView) holder.popLayout
					.findViewById(R.id.reply_tv);
			holder.imageView = (CropImageView) holder.popLayout
					.findViewById(R.id.post_pic);
			holder.userHeadImgv = (CircularImageView) holder.popLayout
					.findViewById(R.id.user_head);*/
			//holder.imageView.setLayoutParams(mLayoutParams);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mDatas.get(groupPosition).getPosts().size() == 0) {
			// 加载视频
			final VideoList video = (VideoList) getChild(groupPosition,
					childPosition);
			holder.movieLayout.setVisibility(View.VISIBLE);
			holder.popLayout.setVisibility(View.GONE);
			if(video.getRecreation_updatesum()==0){
				holder.movieName.setVisibility(View.GONE);
			}else{
				holder.movieName.setVisibility(View.VISIBLE);
				holder.movieName.setText("更新至" + video.getRecreation_updatesum()
						+ "集");
			}
			holder.movieInfo.setText(video.getRecreation_name());
			// BitmapOptionsFactory.getInstance(mContext).display(holder.moviePic,
			// video.getPicInfos().get(0).getBigPicInfo().getPicUrl(),
			// BitmapOptionsFactory.getOptionConfig(mContext,
			// drawable.pop_post_bg_default));
			mAquery = new AQuery(holder.moviePic);
			mAquery.image(video.getPicInfos().get(0).getBigPicInfo()
					.getPicUrl(), true, true,
					GlobalUtil.displayMetrics.widthPixels,
					drawable.personal_photo);
			holder.movieLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ShareInfoEntity shareInfoEntity = new ShareInfoEntity();
					shareInfoEntity.setTitle(mContext
							.getString(string.recreation_video_share_title));
					shareInfoEntity.setTargetUrl(video.getRecreation_url());
					shareInfoEntity.setContent(mContext
							.getString(string.recreation_video_share_content));
					shareInfoEntity.setSMSContent(mContext
							.getString(string.recreation_video_share_content_msg));
					shareInfoEntity.setPicUrl(video.getPicInfos().get(0)
							.getSmallPicInfo().getPicUrl());
					IntentUtil.startWebView(mContext,
							video.getRecreation_name(),
							video.getRecreation_url(), null, shareInfoEntity);
				}
			});
		} else {
			// 加载帖子
			final PostsList post = (PostsList) getChild(groupPosition,
					childPosition);
			holder.movieLayout.setVisibility(View.GONE);
			holder.popLayout.setVisibility(View.VISIBLE);
			holder.titleTextView.setText(post.getPost_title());
			holder.post_publisher.setText(post.getPostPublisherLists()
					.getNickname());
			holder.scanTextView.setText(post.getPost_browse() + "阅览");
			holder.replyTextView.setText(post.getPost_count() + "回复");
			if (post.getPost_img() != null && post.getPost_img().contains("|")) {
				String[] array = post.getPost_img().split("\\|");
				post.setPost_img(array[0]);
			}

			String imgUrl = "";
			if (post.getPicInfos().size() > 0) {
				imgUrl = post.getPicInfos().get(0).getBigPicInfo().getPicUrl();
			} else {
				imgUrl = "";
			}

			// BitmapOptionsFactory.getInstance(mContext).display(holder.imageView,
			// imgUrl, BitmapOptionsFactory.getOptionConfig(mContext,
			// drawable.pop_post_bg_default));

			BitmapOptionsFactory.getInstance(mContext).display(
					holder.userHeadImgv,
					post.getPostPublisherLists().getMember_avatar(),
					BitmapOptionsFactory.getOptionConfig(mContext,
							drawable.personal_photo), new CropImageListener());

			mAquery = new AQuery(holder.imageView);
			mAquery.image(imgUrl, true, true,
					GlobalUtil.displayMetrics.widthPixels,
					drawable.pop_post_bg_default);

			// mAquery = new AQuery(holder.userHeadImgv);
			// mAquery.image(post.getPostPublisherLists().getMember_avatar(),
			// true, true, GlobalUtil.displayMetrics.widthPixels,
			// drawable.personal_photo);

			holder.popLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(mContext, "1204", post.getPost_code()); 
					IntentUtil.startSisterGroupDetail(mContext,
							post.getPost_code());
				}
			});
		}

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	class ViewHolder {
		public LinearLayout movieLayout;
		public LinearLayout popLayout;

		public ImageView moviePic;
		public TextView movieName;
		public TextView movieInfo;

		public TextView titleTextView;
		public TextView post_publisher;
		public TextView scanTextView;
		public TextView replyTextView;
		public CropImageView imageView;
		public CircularImageView userHeadImgv;
	}
}
