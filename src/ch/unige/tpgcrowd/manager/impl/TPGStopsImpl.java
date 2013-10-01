package ch.unige.tpgcrowd.manager.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.unige.tpgcrowd.manager.ITPGStops;
import ch.unige.tpgcrowd.model.StopList;
import ch.unige.tpgcrowd.net.RequestQueueFactory;
import ch.unige.tpgcrowd.net.TpgJsonObjectRequest;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class TPGStopsImpl implements ITPGStops {
	
	private static final String STOPCODE_PARAM = "stopCode=";
	private static final String STOPNAME_PARAM = "stopName=";
	private static final String LINE_PARAM = "lineCode=";
	private static final String LATITUDE_PARAM = "latitude=";
	private static final String LONGITUDE_PARAM = "longitude=";

	private Context context;
	private RequestQueue requestQueue;

	private ObjectMapper objectMapper;

	public TPGStopsImpl(Context context) {
		this.context = context;
		this.requestQueue = RequestQueueFactory.getRequestQueue(context);
		objectMapper = new ObjectMapper();
	}

	@Override
	public void getAllStops(TPGObjectListener<StopList> listener) {
		addTpgStopsRequest("", listener);
	}

	@Override
	public void getStopsByCodes(List<String> stopCodes,
			TPGObjectListener<StopList> listener) {

		if (stopCodes.isEmpty()) {
			listener.onFailure();
		}
		else {
			String arguments = STOPCODE_PARAM + StringUtils.join(stopCodes, ",");
			Log.d("TPGStopsImpl", "arguments : " + arguments);
			addTpgStopsRequest(arguments, listener);
		}
	}

	@Override
	public void getStopsByName(String stopName,
			TPGObjectListener<StopList> listener) {

		if (stopName.isEmpty()) {
			listener.onFailure();
		}
		else {
			String arguments = STOPNAME_PARAM + stopName;
			addTpgStopsRequest(arguments, listener);
		}

	}

	@Override
	public void getStopsByLine(String line, TPGObjectListener<StopList> listener) {
		if (line.isEmpty()) {
			listener.onFailure();
		}
		else {
			String arguments = LINE_PARAM + line;
			addTpgStopsRequest(arguments, listener);
		}
	}

	@Override
	public void getStopsByPosition(Double lat, Double lon,
			TPGObjectListener<StopList> listener) {
		if (lat != null && lon != null) {
			listener.onFailure();
		}
		else {
			String arguments = LATITUDE_PARAM + lat + "&" + LONGITUDE_PARAM + lon;
			addTpgStopsRequest(arguments, listener);
		}
	}

	@Override
	public void getAllPhysicalStops(TPGObjectListener<StopList> listener) {
		addTpgPhysicalStopsRequest("", listener);
	}

	@Override
	public void getPhysicalStopsByCodes(List<String> stopCodes,
			TPGObjectListener<StopList> listener) {

		if (stopCodes.isEmpty()) {
			listener.onFailure();
		}
		else {
			String arguments = STOPCODE_PARAM + StringUtils.join(stopCodes, ",");
			addTpgPhysicalStopsRequest(arguments, listener);
		}

	}

	@Override
	public void getPhysicalStopsByName(String name,
			TPGObjectListener<StopList> listener) {

		if (name.isEmpty()) {
			listener.onFailure();
		}
		else {
			String arguments = STOPNAME_PARAM + name;
			addTpgPhysicalStopsRequest(arguments, listener);
		}

	}

	private void addTpgStopsRequest(String arguments, TPGObjectListener<StopList> listener) {

		TpgJsonObjectRequest req = new TpgJsonObjectRequest(TpgJsonObjectRequest.METHOD_GET_STOPS, arguments, createSuccessListener(listener), createResultListener(listener));
		requestQueue.add(req);
	}

	private void addTpgPhysicalStopsRequest(String arguments, TPGObjectListener<StopList> listener) {

		TpgJsonObjectRequest req = new TpgJsonObjectRequest(TpgJsonObjectRequest.METHOD_GET_PHYSICAL_STOPS, arguments, createSuccessListener(listener), createResultListener(listener));
		requestQueue.add(req);
	}

	
	private ErrorListener createResultListener(
			final TPGObjectListener<StopList> listener) {
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				listener.onFailure();
				//TODO parse the error and send the good code / message or whatever to the listener
			}
		};
	}

	private Listener<JSONObject> createSuccessListener(
			final TPGObjectListener<StopList> listener) {
		return new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					StopList result = objectMapper.readValue(response.toString(), StopList.class);
					listener.onSuccess(result);

				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listener.onFailure();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listener.onFailure();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listener.onFailure();
				}				
			}
		};
	}
}
