package ch.unige.tpgcrowd.google.activity;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.geofence.StopGeofence;
import ch.unige.tpgcrowd.google.geofence.StopGeofenceStore;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class VehicleLeavingStopIntentService extends IntentService {
	private static final String NAME = "VehicleLeavingStopIntentService";
	private static final int TPG_NOTIFICATION = 1;

	public static PendingIntent getLeavingStopVehicle(final Context context) {
		final Intent intent = new Intent(
				context, VehicleLeavingStopIntentService.class);
		return PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public VehicleLeavingStopIntentService() {
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
				if (activityType == DetectedActivity.IN_VEHICLE) {
					Log.i(NAME, "Detected in vehicle with confidence" + confidence + ", start in bus/tram dialog...");
					
					ActivityRecognitionHandler.stopActivityRecognition(getApplicationContext(), 
							getLeavingStopVehicle(getApplicationContext()));
					
					final StopGeofence geofence = StopGeofenceStore.getGeofence(getApplicationContext(), StopGeofence.STOP_GEOFENCE_ID);
					final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
					notificationBuilder.setContentTitle("Left the stop " + geofence.getStopCode());
					notificationBuilder.setContentText("On " + geofence.getLineCode() + " towards " + geofence.getDestinationCode());
					notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
					final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					notificationManager.notify(TPG_NOTIFICATION, notificationBuilder.build());
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
