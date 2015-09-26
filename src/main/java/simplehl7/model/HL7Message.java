package simplehl7.model;

import java.util.ArrayList;
import java.util.List;

public class HL7Message {

	private final HL7MessageConfig messageConfig;

	private List<HL7Segment> segments = new ArrayList<HL7Segment>();

	public HL7Message() {
		this(HL7MessageConfig.DEFAULT);
	}

	public HL7Message(final HL7MessageConfig messageConfig) {
		this.messageConfig = messageConfig;
	}

	public List<HL7Segment> getSegments() {
		return segments;
	}
}
