package com.ipassistat.ipa.business;

import android.app.Activity;
import android.content.Context;

import com.ipassistat.ipa.bean.local.RequestOptions;
import com.ipassistat.ipa.bean.request.BaseRequest;
import com.ipassistat.ipa.bean.request.MessageOperationRequest;
import com.ipassistat.ipa.bean.request.PopPostListRequest;
import com.ipassistat.ipa.bean.request.PostOperationCommentReplyRequest;
import com.ipassistat.ipa.bean.request.PostOperationFollowAddRequest;
import com.ipassistat.ipa.bean.request.PostOperationPullRequest;
import com.ipassistat.ipa.bean.request.PostOperationRequest;
import com.ipassistat.ipa.bean.request.PostOperationSendRequest;
import com.ipassistat.ipa.bean.request.ProductSearchRequest;
import com.ipassistat.ipa.bean.request.entity.PageOption;
import com.ipassistat.ipa.bean.response.AppHomeResponse;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.PostOperationCommentListResponse;
import com.ipassistat.ipa.bean.response.PostOperationResponse;
import com.ipassistat.ipa.bean.response.PostOperationTagsResponse;
import com.ipassistat.ipa.bean.response.ProductSearchResponse;
import com.ipassistat.ipa.bean.response.RecreationResponse;
import com.ipassistat.ipa.bean.response.SisterGroupPostListResponse;
import com.ipassistat.ipa.bean.response.SystemMessageResponse;
import com.ipassistat.ipa.bean.response.UserMessageResponse;
import com.ipassistat.ipa.bean.response.entity.SisterGroupPostVo;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.dao.cache.CacheConstant;
import com.ipassistat.ipa.ui.fragment.RecreationFragment;
import com.ipassistat.ipa.ui.fragment.SisterGroupMessageFragment;
import com.ipassistat.ipa.util.ScreenInfoUtil;
import com.ipassistat.ipa.util.http.HttpProcessor;
import com.ipassistat.ipa.util.http.RequestListener;

/**
 * 姐妹圈模块
 * 
 * @author lxc
 *
 */
public class SisterGroupModule extends BaseModule {
	// public static final String TEST_TOKEN =
	// "98182e44d7ef44f483974a7a878809cb82a8349649ee413191e2698ee6f92e48";

	public static final int TYPE_POST_UP = 1;
	public static final int TYPE_POST_COLLECT = 2;
	public static final int TYPE_POST_COMMENT_UP = 3;

	public static final int TYPE_POST_ADD = 1;
	public static final int TYPE_POST_REPLY = 2;
	public static final int TYPE_POST_COMMENT_REPLY = 3;

	public static final String POST_TYPE_ALL = "0";
	public static final String POST_TYPE_SENDED = "1";
	public static final String POST_TYPE_PART = "2";
	public static final String POST_TYPE_COLLECT = "3";

	private HttpProcessor httpProcessor;

	public SisterGroupModule(BusinessInterface dataCallBack) {
		super(dataCallBack);

	}

	public void init(Context context) {
		// , RequestListener requestListener
		httpProcessor = new HttpProcessor(context);
	}

	/**
	 * 消息中心 - 系统相关
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 *
	 */
	public void messageOperationListPull(Context context, int type, int page) {
		MessageOperationRequest request = new MessageOperationRequest();
		request.paging = new PageOption();
		request.paging.setLimit(20);
		request.paging.setOffset(page);

		request.tag = page;

		if (type == SisterGroupMessageFragment.TYPE_SYSTEM) {
			getDataByPost(context, ConfigInfo.API_MESSAGE_SYSTEM, request, SystemMessageResponse.class);
		} else if (type == SisterGroupMessageFragment.TYPE_MINE) {
			getDataByPost(context, ConfigInfo.API_MESSAGE_MINE, request, UserMessageResponse.class);
		}
	}

