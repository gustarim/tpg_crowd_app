package ch.unige.tpgcrowd.google.geofence;

import java.util.LinkedList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import ch.unige.tpgcrowd.google.GooglePlayServiceCheckUtility;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;

public final class GeofenceHandler {
	private static final String HANDLER_NAME = "Geofence";
	private static GeofenceHandler ghandler;
	
	public static GeofenceHandler getGeofanceHandler() {
		if (ghandler == null) {
			ghandler = new GeofenceHandler();
		}
		return ghandler;
	}
	
	private final Handler handler;
    // Defines the allowable request types.
    public enum REQUEST_TYPE {ADD, REMOVE}
    // Flag that indicates if a request is underway.
    private boolean inProgress;
    // Holds the location client
    protected LocationClient locationClient;
    // Stores the PendingIntent used to request geofence monitoring
	private PendingIntent transitionPendingIntent;
	
	public GeofenceHandler() {
		inProgress = false;
		final HandlerThread ht = new HandlerThread(HANDLER_NAME);
		ht.start();
		handler = new Handler(ht.getLooper());
	}
	
	public void addGeofences(final FragmentActivity activity, final String[] ids) {
		handler.post(new GeofancesManager(activity, ids, REQUEST_TYPE.ADD));
	}
	
	public void removeGeofences(final FragmentActivity activity, final String[] ids) {
		handler.post(new GeofancesManager(activity, ids, REQUEST_TYPE.REMOVE));
	}
	
	private class GeofancesManager implements Runnable, 
		ConnectionCallbacks, OnConnectionFailedListener,
		OnAddGeofencesResultListener, OnRemoveGeofencesResultListener {
		private final FragmentActivity activity;
		private final List<Geofence> currentGeofences;
		private final REQUEST_TYPE requestType;
		
		public GeofancesManager(final FragmentActivity activity, final String[] ids, 
				final REQUEST_TYPE requestType) {
			this.activity = activity;
			this.requestType = requestType;
			final SimpleGeofenceStore store = new SimpleGeofenceStore(activity.getApplicationContext());
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
	        /*
	         * Test for Google Play services after setting the request type.
	         * If Google Play services isn't present, the proper request
	         * can be restarted.
	         */
	        if (!GooglePlayServiceCheckUtility.servicesConnected(activity)) {
	            return;
	        }
	        /*
	         * Create a new location client object. Since the current
	         * activity class implements ConnectionCallbacks and
	         * OnConnectionFailedListener, pass the current activity object
	         * as the listener for both parameters
	         */
	        locationClient = new LocationClient(activity.getApplicationContext(), this, this);
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
	        	locationClient.disconnect();
	        }
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
	    		break;
	    	case REMOVE:
	    		locationClient.removeGeofences(transitionPendingIntent, this);
	    		break;
	    	}
		}
	    
	    @Override
		public void onDisconnected() {
			// Turn off the request flag
	        inProgress = false;
	        // Destroy the current location client
	        locationClient = null;
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
	        disconnect();
		}
		
		@Override
		public void onRemoveGeofencesByRequestIdsResult(final int statusCode,
				final String[] geofenceRequestIds) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onRemoveGeofencesByPendingIntentResult(final int statusCode,
				final PendingIntent pendingIntent) {
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
			disconnect();
		}

		private PendingIntent getTransitionPendingIntent() {
	        // Create an explicit Intent
	        final Intent intent = new Intent(activity,
	                ReceiveTransitionsIntentService.class);
	        /*
	         * Return the PendingIntent
	         */
	        return PendingIntent.getService(
	                activity,
	                0,
	                intent,
	                PendingIntent.FLAG_UPDATE_CURRENT);
	    }
		
		private void disconnect() {
			// Turn off the request flag
	        inProgress = false;
	        //check if location client is still connected
	        if (locationClient.isConnected()) {
	        	locationClient.disconnect();
	        }
	        // Destroy the current location client
	        locationClient = null;
		}
	}
}
