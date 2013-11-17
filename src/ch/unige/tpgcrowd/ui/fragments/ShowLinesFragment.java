package ch.unige.tpgcrowd.ui.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.model.PhysicalStop;
import ch.unige.tpgcrowd.model.Stop;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment.PhysicalStopRender;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment.PhysicalStopSelectedListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
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
		final FragmentTransaction ft = fm.beginTransaction();
		spsf = ShowPhisicalStopsFragment.newInstance();
		ft.add(R.id.phisicalStopsFragment, spsf, null);
		ft.commit();

		return layout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		final FragmentManager fm = getFragmentManager();
		final GoogleMapOptions mapOpt = new GoogleMapOptions();
		mapOpt.useViewLifecycleInFragment(true);
		final SupportMapFragment mapfr = SupportMapFragment.newInstance(mapOpt);
		final FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.smallMap, mapfr, SMALL_MAP_FRAGMENT);
		ft.commit();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		final FragmentManager fm = getFragmentManager();
		final SupportMapFragment map = (SupportMapFragment)fm.findFragmentByTag(SMALL_MAP_FRAGMENT);
		final GoogleMap gmap = map.getMap();
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.204705, 6.1431301), 10));
		final UiSettings settings = gmap.getUiSettings();
		settings.setAllGesturesEnabled(false);
		settings.setMyLocationButtonEnabled(false);
		settings.setZoomControlsEnabled(false);
		map.getMap().setOnMapClickListener(mapClick);
	}
	
	@Override
	public void setAsReloading() {
		if (spsf != null) {			
			((PhysicalStopRender)spsf).setAsReloading();
		}
	}

	@Override
	public void setPhysicalStops(final Stop rootStop, final List<PhysicalStop> stops, PhysicalStopSelectedListener listener) {
		((PhysicalStopRender)spsf).setPhysicalStops(rootStop, stops, listener);
	}

	@Override
	public void showError() {
		((PhysicalStopRender)spsf).showError();
	}

}
