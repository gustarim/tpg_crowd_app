package ch.unige.tpgcrowd.ui.fragments;

import android.app.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InitialMapFragment extends SupportMapFragment {
	public interface MapEventListener {
		public void onLongClick(double latitude, double longitude);
	}

	private MapEventListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//default location
		double latitude = 46.2022200;
		double longitude = 6.1456900;
		

		setLocation(latitude, longitude, -1);
		
		this.getMap().setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng point) {
				listener.onLongClick(point.latitude, point.longitude);
				
				final MarkerOptions mo = new MarkerOptions();
				mo.position(point);
				
				getMap().clear();				
				getMap().addMarker(mo);
			}
		});
	}
	
	public void setLocation(double latitude, double longitude, double accuracy) {
		float zoom;
		
		if (accuracy > -1) {
			zoom = 13.f;
		}else{
			zoom = 16.f;
		}
		
		final LatLng ll = new LatLng(latitude, longitude);
		this.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(ll, zoom));
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			listener = (MapEventListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement MapEventListener");
		}
	}

	public void setLocationWithMarker(LatLng currentCenter,
			LatLng pressPosition, float zoom) {
		this.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(currentCenter, zoom));
		
		final MarkerOptions mo = new MarkerOptions();
		mo.position(pressPosition);
		
		getMap().clear();				
		getMap().addMarker(mo);
	}
}
