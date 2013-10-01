package ch.unige.tpgcrowd.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinates {

	@JsonProperty("referential")
	String referential;
	
	@JsonProperty("latitude")
	Double latitude;
	
	@JsonProperty("longitude")
	Double longitude;

	public String getReferential() {
		return referential;
	}

	public void setReferential(String referential) {
		this.referential = referential;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
