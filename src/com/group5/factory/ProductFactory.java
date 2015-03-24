package com.group5.factory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.util.Log;

import com.group5.bean.Product;
import com.group5.bean.Products;
import com.group5.bean.SalesBeanList;
import com.group5.bean.Store;
import com.group5.bean.Stores;

public class ProductFactory implements Factory {
	/**
	 * 
	 * "description":"fuirNode","name":"orange","barcode":0,"uuid":
	 * "792617824d8d11e4b1b6b870f44afbf0"
	 * 
	 * */
	@Override
	public Product createBean(JSONObject jo) {
		// TODO Auto-generated method stub
		Product product = new Product();
		try {
			product.setUuid(jo.getString("uuid"));
			product.setDescription(jo.getString("description"));
			product.setName(jo.getString("name"));
			product.setBarcode(jo.getLong("barcode"));
			product.setSelected(jo.getBoolean("selected"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public SalesBeanList createBeanList(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		Products products = new Products();
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("getStores");
			Log.e("",jsonArray.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jo = jsonArray.getJSONObject(i);
				Product product = createBean(jo);
				products.add(product);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public String getJsonDataBySalesBeanList(SalesBeanList sbl) {
		// TODO Auto-generated method stub
		String json = null;
		Products products = (Products) sbl;
		JSONArray array = new JSONArray();
		for (int i = 0; i < products.size(); i++) {
			Product sb = (Product) products.get(i);
			JSONObject jso = new JSONObject();
			try {
				jso.put("name", sb.getName());
				jso.put("uuid", sb.getUuid());
				jso.put("description", sb.getDescription());
				jso.put("selected", sb.isSelected());
				jso.put("barcode", sb.getBarcode());
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
