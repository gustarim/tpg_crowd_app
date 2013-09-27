package ch.unige.tpgcrowd.net;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * @author Gustarini
 * A specific implementation of JsonObjectRequest to perform TPG API requests
 */
public class TpgJsonObjectRequest extends JsonObjectRequest {
	private static final String TPG_URL = "http://rtpi.data.tpg.ch/v1/";
	private static final String OUTPUT_FORMAT = ".json";
	private static final String TPG_KEY = "?key=" + TPGKey.KEY + "&";
	
	/*
	 * Methods and their arguments are explained in the document 
	 * about open data in the doc folder of the project
	 */
	public static final String METHOD_GET_STOPS = "GetStops";

	/**
	 * Creates a TpgJsonObjectRequest instance ready to be added to the request queue.
	 * @param action - the method to call in the TPG API (default values are listed as static String in this class TpgJsonObjectRequest.METHOD_)
	 * @param arguments - the arguments to the method formatted like <argument_name>=<argument_value>&<argument_name>=<argument_value>...
	 * @param listener - the listener for the complete request
	 * @param errorListener - the listener for errors during request
	 */
	public TpgJsonObjectRequest(final String action, final String arguments,
			final Listener<JSONObject> listener, final ErrorListener errorListener) {
		super(Request.Method.GET, TPG_URL + action + OUTPUT_FORMAT + TPG_KEY + arguments, null, listener, errorListener);
	}

}
