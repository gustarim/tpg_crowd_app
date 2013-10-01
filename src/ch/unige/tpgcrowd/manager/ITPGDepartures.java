package ch.unige.tpgcrowd.manager;

import java.util.List;

import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public interface ITPGDepartures {

	/**
	 * Return the list of all departures in the next hour
	 * 
	 * @param listener			listener called on result
	 */
	public void getNextDepartures(final TPGObjectListener<Void> listener);

	/**
	 * Return the list of all departures in the next hour for the specified stop code
	 * 
	 * @param stopCode			the stop code
	 * @param departureCode		timecode (for connection only, can be null)
	 * @param listener			listener called on result
	 */
	public void getNextDepartures(String stopCode, Integer departureCode, final TPGObjectListener<Void> listener);

	/**
	 * Return the list of all departures in the next hour for the specified stop code, filtered by lines and destination codes
	 * 
	 * @param stopCode			the stop code
	 * @param departureCode		timecode (for connection only, can be null)
	 * @param linesCode			lines code filter
	 * @param destinationsCode	destination code filter
	 * @param listener			listener called on result
	 */
	public void getNextDepartures(String stopCode, Integer departureCode, List<String> linesCode, List<String> destinationsCode, final TPGObjectListener<Void> listener);
	
	/**
	 * Return the list of all departures for the day
	 * 
	 * @param stopCode			the stop code	
	 * @param lineCode			the line code
	 * @param destinationCode	the destination code
	 * @param listener			listener called on result
	 */
	public void getAllNextDepartures(String stopCode, String lineCode, String destinationCode, final TPGObjectListener<Void> listener);
}
