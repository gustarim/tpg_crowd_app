package ch.unige.tpgcrowd.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * @author Gustarini
 * Helper class to create a singleton RequestQueue
 */
public final class RequestQueueFactory {
	private static RequestQueue requestQueue;
	
	/**
	 * Method to get the RequestQueue singleton
	 * @param context - the current activity or service that needs the request queue
	 * @return a RequestQueue
	 */
	public static RequestQueue getRequestQueue(final Context context) {
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(context);
		}
		return requestQueue;
	}
}
