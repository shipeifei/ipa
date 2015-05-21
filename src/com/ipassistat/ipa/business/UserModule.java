package com.ipassistat.ipa.business;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.bean.local.RequestOptions;
import com.ipassistat.ipa.bean.request.AddressDefaultRequest;
import com.ipassistat.ipa.bean.request.BaseRequest;
import com.ipassistat.ipa.bean.request.CollectionRequest;
import com.ipassistat.ipa.bean.request.DeleteCollectionRequest;
import com.ipassistat.ipa.bean.request.ForgetPwdRequest;
import com.ipassistat.ipa.bean.request.LoginRequest;
import com.ipassistat.ipa.bean.request.MyTryRequest;
import com.ipassistat.ipa.bean.request.PwdModifyRequest;
import com.ipassistat.ipa.bean.request.RegisterRequest;
import com.ipassistat.ipa.bean.request.UserInfoRequest;
import com.ipassistat.ipa.bean.request.VerifyCodeCheckRequest;
import com.ipassistat.ipa.bean.request.VerifyCodeRequest;
import com.ipassistat.ipa.bean.request.entity.PageOption;
import com.ipassistat.ipa.bean.response.AddresssReceiveResponse;
import com.ipassistat.ipa.bean.response.BaseResponse;
import com.ipassistat.ipa.bean.response.CollectionResponse;
import com.ipassistat.ipa.bean.response.LoginResponse;
import com.ipassistat.ipa.bean.response.MyTryOutResponse;
import com.ipassistat.ipa.bean.response.RegisterResponse;
import com.ipassistat.ipa.bean.response.SkinExplainReponse;
import com.ipassistat.ipa.bean.response.SkinTypeResponse;
import com.ipassistat.ipa.bean.response.UpdateUserInfoResponse;
import com.ipassistat.ipa.bean.response.UserInfoResponse;
import com.ipassistat.ipa.bean.response.entity.BeautyAddress;
import com.ipassistat.ipa.bean.response.entity.MemberInfo;
import com.ipassistat.ipa.bean.response.entity.SaleProduct;
import com.ipassistat.ipa.bean.response.entity.UserInfoSaveEntity;
import com.ipassistat.ipa.constant.ConfigInfo;
import com.ipassistat.ipa.constant.Constant;
import com.ipassistat.ipa.dao.BusinessInterface;
import com.ipassistat.ipa.util.LogUtil;
import com.ipassistat.ipa.util.SharedPreferenceUtil;

/**
 * 用户中心模块
 * 
 * @author shipeifei
 * 
 */
public class UserModule extends BaseModule {
	private static String user_token_key = "user_token";
	private static String USER_MEMBER_CODE = "user_member_code";
	private static String USER_MEMBER_INFO = "member_info";
	private static String USER_ACCOUNT = "user_account";
	private static String USER_ADDRESS = "user_address";
	private static String RECEIVE_PUSH_STATUS = "receive_push_status";// 用户是否接受推送的状态

	/**
	 * 用户默认收货地址sp
	 */
	public static final String USER_DEFAULT_ADD_SP = "user_default_add_sp";

	public UserModule(BusinessInterface dataCallBack) {
		super(dataCallBack);
		// TODO Auto-generated constructor stub
	}

	public void saveUserAccount(Context context, String account) {
		SharedPreferenceUtil.putStringInfo(context, USER_ACCOUNT, account);
	}

	public static String getUserAccount(Context context) {
		return SharedPreferenceUtil.getStringInfo(context, USER_ACCOUNT);
	}

	/**
	 * 本地注销，清除本地相关信息
	 * 
	 * @param context
	 */
	public void logoutLocal(Context context) {
		clearShoppingcart(context);
		clearUserToken(context);
		clearUserDeafaultAdd(context);
		clearUserMemberCode(context);
		clearMemberInfo(context);
	}

	/**
	 * 存储user_token
	 * 
	 * @param context
	 * @param user_token
	 */
	public void saveUserToken(Context context, String user_token) {
		SharedPreferenceUtil.putStringInfo(context, user_token_key, user_token);
	}

