package ch.unige.tpgcrowd.manager;

import ch.unige.tpgcrowd.model.Thermometer;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public interface ITPGThermometer {

	/**
	 * Return the "thermometer" for the specified departureCode, i.e. the description of all steps for the line
	 * A thermometer contains also disruption and deviation data if any.
	 * 
	 * @param departureCode
	 * @param listener
	 */
	public void getThermometer(Integer departureCode, TPGObjectListener<Thermometer> listener);
	
	/**
	 * Return the "thermometer" for the specified departureCode, i.e. the description of all steps for the line
	 * A thermometer contains also disruption and deviation data if any.
	 * 
	 * @param departureCode
	 * @param listener
	 */
	public void getThermometerPhisicalStops(Integer departureCode, TPGObjectListener<Thermometer> listener);
}
