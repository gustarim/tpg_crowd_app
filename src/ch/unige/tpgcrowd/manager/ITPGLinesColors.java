package ch.unige.tpgcrowd.manager;

import ch.unige.tpgcrowd.model.LineColorList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public interface ITPGLinesColors {

	/**
	 * Return the colors of all lines
	 * 
	 * @param listener		listener called on result
	 */
	public void getLinesColors(final TPGObjectListener<LineColorList> listener);
}
