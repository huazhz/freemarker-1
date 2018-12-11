package com.golaxy.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.golaxy.config.CustomProperty;
import com.golaxy.model.BfbModel;
import com.golaxy.model.CommModel;
import com.golaxy.model.HotReport;
import com.golaxy.model.LocalModel;
import com.golaxy.model.ProModel;
import com.golaxy.model.SiteModel;
import com.golaxy.model.WmwbModel;
import com.golaxy.model.WordConfiguration;
import com.golaxy.model.ZdhtModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ict.http.HttpClient;
import ict.http.HttpParam;
import ict.http.Response;

public class CreateData {
	private Logger logger = Logger.getLogger(CreateData.class);
	private String taskId;
	private HttpClient client = HttpClient.createInstance(5, 10, 10000, 60000);
	private Integer total = 0;

	public String startTime;
	public String endTime;
	public String topicName;
	public Integer totalDocNum = 0;
	public Integer MtTotal = 0;
	public List<ProModel> transList = new LinkedList<>();
	public List<BfbModel> bfbList = new LinkedList<>();
	public List<WmwbModel> wmmbList = new LinkedList<>();
	public List<SiteModel> siteList = new LinkedList<>();
	public List<LocalModel> localListTop10 = new LinkedList<>();
	public List<LocalModel> localList = new LinkedList<>();
	public List<ZdhtModel> zdhtList = new LinkedList<>();
	public List<CommModel> commList = new LinkedList<>();
	public List<HotReport> hotList = new LinkedList<>();
	public String transListSize = 0 + "";
	public String bfbListSize = 0 + "";
	public String wmmbSize = 0 + "";
	public String siteSize = 0 + "";
	public String localListTopSize = 0 + "";
	public String localListSize = 0 + "";
	public String zdhtListSize = 0 + "";
	public String commListSize = 0 + "";
	public String statisticStr = "";
	public String pstatisticStr = "";
	public String wtOne = "";
	public String localStatistic = "";
	public String tbwbSent = "";
	public String wmSent = "";
	public String hotStr = "";
	public Pair<String, String> maxComm = new Pair<>();
	public WordConfiguration config = WordConfiguration.getInstance();

	public CreateData(String taskId, String startTime, String endTime, String topicName) {
		this.taskId = taskId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.topicName = topicName;
		logger.info("Data Initialization ...");
		init(this.taskId, this.startTime, this.endTime);
		logger.info("finished");
	}

	private void init(String taskId, String startTime, String endTime) {
		transList = getTransList(taskId, startTime, endTime);// 图1信息传播总量趋势 表1信息量日统计表
		bfbList = getbfbList(taskId, startTime, endTime);// 图2传播载体分布 表2传播载体分布表
		getMTTotal();
		wmmbList = getWmwbList(taskId, startTime, endTime);// 图3网媒微博传播趋势 表3网媒微博传播趋势表
		siteList = getSiteList(taskId, startTime, endTime);// 表4传播媒体列表
		localListTop10 = getLocalListTop10(taskId, startTime, endTime);// 图4媒体报道所属地地域分析
		localList = getLocalList(taskId, startTime, endTime);// 表5媒体报道所属地域分布表
		zdhtList = getZdhtList(taskId, startTime, endTime);// 表6 重点新闻传播话题
		commList = getCommList(taskId, startTime, endTime);// 表7 评论统计表
		hotList = gethotList(taskId, startTime, endTime);// 报道热点
		statisticStr = getStatisticStr();// 传播载体分布
		pstatisticStr = getPStatistic();// 重点传播载体
		wtOne = getWtOneStr();// 传播媒体分布
		localStatistic = getLocaStatisticStr();// 地域分布
		tbwbSent = getwbForumSentiment(taskId, startTime, endTime);// 评论热点第一句
		wmSent = getappSentiment(taskId, startTime, endTime);// 评论热点第二句
		transListSize = (transList != null) ? transList.size() + "" : 0 + "";
		bfbListSize = (bfbList != null) ? bfbList.size() + "" : 0 + "";
		wmmbSize = (wmmbList != null) ? wmmbList.size() + "" : 0 + "";
		siteSize = (siteList != null) ? siteList.size() + "" : 0 + "";
		localListTopSize = (localListTop10 != null) ? localListTop10.size() + "" : 0 + "";
		localListSize = (localList != null) ? localList.size() + "" : 0 + "";
		zdhtListSize = (zdhtList != null) ? zdhtList.size() + "" : 0 + "";
		commListSize = (commList != null) ? commList.size() + "" : 0 + "";
		hotStr = getHotStatistic();
	}

