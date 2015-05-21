package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.PageResults;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;


/**
 * 商品列表返回结果
 * @author wr
 *
 */
public class GoodsListResponse extends BaseResponse{
	private PageResults paged;//翻页结果
	private String count;//该类别下商品总数
	private List<SaleProduct> products;//商品list
	
	public PageResults getPaged() {
		return paged;
	}
	public void setPaged(PageResults paged) {
		this.paged = paged;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public List<SaleProduct> getProducts() {
		return products;
	}
	public void setProducts(List<SaleProduct> products) {
		this.products = products;
	}

}
