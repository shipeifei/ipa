package com.ipassistat.ipa.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.ipassistat.ipa.bean.local.GoodsEntity;
import com.ipassistat.ipa.bean.request.BaseRequest;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.NetUtil;
import com.ipassistat.ipa.util.http.HttpContext;
import com.ipassistat.ipa.util.http.HttpProcessor;
import com.ipassistat.ipa.util.http.RequestListener;

/**
 * 购物车的管理工具类
 * 
 * @author LiuYuHang
 * @date 2014年11月1日
 */
public abstract class ShoppingCartController<T extends GoodsEntity> implements RequestListener {
	// private static ShoppingCartController shoppingCartManager;
	private final int CART_MAX_COUNT = 99;

	private List<ShopingCartObserver<T>> mObservers;
	private HttpProcessor mHttpProcessor;// 请求工具方法
	private Context context;
	private Handler handler;

	/**
	 * 存放当前购物车里面的缓存数据
	 */
	private List<T> mCartList;
	private boolean isServerNoData;// 服务器没有数据的标识，防止多次请求服务器
	private boolean isRequestRuning;// 当前是否正在请求服务器数据

	/**
	 * 从数据库获取数据的方法
	 * 
	 * @param context
	 * @return
	 */
	protected abstract List<T> onDataGetFromCache(Context context);

	/**
	 * 存储到数据库的方法
	 * 
	 * @param context
	 * @param list
	 */
	protected abstract void onDataSave(Context context, List<T> list);

	/**
	 * 从网络同步成功之后的方法
	 * 
	 * @param context
	 * @param responseVo
	 * @return
	 */
	protected abstract List<T> onDataRequestSeccess(Context context, BaseResponse responseVo);

	/**
	 * 比较商品是否是同一个
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月24日
	 */
	protected abstract boolean dataCompare(T object1, T object2);

	protected abstract int getCount(T object);

	/**
	 * 用户注销时候，需要清空当前的缓存数据，调用应该在清除token之前。
	 * 
	 * @param context
	 * @author LiuYuHang
	 * @date 2014年11月27日
	 */
	protected abstract void onLogout(Context context);

	/**
	 * 获取购物车所有商品总数
	 * 
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月18日
	 */
	protected abstract int getAllCartCount(List<T> list);

	/**
	 * 在获取并发送给广播注册者时候，需要过滤的元素
	 * 
	 * @param object
	 * @return 返回true表示需要过滤
	 * @author LiuYuHang
	 * @date 2014年12月1日
	 */
	protected abstract boolean cartElementFilter(T object);

	/**
	 * 获取单例
	 * 
	 * @param context
	 * @param observer
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	// public static ShoppingCartController instance(Context context) {
	// synchronized (ShoppingCartController.class) {
	// if (shoppingCartManager == null) shoppingCartManager = new
	// ShoppingCartController(context);
	// return shoppingCartManager;
	// }
	// }

	/**
	 * 构造器，保持只有一个单例
	 * 
	 * @param context
	 * @param observer
	 */
	public ShoppingCartController(Context context) {
		this.context = context.getApplicationContext();
		this.handler = new Handler();

		// 首次生成实例会做一次本地同步
		syncCartFromCache(null, 0);
		// syncCartFromSever(false);
	}

	/**
	 * 注册监听器，在{@link #onDestory()}的时候，调用
	 * {@link #unRegisterListener(ShopingCartObserver)}，首次注册监听器，会做一次本地同步
	 * 
	 * @param observer
	 * @author LiuYuHang
	 * @date 2014年11月17日
	 */
	public void registerListener(ShopingCartObserver<T> observer) {
		if (mObservers == null) {
			mObservers = new ArrayList<ShopingCartObserver<T>>();
		} else {
			unRegisterListener(observer);
		}
		mObservers.add(observer);

		isServerNoData = false;
		syncCartFromCache(null, 0);
	}

	/**
	 * 反注册监听器,在{@link #onDestory()}的时候，调用
	 * 
	 * @param observer
	 * @author LiuYuHang
	 * @date 2014年11月18日
	 */
	public void unRegisterListener(ShopingCartObserver<T> observer) {
		if (mObservers != null && mObservers.contains(observer)) {
			mObservers.remove(observer);
		}
	}

