package ch.unige.tpgcrowd.ui;

import java.util.List;

import com.google.android.gms.location.Geofence;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.geofence.GeofenceHandler;
import ch.unige.tpgcrowd.google.geofence.StopGeofence;
import ch.unige.tpgcrowd.google.geofence.StopGeofenceStore;
import ch.unige.tpgcrowd.google.geofence.StopTransitionsIntentService;
import ch.unige.tpgcrowd.manager.ITPGStops;
import ch.unige.tpgcrowd.manager.TPGManager;
import ch.unige.tpgcrowd.model.Connection;
import ch.unige.tpgcrowd.model.Coordinates;
import ch.unige.tpgcrowd.model.PhysicalStop;
import ch.unige.tpgcrowd.model.Stop;
import ch.unige.tpgcrowd.model.StopList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;
import ch.unige.tpgcrowd.ui.fragments.CrowdStopFragment;
import ch.unige.tpgcrowd.ui.fragments.ShowNextDeparturesFragment;
import ch.unige.tpgcrowd.ui.fragments.ShowPhisicalStopsFragment;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment.PhysicalStopRender;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment.PhysicalStopSelectedListener;
import ch.unige.tpgcrowd.util.ColorStore;

public class StopNotificationView extends FragmentActivity {

	private static final String TAG = StopNotificationView.class.getSimpleName();

	private StopGeofence stop;
	private ShowNextDeparturesFragment sndf;
	private CrowdStopFragment crsf;
	private TextView wrongStopBtn; 
	
	private PhysicalStopRender render;

	private ShowPhisicalStopsFragment spsf;

	private PhysicalStopSelectedListener phyStopListener = new PhysicalStopSelectedListener() {
		
		@Override
		public boolean onPhysicalStopSelected(Stop rootStop, PhysicalStop stop,
				Connection conn) {
			final Coordinates pos = stop.getCoordinates();

			final StopGeofence sg = new StopGeofence(pos.getLatitude(), 
					pos.getLongitude(), Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT, conn.getLineCode(), conn.getDestinationCode(), conn.getDestinationName(), stop.getStopCode(), rootStop.getStopCode(), stop.getCrowd());
			StopGeofenceStore.setGeofence(StopNotificationView.this, StopGeofence.STOP_GEOFENCE_ID, sg);
			final boolean added =  GeofenceHandler.addGeofences(StopNotificationView.this, new String[] {StopGeofence.STOP_GEOFENCE_ID}, 
					StopTransitionsIntentService.getTransitionPendingIntent(StopNotificationView.this.getApplicationContext()));
			if (added) {
				Log.i(StopGeofence.STOP_GEOFENCE_ID, "Geofence added");
				//update the current view
				updateCurrent(sg);
				
				//Update the notification
				updateNotification(sg);
				spsf.dismiss();
			}
			else {
				spsf.dismiss();
				Toast.makeText(getApplicationContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
			}
			

			return added;
		}
	};
	
	private final TPGObjectListener<StopList> stopsListener = new TPGObjectListener<StopList>() {
		
		@Override
		public void onSuccess(final StopList results) {
			final List<Stop> stops = results.getStops();
			if (stops != null && !stops.isEmpty()) {
				final Stop stop = results.getStops().get(0);
				final List<PhysicalStop> phyStops = stop.getPhysicalStops();
				render.setPhysicalStops(stop, phyStops, phyStopListener);
			}
		}
		
		@Override
		public void onFailure() {
			render.showError();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		this.stop = (StopGeofence) extras.getSerializable("STOP_GEOFENCE_KEY");

		setContentView(R.layout.stop_notification_view);

		wrongStopBtn = (TextView) findViewById(R.id.wrong_stop_view);
		wrongStopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				spsf = ShowPhisicalStopsFragment.newInstance();
			    render = spsf;
				spsf.show(getSupportFragmentManager(), "dialog");
			    
				final ITPGStops phisicalStops = TPGManager.getStopsManager(StopNotificationView.this);
				phisicalStops.getPhysicalStopByCode(stop.getStopCode(), stopsListener);

			}
		});
	}

	protected void updateNotification(StopGeofence sg) {
		// TODO Auto-generated method stub
		final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());

		//Small view
		RemoteViews rv = new RemoteViews(this.getPackageName(), R.layout.notification_small_at_stop);
		rv.setInt(R.id.lineIcon, "setBackgroundColor", ColorStore.getColor(getApplicationContext(), sg.getLineCode()));

		rv.setTextViewText(R.id.lineIcon,sg.getLineCode());
		rv.setTextViewText(R.id.textDirection,sg.getDestinationName());

		notificationBuilder.setContent(rv);

		notificationBuilder.setSmallIcon(R.drawable.ic_launcher);

		/* Creates an explicit intent for an Activity in your app */
		Intent resultIntent = new Intent(getApplicationContext(), StopNotificationView.class);
		resultIntent.putExtra("STOP_GEOFENCE_KEY", sg);

		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		/* Adds the Intent that starts the Activity to the top of the stack */
		PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		notificationBuilder.setContentIntent(resultPendingIntent);

		final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notificationBuilder.build());

	}

	protected void updateCurrent(StopGeofence sg) {
		//Create the crowd fragment
		this.stop = sg;
		final Bundle bCrowd = new Bundle();

		bCrowd.putString(CrowdStopFragment.EXTRA_PHYSICAL_STOP_CODE, sg.getPhysicalStopCode());
		bCrowd.putInt(CrowdStopFragment.EXTRA_PHYSICAL_STOP_CROWD, sg.getStopCrowd());
		crsf = new CrowdStopFragment();
		crsf.setArguments(bCrowd);

		//Create the next departure fragment
		final Bundle bNextDep = new Bundle();

		bNextDep.putString(ShowNextDeparturesFragment.EXTRA_STOP_CODE, sg.getStopCode());
		bNextDep.putString(ShowNextDeparturesFragment.EXTRA_LINE_CODE, sg.getLineCode());
		bNextDep.putString(ShowNextDeparturesFragment.EXTRA_DEST_CODE, sg.getDestinationCode());
		bNextDep.putString(ShowNextDeparturesFragment.EXTRA_DEST_NAME, sg.getDestinationName());
		sndf = new ShowNextDeparturesFragment();
		sndf.setArguments(bNextDep);

		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();

		ft.replace(R.id.crowdFragment, crsf, CrowdStopFragment.class.getSimpleName());
		ft.replace(R.id.nextDepFragment, sndf, ShowNextDeparturesFragment.class.getSimpleName());
		ft.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();

		updateCurrent(stop);
	}
}
