package ch.unige.tpgcrowd.ui.fragments;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.manager.ITPGThermometer;
import ch.unige.tpgcrowd.manager.TPGManager;
import ch.unige.tpgcrowd.model.Step;
import ch.unige.tpgcrowd.model.Stop;
import ch.unige.tpgcrowd.model.Thermometer;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;
import ch.unige.tpgcrowd.util.ColorStore;

public class ShowNextStopsFragment extends Fragment {
	
	public static final String EXTRA_DEP_CODE = "ch.unige.tpgcrowd.extra.DEP_CODE";
	protected static final String STRING_HOUR = "&gt;1h";
	public static final String EXTRA_LINE_CODE = "ch.unige.tpgcrowd.extra.LINE_CODE";
	public static final String EXTRA_DEST_NAME = "ch.unige.tpgcrowd.extra.DEST_CODE";
	public static final String EXTRA_STOP_CODE = "ch.unige.tpgcrowd.extra.STOP_CODE";
	
	private final Runnable updateNextStops = new Runnable() {
		
		@Override
		public void run() {
			requestNextStops();
		}
	};
	
	private final TPGObjectListener<Thermometer> thermListener = new TPGObjectListener<Thermometer>() {

		@Override
		public void onSuccess(final Thermometer results) {
			progres.setVisibility(View.GONE);
			steps = results.getRemainingSteps();
			
			final View noData = layout.findViewById(R.id.noData);
			final ImageButton sortCrowd = (ImageButton)layout.findViewById(R.id.sortCrowd);
			final ImageButton sortTime = (ImageButton)layout.findViewById(R.id.sortTime);
			if (steps != null) {
				Log.i("", steps.size() + "");
				final Step nonZero = results.getFirstNonZeroRemainingStep();
				if (nonZero != null) {
					final int next = nonZero.getArrivalTime();
					handler.postDelayed(updateNextStops, next * 60 * 1000);
					final TextView nextUpdate = (TextView)layout.findViewById(R.id.nextUpdateTime);
					final Resources res = getResources();
					nextUpdate.setText(res.getQuantityString(R.plurals.next_update, next, next));
				}
				noData.setVisibility(View.GONE);
				
				sortCrowd.setOnClickListener(crowdSortClick);
				sortCrowd.setBackgroundResource(0);
				sortTime.setOnClickListener(timeSortClick);
				sortTime.setBackgroundResource(R.drawable.background_selected);
				final StopListAdapter adapter = new StopListAdapter(steps);
				list.setAdapter(adapter);
				list.setVisibility(View.VISIBLE);
			}
			else {
				noData.setVisibility(View.VISIBLE);
				sortCrowd.setOnClickListener(null);
				sortCrowd.setBackgroundResource(0);
				sortTime.setOnClickListener(null);
				sortTime.setBackgroundResource(0);
			}
			
			final TextView update = (TextView)layout.findViewById(R.id.updateTime);
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
			requestNextStops();
			final TextView update = (TextView)getView().findViewById(R.id.updateTime);
			update.setText(R.string.loading);
		}
	};
	
