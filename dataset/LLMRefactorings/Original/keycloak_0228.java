public class keycloak_0228 {

        @Override
        public List<ProviderConfigProperty> getConfigProperties() {
            ProviderConfigProperty authNoteName = new ProviderConfigProperty();
            authNoteName.setType(ProviderConfigProperty.STRING_TYPE);
            authNoteName.setName(CONF_ATTRIBUTE_NAME);
            authNoteName.setLabel("Attribute name");
            authNoteName.setHelpText("Name of the attribute to check");
    
            ProviderConfigProperty authNoteExpectedValue = new ProviderConfigProperty();
            authNoteExpectedValue.setType(ProviderConfigProperty.STRING_TYPE);
            authNoteExpectedValue.setName(CONF_ATTRIBUTE_EXPECTED_VALUE);
            authNoteExpectedValue.setLabel("Expected attribute value");
            authNoteExpectedValue.setHelpText("Expected value in the attribute");
    
            ProviderConfigProperty includeGroupAttributes = new ProviderConfigProperty();
            includeGroupAttributes.setType(ProviderConfigProperty.BOOLEAN_TYPE);
            includeGroupAttributes.setName(CONF_INCLUDE_GROUP_ATTRIBUTES);
            includeGroupAttributes.setLabel("Include group attributes");
            includeGroupAttributes.setHelpText("If On, the condition checks if any of the joined groups has one attribute matching the configured name and value (this option can affect performance)");
    
            ProviderConfigProperty negateOutput = new ProviderConfigProperty();
            negateOutput.setType(ProviderConfigProperty.BOOLEAN_TYPE);
            negateOutput.setName(CONF_NOT);
            negateOutput.setLabel("Negate output");
            negateOutput.setHelpText("Apply a not to the check result");
    
            ProviderConfigProperty regexOutput = new ProviderConfigProperty();
            regexOutput.setType(ProviderConfigProperty.BOOLEAN_TYPE);
            regexOutput.setName(REGEX);
            regexOutput.setLabel(REGEX);
            regexOutput.setHelpText("Check equality with regex");
    
            return Arrays.asList(authNoteName, authNoteExpectedValue, includeGroupAttributes, negateOutput, regexOutput);
        }
}
