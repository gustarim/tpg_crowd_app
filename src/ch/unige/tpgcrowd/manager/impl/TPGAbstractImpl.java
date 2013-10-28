package ch.unige.tpgcrowd.manager.impl;

import java.io.IOException;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import ch.unige.tpgcrowd.model.ITPGModelEntity;
import ch.unige.tpgcrowd.net.RequestQueueFactory;
import ch.unige.tpgcrowd.net.TpgJsonObjectRequest;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class TPGAbstractImpl {
	protected static final String STOPCODE_PARAM = "stopCode=";
	protected static final String STOPNAME_PARAM = "stopName=";
	protected static final String LINE_PARAM = "lineCode=";
	protected static final String LATITUDE_PARAM = "latitude=";
	protected static final String LONGITUDE_PARAM = "longitude=";
	
	protected static final String DEPARTURECODE_PARAM = "departureCode=";
	protected static final String LINECODE_SIMPLE_PARAM = "lineCode=";
	protected static final String LINECODE_MULTI_PARAM = "linesCode=";
	protected static final String DESTCODE_SIMPLE_PARAM = "destinationCode=";
	protected static final String DESTCODE_MULTI_PARAM = "destinationsCode=";
	
	protected static final String EMPTY_ARGS = "";
	
	private final Context context;
	private final RequestQueue requestQueue;
	private final ObjectMapper objectMapper;
	
	public TPGAbstractImpl(final Context context) {
		this.context = context;
		this.requestQueue = RequestQueueFactory.getRequestQueue(context);
		this.objectMapper = new ObjectMapper();
	}
	
	protected abstract Class<? extends ITPGModelEntity> getResponseClass();
	
	protected void addRequest(final String method, final String arguments, final TPGObjectListener<? extends ITPGModelEntity> listener) {
		final String encodedArguments = arguments.replaceAll(" ", "%20");
		final TpgJsonObjectRequest req = new TpgJsonObjectRequest(method, encodedArguments, createSuccessListener(listener), createErrorListener(listener));
		Log.d("Volley", req.toString());
		requestQueue.add(req);
	}
	
	protected ErrorListener createErrorListener(final TPGObjectListener<? extends ITPGModelEntity> listener) {
		return new ErrorListener() {
			@Override
			public void onErrorResponse(final VolleyError error) {
				listener.onFailure();
				//TODO parse the error and send the good code / message or whatever to the listener
			}
		};
	}

	protected <T extends ITPGModelEntity> Listener<JSONObject> createSuccessListener(final TPGObjectListener<T> listener) {
		return new Listener<JSONObject>() {
			@Override
			public void onResponse(final JSONObject response) {
				try {
					listener.onSuccess((T) objectMapper.readValue(response.toString() /*.getBytes(Charset.forName("ISO-8859-1")) */, getResponseClass()));

				} catch (final JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listener.onFailure();
				} catch (final JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listener.onFailure();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listener.onFailure();
				}				
			}
		};
	}
}
