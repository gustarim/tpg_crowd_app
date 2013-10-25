package ch.unige.tpgcrowd.ui.fragments;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.manager.ITPGDepartures;
import ch.unige.tpgcrowd.manager.TPGManager;
import ch.unige.tpgcrowd.model.Departure;
import ch.unige.tpgcrowd.model.DepartureList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;
import ch.unige.tpgcrowd.util.ColorStore;

public class ShowNextDeparturesFragment extends Fragment {
	
	public static final String EXTRA_LINE_CODE = "ch.unige.tpgcrowd.extra.LINE_CODE";
	public static final String EXTRA_DEST_CODE = "ch.unige.tpgcrowd.extra.DEST_CODE";
	public static final String EXTRA_STOP_CODE = "ch.unige.tpgcrowd.extra.STOP_CODE";
	public static final String EXTRA_DEST_NAME = "ch.unige.tpgcrowd.extra.DEST_NAME";
	
	private final TPGObjectListener<DepartureList> departureListener = new TPGObjectListener<DepartureList>() {
		
		@Override
		public void onSuccess(final DepartureList results) {
			progres.setVisibility(View.GONE);
			final DepartureListAdapter adapter = new DepartureListAdapter(results.getDepartures());
			list.setAdapter(adapter);
			list.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onFailure() {
			// TODO Auto-generated method stub
			
		}
	};
	
	private View progres;
	private ListView list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.show_line_next_departures, container, false);
		final Bundle b = getArguments();
		final String lineCode = b.getString(EXTRA_LINE_CODE);
		
		final LinearLayout lineInfo = (LinearLayout)layout.findViewById(R.id.lineInfo);
		final TextView lineIcon = (TextView)lineInfo.findViewById(R.id.lineIcon);
		lineIcon.setText(lineCode);
		lineIcon.setBackgroundColor(ColorStore.getColor(getActivity(), lineCode));
		final TextView dir = (TextView)lineInfo.findViewById(R.id.direction);
		dir.setText(b.getString(EXTRA_DEST_NAME));
		
		progres = layout.findViewById(R.id.progres);
		list = (ListView)layout.findViewById(R.id.departuresList);
		
		final ITPGDepartures depMan = TPGManager.getDeparturesManager(getActivity());
		final String destinationCode = b.getString(EXTRA_DEST_CODE);
		final String stopCode = b.getString(EXTRA_STOP_CODE);
		depMan.getNextDepartures(stopCode, lineCode, destinationCode, departureListener);
		
		return layout;
	}
	
	private class DepartureListAdapter extends ArrayAdapter<Departure> {
		private final List<Departure> departures;

		public DepartureListAdapter(final List<Departure> deps) {
			super(getActivity(), R.layout.list_row_show_departure, deps);
			departures = deps;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final LayoutInflater inflater = (LayoutInflater) getActivity()
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View rowView = inflater.inflate(R.layout.list_row_show_departure, parent, false);
			final Departure dep = departures.get(position);
			
			final TextView time = (TextView)rowView.findViewById(R.id.time);
			time.setText(dep.getWaitingTime());
			
			return rowView;
		}
	}

}
