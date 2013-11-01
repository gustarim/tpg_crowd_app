package ch.unige.tpgcrowd.ui;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.widget.RemoteViews;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.GooglePlayServiceCheckUtility;
import ch.unige.tpgcrowd.google.activity.VehicleLeavingStopIntentService;
import ch.unige.tpgcrowd.google.geofence.StopGeofence;
import ch.unige.tpgcrowd.google.geofence.StopGeofenceStore;
import ch.unige.tpgcrowd.model.Stop;
import ch.unige.tpgcrowd.ui.fragments.InitialMapFragment;
import ch.unige.tpgcrowd.ui.fragments.InitialMapFragment.MapEventListener;
import ch.unige.tpgcrowd.ui.fragments.ShowLinesMapFragment.OnLineMapLongClickListener;
import ch.unige.tpgcrowd.ui.fragments.ShowNearbyStopsFragment;
import ch.unige.tpgcrowd.ui.fragments.ShowNearbyStopsFragment.StopRender;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment;
import ch.unige.tpgcrowd.util.ColorStore;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity implements StopRender, MapEventListener, OnLineMapLongClickListener {
	private ShowStopFragment spsf;
	private ShowNearbyStopsFragment nearbyFragment;
	private InitialMapFragment map;

	private boolean init = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ColorStore.updateColors(this);
		setContentView(R.layout.activity_main);
		//if this call return false we must stop all the rest..
		GooglePlayServiceCheckUtility.servicesConnected(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (nearbyFragment == null) {
			final FragmentManager fm = getSupportFragmentManager();
			final FragmentTransaction ft = fm.beginTransaction();
			nearbyFragment = new ShowNearbyStopsFragment();
			
			ft.add(R.id.main, nearbyFragment, ShowStopFragment.TAG);		

			map = new InitialMapFragment();

			ft.add(R.id.main, map, InitialMapFragment.class.getSimpleName());
			
			ft.commit();
		}

		final StopGeofence geofence = StopGeofenceStore.getGeofence(getApplicationContext(), StopGeofence.STOP_GEOFENCE_ID);
		final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
		//Small view
		final RemoteViews rv = new RemoteViews(getPackageName(), R.layout.notification_small_in_vehicle);
		rv.setInt(R.id.lineIcon, "setBackgroundColor", ColorStore.getColor(getApplicationContext(), geofence.getLineCode()));

		rv.setTextViewText(R.id.lineIcon, geofence.getLineCode());
		rv.setTextViewText(R.id.textDirection, geofence.getDestinationName());

		notificationBuilder.setContent(rv);

		notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
		
		/* Creates an explicit intent for an Activity in your app */
		final Intent resultIntent = new Intent(getApplicationContext(), VehicleNotificationView.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		/* Adds the Intent that starts the Activity to the top of the stack */
		final PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		notificationBuilder.setContentIntent(resultPendingIntent);
		
		final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(VehicleLeavingStopIntentService.TPG_NOTIFICATION, notificationBuilder.build());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		switch (requestCode) {
		case GooglePlayServiceCheckUtility.CONNECTION_FAILURE_RESOLUTION_REQUEST :
			/*
			 * If the result code is Activity.RESULT_OK, try
			 * to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK :
				/*
				 * Try the request again
				 */
				GooglePlayServiceCheckUtility.servicesConnected(this);
				break;
			}
		}
	}

	@Override
	public void onStopSelected(Stop stop) {

		if (stop != null) {
			
			final Bundle b = new Bundle();
			b.putString(ShowStopFragment.EXTRA_STOP_NAME, stop.getStopName());
			b.putString(ShowStopFragment.EXTRA_STOP_CODE, stop.getStopCode());
			
			if (spsf == null) {
				final FragmentManager fm = getSupportFragmentManager();
				final FragmentTransaction ft = fm.beginTransaction();

				spsf = new ShowStopFragment();
				spsf.setArguments(b);

				ft.hide(map);
				ft.add(R.id.main, spsf, ShowStopFragment.TAG);

				ft.commit();
				
				init = true;
			}
			else {
				if (init) {
					spsf.updateContent(b);
				}
				else {
					final FragmentManager fm = getSupportFragmentManager();
					final FragmentTransaction ft = fm.beginTransaction();

					ft.hide(map);
					ft.show(spsf);
					ft.commit();
					
					spsf.updateContent(b);
					init = true;
				}
			}
		}
		else {
			final FragmentManager fm = getSupportFragmentManager();
			final FragmentTransaction ft = fm.beginTransaction();
			
			ft.hide(spsf);
			ft.show(map);
			ft.commit();
			
			init = false;
		}
	}

	@Override
	public void setMapLocation(Location loc) {
		double accuracy = -1;
		if (loc.hasAccuracy()){
			accuracy = loc.getAccuracy();
		}
		map.setLocation(loc.getLatitude(), loc.getLongitude(), accuracy);
	}

	@Override
	public void onLongClick(double latitude, double longitude) {
		nearbyFragment.setUserLocation(latitude, longitude);
		if(spsf!=null){
			spsf.setRefMarker(latitude, longitude);
		}
	
	}

	@Override
	public void onLineMapLongClick(float zoom, LatLng currentCenter,
			LatLng pressPosition) {
		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		
		ft.hide(spsf);
		ft.show(map);
		ft.commit();
		
		init = false;
		map.setLocationWithMarker(currentCenter, pressPosition, zoom);
		nearbyFragment.setUserLocation(pressPosition.latitude, pressPosition.longitude);
		spsf.setRefMarker(pressPosition.latitude, pressPosition.longitude);
	}

	@Override
	public void setSystemLocation(Location loc) {
		map.setSystemLocation(loc);
		if(spsf!=null){
			spsf.setSystemLocation(loc);
		}	
	}

	

}
