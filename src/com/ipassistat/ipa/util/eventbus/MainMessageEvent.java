/*
 * MainMessageEvent.java [V 1.0.0]
 * classes : com.ichsy.eventbus.message.MainMessageEvent
 * 时培飞 Create at 2015-4-14 下午5:45:54
 */
package com.ipassistat.ipa.util.eventbus;


/**
 * com.ichsy.eventbus.message.MainMessageEvent
 * @author 时培飞 
 * Create at 2015-4-14 下午5:45:54
 */
public class MainMessageEvent extends BaseEvent {
	private Object mMsg;
	
	private String mKey;

	/**
	 * 
	 * @param msg 要传送的信息
	 * @param key  传送消息的key 
	 */
	public MainMessageEvent(String key,Object msg) {
		// TODO Auto-generated constructor stub
		mMsg = msg;
		mKey=key;
	}

	public Object getMsg() {
		return mMsg;
	}

	/**
	 * @return the type
	 */
	public String getKey() {
		return mKey;
	}

	
}
