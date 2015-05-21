package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.ProductCategory;
import com.ipassistat.ipa.bean.response.entity.Sort;

/**
 * 商品分类返回结果
 * @author wr
 *
 */
public class GoodsTypeResponse extends BaseResponse{
	private List<ProductCategory> categories;//分类列表
	private List<Sort> sort;//排序列表

	public List<Sort> getSort() {
		return sort;
	}

	public void setSort(List<Sort> sort) {
		this.sort = sort;
	}

	public List<ProductCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<ProductCategory> categories) {
		this.categories = categories;
	}

}
