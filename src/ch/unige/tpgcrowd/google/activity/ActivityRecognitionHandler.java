package ch.unige.tpgcrowd.google.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import ch.unige.tpgcrowd.google.GooglePlayServiceCheckUtility;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognitionClient;

public class ActivityRecognitionHandler implements ConnectionCallbacks,
		OnConnectionFailedListener {
	
	 // Constants that define the activity detection interval
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int DETECTION_INTERVAL_SECONDS = 20;
    public static final int DETECTION_INTERVAL_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;
    
    public static final String ACTIVITY_RECOGNITION_START_TIME = "ch.unige.tpgcrowd.activity.START_TIME_KEY";
    
	private static final String SHARED_PREFERENCES =
			"SharedPreferences";
	
    private enum REQUEST_TYPE {START, STOP};
    /*
     * Store the PendingIntent used to send activity recognition events
     * back to the app
     */
    private final PendingIntent activityRecognitionPendingIntent;
    // Store the current activity recognition client
    private ActivityRecognitionClient activityRecognitionClient;
    private boolean inProgress;
    private REQUEST_TYPE requestType;
    
    private ActivityRecognitionHandler(final PendingIntent activityRecognitionPendingIntent,
    		final REQUEST_TYPE requestType) {
    	this.activityRecognitionPendingIntent = activityRecognitionPendingIntent;
    	this.requestType = requestType;
		inProgress = false;
	}
    
    public static boolean startActivityRecognition(final Context context, 
    		final PendingIntent activityRecognitionPendingIntent) {
    	
    	SharedPreferences mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		final Editor editor = mPrefs.edit();
		
		// Write the start time value to SharedPreferences
		editor.putLong(ACTIVITY_RECOGNITION_START_TIME,System.currentTimeMillis());
		editor.commit();
    	    	
    	final ActivityRecognitionHandler ahandler = new ActivityRecognitionHandler(activityRecognitionPendingIntent,
    			REQUEST_TYPE.START);
    	return ahandler.connectToActivityRecognitionclient(context);
    }
    
    public static boolean stopActivityRecognition(final Context context, 
    		final PendingIntent activityRecognitionPendingIntent) {
    	final ActivityRecognitionHandler ahandler = new ActivityRecognitionHandler(activityRecognitionPendingIntent,
    			REQUEST_TYPE.STOP);
    	return ahandler.connectToActivityRecognitionclient(context);
    }

	@Override
	public void onConnected(final Bundle connectionHint) {
		switch (requestType) {
		case START :
			activityRecognitionClient.requestActivityUpdates(
	                DETECTION_INTERVAL_MILLISECONDS,
	                activityRecognitionPendingIntent);
			Log.i("ActivityRecHandler", "started activity recognition");
			break;
		case STOP:
			activityRecognitionClient.removeActivityUpdates(activityRecognitionPendingIntent);
			Log.i("ActivityRecHandler", "stop activity recognition");
			break;
		}
		
        /*
         * Since the preceding call is synchronous, turn off the
         * in progress flag and disconnect the client
         */
        inProgress = false;
        activityRecognitionClient.disconnect();
	}

	@Override
	public void onDisconnected() {
		// Turn off the request flag
        inProgress = false;
        // Delete the client
        activityRecognitionClient = null;
	}
	
	@Override
	public void onConnectionFailed(final ConnectionResult connectionResult) {
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

	private boolean connectToActivityRecognitionclient(final Context context) {
		boolean result = false;
		
		//MOVED TO MAIN ACTIVITY!
//		if (GooglePlayServiceCheckUtility.servicesConnected(activity)) {
			/*
			 * Create a new location client object. Since the current
			 * activity class implements ConnectionCallbacks and
			 * OnConnectionFailedListener, pass the current activity object
			 * as the listener for both parameters
			 */
		activityRecognitionClient = new ActivityRecognitionClient(context, this, this);
		// If a request is not already underway
		if (!inProgress) {
			// Indicate that a request is underway
			inProgress = true;
			// Request a connection from the client to Location Services
			activityRecognitionClient.connect();
			result = true;
		} else {
			/*
			 * A request is already underway. You can handle
			 * this situation by disconnecting the client,
			 * re-setting the flag, and then re-trying the
			 * request.
			 */
			activityRecognitionClient.disconnect();
		}
//		}
		return result;
	}
}
