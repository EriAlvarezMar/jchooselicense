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
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import jchooselicense.base.Model;

public class License extends Model {

	private String code;
	private String name;
	private String url;
	private String body;
	private String header;
	private ArrayList<Parameter> parameters;
	private ArrayList<String> attachments;
	private String path;

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
		return String.format("License [code=%s, name=%s, url=%s, body=%s, header=%s, parameters=%s, attachments=%s, path=%s]", code, name, url, body, header, parameters, attachments, path);
	}

	@Override
	public String toString() {
		return code;
	}

	public void writeLicence(String project) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(new File(path, body)));
		BufferedWriter out = new BufferedWriter(new FileWriter(new File(project, body)));

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
			BufferedReader in = new BufferedReader(new FileReader(new File(path, attached)));
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(project, attached)));

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

		BufferedReader brHeader = new BufferedReader(new FileReader(new File(path, this.header)));
		while ((line = brHeader.readLine()) != null) {
			for (Parameter param : parameters) {
				line = line.replace(param.getReference(), param.getValue());
			}
			header.append(lang.getComment() + line + "\n");
		}
		brHeader.close();
		header.append(lang.getEndComment() + "\n");

		StringBuilder file = new StringBuilder();
		BufferedReader brFile = new BufferedReader(new FileReader(source));

		boolean replace = false;

		String licenseStartToken = lang.getStartComment().trim();
		String licenseEndToken = lang.getEndComment().trim();

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
		if (file.length() > 0 && file.charAt(0) != '\n')
			out.write("\n");
		out.write(file.toString());
		out.flush();
		out.close();
	}
}
