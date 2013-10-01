package ch.unige.tpgcrowd.manager.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.Context;

import ch.unige.tpgcrowd.manager.ITPGDepartures;
import ch.unige.tpgcrowd.model.DepartureList;
import ch.unige.tpgcrowd.net.RequestQueueFactory;
import ch.unige.tpgcrowd.net.TpgJsonObjectRequest;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class TPGDeparturesImpl implements ITPGDepartures {

	private static final String STOPCODE_PARAM = "stopCode=";
	private static final String DEPARTURECODE_PARAM = "departureCode=";
	private static final String LINECODE_SIMPLE_PARAM = "lineCode=";
	private static final String LINECODE_MULTI_PARAM = "linesCode=";
	private static final String DESTCODE_SIMPLE_PARAM = "destinationCode=";
	private static final String DESTCODE_MULTI_PARAM = "destinationsCode=";

	private Context context;
	private RequestQueue requestQueue;

	private ObjectMapper objectMapper;

	public TPGDeparturesImpl(Context context) {
		this.context = context;
		this.requestQueue = RequestQueueFactory.getRequestQueue(context);
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void getNextDepartures(String stopCode, TPGObjectListener<DepartureList> listener) {
		if (stopCode.isEmpty()) {
			listener.onFailure();
		}
		else {
			String arguments = STOPCODE_PARAM + stopCode;
			addTpgDepaturesRequest(arguments, listener);
		}
	}

	@Override
	public void getNextDepartures(String stopCode, Integer departureCode, TPGObjectListener<DepartureList> listener) {
		if (stopCode.isEmpty() || departureCode == null) {
			listener.onFailure();
		}
		else {
			String arguments = STOPCODE_PARAM + stopCode;
			arguments += "&" + DEPARTURECODE_PARAM + departureCode;
			addTpgDepaturesRequest(arguments, listener);
		}
	}

	@Override
	public void getNextDepartures(String stopCode, Integer departureCode, List<String> linesCode, List<String> destinationsCode, TPGObjectListener<DepartureList> listener) {
		if (stopCode.isEmpty()) {
			listener.onFailure();
		}
		else {
			String arguments = STOPCODE_PARAM + stopCode;
			if (departureCode != null) {
				arguments += "&" + DEPARTURECODE_PARAM + departureCode;
			}
			if (!linesCode.isEmpty()) {
				arguments += "&" + LINECODE_MULTI_PARAM + StringUtils.join(linesCode, ",");
			}
			if (!destinationsCode.isEmpty()) {
				arguments += "&" + DESTCODE_MULTI_PARAM + StringUtils.join(destinationsCode, ",");
			}
			
			addTpgDepaturesRequest(arguments, listener);
		}
	}

	@Override
	public void getAllNextDepartures(String stopCode, String lineCode, String destinationCode, TPGObjectListener<DepartureList> listener) {
		if (stopCode.isEmpty() || lineCode.isEmpty() || destinationCode.isEmpty()) {
			listener.onFailure();
		}
		else {
			String arguments = STOPCODE_PARAM + stopCode;
			arguments += "&" + LINECODE_SIMPLE_PARAM + lineCode;
			arguments += "&" + DESTCODE_SIMPLE_PARAM + destinationCode;

			addTpgAllDepaturesRequest(arguments, listener);		
		}

	}

	private void addTpgAllDepaturesRequest(String arguments, TPGObjectListener<DepartureList> listener) {

		TpgJsonObjectRequest req = new TpgJsonObjectRequest(TpgJsonObjectRequest.METHOD_GET_ALL_DEPARTURES, arguments, createSuccessListener(listener), createErrorListener(listener));
		requestQueue.add(req);

	}

	private void addTpgDepaturesRequest(String arguments, TPGObjectListener<DepartureList> listener) {

		TpgJsonObjectRequest req = new TpgJsonObjectRequest(TpgJsonObjectRequest.METHOD_GET_DEPARTURES, arguments, createSuccessListener(listener), createErrorListener(listener));
		requestQueue.add(req);
	}


	private ErrorListener createErrorListener(final TPGObjectListener<DepartureList> listener) {
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				listener.onFailure();
				//TODO parse the error and send the good code / message or whatever to the listener
			}
		};
	}

	private Listener<JSONObject> createSuccessListener(final TPGObjectListener<DepartureList> listener) {
		return new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					DepartureList result = objectMapper.readValue(response.toString(), DepartureList.class);
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