	/**
	 * 得到user_token
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserToken(Context context) {
		String userToken = SharedPreferenceUtil.getStringInfo(context, user_token_key);
		// String userToken =
		// "6f7535b1dd6c44ddb64d9433b9ebc1ee258abc06b81445fd89eb7592d2d3b394";
		return userToken;
	}

	/**
	 * 存取用户编号
	 * 
	 * @param context
	 * @param userMemberCode
	 */
	public void saveUserMemberCode(Context context, String userMemberCode) {
		SharedPreferenceUtil.putStringInfo(context, USER_MEMBER_CODE, userMemberCode);
	}

	/**
	 * 得到用户编号
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserMemberCode(Context context) {
		return SharedPreferenceUtil.getStringInfo(context, USER_MEMBER_CODE);
	}

	/**
	 * 保存用户对象(不带Token)
	 * 
	 * @param context
	 * @param memberInfo
	 * @return
	 */
	public boolean saveMemberInfo(Context context, MemberInfo memberInfo) {
		boolean flag = false;
		try {
			flag = SharedPreferenceUtil.saveObject(context, USER_MEMBER_INFO, memberInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 保存用户对象
	 * 
	 * @param context
	 * @param memberInfo
	 * @return
	 */
	public boolean saveMemberInfo(Context context, MemberInfo memberInfo, String userKey) {
		boolean flag = false;
		try {
			flag = SharedPreferenceUtil.saveObject(context, userKey + USER_MEMBER_INFO, memberInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 清空用户对象
	 * 
	 * @param context
	 * @return
	 */
	public boolean clearMemberInfo(Context context) {
		return SharedPreferenceUtil.clearObject(context, USER_MEMBER_INFO);
	}

	/**
	 * 得到用户对象
	 * 
	 * @param context
	 * @return
	 */
	public static MemberInfo getMemberInfo(Context context) {
		MemberInfo memberInfo = null;
		try {
			memberInfo = (MemberInfo) SharedPreferenceUtil.getObject(context, USER_MEMBER_INFO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return memberInfo;
	}

	/**
	 * 得到用户对象
	 * 
	 * @param context
	 * @return
	 */
	public static MemberInfo getMemberInfo(Context context, String userKey) {
		MemberInfo memberInfo = null;
		try {
			memberInfo = (MemberInfo) SharedPreferenceUtil.getObject(context, userKey
					+ USER_MEMBER_INFO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return memberInfo;
	}

	/***
	 * @author shipf
	 * @param context
	 *            判断用户是否登录
	 * @return
	 */
	public static boolean isLogin(Context context) {
		String userToken = SharedPreferenceUtil.getStringInfo(context, user_token_key);
		return (!TextUtils.isEmpty(userToken));
	}

	public static boolean clearUserToken(Context context) {
		return SharedPreferenceUtil.clearStringInfo(context, user_token_key);
	}

	/**
	 * 清除用户编号
	 * 
	 * @param context
	 * @return
	 */
	public static boolean clearUserMemberCode(Context context) {
		return SharedPreferenceUtil.clearStringInfo(context, USER_MEMBER_CODE);
	}

	/**
	 * 清除购物车
	 * 
	 * @param context
	 */
	public void clearShoppingcart(Context context) {
		//HmlShoppingCartController.instance(context).logout();
	}

	/**
	 * 保存用户是否接收推送的状态
	 * 
	 * @param context
	 * @param isReceive
	 * @return
	 */
	public static void saveReceiveStatus(Context context, String isReceive) {
		SharedPreferenceUtil.putStringInfo(context, RECEIVE_PUSH_STATUS, isReceive);
	}

	/**
	 * 得到用户是否接受推送的状态
	 * 
	 * @param context
	 * @return
	 */
	public static String getReceiveStatus(Context context) {
		return SharedPreferenceUtil.getStringInfo(context, RECEIVE_PUSH_STATUS);
	}

	/**
	 * @discretion: 获取“我的收藏”界面的数据
	 * @author: MaoYaNan
	 * @date: 2014-9-25 下午4:57:08
	 * @param context
	 *            数据返回的页面
	 * @param offset
	 *            第几页
	 */
	public void postMyCollection(Context context, int offset, int width) {
		PageOption page = new PageOption();
		page.setLimit(10);
		page.setOffset(offset);
		CollectionRequest request = new CollectionRequest();
		request.setPaging(page);
		request.setPicWidth(width);
		request.tag = offset;
		getDataByPost(context, ConfigInfo.API_MYCOLLECTION, request, CollectionResponse.class);
	}

	/**
	 * @discretion: 获取个人中心的数据
	 * @author: MaoYaNan
	 * @date: 2014-9-25 下午5:13:13
	 * @param context
	 *            数据返回的页面
	 */
	public void postUserCenter(Context context) {
		BaseRequest request = new BaseRequest();
		getDataByPost(context, ConfigInfo.API_USERCENTER, request, UserInfoResponse.class);
	}

	/**
	 * @discretion: 个人资料的获取
	 * @author: MaoYaNan
	 * @date: 2014-9-25 下午5:21:29
	 * @param context
	 *            数据返回的页面
	 */
	public void postUserInfo(Context context) {
		BaseRequest request = new BaseRequest();
		getDataByPost(context, ConfigInfo.API_USERINFO_GET, request, UserInfoResponse.class);
	}

	/**
	 * @discretion: 个人资料的修改
	 * @author: MaoYaNan
	 * @date: 2014-9-25 下午5:32:43
	 * @update: 任恒
	 * @param context
	 *            数据返回的界面
	 * @param sexId
	 *            性别的编码
	 * @param birthday
	 *            生日
	 * @param cityCode
	 *            城市ID
	 * @param address
	 *            默认收货地址
	 * @param avator
	 *            头像 url
	 * @param nickname
	 *            昵称
	 */
	public void updateUserInfo(Context context, String sexId, String birthday, String cityCode,
			BeautyAddress address, String avator, String nickname, boolean toast) {
		UserInfoRequest request = new UserInfoRequest();

		if (!TextUtils.isEmpty(sexId)) {
			request.setSex(sexId);
		} else {
			request.setSex("");
		}
		if (address != null) {
			request.setAdress(address);
		}
		request.setArea_code(cityCode);
		request.setBirthday(birthday);
		request.setAvatar(avator);
		request.setNickname(nickname);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = toast;
		getDataByPost(context, ConfigInfo.API_USERINFO_UPDATA, request, requestOptions,
				UpdateUserInfoResponse.class);
	}

	public void updateUserInfo(Context context, String sexId, String birthday, String city,
			BeautyAddress address, String avator, String nickname) {
		updateUserInfo(context, sexId, birthday, city, address, avator, nickname, false);
	}

	public void register(Context context, String loginName, String nickName, String pwd,
			String verifyCode) {
		String apiName = "com_cmall_membercenter_api_UserReginster";
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setClient_source("app");
		registerRequest.setLogin_name(loginName);
		registerRequest.setNickname(nickName);
		registerRequest.setPassword(pwd);
		registerRequest.setVerify_code(verifyCode);
		getDataByPost(context, apiName, registerRequest, RegisterResponse.class);
	}

	public void login(Context context, String loginName, String pwd) {
		String apiName = ConfigInfo.API_LOGIN;
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setClient_source("app");
		loginRequest.setLogin_name(loginName);
		loginRequest.setPassword(pwd);
		getDataByPost(context, apiName, loginRequest, LoginResponse.class);
	}

	/**
	 * @discretion: 用户注销登录 ，在 “设置界面”
	 * @author: MaoYaNan
	 * @date: 2014-10-11 上午10:45:44
	 * @param context
	 *            返回数据的界面
	 */
	public void logout(Context context) {
		getDataByPost(context, ConfigInfo.API_LOGOUT, new BaseRequest(), BaseResponse.class);
	}

	/**
	 * 获取用户皮肤类型图片
	 * 
	 * @param context
	 * @param userskin
	 *            皮肤类型字符串
	 * @return
	 */
	public static int getUserSkin(Context context, String userskin) {
		int skinphoto = 0;
		if (userskin != null && userskin.equals("449746650001")) {
			skinphoto = R.drawable.skin_hunhe;
		} else if (userskin != null && userskin.equals("449746650002")) {
			skinphoto = R.drawable.skin_ganxing;
		} else if (userskin != null && userskin.equals("449746650003")) {
			skinphoto = R.drawable.skin_zhongxing;
		} else if (userskin != null && userskin.equals("449746650004")) {
			skinphoto = R.drawable.skin_youxing;
		} else if (userskin != null && userskin.equals("449746650005")) {
			skinphoto = R.drawable.skin_mingan;
		}
		return skinphoto;
	}

	/**
	 * 得到收货地址列表
	 */
	public void getAddressList(Context context) {
		BaseRequest req = new BaseRequest();
		getDataByPost(context, ConfigInfo.addrRecApiName, req, AddresssReceiveResponse.class);
	}

	/**
	 * @discretion: 请求我的收藏的数据
	 * @author: MaoYaNan
	 * @date: 2014-10-14 下午5:18:10
	 * @param context
	 * @param type
	 * @param page
	 * @param width
	 */
	public void postMyTry(Context context, String type, int page, int width) {
		PageOption pageOption = new PageOption();
		pageOption.setLimit(20);
		pageOption.setOffset(page);
		MyTryRequest request = new MyTryRequest();
		request.setPaging(pageOption);
		request.tag = page;
		request.setType(type);
		request.setPicWidth(width);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.errorToast = false;
		getDataByPost(context, ConfigInfo.API_MYTRYOUTCENTERAPI, request, requestOptions,
				MyTryOutResponse.class);
	}

	/**
	 * 重置密码（忘记密码后）
	 */
	public void resetPwd(Context context, String loginName, String password, String verify_code,
			String client_source) {
		ForgetPwdRequest forgetPwdRequest = new ForgetPwdRequest();
		forgetPwdRequest.setLogin_name(loginName);
		forgetPwdRequest.setPassword(password);
		forgetPwdRequest.setVerify_code(verify_code);
		forgetPwdRequest.setClient_source(client_source);
		getDataByPost(context, ConfigInfo.API_FORGET_PWD, forgetPwdRequest, BaseResponse.class);
	}

	/**
	 * 修改密码（设置页面的修改密码）
	 * 
	 * @param context
	 * @param old_password
	 * @param newPwd
	 */
	public void modifyPwd(Context context, String old_password, String newPwd) {
		PwdModifyRequest pwdModifyRequest = new PwdModifyRequest();
		pwdModifyRequest.setNew_password(newPwd);
		pwdModifyRequest.setOld_password(old_password);
		getDataByPost(context, ConfigInfo.API_MODIFY_PWD, pwdModifyRequest, BaseResponse.class);
	}

	/**
	 * @discretion: 删除我的收藏的数据
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午7:54:40
	 * @param context
	 * @param list
	 */
	public void deleteCollection(Context context) {
		DeleteCollectionRequest request = new DeleteCollectionRequest();
		request.setIsAll(Constant.DELETEALL);
		getDataByPost(context, ConfigInfo.API_FAVDELETEAPI, request, BaseResponse.class);
	}

	/**
	 * @discretion: 删除我的收藏的数据
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午7:54:40
	 * @param context
	 * @param list
	 */
	public void deleteCollection(Context context, List<SaleProduct> list) {
		DeleteCollectionRequest request = new DeleteCollectionRequest();
		StringBuffer buffer = new StringBuffer("");
		for (SaleProduct saleProduct : list) {
			buffer.append(saleProduct.getId() + ",");
		}
		request.setIds(buffer.toString());
		request.setIsAll(Constant.DELETEALL);
		getDataByPost(context, ConfigInfo.API_FAVDELETEAPI, request, BaseResponse.class);
	}

	// /**
	// * 从收货地址列表取出默认收货地址对象
	// *
	// * @param resp
	// * @return 如果没有 默认地址，则返回null
	// */
	// public BeautyAddress getDefaultAddr(AddresssReceiveResponse resp) {
	// List<BeautyAddress> list = resp.getAdress();
	//
	// for (int i = 0; i < list.size(); i++) {
	// BeautyAddress addr = list.get(i);
	// String str = addr.getIsdefault();
	// if ("1".equals(str)) {
	// return addr;
	// }
	// }
	// return null;
	// }
	/**
	 * 
	 * @discretion: 删除我的收藏的数据,单条数据
	 * @author: MaoYaNan
	 * @date: 2014-10-15 下午7:54:40
	 * @param context
	 * @param list
	 */
	public void deleteCollection(Context context, SaleProduct list) {
		DeleteCollectionRequest request = new DeleteCollectionRequest();
		request.setIds(list.getId());
		request.setIsAll(Constant.DELETEONE);
		getDataByPost(context, ConfigInfo.API_FAVDELETEAPI, request, BaseResponse.class);
	}

	/**
	 * 从收货地址列表取出默认收货地址对象
	 * 
	 * @param resp
	 * @return 如果没有 默认地址，则返回null
	 */
	public BeautyAddress getDefaultAddr(AddresssReceiveResponse resp) {
		List<BeautyAddress> list = resp.getAdress();

		for (int i = 0; i < list.size(); i++) {
			BeautyAddress addr = list.get(i);
			String str = addr.getIsdefault();
			if ("1".equals(str)) {
				return addr;
			}
		}
		return null;
	}

	/**
	 * 保存默认收货地址
	 */
	public boolean saveUserDefaultAdd(Context context, AddresssReceiveResponse resp) {
		boolean flag = false;
		List<BeautyAddress> list = resp.getAdress();
		if (list == null || list.size() == 0) {
			SharedPreferenceUtil.clearObject(context, USER_DEFAULT_ADD_SP);
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			BeautyAddress addr = list.get(i);
			String str = addr.getIsdefault();
			if ("1".equals(str)) {
				try {
					flag = SharedPreferenceUtil.saveObject(context, USER_ADDRESS,
							USER_DEFAULT_ADD_SP, addr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LogUtil.outLogDetail(e.getMessage());
				}
			}
		}
		return flag;
	}

	/**
	 * 保存默认收货地址
	 * 
	 * @param context
	 * @param beautyAddress
	 *            地址实体。
	 * @return
	 */
	public boolean saveUserDefaultAdd(Context context, BeautyAddress beautyAddress) {
		boolean flag = false;
		try {
			flag = SharedPreferenceUtil.saveObject(context, USER_ADDRESS, USER_DEFAULT_ADD_SP,
					beautyAddress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.outLogDetail(e.getMessage());
		}
		return flag;
	}

	/**
	 * 清除用户默认地址。
	 * 
	 * @param context
	 * @return
	 */
	public boolean clearUserDeafaultAdd(Context context) {
		boolean isClear = false;
		try {
			isClear = SharedPreferenceUtil.clearObject(context, USER_DEFAULT_ADD_SP);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isClear;
	}

	/**
	 * 得到用户默认收货地址
	 */
	public BeautyAddress getUserDefaultAddres(Context context) {
		BeautyAddress beautyAddress = null;
		try {
			beautyAddress = (BeautyAddress) SharedPreferenceUtil.getObject(context, USER_ADDRESS,
					USER_DEFAULT_ADD_SP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return beautyAddress;
	}

	/**
	 * 检查验证码是否有效
	 * 
	 * @param context
	 * @param phone
	 * @param verifycode
	 * @param type
	 */
	public void checkVerifyCode(Context context, String phone, String verifycode, String type) {

		VerifyCodeCheckRequest verifyCodeCheckRequest = new VerifyCodeCheckRequest();
		verifyCodeCheckRequest.setPhone(phone);
		verifyCodeCheckRequest.setVerify(verifycode);
		verifyCodeCheckRequest.setType(type);
		getDataByPost(context, ConfigInfo.API_VERIFY_CODE_CHECK, verifyCodeCheckRequest,
				BaseResponse.class);

	}

	/**
	 * 获取验证码
	 * 
	 * @param context
	 * @param mobile
	 *            手机
	 * @param sendType
	 *            验证码类型
	 */
	public void getIdentifyCode(Context context, String mobile, String sendType) {
		VerifyCodeRequest identifycodeRequest = new VerifyCodeRequest();
		identifycodeRequest.setMobile(mobile);
		identifycodeRequest.setSend_type(sendType);
		getDataByPost(context, ConfigInfo.API_GET_VERIFY_CODE, identifycodeRequest,
				BaseResponse.class);
	}

	/**
	 * 调取设置默认收货地址接口
	 * 
	 * @param addressId
	 *            地址ID
	 */
	public void postDefaultAddresss(Context context, String addressId) {
		AddressDefaultRequest req = new AddressDefaultRequest();
		req.setAddress(addressId);
		getDataByPost(context, ConfigInfo.API_POST_SET_DEFAULT_ADDRESS, req, BaseResponse.class);
	}

	/***
	 * @discretion 保存用户信息
	 * @param userPhoto
	 *            :用户头像路径
	 */
	public void saveDatas(Context mActivity, String sex, String birthday, String cityCode,
			String userPhoto, String nickName, String century) {
		// 保存的时候不加token
		MemberInfo memberInfo = UserModule.getMemberInfo(mActivity);

		if (memberInfo == null) {
			memberInfo = new MemberInfo();
		}
		if (!TextUtils.isEmpty(birthday)) { // 生日
			memberInfo.setBirthday(birthday);
		}
		if (!TextUtils.isEmpty(cityCode)) { // 城市编码
			memberInfo.setCity(cityCode);
		}
		if (!TextUtils.isEmpty(sex)) { // 性别编码
			memberInfo.setGender(Long.parseLong(sex));
		}
		if (!TextUtils.isEmpty(nickName)) { // 昵称
			memberInfo.setNickname(nickName);
		}
		if (!TextUtils.isEmpty(userPhoto)) { // 用户头像
			memberInfo.setPhoto(userPhoto);
		}
		if (!TextUtils.isEmpty(century)) { // 年代
			memberInfo.setCentury(century);
		}
		saveMemberInfo(mActivity, memberInfo);
	}

	public void getSkinType(Context context, int versionCode) {
		BaseRequest request = new BaseRequest();
		getDataByPost(context, ConfigInfo.API_SKINTYPE, request, SkinTypeResponse.class);
	}

	/**
	 * @discretion 请求护肤需求
	 * @author 时培飞 Create at 2014-12-12 上午9:24:47
	 */
	public void getSkinExplain(Context context, int versionCode) {
		BaseRequest request = new BaseRequest();
		getDataByPost(context, ConfigInfo.API_SKINEXPLAIN, request, SkinExplainReponse.class);
	}

	/**
	 * 保存用户信息(带Token)
	 * 
	 * @param userSaveInfo
	 * @param context
	 */
	public void saveUserLoginMessage(UserInfoSaveEntity userSaveInfo, Context context) {
		MemberInfo userInfo = userSaveInfo.getmInfo();
		// 用户Account
		if (!TextUtils.isEmpty(userSaveInfo.getmAccount())) {
			saveUserAccount(context, userSaveInfo.getmAccount());
		}
		// 用户Token
		if (!TextUtils.isEmpty(userSaveInfo.getmUserToken())) {
			saveUserToken(context, userSaveInfo.getmUserToken());
		}
		// 用户MemberCode
		if (!TextUtils.isEmpty(userSaveInfo.getmMemberCode())) {
			saveUserMemberCode(context, userSaveInfo.getmMemberCode());
		}
		if (userInfo != null) {
			// 获取带Token的用户信息
			MemberInfo memberInfo = UserModule.getMemberInfo(context,
					UserModule.getUserToken(context));
			if (memberInfo == null) {
				memberInfo = new MemberInfo();
			}
			// 昵称
			if (!TextUtils.isEmpty(userInfo.getNickname())) {
				memberInfo.setNickname(userInfo.getNickname());
			}
			// 图片
			if (!TextUtils.isEmpty(userInfo.getPhoto())) {
				memberInfo.setPhoto(userInfo.getPhoto());
			}
			// 生日
			if (!TextUtils.isEmpty(userInfo.getBirthday())) {
				memberInfo.setBirthday(userInfo.getBirthday());
			}
			// 城市
			if (!TextUtils.isEmpty(userInfo.getCity())) {
				memberInfo.setCity(userInfo.getCity());
			}
			// 性别
			if (userInfo.getGender() != 0 && userInfo.getGender() != -1) {
				memberInfo.setGender(userInfo.getGender());
			}
			// 年代
			if (!TextUtils.isEmpty(userInfo.getCentury())) {
				memberInfo.setCentury(userInfo.getCentury());
			}
			// 保存带Token的用户信息
			saveMemberInfo(context, memberInfo, UserModule.getUserToken(context));
		}
	}

	/**
	 * 保存Token的用户信息
	 * 
	 * @param mUserInfoResponse
	 */
	public void saveMemberInfo(UserInfoSaveEntity mInfoSaveEntity,
			UserInfoResponse mUserInfoResponse, Context context) {
		MemberInfo mInfo = new MemberInfo();
		mInfo.setPhoto(mUserInfoResponse.getAvatar());
		mInfo.setNickname(mUserInfoResponse.getNickname());
		mInfo.setCentury(mUserInfoResponse.getCentury());
		if (mUserInfoResponse.getSex() != null && !"".equals(mUserInfoResponse.getSex())) {
			mInfo.setGender(Long.parseLong(mUserInfoResponse.getSex()));
		}
		mInfo.setBirthday(mUserInfoResponse.getBirthday());
		mInfo.setCity(mUserInfoResponse.getArea_code());
		mInfoSaveEntity.setmInfo(mInfo);
		saveUserLoginMessage(mInfoSaveEntity, context);
	}
}
