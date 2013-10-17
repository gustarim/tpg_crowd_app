package ch.unige.tpgcrowd.google.activity;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionAtStopIntentService extends IntentService {
	private static final String NAME = "ActivityRecognitionIntentService";
	
	//TODO add current objectives parameter for intent so we can control which activity we are detecting
	public static PendingIntent getActivityRecognitionAtStopPI(final Context context) {
		final Intent intent = new Intent(
				context, ActivityRecognitionAtStopIntentService.class);
		
		return PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public ActivityRecognitionAtStopIntentService() {
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
			if (activityType == DetectedActivity.STILL) {
				Log.i(NAME, "Detected still with confidence" + confidence + ", start stop dialog...");
				ActivityRecognitionHandler.stopActivityRecognition(getApplicationContext(), 
						getActivityRecognitionAtStopPI(getApplicationContext()));
			}
			else {
				Log.i(NAME, "Detected other activity...");
			}
		}
	}
}
