package ch.unige.tpgcrowd.google.geofence;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import ch.unige.tpgcrowd.google.GooglePlayServiceCheckUtility;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;

public final class GeofenceHandler implements ConnectionCallbacks, OnConnectionFailedListener,
OnAddGeofencesResultListener, OnRemoveGeofencesResultListener {
	// Defines the allowable request types.
	private enum REQUEST_TYPE {ADD, REMOVE_PENDING, REMOVE_IDS}
	// Flag that indicates if a request is underway.
	private boolean inProgress;
	// Holds the location client
	private LocationClient locationClient;
	// Stores the PendingIntent used to request geofence monitoring
	private final PendingIntent transitionPendingIntent;
	//private FragmentActivity activity;
	private final List<Geofence> currentGeofences;
	private final String[] ids;
	private final REQUEST_TYPE requestType;

	private GeofenceHandler(final PendingIntent transitionPendingIntent,
			final String[] ids, final REQUEST_TYPE requestType) {
		this.transitionPendingIntent = transitionPendingIntent;
		this.ids = ids;
		this.requestType = requestType;
		currentGeofences = new LinkedList<Geofence>();
		inProgress = false;
	}

	public static boolean addGeofences(final Context context, final String[] ids,
			final PendingIntent transitionPendingIntent) {
		final GeofenceHandler gh = new GeofenceHandler(transitionPendingIntent, ids, REQUEST_TYPE.ADD);
		return gh.connectLocationService(context);
	}

	public boolean removeGeofencesByPendingIntent(final Context context,
			final PendingIntent transitionPendingIntent) {
		final GeofenceHandler gh = new GeofenceHandler(transitionPendingIntent, new String[] {}, REQUEST_TYPE.REMOVE_PENDING);
		return gh.connectLocationService(context);
	}
	
	public boolean removeGeofencesByIds(final Context context, final String[] ids,
			final PendingIntent transitionPendingIntent) {
		final GeofenceHandler gh = new GeofenceHandler(transitionPendingIntent, ids, REQUEST_TYPE.REMOVE_IDS);
		return gh.connectLocationService(context);
	}

	/**
	 * Start a request for geofence monitoring by calling
	 * LocationClient.connect().
	 */
	private boolean connectLocationService(final Context context) {
		boolean result = false;
		for (int i = 0; i < ids.length; i++) {
			currentGeofences.add(SimpleGeofenceStore.getGeofence(context, ids[i]).toGeofence());
		}
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
	public void onConnected(final Bundle connectionHint) {
		switch (requestType) {
		case ADD :
			// Get the PendingIntent for the request
			//transitionPendingIntent = getTransitionPendingIntent();
			// Send a request to add the current geofences
			locationClient.addGeofences(
					currentGeofences, transitionPendingIntent, this);
			break;
		case REMOVE_PENDING:
			locationClient.removeGeofences(transitionPendingIntent, this);
			break;
		case REMOVE_IDS:
			locationClient.removeGeofences(Arrays.asList(ids), this);
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
			GooglePlayServiceCheckUtility.tryResolutionOnConnectionError(connectionResult);
			//	        // If no resolution is available, display an error dialog
		} else {
			//	            // Get the error code
			final int errorCode = connectionResult.getErrorCode();
			GooglePlayServiceCheckUtility.generateErrorDialog(errorCode);
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
