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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Console and file log
 * 
 * @author Saul Piña <sauljp07@gmail.com>
 * @version 1.3
 */
public final class AppLog {

	private static AppLog INSTANCE = null;
	public static final String DATEFORMAT_TIME = "H:mm:ss";
	public static final String DATEFORMAT_DATE = "yyyy-MM-dd";
	public static final String DATEFORMAT_TIMESTAMP = "yyyy-MM-dd H:mm:ss";
	public static final String VERSION = "1.3";
	private UtilLogLevel level;
	private Charset fileEncoding;
	private String path;
	private SimpleDateFormat sdf;
	private String dateFormat;

	/**
	 * UtilLog levels
	 * 
	 * @author Saul Piña <sauljp07@gmail.com>
	 */
	public enum UtilLogLevel {
		NONE(0), INFO(1), WARN(2), ERROR(3), DEVEL(4);
		public final int value;

		UtilLogLevel(int value) {
			this.value = value;
		}
	}

	/**
	 * Get instance singleton
	 * 
	 * @return UtilLog
	 */
	public synchronized static AppLog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new AppLog();
		return INSTANCE;
	}

	private AppLog() {
		level = UtilLogLevel.DEVEL;
		fileEncoding = StandardCharsets.UTF_8;
		path = "log";
		setDateFormat(DATEFORMAT_TIMESTAMP);
	}

	/**
	 * Get date format
	 * 
	 * @return Date format
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * Set date format
	 * 
	 * @param dateFormat
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		sdf = new SimpleDateFormat(dateFormat);
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
	 *            String file encoding
	 */
	public void setFileEncoding(Charset fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	/**
	 * Get directory to save log
	 * 
	 * @return Path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set directory to save log
	 * 
	 * @param path
	 *            Path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get level
	 * 
	 * @return Level
	 */
	public UtilLogLevel getLevel() {
		return level;
	}

	/**
	 * Set level for print: UtilLogLevel.NONE, UtilLogLevel.INFO,
	 * UtilLogLevel.WARN, UtilLogLevel.ERROR, UtilLogLevel.DEVEL
	 * 
	 * @param level
	 */
	public void setLevel(UtilLogLevel level) {
		this.level = level;
	}

	private void print(UtilLogLevel printLevel, Class<?> clazz, String msg, Exception e) {
		if (level == UtilLogLevel.NONE)
			return;

		if (printLevel.value > level.value)
			return;

		String type = "";

		switch (printLevel) {
		case INFO:
			type = "INFORMATION";
			break;
		case WARN:
			type = "WARNING";
			break;
		case ERROR:
			type = "ERROR";
			break;
		case DEVEL:
			type = "DEVELOPER";
			break;
		default:
			break;
		}

		String clazzString = "";
		if (clazz != null)
			clazzString = String.format("FROM: %s\n", clazz.getName());

		String string = String.format("%sTYPE: %s\nTIME: %s\n----> %s\n", clazzString, type, sdf.format(new Date()), msg);

		printToConsole(string, e);

		if (printLevel.equals(UtilLogLevel.DEVEL))
			return;

		printToFile(string, e);
	}

	private void printToConsole(String string, Exception e) {
		PrintStream ps = System.out;
		ps.print(string);
		if (e != null) {
			e.printStackTrace(ps);
		}
		ps.println();
	}

	private void printToFile(String string, Exception e) {
		File folder = new File(path);
		folder.mkdir();
		FileOutputStream fs;
		OutputStreamWriter os;
		BufferedWriter bw;
		Date today = new Date();
		try {
			fs = new FileOutputStream(String.format("%s/%tY-%tm-%td.log", folder.getPath(), today, today, today), true);
			os = new OutputStreamWriter(fs, fileEncoding);
			bw = new BufferedWriter(os);
			bw.write(string);
			bw.flush();
			if (e != null)
				e.printStackTrace(new PrintWriter(os));
			bw.write("\n");
			bw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Print log for info level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param msg
	 *            message
	 */
	public synchronized void info(Class<?> clazz, String msg) {
		info(clazz, null, msg);
	}

	/**
	 * Print log for info level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param format
	 *            String format
	 * @param args
	 *            Arguments of message
	 */
	public synchronized void info(Class<?> clazz, String format, Object... args) {
		info(clazz, String.format(format, args));
	}

	/**
	 * Print log for info level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param e
	 *            Exception
	 * @param msg
	 *            message
	 */
	public synchronized void info(Class<?> clazz, Exception e, String msg) {
		print(UtilLogLevel.INFO, clazz, msg, e);
	}

	/**
	 * Print log for info level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param e
	 *            Exception
	 * @param format
	 *            String format
	 * @param args
	 *            Arguments of message
	 */
	public synchronized void info(Class<?> clazz, Exception e, String format, Object... args) {
		info(clazz, e, String.format(format, args));
	}

	/**
	 * Print log for error level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param msg
	 *            message
	 */
	public synchronized void error(Class<?> clazz, String msg) {
		error(clazz, null, msg);
	}

	/**
	 * Print log for error level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param format
	 *            String format
	 * @param args
	 *            Arguments of message
	 */
	public synchronized void error(Class<?> clazz, String format, Object... args) {
		error(clazz, String.format(format, args));
	}

	/**
	 * Print log for error level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param e
	 *            Exception
	 * @param msg
	 *            message
	 */
	public synchronized void error(Class<?> clazz, Exception e, String msg) {
		print(UtilLogLevel.ERROR, clazz, msg, e);
	}

	/**
	 * Print log for error level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param e
	 *            Exception
	 * @param format
	 *            String format
	 * @param args
	 *            Arguments of message
	 */
	public synchronized void error(Class<?> clazz, Exception e, String format, Object... args) {
		error(clazz, e, String.format(format, args));
	}

	/**
	 * Print log for warning level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param msg
	 *            message
	 */
	public synchronized void warning(Class<?> clazz, String msg) {
		warning(clazz, null, msg);
	}

	/**
	 * Print log for warning level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param format
	 *            String format
	 * @param args
	 *            Arguments of message
	 */
	public synchronized void warning(Class<?> clazz, String format, Object... args) {
		warning(clazz, String.format(format, args));
	}

	/**
	 * Print log for warning level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param e
	 *            Exception
	 * @param msg
	 *            message
	 */
	public synchronized void warning(Class<?> clazz, Exception e, String msg) {
		print(UtilLogLevel.WARN, clazz, msg, e);
	}

	/**
	 * Print log for warning level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param e
	 *            Exception
	 * @param format
	 *            String format
	 * @param args
	 *            Arguments of message
	 */
	public synchronized void warning(Class<?> clazz, Exception e, String format, Object... args) {
		warning(clazz, e, String.format(format, args));
	}

	/**
	 * Print log for developer level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param msg
	 *            message
	 */
	public synchronized void devel(Class<?> clazz, String msg) {
		devel(clazz, null, msg);
	}

	/**
	 * Print log for developer level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param format
	 *            String format
	 * @param args
	 *            Arguments of message
	 */
	public synchronized void devel(Class<?> clazz, String format, Object... args) {
		devel(clazz, String.format(format, args));
	}

	/**
	 * Print log for developer level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param e
	 *            Exception
	 * @param msg
	 *            message
	 */
	public synchronized void devel(Class<?> clazz, Exception e, String msg) {
		print(UtilLogLevel.DEVEL, clazz, msg, e);
	}

	/**
	 * Print log for developer level
	 * 
	 * @param clazz
	 *            Class that prints the log
	 * @param e
	 *            Exception
	 * @param format
	 *            String format
	 * @param args
	 *            Arguments of message
	 */
	public synchronized void devel(Class<?> clazz, Exception e, String format, Object... args) {
		devel(clazz, e, String.format(format, args));
	}
}
