package ch.unige.tpgcrowd.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Deviation {

	@JsonProperty("deviationCode")
	Integer deviationCode;
	
	@JsonProperty("startStop")
	Stop startStop;
	
	@JsonProperty("endStop")
	Stop endStop;

	public Integer getDeviationCode() {
		return deviationCode;
	}

	public void setDeviationCode(Integer deviationCode) {
		this.deviationCode = deviationCode;
	}

	public Stop getStartStop() {
		return startStop;
	}

	public void setStartStop(Stop startStop) {
		this.startStop = startStop;
	}

	public Stop getEndStop() {
		return endStop;
	}

	public void setEndStop(Stop endStop) {
		this.endStop = endStop;
	}
	
}
