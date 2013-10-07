package ch.unige.tpgcrowd.manager.impl;

import android.content.Context;
import ch.unige.tpgcrowd.manager.ITPGThermometer;
import ch.unige.tpgcrowd.model.ITPGModelEntity;
import ch.unige.tpgcrowd.model.Thermometer;
import ch.unige.tpgcrowd.net.TpgJsonObjectRequest;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class TPGThermometerImpl extends TPGAbstractImpl implements ITPGThermometer {

	public TPGThermometerImpl(final Context context) {
		super(context);
	}

	@Override
	public void getThermometer(final Integer departureCode, final TPGObjectListener<Thermometer> listener) {
		
		if (departureCode == null) {
			listener.onFailure();
		}
		else {
			final String arguments = DEPARTURECODE_PARAM + departureCode;
			addRequest(TpgJsonObjectRequest.METHOD_GET_THERMO, arguments, listener);
		}

	}
	
	@Override
	protected Class<? extends ITPGModelEntity> getResponseClass() {
		return Thermometer.class;
	}
	
}
