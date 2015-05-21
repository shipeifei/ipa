package com.ipassistat.ipa.util;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {
	
	/**
	 * 字典排序
	 * @return 
	 */
	public static void sort(List<String> list){
		Collections.sort(list, new Comparator<Object>() {
		      public int compare(Object o1, Object o2) {
		    	  String one =(String) o1;
		    	  String two = (String) o2;
		        return one.compareTo(two);
		      }
		    });
	};
	
	/*
	 * sha1 加密
	 */
	public static String sha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes());
			
			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
	
 }
