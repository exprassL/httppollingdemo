package com.exp.common.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RSAEncryption {
	private static Log logger = LogFactory.getLog(RSAEncryption.class);

	private static KeyPair keyPair = null;
	private static Cipher cipher = null;
	private static Cipher cipherEncrypt = null;
	private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"; //$NON-NLS-1$
	private static final String keyPairFileName = "RSA.keypair"; //$NON-NLS-1$

	public static void main(String[] args) {
		System.out.println(getPublicKey());
	}

	static {
		try {
//			createRSAKeyPair(RSAEncryption.class.getClassLoader().getResource("RSA.keypair").getPath());
			getKeyPair();
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException("初始化RSA加密工具失败，服务不能正常启动。"); //$NON-NLS-1$
		}
	}

	/**
	 * 使用私钥解密。
	 * 
	 * @param srcStr
	 * @return
	 * @throws IOException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String encrypt(String srcStr) throws IOException, IllegalBlockSizeException, BadPaddingException {
		byte[] b = cipherEncrypt.doFinal(srcStr.getBytes("utf-8")); //$NON-NLS-1$
		return Base64.encodeBase64String(b);
	}

	/**
	 * 使用公钥加密。
	 * 
	 * @param encryptedStr
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String decrypt(String encryptedStr) throws IllegalBlockSizeException, BadPaddingException {
		byte[] plainText = cipher.doFinal(Base64.decodeBase64(encryptedStr));
		String str1 = new String(plainText);
		return str1;
	}

	/**
	 * 用于部署前生成keypair文件。调用方式：<br>
	 * createRSAKeyPair(RSAEncryption.class.getClassLoader().getResource("RSA.keypair").getPath());
	 * @param keyFilePath
	 * @throws Exception
	 */
	public static void createRSAKeyPair(String keyFilePath) throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider()); //$NON-NLS-1$
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG"); //$NON-NLS-1$
		keyPairGen.initialize(1024, random);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		try (FileOutputStream fos = new FileOutputStream(keyFilePath);
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(keyPair);
		}
	}

	/**
	 * 从keypair文件中加载KeyPair对象，用于初始化KeyPair。
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	public static void getKeyPair() throws IOException, ClassNotFoundException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException {
		if (keyPair == null) {
			try (InputStream inStream = RSAEncryption.class.getClassLoader().getResourceAsStream(keyPairFileName);
					ObjectInputStream ois = new ObjectInputStream(inStream)) {
				keyPair = (KeyPair) ois.readObject();
			}

		}

		if (cipher == null) {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM, new BouncyCastleProvider());
			RSAPrivateKey pbk = (RSAPrivateKey) keyPair.getPrivate();
			cipher.init(Cipher.DECRYPT_MODE, pbk);
		}

		if (cipherEncrypt == null) {
			cipherEncrypt = Cipher.getInstance(CIPHER_ALGORITHM, new BouncyCastleProvider());
			cipherEncrypt.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
		}
	}

	/**
	 * 用户生成公钥。
	 * 
	 * @return
	 */
	public static String getPublicKey() {
		PublicKey key = keyPair.getPublic();
		String publicKey = new String(Base64.encodeBase64(key.getEncoded()));
		return publicKey;
	}
}
