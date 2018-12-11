package com.golaxy.utils;

import com.google.gson.JsonObject;

import ict.http.HttpClient;
import ict.http.Response;

public class EsUtil {
	public static HttpClient httpClient;

	static {

		httpClient = HttpClient.createSSLInstance(20, 200, 30000, 40000, "zktjrw", "zktjrw");

	}

	public static JsonObject getQueryResponse(String url) {
		Response response = httpClient.get(url);
		if (response.getStatusCode() == 200) {
			JsonObject hitsObj = JsonUtils.str2Obj(response.getResponseAsString()).get("hits").getAsJsonObject();
			Integer total = hitsObj.get("total").getAsInt();
			if (total != 0) {
				JsonObject result = hitsObj.get("hits").getAsJsonArray().get(0).getAsJsonObject();
				return result;
			}
		}
		return null;
	}

	public static Integer getEvidNumberResponse(String url, String body) {
		Response response = httpClient.post(url, body, "application/json", "UTF-8");
		if (response.getStatusCode() == 200) {
			JsonObject hitsObj = JsonUtils.str2Obj(response.getResponseAsString()).get("hits").getAsJsonObject();
			Integer total = hitsObj.get("total").getAsInt();
			if (total != 0) {
				return total;
			}
		}
		return 1;
	}

}
