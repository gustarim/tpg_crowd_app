package ch.unige.tpgcrowd.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.manager.ITPGDepartures;
import ch.unige.tpgcrowd.manager.TPGManager;
import ch.unige.tpgcrowd.model.Departure;
import ch.unige.tpgcrowd.model.DepartureList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class ShowLinesFragment extends Fragment {
	
	
	private TextView tempDisplay;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		final View layout = inflater.inflate(R.layout.fragment_show_lines, container);
		tempDisplay = (TextView)layout.findViewById(R.id.tempShowLines);
		return layout;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		final ITPGDepartures nextDepartures = TPGManager.getDeparturesManager(getActivity());
		nextDepartures.getNextDepartures("CVIN", new TPGObjectListener<DepartureList>() {
			
			@Override
			public void onSuccess(final DepartureList results) {
				String resultText = results.getTimestamp() + " - ";
				for (final Departure dep : results.getDepartures()) {
					resultText += dep.getLine().getDestinationName() + "\n";
				}
				tempDisplay.setText(resultText);
			}
			
			@Override
			public void onFailure() {
				tempDisplay.setText("Failed");
			}
		});
	}

}
