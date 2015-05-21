package com.ipassistat.ipa.business;

import java.util.List;

import android.content.Context;
import com.ipassistat.ipa.bean.local.RequestOptions;
import com.ipassistat.ipa.bean.request.ShoppingcartSyncRequest;
import com.ipassistat.ipa.bean.request.entity.GoodsInfoForAdd;
import com.ipassistat.ipa.bean.response.ShoppingcartSyncResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.httprequest.HttpRequestLisenter;

public class ShoppingCartModule extends BaseModule {

	public ShoppingCartModule(HttpRequestLisenter dataCallBack) {
		super(dataCallBack);
	}

	/**
	 * 请求购物车同步接口数据 (刷新时带提示的)
	 * 
	 * @param context
	 * @param goodsList
	 */
	public void postShopoingcartListWithToast(Context context, List<GoodsInfoForAdd> goodsList) {
		RequestOptions options = new RequestOptions();
		options.timeOutToast = true;
		options.errorToast = true;

		getDataByPost(context, ConfigInfo.API_SHOPPINGCART, getAsyncShoppingCardRequestData(context, goodsList), options, ShoppingcartSyncResponse.class);
	}

	public static ShoppingcartSyncRequest getAsyncShoppingCardRequestData(Context context, List<GoodsInfoForAdd> goodsList) {
		ShoppingcartSyncRequest shoppingcartRequest = new ShoppingcartSyncRequest();
		shoppingcartRequest.setGoodsList(goodsList);
		shoppingcartRequest.setBuyer_code("");
		//shoppingcartRequest.tag = UserModule.getUserMemberCode(context);

		return shoppingcartRequest;
	}

	/**
	 * 请求购物车同步接口数据
	 * 
	 * @param context
	 * @param goodsList
	 */
	public void postShopoingcartList(Context context, List<GoodsInfoForAdd> goodsList) {
		RequestOptions options = new RequestOptions();
		options.timeOutToast = false;
		options.errorToast = false;
		getDataByPost(context, ConfigInfo.API_SHOPPINGCART, getAsyncShoppingCardRequestData(context, goodsList), options, ShoppingcartSyncResponse.class);
	}


}
