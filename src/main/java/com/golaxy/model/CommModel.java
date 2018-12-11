package com.golaxy.model;

public class CommModel {
	private String channel;
	private String pNum;
	private String nNum;
	private String negNum;
	private String total;

	public CommModel() {
	}

	public CommModel(String channel, String pNum, String nNum, String negNum, String total) {
		this.channel=channel;
		this.pNum = pNum;
		this.nNum = nNum;
		this.negNum = negNum;
		this.total = total;
	}
	public String getChannel() {
		return channel;
	}

	public String getpNum() {
		return pNum;
	}

	public String getnNum() {
		return nNum;
	}

	public String getNegNum() {
		return negNum;
	}

	public String getTotal() {
		return total;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setpNum(String pNum) {
		this.pNum = pNum;
	}

	public void setnNum(String nNum) {
		this.nNum = nNum;
	}

	public void setNegNum(String negNum) {
		this.negNum = negNum;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
