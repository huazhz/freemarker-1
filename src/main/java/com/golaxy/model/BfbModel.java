package com.golaxy.model;

public class BfbModel {
	private String id;
	private String channelName;
	private String number;
	private String per;

	public BfbModel() {
	}

	public BfbModel(String id, String channelName, String number, String per) {
		this.id = id;
		this.channelName = channelName;
		this.number = number;
		this.per = per;
	}

	public String getId() {
		return id;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getNumber() {
		return number;
	}

	public String getPer() {
		return per;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setPer(String per) {
		this.per = per;
	}

}
