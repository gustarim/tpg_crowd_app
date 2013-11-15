package ch.unige.tpgcrowd.ui.fragments;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.google.geofence.StopGeofence;
import ch.unige.tpgcrowd.google.geofence.StopGeofenceStore;
import ch.unige.tpgcrowd.manager.ITPGDepartures;
import ch.unige.tpgcrowd.manager.TPGManager;
import ch.unige.tpgcrowd.model.Departure;
import ch.unige.tpgcrowd.model.DepartureList;
import ch.unige.tpgcrowd.model.Disruption;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;
import ch.unige.tpgcrowd.ui.component.InfoItem;
import ch.unige.tpgcrowd.ui.component.QuickInfo;
import ch.unige.tpgcrowd.util.ColorStore;

public class ShowNextDeparturesFragment extends Fragment {

	public static final String EXTRA_LINE_CODE = "ch.unige.tpgcrowd.extra.LINE_CODE";
	public static final String EXTRA_DEST_CODE = "ch.unige.tpgcrowd.extra.DEST_CODE";
	public static final String EXTRA_STOP_CODE = "ch.unige.tpgcrowd.extra.STOP_CODE";
	public static final String EXTRA_PHYSICAL_STOP_CODE = "ch.unige.tpgcrowd.extra.PHYSICAL_STOP_CODE";
	public static final String EXTRA_DEST_NAME = "ch.unige.tpgcrowd.extra.DEST_NAME";
	protected static final String STRING_HOUR = "&gt;1h";

    private static final int ID_INCIDENT = 1;
    private static final int ID_PMR_GREEN = 2;
    private static final int ID_PMR_ORANGE = 3;
    private static final int ID_PMR_RED = 4;	
	
	public interface OnDepartureClickListener {
		public void onItemListclicked(final String lineCode, final String destinationName, final int departurecode);
	}

	private OnDepartureClickListener depClickListener;

