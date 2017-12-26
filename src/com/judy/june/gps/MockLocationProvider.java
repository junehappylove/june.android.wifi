/**
 * 
 */
package com.judy.june.gps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * 伪装位置工具
 * 
 * @author junehappylove
 *
 */
public class MockLocationProvider {
	String providerName;
	Context ctx;

	public MockLocationProvider(String name, Context ctx) {
		this.providerName = name;
		this.ctx = ctx;

		LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		lm.addTestProvider(providerName, false, false, false, false, false, true, true, 0, 5);
		lm.setTestProviderEnabled(providerName, true);
	}

	public void pushLocation(double lat, double lon) {
		LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		Location mockLocation = new Location(providerName);
		mockLocation.setLatitude(lat);
		mockLocation.setLongitude(lon);
		mockLocation.setAltitude(0);
		mockLocation.setTime(System.currentTimeMillis());
		mockLocation.setAccuracy(Criteria.ACCURACY_FINE);
		mockLocation.setElapsedRealtimeNanos(System.currentTimeMillis());
		lm.setTestProviderLocation(providerName, mockLocation);
	}

	public void shutdown() {
		LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		lm.removeTestProvider(providerName);
	}
}