	/**
	 * 将商品更新到本地缓存（并网络同步）
	 * 
	 * @param object
	 * @param productType
	 * @author LiuYuHang
	 * @date 2014年11月18日
	 */
	boolean addProductToShopCart(T goodsEntity) {
		LogUtil.outLogDetail("将要加入购物车的商品sku_code：" + goodsEntity.getSku_code());

		int skuCount = getCount(goodsEntity);// 当前将要加入购物车的物品在购物车中的数量
		if (skuCount >= CART_MAX_COUNT) {
			observerOnWarning("亲，购物车中已有99件此商品，没办法买更多了哦~");
			return false;
		} else if (skuCount >= 1 && goodsEntity.getProduct_type().equals(Constant.GOODS_TYPE_LIMIT)) {
			observerOnWarning("限购商品每单限购一件哦");
			return false;
		}
		goodsEntity.setSku_num(skuCount + 1);
		goodsEntity.setSku_changenum(1);

		if (skuCount == 0) {
			T entity = findGoods(goodsEntity.getSku_code());
			if (entity == null)
				getCartCacheMap().add(0, goodsEntity);
		}

		syncCartFromCache(getCartCacheMap(), 1);
		syncCartFromSever(false);

		observerOnWarning("加入购物车成功！");
		return true;
	}

	/**
	 * 从购物车移除
	 * 
	 * @param salteProductId
	 * @author LiuYuHang
	 * @date 2014年11月17日
	 */
	public void removeFromCart(String skuCode) {
		T goodsEntity = findGoods(skuCode);
		if (goodsEntity == null) {
			observerOnWarning("购物车已经没有这件商品了！");
		} else {
			LogUtil.outLogDetail("购物车：removeFromCart:" + skuCode);
			int changeCount = getCount(goodsEntity);
			goodsEntity.setSku_num(0);

			syncCartFromCache(getCartCacheMap(), changeCount);
			syncCartFromSever(false);
			observerOnWarning("删除成功！");
		}
	}

	/**
	 * 更改购物车物品数量
	 * 
	 * @param skuCode
	 * @param changeCount
	 *            本次更改的数量，比如+1或者-1
	 * @author LiuYuHang
	 * @date 2014年11月18日
	 */
	public void changeCartProductCount(String skuCode, int changeCount) {
		T goodsEntity = findGoods(skuCode);
		if (goodsEntity == null) {
			observerOnWarning("购物车已经没有这件商品了！");
		} else if (changeCount != 0) {// 为0不更新
			int oldCount = getCount(goodsEntity);
			// if (oldCount <= 1 || oldCount >= 100) return;

			int nowCount = oldCount + changeCount;
			// if (nowCount > 99 && changeCount > 0) {
			// observerOnWarning("亲亲，没办法买更多了哟~");
			// nowCount = 99;
			// changeCount = 99 - oldCount;
			// } else if (nowCount < 1 && changeCount < 0) {
			// observerOnWarning("亲亲，至少买一件哦~");
			// nowCount = 1;
			// changeCount = oldCount - 1;
			// }
			updateCartProductCount(skuCode, nowCount);
		}

	}

	/**
	 * 更改购物车物品数量
	 * 
	 * @param skuCode
	 * @param changeCount
	 *            改变之后的数字，比如之前是50，直接改成20
	 * @author LiuYuHang
	 * @date 2014年11月18日
	 */
	public void updateCartProductCount(String skuCode, int newCount) {
		if (newCount > 99) {
			observerOnWarning("亲亲，没办法买更多了哟~");
			newCount = 99;
		} else if (newCount < 1) {
			observerOnWarning("亲亲，至少买一件哦~");
			newCount = 1;
		}

		T goodsEntity = findGoods(skuCode);
		if (goodsEntity != null) {
			int oldNumber = getCount(goodsEntity);
			if (oldNumber != newCount) {
				LogUtil.outLogDetail("购物车：skuCode:" + skuCode + " 更改数量为:" + newCount + " 之前数量为:" + oldNumber);
				goodsEntity.setSku_num(newCount);

				syncCartFromCache(getCartCacheMap(), 0);
				syncCartFromSever(false);
			} else {
				// LogUtil.outLogDetail("购物车：skuCode:" + skuCode +
				// " 不更改（数量相等）");
			}
		}

	}

