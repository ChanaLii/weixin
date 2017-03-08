package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonTest {

	public static String getWeatherInfo(String cityName) {
		// https://free-api.heweather.com/v5/weather?city=cn101010100&key=53a2c2c080094433b736c860631522fa
		URL url;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			url = new URL("https://free-api.heweather.com/v5/weather?city=" + cityName
					+ "&key=53a2c2c080094433b736c860631522fa");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			InputStream is = conn.getInputStream();
			BufferedReader bis = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = bis.readLine()) != null) {
				sb.append(line);
			}
			bis.close();
			conn.disconnect();
			String jsonData = sb.toString();

			/**
			 * fastJson解析json数据
			 */
			JSONObject jsonObject = JSON.parseObject(jsonData);
			JSONArray heWeather5 = jsonObject.getJSONArray("HeWeather5");
			JSONObject jsonObject1 = heWeather5.getJSONObject(0);
			JSONObject basic = jsonObject1.getJSONObject("basic");
			// 城市
			String city = basic.getString("city");
			JSONObject update = basic.getJSONObject("update");
			// 更新时间
			String loc = update.getString("loc");
			JSONArray daily_forecast = jsonObject1.getJSONArray("daily_forecast");
			JSONObject jsonObject2 = daily_forecast.getJSONObject(0);
			JSONObject tmp = jsonObject2.getJSONObject("tmp");
			// 最高~最低温
			String max = tmp.getString("max");
			String min = tmp.getString("min");

			stringBuilder.append(city).append("\n");
			stringBuilder.append("更新时间：" + loc).append("\n");
			stringBuilder.append("气温介于：" + min + "~" + max + "°C").append("\n");
			System.out.println(stringBuilder.toString());
			return stringBuilder.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
}
