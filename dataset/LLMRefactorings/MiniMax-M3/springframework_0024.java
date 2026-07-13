public class springframework_0024 {

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		super.doParse(element, parserContext, builder);

		builder.addPropertyValue("ignoreUnresolvablePlaceholders",
				Boolean.valueOf(element.getAttribute("ignore-unresolvable")));

		String systemPropertiesModeName = element.getAttribute(SYSTEM_PROPERTIES_MODE_ATTRIBUTE);
		if (StringUtils.hasLength(systemPropertiesModeName) &&
				!systemPropertiesModeName.equals(SYSTEM_PROPERTIES_MODE_DEFAULT)) {
			builder.addPropertyValue("systemPropertiesModeName", "SYSTEM_PROPERTIES_MODE_" + systemPropertiesModeName);
		}

		setOptionalAttribute(element, builder, "value-separator", "valueSeparator");
		setOptionalAttribute(element, builder, "trim-values", "trimValues");
		setOptionalAttribute(element, builder, "null-value", "nullValue");
	}

	private void setOptionalAttribute(Element element, BeanDefinitionBuilder builder,
			String xmlAttribute, String propertyName) {
		if (element.hasAttribute(xmlAttribute)) {
			builder.addPropertyValue(propertyName, element.getAttribute(xmlAttribute));
		}
	}
}