	/**
	 * 和服务器同步，会cancel掉上一次的请求，重新发送一个新的同步请求
	 * 
	 * @param toast
	 *            是否有同步提示
	 * @author LiuYuHang
	 * @date 2014年11月18日
	 */
	public synchronized boolean syncCartFromSever(boolean toast) {
		if (UserModule.isLogin(context) && NetUtil.isNetworkConnected(context)) {// 登陆并且有网络，才进行网络更新
			LogUtil.outLogDetail("购物车：开始同步网络数据");
			cancelSyncRequest();

			isRequestRuning = true;
			handler.removeCallbacks(syncCartRunnable);// 先移除上一次的请求
			handler.postDelayed(syncCartRunnable, 1000);
			return true;
		} else {
			LogUtil.outLogDetail("购物车：本次不同步网络数据 (未登录或者没有网络)");
			observerOnChanged(0);
			return false;
		}
	}

	/**
	 * 取消本次网络同步的请求（请求还会发出去，只是不会通知到本地）
	 * 
	 * @author LiuYuHang
	 * @date 2014年11月28日
	 */
	private void cancelSyncRequest() {
		if (mHttpProcessor != null) {
			// 取消上次同步
			mHttpProcessor.cancel();
			isRequestRuning = false;
		}
	}

	protected void onPostSyncPost(Context context, List<T> list, BaseRequest request, Class<? extends BaseResponse> cls) {
		if (request == null) {
			throw new NullPointerException("必须重写 onPostSyncPost()方法，并且传入request");
		}

		mHttpProcessor = new HttpProcessor(context);
		mHttpProcessor.doPost(ConfigInfo.API_SHOPPINGCART, request, cls, this);
	}

	/**
	 * 优化请求网络的逻辑，请求网络会先停止上一次的网络
	 */
	private Runnable syncCartRunnable = new Runnable() {

		@Override
		public void run() {
			onPostSyncPost(context, getCartCacheMap(), null, null);
		}

	};

	/**
	 * 和本地数据同步， 不要在这里调用{@link #getCartCacheMap()}
	 * 
	 * @param list
	 *            如果为空，表示当前列表和本地缓存数据同步，如果不为空，表示当前列表和传入的list同步
	 * @param changeCount
	 *            本次同步，改变的数据
	 * @author LiuYuHang
	 * @date 2014年11月18日
	 */
	public synchronized void syncCartFromCache(List<T> list, int changeCount) {
		LogUtil.outLogDetail("购物车：开始同步缓存文件数据");
		if (list == null) {
			// CartGoodsObject cartGoodsObject =
			// GoodsModule.getCartGoodsObject(context,
			// UserModule.getUserMemberCode(context));
			// if (cartGoodsObject == null) {
			// mCartList = new ArrayList<GoodsEntity>();
			// } else {
			// mCartList = cartGoodsObject.getGoodsList();
			// }
			mCartList = onDataGetFromCache(context);
			LogUtil.outLogDetail("购物车：从缓存文件获取数据");
			
//			printList(mCartList);
		} else {
			// GoodsModule.addCartGoodsObject(context, list,
			// UserModule.getUserMemberCode(context));
			onDataSave(context, list);
			mCartList = list;
			LogUtil.outLogDetail("购物车：将数据同步到缓存文件");
			
//			printList(list);
		}

		if (mCartList.isEmpty() && !isServerNoData) {
			if (!isRequestRuning) {// 如果正在请求，就本次不需要再次发送请求了（一会关于请求回数据）
				syncCartFromSever(false);
			} else {
				LogUtil.outLogError("列表为空，但是正在同步数据，本次网络同步取消");
			}
		} else {
			observerOnChanged(changeCount);
		}
	}

	public synchronized void logout() {
		mCartList.clear();
		cancelSyncRequest();
		onLogout(context);
	}

	/**
	 * 打印调试信息
	 * 
	 * @param list
	 */
	void printList(List<T> list) {
		if (list == null) {
			LogUtil.outLogDetail("购物车：本次购物车为空 ");
			return;
		}
		LogUtil.outLogDetail("购物车：size " + list.size() + "同步本地缓存开始===========================================");
		for (T goodsEntity : list) {
			LogUtil.outLogDetail("购物车： sku_code:" + goodsEntity.getSku_code() + "  num:" + goodsEntity.getSku_num());
		}
		LogUtil.outLogDetail("购物车：同步本地缓存结束=================================================");
	}

