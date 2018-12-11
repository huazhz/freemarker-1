package com.golaxy.model;

public class WmwbModel {
	private String id;
	private String dateStr;
	private String wmnumber;
	private String wbnumber;

	public WmwbModel() {
	}

	public WmwbModel(String id, String dateStr, String wmnumber, String wbnumber) {
		this.id = id;
		this.dateStr = dateStr;
		this.wmnumber = wmnumber;
		this.wbnumber = wbnumber;
	}

	public String getId() {
		return id;
	}

	public String getDateStr() {
		return dateStr;
	}

	public String getWmnumber() {
		return wmnumber;
	}

	public String getWbnumber() {
		return wbnumber;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public void setWmnumber(String wmnumber) {
		this.wmnumber = wmnumber;
	}

	public void setWbnumber(String wbnumber) {
		this.wbnumber = wbnumber;
	}

}
