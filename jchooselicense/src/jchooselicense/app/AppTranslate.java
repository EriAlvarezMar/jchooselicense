/**
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE file.
 */

package jchooselicense.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public final class AppTranslate {

	private static AppTranslate INSTANCE = null;
	private Properties properties;
	private String path;
	private Charset fileEncoding;

	private AppTranslate() {
		properties = new Properties();
		path = null;
		fileEncoding = StandardCharsets.UTF_8;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Charset getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(Charset fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public synchronized static AppTranslate getInstance() {
		if (INSTANCE == null)
			INSTANCE = new AppTranslate();
		return INSTANCE;
	}

	public synchronized void load() throws IOException {
		load(path);
	}

	public synchronized void load(String configPath) throws IOException {
		load(new File(configPath));
	}

	public synchronized void load(File configFile) throws IOException {
		path = configFile.getAbsolutePath();
		properties.clear();
		if (path != null)
			properties.load(new InputStreamReader(new FileInputStream(path), fileEncoding));
	}

	public synchronized String get(String key) {
		return properties.getProperty(key);
	}

	public synchronized String get(String key, String defaultValue) {
		String returnValue = properties.getProperty(key);
		if (returnValue == null)
			return defaultValue;
		return returnValue;
	}

	public synchronized List<String> getKeys() {
		List<String> listKeys = new ArrayList<String>();
		Enumeration<Object> keys = properties.keys();
		while (keys.hasMoreElements()) {
			listKeys.add((String) keys.nextElement());
		}
		return listKeys;
	}

}
