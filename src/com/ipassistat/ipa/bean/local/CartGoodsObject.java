package com.ipassistat.ipa.bean.local;

import java.io.Serializable;
import java.util.List;

/**
 * 本地购物车对象
 * 
 * @author Administrator
 *
 */
public class CartGoodsObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<GoodsEntity> goodsList;

	/**
	 * 根据userCode保存购物车
	 */
	private String userCode;
	private boolean serverEmpty;

	public List<GoodsEntity> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsEntity> goodsList) {
		this.goodsList = goodsList;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public boolean isServerEmpty() {
		return serverEmpty;
	}

	public void setServerEmpty(boolean serverEmpty) {
		this.serverEmpty = serverEmpty;
	}

}