	private void getMTTotal() {
		for (BfbModel bfb : bfbList) {
			String channelName = bfb.getChannelName();
			if (channelName.equals("新闻")) {
				MtTotal = Integer.valueOf(bfb.getNumber());
			}
		}
	}
	private Pair<String, String> getMaxComm(String id, String title) {
		Pair<String, String> pair = new Pair<>();
		JsonObject resultObj = EsUtil.getQueryResponse(config.getMaxCommentEsAddress() + id);
		if (resultObj != null) {
			JsonObject source = resultObj.get("_source").getAsJsonObject();
			if (source.has("evid")) {
				id = source.get("evid").getAsJsonArray().get(0).getAsString();
				String body = "{\"query\": {\"terms\": {\"evid\": [\"" + id + "\"]}},\"size\": 0}";
				Integer number = EsUtil.getEvidNumberResponse(config.getEsEvidStatisticAddress(), body);
				pair.setKey(title);
				pair.setValue(number + "");
				return pair;
			} else if (source.has("smid")) {
				id = source.get("smid").getAsString();
			} else {

			}
		}
		String url = config.getSimRequestHost() + config.getSmidAddress() + "?ids=" + id;
		Response response = client.post(url, "", "application/x-www-form-urlencoded", "UTF-8");
		if (response.getStatusCode() == 200) {
			JsonArray datas = JsonUtils.str2Obj(response.getResponseAsString()).get("datas").getAsJsonArray();
			if (datas.size() == 0) {
				pair.setKey(null);
				pair.setValue(null);
				return pair;
			}
			JsonObject simObj = datas.get(0).getAsJsonObject();
			String docNumber = simObj.get("value").getAsString();
			pair.setKey(title);
			pair.setValue(docNumber);
			return pair;
		}
		pair.setKey(null);
		pair.setValue(null);
		return pair;
	}

