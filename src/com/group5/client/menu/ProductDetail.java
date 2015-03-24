package com.group5.client.menu;

import java.io.Serializable;
import java.util.ArrayList;

import location.GpsLocation;
import location.LocateTranslate;

import com.g5.saleshare.R;
import com.g5.saleshare.R.id;
import com.g5.saleshare.R.layout;
import com.g5.saleshare.R.menu;
import com.group5.bean.Price;
import com.group5.controller.ListController;
import com.group5.factory.Factory;
import com.group5.factory.PriceFactory;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetail extends Activity {

	private ImageView iv_Product;
	private TextView tv_ProductName;
	private TextView tv_StoreName;
	private TextView tv_Price;
	private TextView tv_ProductDetail;
	private TextView tv_StoreDetail;
	private ArrayList priceList;
	private WebView wv_Map;
	private ProgressDialog pd;
	private Handler handler;
	private Price selected_Price;
	private double mLat=32.730468, mLng=-97.114774;
	private String url = "http://zhangke3012.sinaapp.com/map.html?operation=0&gpsinfo=";
	private String urlcalcRoute = "http://zhangke3012.sinaapp.com/map.html?operation=1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		initUI();
		//getGPS();
		handler = new Handler();
		getUrl();
		initMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.product_detail, menu);
		return true;
	}

	// init map
	private void initMap() {
		wv_Map.getSettings().setJavaScriptEnabled(true);
		// when the user click the popupwindow in the google map, the call the
		// function in android to reset the activity to show the
		// selected detail
		wv_Map.addJavascriptInterface(new Object() {
			public void clickOnAndroid(final int i) {
				handler.post(new Runnable() {
					public void run() {
						Bundle bd = new Bundle();
						bd.putSerializable("all", priceList);
						bd.putSerializable("selected_price",
								(Serializable) priceList.get(i));
						// start the activity
						Intent intent = new Intent();
						intent.putExtras(bd);
						intent.setClass(ProductDetail.this, ProductDetail.class);
						ProductDetail.this.startActivity(intent);
						ProductDetail.this.finish();
					}
				});
			}
		}, "demo");
		// reload the web view
		wv_Map.loadUrl(url);
	}

	// get the url for the google map
	private void getUrl() {
		for (int i = 0; i < priceList.size(); i++) {
			Price price = (Price) priceList.get(i);
			url = url + "storename=" + price.getStore().getName() + ",lat="
					+ price.getStore().getLat() + ",lng="
					+ price.getStore().getLng() + ",price=" + price.getPrice()
					+ ",name=" + price.getProduct().getName() + ";";
		}
	}

	private ArrayList priceListAll = new ArrayList();
	private ListController shoppingListController = new ListController();
	Factory priceFactory = new PriceFactory();
	
	// init ui
	private void initUI() {
		
		shoppingListController.setPathName("getShoppingList");
		shoppingListController.setFactory(priceFactory);
		
		// set the information of the selected product
		Bundle bd = getIntent().getExtras();
		selected_Price = (Price) bd.getSerializable("selected_price");
		priceListAll = (ArrayList) bd.getSerializable("all");
		priceList = new ArrayList();
		for (int i = 0; i < priceListAll.size(); i++) {
			if (((Price) priceListAll.get(i)).getProduct().getName()
					.equalsIgnoreCase(selected_Price.getProduct().getName())) {
				priceList.add((Price) priceListAll.get(i));
			}
		}
		iv_Product = (ImageView) this.findViewById(R.id.iv_Product);
		tv_ProductName = (TextView) this.findViewById(R.id.tv_ProductName);
		tv_StoreName = (TextView) this.findViewById(R.id.tv_StoreName);
		tv_Price = (TextView) this.findViewById(R.id.tv_Price);
		tv_ProductDetail = (TextView) this.findViewById(R.id.tv_ProductDetail);
		tv_StoreDetail = (TextView) this.findViewById(R.id.tv_StoreDetail);
		tv_ProductName.setText(selected_Price.getProduct().getName());
		tv_StoreName.setText(selected_Price.getStore().getName());
		tv_Price.setText("$" + String.valueOf(selected_Price.getPrice()));
		tv_ProductDetail.setText(selected_Price.getProduct().getDescription());
		tv_StoreDetail.setText(selected_Price.getStore().getLocation());
		// get the address based on the lng and lat then set it to the text view
		// to show
//		final Handler storeAddressHandler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				Bundle bd = msg.getData();
//				String address = bd.getString("address");
//				tv_StoreDetail.setText(address);
//			}
//		};
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				double slat = selected_Price.getStore().getLat();
//				double slng = selected_Price.getStore().getLng();
//				String address = LocateTranslate.getAddress(slat, slng);
//				Bundle bd = new Bundle();
//				bd.putString("address", selected_Price.getStore().getLocation());
//				Message msg = new Message();
//				msg.setData(bd);
//				storeAddressHandler.sendMessage(msg);
//			}
//		}).start();
		// init the webview to show the map
		wv_Map = (WebView) this.findViewById(R.id.webView1);
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("Loading");
		// set the button to show the path to the store
		Button bt_Go = (Button) this.findViewById(R.id.bt_go);
		bt_Go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calcRoute();
			}

		});
		// set the button to show all the popupwindows
		Button bt_All = (Button) this.findViewById(R.id.All);
		bt_All.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wv_Map.loadUrl(url);
			}

		});

		iv_Product.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shoppingListController.getList();
				shoppingListController.add(selected_Price);
				Toast.makeText(ProductDetail.this,
						"Added to the shopping list!", Toast.LENGTH_SHORT)
						.show();
			}

		});
		iv_Product.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				intent.setClass(ProductDetail.this,
						ShoppingListActivity.class);
				ProductDetail.this.startActivity(intent);
				return false;
			}
			
		});

	}

	// calc the path
	private void calcRoute() {
		double dLat = selected_Price.getStore().getLat();
		double dLng = selected_Price.getStore().getLng();
		urlcalcRoute = urlcalcRoute + "&mlat=" + mLat + "&mlng=" + mLng
				+ "&dlat=" + dLat + "&dlng=" + dLng;
		wv_Map.loadUrl(urlcalcRoute);
	}

	private Handler gpsHandler;

	// get the gps information
	public void getGPS() {
		gpsHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle bd = msg.getData();
				mLat = bd.getDouble("mlat");
				mLng = bd.getDouble("mlng");
			}
		};

		GpsLocation gps = new GpsLocation(ProductDetail.this, gpsHandler);
		new Thread(gps).start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
