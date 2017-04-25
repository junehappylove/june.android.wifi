package com.judy.june.wifi;

import java.util.List;

import com.judy.june.util.Root;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(!Root.getRootAhth()){
			setTitle(this.getTitle()+"\t请先获取root权限");
			return;
		}
		List<WifiInfo> list = WifiUtils.wifis();
		int num = list==null?0:list.size();
		setTitle(this.getTitle()+"\t共"+num+"条记录");
		ListView view = (ListView) findViewById(R.id.listView1);
		WifiAdapter wifi = new WifiAdapter(list, MainActivity.this);
		view.setAdapter(wifi);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	class WifiAdapter extends BaseAdapter{
		List<WifiInfo> list = null;
		Context context;
		
		public WifiAdapter(List<WifiInfo> list, Context context) {
			super();
			this.list = list;
			this.context = context;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
			tv.setText((position+1)+"\nwifi:"+list.get(position).getSsid()+"\npsk:"+list.get(position).getPass());
			return convertView;
		}
		
	}
}
