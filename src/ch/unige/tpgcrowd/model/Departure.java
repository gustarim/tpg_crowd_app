package ch.unige.tpgcrowd.model;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Departure {
	@JsonProperty("timestamp")
	Date timestamp;

	@JsonProperty("departureCode")
	Integer departureCode;
	
	@JsonProperty("waitingTime")
	String waitingTime;
	
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
	
	@JsonProperty("line")
	Line line;
	
	final int crowd;
	
	public Departure() {
		crowd = new Random().nextInt(5);
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getDepartureCode() {
		return departureCode;
	}

	public void setDepartureCode(Integer departureCode) {
		this.departureCode = departureCode;
	}

	public String getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(String waitingTime) {
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

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}
	
	public int getCrowd() {
		return crowd;
	}
	
}
