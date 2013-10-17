package ch.unige.tpgcrowd.ui.fragments;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.geofence.GeofenceHandler;
import ch.unige.tpgcrowd.google.geofence.SimpleGeofence;
import ch.unige.tpgcrowd.google.geofence.SimpleGeofenceStore;
import ch.unige.tpgcrowd.google.geofence.StopTransitionsIntentService;
import ch.unige.tpgcrowd.manager.ITPGStops;
import ch.unige.tpgcrowd.manager.TPGManager;
import ch.unige.tpgcrowd.model.Connection;
import ch.unige.tpgcrowd.model.Coordinates;
import ch.unige.tpgcrowd.model.PhysicalStop;
import ch.unige.tpgcrowd.model.Stop;
import ch.unige.tpgcrowd.model.StopList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;
import ch.unige.tpgcrowd.util.ColorStore;

import com.google.android.gms.location.Geofence;

public class ShowPhisicalStopsFragment extends Fragment {
	protected static final String STOP_GEOFENCE_ID = "StopGeofence";
	protected static final float STOP_GEOFENCE_RADIUS = 15;
	protected static final long STOP_GEOFENCE_EXPIRATION = 1200000;

	private final TPGObjectListener<StopList> stopsListener = new TPGObjectListener<StopList>() {
		
		@Override
		public void onSuccess(final StopList results) {
			final List<Stop> stops = results.getStops();
			if (stops != null && !stops.isEmpty()) {
				progres.setVisibility(View.GONE);
				final Stop stop = results.getStops().get(0);
				final List<PhysicalStop> phyStops = stop.getPhysicalStops();
				adapter = new PhysicalStopsExpandableListAdapter(phyStops, getActivity());
				list.setAdapter(adapter);
				list.setOnChildClickListener(lineClick);
			}
		}
		
		@Override
		public void onFailure() {
			progres.setText(R.string.error);
		}
	};
	
	private final OnChildClickListener lineClick = new OnChildClickListener() {
		
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			Log.i("LIST", "STOP CLICK");
			final PhysicalStop stop = (PhysicalStop)adapter.getGroup(groupPosition);
			final Coordinates pos = stop.getCoordinates();
			final SimpleGeofence sg = new SimpleGeofence(STOP_GEOFENCE_ID, pos.getLatitude(), 
					pos.getLongitude(), STOP_GEOFENCE_RADIUS, STOP_GEOFENCE_EXPIRATION,
					Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
			SimpleGeofenceStore.setGeofence(getActivity(), STOP_GEOFENCE_ID, sg);
			final boolean added =  GeofenceHandler.addGeofences(getActivity(), new String[] {STOP_GEOFENCE_ID}, 
					StopTransitionsIntentService.getTransitionPendingIntent(getActivity().getApplicationContext()));
			if (added) {
				Log.i(STOP_GEOFENCE_ID, "Geofence added");
			}
			return added;
		}
	};
	
	private TextView progres;
	private ExpandableListView list;
	private PhysicalStopsExpandableListAdapter adapter;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		final View layout = inflater.inflate(R.layout.show_phisical_stops, container, false);
		progres = (TextView)layout.findViewById(R.id.progres);
		list = (ExpandableListView)layout.findViewById(R.id.list);
		
		final ITPGStops phisicalStops = TPGManager.getStopsManager(getActivity());
		phisicalStops.getPhysicalStopByCode("CVIN", stopsListener);
		
		return layout;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	private class PhysicalStopsExpandableListAdapter extends BaseExpandableListAdapter {
		private final List<PhysicalStop> stops;
		private final Context context;
		
		public PhysicalStopsExpandableListAdapter(final List<PhysicalStop> stops, final Context context) {
			this.stops = stops;
			this.context = context;
		}

		@Override
		public Object getChild(final int stop, final int conn) {
			return stops.get(stop).getConnections().get(conn);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			final LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View rowView = inflater.inflate(R.layout.list_row_show_connection, parent, false);
			final Connection conn = (Connection)getChild(groupPosition, childPosition);
			final TextView lineIcon = (TextView)rowView.findViewById(R.id.lineIcon);
			lineIcon.setText(conn.getLineCode());
			lineIcon.setBackgroundColor(ColorStore.getColor(context, conn.getLineCode()));
			final TextView destination = (TextView)rowView.findViewById(R.id.direction);
			destination.setText(conn.getDestinationName());
			return rowView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return stops.get(groupPosition).getConnections().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return stops.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return stops.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			final LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View rowView = inflater.inflate(R.layout.show_physical_stop, parent, false);
			final TextView name = (TextView)rowView.findViewById(R.id.stopName);
			final PhysicalStop stop = (PhysicalStop)getGroup(groupPosition);
			name.setText(stop.getStopCode());
			if (!isExpanded) {
				final List<String> seen = new LinkedList<String>();
				final List<Connection> conns = stop.getConnections();
				final LinearLayout icons = (LinearLayout)rowView.findViewById(R.id.lineIcons);
				for (Connection conn : conns) {
					final String code = conn.getLineCode();
					if (!seen.contains(code)) {
						final TextView icon = (TextView)inflater.inflate(R.layout.large_line_icon, parent, false);
						icon.setText(code);
						icon.setBackgroundColor(ColorStore.getColor(context, code));
						icons.addView(icon);
						seen.add(code);
					}
				}
			}
			return rowView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
	}
}