	/**
	 * 购物车发生变化的时候会对所有注册的观察者发出通知
	 * 
	 * @param changedCount
	 *            本次发生改变的数据量 +1或者-1或者其他
	 * @author LiuYuHang
	 * @date 2014年11月18日
	 */
	void observerOnChanged(int changedCount) {
		if (mObservers != null && !mObservers.isEmpty()) {
			LogUtil.outLogDetail("购物车：observer(size:" + mObservers.size() + ") -> observerOnChanged");
			for (ShopingCartObserver<T> observer : mObservers) {
				observer.onShopCartCountChanged(getCartCacheMap(true), getAllCartCount(getCartCacheMap()), changedCount);
			}
		}
	}

	void observerOnWarning(String warn) {
		if (mObservers != null && !mObservers.isEmpty()) {
			// Warning只需要通知最后一个订阅监听器的观察者就可以
			mObservers.get(mObservers.size() - 1).onWarning(warn);

			// for (ShopingCartObserver observer : mObservers) {
			// observer.onWarning(warn);
			// }
		}
	}

	void obseverOnError(int code, String message) {
		if (mObservers != null && !mObservers.isEmpty()) {
			mObservers.get(mObservers.size() - 1).onError(code, message);
			// for (ShopingCartObserver<T> observer : mObservers) {
			// observer.onError(code, message);
			// }
		}
	}

	/**
	 * 获取购物车的统一公共方法
	 * 
	 * @param fliterEmpty
	 *            是否过滤掉数量为0的商品 true:过滤 false:不过滤
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月20日
	 */
	synchronized List<T> getCartCacheMap(boolean fliterEmpty) {
		if (mCartList != null) {
			if (fliterEmpty) {
				List<T> copyCartList = new ArrayList<T>(mCartList);
				Iterator<T> iterator = copyCartList.iterator();
				while (iterator.hasNext() && fliterEmpty) {
					T goodsEntity = (T) iterator.next();
					if (getCount(goodsEntity) == 0 || cartElementFilter(goodsEntity))
						iterator.remove();
				}
				return copyCartList;
			} else {
				return mCartList;
			}
		}
		LogUtil.outLogDetail("购物车：内存数据为空，正在从本地缓存同步数据 init");
		syncCartFromCache(null, 0);
		return mCartList;
	}

	private List<T> getCartCacheMap() {
		return getCartCacheMap(false);
	}

	/**
	 * 从本地购物车缓存列表找到GoodsEntity
	 * 
	 * @param skuCode
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月18日
	 */
	protected T findGoods(String skuCode) {
		for (T goodsVo : getCartCacheMap()) {
			if (goodsVo.getSku_code().equals(skuCode)) {
				return goodsVo;
			}
		}
		return null;
	}

	/**
	 * 获取购物车数量
	 * 
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月1日
	 */
	public int getShopCartCount() {
		return getCartCacheMap().size();
	}

	// /**
	// * 获取购物车所有商品总数
	// *
	// * @return
	// * @author LiuYuHang
	// * @date 2014年11月18日
	// */
	// public int getAllCartCount() {
	// int count = 0;
	// Iterator<T> iterator = getCartCacheMap().iterator();
	// while (iterator.hasNext()) {
	// T entry = iterator.next();
	// count += entry.getSku_num();
	// }
	// return count;
	// }

	// ---------------------请求网络的回调

	@Override
	public void onHttpRequestBegin(String url) {

	}

	@Override
	public void onHttpRequestSuccess(String url, HttpContext httpContext) {

	}

	@Override
	public void onHttpRequestFailed(String url, HttpContext httpContext) {

	}

	@Override
	public void onHttpRequestComplete(boolean success, String url, HttpContext httpContext) {
		if (url.equals(ConfigInfo.API_SHOPPINGCART)) { // 同步购物车接口
			isRequestRuning = false;
			if (httpContext.code == ConfigInfo.RESULT_OK && UserModule.isLogin(context)) {
				List<T> goodsEntities = onDataRequestSeccess(context, httpContext.responseVo);
				isServerNoData = goodsEntities.isEmpty();
				syncCartFromCache(goodsEntities, 0);
			} else {
				obseverOnError(httpContext.code, httpContext.message);
			}
		}
	}

	@Override
	public void onHttpRequestCancel(String url, HttpContext httpContext) {
		LogUtil.outLogDetail("**url:" + url + " request has been canceled");
	}
}
