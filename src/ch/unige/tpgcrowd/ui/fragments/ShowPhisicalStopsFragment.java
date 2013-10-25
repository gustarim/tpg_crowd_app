package ch.unige.tpgcrowd.ui.fragments;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.geofence.GeofenceHandler;
import ch.unige.tpgcrowd.google.geofence.SimpleGeofence;
import ch.unige.tpgcrowd.google.geofence.SimpleGeofenceStore;
import ch.unige.tpgcrowd.google.geofence.StopTransitionsIntentService;
import ch.unige.tpgcrowd.model.Connection;
import ch.unige.tpgcrowd.model.Coordinates;
import ch.unige.tpgcrowd.model.PhysicalStop;
import ch.unige.tpgcrowd.ui.fragments.ShowStopFragment.PhysicalStopRender;
import ch.unige.tpgcrowd.util.ColorStore;

import com.google.android.gms.location.Geofence;

public class ShowPhisicalStopsFragment extends Fragment implements PhysicalStopRender {
	protected static final String STOP_GEOFENCE_ID = "StopGeofence";
	protected static final float STOP_GEOFENCE_RADIUS = 15;
	protected static final long STOP_GEOFENCE_EXPIRATION = 1200000;
	
	private final OnChildClickListener lineClick = new OnChildClickListener() {
		
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			final PhysicalStopsExpandableListAdapter.Line line = (PhysicalStopsExpandableListAdapter.Line)adapter.getGroup(groupPosition);
			final PhysicalStop stop = line.getConnectionPhysicalStop(childPosition);
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
	
	private View progres;
	private ExpandableListView list;
	private PhysicalStopsExpandableListAdapter adapter;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		final View layout = inflater.inflate(R.layout.show_phisical_stops, null, false);
		progres = layout.findViewById(R.id.progres);
		list = (ExpandableListView)layout.findViewById(R.id.list);
		return layout;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void setAsReloading() {
		list.setVisibility(View.GONE);
		progres.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void setPhysicalStops(final List<PhysicalStop> stops) {
		progres.setVisibility(View.GONE);
		adapter = new PhysicalStopsExpandableListAdapter(stops, getActivity());
		list.setAdapter(adapter);
		if (adapter.getGroupCount() == 1) {
			list.expandGroup(0, true);
		}
		list.setOnChildClickListener(lineClick);
		list.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void showError() {
		//progres.setText(R.string.error);
	}
	
	private class PhysicalStopsExpandableListAdapter extends BaseExpandableListAdapter {
		private final List<Line> lines;
		private final Context context;
		
		public final class Line {
			private final String lineCode;
			private final LinkedList<Connection> connections;
			private final SparseArray<PhysicalStop> connStops;
			
			public Line(final String lineCode) {
				this.lineCode = lineCode;
				connections = new LinkedList<Connection>();
				connStops = new SparseArray<PhysicalStop>();
			}
			
			public String getLineCode() {
				return lineCode;
			}
			
			public void addConnection(final Connection conn, final PhysicalStop stop) {
				connections.add(conn);
				connStops.append(connections.size() - 1, stop);
			}
			
			public Connection getConnection(final int index) {
				return connections.get(index);
			}
			
			public PhysicalStop getConnectionPhysicalStop(final int index) {
				return connStops.get(index);
			}
			
			public int getConnectionsSize() {
				return connections.size();
			}
		}
		
		public PhysicalStopsExpandableListAdapter(final List<PhysicalStop> stops, final Context context) {
			this.context = context;
			lines = new LinkedList<Line>();
			final LinkedList<String> tempLines = new LinkedList<String>();
			for (int i = 0; i < stops.size(); i++) {
				final PhysicalStop stop = stops.get(i);
				final List<Connection> conns = stop.getConnections();
				for (int j = 0; j < conns.size(); j++) {
					final Connection conn = conns.get(j);
					final String lineCode = conn.getLineCode();
					Line line = null;
					if (!tempLines.contains(lineCode)) {
						tempLines.add(lineCode);
						line = new Line(lineCode);
						lines.add(line);
					}
					line = lines.get(tempLines.indexOf(lineCode));
					line.addConnection(conn, stop);
				}
			}
		}

		@Override
		public Object getChild(final int line, final int conn) {
			return lines.get(line).getConnection(conn);
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
			final TextView destination = (TextView)rowView.findViewById(R.id.direction);
			destination.setText(conn.getDestinationName());
			return rowView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return lines.get(groupPosition).getConnectionsSize();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return lines.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return lines.size();
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
			final View rowView = inflater.inflate(R.layout.show_line, parent, false);
			final TextView lineIcon = (TextView)rowView.findViewById(R.id.lineIcon);
			final Line line = lines.get(groupPosition);
			lineIcon.setText(line.getLineCode());
			lineIcon.setBackgroundColor(ColorStore.getColor(context, line.getLineCode()));
//			final TextView name = (TextView)rowView.findViewById(R.id.stopName);
//			final PhysicalStop stop = (PhysicalStop)getGroup(groupPosition);
//			name.setText(stop.getStopCode());
//			if (!isExpanded) {
//				final List<String> seen = new LinkedList<String>();
//				final List<Connection> conns = stop.getConnections();
//				final LinearLayout icons = (LinearLayout)rowView.findViewById(R.id.lineIcons);
//				for (Connection conn : conns) {
//					final String code = conn.getLineCode();
//					if (!seen.contains(code)) {
//						final TextView icon = (TextView)inflater.inflate(R.layout.large_line_icon, parent, false);
//						icon.setText(code);
//						icon.setBackgroundColor(ColorStore.getColor(context, code));
//						icons.addView(icon);
//						seen.add(code);
//					}
//				}
//			}
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
