package com.group5.client.communication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.group5.bean.SalesBeanList;

public class WebService {

	public static final String METHODNAME_GETPRICES = "getPrices";
	public static final String METHODNAME_GETSTORES = "getStores";
	public static final String METHODNAME_GETCLASSFY = "getClassfy";
	public static final String METHODNAME_GETPRODUCT = "getProduct";
	public static final String METHODNAME_GETPRICESBYBARCODE = "getPricesbyBarcode";
	public static final String URL = "http://zhangke3012.sinaapp.com/";
	private static final ExecutorService executorService = Executors
			.newFixedThreadPool(10);

	/**
	 * Get Remote SalesBean by call the WebServiceRun running in another Thread
	 * */

	public static void getRemotePrices(String productname, String parentID,
			String storeID, final WebServiceCallBack webServiceCallBack) {

		String querry = "productname=" + productname;
		querry = querry + "&parentID=" + parentID;
		querry = querry + "&storeID=" + storeID;

		executorService.submit(new WebServiceRun(METHODNAME_GETPRICES,
				webServiceCallBack, querry));
	}

	public static void getRemotePricesbyBarcode(String barcode,
			String parentID, String storeID,
			final WebServiceCallBack webServiceCallBack) {

		String querry = "barcode=" + barcode;
		querry = querry + "&parentID=" + parentID;
		querry = querry + "&storeID=" + storeID;
		
		executorService.submit(new WebServiceRun(METHODNAME_GETPRICESBYBARCODE,
				webServiceCallBack, querry));
	}

	public static void getRemoteStores(
			final WebServiceCallBack webServiceCallBack) {
		executorService.submit(new WebServiceRun(METHODNAME_GETSTORES,
				webServiceCallBack, null));
	}

	public static void getRemoteClassfy(
			final WebServiceCallBack webServiceCallBack) {
		executorService.submit(new WebServiceRun(METHODNAME_GETCLASSFY,
				webServiceCallBack, null));
	}
	
	
	public static void getRemoteProduct(final WebServiceCallBack webServiceCallBack) {
		executorService.submit(new WebServiceRun(METHODNAME_GETPRODUCT,
				webServiceCallBack, null));
	
	}
	
	public interface WebServiceCallBack {
		public void callBack(SalesBeanList result);
	}
}
/**
 * {"getPrices":
 * [{"product":{"description":"fuirNode","name":"orange","barcode":
 * 0,"uuid":"792617824d8d11e4b1b6b870f44afbf0"} ,"price":3.5,
 * "store":{"selected"
 * :false,"location":"Whole Foods Market, East Lamar Boulevard, Arlington, TX"
 * ,"name"
 * :"wholeFood","lng":-97.135978,"uuid":"eea9eb783fc311e4bb22b870f44afbf0"
 * ,"lat":32.766878}, "uuid":"a75414be4d9b11e4b1b6b870f44afbf0"},
 */
