package ch.unige.tpgcrowd.manager.impl;

import java.io.IOException;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.unige.tpgcrowd.manager.ITPGThermometer;
import ch.unige.tpgcrowd.model.Thermometer;
import ch.unige.tpgcrowd.net.RequestQueueFactory;
import ch.unige.tpgcrowd.net.TpgJsonObjectRequest;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class TPGThermometerImpl implements ITPGThermometer {

	private static final String DEPARTURECODE_PARAM = "departureCode=";

	private Context context;
	private RequestQueue requestQueue;

	private ObjectMapper objectMapper;

	public TPGThermometerImpl(Context context) {
		this.context = context;
		this.requestQueue = RequestQueueFactory.getRequestQueue(context);
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void getThermometer(Integer departureCode, TPGObjectListener<Thermometer> listener) {
		
		if (departureCode == null) {
			listener.onFailure();
		}
		else {
			String arguments = DEPARTURECODE_PARAM + departureCode;
			TpgJsonObjectRequest req = new TpgJsonObjectRequest(TpgJsonObjectRequest.METHOD_GET_THERMO, arguments, createSuccessListener(listener), createResultListener(listener));
			requestQueue.add(req);
		}

	}

	private ErrorListener createResultListener(final TPGObjectListener<Thermometer> listener) {
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				listener.onFailure();
				//TODO parse the error and send the good code / message or whatever to the listener
			}
		};
	}

	private Listener<JSONObject> createSuccessListener(final TPGObjectListener<Thermometer> listener) {
		return new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					Thermometer result = objectMapper.readValue(response.toString(), Thermometer.class);
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
