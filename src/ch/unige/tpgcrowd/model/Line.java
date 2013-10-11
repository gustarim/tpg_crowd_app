package ch.unige.tpgcrowd.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Line {
//"line":{"lineCode":"25","destinationName":"Jar.-Botanique","destinationCode":"JAR. BOTANIQUE"}
	@JsonProperty("lineCode")
	String lineCode;
	
	@JsonProperty("destinationName")
	String destinationName;
	
	@JsonProperty("destinationCode")
	String destinationCode;

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
	
	
}
