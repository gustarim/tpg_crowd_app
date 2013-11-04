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
	
	public int getIndexFirstRemainingStep() {
		int i = 0;
		while (i < steps.size() - 1 && steps.get(i).arrivalTime == null) {
			i++;
		}
		if (i == steps.size() - 1) {
			if (steps.get(i).arrivalTime == null) {
				i = -1;
			}
		}
		return i;
	}
	
	public Step getFirstNonZeroRemainingStep() {
		Step step = null;
		int i = getIndexFirstRemainingStep();
		if (i != -1) {
			while (i < steps.size() - 1 && steps.get(i).arrivalTime == 0 ) {
				i++;
			}
			if (i == steps.size() - 1) {
				if (steps.get(i).arrivalTime != 0) {
					step = steps.get(i);
				}
			}
			else {
				step = steps.get(i);
			}
		}
		return step;
	}
	
	public Step getFirstRemainingStep() {
		Step step = null;
		final int i = getIndexFirstRemainingStep();
		if (i != -1) {
			step = steps.get(i);
		}
		return step;
	}
	
	public List<Step> getRemainingSteps() {
		List<Step> steps = null;
		final int i = getIndexFirstRemainingStep();
		if (i != -1) {
			steps = this.steps.subList(i, this.steps.size());
		}
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
