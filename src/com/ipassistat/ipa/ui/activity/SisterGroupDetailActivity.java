package com.ipassistat.ipa.ui.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.R.drawable;
import com.ipassistat.ipa.R.id;
import com.ipassistat.ipa.R.layout;
import com.ipassistat.ipa.R.string;
import com.ipassistat.ipa.adapter.SisterGroupDetailAdapter;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.PostOperationCommentListResponse;
import com.ipassistat.ipa.bean.response.entity.MemberInfo;
import com.ipassistat.ipa.bean.response.entity.SisterGroupCommentVo;
import com.ipassistat.ipa.bean.response.entity.SisterGroupDetailVo;
import com.ipassistat.ipa.bean.response.entity.SisterGroupPostVo;
import com.ipassistat.ipa.bean.response.entity.SisterGroupPostVoExt;
import com.ipassistat.ipa.business.UserModule;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.util.ApiUrl;
import com.ipassistat.ipa.util.InputMethodUtil;
import com.ipassistat.ipa.util.IntentUtil;
import com.ipassistat.ipa.util.NetUtil;
import com.ipassistat.ipa.util.ProgressHub;
import com.ipassistat.ipa.util.ReportUtil;
import com.ipassistat.ipa.util.ToastUtil;
import com.ipassistat.ipa.util.UpdateUtil;
import com.ipassistat.ipa.util.ViewUtil;
import com.ipassistat.ipa.util.InputMethodUtil.InputMethodListner;
import com.ipassistat.ipa.util.UpdateUtil.ImageUpdateListener;
import com.ipassistat.ipa.util.share.um.ShareUM;
import com.ipassistat.ipa.view.PaginationListView;
import com.ipassistat.ipa.view.PhotoPickerView;
import com.ipassistat.ipa.view.PaginationListView.PaginationListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 姐妹圈 - 帖子详情页
 * 
 * @author LiuYuHang
 * @author shipeifei  2015-03-24
 * @date 2014年10月29日
 */
