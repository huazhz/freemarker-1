package com.golaxy.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.golaxy.config.CustomProperty;
import com.golaxy.utils.CreateData;
import com.golaxy.utils.FileUtil;
import com.golaxy.utils.JDBCUtil;
import com.golaxy.utils.TimeUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class DynamicallyGeneratedWord {
	private Logger logger = Logger.getLogger(DynamicallyGeneratedWord.class);
	private static Configuration freemarkerConfig;

	static {
		freemarkerConfig = new Configuration(Configuration.VERSION_2_3_25);
		freemarkerConfig.setEncoding(Locale.getDefault(), "UTF-8");
	}

	/**
	 * 数据写入模板
	 * 
	 * @param taskid
	 * @param startTimeStamp
	 * @param endTimeStamp
	 * @param targetFilePath
	 * @return
	 */
	public Writer wordCreate(String taskid, Long startTimeStamp, Long endTimeStamp, String targetFilePath) {
		freemarkerConfig.setClassForTemplateLoading(DynamicallyGeneratedWord.class, "/");
		try {

			logger.info("get word model");
			Template template = freemarkerConfig.getTemplate(CustomProperty.templatePath);
			File targetFile = new File(targetFilePath + FileUtil.getFileName());
			Writer out = new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8");
			Map<String, Object> result = createData(taskid, startTimeStamp, endTimeStamp);// 获取word数据
			logger.info("数据写入模板");
			template.process(result, out);
			return out;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 封装word数据
	 * 
	 * @param taskid
	 * @param startTimeStamp
	 * @param endTimeStamp
	 * @return
	 */
	private Map<String, Object> createData(String taskid, Long startTimeStamp, Long endTimeStamp) {
		String taskName = JDBCUtil.getTaskName(Long.valueOf(taskid));
		logger.info("the taskName is " + taskName);
		String jcStart = TimeUtils.getDateStr(startTimeStamp);
		String jcEnd = TimeUtils.getDateStr(endTimeStamp);
		String startTime = TimeUtils.getQuery(startTimeStamp);
		String endTime = TimeUtils.getQuery(endTimeStamp);
		CreateData create = new CreateData(taskid, startTime, endTime, taskName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("jcStart", jcStart);
		result.put("jcEnd", jcEnd);
		result.put("topicName", create.topicName);// mysql中获取
		result.put("total", create.totalDocNum + "");
		result.put("statisticStr", create.statisticStr);
		result.put("pStatistic", create.pstatisticStr);
		result.put("wmTotal", create.MtTotal + "");
		result.put("wtOne", create.wtOne);
		result.put("localStatistic", create.localStatistic);
		result.put("hotStr", create.hotStr);
		result.put("topTitleThree", create.maxComm.getKey());
		result.put("commSameNum", create.maxComm.getValue());
		result.put("tbwbSent", create.tbwbSent);
		result.put("wmSent", create.wmSent);
		result.put("bfbList", create.bfbList);
		result.put("pPic", create.transList);
		result.put("wmwbList", create.wmmbList);
		result.put("siteList", create.siteList);
		result.put("localistTop", create.localListTop10);
		result.put("localist", create.localList);
		result.put("zdhtList", create.zdhtList);
		result.put("commList", create.commList);
		result.put("localistTopSize", create.localListTopSize);
		result.put("wmwbListSize", create.wmmbSize);
		return result;
	}

	public static void main(String[] args) {
		DynamicallyGeneratedWord d = new DynamicallyGeneratedWord();
		Writer wordCreate = d.wordCreate("341", 1541001600000l, 1542593599000l, "Z:\\");
		try {
			wordCreate.flush();
			wordCreate.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
