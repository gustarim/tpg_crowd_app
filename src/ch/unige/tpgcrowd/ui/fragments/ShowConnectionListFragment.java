package ch.unige.tpgcrowd.ui.fragments;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.unige.tpgcrowd.R;
import ch.unige.tpgcrowd.model.Connection;
import ch.unige.tpgcrowd.util.ColorStore;

public class ShowConnectionListFragment extends ListFragment {
	public static final String EXTRA_CONNECTIONS = "ch.unige.tpg.extra.CONNECTIONS";
	
	@Override
	public void onStart() {
		super.onStart();
		final Bundle b = getArguments();
		final List<Connection> connections = (List<Connection>)b.getSerializable(EXTRA_CONNECTIONS);
		final PhisicalStopsAdapter adapter = new PhisicalStopsAdapter(getActivity(), connections);
		setListAdapter(adapter);
	}

	private class PhisicalStopsAdapter extends ArrayAdapter<Connection> {
		private final Context context;
		private final List<Connection> connections;

		public PhisicalStopsAdapter(final Context context,
				final List<Connection> connections) {
			super(context, R.layout.list_row_show_connection, connections);
			this.context = context;
			this.connections = connections;
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			final LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View rowView = inflater.inflate(R.layout.list_row_show_connection, parent, false);
			final Connection conn = connections.get(position);
			final TextView lineIcon = (TextView)rowView.findViewById(R.id.lineIcon);
			lineIcon.setText(conn.getLineCode());
			lineIcon.setBackgroundColor(ColorStore.getColor(getContext(), conn.getLineCode()));
			final TextView destination = (TextView)rowView.findViewById(R.id.direction);
			destination.setText(conn.getDestinationName());
			return rowView;
		}
		
	}
}
