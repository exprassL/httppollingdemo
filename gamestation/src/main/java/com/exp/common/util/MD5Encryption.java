/**
 * 
 */
package com.exp.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Exprass
 * 
 */
public class MD5Encryption {

	private static MessageDigest md5;

	static {
		try {
			md5 = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String getMD5(String original) {
		try {
			md5.update(original.getBytes("UTF-8")); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String md5Str = new BigInteger(1, md5.digest()).toString(16);
		return md5Str;
	}
}
