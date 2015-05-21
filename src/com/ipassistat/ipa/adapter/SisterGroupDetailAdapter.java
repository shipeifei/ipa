package com.ipassistat.ipa.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.array;
import com.ipassistat.ipa.R.color;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.bean.response.entity.ImageInfo;
import com.ipassistat.ipa.bean.response.entity.ImageInfoVo;
import com.ipassistat.ipa.bean.response.entity.MemberInfo;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;
import com.ipassistat.ipa.bean.response.entity.SisterGroupCommentVo;
import com.ipassistat.ipa.bean.response.entity.SisterGroupDetailVo;
import com.ipassistat.ipa.bean.response.entity.SisterGroupPostVo;
import com.ipassistat.ipa.bean.response.entity.SisterGroupPostVoExt;
import com.ipassistat.ipa.bean.response.entity.UserInfo;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.util.BitmapOptionsFactory;
import com.ipassistat.ipa.util.GlobalUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.ReportUtil;
import com.ipassistat.ipa.util.UpdateUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.view.CircularImageView;
import com.ipassistat.ipa.view.CropImageView;
import com.ipassistat.ipa.view.ViewFiller;
import com.umeng.analytics.MobclickAgent;

/**
 * 姐妹圈详情页的adapter
 * 
 * .. 详情页分4部分：
 * 
 * 1：顶部的发帖人，包括标题，总阅读和回复数<br>
 * 2：带图片的帖子内容，包括时间，赞，收藏等（个数不定）<br>
 * 3：评论，可以加载更多(个数不定)<br>
 * 
 * @author LiuYuHang
 * 
 */
public class SisterGroupDetailAdapter extends PaginationAdapter<SisterGroupDetailVo> {
	private final int INTRO_MAX_IMAGE = 9;// 正文每个ITEM最多显示的图片数量
	private boolean isOfficial = false;

	private String mFloorCode;// 楼主code

	private int mMeasuredWidth = 0;

	private AQuery mAquery;

	// private float mImageScale;

	public SisterGroupDetailAdapter(Context pContext, List<SisterGroupDetailVo> data) {
		super(pContext, data);
		mAquery = new AQuery(getContext());
	}

	/**
	 * 设置帖子是否是官方贴
	 * 
	 * @param isOfficial
	 * @author LiuYuHang
	 * @date 2014年10月29日
	 */
	public void setIsOfficial(boolean isOfficial) {
		this.isOfficial = isOfficial;
	}

	public void setFloorCode(String code) {
		mFloorCode = code;
	}

	/**
	 * 获取第一个评论的position
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月25日
	 * 
	 * @return
	 */
	private int getFisrtCommentPosition() {
		for (int i = 0; i < getData().size(); i++) {
			SisterGroupDetailVo item = getData().get(i);
			if (item.sectionType == SisterGroupDetailVo.TYPE_COMMENT) {
				return i;
			}
		}
		return getData().size();
	}

	/**
	 * 追贴，添加一个本地假数据
	 * 
	 * @author LiuYuHang
	 * @date 2014年10月13日
	 */
	public void fillLocalWithIntro(String content, SaleProduct product, ImageInfo[] picInfos) {
		SisterGroupDetailVo vo = new SisterGroupDetailVo();
		vo.sectionType = SisterGroupDetailVo.TYPE_INTRO;

		vo.section = new SisterGroupPostVoExt();
		vo.section.post_code = "-1";
		vo.section.publish_time = "1秒前";
		vo.section.post_content = content;
		vo.section.productinfo = product;
		// vo.section.post_img = images;
		vo.section.picInfos = picInfos;

		getData().add(getFisrtCommentPosition() - 1, vo);
		notifyDataSetChanged();
	}

