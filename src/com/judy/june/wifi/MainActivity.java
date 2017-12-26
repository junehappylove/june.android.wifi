package com.judy.june.wifi;

import java.util.List;

import com.judy.june.gps.GPSLocation;
import com.judy.june.gps.MockLocationProvider;
import com.judy.june.util.Root;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
	private static final double dx = 038.267271d;
	private static final double dy = 122.272148d;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!Root.getRootAhth()) {
			setTitle(this.getTitle() + "\t请先获取root权限");
			return;
		}
		List<WifiInfo> list = WifiUtils.wifis();
		int num = list == null ? 0 : list.size();
		setTitle(this.getTitle() + "\t共" + num + "条记录");
		ListView view = (ListView) findViewById(R.id.list_wifi);
		WifiAdapter wifi = new WifiAdapter(list, MainActivity.this);
		view.setAdapter(wifi);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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

	public void onGPSClick(View source) {
		Intent intent = new Intent(MainActivity.this, GPSLocation.class);
		startActivity(intent);
	}

	MockLocationProvider mock;

	public void onQZDWClick(View source) {
		mock = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, this);
		mock.pushLocation(dx, dy);
		
		LocationManager locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocationListener lis = new LocationListener() {
			public void onLocationChanged(Location location) {
				// You will get the mock location
				mock.pushLocation(dx, dy);
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				mock.pushLocation(dx, dy);
			}

			@Override
			public void onProviderEnabled(String provider) {
				mock.pushLocation(dx, dy);
			}

			@Override
			public void onProviderDisabled(String provider) {
				mock.pushLocation(dx, dy);
			}
		};
		locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, lis);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
						mock.pushLocation(dx, dy);
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();;
	}
	
	@Override
	protected void onDestroy() {
		mock.shutdown();
		super.onDestroy();
	}

	class WifiAdapter extends BaseAdapter {
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
			tv.setText((position + 1) + "\nwifi:" + list.get(position).getSsid() + "\npsk:"
					+ list.get(position).getPass());
			return convertView;
		}

	}
}
