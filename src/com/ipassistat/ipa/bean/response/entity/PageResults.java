package com.ipassistat.ipa.bean.response.entity;
/**
 * 翻页结果实体类
 * @author wr
 *
 */
public class PageResults {
	private int total;//总数量
	private int more;//是否还有更多 0代表没有了   1代表有
	private int count;//返回数量
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getMore() {
		return more;
	}
	public void setMore(int more) {
		this.more = more;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}
