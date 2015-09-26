package simplehl7.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import simplehl7.exception.HL7Exception;

public class HL7Segment {

	private static final String INVALID_FIELD_MESSAGE = "Field number must be at least 1";

	private String name;
	private final Map<Integer, List<HL7Field>> fields = new HashMap<Integer, List<HL7Field>>();
	private final HL7MessageConfig messageConfig;

	public HL7Segment(final HL7MessageConfig messageConfig) {
		this.messageConfig = messageConfig;
	}

	public HL7Segment(final String fullSegment, final HL7MessageConfig messageConfig) {
		this(messageConfig);
		setSegment(fullSegment);
	}

	public void setSegment(final String fullSegment) {
		this.fields.clear();
		this.name = null;

		if(!StringUtils.isBlank(fullSegment)) {
			final String delimiter = Character.toString(messageConfig.getFieldDelimiter());
			final String[] newFields = fullSegment.trim().split(Pattern.quote(delimiter));

			this.name = newFields[0];

			int fieldCount = 1;
			while(fieldCount < newFields.length) {
				setField(newFields[fieldCount], fieldCount);
				fieldCount++;
			}
		}
	}

	public void setField(final String fullField, final int fieldNumber) {
		if(fieldNumber < 1)
			throw new HL7Exception(INVALID_FIELD_MESSAGE);

		List<HL7Field> fieldReps = fields.get(fieldNumber);
		if(fieldReps == null) {
			fieldReps = new ArrayList<HL7Field>();
			fields.put(fieldNumber, fieldReps);
		} else {
			fieldReps.clear();
		}

		if(!StringUtils.isBlank(fullField)) {
			final String delimiter = Character.toString(messageConfig.getRepeatDelimiter());
			final String[] newFieldReps = fullField.trim().split(Pattern.quote(delimiter));
			for(String newFieldRep: newFieldReps) {
				fieldReps.add(new HL7Field(newFieldRep, messageConfig));
			}
		}
	}

	public HL7Field getFirstInstanceOfField(final int fieldNumber) {
		List<HL7Field> fieldReps = getField(fieldNumber);
		if(fieldReps.isEmpty()) {
			fieldReps.add(new HL7Field(messageConfig));
		}
		return fieldReps.get(0);
	}

	public List<HL7Field> getField(final int fieldNumber) {
		if(fieldNumber < 1)
			throw new HL7Exception(INVALID_FIELD_MESSAGE);

		if(!fields.containsKey(fieldNumber)) {
			fields.put(fieldNumber, new ArrayList<HL7Field>());
		}

		return fields.get(fieldNumber);
	}

	public String getName() {
		return name;
	}

}
