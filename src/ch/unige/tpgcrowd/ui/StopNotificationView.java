package ch.unige.tpgcrowd.ui;

import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.geofence.StopGeofence;
import ch.unige.tpgcrowd.ui.fragments.CrowdStopFragment;
import ch.unige.tpgcrowd.ui.fragments.ShowNextDeparturesFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class StopNotificationView extends FragmentActivity {

	private StopGeofence stop;
	private ShowNextDeparturesFragment sndf;
	private CrowdStopFragment crsf; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		this.stop = (StopGeofence) extras.getSerializable("STOP_GEOFENCE_KEY");

		setContentView(R.layout.stop_notification_view);
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
