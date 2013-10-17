package ch.unige.tpgcrowd.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LineColorList implements ITPGModelEntity {

	@JsonProperty("timestamp")
	Date timestamp;
	
	@JsonProperty("colors")
	List<Color> colors;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<Color> getColors() {
		return colors;
	}

	public void setColors(List<Color> colors) {
		this.colors = colors;
	}
	
	
}
