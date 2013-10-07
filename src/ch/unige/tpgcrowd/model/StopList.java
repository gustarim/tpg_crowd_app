package ch.unige.tpgcrowd.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StopList implements ITPGModelEntity {

	@JsonProperty("timestamp")
	Date timestamp;
	
	@JsonProperty("stops")
	List<Stop> stops;

	@JsonProperty("physicalStops")
	List<PhysicalStop> physicalStops;
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<Stop> getStops() {
		return stops;
	}

	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	public List<PhysicalStop> getPhysicalStops() {
		return physicalStops;
	}

	public void setPhysicalStops(List<PhysicalStop> physicalStops) {
		this.physicalStops = physicalStops;
	}
	
}
