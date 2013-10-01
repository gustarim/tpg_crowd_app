package ch.unige.tpgcrowd.manager;

import ch.unige.tpgcrowd.model.DisruptionList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public interface ITPGDisruptions {

	/**
	 * Return the list of all disruptions in the network
	 * 
	 * @param listener		listener called on result
	 */
	public void getDisruption(final TPGObjectListener<DisruptionList> listener);
	
}
