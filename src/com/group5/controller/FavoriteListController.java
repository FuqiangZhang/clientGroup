package com.group5.controller;

import java.util.ArrayList;




import com.group5.bean.*;
import com.group5.factory.PriceFactory;
import com.group5.property.CacheDataController;

public class FavoriteListController {

	private CacheDataController cdc;
	private String fileName = "getFavoriteList";
	private SalesBeanList sbl;
	
	
	public FavoriteListController(){
		init();
	}
	public void add(Product product) {
		sbl.add(product);
		String jsonData = cdc.getJsonDataBySalesBeanList(sbl);
		cdc.writeFile(jsonData);
	}

	public void remove(Product product) {
		for(int i = 0 ;i <sbl.size();i++){
			Product p = (Product) sbl.get(i);
			if(p.getUuid().equals(product.getUuid())){
				sbl.remove(p);
				break;
			}
		}
		String jsonData = cdc.getJsonDataBySalesBeanList(sbl);
		cdc.writeFile(jsonData);
	}
	
	public void remove(int i){
		sbl = getList();
		sbl.remove(i);
		String jsonData = cdc.getJsonDataBySalesBeanList(sbl);
		cdc.writeFile(jsonData);
	}
	
	public SalesBeanList getList() {
		if (cdc.iFExist()) {
			sbl = cdc.getSalesBeanList(null);
		}else{
			sbl = new SalesBeanList();
			String jsonData = cdc.getJsonDataBySalesBeanList(sbl);
			cdc.writeFile(jsonData);
		}
		return sbl;
	}

	public void init() {
		cdc = new CacheDataController();
		cdc.setPathName(fileName);
		PriceFactory factory = new PriceFactory();
		cdc.setFactory(factory);
		getList();
	}
}
