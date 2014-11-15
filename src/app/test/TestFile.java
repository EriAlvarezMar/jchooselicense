/**
 * 
 * TestFile.java
 * 
 * Copyright (c) 2014, Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE.txt file.
 *
 */

package app.test;

import java.io.File;

public class TestFile {

	public static void editFile(File file) {
		if (!file.isFile())
			return;
		System.out.println(file.getPath());
	}

	public static void findFile(File file) {
		if (file.isFile())
			editFile(file);
		else
			for (File f : file.listFiles())
				findFile(f);
	}

	public static void main(String[] args) {
		File file = new File(".");
		findFile(file);
	}

}
