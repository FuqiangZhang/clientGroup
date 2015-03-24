package com.group5.client.communication;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.util.ByteArrayBuffer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.group5.bean.SalesBeanList;
import com.group5.client.communication.WebService.WebServiceCallBack;
import com.group5.factory.FactoryBuilder;
import com.group5.property.CacheDataController;

class WebServiceRun implements Runnable {
	String METHODNAME;
	private String url;
	WebServiceCallBack webServiceCallBack;
	final Handler mHandler;

	public WebServiceRun(String METHODNAME,
			final WebServiceCallBack webServiceCallBack, String argList) {
		this.url = METHODNAME + "?" + argList;
		Log.e("url",url);
		this.METHODNAME = METHODNAME;
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				webServiceCallBack.callBack((SalesBeanList) msg.getData()
						.getSerializable("resultList"));
			}
		};
	}
	/**
	 * get the result form the remote server  in a String format
	 * */
	public String getHtmlFromURLALL(String url) {
		String URL_DATA;
		try {
			URL uri = new URL(url);
			URLConnection ucon = uri.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(100);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			URL_DATA = new String(baf.toByteArray(), "utf-8");
		} catch (Exception e) {
			URL_DATA = e.getMessage();
		}
		return URL_DATA;
	}
	
	/**
	 * transform the String result and send it the handler 
	 * */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		String jsonData = getHtmlFromURLALL(WebService.URL + url);
		Log.e("jsondata",jsonData);
		CacheDataController cdc = new CacheDataController();
		cdc.setPathName(METHODNAME);
		cdc.setFactory(FactoryBuilder.getFactory(METHODNAME));
		cdc.writeFile(jsonData);
		SalesBeanList al = cdc.getSalesBeanList(jsonData);
		Message msg = new Message();
		Bundle bd = new Bundle();
		bd.putSerializable("resultList", (SalesBeanList) al);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}
}
