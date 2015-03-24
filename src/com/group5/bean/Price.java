package com.group5.bean;

import java.io.Serializable;

import com.group5.client.communication.WebService;

public class Price implements SalesBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7599451743047095276L;
	private Product product;
	private Store 	store;
	private String priceID;
	private float price;
	private boolean isSelected;
	public String getUuid(){
		return "";
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
	public String getPriceID() {
		return priceID;
	}
	public void setPriceID(String priceID) {
		this.priceID = priceID;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@Override
	public String toString(){
		return this.product.toString()+"#"+this.store.toString()+"#"+this.priceID+"#"+this.price;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		
		return this.isSelected;
	}
	@Override
	public void setSelected(boolean isChecked) {
		// TODO Auto-generated method stub
		this.isSelected = isChecked;
		
	}
}