	private final OnClickListener crowdSortClick = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			sortByCrowd(steps);
			final ImageButton sortCrowd = (ImageButton)layout.findViewById(R.id.sortCrowd);
			sortCrowd.setBackgroundResource(R.drawable.background_selected);
			final ImageButton sortTime = (ImageButton)layout.findViewById(R.id.sortTime);
			sortTime.setBackgroundResource(0);
			final StopListAdapter adapter = new StopListAdapter(steps);
			list.setAdapter(adapter);
		}
	};
	
	private final OnClickListener timeSortClick = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			sortByTime(steps);
			final ImageButton sortCrowd = (ImageButton)layout.findViewById(R.id.sortCrowd);
			sortCrowd.setBackgroundResource(0);
			final ImageButton sortTime = (ImageButton)layout.findViewById(R.id.sortTime);
			sortTime.setBackgroundResource(R.drawable.background_selected);
			final StopListAdapter adapter = new StopListAdapter(steps);
			list.setAdapter(adapter);
		}
	};
	
	private LinearLayout layout;
	private View progres;
	private ListView list;
	private List<Step> steps;
	private int departureCode;
	private String lineCode;
	private Handler handler;
	private String stopCode;
	private int currentStopPosition;
	
	@Override
	public void onStart() {
		super.onStart();
		final HandlerThread thread = new HandlerThread("UpdateNextStopsHandler");
		thread.start();
		handler = new Handler(thread.getLooper());
		
		requestNextStops();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = (LinearLayout)inflater.inflate(R.layout.show_line_next_stops, container, false);
		final Bundle b = getArguments();
		lineCode = b.getString(EXTRA_LINE_CODE);
		stopCode = b.getString(EXTRA_STOP_CODE);
		
		final LinearLayout lineInfo = (LinearLayout)layout.findViewById(R.id.lineInfo);
		final TextView lineIcon = (TextView)lineInfo.findViewById(R.id.lineIcon);
		lineIcon.setText(lineCode);
		lineIcon.setBackgroundColor(ColorStore.getColor(getActivity(), lineCode));
		final ImageButton refresh = (ImageButton)layout.findViewById(R.id.refresh);
		refresh.setOnClickListener(refreshClick);
		final TextView dir = (TextView)lineInfo.findViewById(R.id.direction);
		dir.setText(b.getString(EXTRA_DEST_NAME));
		
		progres = layout.findViewById(R.id.progres);
		list = (ListView)layout.findViewById(R.id.departuresList);
		
		departureCode = b.getInt(EXTRA_DEP_CODE);
		Log.d("dep", departureCode + " " + lineCode + " " + lineCode);
		
		return layout;
	}
	
	@Override
	public void onStop() {
		super.onStop();
		handler.getLooper().quit();
	}
	
	private void requestNextStops() {
		final ITPGThermometer therMan = TPGManager.getThermometerManager(getActivity());
		therMan.getThermometerPhisicalStops(departureCode, thermListener);
	}
	
	private class StopListAdapter extends ArrayAdapter<Step> {
		private final List<Step> steps;

		public StopListAdapter(final List<Step> steps) {
			super(getActivity(), R.layout.list_row_show_next_stop, steps);
			this.steps = steps;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final LayoutInflater inflater = (LayoutInflater) getActivity()
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View rowView = inflater.inflate(R.layout.list_row_show_next_stop, parent, false);
			final Step step = steps.get(position);
			final Stop stop = step.getStop();
			
			
			if (stop.getStopCode().equals(stopCode)) {
				currentStopPosition = position;
				rowView.setBackgroundResource(R.drawable.background_selected);
			}
			
			final TextView stopName = (TextView)rowView.findViewById(R.id.stopName);
			stopName.setText(step.getPhysicalStop().getStopName());
			
			final TextView time = (TextView)rowView.findViewById(R.id.time);
			time.setText(step.getArrivalTime() + "");
			
			final ImageView crowd = (ImageView)rowView.findViewById(R.id.crowd);
			crowd.setImageLevel(step.getPhysicalStop().getCrowd());
			
			return rowView;
		}
	}

	private static void sortByCrowd(final List<Step> steps) {
		Collections.sort(steps, new Comparator<Step>() {

			@Override
			public int compare(final Step lhs, final Step rhs) {
				return lhs.getPhysicalStop().getCrowd() - rhs.getPhysicalStop().getCrowd();
			}
		});
	}
	
	private static void sortByTime(final List<Step> steps) {
		Collections.sort(steps, new Comparator<Step>() {

			@Override
			public int compare(final Step lhs, final Step rhs) {
				return lhs.getArrivalTime() - rhs.getArrivalTime();
			}
		});
	}
}
