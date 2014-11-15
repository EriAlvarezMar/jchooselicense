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
