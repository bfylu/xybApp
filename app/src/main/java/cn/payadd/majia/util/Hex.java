package cn.payadd.majia.util;

public class Hex {

//	private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//												'A', 'B', 'C', 'D', 'E', 'F'};
	
	private static final String HEX_STR = "0123456789ABCDEF";
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public static String byteCoverToString(byte[] b) {
		
		StringBuilder buff = new StringBuilder();
		
		for (int i = 0, j = b.length; i < j; i++) {
			int n = b[i] & 0xFF;
			String s = Integer.toHexString(n);
			if (s.length() < 2) {
				buff.append("0").append(s);
			} else {
				buff.append(s);
			}

		}

		String str = buff.toString();
		
		return str;
	}
	
	/**
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] stringCoverToByte(String hexStr) {
		
	    char[] hexs = hexStr.toCharArray();
	    byte[] bytes = new byte[hexStr.length() / 2];
	    int n;
	    for (int i = 0; i < bytes.length; i++) {
	        n = HEX_STR.indexOf(hexs[2 * i]) * 16;
	        n += HEX_STR.indexOf(hexs[2 * i + 1]);
	        bytes[i] = (byte) (n & 0xff);
	    }
	    
	    return bytes;
	}
	
}
