package com.golaxy.model;

public class LocalModel {
	private String id;
	private String name;
	private String number;
	private String mtNum;
	private String sjNum;

	public LocalModel() {
	}

	public LocalModel(String id, String name, String number) {
		this.id = id;
		this.name = name;
		this.number = number;
	}

	public LocalModel(String id, String name, String mtNum, String sjNum) {
		this.id = id;
		this.name = name;
		this.mtNum = mtNum;
		this.sjNum = sjNum;
	}

	public LocalModel(String id, String name, String number, String mtNum, String sjNum) {
		this.id = id;
		this.name = name;
		this.number = number;
		this.mtNum = mtNum;
		this.sjNum = sjNum;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

	public String getMtNum() {
		return mtNum;
	}

	public String getSjNum() {
		return sjNum;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setMtNum(String mtNum) {
		this.mtNum = mtNum;
	}

	public void setSjNum(String sjNum) {
		this.sjNum = sjNum;
	}

}
