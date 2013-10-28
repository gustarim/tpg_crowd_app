package ch.unige.tpgcrowd.google.geofence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Storage for geofence values, implemented in SharedPreferences.
 */
public class StopGeofenceStore {

	// Keys for flattened geofences stored in SharedPreferences
	public static final String KEY_LATITUDE =
			"ch.unige.tpgcrowd.geofence.KEY_LATITUDE";
	public static final String KEY_LONGITUDE =
			"ch.unige.tpgcrowd.geofence.KEY_LONGITUDE";
	public static final String KEY_RADIUS =
			"ch.unige.tpgcrowd.geofence.KEY_RADIUS";
	public static final String KEY_EXPIRATION_DURATION =
			"ch.unige.tpgcrowd.geofence.KEY_EXPIRATION_DURATION";
	public static final String KEY_TRANSITION_TYPE =
			"ch.unige.tpgcrowd.geofence.KEY_TRANSITION_TYPE";
	public static final String KEY_DESTINATION_CODE = 
			"ch.unige.tpgcrowd.geofence.KEY_DIRECTION_CODE";
	public static final String KEY_STOP_CODE = 
			"ch.unige.tpgcrowd.geofence.KEY_STOP_CODE";
	public static final String KEY_LINE_CODE = 
			"ch.unige.tpgcrowd.geofence.KEY_LINE_CODE";
	// The prefix for flattened geofence keys
	public static final String KEY_PREFIX =
			"ch.unige.tpgcrowd.geofence.KEY";
	/*
	 * Invalid values, used to test geofence storage when
	 * retrieving geofences
	 */
	public static final long INVALID_LONG_VALUE = -999l;
	public static final float INVALID_FLOAT_VALUE = -999.0f;
	public static final int INVALID_INT_VALUE = -999;
	public static final String INVALID_CODE = "invalid";
	// The SharedPreferences object in which geofences are stored
	private final SharedPreferences mPrefs;
	// The name of the SharedPreferences
	private static final String SHARED_PREFERENCES =
			"SharedPreferences";
	
	public static void setGeofence(final Context context, final String id, 
			final StopGeofence geofence) {
		final StopGeofenceStore store = new StopGeofenceStore(context);
		store.setGeofence(id, geofence);
	}
	
	public static StopGeofence getGeofence(final Context context, final String id) {
		final StopGeofenceStore store = new StopGeofenceStore(context);
		return store.getGeofence(id);
	}
			
	public static void clearGeofence(final Context context, final String id) {
		final StopGeofenceStore store = new StopGeofenceStore(context);
		store.clearGeofence(id);
	}
	
	// Create the SharedPreferences storage with private access only
	private StopGeofenceStore(final Context context) {
		mPrefs =
				context.getSharedPreferences(
						SHARED_PREFERENCES,
						Context.MODE_PRIVATE);
	}
	
