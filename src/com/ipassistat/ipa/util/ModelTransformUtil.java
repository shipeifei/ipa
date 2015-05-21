package com.ipassistat.ipa.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ipassistat.ipa.bean.local.GoodsEntity;
import com.ipassistat.ipa.bean.request.entity.GoodsInfoForAdd;
import com.ipassistat.ipa.bean.request.entity.PurchaseGoods;
import com.ipassistat.ipa.bean.response.GoodsDetailResponse;
import com.ipassistat.ipa.bean.response.GoodsPriceResponse;
import com.ipassistat.ipa.bean.response.TimeScaredBuyGoodInfoResponse;
import com.ipassistat.ipa.bean.response.entity.GoodsInfoForQuery;
import com.ipassistat.ipa.constant.Constant;

/**
 * 实体类对象转换的工具类
 * 
 * @author LiuYuHang
 * @date 2014年11月18日
 */
public class ModelTransformUtil {

	/**
	 * 普通商品类型转换为本地购物车商品类型
	 * @param goodsdetailObject 商品详情接口返回结果对象
	 * @param goodspriceObject 商品价格接口返回结果对象
	 * @return
	 */
	public static GoodsEntity parser(GoodsDetailResponse goodsdetailObject,GoodsPriceResponse goodspriceObject) {
		GoodsEntity goodsEntity = new GoodsEntity();
		goodsEntity.setProduct_name(goodsdetailObject.getName());// 商品名称
		goodsEntity.setSku_num(1);// 商品数量
		goodsEntity.setSku_code(goodsdetailObject.getSku_code());// sku编码
		goodsEntity.setProduct_price(goodspriceObject.getNewPrice());// 商品价格
		goodsEntity.setProduct_type(Constant.GOODS_TYPE_NORMAL);// 商品类型
		goodsEntity.setSku_changenum(1);// 变化数量
		goodsEntity.setProduct_picurl(goodsdetailObject.getPhotos()
				.get(0).getPicNewUrl());// 商品图片
		goodsEntity.setFlag_product(Constant.GOODS_VALID);//商品是否有效的状态
		if (!goodsdetailObject.getStore_num().isEmpty()) {
			int store_num = Integer.parseInt(goodsdetailObject
					.getStore_num());
			goodsEntity.setSku_stock(store_num);// 商品库存剩余件数
		}
		// 库存是否足够的状态
		if (goodsdetailObject.getStore_num()
				.equals(Constant.STOCK_LACK)) {
			goodsEntity.setFlag_stock(Constant.STOCK_LACK);
		} else {
			goodsEntity.setFlag_stock(Constant.STOCK_FULL);
		}

		return goodsEntity;
	}

	/**
	 * 限购商品类型转换为本地购物车商品类型
	 * @param timescaredbuyObject 限时抢购详情接口返回结果对象
	 * @return
	 */
	public static GoodsEntity parser(TimeScaredBuyGoodInfoResponse timescaredbuyObject){
		GoodsEntity goodsEntity = new GoodsEntity();
		goodsEntity.setProduct_name(timescaredbuyObject.getName());// 商品名称
//		goodsEntity.setSku_num(1);// 商品数量
		goodsEntity.setSku_code(timescaredbuyObject.getSku_code());// sku编码
		goodsEntity.setProduct_price(timescaredbuyObject.getNewPrice());// 商品价格
		goodsEntity.setProduct_type(Constant.GOODS_TYPE_LIMIT);// 商品类型
		goodsEntity.setProduct_picurl(timescaredbuyObject.getPhotos()
				.get(0).getPicNewUrl());// 商品图片
		goodsEntity.setFlag_product(Constant.GOODS_VALID);//商品是否有效的状态
		if (!timescaredbuyObject.getRemaind_count().isEmpty()) {
			int store_num = Integer.parseInt(timescaredbuyObject
					.getRemaind_count());
			goodsEntity.setSku_stock(store_num);// 商品库存剩余件数
		}
		// 库存是否足够的状态
		if (timescaredbuyObject.getRemaind_count().equals(
				Constant.STOCK_LACK)) {
			goodsEntity.setFlag_stock(Constant.STOCK_LACK);
		} else {
			goodsEntity.setFlag_stock(Constant.STOCK_FULL);
		}
		return goodsEntity;

	}


