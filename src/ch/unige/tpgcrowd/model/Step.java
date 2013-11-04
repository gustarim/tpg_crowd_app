package ch.unige.tpgcrowd.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Step {

	@JsonProperty("departureCode")
	Integer departureCode;
	
	@JsonProperty("timestamp")
	Date timestamp;
	
	@JsonProperty("stop")
	Stop stop;
	
	@JsonProperty("physicalStop")
	PhysicalStop physicalStop;
	
	@JsonProperty("reliability")
	String reliability;
	
	@JsonProperty("arrivalTime")
	Integer arrivalTime;
	
	@JsonProperty("deviation")
	boolean deviation;
	
	@JsonProperty("deviationCode")
	Integer deviationCode;
	
	@JsonProperty("visible")
	boolean visible;

	public Integer getDepartureCode() {
		return departureCode;
	}

	public void setDepartureCode(Integer departureCode) {
		this.departureCode = departureCode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Stop getStop() {
		return stop;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public String getReliability() {
		return reliability;
	}

	public void setReliability(String reliability) {
		this.reliability = reliability;
	}

	public Integer getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Integer arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public boolean isDeviation() {
		return deviation;
	}

	public void setDeviation(boolean deviation) {
		this.deviation = deviation;
	}

	public Integer getDeviationCode() {
		return deviationCode;
	}

	public void setDeviationCode(Integer deviationCode) {
		this.deviationCode = deviationCode;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public PhysicalStop getPhysicalStop() {
		return physicalStop;
	}
	
	public void setPhysicalStop(PhysicalStop physicalStop) {
		this.physicalStop = physicalStop;
	}
}
