package ch.unige.tpgcrowd.ui.fragments;

import java.util.LinkedList;
import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
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
import ch.unige.tpgcrowd.ui.StopNotificationView;
import ch.unige.tpgcrowd.ui.fragments.ShowLinesFragment.OnLinesMapClickListener;
import ch.unige.tpgcrowd.ui.fragments.ShowLinesMapFragment.OnLinesClickListener;
import ch.unige.tpgcrowd.ui.fragments.ShowPhisicalStopsFragment.OnConnectionClickListener;
import ch.unige.tpgcrowd.util.ColorStore;

import com.google.android.gms.location.Geofence;

public class ShowStopFragment extends Fragment 
	implements OnLinesMapClickListener, OnLinesClickListener, OnConnectionClickListener {
	public static final String TAG = "stop";
	public static final String EXTRA_STOP_CODE = "ch.unige.tpgcrowd.extra.STOP_CODE";
	public static final String EXTRA_STOP_NAME = "ch.unige.tpgcrowd.extra.STOP_NAME";
	private static final String FRAGMENT_LINE_MAP = "linemap";
	private ShowLinesFragment slf;
	private ShowLinesMapFragment slmf;
	private ShowNextDeparturesFragment sndf; 
	private LinkedList<PhysicalStopRender> renders;
	public interface PhysicalStopRender {
		public void setAsReloading();
		public void setPhysicalStops(final Stop rootStop, final List<PhysicalStop> stops, PhysicalStopSelectedListener listener);
		public void showError();
	}
	
	public interface PhysicalStopSelectedListener {
		public boolean onPhysicalStopSelected(Stop rootStop, PhysicalStop stop, Connection conn);
	}
	
	private final PhysicalStopSelectedListener phySelectedListener = new PhysicalStopSelectedListener() {
		
		@Override
		public boolean onPhysicalStopSelected(Stop rootStop, PhysicalStop stop,
				Connection conn) {

			ShowStopFragment.this.onConnectionClick(conn);

			final Coordinates pos = stop.getCoordinates();

			final StopGeofence sg = new StopGeofence(pos.getLatitude(), 
					pos.getLongitude(), Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT, conn.getLineCode(), conn.getDestinationCode(), conn.getDestinationName(), stop.getStopCode(), rootStop.getStopCode(), stop.getCrowd());
			StopGeofenceStore.setGeofence(getActivity(), StopGeofence.STOP_GEOFENCE_ID, sg);
			final boolean added =  GeofenceHandler.addGeofences(getActivity(), new String[] {StopGeofence.STOP_GEOFENCE_ID}, 
					StopTransitionsIntentService.getTransitionPendingIntent(getActivity().getApplicationContext()));
			if (added) {
				Log.i(StopGeofence.STOP_GEOFENCE_ID, "Geofence added");
			}
			
			//displayNotif(sg);
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
				for (final PhysicalStopRender rend : renders) {
					rend.setPhysicalStops(stop, phyStops, phySelectedListener);
				}
			}
		}
		
		@Override
		public void onFailure() {
			for (final PhysicalStopRender rend : renders) {
				rend.showError();
			}
		}
	};
	private TextView name;
	private String stopCode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View layout = inflater.inflate(R.layout.show_stop, container, false);
		name = (TextView)layout.findViewById(R.id.stop);
		
		final FragmentManager fm = getFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		slf = new ShowLinesFragment();
		ft.add(R.id.lineMapFragment, slf, ShowLinesFragment.class.getSimpleName());
		slmf = new ShowLinesMapFragment();
		ft.add(R.id.lineMapFragment, slmf, ShowLinesMapFragment.class.getSimpleName());
		ft.hide(slmf);
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//		ft.add(R.id.lineMapFragment, slf, FRAGMENT_LINE_MAP);
		ft.commit();
		renders = new LinkedList<ShowStopFragment.PhysicalStopRender>();
		renders.add(slf);
		renders.add(slmf);
		
		final Bundle b = getArguments();
		updateContent(b);
		
		return layout;
	}
	
	public void updateContent(Bundle b) {
		removeConnectionFragment();
		name.setText(b.getString(EXTRA_STOP_NAME));
		for (final PhysicalStopRender rend : renders) {
			rend.setAsReloading();
		}
		
		stopCode = b.getString(EXTRA_STOP_CODE);
		final ITPGStops phisicalStops = TPGManager.getStopsManager(getActivity());
		phisicalStops.getPhysicalStopByCode(stopCode, stopsListener);
	}
	

	@Override
	public void onMapClick() {
		final FragmentManager fm = getFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		ft.hide(slf);
		ft.show(slmf);
		ft.addToBackStack(FRAGMENT_LINE_MAP);
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}

	@Override
	public void onLinesClick() {
		final FragmentManager fm = getFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		ft.hide(slmf);
		ft.show(slf);
//		ft.addToBackStack(FRAGMENT_LINE_MAP);
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}

	@Override
	public void onConnectionClick(final Connection c) {
		final Bundle b = new Bundle();
		b.putString(ShowNextDeparturesFragment.EXTRA_STOP_CODE, stopCode);
		b.putString(ShowNextDeparturesFragment.EXTRA_LINE_CODE, c.getLineCode());
		b.putString(ShowNextDeparturesFragment.EXTRA_DEST_CODE, c.getDestinationCode());
		b.putString(ShowNextDeparturesFragment.EXTRA_DEST_NAME, c.getDestinationName());
		sndf = new ShowNextDeparturesFragment();
		sndf.setArguments(b);
		final FragmentManager fm = getFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.lineMapFragment, sndf, ShowNextDeparturesFragment.class.getSimpleName());
		ft.hide(slf);
		ft.addToBackStack(FRAGMENT_LINE_MAP);
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}

	public void setRefMarker(double latitude, double longitude) {
		slmf.setRefMarker(latitude, longitude);
		
	}

	public void setSystemLocation(Location loc) {
		slmf.setSystemLocation(loc);
		
	}
	
	private void removeConnectionFragment() {
		if (sndf != null) {
			final FragmentManager fm = getFragmentManager();
			final FragmentTransaction ft = fm.beginTransaction();
			ft.remove(sndf);
			ft.show(slf);
//			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		}
	}
	
	
	private void displayNotif(StopGeofence sg) {

		//	TEST METHOD - TO REMOVE
		//
		////// 
		final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext());

		//Small view
		RemoteViews rv = new RemoteViews(getActivity().getPackageName(), R.layout.notification_small_at_stop);
		rv.setInt(R.id.lineIcon, "setBackgroundColor", ColorStore.getColor(getActivity(), sg.getLineCode()));

		rv.setTextViewText(R.id.lineIcon,sg.getLineCode());
		rv.setTextViewText(R.id.textDirection,sg.getDestinationName());

		notificationBuilder.setContent(rv);

		notificationBuilder.setSmallIcon(R.drawable.ic_launcher);

		/* Creates an explicit intent for an Activity in your app */
		Intent resultIntent = new Intent(getActivity(), StopNotificationView.class);
		resultIntent.putExtra("STOP_GEOFENCE_KEY", sg);

		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		/* Adds the Intent that starts the Activity to the top of the stack */
		PendingIntent resultPendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		notificationBuilder.setContentIntent(resultPendingIntent);

		final NotificationManager notificationManager = (NotificationManager) getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notificationBuilder.build());


	}
}
