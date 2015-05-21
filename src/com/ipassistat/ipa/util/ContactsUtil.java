package com.ipassistat.ipa.util;

import java.util.HashMap;
import java.util.Map;

import com.ipassistat.ipa.bean.request.entity.ContactPerson;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;

/***
 * 获取手机联系人信息 com.ipassistat.ipa.util.ContactsUtil
 * 
 * @author 时培飞 Create at 2015-5-4 上午11:31:21
 */
public class ContactsUtil {
	/** 获取库Phon表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	private static ContactsUtil contactsUtil;

	public static synchronized ContactsUtil getSharedInstance() {
		if (contactsUtil == null) {
			contactsUtil = new ContactsUtil();
		}
		return contactsUtil;
	}

	/** 得到手机通讯录联系人信息 **/
	public  Map<String, ContactPerson> getPhoneContacts(Context mContext) {
		Map<String, ContactPerson> personMap = new HashMap<String, ContactPerson>();
		ContentResolver resolver = mContext.getContentResolver();
		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				ContactPerson cp = new ContactPerson();
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				String number = GetNumber(phoneNumber);
				// 得到联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				if (number.length() == 11) {
					cp.setName(contactName);
					cp.setPhoneNum(number);
					cp.setItemType(1);
					personMap.put(contactName, cp);
					if (cp != null && cp.getName() != null) {
						if (cp.getName().length() == 1) {
							cp.setName(cp.getName() + " ");
						}
						if (cp.getName().equals(phoneNumber)) // 如果没有输入存储的用户名
																// 则修改默认名字
						{
							cp.setName("#  ");
							cp.setPinYinName("# ");
						}
					}
				}
			}
			phoneCursor.close();
		}
		return personMap;
	}

	/** 得到手机SIM卡联系人人信息 读取SIM卡手机号,有两种可能:content://icc/adn与content://sim/adn **/
	public  Map<String, ContactPerson> getSIMContacts(Context mContext) {
		Map<String, ContactPerson> personMap = new HashMap<String, ContactPerson>();
		ContentResolver resolver = mContext.getContentResolver();
		// 获取Sims卡联系人

		Uri uri = Uri.parse("content://icc/adn");
		Uri uriSansung = Uri.parse("content://sim/adn");

		Cursor phoneCursor = null;
		try {
			phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);
		} catch (Exception e) {
			phoneCursor = resolver.query(uriSansung, PHONES_PROJECTION, null, null, null);
		}
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				ContactPerson cp = new ContactPerson();
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				String number = GetNumber(phoneNumber);
				// 得到联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				if (number.length() == 11) {
					cp.setName(contactName);
					cp.setPhoneNum(number);
					cp.setItemType(1);
					personMap.put(contactName, cp);
				}
				if (cp != null && cp.getName() != null) {
					if (cp.getName().replaceAll(" ", "").equals(cp.getPhoneNum())) // 如果没有输入存储的用户名
					{
						cp.setName("# ");
						cp.setPinYinName("# ");
					}
					if (cp.getName().length() == 1) {
						cp.setName(cp.getName() + " ");
					}
				}
			}
			phoneCursor.close();
		}
		return personMap;
	}

	/***
	 * 根据联系人姓名查找电话号码
	 * 
	 * @author 时培飞 Create at 2015-5-4 下午4:30:46
	 */
	public  String findPhoneNumber(Context mContext, String userName) {
		String phoneNumber = "";
		Map<String, ContactPerson> contactMap = getPhoneContacts(mContext);
		if (contactMap != null && contactMap.size() > 0) {
			ContactPerson temPerson = contactMap.get(userName.toString());
			if (temPerson != null) {
				phoneNumber = temPerson.getPhoneNum();
			}

		} else {
			contactMap = getSIMContacts(mContext);
			if (contactMap != null && contactMap.size() > 0) {
				ContactPerson temPerson = contactMap.get(userName.toString());
				if (temPerson != null) {
					phoneNumber = temPerson.getPhoneNum();
				}
			}
		}
		return phoneNumber;

	}

	// 还原11位手机号 包括去除“-”
	public static String GetNumber(String num) {
		String phoneNum = "";
		if (num != null) {
			num = num.replaceAll(" ", "");// 中文半角
			num = num.replaceAll("　", "");// 中文全角
			num = num.replaceAll(" ", "");// 英文半角
			num = num.replaceAll("　", "");// 英文全角
			num = num.replaceAll("-", "");
			if (num.startsWith("+86")) {
				num = num.substring(3);
			} else if (num.startsWith("86")) {
				num = num.substring(2);
			}
		} else {
			num = "";
		}
		if (num.length() == 11 && StringUtil.isPhoneNo(num)) {
			phoneNum = num;
		}
		return phoneNum;
	}
}
