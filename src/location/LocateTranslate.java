package location;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class LocateTranslate {
	public static double[] getLocationInfo(String address) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://maps.google."
				+ "com/maps/api/geocode/json?address=" + address
				+ "ka&sensor=false");
		StringBuilder sb = new StringBuilder();
		try {
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				sb.append((char) b);
			}
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONObject location = jsonObject.getJSONArray("results")
					.getJSONObject(0).getJSONObject("geometry")
					.getJSONObject("location");
			double longitude = location.getDouble("lng");
			double latitude = location.getDouble("lat");
			return new double[] { longitude, latitude };
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getAddress(double longitude, double latitude) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/"
				+ "geocode/json?latlng=" + latitude + "," + longitude
				+ "&sensor=false");
		StringBuilder sb = new StringBuilder();
		try {
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				sb.append((char) b);
			}
			JSONObject jsonObj = new JSONObject(sb.toString());
			// Log.e("add", sb.toString());
			return jsonObj.getJSONArray("results").getJSONObject(0)
					.getString("formatted_address");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
