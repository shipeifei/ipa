package com.ipassistat.ipa.bean.response.entity;

import java.io.Serializable;

/**
 * 
 * @Description 用户信息保存类
 * @author lis
 * @date 2015-2-9
 * 
 */
public class UserInfoSaveEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 用户账号
	 */
	private String mAccount = "";
	/**
	 * 用户Token
	 */
	private String mUserToken = "";
	/**
	 * 用户Code
	 */
	private String mMemberCode = "";
	/**
	 * 用户类
	 */
	private MemberInfo mInfo;

	// /**
	// * 皮肤类型
	// */
	// private String mSkintype = "";
	// /**
	// * 皮肤名
	// */
	// private String mSkinName = "";

	// /**
	// * @return the mSkintype
	// */
	// public String getmSkintype() {
	// return mSkintype;
	// }
	//
	// /**
	// * @param mSkintype
	// * the mSkintype to set
	// */
	// public void setmSkintype(String mSkintype) {
	// this.mSkintype = mSkintype;
	// }
	//
	// /**
	// * @return the mSkinName
	// */
	// public String getmSkinName() {
	// return mSkinName;
	// }
	//
	// /**
	// * @param mSkinName
	// * the mSkinName to set
	// */
	// public void setmSkinName(String mSkinName) {
	// this.mSkinName = mSkinName;
	// }

	/**
	 * @return the mAccount
	 */
	public String getmAccount() {
		return mAccount;
	}

	/**
	 * @param mAccount
	 *            the mAccount to set
	 */
	public void setmAccount(String mAccount) {
		this.mAccount = mAccount;
	}

	/**
	 * @return the mUserToken
	 */
	public String getmUserToken() {
		return mUserToken;
	}

	/**
	 * @param mUserToken
	 *            the mUserToken to set
	 */
	public void setmUserToken(String mUserToken) {
		this.mUserToken = mUserToken;
	}

	/**
	 * @return the mMemberCode
	 */
	public String getmMemberCode() {
		return mMemberCode;
	}

	/**
	 * @param mMemberCode
	 *            the mMemberCode to set
	 */
	public void setmMemberCode(String mMemberCode) {
		this.mMemberCode = mMemberCode;
	}

	/**
	 * @return the mInfo
	 */
	public MemberInfo getmInfo() {
		return mInfo;
	}

	/**
	 * @param mInfo
	 *            the mInfo to set
	 */
	public void setmInfo(MemberInfo mInfo) {
		this.mInfo = mInfo;
	}

}
