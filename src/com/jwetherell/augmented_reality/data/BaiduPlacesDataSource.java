package com.jwetherell.augmented_reality.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.jwetherell.augmented_reality.R;
import com.jwetherell.augmented_reality.ui.IconMarker;
import com.jwetherell.augmented_reality.ui.Marker;

/**
 * 继承NetworkDataSource数据源，从百度 Places获取数据。
 */
public class BaiduPlacesDataSource extends NetworkDataSource {
//	//测试
//	private static final String URL = "http://maps.google.cn/maps/api/place/search/json?";
	
	

	private static final String URL = "http://api.map.baidu.com/place/v2/search?";
	
	private static final String TYPES = "银行";
	private static String ak = null;
	private static Bitmap icon = null;

	public BaiduPlacesDataSource(Resources res) {
		if (res == null)
			throw new NullPointerException();

		ak = res.getString(R.string.baidu_places_api_ak);

		createIcon(res);
	}

	protected void createIcon(Resources res) {
		if (res == null)
			throw new NullPointerException();

		icon = BitmapFactory.decodeResource(res, R.drawable.buzz);
	}
	//https://maps.googleapis.com/maps/api/place/search/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&sensor=true&key=AIzaSyBMGzm-1zXeLX-QNi9N7ozRJWZlREyYEe4
//http://api.map.baidu.com/place/v2/search?query=银行&location=39.915,116.404&radius=2000&output=json&ak=wawyuy9wQ2AywA6IIF7ojCCanctYzvv7
	@Override
	public String createRequestURL(double lat, double lon, double alt,
			float radius, String locale) {
		try {
//			return URL + "query="+TYPES+"&location=" + lat + "," + lon + "&radius="
//					+ (radius * 1000.0f) + "&output=json" + "&ak=" + ak;
			return URL + "query="+TYPES+"&location=" + lat + "," + lon + "&radius="+(radius * 1000.0f)
			 + "&output=json" + "&ak=" + ak;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<Marker> parse(String URL) {
		if (URL == null)
			throw new NullPointerException();

		InputStream stream = null;
		stream = getHttpGETInputStream(URL);
		if (stream == null)
			throw new NullPointerException();

		String string = null;
		string = getHttpInputString(stream);
		if (string == null)
			throw new NullPointerException();

		JSONObject json = null;
		try {
			json = new JSONObject(string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (json == null)
			throw new NullPointerException();

		return parse(json);
	}

	@Override
	public List<Marker> parse(JSONObject root) {
		if (root == null)
			throw new NullPointerException();

		JSONObject jo = null;
		JSONArray dataArray = null;
		List<Marker> markers = new ArrayList<Marker>();

		try {
			if (root.has("results"))
				dataArray = root.getJSONArray("results");
			if (dataArray == null)
				return markers;
			int top = Math.min(MAX, dataArray.length());
			for (int i = 0; i < top; i++) {
				jo = dataArray.getJSONObject(i);
				Marker ma = processJSONObject(jo);
				if (ma != null)
					markers.add(ma);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return markers;
	}

	private Marker processJSONObject(JSONObject jo) {
		if (jo == null)
			throw new NullPointerException();

		if (!jo.has("location"))
			throw new NullPointerException();

		Marker ma = null;
		
		try {
			
			Double lat = null, lon = null;

			if (!jo.isNull("location")) {
				JSONObject geo = jo.getJSONObject("location");
				//JSONObject coordinates = jo.getJSONObject("location");
				lat = Double.parseDouble(geo.getString("lat"));
				lon = Double.parseDouble(geo.getString("lng"));
			}
			if (lat != null) {
				String user = jo.getString("name");

//				ma = new IconMarker(user + ": " + jo.getString("name"), lat,
//						lon, 0, Color.RED, icon);
				ma = new IconMarker(user, lat,
						lon, 0, Color.RED, icon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ma;
	}
}