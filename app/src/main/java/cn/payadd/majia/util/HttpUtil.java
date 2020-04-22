package cn.payadd.majia.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import cn.payadd.majia.exception.BusinessRuntimeException;
import cn.payadd.majia.exception.ExceptionCode;

public class HttpUtil {

	public static final String LOG_TAG = "HttpUtil";

	private static final String CHARSET_NAME = "UTF-8";

	private static final String METHOD_POST = "POST";
	private static final String METHOD_GET = "GET";

	private static final int CONNECT_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 20000;

	static {
		CertTrust.allowAllSSL();
	}

	/**
	 *
	 * @param url
	 * @param content
	 * @return
	 */
	public static String post(String url, String content) throws BusinessRuntimeException {

		if (null == url) {
			return null;
		}
		AppLog.d(LOG_TAG, "send msg -> " + content);

		HttpURLConnection httpConn = null;
		OutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			httpConn = (HttpURLConnection) new URL(url).openConnection();
			httpConn.setRequestMethod(METHOD_POST);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.setConnectTimeout(CONNECT_TIMEOUT);
			httpConn.setReadTimeout(READ_TIMEOUT);
			os = httpConn.getOutputStream();
			if (null != content) {
				os.write(content.getBytes(CHARSET_NAME));
			}
			is = httpConn.getInputStream();
			byte[] b = new byte[1024];
			int count = -1;
			baos = new ByteArrayOutputStream();
			while ((count = is.read(b)) != -1) {
				baos.write(b, 0, count);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			String errCode = ExceptionCode.NETWORK_ERROR;
			throw new BusinessRuntimeException(errCode);

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			String errCode = ExceptionCode.NETWORK_TIMEOUT;
			throw new BusinessRuntimeException(errCode);

		} catch (IOException e) {
			e.printStackTrace();
			String errCode = ExceptionCode.NETWORK_ERROR;
			throw new BusinessRuntimeException(errCode);

		} finally {
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != httpConn) {
				httpConn.disconnect();
			}
		}

		String result = null;
		try {
			result = baos.toString(CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		AppLog.d(LOG_TAG, "return msg -> " + result);
		return result;
	}

	/**
	 *
	 * @param url
	 * @param content
	 * @return
	 */
	public static String post(String url, String content, String header) throws BusinessRuntimeException {

		if (null == url) {
			return null;
		}
		AppLog.d(LOG_TAG, "send msg -> " + content);

		HttpURLConnection httpConn = null;
		OutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			httpConn = (HttpURLConnection) new URL(url).openConnection();
			httpConn.setRequestProperty("Content-type", header);
			httpConn.setRequestMethod(METHOD_POST);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.setConnectTimeout(CONNECT_TIMEOUT);
			httpConn.setReadTimeout(READ_TIMEOUT);
			os = httpConn.getOutputStream();
			if (null != content) {
				os.write(content.getBytes(CHARSET_NAME));
			}
			is = httpConn.getInputStream();
			byte[] b = new byte[1024];
			int count = -1;
			baos = new ByteArrayOutputStream();
			while ((count = is.read(b)) != -1) {
				baos.write(b, 0, count);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			String errCode = ExceptionCode.NETWORK_ERROR;
			throw new BusinessRuntimeException(errCode);

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			String errCode = ExceptionCode.NETWORK_TIMEOUT;
			throw new BusinessRuntimeException(errCode);

		} catch (IOException e) {
			e.printStackTrace();
			String errCode = ExceptionCode.NETWORK_ERROR;
			throw new BusinessRuntimeException(errCode);

		} finally {
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != httpConn) {
				httpConn.disconnect();
			}
		}

		String result = null;
		try {
			result = baos.toString(CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		AppLog.d(LOG_TAG, "return msg -> " + result);
		return result;
	}


	/**
	 *
	 * @param url
	 * @param content
	 * @return
	 */
	public static Object post2(String url, String content, String header) throws BusinessRuntimeException {

		if (null == url) {
			return null;
		}
		AppLog.d(LOG_TAG, "send msg -> " + content);

		HttpURLConnection httpConn = null;
		OutputStream os = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			httpConn = (HttpURLConnection) new URL(url).openConnection();
			httpConn.setRequestProperty("Content-type", header);
			httpConn.setRequestMethod(METHOD_POST);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.setConnectTimeout(CONNECT_TIMEOUT);
			httpConn.setReadTimeout(READ_TIMEOUT);
			os = httpConn.getOutputStream();
			if (null != content) {
				os.write(content.getBytes(CHARSET_NAME));
			}
			is = httpConn.getInputStream();


		} catch (MalformedURLException e) {
			e.printStackTrace();
			String errCode = ExceptionCode.NETWORK_ERROR;
			throw new BusinessRuntimeException(errCode);

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			String errCode = ExceptionCode.NETWORK_TIMEOUT;
			throw new BusinessRuntimeException(errCode);

		} catch (IOException e) {
			e.printStackTrace();
			String errCode = ExceptionCode.NETWORK_ERROR;
			throw new BusinessRuntimeException(errCode);

		} finally {
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != httpConn) {
				httpConn.disconnect();
			}
		}
		return is;
	}

	/**
	 * GET请求
	 *
	 * @param url
	 * @return
	 */
	public static String get(String url) {

		if (null == url) {
			return null;
		}

		HttpURLConnection httpConn = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		String result = null;

		try {

			httpConn = (HttpURLConnection) new URL(url).openConnection();
			httpConn.setRequestMethod(METHOD_GET);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(false);
			httpConn.setConnectTimeout(CONNECT_TIMEOUT);
			httpConn.setReadTimeout(READ_TIMEOUT);
			is = httpConn.getInputStream();
			byte[] b = new byte[1024];
			int count = -1;
			baos = new ByteArrayOutputStream();
			while ((count = is.read(b)) != -1) {
				baos.write(b, 0, count);
			}

			result = baos.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.NETWORK_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessRuntimeException(ExceptionCode.NETWORK_ERROR);
		} finally {
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != httpConn) {
				httpConn.disconnect();
			}
		}

		return result;
	}

}
