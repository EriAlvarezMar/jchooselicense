/**
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE file.
 */

package jchooselicense.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import jchooselicense.base.Model;

public class License extends Model {

	public static final String CHARSET_UTF_8 = "UTF-8";
	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";

	private String code;
	private String charset;
	private String name;
	private String url;
	private String body;
	private String header;
	private ArrayList<Parameter> parameters;
	private ArrayList<String> attachments;
	private String path;

	public License() {
		this.charset = CHARSET_UTF_8;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}

	public ArrayList<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<String> attachments) {
		this.attachments = attachments;
	}

	public String toFullString() {
		return String.format(
				"License [code=%s, name=%s, url=%s, body=%s, header=%s, parameters=%s, attachments=%s, path=%s]", code,
				name, url, body, header, parameters, attachments, path);
	}

	@Override
	public String toString() {
		return code;
	}

	public void writeLicence(String project) throws Exception {
		BufferedReader in = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(path, body)), charset));
		BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(new File(project, body)), charset));

		String line;
		while ((line = in.readLine()) != null) {
			for (Parameter param : parameters) {
				line = line.replace(param.getReference(), param.getValue());
			}
			out.write(line + "\n");
		}
		out.flush();
		out.close();
		in.close();

		writeAttachments(project);

	}

	private void writeAttachments(String project) throws Exception {
		for (String attached : attachments) {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(path, attached)), charset));
			BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(project, attached)), charset));

			String line;
			while ((line = in.readLine()) != null) {
				for (Parameter param : parameters) {
					line = line.replace(param.getReference(), param.getValue());
				}
				out.write(line + "\n");
			}
			out.flush();
			out.close();
			in.close();
		}
	}

	public void writeHeader(File source, Language lang, boolean writeFileName) throws Exception {
		String line;
		StringBuilder header = new StringBuilder();
		header.append(lang.getStartComment());
		header.append("\n");

		if (writeFileName) {
			header.append(lang.getComment());
			header.append(source.getName());
			header.append("\n");
			header.append(lang.getComment());
			header.append("\n");
		}

		BufferedReader brHeader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(path, this.header)), charset));
		while ((line = brHeader.readLine()) != null) {
			for (Parameter param : parameters) {
				line = line.replace(param.getReference(), param.getValue());
			}
			header.append(lang.getComment() + line + "\n");
		}
		brHeader.close();
		header.append(lang.getEndComment() + "\n");

		StringBuilder file = new StringBuilder();
		BufferedReader brFile = new BufferedReader(new InputStreamReader(new FileInputStream(source), charset));

		boolean replace = false;

		String licenseStartToken = lang.getStartComment().trim();
		String licenseEndToken = lang.getEndComment().trim();
		int countLinesStart = licenseStartToken.length() - licenseStartToken.replace("\n", "").length() + 1;
		String startLineFiles = "";

		for (int i = 0; i < countLinesStart && (line = brFile.readLine()) != null; i++) {
			startLineFiles += line + "\n";
		}

		if (licenseStartToken.equals(startLineFiles.trim())) {
			replace = true;
		} else if (startLineFiles.trim() != null) {
			file.append(startLineFiles.trim() + "\n");
		}

		while ((line = brFile.readLine()) != null) {
			if (replace) {
				if (licenseEndToken.equals(line.trim()))
					replace = false;
			} else if (line != null) {
				file.append(line + "\n");
			}
		}
		brFile.close();

		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(source), charset));
		out.write(header.toString());
		if (file.length() > 0 && file.charAt(0) != '\n')
			out.write("\n");
		out.write(file.toString());
		out.flush();
		out.close();
	}
}