	/**
	 * 消息中心 - 是否有未读消息
	 *
	 *
	 * @author LiuYuHang
	 * @date 2014年9月30日
	 */
	public void messageOperationStateGet(Context context) {
		BaseRequest request = new BaseRequest();

		RequestOptions options = new RequestOptions();
		options.errorToast = false;
		options.timeOutToast = false;
		options.noNetToast = false;

		getDataByPost(context, ConfigInfo.API_MESSAGE_READ_STATE, request, options, SystemMessageResponse.class);
	}

	/**
	 * 消息中心 - 修改消息为已读
	 *
	 * @param context
	 * @param messageId
	 *            多个消息用 , 分割
	 *
	 * @author LiuYuHang
	 * @date 2014年9月30日
	 */
	public void messageOperationStateChange(Context context, String messageId) {
		MessageOperationRequest request = new MessageOperationRequest();
		request.message_code = messageId;
		getDataByPost(context, ConfigInfo.API_MESSAGE_READ_MODIFY, request, SystemMessageResponse.class);
	}

	/**
	 * 搜索商品
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 *
	 * @param context
	 */
	public void productSearch(Context context, String keyword) {
		ProductSearchRequest request = new ProductSearchRequest(keyword);
		request.picWidth = String.valueOf(context.getResources().getDisplayMetrics().widthPixels);
		getDataByPost(context, ConfigInfo.API_PRODUCT_SEARCH, request, ProductSearchResponse.class);
	}

	/**
	 * 帖子详情 - 获取详情
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月24日
	 *
	 */
	public void postOperationDetail(Context context, String postId) {
		PostOperationRequest request = new PostOperationRequest();
		request.post_code = postId;
		request.picWidth = String.valueOf(context.getResources().getDisplayMetrics().widthPixels);
		getDataByPost(context, ConfigInfo.API_POST_OPERATION_DETAIL, request, SisterGroupPostVo.class);
	}

	/**
	 * 帖子详情 - 拉取评论
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 *
	 * @param context
	 */
	public void postOperationCommentPull(Context context, String postId, int page) {
		PostOperationRequest request = new PostOperationRequest();
		request.post_code = postId;
		request.picWidth = String.valueOf(context.getResources().getDisplayMetrics().widthPixels);
		request.tag = page;

		// 分页
		request.paging = new PageOption();
		request.paging.setLimit(20);
		request.paging.setOffset(page);
		getDataByPost(context, ConfigInfo.API_POST_OPERATION_COMMENTLIST, request, PostOperationCommentListResponse.class);
	}

	/**
	 * 帖子 发帖 获取标签
	 *
	 * @param context
	 *
	 * @author LiuYuHang
	 * @date 2014年9月30日
	 */
	public void postOperationPullTags(Context context) {
		BaseRequest request = new BaseRequest();
		getDataByPost(context, ConfigInfo.API_POST_OPERATION_SEND_TAGS, request, PostOperationTagsResponse.class);
	}

	/**
	 * 帖子（包括回复）操作，点赞，收藏
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 *
	 */
	public void postOperationState(Context context, String postId, int type) {
		PostOperationRequest request = new PostOperationRequest();
		switch (type) {
		case TYPE_POST_UP:
			request.post_code = postId;
			getDataByPost(context, ConfigInfo.API_POST_OPERATION_UP, request, PostOperationResponse.class);
			break;
		case TYPE_POST_COLLECT:
			request.post_code = postId;
			getDataByPost(context, ConfigInfo.API_POST_OPERATION_COLLECT, request, PostOperationResponse.class);
			break;
		case TYPE_POST_COMMENT_UP:
			request.comment_code = postId;
			getDataByPost(context, ConfigInfo.API_POST_OPERATION_COMMENT_UP, request, PostOperationResponse.class);
			break;
		}
	}

