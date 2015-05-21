/*
 * BaseEvent.java [V 1.0.0]
 * classes : com.ichsy.eventbus.message.BaseEvent
 * 时培飞 Create at 2015-4-8 下午1:46:28
 */
package com.ipassistat.ipa.util.eventbus;

/**
 * EventBus的基类
 * com.ichsy.eventbus.message.BaseEvent
 * @author 时培飞 
 * Create at 2015-4-8 下午1:46:28
 */
public class BaseEvent {
  
	/***
	 * 消息发送的目标，用来处理同一个Activity多个消息
	 */
	public String target;
	
}