	/**
	 * Returns a stored geofence by its id, or returns null
	 * if it's not found.
	 *
	 * @param id The ID of a stored geofence
	 * @return A geofence defined by its center and radius. See
	 */
	private StopGeofence getGeofence(final String id) {
		/*
		 * Get the latitude for the geofence identified by id, or
		 * INVALID_FLOAT_VALUE if it doesn't exist
		 */
		final double lat = mPrefs.getFloat(
				getGeofenceFieldKey(id, KEY_LATITUDE),
				INVALID_FLOAT_VALUE);
		/*
		 * Get the longitude for the geofence identified by id, or
		 * INVALID_FLOAT_VALUE if it doesn't exist
		 */
		final double lng = mPrefs.getFloat(
				getGeofenceFieldKey(id, KEY_LONGITUDE),
				INVALID_FLOAT_VALUE);
		/*
		 * Get the radius for the geofence identified by id, or
		 * INVALID_FLOAT_VALUE if it doesn't exist
		 */
		final float radius = mPrefs.getFloat(
				getGeofenceFieldKey(id, KEY_RADIUS),
				INVALID_FLOAT_VALUE);
		/*
		 * Get the expiration duration for the geofence identified
		 * by id, or INVALID_LONG_VALUE if it doesn't exist
		 */
		final long expirationDuration = mPrefs.getLong(
				getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
				INVALID_LONG_VALUE);
		/*
		 * Get the transition type for the geofence identified by
		 * id, or INVALID_INT_VALUE if it doesn't exist
		 */
		final int transitionType = mPrefs.getInt(
				getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
				INVALID_INT_VALUE);
		
		final String lineCode = mPrefs.getString(getGeofenceFieldKey(id, KEY_LINE_CODE), INVALID_CODE);
		final String directionCode = mPrefs.getString(getGeofenceFieldKey(id, KEY_DESTINATION_CODE), INVALID_CODE);
		final String stopCode = mPrefs.getString(getGeofenceFieldKey(id, KEY_STOP_CODE), INVALID_CODE);
		// If none of the values is incorrect, return the object
		if (
				lat != INVALID_FLOAT_VALUE &&
				lng != INVALID_FLOAT_VALUE &&
				radius != INVALID_FLOAT_VALUE &&
				expirationDuration !=
				INVALID_LONG_VALUE &&
				transitionType != INVALID_INT_VALUE &&
				!lineCode.equals(INVALID_CODE) &&
				!directionCode.equals(INVALID_CODE) &&
				!stopCode.equals(INVALID_CODE)) {

			// Return a true Geofence object
			return new StopGeofence(lat, lng,
					transitionType, lineCode, directionCode, stopCode);
			// Otherwise, return null.
		} else {
			return null;
		}
	}
	
	/**
	 * Save a geofence.
	 * @param geofence The SimpleGeofence containing the
	 * values you want to save in SharedPreferences
	 */
	private void setGeofence(final String id, final StopGeofence geofence) {
		/*
		 * Get a SharedPreferences editor instance. Among other
		 * things, SharedPreferences ensures that updates are atomic
		 * and non-concurrent
		 */
		final Editor editor = mPrefs.edit();
		// Write the Geofence values to SharedPreferences
		editor.putFloat(
				getGeofenceFieldKey(id, KEY_LATITUDE),
				(float) geofence.getLatitude());
		editor.putFloat(
				getGeofenceFieldKey(id, KEY_LONGITUDE),
				(float) geofence.getLongitude());
		editor.putFloat(
				getGeofenceFieldKey(id, KEY_RADIUS),
				geofence.getRadius());
		editor.putLong(
				getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
				geofence.getExpirationDuration());
		editor.putInt(
				getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
				geofence.getTransitionType());
		editor.putString(
				getGeofenceFieldKey(id, KEY_LINE_CODE),
				geofence.getLineCode());
		editor.putString(
				getGeofenceFieldKey(id, KEY_DESTINATION_CODE),
				geofence.getDestinationCode());
		editor.putString(
				getGeofenceFieldKey(id, KEY_STOP_CODE),
				geofence.getStopCode());
		// Commit the changes
		editor.commit();
	}
	
	private void clearGeofence(final String id) {
		/*
		 * Remove a flattened geofence object from storage by
		 * removing all of its keys
		 */
		final Editor editor = mPrefs.edit();
		editor.remove(getGeofenceFieldKey(id, KEY_LATITUDE));
		editor.remove(getGeofenceFieldKey(id, KEY_LONGITUDE));
		editor.remove(getGeofenceFieldKey(id, KEY_RADIUS));
		editor.remove(getGeofenceFieldKey(id,
				KEY_EXPIRATION_DURATION));
		editor.remove(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE));
		editor.remove(getGeofenceFieldKey(id, KEY_LINE_CODE));
		editor.remove(getGeofenceFieldKey(id, KEY_DESTINATION_CODE));
		editor.remove(getGeofenceFieldKey(id, KEY_STOP_CODE));
		editor.commit();
	}
	
	/**
	 * Given a Geofence object's ID and the name of a field
	 * (for example, KEY_LATITUDE), return the key name of the
	 * object's values in SharedPreferences.
	 *
	 * @param id The ID of a Geofence object
	 * @param fieldName The field represented by the key
	 * @return The full key name of a value in SharedPreferences
	 */
	private String getGeofenceFieldKey(final String id,
			final String fieldName) {
		return KEY_PREFIX + "_" + id + "_" + fieldName;
	}
}
