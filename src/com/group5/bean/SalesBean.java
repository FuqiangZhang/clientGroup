package com.group5.bean;

import java.io.Serializable;

public interface SalesBean extends Serializable{
	public String getName();
	public String getUuid();
	public boolean isSelected();
	public void setSelected(boolean isChecked);
}