public class SisterGroupDetailActivity extends BaseActivity implements OnClickListener, InputMethodListner, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
	
	
	/**成员变量*/
	private SisterGroupDetailAdapter mAdapter;
	private PaginationListView mListView;
	private SisterGroupPostVo mIntentPost;
	private PhotoPickerView mPhotoPickerView;
	private EditText mCommentEditView;

	/** 主要用在页面跳转动画实现 */
	public static SisterGroupDetailActivity mActivitySisterGroupDetail;
	private ShareUM shareUM = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sistergroup_detail);
		
		MobclickAgent.onEvent(getApplicationContext(), "1040");
		
		setTitleText(getString(string.sistergroup_post_activity_title));
		setTitleRightImage(R.drawable.button_goodsdetail_share);
		
		mActivitySisterGroupDetail = SisterGroupDetailActivity.this;
		findListeners(this, R.id.left_imgv, R.id.right_imgv, id.button_submit);
		initWidgets();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("1155"); // 统计页面
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("1155"); // 统计页面
	}

	/***
	 * 初始化页面控件
	 * @author 时培飞
	 * Create at 2015-4-10 上午10:46:28
	 */
	private void initWidgets() {
		
		//获取姐妹圈通过Intent传递的信息
		mIntentPost = (SisterGroupPostVo) getIntent().getSerializableExtra(IntentUtil.INTENT_SISTER_POST);
		
		
		mCommentEditView = (EditText) findViewById(id.editview_comment);
		mPhotoPickerView = (PhotoPickerView) findViewById(id.photo_picker_view);
		mPhotoPickerView.setMaxPhotoNumber(4);
		
		mAdapter = new SisterGroupDetailAdapter(this, formatDetailList(mIntentPost));
		mAdapter.setIsOfficial(getIntent().getBooleanExtra(IntentUtil.INTENT_SISTER_POST_ISOFFICIAL, false));
		//配置PaginationListView的相关信息
		mListView = (PaginationListView) findViewById(R.id.listview);
		mListView.setAdapter(mAdapter);
		mListView.showLoadingView(true);
		mListView.setPullAvailable(true, false);
		mListView.setOnPaginationListener(new PaginationListener() {

			@Override
			public void onRequestNextPage(int page) {
				asyncRequestPostComment(mIntentPost.post_code, page);
			}

			@Override
			public void onRefresh() {
				mListView.setPullAvailable(true, false);
				mListView.mayHaveNextPage();
				asyncRequestPostDetail(mIntentPost.post_code);
			}
		});
		asyncRequestPostDetail(mIntentPost.post_code);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);

		mCommentEditView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mPhotoPickerView.setVisibility(View.GONE);

				boolean keyboardState = InputMethodUtil.isKeyboardOpen(findViewById(id.activity_root));
				if (!keyboardState) {
					CommentVO commentVo = (CommentVO) mCommentEditView.getTag();
					if (commentVo != null) {
						commentVo.type = CommentVO.TYPE_COMMENT;
					}
					updateCommentEditText(keyboardState);
				}
				return false;
			}
		});

		InputMethodUtil.setOnKeyboardStateListener(findViewById(id.activity_root), new InputMethodListner() {

			@Override
			public void onInputMethodOpend(boolean open) {
				if (open) {
					// LogUtil.outLogDetail("inputMethod open");
					mListView.findTouchView().setVisibility(View.VISIBLE);
					mListView.findTouchView().setOnTouchListener(new TouchViewListener());
				} else if (!isPhotoPickerVisibility()) {
					// LogUtil.outLogDetail("inputMethod close");
					mListView.findTouchView().setVisibility(View.GONE);
				}
				updateCommentEditText(open);
			}
		});
		updateUI();
	}

	/**
	 * 点击listview透明touch层的处理
	 * 
	 * @author LiuYuHang
	 * @date 2014年10月14日
	 */
	public class TouchViewListener implements OnTouchListener {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			InputMethodUtil.showForced(getApplicationContext(), v, false, SisterGroupDetailActivity.this);
			v.setVisibility(View.GONE);
			mPhotoPickerView.setVisibility(View.GONE);
			updateCommentEditText(false);
			return true;
		}

	}

	/**
	 * 是否是自己的帖子
	 * 
	 * @return
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月28日
	 */
	private boolean isSelftPost() {
		if (!UserModule.isLogin(getApplicationContext()))
			return false;
		MemberInfo userModule = UserModule.getMemberInfo(getApplicationContext());
		if (mIntentPost == null || mIntentPost.postPublisherLists == null || userModule == null || userModule.getMember_code() == null)
			return false;
		return userModule.getMember_code().equals(mIntentPost.postPublisherLists.getMember_code());
	}

	/**
	 * 根据model，重新绘制UI
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月28日
	 */
	private void updateUI() {
		View pulsView = findViewById(id.button_plus);
		TextView submitButton = (TextView) findViewById(id.button_submit);
		// String[] submitText =
		// getResources().getStringArray(array.sistergroup_bottom_btn);

		int submitButtonRes[] = new int[] { drawable.button_comment, drawable.button_sender };

		pulsView.setVisibility(View.VISIBLE);
		pulsView.setOnClickListener(this);

		if (isSelftPost()) {
			submitButton.setBackgroundResource(submitButtonRes[1]);
			// submitButton.setText(submitText[1]);
		} else {
			submitButton.setBackgroundResource(submitButtonRes[0]);
			// submitButton.setText(submitText[0]);
			// pulsView.setVisibility(View.GONE);
		}
	}

	/**
	 * 绘制底部文本框的状态
	 * 
	 * @param keyboardOpen
	 *            软键盘的打开状态
	 * 
	 * @author LiuYuHang
	 * @date 2014年10月9日
	 */
	private void updateCommentEditText(boolean keyboardOpen) {
		View pulsView = findViewById(id.button_plus);// 回复旁边的小加号
		pulsView.setVisibility(View.VISIBLE);

		CommentVO commentVo = (CommentVO) mCommentEditView.getTag();
		if (isSelftPost()) {
			mCommentEditView.setHint(getString(string.sistergroup_bottom_edit_self_tips));
		} else {
			mCommentEditView.setHint(getString(string.sistergroup_bottom_edit_tips));
		}
		if (keyboardOpen) {
			// mCommentEditView.requestFocus();
			if (commentVo != null) {
				if (!TextUtils.isEmpty(commentVo.commentBackup[commentVo.type])) {
					mCommentEditView.setText(commentVo.commentBackup[commentVo.type]);
					boolean hasContent = TextUtils.isEmpty(mCommentEditView.getText());
					if (hasContent)
						mCommentEditView.setSelection(mCommentEditView.getText().toString().length());
				}
				if (commentVo.replyUser != null && commentVo.type == CommentVO.TYPE_REPLY) {
					mCommentEditView.setHint(getString(string.sistergroup_post_reply_text, commentVo.replyUser.native_comment.postPublisherList.getNickname()));
					pulsView.setVisibility(View.GONE);// 回复评论的时候，不需要小加号
				}
			}
		} else {
			// mListView.requestFocus();
			String editTextContent = mCommentEditView.getText().toString();
			if (commentVo == null) {
				commentVo = new CommentVO();
			}
			if (!TextUtils.isEmpty(editTextContent)) {
				commentVo.commentBackup[commentVo.type] = editTextContent;
			}

			mCommentEditView.setTag(commentVo);
			if (isPhotoPickerVisibility() && !TextUtils.isEmpty(findCommentContent(commentVo))) {
				mCommentEditView.setText(findCommentContent(commentVo));
			} else {
				mCommentEditView.setText("");
			}
		}
	}

	/**
	 * 找到内存中保存的content
	 * 
	 * @param commentVo
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月29日
	 */
	private String findCommentContent(CommentVO commentVo) {
		if (commentVo == null) {
			return null;
		}
		return commentVo.commentBackup[commentVo.type];
	}

	/**
	 * 异步请求
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月24日
	 * 
	 * @param postId
	 */
	private void asyncRequestPostDetail(String postId) {
		//new SisterGroupModule(this).postOperationDetail(getApplicationContext(), postId);
	}

	/**
	 * 异步请求评论
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月24日
	 * 
	 * @param postId
	 * @param page
	 */
	private void asyncRequestPostComment(String postId, int page) {
		//new SisterGroupModule(this).postOperationCommentPull(getApplicationContext(), postId, page);
	}

	@Override
	public void onMessageSucessCalledBack(String url, Object object) {
		super.onMessageSucessCalledBack(url, object);

		if (url.equals(ConfigInfo.API_POST_OPERATION_DETAIL)) {
			SisterGroupPostVo post = (SisterGroupPostVo) object;

			if (post.getResultCode() == ConfigInfo.RESULT_POST_DELETE) {
				// 判断帖子是否被删除
				finish();
				return;
			}
			List<SisterGroupDetailVo> data = formatDetailList(post);
			if (mAdapter != null) {
				mAdapter.resetData(data);
				asyncRequestPostComment(post.post_code, 0);
			}
			mIntentPost = post;
			mIntentPost.postPublisherLists = post.postPublisherLists;
			mAdapter.setFloorCode(mIntentPost.postPublisherLists.getMember_code());
		} else if (url.equals(ConfigInfo.API_POST_OPERATION_COMMENTLIST)) {
			// 加载评论
			PostOperationCommentListResponse responseComment = (PostOperationCommentListResponse) object;
			List<SisterGroupCommentVo> commentList = responseComment.postsCommentLists;
			List<SisterGroupDetailVo> appendList = new ArrayList<SisterGroupDetailVo>();
			SisterGroupDetailVo detailVo;

			// if ((Integer) responseComment.getTag() == 0) {//
			// 如果是第一次的评论，会添加一个最新评论的Section
			// detailVo = new SisterGroupDetailVo();
			// detailVo.sectionType = SisterGroupDetailVo.TYPE_COMMENT_HEAD;
			// appendList.add(detailVo);
			// }

			if (commentList != null && commentList.size() > 0) {

				Iterator<SisterGroupCommentVo> iterator = commentList.iterator();
				while (iterator.hasNext()) {
					detailVo = new SisterGroupDetailVo();

					detailVo.sectionType = SisterGroupDetailVo.TYPE_COMMENT;
					detailVo.section = new SisterGroupPostVoExt();
					((SisterGroupPostVoExt) detailVo.section).native_comment = iterator.next();
					appendList.add(detailVo);
				}
				mListView.setPullAvailable(true, true);
				mAdapter.loadNextPage(appendList);
			}
			if (mAdapter.hasComment()) {
				mListView.delegatePageCheck(responseComment.paged);
			} else {
				mListView.noMorePage(View.inflate(getApplicationContext(), layout.adapter_comment_empty, null));
			}
		} else if (url.equals(ConfigInfo.API_POST_OPERATION_COMMENT_SEND) || url.equals(ConfigInfo.API_POST_OPERATION_FOLLOW_ADD) || url.equals(ConfigInfo.API_POST_OPERATION_COMMENT_REPLY)) {
			BaseResponse response = (BaseResponse) object;
			if (response.getResultCode() == ConfigInfo.RESULT_OK) {
				// 发帖，追贴成功之后 的后续操作（清空输入框，已选择的商品和图片）
				// mPhotoPickerView.clear();
				mPhotoPickerView.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onFinish() {
		super.onFinish();
		mListView.onRefreshComplete();
	}

	/**
	 * 详情页分3段，需要根据传入的post做修改，第一段是标题，第二段是主贴（分为主贴和追贴），第三段是评论
	 * 
	 * @param post
	 * @return
	 */
	private List<SisterGroupDetailVo> formatDetailList(SisterGroupPostVo post) {
		List<SisterGroupDetailVo> list = new ArrayList<SisterGroupDetailVo>();
		SisterGroupDetailVo detailVo;

		detailVo = new SisterGroupDetailVo();
		detailVo.sectionType = SisterGroupDetailVo.TYPE_USERINFO;
		detailVo.section = (SisterGroupPostVo) post.clone();
		list.add(detailVo);

		detailVo = new SisterGroupDetailVo();
		detailVo.sectionType = SisterGroupDetailVo.TYPE_INTRO;
		detailVo.section = (SisterGroupPostVo) post.clone();
		list.add(detailVo);

		if (post.postFollowLists != null && post.postFollowLists.size() != 0) {
			Iterator<SisterGroupPostVo> iterator = post.postFollowLists.iterator();
			while (iterator.hasNext()) {
				detailVo = new SisterGroupDetailVo();
				detailVo.sectionType = SisterGroupDetailVo.TYPE_INTRO;
				detailVo.section = (SisterGroupPostVo) iterator.next().clone();
				list.add(detailVo);
			}
		}

		// if ((Integer) responseComment.getTag() == 0) {//
		// 如果是第一次的评论，会添加一个最新评论的Section
		detailVo = new SisterGroupDetailVo();
		detailVo.sectionType = SisterGroupDetailVo.TYPE_COMMENT_HEAD;
		list.add(detailVo);
		// }

		// 评论要单独获取
		// for (int i = 0; i < 10; i++) {
		// detailVo = new SisterGroupDetailVo();
		// detailVo.sctionType = SisterGroupDetailVo.TYPE_COMMENT;
		// detailVo.section = (SisterGroupPostVo) post.clone();
		// list.add(detailVo);
		// }
		return list;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mPhotoPickerView.onActivityResult(requestCode, resultCode, data);

		// UMSocialService mController =
		// UMServiceFactory.getUMSocialService("com.umeng.share",
		// RequestType.SOCIAL);

		if (shareUM != null) {
			UMSsoHandler ssoHandler = shareUM.mController.getConfig().getSsoHandler(requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ichsy.mboss.ui.activity.BaseActivity#onMessageFailedCalledBack(java
	 * .lang.String, java.lang.Object)
	 */
	@Override
	public void onMessageFailedCalledBack(String url, Object object) {
		super.onMessageFailedCalledBack(url, object);
		mListView.noNet(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				asyncRequestPostDetail(mIntentPost.post_code);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_imgv:
			MobclickAgent.onEvent(this, "1119");
			finish();
			break;
		case R.id.right_imgv://友盟分享
			MobclickAgent.onEvent(this, "1210",mIntentPost.post_code);
			
			// 可配置内容为：帖子的第一张图片(没有图片用剧微商logo)+帖子标题，短信的可配置内容为：剧微商+帖子标题+详情页地址+快来姐妹圈找我吧；
			// 姐妹们都在关注这个，‘帖子标题’，聊美丽聊女人聊生活…快加入姐妹圈来找我吧！+姐妹圈帖子链接地址
			String shareUrl = ApiUrl.getShareUrl(mIntentPost.linkUrl);
			String shareImageUrl = null;

			String imageUrls = formatDetailList(mIntentPost).get(1).section.post_img;
			if (!TextUtils.isEmpty(imageUrls)) {
				shareImageUrl = UpdateUtil.getImageUrl(imageUrls)[0];
			}
			shareUM = new ShareUM(this);

			if (shareImageUrl == null) {
				// 姐妹们都在关注这个，‘帖子标题’，聊美丽聊女人聊生活…不加入姐妹圈你就OUT啦
				shareUM.setShare("快来参与‘" + mIntentPost.post_title + "’话题互动哟~", "快来参与‘" + mIntentPost.post_title + "’话题互动哟，请戳", mIntentPost.post_title, drawable.icon, shareUrl);
			} else {
				shareUM.setShare("快来参与‘" + mIntentPost.post_title + "’话题互动哟~", "快来参与‘" + mIntentPost.post_title + "’话题互动哟，请戳", mIntentPost.post_title, shareImageUrl, shareUrl);
			}
			break;
		case id.button_submit:
			if (!IntentUtil.checkAndLogin(getApplicationContext())) {
				return;
			}
			// if (!UserModule.isLogin(getApplicationContext())) {
			// IntentUtil.startLoginActivity(this);
			// return;
			// }
			final String commentStr = mCommentEditView.getText().toString();
			final String postId = mIntentPost.post_code;

			final CommentVO tag = (CommentVO) mCommentEditView.getTag();
			if (!NetUtil.isNetworkConnected(this)) {
				ToastUtil.showToast(getApplicationContext(), getString(string.global_message_no_net));
				return;
			} else if (ViewUtil.isEdittextEmpty(mCommentEditView)) {
				ToastUtil.showToast(getApplicationContext(), getString(string.global_message_content_empty, ""));
				return;
			}
			UpdateUtil.getInstance().updateImages(mPhotoPickerView.getBitmapPath(), new ImageUpdateListener() {

				@Override
				public void onUpdateComplate(List<String> bitmapUrls) {
					ProgressHub.getInstance(SisterGroupDetailActivity.this).dismiss();

					String productId = mPhotoPickerView.getProduct() != null ? mPhotoPickerView.getProduct().getId() : null;
					String updateImgs = UpdateUtil.formatImageString(bitmapUrls);

					if (tag == null || tag.type == CommentVO.TYPE_COMMENT) {// 回复帖子的逻辑
						if (isSelftPost()) {
							MobclickAgent.onEvent(getApplicationContext(), "1148");
							// 追贴
							// if (mPhotoPickerView.getBitmapPath() !=
							// null) {

							mAdapter.fillLocalWithIntro(commentStr, mPhotoPickerView.getProduct(), UpdateUtil.formatImageInfo(bitmapUrls, mPhotoPickerView.getBitmapPath()));
//							new SisterGroupModule(SisterGroupDetailActivity.this).postOperationFollowAddOrReply(getApplicationContext(), SisterGroupModule.TYPE_POST_ADD, postId, commentStr,
//									productId, updateImgs);

							// } else {
							// mAdapter.fillLocalWithIntro(commentStr,
							// mPhotoPickerView.getProduct(), null);
							// new
							// SisterGroupModule(this).postOperationFollowAddOrReply(getApplicationContext(),
							// SisterGroupModule.TYPE_POST_ADD, postId,
							// commentStr,
							// mPhotoPickerView.getProduct() != null ?
							// mPhotoPickerView.getProduct().id : null,
							// null);
							// }
						} else {
							MobclickAgent.onEvent(getApplicationContext(), "1157");
							// 评论
							CommentVO commentVo = (CommentVO) mCommentEditView.getTag();
							mAdapter.fileLocalWithComment(commentStr, commentVo == null ? null : commentVo.replyUser, "0", mPhotoPickerView.getProduct(),
									UpdateUtil.formatImageInfo(bitmapUrls, mPhotoPickerView.getBitmapPath()));
//							new SisterGroupModule(SisterGroupDetailActivity.this).postOperationFollowAddOrReply(getApplicationContext(), SisterGroupModule.TYPE_POST_REPLY, postId, commentStr,
//									productId, updateImgs);

							mListView.mayHaveNextPage();
							mListView.noMorePage();
							mListView.updateNoMorePageText(getString(string.global_message_loading_nomore));
						}
					} else if (tag.type == CommentVO.TYPE_REPLY) {// 回复评论的逻辑
						// 对评论的回复
						CommentVO commentVo = (CommentVO) mCommentEditView.getTag();
						mAdapter.fileLocalWithComment(commentStr, commentVo == null ? null : commentVo.replyUser, "1", mPhotoPickerView.getProduct(),
								UpdateUtil.formatImageInfo(bitmapUrls, mPhotoPickerView.getBitmapPath()));
						//new SisterGroupModule(SisterGroupDetailActivity.this).postOperationCommentReply(getApplicationContext(), postId, tag.replyUser.native_comment.comment_code, commentStr);

						mListView.mayHaveNextPage();
						mListView.noMorePage();
						mListView.updateNoMorePageText(getString(string.global_message_loading_nomore));
					}
					mPhotoPickerView.clear();
					mCommentEditView.setText("");
					mCommentEditView.setTag(new CommentVO());
				}

				@Override
				public void onProgress(int max, int position) {

				}

				@Override
				public void onError(String error) {

				}

				@Override
				public void onBegin() {
					ProgressHub.getInstance(SisterGroupDetailActivity.this).show(getString(R.string.uploadingimages));
				}
			});

			InputMethodUtil.showForced(getApplicationContext(), mCommentEditView, false, this);

			break;
		case id.button_plus:
			InputMethodUtil.showForced(getApplicationContext(), mCommentEditView, false, this);

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					mPhotoPickerView.setVisibility(isPhotoPickerVisibility() ? View.GONE : View.VISIBLE);

					if (isPhotoPickerVisibility()) {
						mListView.findTouchView().setVisibility(View.VISIBLE);
						mListView.findTouchView().setOnTouchListener(new TouchViewListener());
					} else {
						mListView.findTouchView().setVisibility(View.GONE);
					}
				}
			}, 100);

			if (isSelftPost()) {

			} else {
				MobclickAgent.onEvent(getApplicationContext(), "1118");
			}

			// if (photoPickerVisibility) {
			// mListView.findTouchView().setVisibility(View.VISIBLE);
			// mListView.findTouchView().setOnTouchListener(new
			// TouchViewListener());
			// }
			break;
		}

	}

	/**
	 * 追加图片或者商品的view是否显示
	 * 
	 * @return
	 * @author LiuYuHang
	 * @date 2014年10月29日
	 */
	private boolean isPhotoPickerVisibility() {
		return mPhotoPickerView.getVisibility() == View.VISIBLE;
	}

	@Override
	public void onInputMethodOpend(boolean open) {
		if (open) {
			mPhotoPickerView.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		SisterGroupDetailVo item = (SisterGroupDetailVo) mAdapter.getItem(position - 1);
		if (item.sectionType == SisterGroupDetailVo.TYPE_COMMENT) {
			SisterGroupPostVoExt actionPostVo = (SisterGroupPostVoExt) item.section;

			// 当前要评论的人的用户code
			String comentUserCode = actionPostVo.native_comment.postPublisherList.getMember_code();
			int upCount = Integer.valueOf(actionPostVo.native_comment.post_praise);

			// 如果未登录，获取当前用户信息可能为NULL
			String selfUserCode = UserModule.getMemberInfo(getApplicationContext()) == null ? "" : UserModule.getMemberInfo(getApplicationContext()).getMember_code();

			if (TextUtils.isEmpty(comentUserCode))
				return false;// 为空说明是本地模拟数据
			// 不能评论自己的帖子
			if (comentUserCode.equals(selfUserCode) || upCount == -100) {
				return false;
			}

			MobclickAgent.onEvent(getApplicationContext(), "1161");
			new ReportUtil().report(this, mIntentPost.post_code, actionPostVo.native_comment.comment_code);
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SisterGroupDetailVo item = (SisterGroupDetailVo) mAdapter.getItem(position - 1);
		if (item.sectionType == SisterGroupDetailVo.TYPE_COMMENT) {
			SisterGroupPostVoExt actionUser = (SisterGroupPostVoExt) item.section;

			// 当前要评论的人的用户code
			String comentUserCode = actionUser.native_comment.postPublisherList.getMember_code();
			int upCount = Integer.valueOf(actionUser.native_comment.post_praise);

			// 如果未登录，获取当前用户信息可能为NULL
			String selfUserCode = UserModule.getMemberInfo(getApplicationContext()) == null ? "" : UserModule.getMemberInfo(getApplicationContext()).getMember_code();

			if (TextUtils.isEmpty(comentUserCode))
				return;// 为空说明是本地模拟数据
			// 不能评论自己的帖子
			if (comentUserCode.equals(selfUserCode) || upCount == -100) {
				return;
			}

			CommentVO commentVo = (CommentVO) mCommentEditView.getTag();
			if (commentVo == null) {
				commentVo = new CommentVO();
				mCommentEditView.setTag(commentVo);
			}
			commentVo.type = CommentVO.TYPE_REPLY;
			commentVo.replyUser = actionUser;
			InputMethodUtil.showForced(getApplicationContext(), mCommentEditView, true, SisterGroupDetailActivity.this);
		}
	}

	/**
	 * 保存当前页面底部文本框的状态实体类
	 * 
	 * @author LiuYuHang
	 * @date 2014年10月10日
	 * 
	 */
	private class CommentVO {
		final static int TYPE_COMMENT = 0, TYPE_REPLY = 1;
		/**
		 * 1是评论2是回复
		 */
		int type;// 评论还是回复
		String[] commentBackup = new String[2];// 评论和对评论回复的备份
		SisterGroupPostVoExt replyUser;// 回复人
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindEvents() {
		// TODO Auto-generated method stub
		
	}
}
