package simplehl7.model;

public class HL7MessageConfig {

	public static final HL7MessageConfig DEFAULT = new HL7MessageConfig('|', '^', '&', '~', '\\', "\r");

	private final char fieldDelimiter;
	private final char componentDelimiter;
	private final char subComponentDelimiter;
	private final char repeatDelimiter;
	private final char escapeCharacter;
	private final String segmentDelimiter;

	public HL7MessageConfig(char fieldDelimiter, char componentDelimiter, char subComponentDelimiter,
			char repeatDelimiter, char escapeCharacter, String segmentDelimiter) {
		this.fieldDelimiter = fieldDelimiter;
		this.componentDelimiter = componentDelimiter;
		this.subComponentDelimiter = subComponentDelimiter;
		this.repeatDelimiter = repeatDelimiter;
		this.escapeCharacter = escapeCharacter;
		this.segmentDelimiter = segmentDelimiter;
	}

	public char getFieldDelimiter() {
		return fieldDelimiter;
	}

	public char getComponentDelimiter() {
		return componentDelimiter;
	}

	public char getSubComponentDelimiter() {
		return subComponentDelimiter;
	}

	public char getRepeatDelimiter() {
		return repeatDelimiter;
	}

	public char getEscapeCharacter() {
		return escapeCharacter;
	}

	public String getSegmentDelimiter() {
		return segmentDelimiter;
	}

}
