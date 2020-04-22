package cn.payadd.majia.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import cn.payadd.majia.exception.BusinessRuntimeException;
import cn.payadd.majia.exception.ExceptionCode;

public class DESCipher {

	private static final String ALGORITHM = "DESede";

	private SecretKey key;
	
	public DESCipher(SecretKey key) {
		this.key = key;
	}

	/**
	 * 加密
	 * 
	 * @param plaintext
	 * @return
	 */
	public byte[] encipher(byte[] plaintext) throws BusinessRuntimeException {

		byte[] ciphertext = null;
		try {

			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, key);
			ciphertext = c.doFinal(plaintext);

		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.ENCRYPT_ERROR);
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.ENCRYPT_ERROR);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.ENCRYPT_ERROR);
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.ENCRYPT_ERROR);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.ENCRYPT_ERROR);
		}

		return ciphertext;
	}

	public byte[] encipher(String plaintext) {

		return encipher(plaintext.getBytes());
	}

	/**
	 * 解密
	 * 
	 * @param ciphertext
	 * @return
	 */
	public byte[] decipher(byte[] ciphertext) throws BusinessRuntimeException {

		byte[] plaintext = null;
		
		try {

			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, key);
			plaintext = c.doFinal(ciphertext);

		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.DECRYPT_ERROR);
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.DECRYPT_ERROR);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.DECRYPT_ERROR);
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.DECRYPT_ERROR);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.DECRYPT_ERROR);
		}

		return plaintext;
	}
}
