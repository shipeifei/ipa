package com.ipassistat.ipa.business;

import com.ipassistat.ipa.bean.response.entity.MemberInfo;

import android.content.Context;

/**
 * 用户相关的操作
 * 
 * @author LiuYuHang
 * @date 2014年11月9日
 */
public class UserOptions {
	private Context context;

	public UserOptions(Context context) {
		this.context = context;
	}

	public void regist(String userName, String passWord) {

	}

	public void registAndLogin(String userName, String passWord) {
 
		login(userName, passWord);
	}

	/**
	 * 登陆
	 *
	 * @author LiuYuHang
	 * @date 2014年11月9日
	 */
	public void login(String userName, String passWord) {

	}

	/**
	 * 注销
	 *
	 * @author LiuYuHang
	 * @date 2014年11月9日
	 */
	public void logout() {

	}

	/**
	 * 判断用户是否已经登陆
	 *
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月9日
	 */
	public boolean isLogin() {
		return true;
	}

	/**
	 * 返回当前登陆用户的信息
	 *
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月9日
	 */
	public MemberInfo getUserInfo() {
		return null;
	}

	/**
	 * 获取姐妹圈 - 未读消息数量
	 *
	 * @return
	 * @author LiuYuHang
	 * @date 2014年11月9日
	 */
	public int getMessageCount() {
		return 0;
	}
}
