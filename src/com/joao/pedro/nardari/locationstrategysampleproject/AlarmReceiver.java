package com.joao.pedro.nardari.locationstrategysampleproject;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		turnOnGPSAndGetLocation(context);
	}
	
	private void turnOnGPSAndGetLocation(final Context context) {
		final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		// Try to gast lastknowlocation by GPS or Network provider
		Location getLast = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (getLast != null){ makeUseOfNewLocation(context, getLast); return; }
		getLast = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (getLast != null){ makeUseOfNewLocation(context, getLast); return; }
		
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
		    
			public void onLocationChanged(Location location) {
				makeUseOfNewLocation(context, location);
				
				// Turn Off Gps - Battery save
				locationManager.removeUpdates(this);
		    }

			public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		};

		// Register the listener with the Location Manager to receive location updates
		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}

	private void makeUseOfNewLocation(final Context context, Location location) {
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(context)
			    .setSmallIcon(R.drawable.ic_launcher)
			    .setContentTitle("Alarm!")
			    .setContentText("New Location! " + location.getLatitude());
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, mBuilder.build()); 
	}
}
