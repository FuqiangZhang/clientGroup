package com.group5.client.menu;

import com.g5.saleshare.R;
import com.g5.saleshare.R.id;
import com.g5.saleshare.R.layout;
import com.g5.saleshare.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class NewActiviy extends Activity {

	private Button bt_Test;
	private TextView tv_Test;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_activiy);
		initUI();
	}
	
	private void initUI(){
		bt_Test = (Button) this.findViewById(R.id.button1);
		tv_Test = (TextView) this.findViewById(R.id.textView1);
		
		bt_Test.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_Test.setText("xiao ye zhen niu bi");
			}
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_activiy, menu);
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
