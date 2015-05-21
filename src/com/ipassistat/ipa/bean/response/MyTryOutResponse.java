package com.ipassistat.ipa.bean.response;

import java.util.ArrayList;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.TryOutGood;

/**
 * @Discription： 我的试用的返回数据
 * @package： com.ichsy.mboss.bean.response.MyTryOutResponse
 * @author： MaoYaNan
 * @date：2014-10-14 下午4:35:56
 */
public class MyTryOutResponse extends BaseResponse {
	private ArrayList<TryOutGood> tryOutGoods; // 试用商品LIST

	private PageResults paged; // 翻页结果

	public ArrayList<TryOutGood> getTryOutGoods() {
		return tryOutGoods;
	}

	public void setTryOutGoods(ArrayList<TryOutGood> tryOutGoods) {
		this.tryOutGoods = tryOutGoods;
	}

	public PageResults getPaged() {
		return paged;
	}

	public void setPaged(PageResults paged) {
		this.paged = paged;
	}
}
