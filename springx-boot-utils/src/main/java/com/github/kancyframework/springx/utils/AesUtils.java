package com.github.kancyframework.springx.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 * <p>
 *     通过静态方法调用，使用的是默认的单例对象
 *     通过实例调用的话，请使用aes开头的方法名
 * </p>
 * @author kancy
 * @version 1.0
 * @date 2019/1/28 11:16
 */
public class AesUtils {

	/**
	 * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
	 */
	private static final String S_KEY = "UalUV^u@F$2ZStxE";
	/**
	 * 使用CBC模式，需要一个向量iv，可增加加密算法的强度
	 */
	private static final String IV_PARAMETER = "lr60I@gpel#IIkSG";

	/** 加密工具     */
	private Cipher encryptCipher;

	/** 解密工具     */
	private Cipher decryptCipher;

	private AesUtils() {
		this(S_KEY, IV_PARAMETER);
	}

	private AesUtils(String S_KEY)  {
		init(S_KEY, IV_PARAMETER);
	}

	private AesUtils(String S_KEY, String IV_PARAMETER)  {
		DebugUtils.startPoint();
		init(S_KEY, IV_PARAMETER);
		DebugUtils.endPoint();
	}

	/**
	 * 使用默认的密码
	 */
	private static AesUtils getInstance(){
		return AesHolder.instance;
	}

	/**
	 * 字定义密码
	 * 通过实例调用的话，请使用aes开头的方法名
	 * @param key
	 * @return
	 */
	public static AesUtils getInstance(String key){
		return new AesUtils(key);
	}

	/**
	 * 字定义密码和偏移值
	 * 通过实例调用的话，请使用aes开头的方法名
	 * @param key
	 * @return
	 */
	public static AesUtils getInstance(String key, String ivParameter){
		return new AesUtils(key, ivParameter);
	}

	/**
	 * 解密
	 * @param sSrc
	 * @return
	 */
	public String aesDecrypt(String sSrc) {
		return aesDecrypt(sSrc, false);
	}
	/**
	 * 解密
	 * @param sSrc
	 * @return
	 */
	public static String decrypt(String sSrc) {
		return getInstance().aesDecrypt(sSrc);
	}

	/**
	 * 解密
	 * @param sSrc
	 * @param isUrl
	 * @return
	 */
    public String aesDecrypt(String sSrc , boolean isUrl) {
		try {
			byte[] base64Encrypt = Base64Utils.decodeString(isUrl ? decodeSlash(sSrc) : sSrc);
			byte[] original = decryptCipher.doFinal(base64Encrypt);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return null;
		}
	}

	/**
	 * 解密
	 * @param sSrc
	 * @param isUrl
	 * @return
	 */
	public static String decrypt(String sSrc , boolean isUrl) {
		return getInstance().aesDecrypt(sSrc, isUrl);
	}


	/**
	 * 解密
	 * @param sSrc
	 * @param isUrl
	 * @return
	 */
    public String aesEncrypt(String sSrc, boolean isUrl) {
        try {
            byte[] encrypted = encryptCipher.doFinal(sSrc.getBytes("utf-8"));
			// 此处使用BASE64做转码。
			String encode = Base64Utils.encodeBytes(encrypted);
            return isUrl ? encodeSlash(encode) : trimLineSeparator(encode);
        } catch (Exception e) {
			System.err.println(e.getMessage());
            return null;
        }
	}
	/**
	 * 解密
	 * @param sSrc
	 * @param isUrl
	 * @return
	 */
	public static String encrypt(String sSrc, boolean isUrl) {
		return getInstance().aesEncrypt(sSrc, isUrl);
	}

	/**
	 * 加密
	 * @param sSrc
	 * @return
	 */
	public String aesEncrypt(String sSrc) {
    	return aesEncrypt(sSrc, false);
	}
	/**
	 * 加密
	 * @param sSrc
	 * @return
	 */
	public static String encrypt(String sSrc) {
		return getInstance().aesEncrypt(sSrc);
	}

	/**
	 * 去除换行符
	 * @param sSrc
	 * @return
	 */
	private static String trimLineSeparator(String sSrc) {
		if (sSrc != null) {
			return sSrc.replaceAll("\\r", "")
					.replaceAll("\\n", "");
		}
		return null;
	}

	/**
	 * 加密后替换斜杠
	 * @param sSrc
	 * @return
	 */
	private static String encodeSlash(String sSrc) {
		if (sSrc != null) {
			return sSrc.replaceAll("/", "_").replaceAll("\\+", "-")
					.replaceAll("\\r", "")
					.replaceAll("\\n", "");
		}
		return null;
	}
	
	/**
	 * 解密前还原斜杠
	 * @param sSrc
	 * @return
	 */
	private static String decodeSlash(String sSrc) {
		if (sSrc != null) {
			return sSrc.replaceAll("_", "/").replaceAll("\\-", "+");
		}
		return null;
	}

	/**
	 * 初始化
	 * @param strKey
	 */
	private void init(String strKey, String ivParameter) {
		try {
			byte[] raw = strKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			encryptCipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			decryptCipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 单例模式 - 懒加载
	 */
	private static class AesHolder {
		public static final AesUtils instance = new AesUtils();
	}

}
