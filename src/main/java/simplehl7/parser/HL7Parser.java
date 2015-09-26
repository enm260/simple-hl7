package simplehl7.parser;

import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;

import simplehl7.exception.HL7EscapeException;
import simplehl7.exception.HL7ParseException;
import simplehl7.model.HL7Message;
import simplehl7.model.HL7MessageConfig;
import simplehl7.model.HL7Segment;

public class HL7Parser {

	private static final String HEADER_NAME = "MSH";
	private static final int MIN_LENGTH = 8;

	private static final char FIELD_ESCAPE = 'F';
	private static final char REPEAT_ESCAPE = 'R';
	private static final char COMPONENT_ESCAPE = 'S';
	private static final char SUBCOMPONENT_ESCAPE = 'T';
	private static final char ESCAPE_ESCAPE = 'E';
	private static final char HEX_ESCAPE = 'X';

	private static final String UNKNOWN_ESCAPE_MESSAGE = "Unknown escape sequence: %s";
	private static final String UNPAIRED_ESCAPE_MESSAGE = "Unpaired escape character found";
	private static final String EMPTY_ESCAPE_MESSAGE = "Empty escape sequence found";
	private static final String INVALID_HEX_MESSAGE = "Invalid hex escape sequence: %s";

	public static HL7Message parse(final String message) throws HL7ParseException {
		if(message.length() < MIN_LENGTH)
			throw new HL7ParseException(String.format("Message must be at least %d characters long", MIN_LENGTH));

		if(!StringUtils.equals(message.substring(0, HEADER_NAME.length()), HEADER_NAME))
			throw new HL7ParseException("Message must contain a header (first 3 characters must be MSH");

		HL7MessageConfig messageConfig = new HL7MessageConfig(message.charAt(3), message.charAt(4), message.charAt(7),
				message.charAt(5), message.charAt(6), "\r");

		HL7Message parsedMessage = new HL7Message(messageConfig);

		final String[] segments = message.trim().split(messageConfig.getSegmentDelimiter());
		for(String segment: segments) {
			parsedMessage.getSegments().add(new HL7Segment(segment, messageConfig));
		}

		return parsedMessage;
	}

	public static String escape(final String value, final HL7MessageConfig messageConfig) {
		StringBuilder escapedValue = new StringBuilder();
		final String escapeDelimiter = Character.toString(messageConfig.getEscapeCharacter());

		int i = 0;
		while(i < value.length()) {
			if(value.charAt(i) == messageConfig.getFieldDelimiter())
				escapedValue.append(escapeDelimiter).append(FIELD_ESCAPE).append(escapeDelimiter);
			else if(value.charAt(i) == messageConfig.getRepeatDelimiter())
				escapedValue.append(escapeDelimiter).append(REPEAT_ESCAPE).append(escapeDelimiter);
			else if(value.charAt(i) == messageConfig.getComponentDelimiter())
				escapedValue.append(escapeDelimiter).append(COMPONENT_ESCAPE).append(escapeDelimiter);
			else if(value.charAt(i) == messageConfig.getSubComponentDelimiter())
				escapedValue.append(escapeDelimiter).append(SUBCOMPONENT_ESCAPE).append(escapeDelimiter);
			else if(value.charAt(i) == messageConfig.getEscapeCharacter())
				escapedValue.append(escapeDelimiter).append(ESCAPE_ESCAPE).append(escapeDelimiter);
			else
				escapedValue.append(value.charAt(i));
			i++;
		}

		return escapedValue.toString();
	}

	public static String unescape(final String escapedValue, final HL7MessageConfig messageConfig) {
		StringBuilder value = new StringBuilder();
		final String escapeStr = Character.toString(messageConfig.getEscapeCharacter());

		int i = 0;
		while(i < escapedValue.length()) {
			if(escapedValue.charAt(i) == messageConfig.getEscapeCharacter()) {
				final int escapeEnd = escapedValue.indexOf(messageConfig.getEscapeCharacter(), i + 1);
				if(escapeEnd == -1)
					throw new HL7EscapeException(UNPAIRED_ESCAPE_MESSAGE, i);

				String escapeSequence = escapedValue.substring(i, escapeEnd);
				escapeSequence = escapeSequence.replaceAll(Pattern.quote(escapeStr), "");

				if(escapeSequence.isEmpty())
					throw new HL7EscapeException(EMPTY_ESCAPE_MESSAGE, i);

				if(escapeSequence.length() == 1) {
					switch(escapeSequence.charAt(0)) {
						case FIELD_ESCAPE:
							value.append(messageConfig.getFieldDelimiter());
							break;
						case REPEAT_ESCAPE:
							value.append(messageConfig.getRepeatDelimiter());
							break;
						case COMPONENT_ESCAPE:
							value.append(messageConfig.getComponentDelimiter());
							break;
						case SUBCOMPONENT_ESCAPE:
							value.append(messageConfig.getSubComponentDelimiter());
							break;
						case ESCAPE_ESCAPE:
							value.append(messageConfig.getEscapeCharacter());
							break;
						default:
							throw new HL7EscapeException(String.format(UNKNOWN_ESCAPE_MESSAGE, escapeSequence), i);
					}
					i += 3;
				} else if(escapeSequence.charAt(0) == HEX_ESCAPE) {
					final String hexValue = escapeSequence.substring(1);
					if((hexValue.length() % 2) == 1)
						throw new HL7EscapeException(String.format(INVALID_HEX_MESSAGE, hexValue), i);

					byte[] escapedBytes;
					try {
						escapedBytes = DatatypeConverter.parseHexBinary(hexValue);
					} catch(IllegalArgumentException e) {
						throw new HL7EscapeException(String.format(INVALID_HEX_MESSAGE, hexValue), i, e);
					}

					value.append(new String(escapedBytes));
					i += (escapeSequence.length() + 2);
				} else
					throw new HL7EscapeException(String.format(UNKNOWN_ESCAPE_MESSAGE, escapeSequence), i);
			} else {
				value.append(escapedValue.charAt(i));
				i++;
			}
		}
		return value.toString();
	}

}
