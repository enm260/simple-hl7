package simplehl7.exception;

public class HL7ParseException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "Error parsing HL7 message: %s";

	public HL7ParseException(final String detailMessage) {
		super(String.format(MESSAGE, detailMessage));
	}

}
