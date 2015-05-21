package com.ipassistat.ipa.business;

import java.util.List;

import android.content.Context;

import com.ipassistat.ipa.bean.local.CartGoodsObject;
import com.ipassistat.ipa.bean.local.GoodsEntity;
import com.ipassistat.ipa.bean.local.LocalResponeResult;
import com.ipassistat.ipa.bean.local.RequestOptions;
import com.ipassistat.ipa.bean.request.BannerRequest;
import com.ipassistat.ipa.bean.request.CommentLabelRequest;
import com.ipassistat.ipa.bean.request.GoodsCommentAddRequest;
import com.ipassistat.ipa.bean.request.GoodsCommentRequest;
import com.ipassistat.ipa.bean.request.GoodsDetailRequest;
import com.ipassistat.ipa.bean.request.GoodsListRequest;
import com.ipassistat.ipa.bean.request.TimeLimitByGoodsListRequest;
import com.ipassistat.ipa.bean.request.entity.PageOption;
import com.ipassistat.ipa.bean.response.BannerResponse;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.CommentLabelResponse;
import com.ipassistat.ipa.bean.response.GoodsCommentResponse;
import com.ipassistat.ipa.bean.response.GoodsDetailResponse;
import com.ipassistat.ipa.bean.response.GoodsLabelResponse;
import com.ipassistat.ipa.bean.response.GoodsListResponse;
import com.ipassistat.ipa.bean.response.GoodsPriceResponse;
import com.ipassistat.ipa.bean.response.GoodsTypeResponse;
import com.ipassistat.ipa.bean.response.TimeLimitGoodsBuyListResponse;
import com.ipassistat.ipa.bean.response.TimeScaredBuyGoodInfoResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.util.SharedPreferenceUtil;

/**
 * 商品模块
 * 
 * @author lxc
 * 
 */
public class GoodsModule extends BaseModule {

	private static String CART_GOODS = "cart_goods";

	private static String CART_GOODS_SP = "cart_goods_sp";

	public GoodsModule(BusinessInterface dataCallBack) {
		super(dataCallBack);
	}

	/**
	 * 获得广告条列表
	 * 
	 * @param context
	 * @param column_code
	 *            栏目 id
	 */
	public void postBarnnerList(Context context, String column_code) {
		BannerRequest bannerRequest = new BannerRequest();
		bannerRequest.setColumn_code(column_code);
		getDataByPost(context, ConfigInfo.API_POST_HOME_BANNER_LIST, bannerRequest, BannerResponse.class);
	}

	/**
	 * @discretion: 请求限时抢购列表数据
	 * @author: MaoYaNan
	 * @date: 2014-9-26 上午10:42:13
	 * @param context
	 *            数据返回的界面
	 * @param offset
	 *            第几页
	 * @param width
	 */
	public void postTimeScaredBuyGoods(Context context, int offset, int width) {
		PageOption page = new PageOption();
		page.setLimit(10);
		page.setOffset(offset);

		TimeLimitByGoodsListRequest request = new TimeLimitByGoodsListRequest();
		request.setPaging(page); // 标记页数
		request.tag = offset;
		request.setPicWidth(context.getResources().getDisplayMetrics().widthPixels);

		getDataByPost(context, ConfigInfo.API_TIMESCAREDBUY, request, TimeLimitGoodsBuyListResponse.class);
	}

