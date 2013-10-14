package ch.unige.tpgcrowd.google.geofence;

import java.util.LinkedList;
import java.util.List;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;

public final class GeofenceService extends Service {
	private static final String HANDLER_NAME = "Geofence";
	private static final String EXTRA_GEOFENCE_IDS = null;
	private Handler handler;
	private int startId;
	// Holds the location client
    protected LocationClient locationClient;
    // Defines the allowable request types.
    public enum REQUEST_TYPE {ADD}
    private REQUEST_TYPE requestType;
    // Flag that indicates if a request is underway.
    private boolean inProgress;
    // Stores the PendingIntent used to request geofence monitoring
	private PendingIntent transitionPendingIntent;

	
	private class GeofancesManager implements Runnable, 
		ConnectionCallbacks, OnConnectionFailedListener,
		OnAddGeofencesResultListener {
		final private List<Geofence> currentGeofences;
		
		public GeofancesManager(final String[] ids) {
			final SimpleGeofenceStore store = new SimpleGeofenceStore(getApplicationContext());
			currentGeofences = new LinkedList<Geofence>();
			for (int i = 0; i < ids.length; i++) {
				currentGeofences.add(store.getGeofence(ids[i]).toGeofence());
			}
		}
		
		@Override
		public void run() {
			connectLocationService();
		}

		/**
	     * Start a request for geofence monitoring by calling
	     * LocationClient.connect().
	     */
	    public void connectLocationService() {
	        // Start a request to add geofences
	        requestType = REQUEST_TYPE.ADD;
	        //TODO check the GP services in ACTIVITY before to start this service!
//	        /*
//	         * Test for Google Play services after setting the request type.
//	         * If Google Play services isn't present, the proper request
//	         * can be restarted.
//	         */
//	        if (!GooglePlayServiceCheckUtility.servicesConnected()) {
//	            return;
//	        }
	        /*
	         * Create a new location client object. Since the current
	         * activity class implements ConnectionCallbacks and
	         * OnConnectionFailedListener, pass the current activity object
	         * as the listener for both parameters
	         */
	        locationClient = new LocationClient(getApplicationContext(), this, this);
	        // If a request is not already underway
	        if (!inProgress) {
	            // Indicate that a request is underway
	            inProgress = true;
	            // Request a connection from the client to Location Services
	            locationClient.connect();
	        } else {
	            /*
	             * A request is already underway. You can handle
	             * this situation by disconnecting the client,
	             * re-setting the flag, and then re-trying the
	             * request.
	             */
	        	stopSelf(startId);
	        }
	    }

		@Override
		public void onAddGeofencesResult(final int statusCode, final String[] geofenceIds) {
			// If adding the geofences was successful
	        if (LocationStatusCodes.SUCCESS == statusCode) {
	            /*
	             * Handle successful addition of geofences here.
	             * You can send out a broadcast intent or update the UI.
	             * geofences into the Intent's extended data.
	             */
	        	//TODO
	        } else {
	        // If adding the geofences failed
	            /*
	             * Report errors here.
	             * You can log the error using Log.e() or update
	             * the UI.
	             */
	        	//TODO
	        }
	        // Turn off the in progress flag and disconnect the client
	        inProgress = false;
	        locationClient.disconnect();
	        
	        stopSelf(startId);
		}

		@Override
		public void onConnectionFailed(final ConnectionResult connectionResult) {
			// Turn off the request flag
	        inProgress = false;
	        //TODO RETURN AN ERROR TO AN ACTIVITY... In activity implement above code..
//	        /*
//	         * If the error has a resolution, start a Google Play services
//	         * activity to resolve it.
//	         */
//	        if (connectionResult.hasResolution()) {
//	            try {
//	                connectionResult.startResolutionForResult(
//	                        this,
//	                        GooglePlayServiceCheckUtility.CONNECTION_FAILURE_RESOLUTION_REQUEST);
//	            } catch (final SendIntentException e) {
//	                // Log the error
//	                e.printStackTrace();
//	            }
//	        // If no resolution is available, display an error dialog
//	        } else {
//	            // Get the error code
//	            int errorCode = connectionResult.getErrorCode();
//	            // Get the error dialog from Google Play services
//	            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
//	                    errorCode,
//	                    this,
//	                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
//	            // If Google Play services can provide an error dialog
//	            if (errorDialog != null) {
//	                // Create a new DialogFragment for the error dialog
//	                ErrorDialogFragment errorFragment =
//	                        new ErrorDialogFragment();
//	                // Set the dialog in the DialogFragment
//	                errorFragment.setDialog(errorDialog);
//	                // Show the error dialog in the DialogFragment
//	                errorFragment.show(
//	                        getSupportFragmentManager(),
//	                        "Geofence Detection");
//	            }
//	        }
		}

		@Override
		public void onConnected(final Bundle connectionHint) {
			switch (requestType) {
			case ADD :
				// Get the PendingIntent for the request
				transitionPendingIntent = getTransitionPendingIntent();
				// Send a request to add the current geofences
				locationClient.addGeofences(
						currentGeofences, transitionPendingIntent, this);
			}
		}

		@Override
		public void onDisconnected() {
			// Turn off the request flag
	        inProgress = false;
	        // Destroy the current location client
	        locationClient = null;
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		inProgress = false;
	}
	
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		this.startId = startId;
		if (intent.hasExtra(EXTRA_GEOFENCE_IDS)) {
			final HandlerThread ht = new HandlerThread(HANDLER_NAME + startId);
			ht.start();
			handler = new Handler(ht.getLooper());
			final String[] ids = intent.getStringArrayExtra(EXTRA_GEOFENCE_IDS);
			handler.post(new GeofancesManager(ids));
		}
		else {
			stopSelf(startId);
		}
		return START_REDELIVER_INTENT;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// Turn off the request flag
        inProgress = false;
        //check if location client still connected
        if (locationClient.isConnected()) {
        	locationClient.disconnect();
        }
        // Destroy the current location client
        locationClient = null;
        handler.getLooper().quit();
	}

	@Override
	public IBinder onBind(final Intent arg0) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private PendingIntent getTransitionPendingIntent() {
        // Create an explicit Intent
        Intent intent = new Intent(this,
                ReceiveTransitionsIntentService.class);
        /*
         * Return the PendingIntent
         */
        return PendingIntent.getService(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
