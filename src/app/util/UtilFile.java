/**
 * 
 * UtilFile.java
 * 
 * Copyright (c) 2014, Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE.txt file.
 *
 */

package app.util;

import java.io.File;
import java.io.FileFilter;

/**
 * Class for handling files
 * 
 * @author Saul Piña <sauljp07@gmail.com>
 * @version 1.0
 */
public class UtilFile {

	public static final String VERSION = "1.0";

	/**
	 * Get files in the path
	 * 
	 * @param file
	 * @return Files in the path
	 */
	public File[] files(File file) {
		return file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		});
	}

	/**
	 * Get files in the path
	 * 
	 * @param path
	 * @return Files in the path
	 */
	public File[] files(String path) {
		return files(new File(path));
	}

	/**
	 * Get directories in the path
	 * 
	 * @param file
	 * @return Directories in the path
	 */
	public File[] directories(File file) {
		return file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
	}

	/**
	 * Get directories in the path
	 * 
	 * @param path
	 * @return Directories in the path
	 */
	public File[] directories(String path) {
		return directories(new File(path));
	}

	/**
	 * Get only file name without extension
	 * 
	 * @param file
	 *            File
	 * @return Only file name
	 */
	public String getOnlyName(File file) {
		return getOnlyName(file.getName());
	}

	/**
	 * Get only file name without extension
	 * 
	 * @param fileName
	 *            File name
	 * @return Only file name
	 */
	public String getOnlyName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}

	/**
	 * Get only file extension
	 * 
	 * @param file
	 *            File
	 * @return Extension
	 */
	public String getExtension(File file) {
		return getExtension(file.getName());
	}

	/**
	 * Get only file extension
	 * 
	 * @param fileName
	 *            File
	 * @return Extension
	 */
	public String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
	}

	/**
	 * Validate if a file belongs to types
	 * 
	 * @param file
	 * @param extensions
	 * @return True if the extension file is in extensions
	 */
	public boolean isFileType(File file, String... extensions) {
		return isFileType(file.getName(), extensions);
	}

	/**
	 * Validate if a file belongs to types
	 * 
	 * @param fileName
	 * @param extensions
	 * @return True if the extension file is in extensions
	 */
	public boolean isFileType(String fileName, String... extensions) {
		for (String extension : extensions) {
			if (fileName.toUpperCase().endsWith("." + extension.toUpperCase()))
				return true;
		}
		return false;
	}
}
