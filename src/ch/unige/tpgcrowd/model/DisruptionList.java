package ch.unige.tpgcrowd.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DisruptionList {

	@JsonProperty("timestamp")
	Date timestamp;	
	
	@JsonProperty("disruptions")
	List<Disruption> disruptions;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<Disruption> getDisruptions() {
		return disruptions;
	}

	public void setDisruptions(List<Disruption> disruptions) {
		this.disruptions = disruptions;
	}

	
}
