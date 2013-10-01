package ch.unige.tpgcrowd.manager.impl;

import java.io.IOException;

import org.json.JSONObject;

import android.content.Context;
import ch.unige.tpgcrowd.manager.ITPGLinesColors;
import ch.unige.tpgcrowd.model.LineColorList;
import ch.unige.tpgcrowd.net.RequestQueueFactory;
import ch.unige.tpgcrowd.net.TpgJsonObjectRequest;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TPGLinesColorsImpl implements ITPGLinesColors {

	private Context context;
	private RequestQueue requestQueue;
	private ObjectMapper objectMapper;

	public TPGLinesColorsImpl(Context context) {
		this.context = context;
		this.requestQueue = RequestQueueFactory.getRequestQueue(context);
		this.objectMapper = new ObjectMapper();
	}
	
	@Override
	public void getLinesColors(TPGObjectListener<LineColorList> listener) {
		addTpgLinesColorsRequest("", listener);
	}

	private void addTpgLinesColorsRequest(String arguments, TPGObjectListener<LineColorList> listener) {
		TpgJsonObjectRequest req = new TpgJsonObjectRequest(TpgJsonObjectRequest.METHOD_GET_LINES_COLORS, arguments, createSuccessListener(listener), createErrorListener(listener));
		requestQueue.add(req);
	}

	private ErrorListener createErrorListener(final TPGObjectListener<LineColorList> listener) {
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				listener.onFailure();
				//TODO parse the error and send the good code / message or whatever to the listener
			}
		};
	}

	private Listener<JSONObject> createSuccessListener(final TPGObjectListener<LineColorList> listener) {
		return new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					LineColorList result = objectMapper.readValue(response.toString(), LineColorList.class);
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
