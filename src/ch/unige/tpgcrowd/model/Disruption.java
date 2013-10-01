package ch.unige.tpgcrowd.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Disruption {

	@JsonProperty("timestamp")
	Date timestamp;

	@JsonProperty("disruptionCode")
	Integer disruptionCode;

	@JsonProperty("place")
	String place;

	@JsonProperty("nature")
	String nature;

	@JsonProperty("stopName")
	String stopName;

	@JsonProperty("lineCode")
	String lineCode;

	@JsonProperty("consequence")
	String consequence;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getDisruptionCode() {
		return disruptionCode;
	}

	public void setDisruptionCode(Integer disruptionCode) {
		this.disruptionCode = disruptionCode;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public String getConsequence() {
		return consequence;
	}

	public void setConsequence(String consequence) {
		this.consequence = consequence;
	}
	
	

}
