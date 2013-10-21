package ch.unige.tpgcrowd.ui.fragments;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.manager.ITPGStops;
import ch.unige.tpgcrowd.manager.TPGManager;
import ch.unige.tpgcrowd.model.PhysicalStop;
import ch.unige.tpgcrowd.model.Stop;
import ch.unige.tpgcrowd.model.StopList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;
import ch.unige.tpgcrowd.ui.fragments.ShowLinesFragment.OnLinesMapClickListener;
import ch.unige.tpgcrowd.ui.fragments.ShowLinesMapFragment.OnLinesClickListener;

public class ShowStopFragment extends Fragment 
	implements OnLinesMapClickListener, OnLinesClickListener {
	public static final String TAG = "stop";
	public static final String EXTRA_STOP_CODE = "ch.unige.tpgcrowd.extra.STOP_CODE";
	public static final String EXTRA_STOP_NAME = "ch.unige.tpgcrowd.extra.STOP_NAME";
//	private static final String LINE_MAP_FRAGMENT = "linemap";
	private ShowLinesFragment slf;
	private ShowLinesMapFragment slmf;
	private LinkedList<PhysicalStopRender> renders;
	public interface PhysicalStopRender {
		public void setPhysicalStops(final List<PhysicalStop> stops);
		public void showError();
	}
	
	private final TPGObjectListener<StopList> stopsListener = new TPGObjectListener<StopList>() {
		
		@Override
		public void onSuccess(final StopList results) {
			final List<Stop> stops = results.getStops();
			if (stops != null && !stops.isEmpty()) {
				final Stop stop = results.getStops().get(0);
				final List<PhysicalStop> phyStops = stop.getPhysicalStops();
				for (final PhysicalStopRender rend : renders) {
					rend.setPhysicalStops(phyStops);
				}
			}
		}
		
		@Override
		public void onFailure() {
			for (final PhysicalStopRender rend : renders) {
				rend.showError();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View layout = inflater.inflate(R.layout.show_stop, container, false);
		final Bundle b = getArguments();
		final TextView name = (TextView)layout.findViewById(R.id.stop);
		name.setText(b.getString(EXTRA_STOP_NAME));
		final FragmentManager fm = getFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		slf = new ShowLinesFragment();
		ft.add(R.id.lineMapFragment, slf);
		slmf = new ShowLinesMapFragment();
		ft.add(R.id.lineMapFragment, slmf);
		ft.hide(slmf);
		ft.commit();
		renders = new LinkedList<ShowStopFragment.PhysicalStopRender>();
		renders.add(slf);
		renders.add(slmf);
		final String stopCode = b.getString(EXTRA_STOP_CODE);
		final ITPGStops phisicalStops = TPGManager.getStopsManager(getActivity());
		phisicalStops.getPhysicalStopByCode(stopCode, stopsListener);
		return layout;
	}

	@Override
	public void onMapClick() {
		final FragmentManager fm = getFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		ft.hide(slf);
		ft.show(slmf);
		ft.commit();
	}

	@Override
	public void onLinesClick() {
		final FragmentManager fm = getFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();
		ft.hide(slmf);
		ft.show(slf);
		ft.commit();
	}
}