	/**
	 * 评论，对评论回复之后，添加一个本地假数据
	 * 
	 * @param content
	 * @param postVoExt
	 * @param commentType
	 * @author LiuYuHang
	 * @date 2014年10月13日
	 */
	public void fileLocalWithComment(String content, SisterGroupPostVoExt postVoExt, String commentType, SaleProduct product, ImageInfo[] picInfos) {
		SisterGroupDetailVo vo = new SisterGroupDetailVo();
		vo.sectionType = SisterGroupDetailVo.TYPE_COMMENT;

		vo.section = new SisterGroupPostVoExt();
		SisterGroupPostVoExt commentVo = (SisterGroupPostVoExt) vo.section;
		commentVo.native_comment = new SisterGroupCommentVo();
		commentVo.native_comment.post_content = content;
		commentVo.native_comment.publish_time = "1秒前";

		// 0:针对帖子的评论；1：针对帖子评论的评论
		commentVo.native_comment.type = commentType;

		commentVo.native_comment.postPublisherList = new UserInfo();
		// 增加判断本地缓存数据是否为空的判断，防止系统闪退
		MemberInfo memberInfo = UserModule.getMemberInfo(getContext());
		if (memberInfo != null) {
			// 昵称
			if (!TextUtils.isEmpty(UserModule.getMemberInfo(getContext()).getNickname())) {
				commentVo.native_comment.postPublisherList.setNickname(UserModule.getMemberInfo(getContext()).getNickname());
			}
			// // 护肤类型
			// if (!TextUtils.isEmpty(UserModule.getMemberInfo(getContext(),
			// UserModule.getUserToken(getContext())).getSkin_type())) {
			// commentVo.native_comment.postPublisherList.setSkin_type(UserModule.getMemberInfo(getContext(),
			// UserModule.getUserToken(getContext())).getSkin_type());
			// }
		}
		//commentVo.native_comment.postPublisherList.setMember_avatar((UserModule.getMemberInfo(getContext()).getAvatar().getThumb()));
		commentVo.native_comment.postPublisherList.setMember_avatar((UserModule.getMemberInfo(getContext()).getPhoto()));

		commentVo.native_comment.postPublisherList.setMember_code(UserModule.getMemberInfo(getContext()).getMember_code());

		commentVo.native_comment.postPublisherList.setMember_avatar((UserModule.getMemberInfo(getContext()).getPhoto()));

		if (commentVo.native_comment.type.equals("1")) {// 评论
			commentVo.native_comment.publishedList = postVoExt.native_comment.postPublisherList;
		}

		boolean hasComment = hasComment();// 判断现在是否有评论楼层

		commentVo.native_comment.comment_floor = hasComment ? ((SisterGroupPostVoExt) getData().get(getFisrtCommentPosition()).section).native_comment.comment_floor + 1 : 1;

		commentVo.native_comment.ispraise = ConfigInfo.ACTION_NO;
		commentVo.native_comment.post_praise = "0";

		commentVo.productinfo = product;
		// commentVo.post_img = images;
		commentVo.picInfos = picInfos;

		// if (hasComment) {
		// getData().add(getFisrtCommentPosition(), vo);
		// } else {
		// SisterGroupDetailVo commentSection = new SisterGroupDetailVo();
		// commentSection.sectionType = SisterGroupDetailVo.TYPE_COMMENT_HEAD;
		// getData().add(commentSection);
		//
		// getData().add(vo);
		// }

		getData().add(getFisrtCommentPosition(), vo);

		notifyDataSetChanged();
	}

	/**
	 * 判断现在是否有评论楼层
	 * 
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月13日
	 */
	public boolean hasComment() {
		return getFisrtCommentPosition() < getData().size();
	}

	@Override
	public int getItemViewType(int position) {
		return getData().get(position).sectionType;
	}

