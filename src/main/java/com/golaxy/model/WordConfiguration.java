package com.golaxy.model;

import java.sql.Connection;
import java.sql.DriverManager;

import com.golaxy.config.CustomProperty;

public class WordConfiguration {

	private String allChannels;
	private String wmwbChannels;
	private String siteChannel;
	private String zdwzAddress;
	private String bdrdAddress;
	private String commentChannels;
	private String requestHost;
	private String simRequestHost;
	private String propagationAddress;
	private String commentAddress;
	private String smidAddress;
	private Connection conn;
	private String templatePath;
	private String mysqlJdbcDriver;
	private String mysqlUrl;
	private String mysqlUsername;
	private String mysqlPasswd;
	private String maxCommentEsAddress;
	private String esEvidStatisticAddress;
	private static volatile WordConfiguration config = null;

	private WordConfiguration() {
	}

	public static WordConfiguration getInstance() {
		if (config == null) {
			synchronized (WordConfiguration.class) {
				if (config == null) {
					config = init();
					try {
						Class.forName(config.getMysqlJdbcDriver());
						Connection mysqlConn = DriverManager.getConnection(config.getMysqlUrl(),
								config.getMysqlUsername(), config.getMysqlPasswd());
						config.setConn(mysqlConn);
						return config;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return config;
	}
	public String getAllChannels() {
		return allChannels;
	}

	public String getWmwbChannels() {
		return wmwbChannels;
	}

	public String getSiteChannel() {
		return siteChannel;
	}

	public String getZdwzAddress() {
		return zdwzAddress;
	}

	public String getBdrdAddress() {
		return bdrdAddress;
	}

	public String getCommentChannels() {
		return commentChannels;
	}

	public String getRequestHost() {
		return requestHost;
	}

	public String getSimRequestHost() {
		return simRequestHost;
	}

	public String getPropagationAddress() {
		return propagationAddress;
	}

	public String getCommentAddress() {
		return commentAddress;
	}

	public String getSmidAddress() {
		return smidAddress;
	}

	public Connection getConn() {
		return conn;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public String getMysqlJdbcDriver() {
		return mysqlJdbcDriver;
	}

	public String getMysqlUrl() {
		return mysqlUrl;
	}

	public String getMysqlUsername() {
		return mysqlUsername;
	}

	public String getMysqlPasswd() {
		return mysqlPasswd;
	}

	public void setAllChannels(String allChannels) {
		this.allChannels = allChannels;
	}

	public void setWmwbChannels(String wmwbChannels) {
		this.wmwbChannels = wmwbChannels;
	}

	public void setSiteChannel(String siteChannel) {
		this.siteChannel = siteChannel;
	}

	public void setZdwzAddress(String zdwzAddress) {
		this.zdwzAddress = zdwzAddress;
	}

	public void setBdrdAddress(String bdrdAddress) {
		this.bdrdAddress = bdrdAddress;
	}

	public void setCommentChannels(String commentChannels) {
		this.commentChannels = commentChannels;
	}

	public void setRequestHost(String requestHost) {
		this.requestHost = requestHost;
	}

	public void setSimRequestHost(String simRequestHost) {
		this.simRequestHost = simRequestHost;
	}

	public void setPropagationAddress(String propagationAddress) {
		this.propagationAddress = propagationAddress;
	}

	public void setCommentAddress(String commentAddress) {
		this.commentAddress = commentAddress;
	}

	public void setSmidAddress(String smidAddress) {
		this.smidAddress = smidAddress;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public void setMysqlJdbcDriver(String mysqlJdbcDriver) {
		this.mysqlJdbcDriver = mysqlJdbcDriver;
	}

	public void setMysqlUrl(String mysqlUrl) {
		this.mysqlUrl = mysqlUrl;
	}

	public void setMysqlUsername(String mysqlUsername) {
		this.mysqlUsername = mysqlUsername;
	}

	public void setMysqlPasswd(String mysqlPasswd) {
		this.mysqlPasswd = mysqlPasswd;
	}

	public WordConfiguration getDefaultInstance() {

		return new WordConfiguration();
	}


	public String getMaxCommentEsAddress() {
		return maxCommentEsAddress;
	}

	public void setMaxCommentEsAddress(String maxCommentEsAddress) {
		this.maxCommentEsAddress = maxCommentEsAddress;
	}

	public String getEsEvidStatisticAddress() {
		return esEvidStatisticAddress;
	}

	public void setEsEvidStatisticAddress(String esEvidStatisticAddress) {
		this.esEvidStatisticAddress = esEvidStatisticAddress;
	}

	private static WordConfiguration init() {
		config = new WordConfiguration();
		config.setAllChannels(CustomProperty.allChannels);
		config.setWmwbChannels(CustomProperty.wmwbChannels);
		config.setSiteChannel(CustomProperty.siteChannel);
		config.setZdwzAddress(CustomProperty.zdwzAddress);
		config.setBdrdAddress(CustomProperty.bdrdAddress);
		config.setCommentChannels(CustomProperty.commentChannels);
		config.setRequestHost(CustomProperty.requestHost);
		config.setSimRequestHost(CustomProperty.simRequestHost);
		config.setPropagationAddress(CustomProperty.propagationAddress);
		config.setCommentAddress(CustomProperty.commentAddress);
		config.setSmidAddress(CustomProperty.smidAddress);
		config.setTemplatePath(CustomProperty.templatePath);
		config.setMysqlJdbcDriver(CustomProperty.mysqlJdbcDriver);
		config.setMysqlUrl(CustomProperty.mysqlUrl);
		config.setMysqlUsername(CustomProperty.mysqlUsername);
		config.setMysqlPasswd(CustomProperty.mysqlPasswd);
		config.setMaxCommentEsAddress(CustomProperty.maxCommentEsAddress);
		config.setEsEvidStatisticAddress(CustomProperty.esEvidStatisticAddress);
		return config;

	}

}
