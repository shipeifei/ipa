package com.ipassistat.ipa.business;

import android.content.Context;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.local.RequestOptions;
import com.ipassistat.ipa.bean.request.FreeApplyRequest;
import com.ipassistat.ipa.bean.request.ProductShareStatusRequest;
import com.ipassistat.ipa.bean.request.ProductSharedRequest;
import com.ipassistat.ipa.bean.request.TryOutCenterRequest;
import com.ipassistat.ipa.bean.request.TryOutGoodInfoRequest;
import com.ipassistat.ipa.bean.request.TryOutInfoRequest;
import com.ipassistat.ipa.bean.request.entity.PageOption;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.FreeTryOutInfoResponse;
import com.ipassistat.ipa.bean.response.PayTryOutInfoResponse;
import com.ipassistat.ipa.bean.response.ProductShareStatusResponse;
import com.ipassistat.ipa.bean.response.TrialCenterResponse;
import com.ipassistat.ipa.bean.response.TryOutGoodInfoResponse;
import com.ipassistat.ipa.bean.response.entity.BeautyAddress;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.util.share.um.ShareUM;

/**
 * 试用中心模块
 * 
 * @author lxc
 * 
 */
public class TiralCenterModule extends BaseModule {

	public TiralCenterModule(BusinessInterface dataCallBack) {
		super(dataCallBack);
	}

	/**
	 * @discretion: 试用中心数据请求
	 * @author: MaoYaNan
	 * @date: 2014-9-26 下午2:44:36
	 * @param context
	 * @param width
	 */
	public void postTryOutCenter(Context context, int offset, int width) {
		TryOutCenterRequest request = new TryOutCenterRequest();
		PageOption paging = new PageOption();
		paging.setLimit(20);
		paging.setOffset(offset);
		request.setPaging(paging);
		request.setPicWidth(width);
		getDataByPost(context, ConfigInfo.API_TRYOUTCENTER, request,
				TrialCenterResponse.class);
	}

	/**
	 * @discretion: 试用——商品详情
	 * @author: MaoYaNan
	 * @date: 2014-9-26 下午1:57:38
	 * @param context
	 *            数据返回的界面
	 * @param product_code
	 *            商品的编号
	 */
	public void postTryOutGoodInfo(Context context, String sku_code,
			String width, String endTime) {
		TryOutGoodInfoRequest request = new TryOutGoodInfoRequest();
		request.setWidth(width);
		request.setSku_code(sku_code);
		request.setEnd_time(endTime);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = false;
		getDataByPost(context, ConfigInfo.API_TRYOUTGOODINFO, request,
				requestOptions, TryOutGoodInfoResponse.class);
	}

	/**
	 * @discretion: 付邮试用——详情
	 * @author: MaoYaNan
	 * @date: 2014-9-26 下午2:17:15
	 * @param context
	 *            数据返回的界面
	 * @param sku_code
	 *            商品编号
	 * @param width
	 */
	public void postPayTryOutInfo(Context context, String sku_code,
			String end_time, int width) {
		TryOutInfoRequest request = new TryOutInfoRequest();
		request.setSku_code(sku_code);
		request.setEnd_time(end_time);
		request.setPicWidth(width);
		getDataByPost(context, ConfigInfo.API_PAYTRYOUTINFO, request,
				PayTryOutInfoResponse.class);
	}

	/**
	 * @discretion: 免费试用——详情
	 * @author: MaoYaNan
	 * @date: 2014-9-26 下午2:38:39
	 * @param context
	 *            数据返回的界面
	 * @param sku_code
	 *            商品编号
	 * @param width
	 */
	public void postFreeTryOutInfo(Context context, String sku_code,
			String end_time, int width) {
		TryOutInfoRequest request = new TryOutInfoRequest();
		request.setSku_code(sku_code);
		request.setEnd_time(end_time);
		request.setPicWidth(width);
		getDataByPost(context, ConfigInfo.API_FREETRYOUTINFO, request,
				FreeTryOutInfoResponse.class);
	}

	/**
	 * @discretion: 查看分享状态
	 * @author: MaoYaNan
	 * @date: 2014-9-28 下午5:23:21
	 * @param context
	 *            数据返回的界面
	 * @param sku_code
	 *            商品编号
	 */
	public void postSharaStatus(Context context, String sku_code,
			String end_time) {
		ProductShareStatusRequest request = new ProductShareStatusRequest();
		request.setSku_code(sku_code);
		request.setEnd_time(end_time);
		getDataByPost(context, ConfigInfo.API_PRODUCTSHARESTATUS, request,
				ProductShareStatusResponse.class);
	}

