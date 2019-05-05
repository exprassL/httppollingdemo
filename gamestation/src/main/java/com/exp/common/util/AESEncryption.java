package com.exp.common.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class AESEncryption {

	private static Key key = null;
	private static Cipher cipher = null;

	static {
		KeyGenerator keyGenerator;
		try {
			keyGenerator = KeyGenerator.getInstance("AES"); //$NON-NLS-1$
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG"); //$NON-NLS-1$
	        random.setSeed("exp_AESEncryption".getBytes("utf-8")); //$NON-NLS-1$ //$NON-NLS-2$
			keyGenerator.init(128, random);
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] keyBytes = secretKey.getEncoded();

			key = new SecretKeySpec(keyBytes, "AES"); //$NON-NLS-1$

			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); //$NON-NLS-1$
		} catch (Exception e) {
			throw new RuntimeException("初始化AES加密工具失败，服务不能正常启动。", e); //$NON-NLS-1$
		}
	}

	public static String encrypt(String source) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encodeResult = cipher.doFinal(source.getBytes());
		return new String(Hex.encodeHex(encodeResult));
	}

	public static String decrypt(String encryptedSource) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decodeResult = cipher.doFinal(Hex.decodeHex(encryptedSource.toCharArray()));
			return new String(decodeResult);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | DecoderException e) {
			e.printStackTrace();
			return null;
		}
	}

}
