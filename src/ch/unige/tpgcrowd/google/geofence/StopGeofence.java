package ch.unige.tpgcrowd.google.geofence;

import java.io.Serializable;

import com.google.android.gms.location.Geofence;

/**
 * A single Geofence object, defined by its center and radius.
 */
public class StopGeofence implements Serializable {

	private static final long serialVersionUID = -4561650328188941513L;
	
	public static final String STOP_GEOFENCE_ID = "StopGeofence";
	private static final float STOP_GEOFENCE_RADIUS = 15;
	private static final long STOP_GEOFENCE_EXPIRATION = 36000000;
	// Instance variables
	private final String mId;
	private final double mLatitude;
	private final double mLongitude;
	private final float mRadius;
	private final long mExpirationDuration;
	private int mTransitionType;
	private final String lineCode;
	private final String destinationCode;
	private final String physicalStopCode;
	private final String stopCode;
	private final String destinationName;
	private final int stopCrowd;

	/**
	 * @param geofenceId The Geofence's request ID
	 * @param latitude Latitude of the Geofence's center.
	 * @param longitude Longitude of the Geofence's center.
	 * @param radius Radius of the geofence circle.
	 * @param expiration Geofence expiration duration
	 * @param transition Type of Geofence transition.
	 */
	public StopGeofence(
			final double latitude,
			final double longitude,
			final int transition,
			final String lineCode,
			final String destinationCode,
			final String destinationName,
			final String physicalStopCode,
			final String stopCode,
			final int stopCrowd) {
		// Set the instance fields from the constructor
		this.mId = STOP_GEOFENCE_ID;
		this.mLatitude = latitude;
		this.mLongitude = longitude;
		this.mRadius = STOP_GEOFENCE_RADIUS;
		this.mExpirationDuration = STOP_GEOFENCE_EXPIRATION;
		this.mTransitionType = transition;
		this.lineCode = lineCode;
		this.stopCode = stopCode;
		this.physicalStopCode = physicalStopCode;
		this.destinationCode = destinationCode;
		this.destinationName = destinationName;
		this.stopCrowd = stopCrowd;
	}
	// Instance field getters
	public String getId() {
		return mId;
	}
	public double getLatitude() {
		return mLatitude;
	}
	public double getLongitude() {
		return mLongitude;
	}
	public float getRadius() {
		return mRadius;
	}
	public long getExpirationDuration() {
		return mExpirationDuration;
	}
	public int getTransitionType() {
		return mTransitionType;
	}
	
	public void setTransitionType(final int mTransitionType) {
		this.mTransitionType = mTransitionType;
	}
	
	public String getDestinationCode() {
		return destinationCode;
	}
	
	public String getDestinationName() {
		return destinationName;
	}
	
	public String getLineCode() {
		return lineCode;
	}
	
	public String getStopCode() {
		return stopCode;
	}
	
	public String getPhysicalStopCode() {
		return physicalStopCode;
	}
	
	public int getStopCrowd() {
		return stopCrowd;
	}
	
	/**
	 * Creates a Location Services Geofence object from a
	 * SimpleGeofence.
	 *
	 * @return A Geofence object
	 */
	public Geofence toGeofence() {
		// Build a new Geofence object
		return new Geofence.Builder()
		.setRequestId(getId())
		.setTransitionTypes(mTransitionType)
		.setCircularRegion(
				getLatitude(), getLongitude(), getRadius())
				.setExpirationDuration(mExpirationDuration)
				.build();
	}


}