	/**
	 * 帖子详情 - 对评论进行回复
	 *
	 *
	 * @author LiuYuHang
	 * @date 2014年9月30日
	 */
	public void postOperationCommentReply(Context context, String postId, String commentId, String content) {
		PostOperationCommentReplyRequest request = new PostOperationCommentReplyRequest();
		request.post_code = postId;
		request.comment_code = commentId;
		request.comment_content = content;

		// request.product_code = productId;
		// request.comment_img = images;

		getDataByPost(context, ConfigInfo.API_POST_OPERATION_COMMENT_REPLY, request, PostOperationCommentListResponse.class);
	}

	/**
	 * 帖子操作 回帖(评论)，追贴
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 *
	 */
	public void postOperationFollowAddOrReply(Context context, int type, String postId, String content, String productId, String images) {
		PostOperationFollowAddRequest request = new PostOperationFollowAddRequest(postId, content);
		request.product_code = productId;

		if (type == TYPE_POST_ADD) {
			request.comment_img = images;
			getDataByPost(context, ConfigInfo.API_POST_OPERATION_FOLLOW_ADD, request, BaseResponse.class);
		} else {
			request.post_img = images;
			getDataByPost(context, ConfigInfo.API_POST_OPERATION_COMMENT_SEND, request, BaseResponse.class);
		}

	}

	/**
	 * 帖子操作，发帖
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 *
	 */
	public void postOperationSend(Context context, String postTag, String title, String content, String productId, String images) {
		PostOperationSendRequest request = new PostOperationSendRequest(postTag, title, content);
		request.product_code = productId;
		request.post_img = images;
		getDataByPost(context, ConfigInfo.API_POST_OPERATION_SEND, request, BaseResponse.class);
	}

	/**
	 * 拉取帖子列表
	 * 
	 * @author LiuYuHang
	 * @date 2014年9月23日
	 *
	 */
	public void postOperationPull(Context context, String type, int page, RequestListener listener) {
		PostOperationPullRequest request = new PostOperationPullRequest();
		request.picWidth = String.valueOf(context.getResources().getDisplayMetrics().widthPixels);
		request.listType = type;
		request.tag = page;

		request.paging = new PageOption();
		request.paging.setLimit(20);
		request.paging.setOffset(page);

		// 获取全部列表和获取其他列表接口不同
		String api = type == POST_TYPE_ALL ? ConfigInfo.API_POST_OPERATION_LIST_ALL : ConfigInfo.API_POST_OPERATION_LIST_OTHER;
		// getDataByPost(context, api, request,
		// SisterGroupPostListResponse.class);

		// -------------------------------------------------------------
		// 修改人：时培飞 修改时间:2014-12-11
		RequestOptions requestOptions = new RequestOptions();
		// 设为可以缓存数据
		requestOptions.setCacheQuestOption(true);
		// 设置请求缓存超时时间
		requestOptions.timeOut = CacheConstant.CACHE_TIME_OUT;

		httpProcessor.cancel();
		httpProcessor.doPost(api, request, SisterGroupPostListResponse.class, requestOptions, listener);
	}

	/**
	 * 获取精华贴列表
	 */
	public void getPopPostList(Context context, int pageSize, int currentPage) {
		getPopPostList(context, pageSize, currentPage, false);
	}

	/**
	 * 获取精华贴列表
	 */
	public void getPopPostList(Context context, int pageSize, int currentPage, boolean isRefresh) {
		PageOption pageOption = new PageOption();
		pageOption.setLimit(pageSize);
		pageOption.setOffset(currentPage);
		PopPostListRequest popPostListRequest = new PopPostListRequest();
		popPostListRequest.setPicWidth(ScreenInfoUtil.getScreenWidth((Activity) context));
		popPostListRequest.tag = currentPage;
		popPostListRequest.setPaging(pageOption);

		RequestOptions requestOptions = new RequestOptions();
		requestOptions.noNetToast=false;
		if (currentPage == 0 && !isRefresh) {
			requestOptions.setCacheQuestOption(true);
		}
		getDataByPost(context, ConfigInfo.API_POP_POST_LIST, popPostListRequest, requestOptions, AppHomeResponse.class);
	}
	
	
	
}
