package simplehl7.exception;

public class HL7Exception extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "HL7 exception: %s";

	public HL7Exception(final String detailMessage) {
		super(String.format(MESSAGE, detailMessage));
	}

	public HL7Exception(final String detailMessage, final Throwable t) {
		super(String.format(MESSAGE, detailMessage), t);
	}

}
