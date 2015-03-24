package com.group5.property;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.group5.bean.SalesBeanList;
import com.group5.bean.Store;
import com.group5.factory.Factory;

import android.util.Log;

public class CacheDataController {

	private Factory factory; // the creator factory for the salesbeans (store, product, price)
	private String pathName; // the pathname of the file you want to read and write (such as )
							 // if you want to write the selected stores you can use "getStores"
							 // all the pathName is predefined in the class WebService, we use the METHODNAME_XXX
							
	private File file;		 //the file object

	public void setFactory(Factory factory) {
		this.factory = factory;
	}
	
	public void setPathName(String pathName) {
		this.pathName = pathName;
		this.file = FileController.getFile(pathName);
	}
	// get the salesbeanlist from the json data string
	public SalesBeanList getSalesBeanList(String jsonData) {
		if (jsonData == null) {
			jsonData = FileController.readFile(pathName);
		}
		SalesBeanList al = (SalesBeanList) new SalesBeanList();
		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			al = factory.createBeanList(jsonObject);
			System.out.println(al.size());
		} catch (JSONException e) {
			return al;
		}
		return al;
	}



	// judge whether the file is existing
	public boolean iFExist() {
		try {
			FileReader fr = new FileReader(file);
			if (fr.read() == -1) {
				return false;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	// write
	/**
	 * write2file by the String of json data
	 * */
	public void writeFile(String jsonData) {
		// TODO Auto-generated method stub
		try {
			BufferedReader reader = new BufferedReader(new StringReader(
					jsonData));
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			int len = 0;
			char[] buffer = new char[1024];
			while ((len = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, len);
			}
			writer.flush();
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * get the json string based on the salesbeanlist
	 * */

	public String getJsonDataBySalesBeanList(SalesBeanList al) {
		String json = "";
		if(al.size()==0){
			json = "";
		}else{
			json = factory.getJsonDataBySalesBeanList(al);
		}
		return json;
	}
}