	/**
	 * 请求商品分类接口数据
	 * 
	 * @param context
	 */
	public void postGoodsTypeData(Context context) {
		getDataByPost(context, ConfigInfo.API_GOODS_TYPE, null, GoodsTypeResponse.class);
	}

	
	/**
	 * 请求商品列表接口数据
	 * 
	 * @param context
	 * @param offset
	 *            请求第几页数据
	 * @param limit
	 *            每页请求多少条数据
	 * @param category
	 *            分类信息
	 * @param sort
	 *            排序信息
	 */
	public void postGoodsListData(Context context, int offset, int limit, String category, String sort, Integer width) {
		postGoodsListData(context, offset, limit, category, sort, width,false);
	}
	public void postGoodsListData(Context context, int offset, int limit, String category, String sort, Integer width,boolean isRefresh) {
		PageOption pageoption = new PageOption();
		pageoption.setOffset(offset);
		pageoption.setLimit(limit);
		GoodsListRequest goodslistrequest = new GoodsListRequest();
		goodslistrequest.setPaging(pageoption);
		goodslistrequest.setCategory(category);
		goodslistrequest.setSort(sort);
		goodslistrequest.setPicWidth(width);
		goodslistrequest.tag = offset;
		RequestOptions requestOptions = new RequestOptions();
		if (offset == 0 && !isRefresh) {
			requestOptions.setCacheQuestOption(true);
		}else {
			requestOptions.setCacheQuestOption(false);
		}
		getDataByPost(context, ConfigInfo.API_GOODS_LIST, goodslistrequest,requestOptions,GoodsListResponse.class);
	}
	
	/**
	 * 请求商品详情接口数据
	 * 
	 * @param context
	 * @param sku_code
	 *            商品编号
	 * @param width
	 *            屏幕宽度
	 */
	public void postGoodsDetailData(Context context, String sku_code, String width) {
		GoodsDetailRequest goodsdetailrequest = new GoodsDetailRequest();
		goodsdetailrequest.setSku_code(sku_code);
		goodsdetailrequest.setWidth(width);
		
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = false;
		getDataByPost(context, ConfigInfo.API_GOODS_DETAIL, goodsdetailrequest,requestOptions,GoodsDetailResponse.class);
	}

	/**
	 * 请求商品价格接口数据
	 * 
	 * @param context
	 * @param sku_code
	 *            商品编号
	 */
	public void postGoodsPriceData(Context context, String sku_code) {
		GoodsDetailRequest goodsdetailrequest = new GoodsDetailRequest();
		goodsdetailrequest.setSku_code(sku_code);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = false;
		getDataByPost(context, ConfigInfo.API_GOODS_PRICE, goodsdetailrequest,requestOptions,GoodsPriceResponse.class);
	}

	/**
	 * 请求印象标签接口数居
	 * 
	 * @param context
	 * @param sku_code
	 *            商品编号
	 */
	public void postGoodsLabelData(Context context, String sku_code) {
		GoodsDetailRequest goodsdetailrequest = new GoodsDetailRequest();
		goodsdetailrequest.setSku_code(sku_code);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = false;
		getDataByPost(context, ConfigInfo.API_GOODS_LABEL, goodsdetailrequest,requestOptions, GoodsLabelResponse.class);
	}

	/**
	 * 请求商品评论列表接口数据
	 * 
	 * @param context
	 * @param offset
	 *            请求第几页数据
	 * @param limit
	 *            每页请求多少条数据
	 * @param sku_code
	 *            商品编号
	 */
	public void postGoodsCommentData(Context context, int offset, int limit, String sku_code,int picWidth) {
		PageOption pageoption = new PageOption();
		pageoption.setOffset(offset);
		pageoption.setLimit(limit);
		GoodsCommentRequest goodscommentrequest = new GoodsCommentRequest();
		goodscommentrequest.setSku_code(sku_code);
		goodscommentrequest.setPaging(pageoption);
		goodscommentrequest.setPicWidth(picWidth);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = false;
		getDataByPost(context, ConfigInfo.API_GOODS_COMMENT, goodscommentrequest,requestOptions,GoodsCommentResponse.class);
	}

	/**
	 * 请求商品收藏接口数据
	 * 
	 * @param context
	 * @param sku_code
	 *            商品编号
	 */
	public void postGoodsCollectionData(Context context, String sku_code) {
		GoodsDetailRequest goodsdetailrequest = new GoodsDetailRequest();
		goodsdetailrequest.setSku_code(sku_code);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = false;
		getDataByPost(context, ConfigInfo.API_GOODS_COLLECTION, goodsdetailrequest,requestOptions,BaseResponse.class);
	}

