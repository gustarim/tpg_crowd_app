package ch.unige.tpgcrowd.ui.fragments;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	protected static final String STRING_HOUR = "&gt;1h";
	
	private final TPGObjectListener<DepartureList> departureListener = new TPGObjectListener<DepartureList>() {
		
		@Override
		public void onSuccess(final DepartureList results) {
			progres.setVisibility(View.GONE);
			Log.d("dep", "next " + results.getDepartures().size());
			deps = results.getDepartures();
			sortByCrowd(deps);
			final DepartureListAdapter adapter = new DepartureListAdapter(deps);
			list.setAdapter(adapter);
			list.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onFailure() {
			// TODO Auto-generated method stub
			
		}
	};
	
	private final OnClickListener crowdSortClick = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			sortByCrowd(deps);
			final DepartureListAdapter adapter = new DepartureListAdapter(deps);
			list.setAdapter(adapter);
		}
	};
	
	private final OnClickListener timeSortClick = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			sortByTime(deps);
			final DepartureListAdapter adapter = new DepartureListAdapter(deps);
			list.setAdapter(adapter);
		}
	};
	
	private View progres;
	private ListView list;
	private List<Departure> deps;
	
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
		final Button sortCrowd = (Button)layout.findViewById(R.id.sortCrowd);
		sortCrowd.setOnClickListener(crowdSortClick);
		final Button sortTime = (Button)layout.findViewById(R.id.sortTime);
		sortTime.setOnClickListener(timeSortClick);
		
		progres = layout.findViewById(R.id.progres);
		list = (ListView)layout.findViewById(R.id.departuresList);
		
		final ITPGDepartures depMan = TPGManager.getDeparturesManager(getActivity());
		final String destinationCode = b.getString(EXTRA_DEST_CODE);
		final String stopCode = b.getString(EXTRA_STOP_CODE);
		Log.d("dep", stopCode + " " + lineCode + " " + destinationCode);
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
			time.setText(dep.getWaitingTime().equals(STRING_HOUR) ? ">60" : dep.getWaitingTime());
			
			final TextView crowd = (TextView)rowView.findViewById(R.id.crowd);
			crowd.setText(dep.getCrowd() + "");
			
			return rowView;
		}
	}

	private static void sortByCrowd(final List<Departure> deps) {
		Collections.sort(deps, new Comparator<Departure>() {

			@Override
			public int compare(final Departure lhs, final Departure rhs) {
				return lhs.getCrowd() - rhs.getCrowd();
			}
		});
	}
	
	private static void sortByTime(final List<Departure> deps) {
		Collections.sort(deps, new Comparator<Departure>() {

			@Override
			public int compare(final Departure lhs, final Departure rhs) {
				final int lhsTime = lhs.getWaitingTime().equals(STRING_HOUR) ? 
						61 : Integer.valueOf(lhs.getWaitingTime());
				final int rhsTime = rhs.getWaitingTime().equals(STRING_HOUR) ? 
						61 : Integer.valueOf(rhs.getWaitingTime());;
				
				
				return lhsTime - rhsTime;
			}
		});
	}
}
