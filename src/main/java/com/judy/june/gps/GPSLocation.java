package com.judy.june.gps;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.judy.june.wifi.R;

import android.app.Activity;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GPSLocation extends Activity implements LocationListener {
	private static final String TAG = GPSLocation.class.getSimpleName();
	private static final String[] S = { "Out of Service", "Temporarily Unavailable", "Available" };

	private TextView output;
	private LocationManager locationManager;
	private List<LocationProvider> providers = new ArrayList<LocationProvider>();
	private String bestProvider;
	private Geocoder geocoder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gpslocation);
		// Get the output UI
		output = (TextView) findViewById(R.id.output);

		// Get the location manager
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		geocoder = new Geocoder(this);

		output.setText("");
		// List all providers:
		List<String> provids = locationManager.getAllProviders();
		output.append("The providers: " + provids + "\n\n");
		LocationProvider provider = null;
		for (String provid : provids) {
			provider = locationManager.getProvider(provid);
			providers.add(provider);
			printProvider(provider);
		}
		
		Criteria criteria = new Criteria();
		bestProvider = locationManager.getBestProvider(criteria, false);
		output.append("\n\nBEST Provider:" + bestProvider + "\n");

		output.append("\n\nLocations (starting with last known):");
		Location location = null;
		for (LocationProvider provid : providers) {
			location = locationManager.getLastKnownLocation(provid.getName());
			printLocation(location);
		} 
	}

	@Override
	protected void onResume() {
		super.onResume();
		for (LocationProvider provider : providers) {
			locationManager.requestLocationUpdates(provider.getName(), 3000, 1, this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onLocationChanged(android.location.
	 * Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		printLocation(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String,
	 * int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		output.append("\n\nProvider Status Changed: " + provider + ", Status=" + S[status] + ", Extras=" + extras);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
		output.append("\n\nProvider Enabled: " + provider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
		output.append("\n\nProvider Disabled: " + provider);
	}

	private void printLocation(Location location) {
		if (location == null)
			output.append("\nLocation[unknown]\n\n");
		else {
			Locale local = Locale.CHINESE;
			String text = String.format(local, "Latitude:\t %f \nLongitude:\t %f\n Altitude:\t %f\n Bearing:\t %f\n",
					location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getBearing());
			Log.d(TAG, text);
			output.append("\n\n" + text);
		}
		try {
			List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
			for (Address address : addresses) {
				output.append("\n" + address.getAddressLine(0));
			}
		} catch (Exception e) {
			Log.e("WhereAmI", "Couldn't get Geocoder data", e);
		}
	}

	private void printProvider(LocationProvider info) {
		Log.d(TAG, "Name:" + info.getName());
		Log.d(TAG, "Accuracy:" + info.getAccuracy());
		Log.d(TAG, "Require Cell?  " + info.requiresCell());
		Log.d(TAG, "Require Network?  " + info.requiresNetwork());
		Log.d(TAG, "Require Satellite?  " + info.requiresSatellite());
		Log.d(TAG, "Supports Altitude? " + info.supportsAltitude());
		Log.d(TAG, "Supports Bearing? " + info.supportsBearing());
		Log.d(TAG, "Supports Speed? " + info.supportsSpeed());
		Log.d(TAG, "Power requirement?" + info.getPowerRequirement());
		Log.d(TAG, "Might steal my money?" + info.hasMonetaryCost());

		output.append("Name:" + info.getName() + "\n");
		output.append("Accuracy:" + info.getAccuracy() + "\n");
		output.append("Require Cell?  " + info.requiresCell() + "\n");
		output.append("Require Network?  " + info.requiresNetwork() + "\n");
		output.append("Require Satellite?  " + info.requiresSatellite() + "\n");
		output.append("Supports Altitude? " + info.supportsAltitude() + "\n");
		output.append("Supports Bearing? " + info.supportsBearing() + "\n");
		output.append("Supports Speed? " + info.supportsSpeed() + "\n");
		output.append("Power requirement?" + info.getPowerRequirement() + "\n");
		output.append("Might steal my money?" + info.hasMonetaryCost() + "\n\n");
	}

}
