package location;

import java.util.Iterator;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class GpsLocation implements Runnable {
	private LocationManager locationManager;
	private static final int minTime = 3000;
	private static final int minDistance = 10;
	private Context context;
	private static final String TAG = "GpsActivity";

	private Handler mHandler;

	/**
	 * Send address 2 activity
	 * 
	 * @param context
	 *            context
	 * @param Handler
	 *            handler in the activity to change the UI
	 * */
	public GpsLocation(Context context, Handler mHandler) {
		this.context = context;
		this.mHandler = mHandler;
	}

	/**
	 * Send address 2 activity
	 * 
	 * @param
	 * 
	 * */
	private void sendLocation2Activity() {
		Location location = getLoc();
		String st_location = LocateTranslate.getAddress(
				location.getLongitude(), location.getLatitude());
		Message msg = new Message();
		Bundle bd = new Bundle();
		bd.putDouble("mlat", location.getLatitude());
		bd.putDouble("mlng", location.getLongitude());
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	public Location getLoc() {
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.addGpsStatusListener(listener);
		LocationListener locationListener = new LocationListener() {

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {

			}

			public void onLocationChanged(Location location) {
				if (location != null) {
					Log.e("Map",
							"Location changed : Lat: " + location.getLatitude()
									+ " Lng: " + location.getLongitude());
				}
			}
		};
		locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,
				minTime, minDistance, locationListener);
		Location location = null;
		if (!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
			String s = "Please open GPS...";
			CharSequence cs = s;
			// argument should be the activity that use this function
		}
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		} else {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, minTime, minDistance,
					(android.location.LocationListener) locationListener);

		}
		return location;
	}

	private GpsStatus.Listener listener = new GpsStatus.Listener() {

		@Override
		public void onGpsStatusChanged(int event) {
			// TODO Auto-generated method stub
			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.i(TAG, "First Locate");
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.i(TAG, "Satellite status changed");
				GpsStatus gpsStatus = locationManager.getGpsStatus(null);
				// The max count of satellites
				int maxSatellites = gpsStatus.getMaxSatellites();
				Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
						.iterator();
				int count = 0;
				while (iters.hasNext() && count <= maxSatellites) {
					GpsSatellite s = iters.next();
					count++;
				}
				System.out.println("find " + count + " satellites.");
			case GpsStatus.GPS_EVENT_STARTED:
				Log.i(TAG, "Start Locate");
				break;
			// ��λ����
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.i(TAG, "Stop Locate");
				break;

			default:
				break;
			}

		}
	};

	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(false);
		criteria.setBearingRequired(false);
		criteria.setAltitudeRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	@Override
	public void run() {
		Looper.prepare();
		sendLocation2Activity();

	}

}