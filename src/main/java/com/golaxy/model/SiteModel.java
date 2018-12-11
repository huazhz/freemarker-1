package com.golaxy.model;

public class SiteModel {
	private String id;
	private String name;
	private String number;

	public SiteModel() {
	}

	public SiteModel(String id, String name, String number) {
		this.id = id;
		this.name = name;
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
