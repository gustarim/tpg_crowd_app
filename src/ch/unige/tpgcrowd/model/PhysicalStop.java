package ch.unige.tpgcrowd.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PhysicalStop {

	@JsonProperty("physicalStopCode")
	String stopCode;
	
	@JsonProperty("stopName")
	String stopName;
	
	@JsonProperty("connections")
	List<Connection> connections;
	
	@JsonProperty("coordinates")
	Coordinates coordinates;

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

	public List<Connection> getConnections() {
		return connections;
	}

	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

}