	/**
	 * 本地购物车商品类型转换为同步购物车接口所需商品类型
	 * @param entitylist 本地购物车商品列表
	 * @return
	 */
	public static List<GoodsInfoForAdd> parser(List<GoodsEntity> entitylist){
		List<GoodsInfoForAdd> addlist = new ArrayList<GoodsInfoForAdd>();//将本地商品类型转换为需要上传的商品类型
		for (int i = 0; i < entitylist.size(); i++) {
			GoodsInfoForAdd addgoods = new GoodsInfoForAdd();
			addgoods.setSku_num(entitylist.get(i).getSku_num());//商品数量
			addgoods.setArea_code("");//区域编号 不用传
			addgoods.setProduct_code("");//商品编码 不用传
			addgoods.setSku_code(entitylist.get(i).getSku_code());//sku编码
			addlist.add(addgoods);
		}
		return addlist;

	}

	/**
	 * 网络购物车商品类型转换为本地购物车商品类型
	 * @param context
	 * @param querylist 网络购物车商品列表
	 * @return
	 */
	public static List<GoodsEntity> parser(Context context,List<GoodsInfoForQuery> querylist){
		List<GoodsEntity> list = new ArrayList<GoodsEntity>();//将得到的网络购物车数据类型转换为本地购物车数据类型
		for(int i = 0 ; i < querylist.size();i++){
			GoodsEntity entity = new GoodsEntity();
			entity.setSku_code(querylist.get(i).getSku_code());//sku编码
			entity.setProduct_name(querylist.get(i).getSku_name());//商品名称
			entity.setProduct_price(querylist.get(i).getSku_price()+"");//商品价格
			entity.setFlag_stock(querylist.get(i).getFlag_stock());//库存是否足够
			entity.setSku_stock(querylist.get(i).getSku_stock());//库存剩余数量			
			entity.setProduct_picurl(querylist.get(i).getPic_url());//商品图片
			entity.setFlag_product(querylist.get(i).getFlag_product());//商品是否有效
			//商品类型
			if(querylist.get(i).getSales_type().equals("闪购")){
				entity.setProduct_type(Constant.GOODS_TYPE_LIMIT);//限时抢购
				entity.setSku_num(1);//商品数量
			}else{
				entity.setProduct_type(Constant.GOODS_TYPE_NORMAL);//普通商品
				entity.setSku_num(querylist.get(i).getSku_num());//商品数量
			}
			list.add(entity);
		}
		return list;
	}

	/**
	 * 购物车 将本地商品类型转为预结算接口所需商品类型
	 * @param selectlist 选中的商品集合
	 * @return
	 */
	public static List<PurchaseGoods> parserPurchase(List<GoodsEntity> selectlist){
		List<PurchaseGoods> prepaylist = new ArrayList<PurchaseGoods>();
		for(int i = 0 ;i < selectlist.size();i++){
			PurchaseGoods prepay_goods = new PurchaseGoods();
			prepay_goods.setOrder_count(selectlist.get(i).getSku_num()+"");
			prepay_goods.setSku_code(selectlist.get(i).getSku_code());
			prepaylist.add(prepay_goods);
		}
		return prepaylist;
		
	}
	
	
	/**
	 * 购物车 将本地商品类型转为确认订单页所需商品类型
	 * @param selectlist 选中的商品集合
	 * @return
	 */
	public static ArrayList<PurchaseGoods> parserTocount(List<GoodsEntity> selectlist){
		ArrayList<PurchaseGoods> tocountlist = new ArrayList<PurchaseGoods>();
		for(int i = 0 ;i < selectlist.size();i++){
			PurchaseGoods tocount_goods = new PurchaseGoods();
			tocount_goods.setOrder_count(selectlist.get(i).getSku_num()+"");
			tocount_goods.setSku_code(selectlist.get(i).getSku_code());
			tocount_goods.setGoods_name(selectlist.get(i).getProduct_name());
			tocount_goods.setPic_url(selectlist.get(i).getProduct_picurl());
			tocount_goods.setPrice(selectlist.get(i).getProduct_price());
			tocount_goods.setProduct_type(selectlist.get(i).getProduct_type());
			tocountlist.add(tocount_goods);
		}
		return tocountlist;
	}
	
