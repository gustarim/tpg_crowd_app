package ch.unige.tpgcrowd.google.location;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
//import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
//import com.google.android.gms.location.LocationClient;

import com.google.android.gms.location.LocationClient;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

public class LocationIntentService extends IntentService{// implements ConnectionCallbacks, OnConnectionFailedListener {
	
	Location currentLocation;

	private static final String NAME = "LocationIntentService";
	
	public LocationIntentService() {
		super(NAME);
	}
	
	public static PendingIntent getLocationPendingIntent(final Context context) {
		// Create an explicit Intent
		final Intent intent = new Intent(context,
				LocationIntentService.class);
		/*
		 * Return the PendingIntent
		 */
		return PendingIntent.getService(
				context,
				0,
				intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	protected void onHandleIntent(Intent intent) {

		currentLocation = (Location) intent.getSerializableExtra(LocationClient.KEY_LOCATION_CHANGED);
        
    }



}
