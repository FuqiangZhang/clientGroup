package com.group5.client.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g5.saleshare.R;
import com.group5.bean.Price;
import com.group5.bean.Product;
import com.group5.bean.SalesBean;
import com.group5.bean.SalesBeanList;
import com.group5.client.communication.WebService;
import com.group5.client.communication.WebService.WebServiceCallBack;
import com.group5.client.menu.SelectDialog.SelectedDialogCallback;
import com.group5.controller.ListController;
import com.group5.factory.Factory;
import com.group5.factory.FactoryBuilder;
import com.group5.factory.PriceFactory;
import com.group5.factory.ProductFactory;
import com.group5.factory.StoreFactory;
import com.group5.widget.SlideCutListView.RemoveDirection;
import com.group5.widget.SlideCutListView.RemoveListener;
import com.zxing.activity.CaptureActivity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.appcompat.R.color;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class SearchActivity extends TabActivity {

	private List<HashMap<String, String>> productList_in_ListView;
	private SearchView searchView;
	private TextView tv_SelectStore;
	private ImageView iv_ShoppingList;
	private ImageView iv_Opencamera;
	private Button bt_select_favorite;
	private SelectDialog storeDialog;
	private SelectDialog favoriteDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initUI();
		initTab();
		storeDialog = getSelectDialog(WebService.METHODNAME_GETSTORES,
				new SelectedDialogCallback() {
					@Override
					public void callback() {
						searchProduct(searchView.getQuery().toString());
					}
				});
		favoriteDialog = getSelectDialog(WebService.METHODNAME_GETPRODUCT,
				new SelectedDialogCallback() {
					@Override
					public void callback() {
						favorite_in_Listview.clear();
						for (int i = 0; i < favoriteDialog.getSelectedSalesBeanList().size(); i++) {
							Product pricez = (Product) favoriteDialog.getSelectedSalesBeanList().get(i);
							if (pricez.isSelected()) {
								HashMap<String, String> mPrice = new HashMap<String, String>();
								String inShoppinglist = "";
								mPrice.put("productName", pricez.getName());
								mPrice.put("isSelected", inShoppinglist);
								favorite_in_Listview.add(mPrice);
							}
						}
						
						favoriteSimpleAdapter.notifyDataSetChanged();
						favoriteSimpleAdapter.notifyDataSetChanged();
					}

				});
		shoppingListController = new ListController();
		favoriteListController = new ListController();
		Bundle bd = getIntent().getExtras();
		boolean isFirstRun = bd.getBoolean("isFirstRun");
		if (isFirstRun) {
			storeDialog.showDialog();
		}
		initListView();
		initFavoriteListView();
	}
	private ArrayList favoriteList = new ArrayList();
	private void initUI() {
		bt_select_favorite = (Button) this
				.findViewById(R.id.bt_select_favorite);
		bt_select_favorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}

		});
		// init shoppingList button
		iv_ShoppingList = (ImageView) this.findViewById(R.id.iv_shoppingList);
		iv_ShoppingList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					iv_ShoppingList
							.setBackgroundColor(color.abc_search_url_text_pressed);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					iv_ShoppingList.setBackgroundColor(0);
					// go to shopping list

					Intent intent = new Intent();
					intent.setClass(SearchActivity.this,
							ShoppingListActivity.class);
					SearchActivity.this.startActivity(intent);
					return true;
				}
				return false;
			}
		});
		// init searchview
		searchView = (SearchView) this.findViewById(R.id.sv_searchProduct);
		searchView.setIconifiedByDefault(true);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Search");
		// searchView.getQuery();
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

		// init select store button
		tv_SelectStore = (TextView) this.findViewById(R.id.iv_SelectStore);
		tv_SelectStore.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					tv_SelectStore
							.setBackgroundColor(color.abc_search_url_text_pressed);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					tv_SelectStore.setBackgroundColor(0);
					storeDialog.showDialog();
					return true;
				}
				return false;
			}
		});

		iv_Opencamera = (ImageView) this.findViewById(R.id.iv_opencamera);
		iv_Opencamera.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					iv_Opencamera
							.setBackgroundColor(color.abc_search_url_text_pressed);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					iv_Opencamera.setBackgroundColor(0);
					startScan();
					return true;
				}
				return false;
			}
		});

		bt_select_favorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				favoriteDialog.showDialog();
			}

		});
	}

	private void startScan() {
		Intent startScan = new Intent(SearchActivity.this,
				CaptureActivity.class);
		startActivityForResult(startScan, 0);
	}

	/**
	 * show selected dialog
	 * 
	 * */
	private SelectDialog getSelectDialog(String path, SelectedDialogCallback sdc) {
		SelectDialog selectDialog = null;
		if (selectDialog == null) {
			selectDialog = new SelectDialog();
			selectDialog.setContext(this);
			selectDialog.setFileName(path);
			selectDialog.setFactory(FactoryBuilder.getFactory(path));
			selectDialog.setItemList();
			selectDialog.setCallBack(sdc);
		}
		return selectDialog;
	}

	private TabHost tabHost;

	public void initTab() {
		tabHost = getTabHost();
		TabSpec page1 = tabHost.newTabSpec("tab1")
				.setIndicator("Search Result").setContent(R.id.tab1);
		tabHost.addTab(page1);

		TabSpec page2 = tabHost.newTabSpec("tab2").setIndicator("Favorite")
				.setContent(R.id.tab2);
		tabHost.addTab(page2);
	}

	private com.group5.widget.SlideCutListView lv_Products;
	private com.group5.widget.SlideCutListView lv_product_search_favorite;
	private ListController shoppingListController;
	private ListController favoriteListController;
	private ArrayList<Price> priceList = new ArrayList<Price>();;
	private SimpleAdapter mSimpleAdapter;
	private SimpleAdapter favoriteSimpleAdapter;
	private Factory priceFactory = new PriceFactory();
	private Factory productFactory = new ProductFactory();
	private ArrayList favorite_in_Listview;
	private void initFavoriteListView() {
		favoriteListController.setPathName(WebService.METHODNAME_GETPRODUCT);
		favoriteListController.setFactory(productFactory);
		favoriteListController.init();

		lv_product_search_favorite = (com.group5.widget.SlideCutListView) this
				.findViewById(R.id.lv_product_search_favorite);
		
		lv_product_search_favorite.setRemoveListener(new RemoveListener(){

			@Override
			public void removeItem(RemoveDirection direction, int position) {
				// TODO Auto-generated method stub
				
			}
		});
		favorite_in_Listview = new ArrayList<HashMap<String, String>>();
		favoriteSimpleAdapter = new SimpleAdapter(this, favorite_in_Listview,
				R.layout.listitem, new String[] { "productName", 
						"isSelected" }, new int[] {
						R.id.tv_productName,
						R.id.tv_isSelected });
		lv_product_search_favorite.setAdapter(favoriteSimpleAdapter);
		favorite_in_Listview.clear();
		for (int i = 0; i < favoriteDialog.getSelectedSalesBeanList().size(); i++) {
			Product pricez = (Product) favoriteDialog.getSelectedSalesBeanList().get(i);
			if (pricez.isSelected()) {
				HashMap<String, String> mPrice = new HashMap<String, String>();
				String inShoppinglist = "";
				mPrice.put("productName", pricez.getName());
				mPrice.put("isSelected", inShoppinglist);
				favorite_in_Listview.add(mPrice);
			}
		}
		
		favoriteSimpleAdapter.notifyDataSetChanged();
	}
		

	private void initListView() {

		shoppingListController.setPathName("getShoppingList");
		shoppingListController.setFactory(priceFactory);
		shoppingListController.init();

		// init listview
		lv_Products = (com.group5.widget.SlideCutListView) this
				.findViewById(R.id.lv_products_search_result);

		lv_Products.setRemoveListener(new RemoveListener() {

			@Override
			public void removeItem(RemoveDirection direction, int position) {
				// TODO Auto-generated method stub
				Price price = (Price) priceList.get(position);
				if (direction == RemoveDirection.RIGHT) {
					price.setSelected(true);
					shoppingListController.getList();
					shoppingListController.add(price);
					productList_in_ListView.clear();
				} else {
					shoppingListController.getList();
					shoppingListController.remove(price);
					price.setSelected(false);
					productList_in_ListView.clear();
				}
				for (int i = 0; i < priceList.size(); i++) {
					HashMap<String, String> mPrice = new HashMap<String, String>();
					Price pricez = (Price) priceList.get(i);
					mPrice.put("productName", pricez.getProduct().getName());
					mPrice.put("price", String.valueOf("$" + pricez.getPrice()));
					mPrice.put("store", pricez.getStore().getName());
					String inShoppinglist = "";
					if (pricez.isSelected()) {
						inShoppinglist = "is in shoppinglist";
					}
					mPrice.put("isSelected", inShoppinglist);
					productList_in_ListView.add(mPrice);
				}
				mSimpleAdapter.notifyDataSetChanged();
			}
		});

		// init List View
		productList_in_ListView = new ArrayList<HashMap<String, String>>();
		mSimpleAdapter = new SimpleAdapter(this, productList_in_ListView,
				R.layout.listitem, new String[] { "productName", "price",
						"store", "isSelected" }, new int[] {
						R.id.tv_productName, R.id.tv_price, R.id.tv_store,
						R.id.tv_isSelected });
		lv_Products.setAdapter(mSimpleAdapter);

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
				intent.setClass(SearchActivity.this, ProductDetail.class);
				SearchActivity.this.startActivity(intent);
			}
		});
	}

	public void searchProduct(String searchString) {
		if (searchString.equalsIgnoreCase("") || searchString == null) {
			Toast.makeText(SearchActivity.this,
					"Please input the product name!", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		String storeid = null;
		String classfyid = null;
		// if the user select one store the use selected_store
		StringBuilder sb = new StringBuilder();
		if (storeDialog.getSelectedSalesBeanList().size() == 0) {
			Toast.makeText(SearchActivity.this,
					"Please select at least one store!", Toast.LENGTH_SHORT)
					.show();
			storeDialog.showDialog();
			return;
		}
		for (SalesBean store : (SalesBeanList) (storeDialog
				.getSelectedSalesBeanList())) {
			sb.append(store.getUuid() + ",");
		}
		storeid = sb.toString();
		storeid = storeid.substring(0, storeid.length() - 1);
		WebService.getRemotePrices(searchString, classfyid, storeid,
				new WebServiceCallBack() {
					@Override
					public void callBack(SalesBeanList result) {
						if (result == null || result.size() == 0) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									SearchActivity.this);
							builder.setTitle("No such product!");

							builder.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									});

							builder.setNegativeButton("Select more Store",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											storeDialog.showDialog();
										}
									});
							builder.show();
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
							mPrice.put("isSelected", price.isSelected() + "");
							productList_in_ListView.add(mPrice);
						}
						mSimpleAdapter.notifyDataSetChanged();
					}
				});
	}

	/***
	 * search product by barcode .
	 */
	public void searchProductbyBarcode(String searchString) {
		if (searchString.equalsIgnoreCase("") || searchString == null) {
			Toast.makeText(SearchActivity.this,
					"Please input the product name!", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		String storeid = null;
		String classfyid = null;
		// if the user select one store the use selected_store
		StringBuilder sb = new StringBuilder();
		for (SalesBean store : (SalesBeanList) (storeDialog
				.getSelectedSalesBeanList())) {
			sb.append(store.getUuid() + ",");
		}
		storeid = sb.toString();
		storeid = storeid.substring(0, storeid.length() - 1);
		WebService.getRemotePricesbyBarcode(searchString, classfyid, storeid,
				new WebServiceCallBack() {
					@Override
					public void callBack(SalesBeanList result) {
						if (result == null || result.size() == 0) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									SearchActivity.this);
							builder.setTitle("No such product!");

							builder.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									});

							builder.setNegativeButton("Select more Store",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											storeDialog.showDialog();
										}
									});
							builder.show();
						}
						Log.e("result", result.size() + "");
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
					}
				});
	}

	/**
	 * get the result from the scan activity
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			String result = data.getExtras().getString("result");
			Log.e("on result", result);
			if (result.startsWith("0"))
				result = result.substring(1);
			searchProductbyBarcode(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		return false;// 返回为true 则显示系统menu
	}

}