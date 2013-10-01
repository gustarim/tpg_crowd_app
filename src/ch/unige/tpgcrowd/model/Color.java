package ch.unige.tpgcrowd.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Color {

	@JsonProperty("lineCode")
	String lineCode;
	
	@JsonProperty("hexa")
	String hexa;

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public String getHexa() {
		return hexa;
	}

	public void setHexa(String hexa) {
		this.hexa = hexa;
	}
	
}