	@Override
	public int getViewTypeCount() {
		return 4 + 1;// pulllistview好像必须加1
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LogUtil.outLogDetail("getview position:" + position);
		UserHolder userHolder = null;
		IntroHolder introHolder = null;
		SectionHolder productHolder = null;
		CommentHolder commentHolder = null;
		SisterGroupPostVo item = ((SisterGroupDetailVo) getItem(position)).section;

		int itemType = getItemViewType(position);
		if (convertView == null) {
			switch (itemType) {
			case SisterGroupDetailVo.TYPE_USERINFO:
				userHolder = new UserHolder();
				convertView = inflater.inflate(R.layout.adapter_sistergroup_detail_userinfo, parent, false);

				userHolder.iconView = (CircularImageView) convertView.findViewById(id.imageview_icon);
				userHolder.skinView = (ImageView) convertView.findViewById(id.imageview_skin);
				userHolder.nicknameView = (TextView) convertView.findViewById(id.textview_nickname);
				userHolder.reportView = (TextView) convertView.findViewById(id.textview_report);

				convertView.setTag(userHolder);
				break;
			case SisterGroupDetailVo.TYPE_INTRO:
				introHolder = new IntroHolder();
				convertView = inflater.inflate(R.layout.adapter_sistergroup_detail_introduction, parent, false);

				introHolder.titleView = convertView.findViewById(id.view_title);
				introHolder.titleTextView = (TextView) convertView.findViewById(id.textview_title);
				introHolder.readNumView = (TextView) convertView.findViewById(id.textview_readcount);
				introHolder.replyNumView = (TextView) convertView.findViewById(id.textview_replycount);
				// 点赞数量
				introHolder.replyupcount = (TextView) convertView.findViewById(R.id.textview_replyupcount);
				introHolder.productView = convertView.findViewById(id.view_product);
				introHolder.contentLyaoutView = (LinearLayout) convertView.findViewById(id.content_layout);
				introHolder.imageViews = new CropImageView[INTRO_MAX_IMAGE];

				for (int i = 0; i < INTRO_MAX_IMAGE; i++) {
					View imageLayout = inflater.inflate(R.layout.adapter_detail_imageview, parent, false);

					introHolder.contentLyaoutView.addView(imageLayout);
					introHolder.imageViews[i] = (CropImageView) imageLayout.findViewById(id.imageview_image);
					introHolder.imageViews[i].setVisibility(View.GONE);
				}

				introHolder.contentView = (TextView) convertView.findViewById(R.id.textview_content);

				// 收藏和赞的资源
				introHolder.collectView = (TextView) convertView.findViewById(R.id.textview_favorite);
				introHolder.timeView = (TextView) convertView.findViewById(id.textview_updatetime);

				introHolder.upTouchView = convertView.findViewById(id.touchview_up);
				introHolder.collectTouchView = convertView.findViewById(id.touchview_collect);

				introHolder.upIconView = (ImageView) convertView.findViewById(id.imageview_up);
				introHolder.collectIconView = (ImageView) convertView.findViewById(id.imageview_collect);

				introHolder.upCountView = (TextView) convertView.findViewById(R.id.textview_upcount);
				// introHolder.upView = (TextView)
				// convertView.findViewById(id.textview_up);

				convertView.setTag(introHolder);
				break;
			case SisterGroupDetailVo.TYPE_COMMENT_HEAD:
				productHolder = new SectionHolder();
				convertView = inflater.inflate(R.layout.adapter_sistergroup_detail_section, parent, false);
				//
				convertView.setTag(productHolder);
				break;
			case SisterGroupDetailVo.TYPE_COMMENT:
				commentHolder = new CommentHolder();
				convertView = inflater.inflate(R.layout.adapter_sistergroup_detail_comment, parent, false);

				commentHolder.nicknameView = (TextView) convertView.findViewById(id.textview_nickname);
				commentHolder.iconView = (CircularImageView) convertView.findViewById(id.imageview_icon);
				commentHolder.skinView = (ImageView) convertView.findViewById(id.imageview_skin);
				commentHolder.viewFloorTag = convertView.findViewById(id.view_landlord);

				commentHolder.contentView = (TextView) convertView.findViewById(id.textview_content);
				commentHolder.timeView = (TextView) convertView.findViewById(id.textview_updatetime);

				commentHolder.floorView = (TextView) convertView.findViewById(id.textview_floor);
				// commentHolder.replyView = (TextView)
				// convertView.findViewById(R.id.textview_reply);

				commentHolder.timeView = (TextView) convertView.findViewById(id.textview_updatetime);

				commentHolder.upTouchView = convertView.findViewById(id.touchview_up);
				commentHolder.upIconView = (ImageView) convertView.findViewById(id.imageview_up);

				commentHolder.upView = (TextView) convertView.findViewById(id.textview_up);

				commentHolder.upCountView = (TextView) convertView.findViewById(R.id.textview_upcount);

				commentHolder.collectTouchView = convertView.findViewById(id.touchview_collect);

				commentHolder.productView = convertView.findViewById(id.view_product);
				commentHolder.contentLyaoutView = (LinearLayout) convertView.findViewById(id.content_layout);
				commentHolder.imageViews = new CropImageView[INTRO_MAX_IMAGE];

				for (int i = 0; i < INTRO_MAX_IMAGE; i++) {
					View imageLayout = inflater.inflate(R.layout.adapter_detail_imageview, parent, false);

					commentHolder.contentLyaoutView.addView(imageLayout);
					commentHolder.imageViews[i] = (CropImageView) imageLayout.findViewById(id.imageview_image);
					commentHolder.imageViews[i].setVisibility(View.GONE);
				}

				convertView.setTag(commentHolder);
				break;
			}
		} else {
			switch (itemType) {
			case SisterGroupDetailVo.TYPE_USERINFO:
				userHolder = (UserHolder) convertView.getTag();
				break;
			case SisterGroupDetailVo.TYPE_INTRO:
				introHolder = (IntroHolder) convertView.getTag();
				break;
			case SisterGroupDetailVo.TYPE_COMMENT_HEAD:
				productHolder = (SectionHolder) convertView.getTag();
				break;
			case SisterGroupDetailVo.TYPE_COMMENT:
				commentHolder = (CommentHolder) convertView.getTag();
				break;
			}
		}

		String postAction[] = getContext().getResources().getStringArray(array.sistergroup_post_action);

		// 设置资源
		switch (itemType) {
		case SisterGroupDetailVo.TYPE_USERINFO:
			UserInfo postUser = item.postPublisherLists;
			if (postUser != null) {
				userHolder.nicknameView.setText(postUser.getNickname());

				if (isOfficial) {// 官方帖子不显示肤质，显示一个官方的小标签
					userHolder.skinView.setBackgroundResource(drawable.ad_guanfang);
				} else {
					userHolder.skinView.setBackgroundDrawable(new BitmapDrawable());
					// userHolder.skinView.setBackgroundResource(UserModule.getUserSkin(getContext(),
					// postUser.getSkin_type()));
				}
				// ImageLoader.getInstance().displayImage(postUser.getMember_avatar(),
				// userHolder.iconView, mAvatarOptions, new
				// DefaultImageLoaderListener());

				// BitmapOptionsFactory.getInstance(getContext()).display(userHolder.iconView,
				// postUser.getMember_avatar(),
				// BitmapOptionsFactory.getOptionConfig(getContext(),
				// drawable.personal_photo));

				mAquery.id(userHolder.iconView).image(postUser.getMember_avatar(), true, true, GlobalUtil.displayMetrics.widthPixels / 4, drawable.personal_photo);

				// mImageWorkerOfAvatar.loadBitmap(TAG_ICON,
				// postUser.getMember_avatar(), userHolder.iconView);
			}

			if (postUser.getMember_code().equals(UserModule.getUserMemberCode(getContext()))) {
				userHolder.reportView.setVisibility(View.INVISIBLE);
			} else {
				userHolder.reportView.setVisibility(View.VISIBLE);
			}

			userHolder.reportView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(getContext(), "1160");
					new ReportUtil().report(getContext(), getItem(position).section.post_code);
				}

			});
			break;
		case SisterGroupDetailVo.TYPE_INTRO:
			final int totalUpCount = item.post_praise_count == 0 ? 0 : Integer.valueOf(item.post_praise_count);
			if (!TextUtils.isEmpty(item.post_title)) {// 如果post_title为空，说明这个帖子是追贴，不是主贴
				introHolder.titleView.setVisibility(View.VISIBLE);
				introHolder.titleTextView.setText(item.post_title);
				introHolder.readNumView.setText(item.post_browse + postAction[0]);
				introHolder.replyNumView.setText(item.reply_acount + postAction[1]);
				introHolder.replyupcount.setText(" " + totalUpCount + postAction[2]);
			} else {
				introHolder.titleView.setVisibility(View.GONE);
			}

			int upCount = TextUtils.isEmpty(item.post_praise) ? 0 : Integer.valueOf(item.post_praise);

			introHolder.collectView.setVisibility(View.VISIBLE);
			introHolder.contentView.setText(item.post_content);
			introHolder.timeView.setText(item.publish_time);

			fillProductAndImagss(position, introHolder.productView, introHolder.imageViews, introHolder.contentLyaoutView);

			if (!TextUtils.isEmpty(item.ispraise)) {
				introHolder.upTouchView.setVisibility(View.VISIBLE);
				introHolder.upIconView.setImageResource(item.ispraise.equals(ConfigInfo.ACTION_UP_OK) ? drawable.icon_up_pressed : drawable.icon_up);
				introHolder.upTouchView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 赞+1/-1
						MobclickAgent.onEvent(getContext(), "1151");
						if (!IntentUtil.checkAndLogin(getContext()))
							return;

						SisterGroupPostVo postVo = getData().get(position).section;
						boolean isUp = postVo.ispraise.equals(ConfigInfo.ACTION_UP_OK);
						int upCount = postVo.post_praise == null ? 0 : Integer.valueOf(postVo.post_praise);

						if (isUp) {
							postVo.post_praise = (upCount - 1) + "";
							postVo.ispraise = ConfigInfo.ACTION_NO;
						} else {
							postVo.post_praise = (upCount + 1) + "";
							postVo.ispraise = ConfigInfo.ACTION_UP_OK;
						}
						notifyDataSetChanged();

						if (TextUtils.isEmpty(postVo.post_code))
							return;// 本地虚拟数据
						// 发送赞的请求
						//new SisterGroupModule((BusinessInterface) getContext()).postOperationState(getContext(), postVo.post_code, SisterGroupModule.TYPE_POST_UP);
					}
				});
			} else {
				introHolder.upTouchView.setVisibility(View.GONE);
			}

			if (TextUtils.isEmpty(item.post_praise)) {
				introHolder.upTouchView.setVisibility(View.GONE);
			} else {
				introHolder.upTouchView.setVisibility(View.VISIBLE);
				introHolder.upCountView.setText(item.post_praise + "人觉得很赞");
			}

			introHolder.upCountView.setVisibility(upCount > 0 ? View.VISIBLE : View.GONE);

			if (item.iscollect == null) {
				introHolder.collectTouchView.setVisibility(View.GONE);
			} else {
				String collectAction[] = getContext().getResources().getStringArray(array.sistergroup_post_collect_action);
				introHolder.collectTouchView.setVisibility(View.VISIBLE);

				boolean isCollect = item.iscollect.equals(ConfigInfo.ACTION_COLLECT_OK);

				introHolder.collectView.setText(" " + (!isCollect ? collectAction[0] : collectAction[1]));
				introHolder.collectIconView.setImageResource(isCollect ? drawable.icon_star_pressed : drawable.icon_star);
				introHolder.collectTouchView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						MobclickAgent.onEvent(getContext(), "1152");
						if (!IntentUtil.checkAndLogin(getContext()))
							return;

						// 赞+1/-1
						SisterGroupPostVo postVo = getData().get(position).section;

						boolean isCollect = postVo.iscollect.equals(ConfigInfo.ACTION_COLLECT_OK);
						if (isCollect) {
							postVo.iscollect = ConfigInfo.ACTION_NO;
						} else {
							postVo.iscollect = ConfigInfo.ACTION_COLLECT_OK;
						}
						notifyDataSetChanged();

						if (TextUtils.isEmpty(postVo.post_code))
							return;// 本地虚拟数据
						// 发送赞的请求
						//new SisterGroupModule((BusinessInterface) getContext()).postOperationState(getContext(), postVo.post_code, SisterGroupModule.TYPE_POST_COLLECT);
					}
				});
			}

			break;
		case SisterGroupDetailVo.TYPE_COMMENT_HEAD:
			break;
		case SisterGroupDetailVo.TYPE_COMMENT:
			commentHolder.collectTouchView.setVisibility(View.GONE);
			SisterGroupCommentVo commentItem = ((SisterGroupPostVoExt) item).native_comment;

			commentHolder.nicknameView.setText(commentItem.postPublisherList.getNickname());

			// 皮肤位置代码
			// commentHolder.skinView.setBackgroundResource(UserModule.getUserSkin(getContext(),
			// commentItem.postPublisherList.getSkin_type()));

			mAquery.id(commentHolder.iconView).image(commentItem.postPublisherList.getMember_avatar(), true, true, GlobalUtil.displayMetrics.widthPixels / 4, drawable.personal_photo);

			commentHolder.timeView.setText(commentItem.publish_time);

			CharSequence commentContent = null;
			if (commentItem.type.equals("1")) {
				String replyNickname = commentItem.publishedList.getNickname();
				String before = postAction[1];
				commentContent = ViewUtil.getTextColorStyle(before + replyNickname + ":" + commentItem.post_content, before.length(), before.length() + replyNickname.length(), getContext()
						.getResources().getColor(color.sistergroup_name_color));
			} else {
				commentContent = commentItem.post_content;
			}

			boolean isFooler = commentItem.postPublisherList != null && commentItem.postPublisherList.getMember_code() != null && mFloorCode != null
					&& commentItem.postPublisherList.getMember_code().equals(mFloorCode);

			commentHolder.viewFloorTag.setVisibility(isFooler ? View.VISIBLE : View.INVISIBLE);

			commentHolder.floorView.setText(getContext().getString(string.sistergroup_post_floor_tips, commentItem.comment_floor + ""));
			commentHolder.upIconView.setImageResource(commentItem.ispraise.equals(ConfigInfo.ACTION_UP_OK) ? drawable.icon_up_pressed : drawable.icon_up);

			int commentUpCount = Integer.valueOf(commentItem.post_praise);

			commentHolder.contentView.setText(commentContent);// 评论
			if (isDeleteComment(commentUpCount)) {
				commentHolder.contentView.setTextColor(getContext().getResources().getColor(color.sistergroup_floor_color));
			} else {
				commentHolder.contentView.setTextColor(getContext().getResources().getColor(color.black_03));
			}

			commentHolder.upView.setText(" " + (commentUpCount > 0 ? commentItem.post_praise : getContext().getString(string.sistergroup_post_operation_up)));

			commentHolder.upTouchView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!IntentUtil.checkAndLogin(getContext()))
						return;
					SisterGroupCommentVo commentItem = ((SisterGroupPostVoExt) (((SisterGroupDetailVo) getItem(position)).section)).native_comment;
					int upCount = commentItem.post_praise == null ? 0 : Integer.valueOf(commentItem.post_praise);
					if (isDeleteComment(upCount)) {
						// 如果是-100，说明是已经被删掉的评论，不允许点击
						return;
					}

					boolean isUp = commentItem.ispraise.equals(ConfigInfo.ACTION_UP_OK);
					if (isUp) {
						commentItem.post_praise = (upCount - 1) + "";
						commentItem.ispraise = ConfigInfo.ACTION_NO;
					} else {
						commentItem.post_praise = (upCount + 1) + "";
						commentItem.ispraise = ConfigInfo.ACTION_UP_OK;
					}
					notifyDataSetChanged();

					if (TextUtils.isEmpty(commentItem.comment_code))
						return;// 本地虚拟数据

					//new SisterGroupModule((BusinessInterface) getContext()).postOperationState(getContext(), commentItem.comment_code, SisterGroupModule.TYPE_POST_COMMENT_UP);
				}
			});
			commentHolder.upCountView.setVisibility(View.GONE);

			fillProductAndImagss(position, commentHolder.productView, commentHolder.imageViews, commentHolder.contentLyaoutView);
			break;
		}
		return convertView;
	}

	/**
	 * 填充商品和图片的数据
	 * 
	 * @param item
	 * @author LiuYuHang
	 * @date 2014年11月28日
	 */
	private void fillProductAndImagss(final int position, View productView, ImageView[] imageViews, View imageViewParentView) {
		SisterGroupPostVo item = ((SisterGroupDetailVo) getItem(position)).section;
		/** 展示商品的逻辑 */

		final SaleProduct saleProduct = item.productinfo == null ? ((SisterGroupPostVoExt) item).native_comment == null ? null : (((SisterGroupPostVoExt) item).native_comment).productinfo
				: item.productinfo;

		if (saleProduct != null && !TextUtils.isEmpty(saleProduct.getId())) {
			productView.setVisibility(View.VISIBLE);
			ViewFiller.fillProductView(saleProduct, productView, BitmapOptionsFactory.getInstance(getContext()));
			productView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					//MobclickAgent.onEvent(getContext(), "1155");
					//IntentUtil.startGoodsDetail(getContext(), saleProduct.getId(), saleProduct.getProductType());
				}

			});
		} else {
			productView.setVisibility(View.GONE);
		}

		/** 展示图片的逻辑 */
		for (int i = 0; i < INTRO_MAX_IMAGE; i++) {
			imageViews[i].setVisibility(View.GONE);
		}

		if (mMeasuredWidth == 0) {
			// mMeasuredWidth = imageViewParentView.getMeasuredWidth();

			// 图片宽度 = 屏幕宽度 - 两边的间隔距离
			mMeasuredWidth = GlobalUtil.displayMetrics.widthPixels - ViewUtil.dip2px(getContext(), 10) * 2;
		}

		final ImageInfo[] imageInfos = item.picInfos == null ? ((SisterGroupPostVoExt) item).native_comment == null ? null : (((SisterGroupPostVoExt) item).native_comment).picInfos : item.picInfos;

		if (imageInfos != null && imageInfos.length > 0) {
			for (int i = 0; i < imageInfos.length; i++) {
				
				//做图片显示判断，最多只能显示的个数
				if (i < INTRO_MAX_IMAGE) {
					
					
					ImageInfoVo imageInfo = imageInfos[i].bigPicInfo;
					imageViews[i].setVisibility(View.VISIBLE);
					imageViews[i].setTag(imageInfo.picUrl);

					//设置图片显示大小根据宽高进行图片压缩
					float scale = (float) imageInfo.height / (float) imageInfo.width;
					LinearLayout.LayoutParams lp = (LayoutParams) imageViews[i].getLayoutParams();
					lp.width = mMeasuredWidth;
					lp.height = (int) (mMeasuredWidth * scale);
					LogUtil.outLogDetail(lp.width + " * " + lp.height);
					imageViews[i].setLayoutParams(lp);
					//BitmapOptionsFactory.getInstance(getContext()).display(imageViews[i], imageInfo.picUrl);
					
					//设置图片缓存以及注册图片点击事件
					mAquery.id(imageViews[i]).image(imageInfo.picUrl, true, true, imageInfo.width, drawable.default_goods_img);
					imageViews[i].setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							MobclickAgent.onEvent(getContext(), "1153");
							String imageUrl = (String) v.getTag();

							final ArrayList<String> urls = new ArrayList<String>();
							// 判断点击的是追贴还是评论的图片

							int firsrt;
							int max;

							SisterGroupPostVo item;

							if (position < getFisrtCommentPosition() - 1) {// 根据当前点击的position和第一条评论的position判断是点击的追贴还是评论的
								// 获取主贴+追贴的图片
								firsrt = 1;
								max = getFisrtCommentPosition() - 1;
							} else {
								
								firsrt = position;
								max = position + 1;
							}
							for (int j = firsrt; j < max; j++) {
								item = ((SisterGroupDetailVo) getItem(j)).section;
								ImageInfo[] imageInfos = item.picInfos == null ? (((SisterGroupPostVoExt) item).native_comment).picInfos : item.picInfos;

								if (imageInfos != null && imageInfos.length > 0) {
									
									urls.addAll(UpdateUtil.getImageUrlList(imageInfos));
								}
							}

							IntentUtil.startImageDetail(getContext(), imageUrl, urls, "SisterGroupDetailAcvitity");
						}
					});
				}
			}
		}

		
	}

	/**
	 * 是否是已经被删除掉的评论
	 * 
	 * @param upCount
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月28日
	 */
	private boolean isDeleteComment(int upCount) {
		if (upCount == -100)
			return true;
		return false;
	}

	/**
	 * 标头用户信息
	 * 
	 * @author LiuYuHang
	 * 
	 */
	private class UserHolder {
		private CircularImageView iconView;
		private ImageView skinView;
		private TextView nicknameView, reportView;
	}

	/**
	 * 正文部分
	 * 
	 * @author LiuYuHang
	 * 
	 */
	private class IntroHolder {
		private View titleView;// 主贴的标题View
		private TextView titleTextView, readNumView, replyNumView;// 主贴的标题
		private LinearLayout contentLyaoutView;
		private View productView;
		private CropImageView imageViews[];
		private View upTouchView, collectTouchView;// 点击的view区域
		private ImageView upIconView, collectIconView;
		private TextView timeView, contentView, upCountView, collectView, replyupcount;
	}

	/**
	 * section的Holder
	 * 
	 * @author LiuYuHang
	 * 
	 */
	private class SectionHolder {
	}

	/**
	 * 评论
	 * 
	 * @author LiuYuHang
	 * 
	 */
	private class CommentHolder {
		private CircularImageView iconView;
		private ImageView skinView;
		private View viewFloorTag;// 楼主的标示
		private View upTouchView, collectTouchView;// 点击的view区域
		private ImageView upIconView;
		private TextView nicknameView, contentView, timeView, floorView, upView, upCountView;

		private LinearLayout contentLyaoutView;
		private CropImageView imageViews[];
		private View productView;
	}

}