	private final TPGObjectListener<DepartureList> departureListener = new TPGObjectListener<DepartureList>() {

		@Override
		public void onSuccess(final DepartureList results) {
			deps = results.getDepartures();
			progres.setVisibility(View.GONE);
			Log.d("dep", "next " + results.getDepartures().size());
			sortByCrowd(deps);
			final ImageButton sortCrowd = (ImageButton)layout.findViewById(R.id.sortCrowd);
			sortCrowd.setOnClickListener(crowdSortClick);
			sortCrowd.setBackgroundResource(R.drawable.background_selected);
			final ImageButton sortTime = (ImageButton)layout.findViewById(R.id.sortTime);
			sortTime.setOnClickListener(timeSortClick);
			sortTime.setBackgroundResource(0);
			final DepartureListAdapter adapter = new DepartureListAdapter(deps);
			list.setAdapter(adapter);
			list.setOnItemClickListener(listItemClick);
			list.setVisibility(View.VISIBLE);
			final TextView update = (TextView)getView().findViewById(R.id.updateTime);
			update.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date()));
		}

		@Override
		public void onFailure() {
			// TODO Auto-generated method stub

		}
	};

	private final OnClickListener refreshClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			requestNextDepartures();
			final TextView update = (TextView)getView().findViewById(R.id.updateTime);
			update.setText(R.string.loading);
		}
	};

	private final OnClickListener crowdSortClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			sortByCrowd(deps);
			final ImageButton sortCrowd = (ImageButton)layout.findViewById(R.id.sortCrowd);
			sortCrowd.setBackgroundResource(R.drawable.background_selected);
			final ImageButton sortTime = (ImageButton)layout.findViewById(R.id.sortTime);
			sortTime.setBackgroundResource(0);
			final DepartureListAdapter adapter = new DepartureListAdapter(deps);
			list.setAdapter(adapter);
		}
	};

	private final OnClickListener timeSortClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			sortByTime(deps);
			final ImageButton sortCrowd = (ImageButton)layout.findViewById(R.id.sortCrowd);
			sortCrowd.setBackgroundResource(0);
			final ImageButton sortTime = (ImageButton)layout.findViewById(R.id.sortTime);
			sortTime.setBackgroundResource(R.drawable.background_selected);
			final DepartureListAdapter adapter = new DepartureListAdapter(deps);
			list.setAdapter(adapter);
		}
	};
	
	private final OnClickListener infoClick = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			quickinfo.show(v);
		}
	};

	private final OnItemClickListener listItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(final AdapterView<?> arg0, final View arg1, final int position,
				final long arg3) {
			final Departure dep = deps.get(position);

			if (dep != null && dep.getDepartureCode() != null) {

				final StopGeofence geo = StopGeofenceStore.getGeofence(getActivity(), StopGeofence.STOP_GEOFENCE_ID);
				geo.setDepartureCode(dep.getDepartureCode());
				StopGeofenceStore.setGeofence(getActivity(), StopGeofence.STOP_GEOFENCE_ID, geo);
				if (depClickListener != null) {
					depClickListener.onItemListclicked(lineCode, destinationName, dep.getDepartureCode());
				}
			}
		}

	};

	private View progres;
	private ListView list;
	private List<Departure> deps;
	private String lineCode;
	private String destinationCode;
	private String destinationName;
	private String stopCode;
	private LinearLayout layout;
	
	protected QuickInfo quickinfo;

	public void setOnDepepartureClickListener(final OnDepartureClickListener depClickListener) {
		this.depClickListener = depClickListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = (LinearLayout)inflater.inflate(R.layout.show_line_next_departures, container, false);
		final Bundle b = getArguments();
		lineCode = b.getString(EXTRA_LINE_CODE);
		destinationName = b.getString(EXTRA_DEST_NAME);

		InfoItem incidentItem     = new InfoItem(ID_INCIDENT, getResources().getString(R.string.info_incident), getResources().getDrawable(R.drawable.incident));
		InfoItem pmrGreenItem     = new InfoItem(ID_PMR_GREEN, getResources().getString(R.string.info_pmr_green), getResources().getDrawable(R.drawable.handi_green));
		InfoItem pmrOrangeItem   = new InfoItem(ID_PMR_ORANGE, getResources().getString(R.string.info_pmr_orange), getResources().getDrawable(R.drawable.handi_orange));
		InfoItem pmrRedItem     = new InfoItem(ID_PMR_RED, getResources().getString(R.string.info_pmr_red), getResources().getDrawable(R.drawable.handi_red));
		
		quickinfo = new QuickInfo(this.getActivity());
		quickinfo.addInfoItem(incidentItem);
		quickinfo.addInfoItem(pmrGreenItem);
		quickinfo.addInfoItem(pmrOrangeItem);
		quickinfo.addInfoItem(pmrRedItem);
		
		final LinearLayout lineInfo = (LinearLayout)layout.findViewById(R.id.lineInfo);
		final TextView lineIcon = (TextView)lineInfo.findViewById(R.id.lineIcon);
		lineIcon.setText(lineCode);
		lineIcon.setBackgroundColor(ColorStore.getColor(getActivity(), lineCode));
		
		final ImageButton refresh = (ImageButton)layout.findViewById(R.id.refresh);
		refresh.setOnClickListener(refreshClick);
		
		final TextView dir = (TextView)lineInfo.findViewById(R.id.direction);
		dir.setText(destinationName);

		final ImageButton infoBtn = (ImageButton)layout.findViewById(R.id.getInfo);
		infoBtn.setOnClickListener(infoClick);
		
		progres = layout.findViewById(R.id.progres);
		list = (ListView)layout.findViewById(R.id.departuresList);

		destinationCode = b.getString(EXTRA_DEST_CODE);
		stopCode = b.getString(EXTRA_STOP_CODE);
		Log.d("dep", stopCode + " " + lineCode + " " + destinationCode);
		requestNextDepartures();
		return layout;
	}

	private void requestNextDepartures() {
		final ITPGDepartures depMan = TPGManager.getDeparturesManager(getActivity());
		depMan.getNextDepartures(stopCode, lineCode, destinationCode, departureListener);
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

			final ImageView crowd = (ImageView)rowView.findViewById(R.id.crowd);
			crowd.setImageLevel(dep.getCrowd());

			final ImageView info = (ImageView)rowView.findViewById(R.id.info);
			if (dep.getDisruptions() != null && !dep.getDisruptions().isEmpty()) {
				info.setVisibility(ImageView.VISIBLE);

				final Disruption dis = dep.getDisruptions().get(0);
				info.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(getContext(), dis.getConsequence(), Toast.LENGTH_SHORT).show();
					}
				});
			}

			final ImageView pmr = (ImageView)rowView.findViewById(R.id.pmr);
			if (dep.getCharacteristics() != null && dep.getCharacteristics().contains("PMR")) {
				pmr.setVisibility(ImageView.VISIBLE);
				if (dep.getCrowd() == -1) {
					pmr.setImageLevel(0);
				}
				else if (dep.getCrowd() < 4) {
					pmr.setImageLevel(1);
				}
				else if (dep.getCrowd() < 7) {
					pmr.setImageLevel(2);
				}
				else {
					pmr.setImageLevel(3);
				}
			}		

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
