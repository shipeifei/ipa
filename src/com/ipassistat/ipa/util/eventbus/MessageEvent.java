/*
 * UpdateNickNameEvent.java [V 1.0.0]
 * classes : com.ichsy.eventbus.message.UpdateNickNameEvent
 * 时培飞 Create at 2015-4-8 上午11:24:14
 */
package com.ipassistat.ipa.util.eventbus;


/**
 * EventBus消息实体类
 * 
 * @author 时培飞 Create at 2015-4-8 上午11:24:14
 */
public class MessageEvent  extends BaseEvent {
	private Object mMsg;
	
	private String mKey;

	/**
	 * 
	 * @param msg 要传送的信息
	 * @param key  传送消息的key 
	 */
	public MessageEvent(String key,Object msg) {
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
