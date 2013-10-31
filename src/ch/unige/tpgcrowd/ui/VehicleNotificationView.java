package ch.unige.tpgcrowd.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.geofence.StopGeofence;
import ch.unige.tpgcrowd.google.geofence.StopGeofenceStore;
import ch.unige.tpgcrowd.ui.fragments.CrowdStopFragment;
import ch.unige.tpgcrowd.ui.fragments.CrowdVehicleFragment;

public class VehicleNotificationView extends FragmentActivity {

	private StopGeofence stop;
	private CrowdVehicleFragment crvf; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.stop = StopGeofenceStore.getGeofence(this, StopGeofence.STOP_GEOFENCE_ID);

		setContentView(R.layout.vehicle_notification_view);
	}

	@Override
	protected void onResume() {
		super.onResume();

		//Create the crowd fragment
		final Bundle bCrowd = new Bundle();

//		bCrowd.putString(CrowdStopFragment.EXTRA_PHYSICAL_STOP_CODE, stop.getPhysicalStopCode());
		bCrowd.putInt(CrowdVehicleFragment.EXTRA_VEHICLE_CROWD, stop.getStopCrowd());
		crvf = new CrowdVehicleFragment();
		crvf.setArguments(bCrowd);

		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();

		ft.replace(R.id.crowdFragment, crvf, CrowdStopFragment.class.getSimpleName());
//		ft.replace(R.id.nextStopsFragment, sndf, ShowNextDeparturesFragment.class.getSimpleName());
		ft.commit();
	}
}
