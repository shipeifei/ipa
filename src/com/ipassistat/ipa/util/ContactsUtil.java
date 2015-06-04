package com.ipassistat.ipa.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.ipassistat.ipa.bean.request.entity.ContactPerson;
import com.ipassistat.ipa.domain.action.DomainContext;
import com.umeng.socialize.bean.UMFriend.PinYin;

import android.R.array;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;

/***
 * 获取手机联系人信息 com.ipassistat.ipa.util.ContactsUtil
 * 
 * @author 时培飞 Create at 2015-5-4 上午11:31:21
 */
public class ContactsUtil {

	private static final String TAG = "ContactsUtil";
	/** 获取库Phon表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	/***
	 * 联系人照片
	 */
	private static final int PHONES_PHOTO_ID_INDEX = 2;
	/***
	 * 联系人编号
	 */
	private static final int PHONES_CONTACT_ID_INDEX = 3;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	private static ContactsUtil contactsUtil;

	public static synchronized ContactsUtil getSharedInstance() {
		if (contactsUtil == null) {
			contactsUtil = new ContactsUtil();
		}
		return contactsUtil;
	}

	// 获取汉语拼音首字母
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);

		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	/***
	 * 获取手机联系人作为list集合返回 create at 2015-5-25 author 时培飞
	 */
	public ArrayList<ContactPerson> getPhoneContactsAsList(Context mContext, boolean isGetPhoto) {
		ArrayList<ContactPerson> personMap = new ArrayList<ContactPerson>();
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
				// 获取联系人拼音
				String contaPinyinString = StringHelper.getPingYin(contactName);
				// if (number.length() == 11) {
				cp.setName(contactName);
				cp.setPhoneNum(number);
				cp.setItemType(1);
				cp.setPinYinName(contaPinyinString);
				cp.setLetter(getAlpha(contaPinyinString));
				if (isGetPhoto) {
					// 获取联系人头像编号
					Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
					Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
					Bitmap contactPhoto = null;

					// photoid
					if (photoid > 0) {
						Uri uriq = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
						InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uriq);
						contactPhoto = BitmapFactory.decodeStream(input);
					} else {
						// contactPhoto =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.contact_photo);
					}

					cp.setHeadImag(contactPhoto);
				}
				personMap.add(cp);
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
				// }
			}
			phoneCursor.close();
		}
		phoneCursor.close();
		return personMap;
	}

	/** 得到手机通讯录联系人信息 **/
	public Map<String, ContactPerson> getPhoneContacts(Context mContext, boolean isGetPhoto) {
		Map<String, ContactPerson> personMap = new HashMap<String, ContactPerson>();
		ContentResolver resolver = mContext.getContentResolver();
		Bitmap contactPhoto = null;
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
				// 获取联系人拼音
				String contaPinyinString = StringHelper.getPingYin(contactName);
				// if (number.length() == 11) {
				cp.setName(contactName);
				cp.setPhoneNum(number);

				cp.setPinYinName(contaPinyinString);
				cp.setLetter(getAlpha(contaPinyinString));
				if (isGetPhoto) {
					// 获取联系人头像编号
					Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
					Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

					// photoid
					if (photoid > 0) {
						Uri uriq = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
						InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uriq);
						contactPhoto = BitmapFactory.decodeStream(input);
						cp.setHeadImag(contactPhoto);
					} else {
						// contactPhoto =
						// BitmapFactory.decodeResource(getResources(),
						// / R.drawable.contact_photo);
					}
				}

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
				// contactPhoto = null;
				// }
			}
			phoneCursor.close();
		}
		phoneCursor.close();
		return personMap;
	}

	/***
	 * 获取sim卡联系人作为list返回 create at 2015-5-25 author 时培飞
	 */
	public ArrayList<ContactPerson> getSIMContactsAsList(Context mContext, boolean isGetPhoto) {
		ArrayList<ContactPerson> personMap = new ArrayList<ContactPerson>();
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
				if (number.length() >= 7) {
					// 得到联系人名称
					String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
					// 获取联系人拼音
					String contaPinyinString = StringHelper.getPingYin(contactName);
					// if (number.length() == 11) {
					cp.setName(contactName);
					cp.setPhoneNum(number);
					cp.setItemType(1);
					cp.setPinYinName(contaPinyinString);
					cp.setLetter(getAlpha(contaPinyinString));
					if (isGetPhoto) {
						// 获取联系人头像编号
						Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
						Long contactid = phoneCursor.getLong(phoneCursor.getColumnIndex("contact_id"));
						Bitmap contactPhoto = null;

						// photoid
						if (photoid > 0) {
							Uri uriq = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
							InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uriq);
							contactPhoto = BitmapFactory.decodeStream(input);
						} else {
							// contactPhoto =
							// BitmapFactory.decodeResource(getResources(),
							// R.drawable.contact_photo);
						}

						cp.setHeadImag(contactPhoto);
					}
					personMap.add(cp);

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
					// contactPhoto = null;
				}
			}
			phoneCursor.close();
		}
		phoneCursor.close();
		return personMap;
	}

	/** 得到手机SIM卡联系人人信息 读取SIM卡手机号,有两种可能:content://icc/adn与content://sim/adn **/
	public Map<String, ContactPerson> getSIMContacts(Context mContext, boolean isGetPhoto) {
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
				if (number.length() >= 7) {
					// 得到联系人名称
					String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
					// 获取联系人拼音
					String contaPinyinString = StringHelper.getPingYin(contactName);
					// if (number.length() == 11) {
					cp.setName(contactName);
					cp.setPhoneNum(number);
					cp.setItemType(1);
					cp.setPinYinName(contaPinyinString);
					cp.setLetter(getAlpha(contaPinyinString));
					if (isGetPhoto) {
						// 获取联系人头像编号
						Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
						Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

						Bitmap contactPhoto = null;
						// photoid
						if (photoid > 0) {
							Uri uriq = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
							InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uriq);
							contactPhoto = BitmapFactory.decodeStream(input);
						} else {
							// contactPhoto =
							// BitmapFactory.decodeResource(getResources(),
							// R.drawable.contact_photo);
						}

						cp.setHeadImag(contactPhoto);
					}
					personMap.put(contactName, cp);

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
			}
			phoneCursor.close();
		}
		phoneCursor.close();
		return personMap;
	}

	/***
	 * 根据联系人姓名查找电话号码
	 * 
	 * @author 时培飞 Create at 2015-5-4 下午4:30:46
	 */
	public String findPhoneNumber(Context mContext, String userName) {
		String phoneNumber = "";
		Map<String, ContactPerson> contactMap = getPhoneContacts(mContext, false);
		if (contactMap != null && contactMap.size() > 0) {
			ContactPerson temPerson = contactMap.get(userName.toString());
			if (temPerson != null) {
				phoneNumber = temPerson.getPhoneNum();
			}

		} else {
			contactMap = getSIMContacts(mContext, false);
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

	public Bitmap getPersonPhoto(String PersonID) {
		String photo_id = null;
		String[] projection1 = new String[] { ContactsContract.Contacts.PHOTO_ID };
		String selection1 = ContactsContract.Contacts._ID + " = " + PersonID;
		Cursor cur1 = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection1, selection1, null, null);
		if (cur1.getCount() > 0) {
			cur1.moveToFirst();
			photo_id = cur1.getString(0);
		}
		String[] projection = new String[] { ContactsContract.Data.DATA15 };

		String selection = "ContactsContract.Data._ID = " + photo_id;

		Cursor cur = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, selection, null, null);
		cur.moveToFirst();
		if (cur.getCount() < 0 || cur.getCount() == 0) {
			return null;
		}
		byte[] contactIcon = cur.getBlob(0);
		if (contactIcon == null) {
			return null;
		} else {
			Bitmap map = BitmapFactory.decodeByteArray(contactIcon, 0, contactIcon.length);
			return map;
		}
	}

	/**
	 * Use a simple string represents the long.
	 */
	private static final String COLUMN_CONTACT_ID = ContactsContract.Data.CONTACT_ID;
	private static final String COLUMN_RAW_CONTACT_ID = ContactsContract.Data.RAW_CONTACT_ID;
	private static final String COLUMN_MIMETYPE = ContactsContract.Data.MIMETYPE;
	private static final String COLUMN_NAME = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME;
	private static final String COLUMN_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
	private static final String COLUMN_NUMBER_TYPE = ContactsContract.CommonDataKinds.Phone.TYPE;
	private static final String COLUMN_EMAIL = ContactsContract.CommonDataKinds.Email.DATA;
	private static final String COLUMN_EMAIL_TYPE = ContactsContract.CommonDataKinds.Email.TYPE;
	private static final String MIMETYPE_STRING_NAME = ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE;
	private static final String MIMETYPE_STRING_PHONE = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;
	private static final String MIMETYPE_STRING_EMAIL = ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE;
	private static ContentResolver contentResolver = DomainContext.getInstance().context.getContentResolver();

	/**
	 * Search and fill the contact information by the contact name given.
	 * 
	 * @param contact
	 *            Only the name is necessary.
	 */
	public static ContactPerson searchContact(String name) {
		Log.w(TAG, "**search start**");
		ContactPerson contact = new ContactPerson();
		contact.setName(name);
		Log.d(TAG, "search name: " + contact.getName());
		String id = getContactID(contact.getName());
		contact.setP_id(id);

		if (id.equals("0")) {
			Log.d(TAG, contact.getName() + " not exist. exit.");
		} else {
			Log.d(TAG, "find id: " + id);
			// Fetch Phone Number
			Cursor cursor = contentResolver.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] { COLUMN_NUMBER, COLUMN_NUMBER_TYPE }, COLUMN_CONTACT_ID + "='"
					+ id + "'", null, null);
			while (cursor.moveToNext()) {
				contact.setPhoneNum(cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)));
				contact.setNumberType(cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER_TYPE)));
				// Log.d(TAG, "find number: " + contact.getNumber());
				// Log.d(TAG, "find numberType: " + contact.getNumberType());
			}
			// cursor.close();

			// Fetch email
			cursor = contentResolver.query(android.provider.ContactsContract.CommonDataKinds.Email.CONTENT_URI, new String[] { COLUMN_EMAIL, COLUMN_EMAIL_TYPE }, COLUMN_CONTACT_ID + "='" + id + "'",
					null, null);
			while (cursor.moveToNext()) {
				contact.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
				contact.setEmailType(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_TYPE)));
				Log.d(TAG, "find email: " + contact.getEmail());
				Log.d(TAG, "find emailType: " + contact.getEmailType());
			}
			cursor.close();
		}
		Log.w(TAG, "**search end**");
		return contact;
	}

	/**
	 * 
	 * @param contact
	 *            The contact who you get the id from. The name of the contact
	 *            should be set.
	 * @return 0 if contact not exist in contacts list. Otherwise return the id
	 *         of the contact.
	 */
	public static String getContactID(String name) {
		String id = "0";
		Cursor cursor = contentResolver.query(android.provider.ContactsContract.Contacts.CONTENT_URI, new String[] { android.provider.ContactsContract.Contacts._ID },
				android.provider.ContactsContract.Contacts.DISPLAY_NAME + "='" + name + "'", null, null);
		if (cursor.moveToNext()) {
			id = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID));
		}
		return id;
	}

	/**
	 * You must specify the contact's ID.
	 * 
	 * @param contact
	 * @throws Exception
	 *             The contact's name should not be empty.
	 */
	public static void addContact(ContactPerson contact) {

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		String id = getContactID(contact.getName());
		if (!id.equals("0")) {

		} else if (contact.getName().trim().equals("")) {

		} else {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
					.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(COLUMN_RAW_CONTACT_ID, 0).withValue(COLUMN_MIMETYPE, MIMETYPE_STRING_NAME)
					.withValue(COLUMN_NAME, contact.getName()).build());

			if (!contact.getPhoneNum().trim().equals("")) {
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(COLUMN_RAW_CONTACT_ID, 0).withValue(COLUMN_MIMETYPE, MIMETYPE_STRING_PHONE)
						.withValue(COLUMN_NUMBER, contact.getPhoneNum()).withValue(COLUMN_NUMBER_TYPE, contact.getNumberType()).build());

			}

			if (!contact.getEmail().trim().equals("")) {
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(COLUMN_RAW_CONTACT_ID, 0).withValue(COLUMN_MIMETYPE, MIMETYPE_STRING_EMAIL)
						.withValue(COLUMN_EMAIL, contact.getEmail()).withValue(COLUMN_EMAIL_TYPE, contact.getEmailType()).build());
				// Log.d(TAG, "add email: " + contact.getEmail());
			}

			try {
				contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
				// Log.d(TAG, "add contact success.");
			} catch (Exception e) {
				// Log.d(TAG, "add contact failed.");
				// Log.e(TAG, e.getMessage());
			}
		}
		// Log.w(TAG, "**add end**");

	}

	/**
	 * Delete contacts who's name equals contact.getName();
	 * 
	 * @param contact
	 */
	public static void deleteContact(ContactPerson contact) {
		// Log.w(TAG, "**delete start**");
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		String id = getContactID(contact.getName());
		// delete contact
		ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=" + id, null).build());
		// delete contact information such as phone number,email
		ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI).withSelection(COLUMN_CONTACT_ID + "=" + id, null).build());
		// Log.d(TAG, "delete contact: " + contact.getName());

		try {
			contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
			// Log.d(TAG, "delete contact success");
		} catch (Exception e) {
			Log.d(TAG, "delete contact failed");
			Log.e(TAG, e.getMessage());
		}
		Log.w(TAG, "**delete end**");
	}

	/**
	 * @param contactOld
	 *            The contact wants to be updated. The name should exists.
	 * @param contactNew
	 */
	public static void updateContact(ContactPerson contactOld, ContactPerson contactNew) {
		Log.w(TAG, "**update start**");
		String id = getContactID(contactOld.getName());
		if (id.equals("0")) {
			Log.d(TAG, contactOld.getName() + " not exist.");
		} else if (contactNew.getName().trim().equals("")) {
			Log.d(TAG, "contact name is empty. exit.");
		} else if (!getContactID(contactNew.getName()).equals("0")) {
			Log.d(TAG, "new contact name already exist. exit.");
		} else {

			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

			// update name
			ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					.withSelection(COLUMN_CONTACT_ID + "=? AND " + COLUMN_MIMETYPE + "=?", new String[] { id, MIMETYPE_STRING_NAME }).withValue(COLUMN_NAME, contactNew.getName()).build());
			Log.d(TAG, "update name: " + contactNew.getName());

			// update number
			if (!contactNew.getPhoneNum().trim().equals("")) {
				ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						.withSelection(COLUMN_CONTACT_ID + "=? AND " + COLUMN_MIMETYPE + "=?", new String[] { id, MIMETYPE_STRING_PHONE }).withValue(COLUMN_NUMBER, contactNew.getPhoneNum())
						.withValue(COLUMN_NUMBER_TYPE, contactNew.getNumberType()).build());
				// Log.d(TAG, "update number: " + contactNew.getNumber());
			}

			// update email if mail
			if (!contactNew.getEmail().trim().equals("")) {
				ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						.withSelection(COLUMN_CONTACT_ID + "=? AND " + COLUMN_MIMETYPE + "=?", new String[] { id, MIMETYPE_STRING_EMAIL }).withValue(COLUMN_EMAIL, contactNew.getEmail())
						.withValue(COLUMN_EMAIL_TYPE, contactNew.getEmailType()).build());
				Log.d(TAG, "update email: " + contactNew.getEmail());
			}

			try {
				contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
				Log.d(TAG, "update success");
			} catch (Exception e) {
				Log.d(TAG, "update failed");
				Log.e(TAG, e.getMessage());
			}
		}
		Log.w(TAG, "**update end**");
	}
}
