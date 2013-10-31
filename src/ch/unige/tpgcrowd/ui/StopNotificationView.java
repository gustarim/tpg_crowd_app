package ch.unige.tpgcrowd.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.geofence.StopGeofence;
import ch.unige.tpgcrowd.manager.ITPGStops;
import ch.unige.tpgcrowd.manager.TPGManager;
import ch.unige.tpgcrowd.model.Connection;
import ch.unige.tpgcrowd.model.PhysicalStop;
import ch.unige.tpgcrowd.model.Stop;
import ch.unige.tpgcrowd.model.StopList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;
import ch.unige.tpgcrowd.ui.fragments.CrowdStopFragment;
import ch.unige.tpgcrowd.ui.fragments.ShowNextDeparturesFragment;
import ch.unige.tpgcrowd.ui.fragments.ShowPhisicalStopsFragment;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment.PhysicalStopRender;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment.PhysicalStopSelectedListener;

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
			spsf.dismiss();
			return true;
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

	@Override
	protected void onResume() {
		super.onResume();

		//Create the crowd fragment
		final Bundle bCrowd = new Bundle();

		bCrowd.putString(CrowdStopFragment.EXTRA_PHYSICAL_STOP_CODE, stop.getPhysicalStopCode());
		bCrowd.putInt(CrowdStopFragment.EXTRA_PHYSICAL_STOP_CROWD, stop.getStopCrowd());
		crsf = new CrowdStopFragment();
		crsf.setArguments(bCrowd);

		//Create the next departure fragment
		final Bundle bNextDep = new Bundle();

		bNextDep.putString(ShowNextDeparturesFragment.EXTRA_STOP_CODE, stop.getStopCode());
		bNextDep.putString(ShowNextDeparturesFragment.EXTRA_LINE_CODE, stop.getLineCode());
		bNextDep.putString(ShowNextDeparturesFragment.EXTRA_DEST_CODE, stop.getDestinationCode());
		bNextDep.putString(ShowNextDeparturesFragment.EXTRA_DEST_NAME, stop.getDestinationName());
		sndf = new ShowNextDeparturesFragment();
		sndf.setArguments(bNextDep);

		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();

		ft.replace(R.id.crowdFragment, crsf, CrowdStopFragment.class.getSimpleName());
		ft.replace(R.id.nextDepFragment, sndf, ShowNextDeparturesFragment.class.getSimpleName());
		ft.commit();
	}
}
