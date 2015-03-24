package com.group5.factory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.util.Log;

import com.group5.bean.*;
import com.group5.client.communication.WebService;

public class PriceFactory implements Factory{
/**
 * 
 * "product":{"description":"fuirNode","name":"orange","barcode":0,"uuid":"792617824d8d11e4b1b6b870f44afbf0"}
 * ,"price":3.5,
 * "store":{"selected":false,"location":"Whole Foods Market, East Lamar Boulevard, Arlington, TX","name":"wholeFood","lng":-97.135978,"uuid":"eea9eb783fc311e4bb22b870f44afbf0","lat":32.766878},
 * "uuid":"a75414be4d9b11e4b1b6b870f44afbf0"
 * 
 * */
	@Override
	public Price createBean(JSONObject jo) {
		// TODO Auto-generated method stub
		Price price = new Price();
		try {
			price.setPrice(Float.valueOf(jo.getString("price")));
			price.setPriceID(jo.getString("uuid"));
			JSONObject jostore = jo.getJSONObject("store");
			price.setStore(new StoreFactory().createBean(jostore));
			JSONObject joproduct = jo.getJSONObject("product");
			price.setProduct(new ProductFactory().createBean(joproduct));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return price;
	}

	@Override
	public Prices createBeanList(JSONObject jos) {
		Prices prices = new Prices();
		try {
			JSONArray jsonArray = jos.getJSONArray(WebService.METHODNAME_GETPRICES);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jo = jsonArray.getJSONObject(i);
				Price price = createBean(jo);
				Log.e("",jo.toString());
				prices.add(price);
				Log.e("pricesss",price.getProduct().getName());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return prices;
	}

	@Override
	public String getJsonDataBySalesBeanList(SalesBeanList sbl) {
		// TODO Auto-generated method stub
		String json = null;
		JSONArray array = new JSONArray();
		if(sbl.size()==0){
			return "";
		}
		for (int i = 0 ; i<sbl.size();i++) {
			Price price = (Price) sbl.get(i);
			JSONObject jso = new JSONObject();
			try {
				jso.put("price", price.getPrice());
				jso.put("uuid", price.getPriceID());
				JSONObject jsoStore = new JSONObject();
				Store sb = price.getStore();
				jsoStore.put("name", sb.getName());
				jsoStore.put("uuid", sb.getUuid());
				jsoStore.put("lat", sb.getLat());
				jsoStore.put("lng", sb.getLng());
				jsoStore.put("decription", sb.getDecription());
				jsoStore.put("location", sb.getLocation());
				jsoStore.put("selected", sb.isSelected());
				jso.put("store", jsoStore);
				JSONObject jsoProduct = new JSONObject();
				Product product = price.getProduct();
				jsoProduct.put("name", product.getName());
				jsoProduct.put("uuid", product.getUuid());
				jsoProduct.put("barcode", product.getBarcode());
				jsoProduct.put("description", product.getDescription());
				jsoProduct.put("classfy", product.getParentid());
				jso.put("product", jsoProduct);
				array.put(jso);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			json = new JSONStringer().object().key("getPrices").value(array)
					.endObject().toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch blockW
			e.printStackTrace();
		}
		return json;
	}

}
