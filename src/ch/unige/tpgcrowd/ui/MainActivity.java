package ch.unige.tpgcrowd.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.GooglePlayServiceCheckUtility;
import ch.unige.tpgcrowd.model.Stop;
import ch.unige.tpgcrowd.ui.fragments.ShowNearbyStopsFragment;
import ch.unige.tpgcrowd.ui.fragments.ShowNearbyStopsFragment.StopRender;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment;
import ch.unige.tpgcrowd.util.ColorStore;

public class MainActivity extends FragmentActivity implements StopRender {
	private ShowStopFragment spsf;
	private ShowNearbyStopsFragment nearbyFragment;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ColorStore.updateColors(this);
		setContentView(R.layout.activity_main);
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

			//		Log.i("TPG", "CALL VIEW ACT.");
			//		if (spsf == null) {
			//			spsf = new ShowStopFragment();
			//			final Bundle b = new Bundle();
			//			b.putString(ShowStopFragment.EXTRA_STOP_NAME, "Gare Cornavin");
			//			b.putString(ShowStopFragment.EXTRA_STOP_CODE, "CVIN");
			//			spsf.setArguments(b);
			//			ft.add(R.id.main, spsf, ShowStopFragment.TAG);
			//		}

			ft.commit();
		}

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
		final Bundle b = new Bundle();
		b.putString(ShowStopFragment.EXTRA_STOP_NAME, stop.getStopName());
		b.putString(ShowStopFragment.EXTRA_STOP_CODE, stop.getStopCode());
		
		if (spsf == null) {
			final FragmentManager fm = getSupportFragmentManager();
			final FragmentTransaction ft = fm.beginTransaction();
			
			spsf = new ShowStopFragment();
			spsf.setArguments(b);
			ft.add(R.id.main, spsf, ShowStopFragment.TAG);
			
			ft.commit();
		}
		else {
			spsf.updateContent(b);
		}
	}

}
