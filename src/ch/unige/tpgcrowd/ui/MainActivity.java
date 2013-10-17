package ch.unige.tpgcrowd.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.ui.fragments.ShowPhisicalStopsFragment;
import ch.unige.tpgcrowd.util.ColorStore;

public class MainActivity extends FragmentActivity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ColorStore.updateColors(this);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        final ShowPhisicalStopsFragment spsf = new ShowPhisicalStopsFragment();
        ft.add(spsf, "PhisicalStops");
        ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
