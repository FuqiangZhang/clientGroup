package com.group5.factory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.util.Log;

import com.group5.bean.*;

public class StoreFactory implements Factory {
	@Override
	public Store createBean(JSONObject jo) {
		Store store = new Store();
		try {
			store.setName(jo.getString("name"));
			store.setUuid(jo.getString("uuid"));
			store.setLat(jo.getDouble("lat"));
			store.setLng(jo.getDouble("lng"));
			store.setLocation(jo.getString("location"));
			store.setSelected(jo.getBoolean("selected"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return store;
	}

	@Override
	public Stores createBeanList(JSONObject jsonObject) {
		Stores stores = new Stores();
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("getStores");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jo = jsonArray.getJSONObject(i);
				Store store = createBean(jo);
				stores.add(store);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stores;
	}

	@Override
	public String getJsonDataBySalesBeanList(SalesBeanList sbl) {
		String json = null;
		Stores stores = (Stores) sbl;
		JSONArray array = new JSONArray();
		for (int i = 0; i < stores.size(); i++) {
			Store sb = (Store) stores.get(i);
			JSONObject jso = new JSONObject();
			try {
				jso.put("name", sb.getName());
				jso.put("uuid", sb.getUuid());
				jso.put("lat", sb.getLat());
				jso.put("lng", sb.getLng());
				jso.put("decription", sb.getDecription());
				jso.put("location", sb.getLocation());
				jso.put("selected", sb.isSelected());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			array.put(jso);
		}
		try {
			json = new JSONStringer().object().key("getStores").value(array)
					.endObject().toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
