package ch.unige.tpgcrowd.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.geofence.StopGeofence;
import ch.unige.tpgcrowd.google.geofence.StopGeofenceStore;
import ch.unige.tpgcrowd.ui.fragments.CrowdStopFragment;
import ch.unige.tpgcrowd.ui.fragments.CrowdVehicleFragment;
import ch.unige.tpgcrowd.ui.fragments.ShowNextStopsFragment;

public class VehicleNotificationView extends FragmentActivity {

	private StopGeofence stop;
	private CrowdVehicleFragment crvf;
	private ShowNextStopsFragment snsf;
	private TextView wrong;
	protected String DIALOG = "dialog";
	
	private final OnClickListener wrongClick = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			final FragmentManager fm = getSupportFragmentManager();
			if (snsf != null) {
				new WrongVehicleDialogFragment().show(fm, DIALOG);
			}
			else {
				new NoDataDialogFragment().show(fm, DIALOG );
			}
		}
	};
	
	public static class WrongVehicleDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(final Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.wrong_vehicle_exp);
			builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    dismiss();
                }
            });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	public static class NoDataDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(final Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.no_data_vehicle_exp);
			builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int id) {
                    dismiss();
                }
            });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.stop = StopGeofenceStore.getGeofence(this, StopGeofence.STOP_GEOFENCE_ID);

		setContentView(R.layout.vehicle_notification_view);
		wrong = (TextView)findViewById(R.id.wrong_vehicle_view);
		wrong.setOnClickListener(wrongClick);
	}

	@Override
	protected void onResume() {
		super.onResume();

		//Create the crowd fragment
		final Bundle bCrowd = new Bundle();

		bCrowd.putInt(CrowdVehicleFragment.EXTRA_VEHICLE_CROWD, stop.getStopCrowd());
		crvf = new CrowdVehicleFragment();
		crvf.setArguments(bCrowd);
		
		if (stop.getDepartureCode() != StopGeofence.UNSET_DEP_CODE) {
			final Bundle bStops = new Bundle();
			bStops.putString(ShowNextStopsFragment.EXTRA_LINE_CODE, stop.getLineCode());
			bStops.putString(ShowNextStopsFragment.EXTRA_DEST_NAME, stop.getDestinationName());
			bStops.putInt(ShowNextStopsFragment.EXTRA_DEP_CODE, stop.getDepartureCode());
			bStops.putString(ShowNextStopsFragment.EXTRA_STOP_CODE, stop.getStopCode());
			snsf = new ShowNextStopsFragment();
			snsf.setArguments(bStops);
		}
		else {
			snsf = null;
			wrong.setText(R.string.no_data_vehicle);
		}

		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();

		ft.replace(R.id.crowdFragment, crvf, CrowdStopFragment.class.getSimpleName());
		if (snsf != null) {
			ft.replace(R.id.nextStopsFragment, snsf, ShowNextStopsFragment.class.getSimpleName());			
		}
		ft.commit();
	}
}