	/**
	 * 商品详情页  普通商品类型转为预结算接口所需商品类型
	 * @param goodsdetailObject 商品详情接口返回结果对象
	 * @return
	 */
	public static PurchaseGoods parserPurchase(GoodsDetailResponse goodsdetailObject){
		PurchaseGoods prepay_goods = new PurchaseGoods();
		prepay_goods.setOrder_count("1");// 商品数量
		prepay_goods.setSku_code(goodsdetailObject.getSku_code());// 商品编号
		return prepay_goods;
	}
	
	/**
	 * 商品详情页  限购商品类型转为预结算接口所需商品类型
	 * @param goodsdetailObject 限购详情接口返回结果对象
	 * @return
	 */
	public static PurchaseGoods parserPurchase(TimeScaredBuyGoodInfoResponse timescaredbuyObject){
		PurchaseGoods prepay_goods = new PurchaseGoods();
		prepay_goods.setOrder_count("1");// 商品数量
		prepay_goods.setSku_code(timescaredbuyObject.getSku_code());// 商品编号
		return prepay_goods;
	}
	
	/**
	 * 商品详情页  普通商品类型转为确认订单页所需商品类型
	 * @param goodsdetailObject 商品详情接口返回结果对象
	 * @param goodspriceObject 商品价格接口返回结果对象
	 * @return
	 */
	public static PurchaseGoods parserBuynow(GoodsDetailResponse goodsdetailObject,GoodsPriceResponse goodspriceObject){
		PurchaseGoods buynow_goods = new PurchaseGoods();
		buynow_goods.setGoods_name(goodsdetailObject.getName());// 商品名称
		buynow_goods.setOrder_count("1");// 商品数量
		buynow_goods.setSku_code(goodsdetailObject.getSku_code());// sku编号
		buynow_goods.setPrice(goodspriceObject.getNewPrice());// 商品价格
		buynow_goods.setPic_url(goodsdetailObject.getPhotos().get(0).getPicNewUrl());// 商品图片
		buynow_goods.setProduct_type(Constant.GOODS_TYPE_NORMAL);// 商品类型
		buynow_goods.setProduct_code(goodsdetailObject.getProduct_code());// 商品编号
		return buynow_goods;
	}
	
	
	/**
	 * 商品详情页  限购商品类型转为确认订单页所需商品类型
	 * @param timescaredbuyObject 限时抢购详情接口返回结果对象
	 * @return
	 */
	public static PurchaseGoods parserBuynow(TimeScaredBuyGoodInfoResponse timescaredbuyObject){
		PurchaseGoods buynow_goods = new PurchaseGoods();
		buynow_goods.setGoods_name(timescaredbuyObject.getName());// 商品名称
		buynow_goods.setOrder_count("1");// 商品数量
		buynow_goods.setSku_code(timescaredbuyObject.getSku_code());// 商品编号
		buynow_goods.setPrice(timescaredbuyObject.getNewPrice());// 商品价格
		buynow_goods.setPic_url(timescaredbuyObject.getPhotos().get(0).getPicNewUrl());// 商品图片
		buynow_goods.setProduct_type(Constant.GOODS_TYPE_LIMIT);// 商品类型
		buynow_goods.setProduct_code(timescaredbuyObject.getProduct_code());// 商品编号
		return buynow_goods;
	}
}
