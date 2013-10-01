package ch.unige.tpgcrowd.manager.impl;

import java.util.List;

import android.content.Context;

import ch.unige.tpgcrowd.manager.ITPGDepartures;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class TPGDeparturesImpl implements ITPGDepartures {

	private Context context;
	
	public TPGDeparturesImpl(Context context) {
		this.context = context;
	}

	@Override
	public void getNextDepartures(TPGObjectListener<Void> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getNextDepartures(String stopCode, Integer departureCode,
			TPGObjectListener<Void> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getNextDepartures(String stopCode, Integer departureCode,
			List<String> linesCode, List<String> destinationsCode,
			TPGObjectListener<Void> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllNextDepartures(String stopCode, String lineCode,
			String destinationCode, TPGObjectListener<Void> listener) {
		// TODO Auto-generated method stub
		
	}

}
