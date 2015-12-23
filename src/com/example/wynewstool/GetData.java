package com.example.wynewstool;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.wynewsobject.NewsTab_Object;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetData {
	public static List<NewsTab_Object> getNewsList(JSONObject response) {
		List<NewsTab_Object> newsData = new ArrayList<NewsTab_Object>();
		try {
			JSONArray array = response.getJSONArray("tList");
			Gson gson = new Gson();
			TypeToken<List<NewsTab_Object>> tt = new TypeToken<List<NewsTab_Object>>() {
			};
			newsData = gson.fromJson(array.toString(), tt.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newsData;
	}
}
