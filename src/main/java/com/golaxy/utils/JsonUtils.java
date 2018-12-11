package com.golaxy.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("unchecked")
public class JsonUtils {
	
	private static Gson gson = new Gson();
	public static List<String> JsonArray2list(JsonArray jsonArray) {
		List<String> k = gson.fromJson(jsonArray.toString(), new TypeToken<List<String>>() {
		}.getType());
		return k;
	}
	
	public static JsonObject str2Obj(String jsonString){
		JsonObject fromJson = gson.fromJson(jsonString, JsonObject.class);
		return fromJson;
	}
	
	public static String obj2Str(JsonElement jsonElement) {
		String str = gson.toJson(jsonElement);
		return str;
	}
	
    public static String javabeanToJson(Object object) {
        String json = gson.toJson(object);
        return json;
    }
    
    public static <T> Collection<T> jsonArray2Collect(JsonArray jsonArray,Collection<T> collection){
		Collection<T> armcoll = gson.fromJson(jsonArray, collection.getClass());
    	return armcoll;
    	
    }
    
    public static <K,V> Map<K, V> jsonObject2Map(JsonObject jsonObject,Map<K, V> map){
		Map<K, V> resultMap = gson.fromJson(jsonObject, map.getClass());
    	return resultMap;
    }
    
    public static <K,V> Map<K, V> jsonStr2Map(String str,Map<K, V> map){
    	
		Map<K, V> resultMap = gson.fromJson(str, map.getClass());
    	return resultMap;
    }
    
    public static Object jsonStr2JavaBean(String str,Object object) {
    	 Object resultObj = gson.fromJson(str, object.getClass());
    	 return resultObj;
    }
    
    public static Object jsonEle2Bean(JsonElement jsonElement,Object object) {
   	 Object resultObj = gson.fromJson(jsonElement, object.getClass());
   	 return resultObj;
   }
    
}
