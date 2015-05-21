package com.ipassistat.ipa.bean.request.entity;
/**
 * 翻页选项实体类
 * @author wr
 *
 */
public class PageOption {
	private int limit;//每页条数
	private int offset;//起码页号
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
}

