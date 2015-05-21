package com.ipassistat.ipa.bean.response.entity;
/**
 * 排序列表实体类
 * @author wr
 *
 */
public class Sort {
	private String name;//排序方式名称
	private String id;//排序方式ID
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
