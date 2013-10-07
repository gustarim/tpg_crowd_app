package ch.unige.tpgcrowd.manager.impl;

import android.content.Context;
import ch.unige.tpgcrowd.manager.ITPGDisruptions;
import ch.unige.tpgcrowd.model.DisruptionList;
import ch.unige.tpgcrowd.model.ITPGModelEntity;
import ch.unige.tpgcrowd.net.TpgJsonObjectRequest;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class TPGDisruptionsImpl extends TPGAbstractImpl implements ITPGDisruptions {

	public TPGDisruptionsImpl(final Context context) {
		super(context);
	}
	 
	@Override
	public void getDisruption(final TPGObjectListener<DisruptionList> listener) {
		addRequest(TpgJsonObjectRequest.METHOD_GET_DISRUPTIONS, EMPTY_ARGS, listener);
	}
	
	@Override
	protected Class<? extends ITPGModelEntity> getResponseClass() {
		return DisruptionList.class;
	}
}
