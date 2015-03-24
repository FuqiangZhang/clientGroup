package com.group5.client.menu;

import java.util.ArrayList;

import org.json.JSONObject;

import com.group5.bean.SalesBean;
import com.group5.bean.SalesBeanList;
import com.group5.bean.Store;
import com.group5.client.communication.WebService;
import com.group5.factory.Factory;
import com.group5.factory.StoreFactory;
import com.group5.property.CacheDataController;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

/**
 * to show a select dialog use it like this: SelectDialog sd = new
 * SelectDialog(); sd.setContext(this);
 * sd.setFileName(WebService.METHODNAME_XXXX); sd.setFactory(new XXXXFactory());
 * sd.setItemList(); sd.showDialog();
 * 
 * and don't forget to create a file for the dialog, which will store the json
 * information
 * 
 * */
public class SelectDialog {
	String[] items;
	boolean[] ifChecked;
	Context context;
	private String fileName;
	private SalesBeanList sbL = null;
	private Factory factory;
	private CacheDataController cdc;
	private SelectedDialogCallback sdc;

	/**
	 * 1. setcontext 2. setfilename 3. showdialog
	 * */
	public SelectDialog() {
	}
	
	public void setCallBack(SelectedDialogCallback sdc){
		this.sdc = sdc;
	}
	
	public void setItems(String[] items) {
		this.items = items;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	public void setCheckedArray(boolean[] ifChecked) {
		this.ifChecked = ifChecked.clone();
	}

	public void setContext(Context context) {
		this.context = context;
	}

	// set which file you wanna read and write (get and set item list from that
	// file)
	public void setFileName(String fileName) {
		this.fileName = fileName;

	}

	public SalesBeanList getSelectedSalesBeanList() {
		SalesBeanList selected_sbl = new SalesBeanList();
		if (sbL != null) {
			for (SalesBean sb : sbL) {
				System.out.println(sb.isSelected());
				if (sb.isSelected()) {
					selected_sbl.add(sb);
				}
			}
		}
		return selected_sbl;
	}

	public void setItemList() {
		ArrayList<String> al = new ArrayList();
		cdc = new CacheDataController();
		cdc.setPathName(fileName);
		cdc.setFactory(factory);
		if (cdc.iFExist()) {
			sbL = cdc.getSalesBeanList(null);
			for (SalesBean store : sbL) {
				al.add(store.getName());
			}
		} else {
			Log.e("SelectDialog ERR",
					"Please create the json file before you use the dialog");
			items = new String[] { "E", "R", "R" };
			ifChecked = new boolean[] { false, false, false };
			return;
		}
		String[] strArr = null;
		if (al != null) {
			strArr = (String[]) al.toArray(new String[1]);

			boolean[] checkedArray = new boolean[al.size()];
			for (int i = 0; i < al.size(); i++) {
				checkedArray[i] = sbL.get(i).isSelected();
			}
			ifChecked = checkedArray.clone();
		}
		this.items = strArr;
	}

	public void showDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Select To Compare");
		builder.setMultiChoiceItems(items, ifChecked,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						if (sbL != null) {
							System.out.println(sbL.get(which).isSelected());
							sbL.get(which).setSelected(isChecked);
							System.out.println(sbL.get(which).isSelected());
						}
					}
				});
		builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (getSelectedSalesBeanList().size() == 0) {
					Toast.makeText(context,
							"You Must Select at Least One Store",
							Toast.LENGTH_SHORT).show();
					showDialog();
					return;
				}
				if (sbL != null && sbL.size() != 0) {
					cdc.setPathName(fileName);
					String json = cdc.getJsonDataBySalesBeanList(sbL);
					cdc.writeFile(json);
					sdc.callback();
					
				} else {
					Toast.makeText(context,
							"You Must Select at Least One ",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		builder.show();
	}
	
	public interface SelectedDialogCallback{
		public void callback();
	};
}
