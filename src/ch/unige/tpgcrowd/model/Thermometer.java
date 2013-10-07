package ch.unige.tpgcrowd.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Thermometer implements ITPGModelEntity {

	@JsonProperty("timestamp")
	Date timestamp;
	
	@JsonProperty("stop")
	Stop stop;
	
	@JsonProperty("lineCode")
	String lineCode;
	
	@JsonProperty("destinationName")
	String destinationName;
	
	@JsonProperty("destinationCode")
	String destinationCode;
	
	@JsonProperty("steps")
	List<Step> steps;
	
	@JsonProperty("disruptions")
	List<Disruption> disruptions;

	@JsonProperty("deviations")
	List<Deviation> deviations;

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

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	public List<Disruption> getDisruptions() {
		return disruptions;
	}

	public void setDisruptions(List<Disruption> disruptions) {
		this.disruptions = disruptions;
	}

	public List<Deviation> getDeviations() {
		return deviations;
	}

	public void setDeviations(List<Deviation> deviations) {
		this.deviations = deviations;
	}

}
