package com.ipassistat.ipa.bean.response;

import java.util.List;

import com.ipassistat.ipa.bean.response.entity.ApiSellerOrderListResult;
import com.ipassistat.ipa.bean.response.entity.OrderList;

/**
 * 订单列表
 * 
 * @author renheng
 * 
 */
public class OrderListResponse extends BaseResponse{


	private List<ApiSellerOrderListResult>sellerOrderList; //	商品订单信息	
	private int nowPage; //	 int	当前页数	
	private int countPage; //	 int	总页数	
	
	public List<ApiSellerOrderListResult> getSellerOrderList() {
		return sellerOrderList;
	}
	public void setSellerOrderList(List<ApiSellerOrderListResult> sellerOrderList) {
		this.sellerOrderList = sellerOrderList;
	}
	public int getNowPage() {
		return nowPage;
	}
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	public int getCountPage() {
		return countPage;
	}
	public void setCountPage(int countPage) {
		this.countPage = countPage;
	}
	
	
	
}
