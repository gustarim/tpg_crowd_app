package ch.unige.tpgcrowd.google.activity;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionIntentService extends IntentService {
	private static final String NAME = "ActivityRecognitionIntentService";

	public ActivityRecognitionIntentService() {
		super(NAME);
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		// If the incoming intent contains an update
		if (ActivityRecognitionResult.hasResult(intent)) {
			// Get the update
			ActivityRecognitionResult result =
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
		}
	}
}
