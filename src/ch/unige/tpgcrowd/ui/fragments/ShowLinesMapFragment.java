package ch.unige.tpgcrowd.ui.fragments;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.model.Connection;
import ch.unige.tpgcrowd.model.Coordinates;
import ch.unige.tpgcrowd.model.PhysicalStop;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment.PhysicalStopRender;
import ch.unige.tpgcrowd.util.ColorStore;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowLinesMapFragment extends Fragment implements PhysicalStopRender {
	public interface OnLinesClickListener {
		public void onLinesClick();
	}
	private GoogleMap map;
	
	List<Marker> markersStops = new ArrayList<Marker>();
	Marker userLocMarker = null;
	Marker sysLocMarker = null;
	Circle sysLocCircle = null;

	private final OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			final FragmentManager fm = getFragmentManager();
			final OnLinesClickListener lines = 
					(OnLinesClickListener)fm.findFragmentByTag(ShowStopFragment.TAG);
			lines.onLinesClick();
		}
	};

	private OnLineMapLongClickListener listener;

	private final OnInfoWindowClickListener infoclick = new OnInfoWindowClickListener() {
		
		@Override
		public void onInfoWindowClick(final Marker marker) {
			if (marker.isInfoWindowShown()) {
				marker.hideInfoWindow();
			}
		}
	};
	
	public interface OnLineMapLongClickListener {
		public void onLineMapLongClick(float zoom, LatLng currentCenter, LatLng pressPosition);
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnLineMapLongClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnLineMapLongClickListener");
		}
	}

	private class StopInfoWindowAdapter implements InfoWindowAdapter {
		private final Hashtable<Marker, List<Connection>> connections;

		public StopInfoWindowAdapter() {
			connections = new Hashtable<Marker, List<Connection>>();
		}

		@Override
		public View getInfoWindow(final Marker marker) {
			return null;
		}

		@Override
		public View getInfoContents(final Marker marker) {
			final LayoutInflater inflater = getActivity().getLayoutInflater();
			final LinearLayout v = (LinearLayout)inflater.inflate(R.layout.info_windows_lines, null);
			final List<Connection> conns = connections.get(marker);
			for (final Connection conn : conns) {
				final View lineView = inflater.inflate(R.layout.show_line_connection, null);
				final TextView icon = (TextView)lineView.findViewById(R.id.lineIcon);
				final String lineCode = conn.getLineCode();
				icon.setText(lineCode);
				icon.setBackgroundColor(ColorStore.getColor(getActivity(), lineCode));
				final TextView dest = (TextView)lineView.findViewById(R.id.direction);
				dest.setText(conn.getDestinationName());
				v.addView(lineView);
			}
			return v;
		}

		public void addConnections(final Marker m, final List<Connection> conns) {
			connections.put(m, conns);
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.show_stop_lines_map, null, false);
		final LinearLayout lines = (LinearLayout)layout.findViewById(R.id.mapLineButton);
		lines.setOnClickListener(click);
		final FragmentManager fm = getFragmentManager();
		map = ((SupportMapFragment)fm.findFragmentByTag("bigMap")).getMap();
		
		map.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				CameraPosition curMapPosition = map.getCameraPosition();
				LatLng ll = curMapPosition.target;
				float zoom = curMapPosition.zoom;
				listener.onLineMapLongClick(zoom, ll, arg0);
			}
		});

		return layout;
	}
	
	@Override
	public void setAsReloading() {
		if (map != null) {
//			//Remove old entries
//			map.clear();		
			//Remove old entries
			if (!markersStops.isEmpty()){
				for (final Marker m : markersStops) {
					m.remove();
				}
				
				markersStops.clear();
				
			}
		}
	}

	@Override
	public void setPhysicalStops(final List<PhysicalStop> stops) {
		

		final StopInfoWindowAdapter siwa = new StopInfoWindowAdapter();

		for (final PhysicalStop stop : stops) {
			final Coordinates c = stop.getCoordinates();
			final LatLng ll = new LatLng(c.getLatitude(), c.getLongitude());
			final MarkerOptions mo = new MarkerOptions();
			mo.position(ll);
			final Marker m = map.addMarker(mo);
			
			markersStops.add(m);

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 16));
			siwa.addConnections(m, stop.getConnections());
		}
		map.setInfoWindowAdapter(siwa);
		map.setOnInfoWindowClickListener(infoclick);
	}

	@Override
	public void showError() {
		// TODO Auto-generated method stub

	}

	public void setRefMarker(double latitude, double longitude) {
		final MarkerOptions mo = new MarkerOptions();
		final LatLng ll = new LatLng(latitude,longitude);
		mo.position(ll);
		
		mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.green_dot));
		
		if (userLocMarker!=null){
			userLocMarker.remove();					
		}
		final Marker m = map.addMarker(mo);
		userLocMarker = m;	
		
	}

	public void setSystemLocation(Location loc) {
		final MarkerOptions mo = new MarkerOptions();
		LatLng ll = new LatLng(loc.getLatitude(),loc.getLongitude());
		mo.position(ll);
		
		mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_dot));
		
		if (sysLocMarker!=null){
			sysLocMarker.remove();					
		}
		final Marker m = map.addMarker(mo);
		sysLocMarker = m;	
		
		final CircleOptions co = new CircleOptions();
		co.center(ll);
		co.strokeWidth(2);
		co.strokeColor(0x440000ff);
		co.fillColor(0x220000ff);
		co.radius(loc.getAccuracy());
		
		if (sysLocCircle!=null){
			sysLocCircle.remove();					
		}
		
		final Circle c = map.addCircle(co);
		sysLocCircle = c;
		
	}
}