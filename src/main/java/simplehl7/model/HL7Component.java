package simplehl7.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import simplehl7.parser.HL7Parser;

public class HL7Component {

	private final List<String> subComponents = new ArrayList<String>();
	private final HL7MessageConfig messageConfig;

	public HL7Component(final HL7MessageConfig messageConfig) {
		this.messageConfig = messageConfig;
	}

	public HL7Component(final String fullComponent, final HL7MessageConfig messageConfig) {
		this(messageConfig);
		setComponent(fullComponent);
	}

	public void setComponent(final String fullComponent) {
		this.subComponents.clear();

		if(!StringUtils.isBlank(fullComponent)) {
			final String delimiter = Character.toString(messageConfig.getSubComponentDelimiter());
			final String[] newSubComponents = fullComponent.trim().split(Pattern.quote(delimiter));
			for(String newSubComponent: newSubComponents) {
				subComponents.add(HL7Parser.unescape(newSubComponent, messageConfig));
			}
		}
	}

	public List<String> getSubComponents() {
		return subComponents;
	}
}
