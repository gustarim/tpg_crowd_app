package ch.unige.tpgcrowd.google.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import ch.unige.tpgcrowd.google.GooglePlayServiceCheckUtility;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognitionClient;

public class ActivityRecognitionHandler implements ConnectionCallbacks,
		OnConnectionFailedListener {
	private static ActivityRecognitionHandler ahandler;
	
	public static ActivityRecognitionHandler getActivityRecognitionHandler() {
		if (ahandler == null) {
			ahandler = new ActivityRecognitionHandler();
		}
		return ahandler;
	}
	
	 // Constants that define the activity detection interval
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int DETECTION_INTERVAL_SECONDS = 20;
    public static final int DETECTION_INTERVAL_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;
    
    private enum REQUEST_TYPE {START, STOP}
    /*
     * Store the PendingIntent used to send activity recognition events
     * back to the app
     */
    private PendingIntent activityRecognitionPendingIntent;
    // Store the current activity recognition client
    private ActivityRecognitionClient activityRecognitionClient;
    private boolean inProgress;
    private FragmentActivity activity;
    private REQUEST_TYPE requestType;
    
    public ActivityRecognitionHandler() {
		inProgress = false;
	}
    
    public boolean startActivityRecognition(final FragmentActivity activity) {
    	return connectToActivityRecognitionclient(activity, REQUEST_TYPE.START);
    }
    
    public boolean stopActivityRecognition(final FragmentActivity activity) {
    	return connectToActivityRecognitionclient(activity, REQUEST_TYPE.STOP);
    }

	@Override
	public void onConnected(final Bundle connectionHint) {
		switch (requestType) {
		case START :
			final Intent intent = new Intent(
					activity.getApplicationContext(), ActivityRecognitionIntentService.class);
			
			activityRecognitionPendingIntent =
	                PendingIntent.getService(activity.getApplicationContext(), 0, intent,
	                PendingIntent.FLAG_UPDATE_CURRENT);
			
			activityRecognitionClient.requestActivityUpdates(
	                DETECTION_INTERVAL_MILLISECONDS,
	                activityRecognitionPendingIntent);
			break;
		case STOP:
			activityRecognitionClient.removeActivityUpdates(activityRecognitionPendingIntent);
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
			GooglePlayServiceCheckUtility.tryResolutionOnConnectionError(connectionResult, activity);
			//	        // If no resolution is available, display an error dialog
		} else {
			//	            // Get the error code
			final int errorCode = connectionResult.getErrorCode();
			GooglePlayServiceCheckUtility.generateErrorDialog(activity, errorCode);
		}
	}

	private boolean connectToActivityRecognitionclient(final FragmentActivity activity, final REQUEST_TYPE requestType) {
		boolean result = false;
		this.activity = activity;
		this.requestType = requestType;
		if (GooglePlayServiceCheckUtility.servicesConnected(activity)) {
			/*
			 * Create a new location client object. Since the current
			 * activity class implements ConnectionCallbacks and
			 * OnConnectionFailedListener, pass the current activity object
			 * as the listener for both parameters
			 */
			activityRecognitionClient = new ActivityRecognitionClient(activity.getApplicationContext(), this, this);
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
		}
		return result;
	}
}
