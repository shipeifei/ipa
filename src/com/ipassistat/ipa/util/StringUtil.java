package com.ipassistat.ipa.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.ipassistat.ipa.constant.Constant;

/***
 * 
 * com.ipassistat.ipa.util.StringUtil
 * 
 * @author 时培飞 Create at 2015-4-30 下午5:40:13
 */
public class StringUtil {

	/***
	 * 截取一个字符串中的所有中文
	 * 
	 * @author 时培飞 Create at 2015-4-30 下午5:40:24
	 */
	public static String getChineseString(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c <= 40891 && c >= 19968) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/***
	 * 去掉字符串最后一位字符
	 * 
	 * @author 时培飞 Create at 2015-4-30 下午5:40:30
	 */
	public static String cutLastString(String str) {
		if (TextUtils.isEmpty(str))
			return str;
		return str.subSequence(0, str.length() - 1).toString();
	}

	/***
	 * 是否是合法手机号
	 * 
	 * @author 时培飞 Create at 2015-4-30 下午5:40:36
	 */
	public static boolean isPhoneNo(String phoneNum) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
		Matcher m = p.matcher(phoneNum);
		return m.matches();
	}

	/***
	 * 是否有效 密码应该为6~16位字母或数字的组合
	 * 
	 * @author 时培飞 Create at 2015-4-30 下午5:40:47
	 */
	public static boolean isValidPwd(String pwd) {
		String pwd_regex = "[[a-zA-Z][\\d]]{6,16}";
		Pattern p = Pattern.compile(pwd_regex);
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

	/***
	 * 是否是合法邮编
	 * 
	 * @author 时培飞 Create at 2015-4-30 下午5:40:57
	 */
	public static boolean isPostCode(String phoneCode) {
		Pattern p = Pattern.compile("[0-9]{6}");
		Matcher m = p.matcher(phoneCode);
		return m.matches();
	}

	/**
	 * 返回带有货币符号的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getCharPrice(String str) {
		return "￥" + str;
	}

	public static String getCharCount(String str) {
		return " x " + str;
	}

	/**
	 * 
	 * @author 时培飞 Create at 2014-12-16 上午9:42:28
	 * @param source
	 *            :需要拆分的字符串
	 * @param symbol
	 */
	public static String[] getArrayFromString(String source, String symbol) {
		String[] result = null;
		if (source.indexOf(symbol) > -1) {
			result = source.split("\\" + symbol);
			if (result != null && result.length > 0) {
				return result;
			} else {
				return null;
			}
		} else {
			result = new String[1];
			result[0] = source;
			return result;
		}

	}

	/***
	 * 检查昵称是否符合条件，如果符合返回true，不符合返回false
	 * 
	 * @author 时培飞 Create at 2015-4-30 下午5:41:12
	 */
	public static boolean checkNickName(String nickName) {
		char[] temC = nickName.toCharArray();

		for (int i = 0; i < temC.length; i++) {
			int a = temC[i];
			if ((a >= 0x4e00 && a <= 0x9FA5) || (a >= 0x30 && a <= 0x39) || (a >= 0x61 && a <= 0x7a) || (a >= 0x41 && a <= 0x5a)) {
				continue;
			} else {
				return false;
			}
		}

		return true;
	}

	/***
	 * 获取字符串的真实长度，汉字占2个长度，字母占1个长度
	 * 
	 * @author 时培飞 Create at 2015-4-30 下午5:41:20
	 */
	public static int getTrueLengh(String nickName) {
		int count = 0;
		char[] temC = nickName.toCharArray();

		for (int i = 0; i < temC.length; i++) {
			int a = temC[i];
			if (a > 0x4e00 && a < 0x9fff) {
				count += 2;
			}

			if ((a >= 0x30 && a <= 0x39) || (a >= 0x61 && a <= 0x7a) || (a >= 0x41 && a <= 0x5a)) {
				count++;
			}
		}
		return count;
	}

	/***
	 * 判断email格式是否正确
	 * @author 时培飞
	 * Create at 2015-4-30 下午5:42:39
	 */
	public static  boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 根据订单状态吗返回订单状态
	 * 
	 * @param str
	 * @return
	 */
	public static String getOrderStatuStr(String str) {
		if (str.equals(Constant.ORDER_STATU_NO_PAY)) {
			return "待付款";
		} else if (str.equals(Constant.ORDER_STATU_NO_SEND)) {
			return "已付款";
		} else if (str.equals(Constant.ORDER_STATU_HAS_SEND)) {
			return "已发货";
		} else if (str.equals(Constant.ORDER_STATU_HAS_RECEIVE)) {
			return "已收货";
		} else if (str.equals(Constant.ORDER_STATU_SUCCESS)) {
			return "交易成功";
		} else if (str.equals(Constant.ORDER_STATU_FAIL)) {
			return "交易关闭";
		} else {
			return "错误状态";
		}
	}
	/**
	 * 
	 * @author 时培飞 Create at 2014-12-16 上午9:42:28
	 * @param source
	 *            :需要拆分的字符串
	 * @param symbol
	 */
	public static HashMap<String, String> getSpareArrayFromString(String source, String symbol) {
		
		HashMap<String, String> map = new HashMap<String, String>(); 
		if(!TextUtils.isEmpty(source))
		{
			String[] result = getArrayFromString(source.replace("{", "").replace("}", ""),symbol);
			if(result!=null&&result.length>0)
			{
				String[] splitStrings=null;
				for (int i = 0; i < result.length; i++) {
					splitStrings=getArrayFromString(result[i],":");
					if(splitStrings!=null&&splitStrings.length>0)
					{
						map.put(splitStrings[0].replace("\"", ""), splitStrings[1].replace("\"", ""));
					}
				}
				return map;
			}
			else {
				return null;
			}
			
		}
		else {
			return null;
		}

	}



}
