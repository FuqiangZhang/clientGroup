package com.group5.client.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g5.saleshare.R;
import com.group5.bean.Price;
import com.group5.bean.SalesBean;
import com.group5.bean.SalesBeanList;
import com.group5.client.communication.WebService;
import com.group5.client.communication.WebService.WebServiceCallBack;
import com.group5.controller.ListController;
import com.group5.factory.Factory;
import com.group5.factory.PriceFactory;
import com.group5.widget.SlideCutListView.RemoveDirection;
import com.group5.widget.SlideCutListView.RemoveListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class SearchResultActivity extends Activity {
	private com.group5.widget.SlideCutListView lv_Products, lv_favorite;
	private View footView;
	private ProgressBar pb_ft_more;
	private SimpleAdapter mSimpleAdapter;
	private SimpleAdapter mFavoriteSimpleAdapter;
	private ArrayList<Price> priceList = new ArrayList<Price>();
	private List<HashMap<String, String>> productList_in_ListView;
	private SearchView searchView;
	private ArrayList<SalesBean> selected_Stores;
	private ProgressBar pb_search_result;
	private ListController shoppingListController;
	private ListController favoriteListController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		initUI();
		initListView();
		// search the product from the main activity
		Bundle bd = getIntent().getExtras();
		String query = bd.getString("query");
		selected_Stores = (ArrayList<SalesBean>) bd.get("stores");
		searchProduct(query);
		shoppingListController = new ListController();
		Factory priceFactory = new PriceFactory();
		shoppingListController.setPathName("getShoppingList");
		shoppingListController.setFactory(priceFactory);

		favoriteListController = new ListController();
		favoriteListController.setPathName("getFavoriteList");
		favoriteListController.setFactory(priceFactory);
		
		initFavoriteList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_result, menu);
		return true;
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

	private ArrayList<SalesBean> favoriteList;
	private ArrayList favoirvateList_in_ListView;

	private void initFavoriteList() {

		// init listview
		lv_favorite = (com.group5.widget.SlideCutListView) this
				.findViewById(R.id.lv_products_search_result);

		lv_favorite.setRemoveListener(new RemoveListener() {

			@Override
			public void removeItem(RemoveDirection direction, int position) {
				// TODO Auto-generated method stub
				Price price = (Price) priceList.get(position);
				favoriteListController.remove(price);
			}
		});

		// init List View
		mFavoriteSimpleAdapter = new SimpleAdapter(this,
				favoirvateList_in_ListView, R.layout.listitem, new String[] {
						"productName", "price", "store" }, new int[] {
						R.id.tv_productName, R.id.tv_price, R.id.tv_store });
		// lv_Products.addFooterView(footView);
		lv_favorite.setAdapter(mSimpleAdapter);

		favoriteList = new ArrayList<SalesBean>();

		favoirvateList_in_ListView = new ArrayList<HashMap<String, String>>();
		favoriteList = favoriteListController.getList();
		if (favoriteList == null || favoriteList.size() == 0)
			return;
		for (int i = 0; i < favoriteList.size(); i++) {
			HashMap<String, String> mPrice = new HashMap<String, String>();
			Price price = (Price) favoriteList.get(i);
			mPrice.put("productName", price.getProduct().getName());
			mPrice.put("price", String.valueOf("$" + price.getPrice()));
			mPrice.put("store", price.getStore().getName());
			favoirvateList_in_ListView.add(mPrice);
		}
	}

	private void initUI() {
		// init progressbar
		pb_search_result = (ProgressBar) this
				.findViewById(R.id.pb_search_result);
		// init searchView
		searchView = (SearchView) this
				.findViewById(R.id.sv_searchProduct_search_result);
		searchView.setIconifiedByDefault(true);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Search");

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				searchProduct(query);
				return false;
			}

		});
		
	}

	private void initListView() {

		// init listview
		lv_Products = (com.group5.widget.SlideCutListView) this
				.findViewById(R.id.lv_products_search_result);

		lv_Products.setRemoveListener(new RemoveListener() {

			@Override
			public void removeItem(RemoveDirection direction, int position) {
				// TODO Auto-generated method stub
				Price price = (Price) priceList.get(position);
				shoppingListController.add(price);
			}

		});
		footView = getLayoutInflater().inflate(R.layout.listfooter, null);
		pb_ft_more = (ProgressBar) this.findViewById(R.id.pb_ft_more);

		// init List View
		productList_in_ListView = new ArrayList<HashMap<String, String>>();
		mSimpleAdapter = new SimpleAdapter(this, productList_in_ListView,
				R.layout.listitem, new String[] { "productName", "price",
						"store" }, new int[] { R.id.tv_productName,
						R.id.tv_price, R.id.tv_store });
		// lv_Products.addFooterView(footView);
		lv_Products.setAdapter(mSimpleAdapter);
		// lv_Products.setLongClickable(true);
		// lv_Products.setOnItemLongClickListener(new OnItemLongClickListener(){
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		// final int selectedItem, long arg3) {
		//
		//
		// AlertDialog.Builder builder = new Builder(SearchResultActivity.this);
		// builder.setMessage("Add it the Shopping List?");
		// builder.setTitle("Confirm");
		// builder.setPositiveButton("OK", new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// Price price = (Price) priceList.get(selectedItem);
		// shoppingListController.add(price);
		// dialog.dismiss();
		// }
		// });
		// builder.setNegativeButton("Cancle", new OnClickListener(){
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		// dialog.dismiss();
		// }
		//
		// });
		// builder.create().show();
		// return false;
		// }
		//
		// });
		lv_Products.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int selected_item, long arg3) {
				productList_in_ListView.get(selected_item);
				Bundle bd = new Bundle();
				bd.putSerializable("all", priceList);
				bd.putSerializable("selected_price",
						priceList.get(selected_item));
				Intent intent = new Intent();
				intent.putExtras(bd);
				intent.setClass(SearchResultActivity.this, ProductDetail.class);
				SearchResultActivity.this.startActivity(intent);
			}
		});
	}

	public void searchProduct(String searchString) {
		pb_search_result.setVisibility(View.VISIBLE);
		if (searchString.equalsIgnoreCase("") || searchString == null) {
			Toast.makeText(SearchResultActivity.this,
					"Please input the product name!", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		String storeid = null;
		String classfyid = null;
		// if the user select one store the use selected_store
		StringBuilder sb = new StringBuilder();
		for (SalesBean store : selected_Stores) {
			sb.append(store.getUuid() + ",");
		}
		storeid = sb.toString();
		storeid = storeid.substring(0, storeid.length() - 1);
		WebService.getRemotePrices(searchString, classfyid, storeid,
				new WebServiceCallBack() {
					@Override
					public void callBack(SalesBeanList result) {
						if (result == null || result.size() == 0) {
							Toast.makeText(SearchResultActivity.this,
									"No such products", Toast.LENGTH_SHORT)
									.show();
							SearchResultActivity.this.finish();
						}
						productList_in_ListView.clear();
						priceList.clear();
						for (int i = 0; i < result.size(); i++) {
							priceList.add((Price) result.get(i));
							HashMap<String, String> mPrice = new HashMap<String, String>();
							Price price = (Price) result.get(i);
							mPrice.put("productName", price.getProduct()
									.getName());
							mPrice.put("price",
									String.valueOf("$" + price.getPrice()));
							mPrice.put("store", price.getStore().getName());
							productList_in_ListView.add(mPrice);
						}
						mSimpleAdapter.notifyDataSetChanged();
						pb_search_result.setVisibility(View.GONE);
					}
				});
	}
}
