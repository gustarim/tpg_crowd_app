package ch.unige.tpgcrowd.manager.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import ch.unige.tpgcrowd.manager.ITPGDepartures;
import ch.unige.tpgcrowd.model.DepartureList;
import ch.unige.tpgcrowd.model.ITPGModelEntity;
import ch.unige.tpgcrowd.net.TpgJsonObjectRequest;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class TPGDeparturesImpl extends TPGAbstractImpl implements ITPGDepartures {

	public TPGDeparturesImpl(final Context context) {
		super(context);
	}

	@Override
	public void getNextDepartures(final String stopCode, final TPGObjectListener<DepartureList> listener) {
		if (stopCode.isEmpty()) {
			listener.onFailure();
		}
		else {
			final String arguments = STOPCODE_PARAM + stopCode;
			addRequest(TpgJsonObjectRequest.METHOD_GET_DEPARTURES, arguments, listener);
		}
	}

	@Override
	public void getNextDepartures(final String stopCode, final Integer departureCode, final TPGObjectListener<DepartureList> listener) {
		if (stopCode.isEmpty() || departureCode == null) {
			listener.onFailure();
		}
		else {
			String arguments = STOPCODE_PARAM + stopCode;
			arguments += "&" + DEPARTURECODE_PARAM + departureCode;
			addRequest(TpgJsonObjectRequest.METHOD_GET_DEPARTURES, arguments, listener);
		}
	}

	@Override
	public void getNextDepartures(final String stopCode, final Integer departureCode, 
			final List<String> linesCode, final List<String> destinationsCode, 
			final TPGObjectListener<DepartureList> listener) {
		if (stopCode.isEmpty()) {
			listener.onFailure();
		}
		else {
			String arguments = STOPCODE_PARAM + stopCode;
			if (departureCode != null) {
				arguments += "&" + DEPARTURECODE_PARAM + departureCode;
			}
			if (!linesCode.isEmpty()) {
				arguments += "&" + LINECODE_MULTI_PARAM + StringUtils.join(linesCode, ",");
			}
			if (!destinationsCode.isEmpty()) {
				arguments += "&" + DESTCODE_MULTI_PARAM + StringUtils.join(destinationsCode, ",");
			}
			
			addRequest(TpgJsonObjectRequest.METHOD_GET_DEPARTURES, arguments, listener);
		}
	}

	@Override
	public void getAllNextDepartures(final String stopCode, final String lineCode, 
			final String destinationCode, final TPGObjectListener<DepartureList> listener) {
		if (stopCode.isEmpty() || lineCode.isEmpty() || destinationCode.isEmpty()) {
			listener.onFailure();
		}
		else {
			String arguments = STOPCODE_PARAM + stopCode;
			arguments += "&" + LINECODE_SIMPLE_PARAM + lineCode;
			arguments += "&" + DESTCODE_SIMPLE_PARAM + destinationCode;
			
			addRequest(TpgJsonObjectRequest.METHOD_GET_ALL_DEPARTURES, arguments, listener);
		}

	}
	
	@Override
	protected Class<? extends ITPGModelEntity> getResponseClass() {
		return DepartureList.class;
	}
}