	private String getStatisticStr() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bfbList.size(); i++) {
			BfbModel bfbModel = bfbList.get(i);
			if(i!=bfbList.size()-1) {
				sb.append(bfbModel.getChannelName() + bfbModel.getNumber() + "条，");
			} else {
				sb.append(bfbModel.getChannelName() + bfbModel.getNumber() + "条");
			}
		}
		return sb.toString();
	}

	private String getPStatistic() {
		StringBuffer sb = new StringBuffer();
		if (bfbList.size() > 3) {
			BfbModel model1 = bfbList.get(0);
			BfbModel model2 = bfbList.get(1);
			BfbModel model3 = bfbList.get(2);
			sb.append(model1.getChannelName() + "、" + model2.getChannelName() + "、" + model3.getChannelName()
					+ "，其占比分别为" + numberFormat(Integer.valueOf(model1.getNumber()), total) + "%、"
					+ numberFormat(Integer.valueOf(model2.getNumber()), total) + "%、"
					+ numberFormat(Integer.valueOf(model3.getNumber()), total)
					+ "%");
		}
		return sb.toString();
	}

	private String getWtOneStr() {
		StringBuffer sb = new StringBuffer();
		if (siteList.size() > 3) {
			SiteModel siteModel = siteList.get(3);
			SiteModel max = siteList.get(0);
			sb.append(siteModel.getName() + "等网络平台，其中【" + max.getName() + "】以" + max.getNumber());
		}
		return sb.toString();
	}

	private String getLocaStatisticStr() {
		StringBuffer sb = new StringBuffer();
		if (localListTop10.size() > 4) {
			LocalModel location1 = localListTop10.get(0);
			LocalModel location2 = localListTop10.get(1);
			LocalModel location3 = localListTop10.get(2);
			LocalModel location4 = localListTop10.get(3);
			String location1Name = location1.getName();
			String location2Name = location2.getName();
			String location3Name = location3.getName();
			String location4Name = location4.getName();
			Integer lo1Num = Integer.parseInt(location1.getNumber());
			Integer lo2Num = Integer.parseInt(location2.getNumber());
			Integer lo3Num = Integer.parseInt(location3.getNumber());
			Integer lo4Num = Integer.parseInt(location4.getNumber());
			Double lo1Per = Double.valueOf(numberFormat(lo1Num, MtTotal));
			Double lo2Per = Double.valueOf(numberFormat(lo2Num, MtTotal));
			Double lo3Per = Double.valueOf(numberFormat(lo3Num, MtTotal));
			Double lo4Per = Double.valueOf(numberFormat(lo4Num, MtTotal));
			Double totalPer = Double.valueOf(numberFormat(lo1Num + lo2Num + lo3Num + lo4Num, MtTotal));
			sb.append(location1Name + "、" + location2Name + "、" + location3Name + "、" + location4Name + "等地是报道【"
					+ topicName + "】媒体所在的重点区域。其中媒体在" + location1Name + "发布" + lo1Num + "篇，占总比" + lo1Per + "%，"
					+ location2Name + "、" + location3Name + "、" + location4Name + "每个地区分别为" + lo2Num + "、" + lo3Num
					+ "、" + lo4Num + "篇，区域占总比分别为" + lo2Per + "%、" + lo3Per + "%、" + lo4Per + "%。以上4个地区占总比" + totalPer
					+ "%");
		}
		return sb.toString();
	}

	private String getHotStatistic() {
		// 
		StringBuffer sb = new StringBuffer();
		if (hotList.size() == 0) {
			sb.append("其中报道最多的标题为《标题》，相同或类似标题的报道数量分别为0篇。");
		}
		else if (hotList.size() == 1) {
			HotReport hotReport = hotList.get(0);
			String title = hotReport.getTitle();
			String docNumber = hotReport.getDocNumber();
			sb.append("其中报道最多的标题为《" + title + "》，相同或类似标题的报道数量分别为" + docNumber + "篇。");
		} else {
			HotReport hotReport1 = hotList.get(0);
			HotReport hotReport2 = hotList.get(1);
			String title1 = hotReport1.getTitle();
			String docNumber1 = hotReport1.getDocNumber();
			String title2 = hotReport2.getTitle();
			String docNumber2 = hotReport2.getDocNumber();
			sb.append("其中报道最多的标题为《" + title1 + "》和《" + title2 + "》，相同或类似标题的报道数量分别为" + docNumber1 + "篇和" + docNumber2
					+ "篇。");
		}
		return sb.toString();
	}
	private String getwbForumSentiment(String taskId, String startTime, String endTime) {
		String Url = config.getRequestHost() + config.getCommentAddress() + "weibo,forum/sentiment";
		HttpParam param1 = new HttpParam("taskId", taskId);
		HttpParam param2 = new HttpParam("startTime", startTime);
		HttpParam param3 = new HttpParam("endTime", endTime);
		HttpParam[] params = new HttpParam[] { param1, param2, param3 };
		Response numResponse = client.get(Url,params);
		if (numResponse.getStatusCode() == 200) {
			JsonObject sentObj = JsonUtils.str2Obj(numResponse.getResponseAsString()).get("metadata")
					.getAsJsonArray().get(0).getAsJsonObject();
			if (sentObj.has("values")) {
				String sentiment = sentObj.get("values").getAsJsonArray().get(0).getAsJsonObject().get("key")
						.getAsString();
				return sentiment;
			}
		}
		return "";
	}

	private String getappSentiment(String taskId, String startTime, String endTime) {
		String Url = config.getRequestHost() + config.getCommentAddress() + "app_news/sentiment";
		HttpParam param1 = new HttpParam("taskId", taskId);
		HttpParam param2 = new HttpParam("startTime", startTime);
		HttpParam param3 = new HttpParam("endTime", endTime);
		HttpParam[] params = new HttpParam[] { param1, param2, param3 };
		Response numResponse = client.get(Url,params);
		if (numResponse.getStatusCode() == 200) {
			JsonObject sentObj = JsonUtils.str2Obj(numResponse.getResponseAsString()).get("metadata").getAsJsonArray()
					.get(0).getAsJsonObject();
			if (sentObj.has("values")) {
				String sentiment = sentObj.get("values").getAsJsonArray().get(0).getAsJsonObject().get("key")
						.getAsString();
				return sentiment;
			}
		}
		return "";
	}


	private List<ProModel> getTransList(String taskId, String startTime, String endTime) {
		logger.info("初始化传播数据列表");
		String url = config.getRequestHost() + config.getPropagationAddress() + config.getAllChannels()
				+ "/time";
		HttpParam param1 = new HttpParam("taskId", taskId);
		HttpParam param2 = new HttpParam("startTime", startTime);
		HttpParam param3 = new HttpParam("endTime", endTime);
		HttpParam param4 = new HttpParam("interval", "day");
		HttpParam[] params = new HttpParam[] { param1, param2, param3, param4 };
		Response response = client.get(url, params);
		logger.info("the request url is:" + url);
		if (response.getStatusCode() == 200) {
			String responseStr = response.getResponseAsString();
			JsonArray metaArray = JsonUtils.str2Obj(responseStr).get("metadata").getAsJsonArray();
			JsonObject asJsonObject = metaArray.get(0).getAsJsonObject();
			if (asJsonObject.has("values")) {
				JsonArray timeArray = asJsonObject.get("values").getAsJsonArray();
				for (int i = 0; i < timeArray.size(); i++) {
					JsonObject timeObj = timeArray.get(i).getAsJsonObject();
					String dateStr = timeObj.get("key").getAsString().replaceAll("00:00:00", "");
					Integer numValue = timeObj.get("value").getAsInt();
					String number = String.valueOf(numValue);
					totalDocNum += numValue;
					ProModel trans = new ProModel(String.valueOf(i), dateStr, number);
					transList.add(trans);
				}
			}
		} else {
			logger.error("request error");
			logger.error("the error message:" + response.getErrorMsg());
			return new ArrayList<>();
		}
		return transList;
	}

	private List<BfbModel> getbfbList(String taskId, String startTime, String endTime) {
		logger.info("初始化通道百分比列表");
		String url = config.getRequestHost() + config.getPropagationAddress() + config.getAllChannels()
				+ "/channel";

		HttpParam param1 = new HttpParam("taskId", taskId);
		HttpParam param2 = new HttpParam("startTime", startTime);
		HttpParam param3 = new HttpParam("endTime", endTime);
		HttpParam[] params = new HttpParam[] { param1, param2, param3 };
		Response response = client.get(url, params);
		logger.info("the request url is:" + url);
		if (response.getStatusCode() == 200) {
			String responseStr = response.getResponseAsString();
			JsonArray metaArray = JsonUtils.str2Obj(responseStr).get("metadata").getAsJsonArray();
			JsonObject asJsonObject = metaArray.get(0).getAsJsonObject();
			if (asJsonObject.has("values")) {
				JsonArray channelArray = asJsonObject.get("values").getAsJsonArray();
				for (JsonElement jsonElement : channelArray) {
					int value = jsonElement.getAsJsonObject().get("value").getAsInt();
					total += value;
				}
				for (int i = 0; i < channelArray.size(); i++) {
					JsonObject channelObj = channelArray.get(i).getAsJsonObject();
					String channelName = channelObj.get("key").getAsString();
					int value = channelObj.get("value").getAsInt();
					String number = String.valueOf(value);
					BfbModel bfb = new BfbModel(String.valueOf(i), channelName, number,
							Double.valueOf(numberFormat2(value, total)) + "");
					bfbList.add(bfb);
				}
			}

		} else {
			logger.error("request error");
			logger.error("the error message:" + response.getErrorMsg());
			return new ArrayList<>();
		}
		return bfbList;
	}

	private List<WmwbModel> getWmwbList(String taskId, String startTime, String endTime) {
		logger.info("初始化网媒微博数据列表");
		String[] channels = config.getWmwbChannels().split(",");
		Response wmResponse = null;
		Response wbResponse = null;
		String wmurl = config.getRequestHost() + config.getPropagationAddress() + channels[0] + "/time";
		HttpParam param1 = new HttpParam("taskId", taskId);
		HttpParam param2 = new HttpParam("startTime", startTime);
		HttpParam param3 = new HttpParam("endTime", endTime);
		HttpParam param4 = new HttpParam("interval", "day");
		HttpParam[] params = new HttpParam[] { param1, param2, param3, param4 };
		wmResponse = client.get(wmurl, params);
		String wburl = config.getRequestHost() + config.getPropagationAddress() + channels[1] + "/time";
		wbResponse = client.get(wburl, params);
		logger.info("the request url is:" + wmurl);
		logger.info("the request url is:" + wburl);
		if (wmResponse.getStatusCode() == 200 && wbResponse.getStatusCode() == 200) {
			String wmresponseStr = wmResponse.getResponseAsString();
			String wbresponseStr = wbResponse.getResponseAsString();
			JsonArray wmMetaArray = JsonUtils.str2Obj(wmresponseStr).get("metadata").getAsJsonArray();
			JsonArray wbMateArray = JsonUtils.str2Obj(wbresponseStr).get("metadata").getAsJsonArray();
			JsonObject wmasJsonObject = wmMetaArray.get(0).getAsJsonObject();
			JsonObject wbasJsonObject = wbMateArray.get(0).getAsJsonObject();
			if (wmasJsonObject.has("values") && wbasJsonObject.has("values")) {
				JsonArray wmtimeArray = wmasJsonObject.get("values").getAsJsonArray();
				JsonArray wbtimeArray = wbasJsonObject.get("values").getAsJsonArray();
				for (int i = 0; i < wmtimeArray.size(); i++) {
					JsonObject wmtimeObj = wmtimeArray.get(i).getAsJsonObject();
					JsonObject wbtimeObj = wbtimeArray.get(i).getAsJsonObject();
					String wmdateStr = wmtimeObj.get("key").getAsString().replaceAll("00:00:00", "");
					String wmnumber = String.valueOf(wmtimeObj.get("value").getAsInt());
					String wbnumber = String.valueOf(wbtimeObj.get("value").getAsInt());
					WmwbModel wmwb = new WmwbModel(String.valueOf(i), wmdateStr, wmnumber, wbnumber);
					wmmbList.add(wmwb);
				}
			}
		} else {
			logger.error("request error");
			logger.error("the error message:" + wmResponse.getErrorMsg());
			logger.error("the error message:" + wbResponse.getErrorMsg());
			return new ArrayList<>();
		}
		return wmmbList;
	}

	private List<SiteModel> getSiteList(String taskId, String startTime, String endTime) {
		logger.info("初始化传播媒体列表");
		String url = config.getRequestHost() + config.getPropagationAddress() + config.getSiteChannel()
				+ "/sitename";
		HttpParam param1 = new HttpParam("taskId", taskId);
		HttpParam param2 = new HttpParam("startTime", startTime);
		HttpParam param3 = new HttpParam("endTime", endTime);
		HttpParam[] params = new HttpParam[] { param1, param2, param3 };
		Response response = client.get(url, params);
		logger.info("the request url is:" + url);
		if (response.getStatusCode() == 200) {
			String responseStr = response.getResponseAsString();
			JsonArray mateArray = JsonUtils.str2Obj(responseStr).get("metadata").getAsJsonArray();
			JsonObject asJsonObject = mateArray.get(0).getAsJsonObject();
			if (asJsonObject.has("values")) {
				JsonArray channelArray = asJsonObject.get("values").getAsJsonArray();
				Integer total = 0;
				for (JsonElement jsonElement : channelArray) {
					int value = jsonElement.getAsJsonObject().get("value").getAsInt();
					total += value;
				}
				for (int i = 0; i < channelArray.size(); i++) {
					JsonObject channelObj = channelArray.get(i).getAsJsonObject();
					String siteName = channelObj.get("key").getAsString();
					int value = channelObj.get("value").getAsInt();
					String number = String.valueOf(value);
					SiteModel site = new SiteModel(String.valueOf(i + 1), siteName, number);
					siteList.add(site);
				}
			}

		} else {
			logger.error("request error");
			logger.error("the error message:" + response.getErrorMsg());
			return new ArrayList<>();

		}
		return siteList;
	}

	private List<LocalModel> getLocalListTop10(String taskId, String startTime, String endTime) {
		logger.info("初始化前十地域信息列表");
		String[] split = config.getWmwbChannels().split(",");
		String channel = "";
		for (String string : split) {
			if (string.equals("news")) {
				channel = string;
			}
		}
		String mturl = config.getRequestHost() + config.getPropagationAddress() + channel
				+ "/location";
		HttpParam param1 = new HttpParam("taskId", taskId);
		HttpParam param2 = new HttpParam("startTime", startTime);
		HttpParam param3 = new HttpParam("endTime", endTime);
		HttpParam param4 = new HttpParam("interval", "day");
		HttpParam[] params = new HttpParam[] { param1, param2, param3, param4 };
		Response mtResponse = client.get(mturl, params);
		logger.info("the request url is:" + mturl);
		if (mtResponse.getStatusCode() == 200) {
			String mtresponseStr = mtResponse.getResponseAsString();
			JsonArray mttimeArray = JsonUtils.str2Obj(mtresponseStr).get("metadata").getAsJsonArray();
			if (mttimeArray.size() == 0) {
				return new ArrayList<>();
			}
			JsonObject asJsonObject = mttimeArray.get(0).getAsJsonObject();
			if (asJsonObject.has("values")) {
				JsonArray valuesArray = asJsonObject.get("values").getAsJsonArray();
				for (int i = 0; i < 10; i++) {
					JsonObject mttimeObj = valuesArray.get(i).getAsJsonObject();
					String location = mttimeObj.get("key").getAsString();
					Integer mtValue = mttimeObj.get("value").getAsInt();
					String mtnumber = String.valueOf(mtValue);
					LocalModel local = new LocalModel(String.valueOf(i), location, mtnumber);
					localListTop10.add(local);
				}
			}
		} else {
			logger.error("request error");
			logger.error("the error message:" + mtResponse.getErrorMsg());
			return new ArrayList<>();
		}
		return localListTop10;
	}

	private List<LocalModel> getLocalList(String taskId, String startTime, String endTime) {
		logger.info("初始化地域分布列表");
		String[] channels = config.getWmwbChannels().split(",");
		Map<String, Pair<String, String>> mtsj = new LinkedHashMap<>();
		HttpParam param1 = new HttpParam("taskId", taskId);
		HttpParam param2 = new HttpParam("startTime", startTime);
		HttpParam param3 = new HttpParam("endTime", endTime);
		HttpParam[] params = new HttpParam[] { param1, param2, param3 };
		String mturl = config.getRequestHost() + config.getPropagationAddress() + channels[0]
				+ "/location";
		String sjurl = config.getRequestHost() + config.getPropagationAddress() + channels[1]
				+ "/location";
		Response mtresponse = client.get(mturl, params);
		Response sjresponse = client.get(sjurl, params);
		logger.info("the request url is:" + mturl);
		logger.info("the request url is:" + sjurl);
		Integer tempMtNumber = 0;
		if (mtresponse.getStatusCode() == 200 && sjresponse.getStatusCode() == 200) {
			String mtresponseStr = mtresponse.getResponseAsString();
			String sjresponseStr = sjresponse.getResponseAsString();
			JsonObject mtObject = JsonUtils.str2Obj(mtresponseStr).get("metadata").getAsJsonArray().get(0)
					.getAsJsonObject();
			JsonObject sjObject = JsonUtils.str2Obj(sjresponseStr).get("metadata").getAsJsonArray().get(0)
					.getAsJsonObject();
			JsonArray mtArray = new JsonArray();
			JsonArray sjArray = new JsonArray();
			if (mtObject.has("values")) {
				mtArray = mtObject.get("values").getAsJsonArray();
			}
			if (sjObject.has("values")) {
				sjArray = sjObject.get("values").getAsJsonArray();
			}
			for (int i = 0; i < mtArray.size(); i++) {
				JsonObject locationObj = mtArray.get(i).getAsJsonObject();
				String location = locationObj.get("key").getAsString();
				Integer mtValue = locationObj.get("value").getAsInt();
				tempMtNumber+=mtValue;
				String number = String.valueOf(mtValue);
				Pair<String, String> numPair = new Pair<>();
				numPair.setKey(number);
				numPair.setValue(0 + "");
				mtsj.put(location, numPair);
			}
			for (int i = 0; i < sjArray.size(); i++) {
				JsonObject locationObj = sjArray.get(i).getAsJsonObject();
				String location = locationObj.get("key").getAsString();
				int sjValue = locationObj.get("value").getAsInt();
				String number = String.valueOf(sjValue);
				if (mtsj.containsKey(location)) {
					Pair<String, String> numPair = mtsj.get(location);
					numPair.setValue(number);
					mtsj.put(location, numPair);
				} else {
					Pair<String, String> numPair = new Pair<>();
					numPair.setKey(0 + "");
					numPair.setValue(number);
					mtsj.put(location, numPair);
				}
			}
			Integer i = 0;
			for (Entry<String, Pair<String, String>> entry : mtsj.entrySet()) {
				String localtion = entry.getKey();
				String mtNumber ="";
				if (localtion.equals("其它")) {
					mtNumber = String.valueOf((MtTotal-tempMtNumber));
				}else {
					mtNumber = entry.getValue().getKey();
				}
				String sjNumber = entry.getValue().getValue();
				LocalModel local = new LocalModel(String.valueOf(++i), localtion, mtNumber, sjNumber);
				localList.add(local);
			}
		} else {
			logger.error("request error");
			logger.error("the error message:" + mtresponse.getErrorMsg());
			logger.error("the error message:" + sjresponse.getErrorMsg());
			return new ArrayList<>();
		}
		return localList;
	}

	private List<CommModel> getCommList(String taskId, String startTime, String endTime) {
		logger.info("初始化评论列表");
		Map<String, FourElem> commMap = new LinkedHashMap<>();
		String[] channels = config.getCommentChannels().split(",");
		for (String channel : channels) {
			String url = config.getRequestHost() + config.getCommentAddress() + channel + "/sentiment";
			HttpParam param1 = new HttpParam("taskId", taskId);
			HttpParam param2 = new HttpParam("startTime", startTime);
			HttpParam param3 = new HttpParam("endTime", endTime);
			HttpParam[] params = new HttpParam[] { param1, param2, param3 };
			Response numResponse = client.get(url, params);
			logger.info("the request url is:" + url);
			Integer pNum = 0;
			Integer nNum = 0;
			Integer negNum = 0;
			Integer total = 0;
			if (numResponse.getStatusCode() == 200) {
				String numResponseStr = numResponse.getResponseAsString();
				JsonObject metaArray = JsonUtils.str2Obj(numResponseStr).get("metadata").getAsJsonArray().get(0)
						.getAsJsonObject();
				if (metaArray.has("values")) {
					JsonArray numArray = metaArray.get("values").getAsJsonArray();
					for (int i = 0; i < numArray.size(); i++) {
						JsonObject mumObj = numArray.get(i).getAsJsonObject();
						String sentiment = mumObj.get("key").getAsString();
						Integer sentNum = mumObj.get("value").getAsInt();
						if (sentiment.equals("正面")) {
							pNum = sentNum;
						} else if (sentiment.equals("中立")) {
							nNum = sentNum;
						} else {
							negNum = sentNum;
						}
					}
				}
				total = pNum + nNum + negNum;
				commMap.put(total + "",
						new FourElem(pNum + "", nNum + "", negNum + "", CustomProperty.zhChMap.get(channel)));
			} else {
				logger.error("request error");
				logger.error("the error message:" + numResponse.getErrorMsg());
				return new ArrayList<>();
			}
		}
		Map<String, FourElem> sortMap = sortMap(commMap);
		for (Entry<String, FourElem> entry : sortMap.entrySet()) {
			CommModel comm = new CommModel(entry.getValue().getFourth(), entry.getValue().getFirst(),
					entry.getValue().getSecond(), entry.getValue().getThird(), entry.getKey());
			commList.add(comm);
		}
		return commList;
	}

	private String numberFormat(Integer number, Integer total) {
		Float num = (float) number / total;
		DecimalFormat df = new DecimalFormat("0.00");// 保留位数
		String numberFormat = df.format(num * 100);
		return numberFormat;
	}

	private String numberFormat2(Integer number, Integer total) {
		Float num = (float) number / total;
		DecimalFormat df = new DecimalFormat("0.00");// 保留位数
		String numberFormat = df.format(num);
		return numberFormat;
	}

	private Map<String, FourElem> sortMap(Map<String, FourElem> k_v) {
		List<Map.Entry<String, FourElem>> list = new ArrayList<Map.Entry<String, FourElem>>(k_v.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, FourElem>>() {

			@Override
			public int compare(Map.Entry<String, FourElem> o1, Map.Entry<String, FourElem> o2) {
				// 降序排序
				return Integer.parseInt(o2.getKey()) - Integer.parseInt(o1.getKey());
			}
		});
		Map<String, FourElem> result = new LinkedHashMap<>();
		for (Map.Entry<String, FourElem> entry : list) {
			result.put(entry.getKey(), entry.getValue());

		}

		return result;
	}

	private List<ZdhtModel> getZdhtList(String taskId, String startTime, String endTime) {
		logger.info("初始化重点话题列表");
		List<ZdhtModel> zdhtList = new LinkedList<>();
		String url = config.getRequestHost() + config.getZdwzAddress();
		HttpParam param1 = new HttpParam("taskId", taskId);
		HttpParam param2 = new HttpParam("startTime", startTime);
		HttpParam param3 = new HttpParam("endTime", endTime);
		HttpParam param4 = new HttpParam("sort", "ncmms");
		HttpParam param5 = new HttpParam("distinct", "true");
		HttpParam[] params = new HttpParam[] { param1, param2, param3, param4, param5 };
		Response response = client.get(url, params);
		logger.info("the request url is:" + url);
		if (response.getStatusCode() == 200) {
			JsonArray articles = JsonUtils.str2Obj(response.getResponseAsString()).get("articles").getAsJsonArray();
			if (articles.size() == 0) {
				return new ArrayList<>();
			}
			for (JsonElement jsonElement : articles) {
				JsonObject articleObj = jsonElement.getAsJsonObject();
				String title = articleObj.get("title").getAsString();
				String siteName = articleObj.get("siteName").getAsString();
				String ncmms = articleObj.get("commentNumber").getAsString();
				String pt = articleObj.get("publishTime").getAsString();
				ZdhtModel zdht = new ZdhtModel();
				zdht.setNcmms(ncmms);
				zdht.setPt(pt);
				zdht.setSiteName(siteName);
				zdht.setTitle(title);
				zdhtList.add(zdht);
				if (zdhtList.size() == 10) {
					break;
				}
			}
			maxComm = getMaxComm(articles.get(0).getAsJsonObject().get("id").getAsString(),
					articles.get(0).getAsJsonObject().get("title").getAsString());
		} else {
			logger.error("request error");
			logger.error("the error message:" + response.getErrorMsg());
			return new ArrayList<>();
		}
		return zdhtList;
	}

	private List<HotReport> gethotList(String taskId, String startTime, String endTime) {
		logger.info("初始化热点报道列表");
		List<HotReport> hotList = new LinkedList<>();
		String url = config.getRequestHost() + config.getBdrdAddress();
		HttpParam param1 = new HttpParam("taskId", taskId);
		HttpParam param2 = new HttpParam("startTime", startTime);
		HttpParam param3 = new HttpParam("endTime", endTime);
		HttpParam param4 = new HttpParam("sort", "hv");
		HttpParam[] params = new HttpParam[] { param1, param2, param3, param4 };
		Response response = client.get(url, params);
		logger.info("the request url is:" + url);
		if (response.getStatusCode() == 200) {
			JsonArray events = JsonUtils.str2Obj(response.getResponseAsString()).get("events").getAsJsonArray();
			if (events.size() == 0) {
				return new ArrayList<>();
			}
			for (JsonElement jsonElement : events) {
				JsonObject articleObj = jsonElement.getAsJsonObject();
				String title = articleObj.get("title").getAsString();
				String docNumber = articleObj.get("docNumber").getAsString();
				HotReport hotPeport = new HotReport(title, docNumber);
				hotList.add(hotPeport);
				if (hotList.size() == 10) {
					break;
				}
			}
		} else {
			logger.error("request error");
			logger.error("the error message:" + response.getErrorMsg());
			return new ArrayList<>();
		}
		return hotList;
	}


}
