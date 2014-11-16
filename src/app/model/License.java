/**
 * 
 * License.java
 * 
 * Copyright (c) 2014, Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE.txt file.
 *
 */

package app.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import app.util.UtilConfig;

public class License {
	private File directory;
	private UtilConfig config;

	public License(File directory) {
		config = UtilConfig.getInstance();
		this.directory = directory;
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	@Override
	public String toString() {
		return String.format("%s", directory.getName());
	}

	public void copyLicence(File project, String projectName, String copyright, String year) throws IOException {
		String line;
		BufferedReader br = new BufferedReader(new FileReader(new File(directory, config.get("path.licensetxt"))));
		BufferedWriter out = new BufferedWriter(new FileWriter(new File(project, config.get("path.licensetxt"))));
		while ((line = br.readLine()) != null) {
			line = line.replace("$PROJECTNAME$", projectName).replace("$COPYRIGHT$", copyright).replace("$YEAR$", year);
			out.write(line + "\n");
		}
		out.flush();
		out.close();
		br.close();
	}

	public void setHeader(File source, String projectName, String copyright, String year) throws IOException {
		String line;
		StringBuilder header = new StringBuilder();
		header.append("/**\n * \n");
		BufferedReader brHeader = new BufferedReader(new FileReader(new File(directory, config.get("path.licenseheader"))));
		while ((line = brHeader.readLine()) != null) {
			line = line.replace("$PROJECTNAME$", projectName).replace("$FILENAME$", source.getName()).replace("$COPYRIGHT$", copyright).replace("$YEAR$", year);
			header.append(" * " + line + "\n");
		}
		brHeader.close();
		header.append(" *\n */\n");

		StringBuilder file = new StringBuilder();
		BufferedReader brFile = new BufferedReader(new FileReader(source));

		boolean replace = false;

		String licenseStartToken = "/**";
		String licenseEndToken = "*/";

		if ((line = brFile.readLine()) != null) {
			if (licenseStartToken.equals(line.trim()))
				replace = true;
			else
				file.append(line + "\n");
		}

		while ((line = brFile.readLine()) != null) {
			if (replace) {
				if (licenseEndToken.equals(line.trim()))
					replace = false;
			} else {
				file.append(line + "\n");
			}
		}
		brFile.close();

		BufferedWriter out = new BufferedWriter(new FileWriter(source));
		out.write(header.toString());
		if (file.charAt(0) != '\n')
			out.write("\n");
		out.write(file.toString());
		out.flush();
		out.close();
	}

}
