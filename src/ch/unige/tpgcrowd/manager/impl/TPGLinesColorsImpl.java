package ch.unige.tpgcrowd.manager.impl;

import android.content.Context;
import ch.unige.tpgcrowd.manager.ITPGLinesColors;
import ch.unige.tpgcrowd.model.LineColorList;
import ch.unige.tpgcrowd.model.ITPGModelEntity;
import ch.unige.tpgcrowd.net.TpgJsonObjectRequest;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class TPGLinesColorsImpl extends TPGAbstractImpl implements ITPGLinesColors {

	public TPGLinesColorsImpl(final Context context) {
		super(context);
	}
	
	@Override
	public void getLinesColors(final TPGObjectListener<LineColorList> listener) {
		addRequest(TpgJsonObjectRequest.METHOD_GET_LINES_COLORS, EMPTY_ARGS, listener);
	}
	
	@Override
	protected Class<? extends ITPGModelEntity> getResponseClass() {
		return LineColorList.class;
	}
	
}
