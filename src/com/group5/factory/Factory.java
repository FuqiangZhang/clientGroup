package com.group5.factory;

import org.json.JSONObject;
import com.group5.bean.SalesBean;
import com.group5.bean.SalesBeanList;
import com.group5.bean.Store;
import com.group5.client.communication.WebService;

public interface Factory {
	public SalesBean createBean(JSONObject jo);
	public SalesBeanList createBeanList(JSONObject jo);
	public String getJsonDataBySalesBeanList(SalesBeanList sbl);
}
