/**
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE.txt file.
 */

package jchooselicense.model;

import java.util.ArrayList;

import jchooselicense.base.Model;

public class Language extends Model {
	private String name;
	private String startComment;
	private String endComment;
	private String comment;
	private ArrayList<String> extensions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartComment() {
		return startComment;
	}

	public void setStartComment(String startComment) {
		this.startComment = startComment;
	}

	public String getEndComment() {
		return endComment;
	}

	public void setEndComment(String endComment) {
		this.endComment = endComment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ArrayList<String> getExtensions() {
		return extensions;
	}

	public void setExtensions(ArrayList<String> extensions) {
		this.extensions = extensions;
	}

	@Override
	public String toString() {
		return name;
	}

}
