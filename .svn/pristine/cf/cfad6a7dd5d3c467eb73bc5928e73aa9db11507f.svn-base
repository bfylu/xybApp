package cn.payadd.majia.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import cn.payadd.majia.exception.BusinessRuntimeException;
import cn.payadd.majia.exception.ExceptionCode;

public class DESGeneral {

	private static final String DES_ALGORITHM = "DESede";

	private static final char[] BASE = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G',
												'H', 'I', 'J', 'K', 'L', 'M', 'N',
												'O', 'P', 'Q', 'R', 'S', 'T',
												'U', 'V', 'W', 'X', 'Y', 'Z',
												'a', 'b', 'c', 'd', 'e', 'f', 'g',
												'h', 'i', 'j', 'k', 'l', 'm', 'n',
												'o', 'p', 'q', 'r', 's', 't',
												'u', 'v', 'w', 'x', 'y', 'z',
												'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	
	private DESGeneral() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static DESGeneral getInstance() {
		
		DESGeneral desGeneral = new DESGeneral();
		
		return desGeneral;
	}
	
	/**
	 * 生成随机密钥
	 * @return
	 */
	public SecretKey generalKey() {
		
		SecretKey srtKey = null;
		try {
			
			srtKey = KeyGenerator.getInstance(DES_ALGORITHM).generateKey();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return srtKey;
	}
	
	
	/**
	 * 根据byte生成密钥
	 * 
	 * @param key
	 * @return
	 */
	public SecretKey generalKey(byte[] key) throws BusinessRuntimeException {

		SecretKey srtKey = null;
		
		try {

			DESedeKeySpec keySpec = new DESedeKeySpec(key);
			srtKey = SecretKeyFactory.getInstance(DES_ALGORITHM).generateSecret(keySpec);

		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.KEY_ERROR);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.KEY_ERROR);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.KEY_ERROR);
		}
		
		return srtKey;

	}
	
	/**
	 * 
	 * @param len
	 * @return
	 */
	public SecretKey generalKey(int len) {
		
		byte[] bkey = generalKeyByte(len);
		return generalKey(bkey);
	}
	
	/**
	 * 生成指定长度的随机密钥，常见的有24、32位
	 * 
	 * @param len
	 * @return
	 */
	public byte[] generalKeyByte(int len) {
		
		Random random = new Random();
		byte[] key = new byte[len];
		int baseLen = BASE.length;
		for (int i = 0; i < len; i++) {
			key[i] = (byte) BASE[random.nextInt(baseLen)];
		}
		
		return key;
	}
}
