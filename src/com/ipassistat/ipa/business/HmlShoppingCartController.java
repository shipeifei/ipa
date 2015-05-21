package com.ipassistat.ipa.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;

import com.ipassistat.ipa.bean.local.CartGoodsObject;
import com.ipassistat.ipa.bean.local.GoodsEntity;
import com.ipassistat.ipa.bean.request.BaseRequest;
import com.ipassistat.ipa.bean.request.ShoppingcartSyncRequest;
import com.ipassistat.ipa.bean.request.entity.GoodsInfoForAdd;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.GoodsDetailResponse;
import com.ipassistat.ipa.bean.response.GoodsPriceResponse;
import com.ipassistat.ipa.bean.response.ShoppingcartSyncResponse;
import com.ipassistat.ipa.bean.response.TimeScaredBuyGoodInfoResponse;
import com.ipassistat.ipa.bean.response.entity.GoodsInfoForQuery;
import com.ipassistat.ipa.constant.Constant;

import com.ipassistat.ipa.util.ModelTransformUtil;

public class HmlShoppingCartController extends ShoppingCartController<GoodsEntity> {
	private static HmlShoppingCartController shoppingCartManager;

	/**
	 * 获取单例
	 * 
	 * @param context
	 * @param observer
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public static HmlShoppingCartController instance(Context context) {
		synchronized (HmlShoppingCartController.class) {
			if (shoppingCartManager == null) shoppingCartManager = new HmlShoppingCartController(context);
			return shoppingCartManager;
		}

	}

	private HmlShoppingCartController(Context context) {
		super(context);
	}

	/**
	 * 普通商品加入购物车
	 * 
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public boolean addProductToShopCart(GoodsDetailResponse goodsDetailResponse, GoodsPriceResponse goodspriceObject) {
		if (goodsDetailResponse == null || goodspriceObject == null) {
			//observerOnWarning("请等待数据加载完成才能加入购物车哦~");
			return false;
		}

		GoodsEntity goodsEntity = findGoods(goodsDetailResponse.getSku_code());
		if (goodsEntity == null) {// 购物车里面没有这个物品
			goodsEntity = ModelTransformUtil.parser(goodsDetailResponse, goodspriceObject);
			goodsEntity.setSku_num(0);
		}
		return addProductToShopCart(goodsEntity);
	}

	/**
	 * 限购商品加入购物车
	 * 
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public boolean addProductToShopCart(TimeScaredBuyGoodInfoResponse timeScaredBuyGoodInfoResponse) {
		if (timeScaredBuyGoodInfoResponse == null) {
			//observerOnWarning("请等待数据加载完成才能加入购物车哦~");
			return false;
		}

		GoodsEntity goodsEntity = findGoods(timeScaredBuyGoodInfoResponse.getSku_code());
		if (goodsEntity == null) {// 购物车里面没有这个物品
			goodsEntity = ModelTransformUtil.parser(timeScaredBuyGoodInfoResponse);
			goodsEntity.setSku_num(0);
		}
		return addProductToShopCart(goodsEntity);
	}

	/**
	 * 从缓存获取数据
	 * 
	 * @param context
	 * @return
	 */
	@Override
	protected List<GoodsEntity> onDataGetFromCache(Context context) {
		CartGoodsObject cartGoodsObject = GoodsModule.getCartGoodsObject(context);// 当前用户的购物车数据
		if (cartGoodsObject == null) { return new ArrayList<GoodsEntity>(); }
		return cartGoodsObject.getGoodsList();
	}

	/**
	 * 将数据存储到缓存
	 * 
	 * @param context
	 * @return
	 */
	@Override
	protected void onDataSave(Context context, List<GoodsEntity> list) {
		GoodsModule.addCartGoodsObject(context, list);
	}

	@Override
	protected void onLogout(Context context) {
		GoodsModule.cleanCartGoodsObject(context);// 当前购物车的缓存
	}

	@Override
	protected void onPostSyncPost(Context context, List<GoodsEntity> list, BaseRequest request, Class<? extends BaseResponse> cls) {
		// CartGoodsObject tempCartCache =
		// GoodsModule.getCartGoodsObject(context, "");// 临时用户（未登录用户的缓存）
		// if (tempCartCache != null && tempCartCache.getGoodsList() != null) {
		// list.addAll(tempCartCache.getGoodsList());
		// }// 获取未登录用户的数据，一起发送给服务器

		List<GoodsInfoForAdd> goodsList = ModelTransformUtil.parser(list);
		ShoppingcartSyncRequest shoppingcartSyncRequest = ShoppingCartModule.getAsyncShoppingCardRequestData(context, goodsList);

		super.onPostSyncPost(context, list, shoppingcartSyncRequest, ShoppingcartSyncResponse.class);
	}

	@Override
	protected List<GoodsEntity> onDataRequestSeccess(Context context, BaseResponse responseVo) {
		ShoppingcartSyncResponse syncresponse = (ShoppingcartSyncResponse) responseVo;
		List<GoodsInfoForQuery> querylist = syncresponse.getShoppingCartList();// 得到网络购物车数据集合

		// GoodsModule.cleanTmepCartGoodsObject(context);// 清除未登录用户的缓存
		return ModelTransformUtil.parser(context, querylist);
	}

	@Override
	protected boolean dataCompare(GoodsEntity object1, GoodsEntity object2) {
		return object1.getSku_code().equals(object2.getSku_code());
	}

	@Override
	protected int getCount(GoodsEntity object) {
		return object.getSku_num();
	}

	/**
	 * 计算购物车商品总数
	 */
	@Override
	protected int getAllCartCount(List<GoodsEntity> list) {
		int count = 0;
		Iterator<GoodsEntity> iterator = list.iterator();
		while (iterator.hasNext()) {
			GoodsEntity entry = iterator.next();
			if (!cartElementFilter(entry)) {
				count += entry.getSku_num();
			}
		}
		return count;
	}

	/**
	 * 判断该商品是否是有效数据（上架或下架）
	 */
	@Override
	protected boolean cartElementFilter(GoodsEntity object) {
		boolean isFilter = false;
		if (object.getFlag_product() != null && object.getFlag_product().equals(Constant.GOODS_IN_VALID)) {
			isFilter = true;
		}
		return isFilter;
	}

}
