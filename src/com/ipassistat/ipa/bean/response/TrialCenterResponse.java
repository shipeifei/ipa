package com.ipassistat.ipa.bean.response;

import java.util.ArrayList;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.TryOutGood;

/**
 * @Discription： 试用中心列表的返回数据
 * @package： com.ichsy.mboss.bean.response.TrialCenterResponse
 * @author： MaoYaNan
 * @date：2014-9-28 上午9:52:15
 */
public class TrialCenterResponse extends BaseResponse {
	private ArrayList<TryOutGood> tryOutGoods;
	private PageResults paged;

	public PageResults getPaged() {
		return paged;
	}

	public void setPaged(PageResults paged) {
		this.paged = paged;
	}

	public ArrayList<TryOutGood> getTryOutGoods() {
		return tryOutGoods;
	}

	public void setTryOutGoods(ArrayList<TryOutGood> tryOutGoods) {
		this.tryOutGoods = tryOutGoods;
	}

}
