package com.ipassistat.ipa.bean.request;

/***
 *  联系人请求实体
 * @author shipeifei
 *
 */
public class ContactRequest extends BaseRequest {

	private String userid;
	private String channelid;
	private String contacts;
	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * @return the channelid
	 */
	public String getChannelid() {
		return channelid;
	}
	/**
	 * @param channelid the channelid to set
	 */
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	/**
	 * @return the contacts
	 */
	public String getContacts() {
		return contacts;
	}
	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

}
