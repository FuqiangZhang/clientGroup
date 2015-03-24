package com.group5.bean;

import java.io.Serializable;

public class Store implements SalesBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1521415937802340316L;
	private String uuid;
	private String name;
	private String location;
	private double lat;
	private double lng;
	private String decription;
	private boolean selected;
	
	public Store(){
		
	}
//	public Store(SoapObject storeSO){
//		
//		this.setUuid(storeSO.getPrimitivePropertyAsString("uuid"));
//		this.setName(storeSO.getPrimitivePropertyAsString("name"));
//		this.setLocation(storeSO.getPrimitivePropertyAsString("location"));
//		this.setLng(Double.valueOf(storeSO.getPrimitivePropertyAsString("lng")));
//		this.setLat(Double.valueOf(storeSO.getPrimitivePropertyAsString("lat")));
//	}
	
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
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getDecription() {
		return decription;
	}
	public void setDecription(String decription) {
		this.decription = decription;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
