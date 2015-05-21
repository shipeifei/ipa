package com.ipassistat.ipa.adapter.viewholder;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.adapter.PaginationAdapter;
import com.ipassistat.ipa.bean.response.entity.ShareInfoEntity;
import com.ipassistat.ipa.bean.response.entity.VideoChannel;
import com.ipassistat.ipa.bean.response.entity.VideoList;
import com.ipassistat.ipa.util.GlobalUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.MathUtil;
import com.ipassistat.ipa.view.PinnedHeaderListView;
import com.ipassistat.ipa.view.PinnedHeaderListView.PinnedHeaderAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 娱乐首页ListView适配器
 * 
 * @Description
 * @author gbr
 * @author lis
 * @date 2015-4-20
 * 
 */
public class RecreationAdapter extends PaginationAdapter<VideoChannel> implements OnScrollListener,
		SectionIndexer, PinnedHeaderAdapter, ListAdapter {
	/** 最大view数 */
	private int maxViewCount = 0;
	/** 类型:数据 */
	private static final int TYPE_ITEM = 0;
	/** 类型:标题 */
	private static final int TYPE_SEPARATOR = 1;
	/** 标题用的TreeSet */
	private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();
	/** 视频频道类 */
	private VideoChannel mVideoChannel;
	/** 视频频道集合 */
	private List<VideoChannel> mDatas = new ArrayList<VideoChannel>();
	/** 每个小item的布局id */
	private int mLayoutIds[] = { id.layout_item_first, id.layout_item_second, id.layout_item_third,
			id.layout_item_forth };
	/** 每个小item的图片id */
	private int mImageViewIds[] = { id.imageview_item_first, id.imageview_item_second,
			id.imageview_item_third, id.imageview_item_forth };
	/** 每个小item的文字id */
	private int mTextViewds[] = { id.textview_item_first, id.textview_item_second,
			id.textview_item_third, id.textview_item_forth };
	/** LinearLayout的集合 用来放入每个小item的布局 */
	private LinearLayout[] mItemLayout = new LinearLayout[4];
	/** ImageView的集合 用来放入每个小item的视频图片 */
	private ImageView[] mItemImage = new ImageView[4];
	/** TextView的集合 用来放入每个小item的视频名称 */
	private TextView[] mTextView = new TextView[4];
	/** 分享内容类 */
	private ShareInfoEntity mShareInfoEntity;
	/** 标题导航头图片集合 */
	private int[] imagBackground = { R.drawable.icon_title_0, R.drawable.icon_title_1,
			R.drawable.icon_title_2 };
	private Context mContext;
	LayoutInflater mInflater;

	public RecreationAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * 添加标题导航
	 * 
	 * @param item
	 */
	public void addSeparatorItem(final VideoChannel item) {
		mDatas.add(item);
		mSeparatorsSet.add(mDatas.size() - 1);
		notifyDataSetChanged();
	}

	/**
	 * 清空数据
	 */
	public void clear() {
		mDatas.clear();
		mSeparatorsSet.clear();
	}

	/**
	 * 添加列表项
	 * 
	 * @param item
	 */
	public void addItem(final VideoChannel item) {
		mDatas.add(item);
		notifyDataSetChanged();
	}

	@Override
	public int getViewTypeCount() {
		return 2; // 两种类型的Item
	}

	@Override
	public int getItemViewType(int position) {
		return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public VideoChannel getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);
		mVideoChannel = mDatas.get(position);
		final List<VideoList> videoList = mVideoChannel.getVideoList();
		if (convertView == null) {
			switch (type) {
			case TYPE_SEPARATOR:
				holder2 = new ViewHolder2();
				convertView = mInflater.inflate(R.layout.adapter_recreation_separator, parent,
						false);
				holder2.layout = (RelativeLayout) convertView
						.findViewById(R.id.layout_recreation_click);
				holder2.separatoriv = (ImageView) convertView
						.findViewById(R.id.imageview_recreation_zhuangshi);
				holder2.separatortv = (TextView) convertView
						.findViewById(R.id.textview_recreation_title);
				holder2.separatorskip = (ImageView) convertView
						.findViewById(R.id.imageview_recreation_skip);
				convertView.setTag(holder2);
				break;
			case TYPE_ITEM:
				holder1 = new ViewHolder1();
				convertView = mInflater.inflate(R.layout.item_recreation_content, parent, false);
				// Title 的布局
				holder1.mTitleLayout = (LinearLayout) convertView
						.findViewById(id.layout_titile_view);
				// Title 的开始播放视频按钮
				holder1.mstartVideoImage = (ImageView) convertView
						.findViewById(id.imageview_start_video);
				holder1.mtitleImage = (ImageView) convertView
						.findViewById(id.imageview_recreation_title);
				// 更新集数
				holder1.mvideoUpdateText = (TextView) convertView
						.findViewById(id.textview_video_update);
				// 更新集数的视频名
				holder1.mvideoNameText = (TextView) convertView
						.findViewById(id.textview_video_name);

				// Content 的布局
				holder1.mContentLayout = (LinearLayout) convertView
						.findViewById(id.layout_content_view);

				holder1.mItemtwoLayout = (LinearLayout) convertView
						.findViewById(id.layout_item_two);
				convertView.setTag(holder1);
				break;
			}
		} else {
			switch (type) {
			case TYPE_SEPARATOR:
				holder2 = (ViewHolder2) convertView.getTag();
				holder2.reset();
				break;
			case TYPE_ITEM:
				holder1 = (ViewHolder1) convertView.getTag();
				break;
			default:
				break;
			}
		}
		if (videoList != null && videoList.size() > 0) {
			switch (type) {
			case TYPE_SEPARATOR:
				holder2.separatortv.setText(mDatas.get(position).getChannel_name());
				holder2.separatoriv.setBackgroundResource(imagBackground[MathUtil
						.getIndexInMovie((position / 2))]);
				if (videoList.size() > 4) {
					holder2.separatorskip.setVisibility(View.VISIBLE);
					holder2.layout.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							MobclickAgent.onEvent(mContext, "1216");
							// 将点击的频道名称传到二级页面
							IntentUtil.startRecreationTvListActivity(mContext, mDatas.get(position));
						}
					});
				} else {
					holder2.separatorskip.setVisibility(View.GONE);
				}

				break;
			case TYPE_ITEM:
				holder1.mContentLayout.setVisibility(View.VISIBLE);
				holder1.mTitleLayout.setVisibility(View.VISIBLE);
				// 每次都需要重新给 maxViewCount 赋值
				if (videoList.size() < 5) {
					maxViewCount = videoList.size();
				} else {
					maxViewCount = 4;
				}
				for (int j = 0; j < maxViewCount; j++) {
					final int fianl_j = j;
					// Content 内的布局和视频缩略图与视频名称
					mItemImage[j] = (ImageView) convertView.findViewById(mImageViewIds[j]);
					mTextView[j] = (TextView) convertView.findViewById(mTextViewds[j]);
					mItemLayout[j] = (LinearLayout) convertView.findViewById(mLayoutIds[j]);
					mTextView[j].setBackgroundResource(R.color.textbackground);
					// Content 内每个布局所做的事件
					mItemLayout[j].setVisibility(View.GONE);
					if (j < 2) {
						mItemLayout[j].setVisibility(View.VISIBLE);
						holder1.mItemtwoLayout.setVisibility(View.GONE);
					} else {
						mItemLayout[j].setVisibility(View.VISIBLE);
						holder1.mItemtwoLayout.setVisibility(View.VISIBLE);
					}

					mItemLayout[j].setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String videoId = videoList.get(fianl_j).getRecreation_name()
									+ videoList.get(fianl_j).getRecreation_updatesum();
							MobclickAgent.onEvent(mContext, "1215", videoId);
							mShareInfoEntity = new ShareInfoEntity();
							mShareInfoEntity.setTitle(mContext
									.getString(string.recreation_video_share_title));
							mShareInfoEntity.setTargetUrl(videoList.get(fianl_j)
									.getRecreation_url());
							mShareInfoEntity.setContent(mContext
									.getString(string.recreation_video_share_content));
							mShareInfoEntity.setSMSContent(mContext
									.getString(string.recreation_video_share_content_msg));
							mShareInfoEntity.setPicUrl(videoList.get(fianl_j).getPicInfos().get(0)
									.getSmallPicInfo().getPicUrl());
							IntentUtil.startWebView(mContext, videoList.get(fianl_j)
									.getRecreation_name(), videoList.get(fianl_j)
									.getRecreation_url(), "1073", mShareInfoEntity);

						}
					});
				}

				if (position == 1) {
					// position = 1 所做的操作
					holder1.mContentLayout.setVisibility(View.GONE);
					holder1.mContentLayout.setEnabled(false);
					if (videoList != null && videoList.size() > 0) {
						// 更新集数
						new AQuery(holder1.mtitleImage).image(videoList.get(0).getPicInfos().get(0)
								.getBigPicInfo().getPicUrl(), true, true,
								GlobalUtil.displayMetrics.widthPixels, drawable.default_goods_img);
						if (videoList.get(0).getRecreation_updatesum() == 0) {
							holder1.mvideoUpdateText.setVisibility(View.GONE);
						} else {
							holder1.mvideoUpdateText.setVisibility(View.VISIBLE);
							holder1.mvideoUpdateText.setText("更新至第"
									+ videoList.get(0).getRecreation_updatesum() + "集");
						}
						// 更新的视频名
						holder1.mvideoNameText.setText(videoList.get(0).getRecreation_name());
						holder1.mTitleLayout.setVisibility(View.VISIBLE);
					} else {
						holder1.mTitleLayout.setVisibility(View.GONE);
					}
					holder1.mstartVideoImage.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String videoId = videoList.get(0).getRecreation_name()
									+ videoList.get(0).getRecreation_updatesum();
							MobclickAgent.onEvent(mContext, "1214", videoId);
							mShareInfoEntity = new ShareInfoEntity();
							mShareInfoEntity.setTitle(mContext
									.getString(string.recreation_video_share_title));
							mShareInfoEntity.setTargetUrl(videoList.get(0).getRecreation_url());
							mShareInfoEntity.setContent(mContext
									.getString(string.recreation_video_share_content));
							mShareInfoEntity.setSMSContent(mContext
									.getString(string.recreation_video_share_content_msg));
							mShareInfoEntity.setPicUrl(videoList.get(0).getPicInfos().get(0)
									.getSmallPicInfo().getPicUrl());
							mShareInfoEntity.setBackCode("1222");
							mShareInfoEntity.setShareCode("1221");
							IntentUtil.startWebView(mContext,
									videoList.get(0).getRecreation_name(), videoList.get(0)
											.getRecreation_url(), "1073", mShareInfoEntity);
						}
					});
				} else {
					// position > 0 的操作
					holder1.mContentLayout.setEnabled(true);
					holder1.mTitleLayout.setVisibility(View.GONE);
					holder1.mContentLayout.setVisibility(View.VISIBLE);

					// 设置每个小item 视频的图片与名字
					for (int i = 0; i < maxViewCount; i++) {

						VideoList videoInfo = videoList.get(i); // 每个视频的信息
						String imageUrl = "";
						if (videoInfo.getPicInfos() != null && videoInfo.getPicInfos().size() > 0) {
							imageUrl = videoInfo.getPicInfos().get(0).getSmallPicInfo().getPicUrl();
						}
						new AQuery(mItemImage[i]).image(imageUrl, true, true,
								GlobalUtil.displayMetrics.widthPixels, drawable.default_goods_img);
						mItemImage[i].setBackgroundResource(drawable.default_goodsdetail_img);
						mTextView[i].setText(videoInfo.getRecreation_name());
					}
				}
				break;
			}
		} else {
			switch (type) {
			case TYPE_SEPARATOR:
				holder2.layout.setVisibility(View.GONE);
				break;
			case TYPE_ITEM:
				holder1.mContentLayout.setVisibility(View.GONE);
				holder1.mTitleLayout.setVisibility(View.GONE);
				break;
			}
		}
		return convertView;
	}

	/**
	 * Item用的ViewHolder
	 * 
	 * @Description
	 * @author lis
	 * @date 2015-3-20
	 * 
	 */
	public class ViewHolder1 {
		/** 重磅部分布局 */
		LinearLayout mTitleLayout;
		/** 视频缩略图 */
		ImageView mtitleImage;
		/** 视频更新集数 */
		TextView mvideoUpdateText;
		/** 视频名称 */
		TextView mvideoNameText;
		/** 播放视频图标 */
		ImageView mstartVideoImage;
		LinearLayout mItemtwoLayout;
		/** content 的holder */
		LinearLayout mContentLayout;
		LinearLayout mItemLayout;
		ImageView mItemImage;
		TextView mItemTextView;

	}

	/**
	 * 可悬浮标题ViewHolder
	 * 
	 * @Description
	 * @author lis
	 * @date 2015-3-20
	 * 
	 */
	class ViewHolder2 {
		RelativeLayout layout;
		ImageView separatoriv;
		TextView separatortv;
		ImageView separatorskip;

		public void reset() {
			separatoriv.setImageDrawable(null);
		}
	}

	/**
	 * 根据位置划分分组
	 */
	@Override
	public int getSectionForPosition(int position) {
		int temp = mSeparatorsSet.floor(position) != null ? mSeparatorsSet.floor(position) : -1;
		return temp;
	}

	public int getNextSectionForPosition(int position) {
		return mSeparatorsSet.ceiling(position) != null ? mSeparatorsSet.ceiling(position) : mDatas
				.size() - 1;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;// 这里没什么别的意思，主要是通讯录中，listview中第一个显示的是所有的联系人，
		// 不是真实的数据，所以会-1,这里偷懒，直接把后面的去掉，还有其他有类似的地方，原因一样
		if (mSeparatorsSet == null) { // section 数组
			return PINNED_HEADER_GONE;
		}
		if (realPosition < 0) {
			return PINNED_HEADER_GONE;
		}
		int section = getSectionForPosition(realPosition);// 得到此item所在的分组位置
		if (section == -1) {
			return PINNED_HEADER_GONE;
		}

		int nextSectionPosition = getNextSectionForPosition(realPosition);// 得到下一个分组的位置
		if (nextSectionPosition != -1 && realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		final int realPosition = position;
		int section = getSectionForPosition(realPosition);
		if (section != -1) {
			int index = MathUtil.getIndexInMovie((position / 2));
			((ImageView) header.findViewById(R.id.imageview_recreation_zhuangshi))
					.setBackgroundResource(imagBackground[index]);
			((TextView) header.findViewById(R.id.textview_recreation_title)).setText(mDatas.get(
					position).getChannel_name());
			ImageView skipiv = ((ImageView) header.findViewById(R.id.imageview_recreation_skip));
			if (mDatas.get(position).getVideoList().size() > 4) {
				skipiv.setVisibility(View.VISIBLE);
				((RelativeLayout) header.findViewById(R.id.layout_recreation_click))
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// 将点击的频道名称传到二级页面
								IntentUtil.startRecreationTvListActivity(mContext,
										mDatas.get(realPosition));
							}
						});
			} else {
				skipiv.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public Object[] getSections() {
		Object[] array = new String[mDatas.size()];
		mDatas.toArray(array);
		return array;
	}

	@Override
	public int getPositionForSection(int section) {
		return section + 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem); // 重新定义
		}
	}

}
