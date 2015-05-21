package com.ipassistat.ipa.business;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.ipassistat.ipa.R;
import com.ipassistat.ipa.util.StringUtil;
import com.ipassistat.ipa.util.ToastUtil;

/**
 * 
 * @Description 登录注册模块控制器
 * @author renheng
 * @author lis 重构
 * @date 2015-2-9
 * 
 */
public class LoginController {

	private Operation oper;
	private Context context;
	private EditText mAccountEditText;

	public LoginController(Context context, Operation oper) {
		this.context = context;
		this.oper = oper;
	}

	/**
	 * 登录
	 * 
	 * @param loginName
	 *            账号
	 * @param pwd
	 *            密码
	 */
	public void login(String loginName, String pwd) {
		if (StringUtil.isPhoneNo(loginName)) {
			if (TextUtils.isEmpty(pwd)) {
				ToastUtil.showToast(context, context.getString(R.string.input_pwd));
			} else if (!StringUtil.isValidPwd(pwd)) {
				ToastUtil.showToast(context, context.getString(R.string.pwd_hint));
			} else {
				LoginOperation loper = (LoginOperation) oper;
				loper.showLoginLoading();
				loper.sendLoginReq(loginName, pwd);
			}
		} else {
			ToastUtil.showToast(context, context.getString(R.string.input_phonenumber));
			mAccountEditText.setText("");
		}
	}

	/**
	 * 登录
	 * 
	 * @param loginName
	 *            账号
	 * @param pwd
	 *            密码
	 */
	public void login(EditText loginName, EditText pwd) {
		mAccountEditText = loginName;
		String name = loginName.getText().toString().trim();
		String pass = pwd.getText().toString().trim();
		login(name, pass);
	}

	/**
	 * 注册
	 * 
	 * @param userName
	 *            用户
	 * @param pwd
	 * @param verifycode
	 * @param loginName
	 */
	public void register(String nickName, String pwd, String loginName, String verifycode) {

		if (TextUtils.isEmpty(nickName) || TextUtils.isEmpty(pwd)) {
			ToastUtil.showToast(context, context.getString(R.string.input_nicknameorpwd));
		} else if (!StringUtil.checkNickName(nickName)) {
			ToastUtil.showToast(context, context.getString(R.string.nickname_check));
		} else if (StringUtil.getTrueLengh(nickName) > 20) {
			ToastUtil.showToast(context, context.getString(R.string.nickname_checklengh));
		} else if (!StringUtil.isValidPwd(pwd)) {
			ToastUtil.showToast(context, context.getString(R.string.pwd_hint));
		} else {
			RegisterOperation rOper = (RegisterOperation) oper;
			rOper.sendRegisterReq(loginName, nickName, pwd, verifycode);
		}
	}

	/**
	 * 注册
	 * 
	 * @param nickName
	 *            用户昵称
	 * @param pwd
	 *            验证码
	 * @param verifycode
	 * @param loginName
	 */
	public void register(EditText nickName, EditText pwd, String loginName, String verifycode) {
		String nickNameStr = nickName.getText().toString().trim();
		String pwdStr = pwd.getText().toString().trim();

		register(nickNameStr, pwdStr, loginName, verifycode);
	}

	/**
	 * 忘记密码
	 * 
	 * @param userName
	 *            用户名
	 * @param verifyCode
	 *            验证码
	 * @param mVerifyCodeType
	 *            验证码类型(注册或重置)
	 */
	public void pwdForget(EditText userName, EditText verifyCode, String mVerifyCodeType) {
		VerifyCodeOperation fOper = (VerifyCodeOperation) oper;
		String userNameStr = userName.getText().toString().trim();
		String verifyCodeStr = "";
		if (verifyCode != null) {
			verifyCodeStr = verifyCode.getText().toString().trim();
		}
		if (!StringUtil.isPhoneNo(userNameStr)) {
			fOper.checkFails();
			ToastUtil.showToast(context, context.getString(R.string.input_phonenumber));
			return;
		}
		if (verifyCode == null) {
			fOper.getIdentifyCode(userNameStr, mVerifyCodeType);
		} else {
			if (TextUtils.isEmpty(verifyCodeStr) || verifyCodeStr.length() != 6) {
				ToastUtil.showToast(context, context.getString(R.string.input_verifycodetext));
				verifyCode.setText("");
				fOper.checkFails();
			} else {
				fOper.checkVerifyCode(userNameStr, verifyCodeStr, mVerifyCodeType);
			}
		}
	}

	/**
	 * 密码重置
	 * 
	 * @param userName
	 *            用户
	 * @param newPwdStr
	 *            密码
	 * @param verifyCode
	 *            验证码
	 * @param clientSource
	 *            客户端资源
	 */
	public void pwdReset(String userName, String newPwdStr, String verifyCode, String clientSource) {
		if (TextUtils.isEmpty(newPwdStr)) {
			ToastUtil.showToast(context, context.getString(R.string.input_pwd));
		} else if (!StringUtil.isValidPwd(newPwdStr)) {
			ToastUtil.showToast(context, context.getString(R.string.pwd_hint));
		} else {
			PwdResetOperation pOper = (PwdResetOperation) oper;
			pOper.sendResetPwdReq(userName, newPwdStr, verifyCode, clientSource);
		}
	}

	public interface Operation {

	}

	/**
	 * 登录回调接口
	 * 
	 * @Description
	 * @author lis
	 * @date 2015-2-11
	 * 
	 */
	public interface LoginOperation extends Operation {
		/** 显示加载条 */
		void showLoginLoading();

		/** 取消加载条 */
		void dismissLoginLoading();

		/** 发送登录请求 @param loginName @param pwd */
		void sendLoginReq(String loginName, String pwd);
	}

	/**
	 * 注册回调接口
	 * 
	 * @Description
	 * @author lis
	 * @date 2015-2-11
	 * 
	 */
	public interface RegisterOperation extends Operation {
		/**
		 * 
		 * @param loginName
		 *            用户名
		 * @param nickName
		 *            昵称
		 * @param pwd
		 *            密码
		 * @param verifycode
		 *            验证码
		 */
		void sendRegisterReq(String loginName, String nickName, String pwd, String verifycode);
	}

	/**
	 * 注册,忘记密码共通界面验证码接口
	 * 
	 * @Description
	 * @author lis
	 * @date 2015-2-11
	 * 
	 */
	public interface VerifyCodeOperation extends Operation {
		/**
		 * 获取验证码请求
		 * 
		 * @param userName
		 *            用户名
		 * 
		 * @param verifyCodeType
		 *            验证码类型
		 */
		void getIdentifyCode(String userName, String verifyCodeType);

		/**
		 * 检查验证码是否有效
		 * 
		 * @param userName
		 *            用户名
		 * @param verifyCode
		 *            验证码
		 * @param verifyCodeType
		 *            验证码类型
		 * */
		void checkVerifyCode(String userName, String verifyCode, String mVerifyCodeType);

		/**
		 * 检查错误
		 */
		void checkFails();
	}

	/**
	 * 密码重置接口
	 * 
	 * @Description
	 * @author lis
	 * @date 2015-2-11
	 * 
	 */
	public interface PwdResetOperation extends Operation {
		void sendResetPwdReq(String userName, String newPwd, String verifyCode, String clientSource);
	}

	public interface logoutOperation extends Operation {
		void sendLogoutReq();
	}
}