	/**
	 * 得到user_token
	 * 
	 * @param context
	 * @return
	 */
//	public static String getUserToken(Context context) {
//		// String userToken = SharedPreferenceUtil.getStringInfo(context,
//		// user_token_key);
//		String userToken = "6f7535b1dd6c44ddb64d9433b9ebc1ee258abc06b81445fd89eb7592d2d3b394";
//		return userToken;
//	}

	/**
	 * 得到评论标签
	 * 
	 * @param context
	 * @param skuCode
	 *            商品编号
	 */
	public void getCommentLabel(Context context, String skuCode) {
		CommentLabelRequest req = new CommentLabelRequest();
		req.setSku_code(skuCode);
		getDataByPost(context, ConfigInfo.API_COMMENT_LABEL, req, CommentLabelResponse.class);
	}

	/**
	 * 添加商品评价
	 * 
	 * @param context
	 * @param commCont
	 *            评价内容
	 * @param prodType
	 *            商品类型
	 * @param label
	 *            印象标签
	 * @param skuCode
	 *            //商品标号
	 */
	public void addGoodsComment(Context context, String commCont, String label, String skuCode, String orderCode, String post_img) {
		GoodsCommentAddRequest req = new GoodsCommentAddRequest();
		req.setComment_content(commCont);
		req.setLabel(label);
		// req.setProduct_type(prodType);
		req.setSku_code(skuCode);
		req.setOrder_code(orderCode);
		req.setPost_img(post_img);
		getDataByPost(context, ConfigInfo.API_GOODS_COMMENT_ADD, req, BaseResponse.class);
	}

	/**
	 * @discretion: 限时抢购详情
	 * @author: MaoYaNan
	 * @date: 2014-9-30 上午10:56:34
	 * @param context
	 * @param sku_code
	 *            商品编号
	 * @param width
	 *            屏幕宽度
	 */
	public void postTimeScaredBuyGoodsInfo(Context context, String sku_code, String width) {
		GoodsDetailRequest request = new GoodsDetailRequest();
		request.setSku_code(sku_code);
		request.setWidth(width);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = false;
		getDataByPost(context, ConfigInfo.API_TIMESCAREDBUYGOODINFO, request,requestOptions,TimeScaredBuyGoodInfoResponse.class);
	}

	/**
	 * 保存本地购物车
	 * 
	 * @param context
	 * @param goods
	 *            sku_num为变化数量，增加的时候为正，减少的时候为负
	 * @param goods
	 *            待加入购物车的商品对象
	 * @param sku_num
	 *            变化数量
	 */
//	public LocalResponeResult addCartGoodsObject(Context context, GoodsEntity goods) {
//		LocalResponeResult localResponeResult = new LocalResponeResult();
//		CartGoodsObject cartGoodsObject = getCartGoodsObject(context);
//		if (cartGoodsObject == null) {
//			List<GoodsEntity> list = new ArrayList<GoodsEntity>();
//			if (null != goods) {
//				list.add(goods);
//			}
//			cartGoodsObject = new CartGoodsObject();
//			cartGoodsObject.setGoodsList(list);
//		} else {
//			boolean isAdded = false;
//			for (GoodsEntity entity : cartGoodsObject.getGoodsList()) {
//				if (entity.getSku_code().equals(goods.getSku_code())) {
//					if (goods.getProduct_type().equals(Constant.GOODS_TYPE_LIMIT)) {
//						if (entity.getSku_num() != 0) {
//							if (goods.getSku_changenum() > 0) {
//								localResponeResult.setStatus(0);
//								localResponeResult.setError("限购商品每单限购一件哦");
//								return localResponeResult;
//							}
//						}
//					}
//					entity.setSku_num(entity.getSku_num() + goods.getSku_changenum());
//					isAdded = true;
//				}
//			}
//			if (!isAdded) {
//				cartGoodsObject.getGoodsList().add(goods);
//			}
//		}
//		try {
//			boolean isSucess = SharedPreferenceUtil.saveObject(context, CART_GOODS, CART_GOODS_SP, cartGoodsObject);
//			if (isSucess) {
//				localResponeResult.setStatus(1);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return localResponeResult;
//	}

