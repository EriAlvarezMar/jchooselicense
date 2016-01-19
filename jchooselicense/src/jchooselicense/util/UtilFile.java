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

package jchooselicense.util;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for handling files
 * 
 * @author Saul Piña <sauljp07@gmail.com>
 * @version 1.0
 */
public class UtilFile {

	public static final String VERSION = "1.1";

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
	 * @param file
	 * @param pattern
	 * @return Files in the path
	 */
	public File[] files(File file, String pattern) {
		Pattern r = Pattern.compile(pattern);

		return file.listFiles(new FileFilter() {
			private Pattern pattern;

			public FileFilter setPattern(Pattern pattern) {
				this.pattern = pattern;
				return this;
			}

			@Override
			public boolean accept(File file) {
				Matcher m = pattern.matcher(file.getName());
				return file.isFile() && m.matches();
			}
		}.setPattern(r));
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
	 * Get files in the pat
	 * 
	 * @param path
	 * @param pattern
	 * @return Files in the path
	 */
	public File[] files(String path, String pattern) {
		return files(new File(path), pattern);
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
