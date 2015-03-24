package com.group5.bean;

import java.io.Serializable;

import android.util.Log;

public class Product implements SalesBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -848093953591578279L;
	private String uuid;
	private String name;
	private String parentid;
	private int child_Count;
	private String description;
	private boolean selected;
	
	private long barcode;
	public Product(){
	}
	
	@Override
	public String toString(){
		return this.uuid+"#"+this.name+"#"+this.parentid+"#"
				+this.child_Count+"#"+this.description+"#"+this.barcode;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public int getChild_Count() {
		return child_Count;
	}
	public void setChild_Count(int child_Count) {
		this.child_Count = child_Count;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getBarcode() {
		return barcode;
	}
	public void setBarcode(long barcode) {
		this.barcode = barcode;
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		
		return this.selected;
	}

	@Override
	public void setSelected(boolean isChecked) {
		// TODO Auto-generated method stub
		this.selected = isChecked;
	}
	
	
}
