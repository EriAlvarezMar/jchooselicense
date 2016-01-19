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
