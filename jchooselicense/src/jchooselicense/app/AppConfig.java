/**
 * 
 * Copyright (c) 2014 Saul Piña <sauljp07@gmail.com>.
 * 
 * This file is part of Jelpers.
 *
 * Jelpers is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jelpers is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jelpers.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package jchooselicense.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Load configurations
 * 
 * @author Saul Piña <sauljp07@gmail.com>
 * @version 1.0
 */
public final class AppConfig {

	private static AppConfig INSTANCE = null;
	private Properties properties;
	private String path;
	private Charset fileEncoding;

	private AppConfig() {
		properties = new Properties();
		path = null;
		fileEncoding = StandardCharsets.UTF_8;
		load();
	}

	/**
	 * Get path to read/save
	 * 
	 * @return Path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set path to read/save
	 * 
	 * @param path
	 *            Path to read/save
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get file encoding, default StandardCharsets.UTF_8
	 * 
	 * @return File encoding
	 */
	public Charset getFileEncoding() {
		return fileEncoding;
	}

	/**
	 * Set file encoding , default StandardCharsets.UTF_8
	 * 
	 * @param fileEncoding
	 *            String file encoding name
	 */
	public void setFileEncoding(Charset fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	/**
	 * Get singleton instance
	 * 
	 * @return Singleton UtilConfig
	 */
	public synchronized static AppConfig getInstance() {
		if (INSTANCE == null)
			INSTANCE = new AppConfig();
		return INSTANCE;
	}

	/**
	 * Load new configuration
	 */
	public synchronized void load() {
		properties.clear();
		initValues();
	}

	private void initValues() {
		String[] initValues = { "os.name", "os.arch", "os.version", "java.version", "java.vendor" };
		for (String value : initValues) {
			set(value, System.getProperty(value));
		}
	}

	/**
	 * Load configuration from file
	 * 
	 * @param configPath
	 *            Path to read configuration
	 * @throws IOException
	 */
	public synchronized void load(String configPath) throws IOException {
		load(new File(configPath));
	}

	/**
	 * Load configuration from file
	 * 
	 * @param configFile
	 *            Path to read configuration
	 * @throws IOException
	 */
	public synchronized void load(File configFile) throws IOException {
		setConfigPath(configFile.getAbsolutePath());
		properties.clear();
		if (path != null)
			properties.load(new InputStreamReader(new FileInputStream(path), fileEncoding));
		initValues();
	}

	/**
	 * Get the configuration value
	 * 
	 * @param key
	 *            Configuration name
	 * @return Configuration value
	 */
	public synchronized String get(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Get the configuration value
	 * 
	 * @param key
	 *            Configuration name
	 * @param defaultReturn
	 *            If value is null
	 * @return Configuration value
	 */
	public synchronized String get(String key, String defaultReturn) {
		String valueReturn = properties.getProperty(key);
		if (valueReturn == null)
			return defaultReturn;
		return valueReturn;
	}

	/**
	 * Get all the configuration names
	 * 
	 * @return Keys
	 */
	public synchronized List<String> getKeys() {
		List<String> listKeys = new ArrayList<String>();
		Enumeration<Object> keys = properties.keys();
		while (keys.hasMoreElements()) {
			listKeys.add((String) keys.nextElement());
		}
		return listKeys;
	}

	/**
	 * Set new configuration
	 * 
	 * @param key
	 *            Configuration name
	 * @param value
	 *            Configuration value
	 */
	public synchronized void set(String key, String value) {
		properties.put(key, value);
	}

	/**
	 * Save configuration
	 * 
	 * @param configPath
	 *            Path to save configuration
	 * @throws IOException
	 */
	public synchronized void save(String configPath) throws IOException {
		setConfigPath(configPath);
		try {
			properties.store(new OutputStreamWriter(new FileOutputStream(path), fileEncoding), configPath);
		} catch (NullPointerException e) {
			throw new NullPointerException("path null");
		}
	}

	/**
	 * Save configuration in default path
	 * 
	 * @throws IOException
	 */
	public synchronized void save() throws IOException {
		save(path);
	}

	/**
	 * Print configuration values
	 */
	public synchronized void print() {
		properties.list(System.out);
	}

	private void setConfigPath(String path) {
		this.path = path;
		set("config.path", path);
	}

}
