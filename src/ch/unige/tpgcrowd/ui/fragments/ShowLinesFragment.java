package ch.unige.tpgcrowd.ui.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.model.PhysicalStop;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment.PhysicalStopRender;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

public class ShowLinesFragment extends Fragment implements PhysicalStopRender {
	public interface OnLinesMapClickListener {
		public void onMapClick();
	}
	private static final String SMALL_MAP_FRAGMENT = "smallMap";
	private final OnMapClickListener mapClick = new OnMapClickListener() {

		@Override
		public void onMapClick(final LatLng point) {
			final FragmentManager fm = getFragmentManager();
			final OnLinesMapClickListener listener = 
					(OnLinesMapClickListener)fm.findFragmentByTag(ShowStopFragment.TAG);
			listener.onMapClick();
		}
	};
	private ShowPhisicalStopsFragment spsf;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View layout = getView();		

		layout = inflater.inflate(R.layout.show_stop_lines, null, false);

		final FragmentManager fm = getFragmentManager();
		final SupportMapFragment map = (SupportMapFragment)fm.findFragmentByTag(SMALL_MAP_FRAGMENT);
		final GoogleMap gmap = map.getMap();
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.204705, 6.1431301), 10));
		final UiSettings settings = gmap.getUiSettings();
		settings.setAllGesturesEnabled(false);
		settings.setMyLocationButtonEnabled(false);
		settings.setZoomControlsEnabled(false);
		map.getMap().setOnMapClickListener(mapClick);

		final FragmentTransaction ft = fm.beginTransaction();
		spsf = new ShowPhisicalStopsFragment();
		ft.add(R.id.phisicalStopsFragment, spsf, null);
		ft.commit();

		return layout;
	}

	@Override
	public void setPhysicalStops(final List<PhysicalStop> stops) {
		((PhysicalStopRender)spsf).setPhysicalStops(stops);
	}

	@Override
	public void showError() {
		((PhysicalStopRender)spsf).showError();
	}

}
