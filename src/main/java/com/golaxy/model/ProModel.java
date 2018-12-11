package com.golaxy.model;

public class ProModel {
	private String id;
	private String dateStr;
	private String number;

	public ProModel() {
	}

	public ProModel(String id, String dateStr, String number) {
		this.id = id;
		this.dateStr = dateStr;
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public String getDateStr() {
		return dateStr;
	}

	public String getNumber() {
		return number;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
