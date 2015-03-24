package com.group5.client.menu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.g5.saleshare.R;
import com.group5.client.communication.WebService;
import com.group5.client.communication.WebService.WebServiceCallBack;
import com.group5.factory.FactoryBuilder;
import com.group5.factory.StoreFactory;
import com.group5.property.CacheDataController;
import com.group5.property.FileController;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.group5.bean.*;

public class LoadActivity extends Activity {
	private static final int SHOW_TIME_MIN = 800;
	private SalesBeanList stores = null;
	private SalesBeanList products = null;
	private boolean isFirstRun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		FileController.setContext(this);

		stores = null;
////		classfy = new SalesBeanList();
		CacheDataController scdc = new CacheDataController();
		scdc.setPathName(WebService.METHODNAME_GETSTORES);
		scdc.setFactory(FactoryBuilder.getFactory(WebService.METHODNAME_GETSTORES));
		if (scdc.iFExist()) {
			stores = scdc.getSalesBeanList(null);
			isFirstRun = false;
		} else {
			isFirstRun = true;
			WebService.getRemoteStores(new WebServiceCallBack() {
				@Override
				public void callBack(SalesBeanList result) {
					stores = (SalesBeanList) result;
				}
			});
		}
//		
		CacheDataController pcdc = new CacheDataController();
		pcdc.setPathName(WebService.METHODNAME_GETPRODUCT);
		pcdc.setFactory(FactoryBuilder.getFactory(WebService.METHODNAME_GETPRODUCT));
		if (pcdc.iFExist()) {
			products = pcdc.getSalesBeanList(null);
			Log.e("exist",products.size()+"");
		//	isFirstRun = false;
		} else {
		//	isFirstRun = true;
			WebService.getRemoteProduct(new WebServiceCallBack() {
				@Override
				public void callBack(SalesBeanList result) {
					products = (SalesBeanList) result;
				}
			});
		}
		
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (stores != null && products !=null) {
						break;
					}
				}
				Bundle bd = new Bundle();
				bd.putSerializable("stores",(SalesBeanList) stores);
				bd.putSerializable("products", products);
				bd.putBoolean("isFirstRun", isFirstRun);
				Intent intent = new Intent();
				intent.putExtras(bd);
				intent.setClass(LoadActivity.this, SearchActivity.class);
				LoadActivity.this.startActivity(intent);
				LoadActivity.this.finish();
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.load, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
