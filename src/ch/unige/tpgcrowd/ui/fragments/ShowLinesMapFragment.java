package ch.unige.tpgcrowd.ui.fragments;

import java.util.Hashtable;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowLinesMapFragment extends Fragment implements PhysicalStopRender {
	public interface OnLinesClickListener {
		public void onLinesClick();
	}
	private GoogleMap map;

	private final OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			final FragmentManager fm = getFragmentManager();
			final OnLinesClickListener lines = 
					(OnLinesClickListener)fm.findFragmentByTag(ShowStopFragment.TAG);
			lines.onLinesClick();
		}
	};

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
			final LatLng ll = marker.getPosition();
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
		final LinearLayout lines = (LinearLayout)layout.findViewById(R.id.mapLineLayout);
		lines.setOnClickListener(click);
		final FragmentManager fm = getFragmentManager();
		map = ((SupportMapFragment)fm.findFragmentByTag("bigMap")).getMap();

		return layout;
	}

	@Override
	public void setPhysicalStops(final List<PhysicalStop> stops) {
		//Remove old entries
		map.clear();
		
		final StopInfoWindowAdapter siwa = new StopInfoWindowAdapter();
		for (final PhysicalStop stop : stops) {
			final Coordinates c = stop.getCoordinates();
			final LatLng ll = new LatLng(c.getLatitude(), c.getLongitude());
			final MarkerOptions mo = new MarkerOptions();
			mo.position(ll);
			final Marker m = map.addMarker(mo);

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 16));
			siwa.addConnections(m, stop.getConnections());
		}
		map.setInfoWindowAdapter(siwa);
	}

	@Override
	public void showError() {
		// TODO Auto-generated method stub

	}
}
