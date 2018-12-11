package com.golaxy.model;

public class HotReport {
	private String title;
	private String docNumber;

	public HotReport() {
	}

	public HotReport(String title, String docNumber) {
		this.title = title;
		this.docNumber = docNumber;
	}

	public String getTitle() {
		return title;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

}
