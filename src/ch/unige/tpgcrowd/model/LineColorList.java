package ch.unige.tpgcrowd.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LineColorList {

	@JsonProperty("timestamp")
	Date timestamp;
	
	@JsonProperty("colors")
	List<Color> colors;
}
