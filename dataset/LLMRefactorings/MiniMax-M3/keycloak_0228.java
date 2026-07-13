public class keycloak_0228 {

        @Override
        public List<ProviderConfigProperty> getConfigProperties() {
            return Arrays.asList(
                createConfigProperty(ProviderConfigProperty.STRING_TYPE, CONF_ATTRIBUTE_NAME, "Attribute name", "Name of the attribute to check"),
                createConfigProperty(ProviderConfigProperty.STRING_TYPE, CONF_ATTRIBUTE_EXPECTED_VALUE, "Expected attribute value", "Expected value in the attribute"),
                createConfigProperty(ProviderConfigProperty.BOOLEAN_TYPE, CONF_INCLUDE_GROUP_ATTRIBUTES, "Include group attributes", "If On, the condition checks if any of the joined groups has one attribute matching the configured name and value (this option can affect performance)"),
                createConfigProperty(ProviderConfigProperty.BOOLEAN_TYPE, CONF_NOT, "Negate output", "Apply a not to the check result"),
                createConfigProperty(ProviderConfigProperty.BOOLEAN_TYPE, REGEX, REGEX, "Check equality with regex")
            );
        }

        private ProviderConfigProperty createConfigProperty(String type, String name, String label, String helpText) {
            ProviderConfigProperty property = new ProviderConfigProperty();
            property.setType(type);
            property.setName(name);
            property.setLabel(label);
            property.setHelpText(helpText);
            return property;
        }
}
