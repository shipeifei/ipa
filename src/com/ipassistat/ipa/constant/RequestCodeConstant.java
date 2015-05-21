package com.ipassistat.ipa.constant;

/**
 * @Discription： 页面见跳转的请求码
 * @package： com.ichsy.mboss.constant.RequestCodeConstant
 * @author： MaoYaNan
 * @date：2014-10-13 上午11:17:20
 */
public class RequestCodeConstant {

	public static final int CHOOSESEX = 20001; // 选择性别
	//public static final int CHOOSESKIN = 20002; // 选择皮肤
	public static final int CHOOSCITY = 20003; 
	public static final int CHOOSEADDR = 20004; // 选择地址
	/***
	 * 调用系统相机
	 */
	public static final int CAMERA = 20005;
	/***
	 * 调用系统相册
	 */
	public static final int PICTURE = 20006; 
	/***
	 * 调用图片剪切界面
	 */
	public static final int PHOTO_OPERATE = 20007;
	public static final int USERINFO = 20008; // 地址选择返回的resultCode
	public static final int STARTEDITPOSTACTIVITY = 20009; // 去写试用报告

	public static final int ADDRESS_SELECT_CODE = 1001; // 地址选择返回的resultCode

	/**
	 * 正常登录（个人中心页面登录）
	 */
	public static final int USER_CENTER = 3000;
	/**
	 * 试用详情跳转到登录界面
	 */
	public static final int TRIAL_INFO = 3001;
	/*
	 * 从设置中心修改密码的时候 ，没有登录，跳到登录页面。
	 */
	public static final int MODIFY_PWD = 3002;

	public static final int RESET_PWD = 3003;

}
