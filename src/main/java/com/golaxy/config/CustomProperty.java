package com.golaxy.config;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;



public class CustomProperty {
	public static final String allChannels;
	public static final String wmwbChannels;
	public static final String siteChannel;
	public static final String zdwzAddress;
	public static final String bdrdAddress;
	public static final String commentChannels;
	public static final String requestHost;
	public static final String simRequestHost;
	public static final String propagationAddress;
	public static final String commentAddress;
	public static final String smidAddress;
	public static Connection conn;
	public static final String templatePath;
	public static final String maxCommentEsAddress;
	public static final String esEvidStatisticAddress;

	public static Map<String, String> zhChMap = new HashMap<>();

	public static final String mysqlJdbcDriver;
	public static final String mysqlUrl;
	public static final String mysqlUsername;
	public static final String mysqlPasswd;
	public static final String FILE_NAME = "conf/yuqingword.properties";
	static {
		Properties properties = new Properties();
		try {
			PropertyConfigurator.configure("conf/log4j.properties");
			properties.load(new FileInputStream(FILE_NAME));
			allChannels = properties.getProperty("all.channels");
			wmwbChannels = properties.getProperty("wmwb.channels");
			siteChannel = properties.getProperty("site.channel");
			zdwzAddress = properties.getProperty("zdwz.address");
			bdrdAddress = properties.getProperty("bdrd.address");
			requestHost = properties.getProperty("request.host");
			simRequestHost = properties.getProperty("smid.request.host");
			propagationAddress = properties.getProperty("propagation.address");
			commentAddress = properties.getProperty("comment.address");
			smidAddress = properties.getProperty("smid.address");
			commentChannels = properties.getProperty("comment.channels");
			mysqlJdbcDriver = properties.getProperty("mysql.jdbc.driver");
			mysqlUrl = properties.getProperty("mysql.url");
			mysqlUsername = properties.getProperty("mysql.username");
			mysqlPasswd = properties.getProperty("mysql.passwd");
			templatePath = properties.getProperty("template.path");
			maxCommentEsAddress = properties.getProperty("max.comment.es.query.address");
			esEvidStatisticAddress = properties.getProperty("es.evid.statistic.address");
			zhChMap.put("app_news", "APP新闻");
			zhChMap.put("forum", "贴吧");
			zhChMap.put("bbs", "论坛");
			zhChMap.put("weibo", "微博");
			zhChMap.put("wenda", "问答");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
	}
}
