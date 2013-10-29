package ch.unige.tpgcrowd.google.activity;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.geofence.GeofenceHandler;
import ch.unige.tpgcrowd.google.geofence.StopGeofence;
import ch.unige.tpgcrowd.google.geofence.StopGeofenceStore;
import ch.unige.tpgcrowd.google.geofence.StopTransitionsIntentService;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;

public class StillAtStopIntentService extends IntentService {
	private static final String NAME = "StillAtStopIntentService";
	private static final int TPG_NOTIFICATION = 0;

	public static PendingIntent getAtStopStill(final Context context) {
		final Intent intent = new Intent(
				context, StillAtStopIntentService.class);
		return PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public StillAtStopIntentService() {
		super(NAME);
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		// If the incoming intent contains an update
		if (ActivityRecognitionResult.hasResult(intent)) {
			// Get the update
			final ActivityRecognitionResult result =
					ActivityRecognitionResult.extractResult(intent);
			// Get the most probable activity
			final DetectedActivity mostProbableActivity =
					result.getMostProbableActivity();
			/*
			 * Get the probability that this activity is the
			 * the user's actual activity
			 */
			final int confidence = mostProbableActivity.getConfidence();
			/*
			 * Get an integer describing the type of activity
			 */
			final int activityType = mostProbableActivity.getType();
			//String activityName = getNameFromType(activityType);
			/*
			 * At this point, you have retrieved all the information
			 * for the current update. You can display this
			 * information to the user in a notification, or
			 * send it to an Activity or Service in a broadcast
			 * Intent.
			 */
			if (confidence > 50) {
				if (activityType == DetectedActivity.STILL) {
					Log.i(NAME, "Detected still with confidence" + confidence + ", start stop dialog...");

					ActivityRecognitionHandler.stopActivityRecognition(getApplicationContext(), 
							getAtStopStill(getApplicationContext()));
					
					final StopGeofence geofence = StopGeofenceStore.getGeofence(getApplicationContext(), StopGeofence.STOP_GEOFENCE_ID);
					final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
					notificationBuilder.setContentTitle("Waiting at the stop " + geofence.getStopCode());
					notificationBuilder.setContentText("Waiting for " + geofence.getLineCode() + " towards " + geofence.getDestinationCode());
					notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
					final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					notificationManager.notify(TPG_NOTIFICATION, notificationBuilder.build());
					
					geofence.setTransitionType(Geofence.GEOFENCE_TRANSITION_EXIT);
					StopGeofenceStore.setGeofence(getApplicationContext(), StopGeofence.STOP_GEOFENCE_ID, geofence);
					GeofenceHandler.addGeofences(getApplicationContext(), new String[] {StopGeofence.STOP_GEOFENCE_ID}, 
							StopTransitionsIntentService.getTransitionPendingIntent(getApplicationContext()));
				}
				else {
					Log.i(NAME, "Detected other activity...");
				}
			}
			else {
				Log.i(NAME, "Not enough confidence...");
			}

		}
		else {
			Log.i(NAME, "No result for current activity..");
		}
	}
}