	/**
	 * @discretion: 上传分享状态到服务器
	 * @author: MaoYaNan
	 * @date: 2014-9-28 下午5:29:35
	 * @param context
	 *            数据返回的界面
	 * @param share_type
	 *            分享平台
	 * @param sku_code
	 *            商品编号
	 */
	public void postProductShare(Context context, String share_type,
			String sku_code, String end_time) {
		ProductSharedRequest request = new ProductSharedRequest();
		request.setSku_code(sku_code);
		request.setShare_type(share_type);
		request.setEnd_time(end_time);
		getDataByPost(context, ConfigInfo.API_PRODUCTSHARE, request,
				BaseResponse.class);
	}

	/**
	 * @discretion: 申请免费试用
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午7:19:11
	 */
	public void postFreeApply(Context context, String activityCode,
			String sku_code, String sku_name, BeautyAddress address,
			String end_time) {
		FreeApplyRequest request = new FreeApplyRequest();
		request.setActivityCode(activityCode); // 活动编号
		request.setAddress_code(address.getId()); // 收货地址Id
		request.setBuyer_mobile(address.getMobile()); // 收货人手机号
		request.setOrderSource(Constant.ORDER_SOURCE); // 订单来源
		request.setArea_code(address.getAreaCode()); // 收货人省市区
		request.setSku_name(sku_name); // 商品名称
		request.setBuyer_address(address.getProvinces() + address.getStreet()); // 收货人地址
		request.setPostCode(address.getPostcode()); // 邮政编码
		request.setBuyer_name(address.getName()); // 收货人姓名
		request.setSku_code(sku_code); // 商品ID
		request.setEnd_time(end_time);
		getDataByPost(context, ConfigInfo.API_TRYOUTAPPLYAPI, request,
				FreeTryOutInfoResponse.class);
	}

	/**
	 * @discretion: 免费试用的分享
	 * @author: MaoYaNan
	 * @date: 2014-10-24 下午5:30:05
	 * @param share
	 *            Umeng 对象
	 * @param name
	 *            商品名称
	 * @param picUrl
	 *            图片链接
	 * @param url
	 *            链接
	 */
	public static void shareTryOutFree(Context context, ShareUM share,
			String name, String picUrl, String targetUrl) {
		StringBuffer content = new StringBuffer();
		StringBuffer contentMsg = new StringBuffer();
		StringBuffer title = new StringBuffer();
		title.append(context.getResources().getString(
				R.string.tryout_share_title));
		content.append(context.getResources().getString(
				R.string.tryout_share_content_free_info_msg_begin));
		content.append(name);
		content.append(context.getResources().getString(
				R.string.tryout_share_content_free_info));
		contentMsg.append(context.getResources().getString(
				R.string.tryout_share_content_free_info_msg_begin));
		contentMsg.append(name);
		contentMsg.append(context.getResources().getString(
				R.string.tryout_share_content_free_info_msg_end));
		share.setShare(content.toString(), contentMsg.toString(),
				title.toString(), picUrl, targetUrl);
	}

	/**
	 * @discretion: 付邮试用的分享
	 * @author: MaoYaNan
	 * @date: 2014-10-24 下午5:30:05
	 * @param share
	 *            uMeng对象
	 * @param name
	 *            商品名称
	 * @param picUrl
	 *            图片链接
	 * @param url
	 *            链家
	 */
	public static void shareTryOutPostage(Context context, ShareUM share,
			String name, String picUrl, String targetUrl) {
		StringBuffer content = new StringBuffer();
		StringBuffer contentMsg = new StringBuffer();
		StringBuffer title = new StringBuffer();
		title.append(context.getResources().getString(
				R.string.tryout_share_title_Postage));
		//
		content.append(context.getResources().getString(
				R.string.tryout_share_content_postage_info_msg_begin));
		content.append(name);
		content.append(context.getResources().getString(
				R.string.tryout_share_content_postage_info_msg_end));
		//
		contentMsg.append(context.getResources().getString(
				R.string.tryout_share_content_postage_info_msg_begin));
		contentMsg.append(name);
		contentMsg.append(context.getResources().getString(
				R.string.tryout_share_content_postage_info_smsmsg_end));

		share.setShare(content.toString(), contentMsg.toString(),
				title.toString(), picUrl, targetUrl);
	}
}
