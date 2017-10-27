package com.jwetherell.augmented_reality.data;

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
 * 暂时弃用掉
 */
public class XiaoyuanDataSource extends NetworkDataSource {
	private static final String BASE_URL = "http://127.0.0.1/get_data.json";

	private static Bitmap icon = null;
	public XiaoyuanDataSource(Resources res) {
		if (res == null)
			throw new NullPointerException();

		createIcon(res);
	}

	private void createIcon(Resources res) {
		if (res == null)
			throw new NullPointerException();

		icon = BitmapFactory.decodeResource(res, R.drawable.xiaoyuan);
		
	}

	@Override
	public String createRequestURL(double lat, double lon, double alt,
			float radius, String locale) {
		return BASE_URL + "?lat=" + lat + "&lng=" + lon + "&radius=" + radius
				+ "&maxRows=40" + "&lang=" + locale;
	}

	@Override
	public List<Marker> parse(JSONObject root) {
		if (root == null)
			return null;
		JSONObject jo = null;
		JSONArray dataArray = null;
		List<Marker> markers = new ArrayList<Marker>();
		try {
			if (root.has("geonames"))
				dataArray = root.getJSONArray("geonames");
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
			return null;

		Marker ma = null;
		if (jo.has("title") && jo.has("lat") && jo.has("lng")
				&& jo.has("elevation")) {
			try {
				ma = new IconMarker(jo.getString("title"), jo.getDouble("lat"),
						jo.getDouble("lng"), jo.getDouble("elevation"),
						Color.WHITE, icon);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return ma;
	}

}
