package com.ipassistat.ipa.bean.response.entity;

public class MemberOther {
	private String expires;//	 String	过期时间	
	private String name;//	 String	名称	
	private String app_key;//	 String	APPKEY	
	private String app_id;	 //String	APPID	
	private String type;//	 String	类型
	public String getExpires() {
		return expires;
	}
	public void setExpires(String expires) {
		this.expires = expires;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApp_key() {
		return app_key;
	}
	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
