package com.hxj.app.android.keystore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyStore {

	private final Logger	LOGGER	= LoggerFactory.getLogger(KeyStore.class);

	private File			ksFile;
	private String			sha1;
	private String			md5;
	private String			ksData;

	public KeyStore(String path) throws FileNotFoundException {
		ksFile = new File(path);
		if (!ksFile.exists()) {
			throw new FileNotFoundException(path);
		}
	}

	public String getSha1() {
		return sha1;
	}

	public String getMd5() {
		return md5;
	}

	public String getKsData() {
		return ksData;
	}

	/**
	 * 读取KeyStore数据
	 * 
	 * @param password KeyStore密码
	 * @throws IOException
	 */
	public void read(String password) throws IOException {
		// 执行命令
		String cmd = "keytool -v -list -keystore " + ksFile.getAbsolutePath();
		LOGGER.debug("读取KeyStore: {}", cmd);
		final Process p = Runtime.getRuntime().exec(cmd);
		writeString(p.getOutputStream(), password);
		// 读取结果
		try {
			int exitValue = p.waitFor();
			LOGGER.debug("退出:{}", exitValue);
			String ret = readToString(p.getInputStream());
			if (exitValue != 0) {
				throw new RuntimeException("文件无效或密码错误!");
			}
			ksData = ret;
			LOGGER.debug("KeyStore信息:{}", ksData);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		// 解析
		extractInfo();
	}

	/**
	 * 提取信息
	 */
	private void extractInfo() {
		Matcher matcher = Pattern.compile("MD5:(.*)[\r][\n].*SHA1:(.*)").matcher(ksData);
		matcher.find();
		md5 = matcher.group(1).trim();
		sha1 = matcher.group(2).trim();
		LOGGER.debug("MD5:{}", md5);
		LOGGER.debug("SHA1:{}", sha1);
	}

	private void writeString(OutputStream os, String str) throws IOException {
		BufferedWriter bw = null;
		bw = new BufferedWriter(new OutputStreamWriter(os));
		try {
			bw.write(str);
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
	}

	private String readToString(InputStream is) throws IOException {
		BufferedReader br = null;
		try {
			StringBuilder ret = new StringBuilder();
			br = new BufferedReader(new InputStreamReader(is, "GB2312"));
			String line;
			while ((line = br.readLine()) != null) {
				ret.append(line);
				ret.append("\r\n");
			}
			return ret.toString();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}
}
