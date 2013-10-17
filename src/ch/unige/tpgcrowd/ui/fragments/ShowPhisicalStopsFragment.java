package ch.unige.tpgcrowd.ui.fragments;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.manager.ITPGStops;
import ch.unige.tpgcrowd.manager.TPGManager;
import ch.unige.tpgcrowd.model.Connection;
import ch.unige.tpgcrowd.model.Stop;
import ch.unige.tpgcrowd.model.StopList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class ShowPhisicalStopsFragment extends Fragment {
	private final TPGObjectListener<StopList> stopsListener = new TPGObjectListener<StopList>() {
		
		@Override
		public void onSuccess(final StopList results) {
			stops = results.getStops();
			 Log.d("TPG", "" + stops.get(0));
			progres.setVisibility(View.GONE);
			
			final FragmentManager fm = getFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();
			
			for (final Stop stop : stops) {
	            final ShowConnectionListFragment connListFrag = new ShowConnectionListFragment();
	            final Bundle arguments = new Bundle();
	            final LinkedList<Connection> connections = new LinkedList<Connection>();
	            connections.addAll(stop.getConnections());
	            Log.d("TPG", "" + connections.getFirst());
	            arguments.putSerializable(ShowConnectionListFragment.EXTRA_CONNECTIONS, connections);
	            connListFrag.setArguments(arguments);
	            ft.add(connListFrag, stop.getStopCode());
			}
			ft.commit();
		}
		
		@Override
		public void onFailure() {
			progres.setText(R.string.error);
		}
	};
	private TextView progres;
	private List<Stop> stops;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		final View layout = inflater.inflate(R.layout.show_phisical_stops, container);
		progres = (TextView)layout.findViewById(R.id.progres);
		return layout;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		final ITPGStops phisicalStops = TPGManager.getStopsManager(getActivity());
		phisicalStops.getPhysicalStopByCode("CVIN", stopsListener);
	}
}
