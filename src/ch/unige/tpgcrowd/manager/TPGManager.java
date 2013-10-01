package ch.unige.tpgcrowd.manager;

import android.content.Context;
import ch.unige.tpgcrowd.manager.impl.TPGDeparturesImpl;
import ch.unige.tpgcrowd.manager.impl.TPGDisruptionsImpl;
import ch.unige.tpgcrowd.manager.impl.TPGLinesColorsImpl;
import ch.unige.tpgcrowd.manager.impl.TPGStopsImpl;
import ch.unige.tpgcrowd.manager.impl.TPGThermometerImpl;

public final class TPGManager {

	private static ITPGDepartures tpgDeparturesManager;
	
	private static ITPGDisruptions tpgDisruptionsManager;
	
	private static ITPGLinesColors tpgLinesColorsManager;

	private static ITPGStops tpgStopsManager;	

	private static ITPGThermometer tpgThermometerManager;
		
	/**
	 * Method called to get the Departures Manager
	 * 
	 * @param context	Application context
	 * @return			The Departures manager
	 */
	public static ITPGDepartures getDeparturesManager(Context context) {
		if (tpgDeparturesManager == null) {
			tpgDeparturesManager = new TPGDeparturesImpl(context);
		}
		
		return tpgDeparturesManager;
	}
	
	/**
	 * Method called to get the Disruptions Manager
	 * 
	 * @param context	Application context
	 * @return			The Disruptions manager
	 */
	public static ITPGDisruptions getDisruptionsManager(Context context) {
		if (tpgDisruptionsManager == null) {
			tpgDisruptionsManager = new TPGDisruptionsImpl(context);
		}
		
		return tpgDisruptionsManager;
	}
	
	/**
	 * Method called to get the Disruptions Manager
	 * 
	 * @param context	Application context
	 * @return			The Disruptions manager
	 */
	public static ITPGLinesColors getLinesColorsManager(Context context) {
		if (tpgLinesColorsManager == null) {
			tpgLinesColorsManager = new TPGLinesColorsImpl(context);
		}
		
		return tpgLinesColorsManager;
	}
	
	/**
	 * Method called to get the Stops Manager
	 * 
	 * @param context	Application context
	 * @return			The Stops manager
	 */
	public static ITPGStops getStopsManager(Context context) {
		if (tpgStopsManager == null) {
			tpgStopsManager = new TPGStopsImpl(context);
		}
		
		return tpgStopsManager;
	}
	

	
	/**
	 * Method called to get the Thermometer Manager
	 * 
	 * @param context	Application context
	 * @return			The Thermometer manager
	 */
	public static ITPGThermometer getThermometerManager(Context context) {
		if (tpgThermometerManager == null) {
			tpgThermometerManager = new TPGThermometerImpl(context);
		}
		
		return tpgThermometerManager;
	}
	

}
