package com.group5.client.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.g5.saleshare.R;
import com.group5.bean.Price;
import com.group5.bean.SalesBeanList;
import com.group5.controller.ListController;
import com.group5.factory.Factory;
import com.group5.factory.PriceFactory;
import com.group5.widget.SlideCutListView;
import com.group5.widget.SlideCutListView.RemoveDirection;
import com.group5.widget.SlideCutListView.RemoveListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.appcompat.R.color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class ShoppingListActivity extends Activity {

	private SlideCutListView lv_products_shoppinglist;
	private List<HashMap<String, String>> productList_in_ListView;
	private SimpleAdapter mSimpleAdapter;
	private SalesBeanList shoppingList;
	private ListController slc;
	private ImageView iv_home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);
		initShoppingList();
		initListView();
	}

	// init shoppinglist
	private void initShoppingList() {
		if (slc == null) {
			slc = new ListController();
			Factory priceFactory = new PriceFactory();
			slc.setPathName("getShoppingList");
			slc.setFactory(priceFactory);
			slc.init();
		}
		productList_in_ListView = new ArrayList<HashMap<String, String>>();
		shoppingList = slc.getList();
		for (int i = 0; i < shoppingList.size(); i++) {
			HashMap<String, String> mPrice = new HashMap<String, String>();
			Price price = (Price) shoppingList.get(i);
			mPrice.put("productName", price.getProduct().getName());
			mPrice.put("price", String.valueOf("$" + price.getPrice()));
			mPrice.put("store", price.getStore().getName());
			productList_in_ListView.add(mPrice);
		}
	}

	private void removePrice(int which) {
		slc.remove(which);
		shoppingList = slc.getList();
		productList_in_ListView.remove(which);
		mSimpleAdapter.notifyDataSetChanged();
	}

	// init list view
	private void initListView() {
		
		iv_home = (ImageView) this.findViewById(R.id.iv_backtomain);
		iv_home.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					iv_home
							.setBackgroundColor(color.abc_search_url_text_pressed);
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					iv_home.setBackgroundColor(0);
					ShoppingListActivity.this.finish();
					return true;
				}
				return false;
			}
			
		});
		
		// init listview
		lv_products_shoppinglist = (SlideCutListView) this
				.findViewById(R.id.lv_products_shoppinglist);

		mSimpleAdapter = new SimpleAdapter(this, productList_in_ListView,
				R.layout.listitem, new String[] { "productName", "price",
						"store" }, new int[] { R.id.tv_productName,
						R.id.tv_price, R.id.tv_store });
		lv_products_shoppinglist.setAdapter(mSimpleAdapter);
		lv_products_shoppinglist.setRemoveListener(new RemoveListener() {
			@Override
			public void removeItem(RemoveDirection direction, int position) {
				// TODO Auto-generated method stub
				removePrice(position);
			}
		});
		lv_products_shoppinglist
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int selected_item, long arg3) {
						productList_in_ListView.get(selected_item);
						Bundle bd = new Bundle();
						bd.putSerializable("all", shoppingList);
						bd.putSerializable("selected_price",
								shoppingList.get(selected_item));
						Intent intent = new Intent();
						intent.putExtras(bd);
						intent.setClass(ShoppingListActivity.this,
								ProductDetail.class);
						ShoppingListActivity.this.startActivity(intent);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping_list, menu);
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
}
