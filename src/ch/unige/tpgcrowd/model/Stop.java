package ch.unige.tpgcrowd.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Stop {
	
	@JsonProperty("stopCode")
	String stopCode;
	
	@JsonProperty("stopName")
	String stopName;
	
	@JsonProperty("distance")
	Integer distance;
	
	@JsonProperty("connections")
	List<Connection> connections;
	
	@JsonProperty("physicalStops")
	List<PhysicalStop> physicalStops;

	public String getStopCode() {
		return stopCode;
	}

	public void setStopCode(String stopCode) {
		this.stopCode = stopCode;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public List<Connection> getConnections() {
		return connections;
	}

	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	public List<PhysicalStop> getPhysicalStops() {
		return physicalStops;
	}

	public void setPhysicalStops(List<PhysicalStop> physicalStops) {
		this.physicalStops = physicalStops;
	}
	
	
}
