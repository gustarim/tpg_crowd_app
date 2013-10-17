package ch.unige.tpgcrowd.manager;

import java.util.List;

import ch.unige.tpgcrowd.model.StopList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public interface ITPGStops {

	//Get stops methods
	/**
	 * Return all stops for the exploitation day
	 * 
	 * @param listener		listener called on result
	 */
	public void getAllStops(final TPGObjectListener<StopList> listener);

	/**
	 * Return the list of all stops with code in stopCodes
	 * 
	 * @param stopCodes		list of stop codes
	 * @param listener		success listener
	 * @param errorListener	error listener
	 */
	public void getStopsByCodes(List<String> stopCodes, final TPGObjectListener<StopList> listener);
	
	/**
	 * Return the list of all stops with name containing the stopName string
	 * 
	 * @param stopName		name of the stop (can be a part of the name)
	 * @param listener		success listener
	 * @param errorListener	error listener
	 */
	public void getStopsByName(String stopName, final TPGObjectListener<StopList> listener);

	/**
	 * Return the list of all stops for a specified line
	 * 
	 * @param line
	 * @param listener
	 * @param errorListener
	 */
	public void getStopsByLine(String line, final TPGObjectListener<StopList> listener);

	/**
	 * Return the list of all stops near the position (within 500m)
	 * 
	 * @param lat
	 * @param lon
	 * @param listener
	 * @param errorListener
	 */
	public void getStopsByPosition(Double lat, Double lon, final TPGObjectListener<StopList> listener);
	
	/**
	 * Return all physical stops for the exploitation day
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void getAllPhysicalStops(final TPGObjectListener<StopList> listener);
	
	/**
	 * Return the list of all physical stops with code in stopCodes
	 * 
	 * @param stopCodes
	 * @param listener
	 * @param errorListener
	 */
	public void getPhysicalStopByCode(String stopCode, final TPGObjectListener<StopList> listener);
	
	/**
	 * Return the list of all physical stops with code in stopCodes
	 * 
	 * @param stopCodes
	 * @param listener
	 * @param errorListener
	 */
	public void getPhysicalStopsByCodes(List<String> stopCodes, final TPGObjectListener<StopList> listener);
	
	/**
	 * Return the list of all physical stops with name containing the stopName string
	 * 
	 * @param name
	 * @param listener
	 * @param errorListener
	 */
	public void getPhysicalStopsByName(String name, final TPGObjectListener<StopList> listener);
}
