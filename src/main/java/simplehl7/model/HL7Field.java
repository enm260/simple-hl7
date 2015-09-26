package simplehl7.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class HL7Field {

	private final List<HL7Component> components = new ArrayList<HL7Component>();
	private final HL7MessageConfig messageConfig;

	public HL7Field(final HL7MessageConfig messageConfig) {
		this.messageConfig = messageConfig;
	}

	public HL7Field(final String fullField, final HL7MessageConfig messageConfig) {
		this(messageConfig);
		setField(fullField);
	}

	public void setField(final String fullField) {
		this.components.clear();

		if(!StringUtils.isBlank(fullField)) {
			final String delimiter = Character.toString(messageConfig.getComponentDelimiter());
			final String[] newComponents = fullField.trim().split(Pattern.quote(delimiter));
			for(String newComponent: newComponents) {
				final HL7Component component = new HL7Component(newComponent, messageConfig);
				components.add(component);
			}
		}
	}

	public List<HL7Component> getComponents() {
		return components;
	}
}