	/**
	 * 
	 * @param context
	 * @param addedGoodsList
	 *            (被添加商品实体类List，注意将SkuChangeNum字段赋值）
	 * @return
	 */
//	public static LocalResponeResult addCartGoodsObject(Context context, List<GoodsEntity> addedGoodsList) {
//		return addCartGoodsObject(context, addedGoodsList, "");
//	}
	
	public static void cleanCartGoodsObject(Context context) {
		SharedPreferenceUtil.clearObject(context, CART_GOODS_SP);
	}
	
//	public static void cleanTmepCartGoodsObject(Context context) {
//		addCartGoodsObject(context, new ArrayList<GoodsEntity>(), "");
//	}

	/**
	 * 
	 *
	 * @param context
	 * @param addedGoodsList
	 * @param userCode
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月19日
	 */
	public static LocalResponeResult addCartGoodsObject(Context context, List<GoodsEntity> addedGoodsList) {
		LocalResponeResult localResponeResult = new LocalResponeResult();
		CartGoodsObject cartGoodsObject = getCartGoodsObject(context);
		if (addedGoodsList == null) {
			localResponeResult.setStatus(0);
			localResponeResult.setError("empty");
			return localResponeResult;
		}
		if (cartGoodsObject == null) {
			cartGoodsObject = new CartGoodsObject();
		}
		cartGoodsObject.setGoodsList(addedGoodsList);
		try {
			boolean isSucess = SharedPreferenceUtil.saveObject(context, CART_GOODS, CART_GOODS_SP , cartGoodsObject);
			if (isSucess) {
				localResponeResult.setStatus(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localResponeResult;
		
//		if (cartGoodsObject == null) {
//			cartGoodsObject = new CartGoodsObject();
//			cartGoodsObject.setGoodsList(addedGoodsList);
//		} else {
//			for (GoodsEntity addedGoodsEntity : addedGoodsList) {
//				boolean isAdded = false;
//				for (GoodsEntity entity : cartGoodsObject.getGoodsList()) {
//					if (addedGoodsEntity.getSku_code().equals(entity.getSku_code())) {
//						if (addedGoodsEntity.getSku_num() != entity.getSku_num()) {
//							entity.setSku_num(entity.getSku_num() + addedGoodsEntity.getSku_changenum());
//						}
//						isAdded = true;
//					}
//				}
//				if (!isAdded) {
//					cartGoodsObject.getGoodsList().add(addedGoodsEntity);
//				}
//			}
//		}
//		try {
//			boolean isSucess = SharedPreferenceUtil.saveObject(context, CART_GOODS, CART_GOODS_SP + userCode, cartGoodsObject);
//			if (isSucess) {
//				localResponeResult.setStatus(1);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return localResponeResult;
	}

	/**
	 * 同步成功后，刷新本地购物车
	 * 
	 * @param context
	 * @param addedGoodsList
	 *            (被添加商品实体类List，注意将SkuChangeNum字段赋值）
	 * @return
	 */
//	public LocalResponeResult refreshCartGoodsObject(Context context, List<GoodsEntity> addedGoodsList) {
//		SharedPreferenceUtil.clearObject(context, CART_GOODS_SP);
//		LocalResponeResult localResponeResult = new LocalResponeResult();
//		try {
//			CartGoodsObject cartGoodsObject = new CartGoodsObject();
//			cartGoodsObject.setGoodsList(addedGoodsList);
//			boolean isSucess = SharedPreferenceUtil.saveObject(context, CART_GOODS, CART_GOODS_SP, cartGoodsObject);
//			if (isSucess) {
//				localResponeResult.setStatus(1);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return localResponeResult;
//	}

	/**
	 * 得到购物车对象
	 * 
	 * @param context
	 * @return
	 */
//	public static CartGoodsObject getCartGoodsObject(Context context) {
//		return getCartGoodsObject(context, "");
//	}

	public static CartGoodsObject getCartGoodsObject(Context context) {
		CartGoodsObject cartGoodsObject = null;
		try {
			cartGoodsObject = (CartGoodsObject) SharedPreferenceUtil.getObject(context, CART_GOODS, CART_GOODS_SP );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cartGoodsObject;
	}
}
