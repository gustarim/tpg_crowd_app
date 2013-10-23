package ch.unige.tpgcrowd.google.location;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import ch.unige.tpgcrowd.google.GooglePlayServiceCheckUtility;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class LocationHandler implements ConnectionCallbacks,
OnConnectionFailedListener{
	
	Location currentLocation;
	
	// Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 7;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 4;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
	    
    private enum REQUEST_TYPE {START, STOP}
   
    private boolean inProgress;
    
    
    private final PendingIntent locationPendingIntent;
    
 // Holds the location client
 	private LocationClient locationClient;
 	
 	private LocationRequest locationRequest;
 	
    private REQUEST_TYPE requestType;
    
    private LocationHandler(final PendingIntent locationPendingIntent, final REQUEST_TYPE requestType){
    	this.locationPendingIntent = locationPendingIntent;
    	this.requestType = requestType;
		inProgress = false;
    }
    // Start automatic location updates
    public static boolean startLocation(final Context context, 
    		final PendingIntent locationPendingIntent) {
    	final LocationHandler lhandler = new LocationHandler(locationPendingIntent,REQUEST_TYPE.START);
    	return lhandler.connectLocationService(context);
    }
    // Stop updates, disconnect
    public static boolean stopLocation(final Context context, 
    		final PendingIntent locationPendingIntent) {
    	final LocationHandler lhandler = new LocationHandler(locationPendingIntent,REQUEST_TYPE.STOP);
    	return lhandler.connectLocationService(context);
    }
    
    private boolean connectLocationService(final Context context) {
		boolean result = false;
		/*
		 * Test for Google Play services after setting the request type.
		 * If Google Play services isn't present, the proper request
		 * can be restarted.
		 */
		//MOVED TO MAIN ACTIVITY!
//		if (GooglePlayServiceCheckUtility.servicesConnected(activity)) {
			/*
			 * Create a new location client object. Since the current
			 * activity class implements ConnectionCallbacks and
			 * OnConnectionFailedListener, pass the current activity object
			 * as the listener for both parameters
			 */
		locationClient = new LocationClient(context, this, this);
		        
		// If a request is not already underway
		if (!inProgress) {
			// Indicate that a request is underway
			inProgress = true;
			// Create the LocationRequest object
	        locationRequest = LocationRequest.create();
	        // Use high accuracy
	        locationRequest.setPriority(
	                LocationRequest.PRIORITY_HIGH_ACCURACY);
	        // Set the update interval to 5 seconds
	        locationRequest.setInterval(UPDATE_INTERVAL);
	        // Set the fastest update interval to 1 second
	        locationRequest.setFastestInterval(FASTEST_INTERVAL);
			// Request a connection from the client to Location Services
			locationClient.connect();
			result = true;
		} else {
			/*
			 * A request is already underway. You can handle
			 * this situation by disconnecting the client,
			 * re-setting the flag, and then re-trying the
			 * request.
			 */
			locationClient.disconnect();
		}
//		}
		return result;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// Turn off the request flag
		inProgress = false;
		//	        /*
		//	         * If the error has a resolution, start a Google Play services
		//	         * activity to resolve it.
		//	         */
		if (connectionResult.hasResolution()) {
			GooglePlayServiceCheckUtility.tryResolutionOnConnectionError(connectionResult);
			//	        // If no resolution is available, display an error dialog
		} else {
			//	            // Get the error code
			final int errorCode = connectionResult.getErrorCode();
			GooglePlayServiceCheckUtility.generateErrorDialog(errorCode);
		}
		
	}

	@Override
	public void onConnected(final Bundle connectionHint) {
		switch (requestType) {
		case START :
			locationClient.requestLocationUpdates(locationRequest,locationPendingIntent);
			break;
		case STOP:
			locationClient.removeLocationUpdates(locationPendingIntent);
			break;
		}
		
        /*
         * Since the preceding call is synchronous, turn off the
         * in progress flag and disconnect the client
         */
        inProgress = false;
        locationClient.disconnect();
		
	}

	@Override
	public void onDisconnected() {
		// Turn off the request flag
		inProgress = false;
		// Destroy the current location client
		locationClient = null;
		
	}


}
