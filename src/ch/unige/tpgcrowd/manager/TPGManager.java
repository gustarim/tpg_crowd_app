package ch.unige.tpgcrowd.manager;

import android.content.Context;
import ch.unige.tpgcrowd.manager.impl.TPGDeparturesImpl;
import ch.unige.tpgcrowd.manager.impl.TPGStopsImpl;

public final class TPGManager {

	private static ITPGStops tpgStopsManager;
	
	private static ITPGDepartures tpgDeparturesManager;
	
	public static ITPGStops getStopsManager(Context context) {
		if (tpgStopsManager == null) {
			tpgStopsManager = new TPGStopsImpl(context);
		}
		
		return tpgStopsManager;
	}
	
	public static ITPGStops getDeparturesManager(Context context) {
		if (tpgDeparturesManager == null) {
			tpgDeparturesManager = new TPGDeparturesImpl(context);
		}
		
		return tpgStopsManager;
	}
}
