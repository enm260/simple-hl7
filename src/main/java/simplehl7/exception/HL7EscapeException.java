package simplehl7.exception;

public class HL7EscapeException extends HL7Exception {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE_FORMAT = "%s at position %d";

	public HL7EscapeException(final String detailMessage, final int position) {
		super(String.format(MESSAGE_FORMAT, detailMessage, position));
	}

	public HL7EscapeException(final String detailMessage, final int position, final Throwable t) {
		super(String.format(MESSAGE_FORMAT, detailMessage, position), t);
	}

}
