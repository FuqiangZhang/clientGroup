package com.group5.controller;

import java.util.ArrayList;





import com.group5.bean.*;
import com.group5.factory.Factory;
import com.group5.factory.PriceFactory;
import com.group5.property.CacheDataController;

public class ListController {

	private CacheDataController cdc;
	private String fileName ;
	private SalesBeanList sbl;

	public ListController(){
	}
	public void add(Price price) {
		sbl.add(price);
		String jsonData = cdc.getJsonDataBySalesBeanList(sbl);
		cdc.writeFile(jsonData);
	}

	public void remove(Price price) {
		for(int i = 0 ;i <sbl.size();i++){
			Price p = (Price) sbl.get(i);
			if(p.getUuid().equals(price.getUuid())){
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

	public void setFactory(Factory factory){
		this.factory = factory;
	}
	
	public void setPathName(String pathName){
		this.fileName = pathName;
	}
	
	private Factory factory;
	
	public void init() {
		cdc = new CacheDataController();
		cdc.setPathName(fileName);
		cdc.setFactory(factory);
		getList();
	}
}
