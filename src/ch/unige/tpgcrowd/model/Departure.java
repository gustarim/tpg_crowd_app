package ch.unige.tpgcrowd.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Departure {

	@JsonProperty("departureCode")
	Integer departureCode;
	
	@JsonProperty("waitingTime")
	Integer waitingTime;
	
	@JsonProperty("waitingTimeMillis")
	Integer waitingTimeMillis;
	
	@JsonProperty("connectionWaitingTime")
	Integer connectionWaitingTime;
	
	@JsonProperty("connection")
	Connection connection;
	
	@JsonProperty("reliability")
	String reliability;
	
	@JsonProperty("characteristics")
	String characteristics;

	@JsonProperty("deviation")
	Deviation deviation;
	
	@JsonProperty("disruptions")
	List<Disruption> disruptions;
	
	public Integer getDepartureCode() {
		return departureCode;
	}

	public void setDepartureCode(Integer departureCode) {
		this.departureCode = departureCode;
	}

	public Integer getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Integer waitingTime) {
		this.waitingTime = waitingTime;
	}

	public Integer getWaitingTimeMillis() {
		return waitingTimeMillis;
	}

	public void setWaitingTimeMillis(Integer waitingTimeMillis) {
		this.waitingTimeMillis = waitingTimeMillis;
	}

	public Integer getConnectionWaitingTime() {
		return connectionWaitingTime;
	}

	public void setConnectionWaitingTime(Integer connectionWaitingTime) {
		this.connectionWaitingTime = connectionWaitingTime;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getReliability() {
		return reliability;
	}

	public void setReliability(String reliability) {
		this.reliability = reliability;
	}

	public String getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}

	public Deviation getDeviation() {
		return deviation;
	}

	public void setDeviation(Deviation deviation) {
		this.deviation = deviation;
	}

	public List<Disruption> getDisruptions() {
		return disruptions;
	}

	public void setDisruptions(List<Disruption> disruptions) {
		this.disruptions = disruptions;
	}
	
}
