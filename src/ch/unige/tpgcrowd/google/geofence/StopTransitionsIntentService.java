package ch.unige.tpgcrowd.google.geofence;

import java.util.List;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import ch.unige.tpgcrowd.google.activity.ActivityRecognitionHandler;
import ch.unige.tpgcrowd.google.activity.StillAtStopIntentService;
import ch.unige.tpgcrowd.google.activity.VehicleLeavingStopIntentService;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

public class StopTransitionsIntentService extends IntentService {
	private static final String NAME = "ReceiveTransitionsIntentService";
	
	public static PendingIntent getTransitionPendingIntent(final Context context) {
		// Create an explicit Intent
		final Intent intent = new Intent(context,
				StopTransitionsIntentService.class);
		/*
		 * Return the PendingIntent
		 */
		return PendingIntent.getService(
				context,
				0,
				intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public StopTransitionsIntentService() {
		super(NAME);
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		// First check for errors
		if (LocationClient.hasError(intent)) {
			// Get the error code with a static method
			int errorCode = LocationClient.getErrorCode(intent);
			// Log the error
			Log.e(NAME,
					"Location Services error: " +
							Integer.toString(errorCode));
			/*
			 * You can also send the error code to an Activity or
			 * Fragment with a broadcast Intent
			 */
			/*
			 * If there's no error, get the transition type and the IDs
			 * of the geofence or geofences that triggered the transition
			 */
		} else {
			// Get the type of transition (entry or exit)
			final int transitionType =
					LocationClient.getGeofenceTransition(intent);
			// Test that a valid transition was reported
			if ((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) ||
					(transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)) {
				final List<Geofence> triggerList =
						LocationClient.getTriggeringGeofences(intent);

				final String[] triggerIds = new String[triggerList.size()];
				
				if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
					for (int i = 0; i < triggerIds.length; i++) {
						// Store the Id of each geofence
						triggerIds[i] = triggerList.get(i).getRequestId();
						Log.i(NAME, "Goefence Triggered enter " + triggerIds[i]);
					}
					//start activity recognition for still
					final PendingIntent pi = StillAtStopIntentService.getAtStopStill(getApplicationContext());
					ActivityRecognitionHandler.startActivityRecognition(getApplicationContext(), pi);
				}
				else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
					for (int i = 0; i < triggerIds.length; i++) {
						// Store the Id of each geofence
						triggerIds[i] = triggerList.get(i).getRequestId();
						Log.i(NAME, "Goefence Triggered exit " + triggerIds[i]);
					}
					//start activity recognition for still
					final PendingIntent pi = VehicleLeavingStopIntentService.getLeavingStopVehicle(getApplicationContext());
					ActivityRecognitionHandler.startActivityRecognition(getApplicationContext(), pi);
					
					GeofenceHandler.removeGeofencesByIds(getApplicationContext(), triggerIds, getTransitionPendingIntent(getApplicationContext()));
				}
				
				/*
				 * At this point, you can store the IDs for further use
				 * display them, or display the details associated with
				 * them.
				 */
				// An invalid transition was reported
			} else {
				Log.e(NAME,
						"Geofence transition error: " +
								Integer.toString(transitionType));
			}
		}
	}
	
	@Override
	public IBinder onBind(final Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
