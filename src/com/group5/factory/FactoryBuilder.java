package com.group5.factory;

import com.group5.client.communication.WebService;
 
/**
 * get the factory based on the factory name (WebService.METOHDNAME_XXXXX)
 * */
public class FactoryBuilder {
	
	public static Factory getFactory(String factoryname){
		Factory factory = null;
		if(factoryname.equalsIgnoreCase(WebService.METHODNAME_GETSTORES))
			factory = new StoreFactory();
		if(factoryname.equalsIgnoreCase(WebService.METHODNAME_GETPRICES))
			factory = new PriceFactory();
		if(factoryname.equalsIgnoreCase(WebService.METHODNAME_GETPRICESBYBARCODE))
			factory = new PriceFactory();
		if(factoryname.equalsIgnoreCase(WebService.METHODNAME_GETPRODUCT))
			factory = new ProductFactory();
		return factory;
	}
}
