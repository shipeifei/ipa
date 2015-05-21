package com.ipassistat.ipa.bean.request;

/**
 * @Discription： 删除收藏商品
 * @package： com.ichsy.mboss.bean.request.DeleteCollectionRequest
 * @author： MaoYaNan
 * @date：2014-10-15 下午8:00:17
 */
public class DeleteCollectionRequest extends BaseRequest {
	private String ids; // 删除的商品sku编码，中间以逗号分隔
	private String isAll; // 是否是删除全部数据

	public String getIsAll() {
		return isAll;
	}

	public void setIsAll(String isAll) {
		this.isAll = isAll;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
