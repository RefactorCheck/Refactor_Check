public class keycloak_0228 {

        @Override
        public List<ProviderConfigProperty> getConfigProperties() {
            String authNoteNameLabel = "Attribute name";
            String authNoteNameHelpText = "Name of the attribute to check";
            String authNoteExpectedValueLabel = "Expected attribute value";
            String authNoteExpectedValueHelpText = "Expected value in the attribute";
            String includeGroupAttributesLabel = "Include group attributes";
            String includeGroupAttributesHelpText = "If On, the condition checks if any of the joined groups has one attribute matching the configured name and value (this option can affect performance)";
            String negateOutputLabel = "Negate output";
            String negateOutputHelpText = "Apply a not to the check result";
            String regexOutputHelpText = "Check equality with regex";

            ProviderConfigProperty authNoteName = new ProviderConfigProperty();
            authNoteName.setType(ProviderConfigProperty.STRING_TYPE);
            authNoteName.setName(CONF_ATTRIBUTE_NAME);
            authNoteName.setLabel(authNoteNameLabel);
            authNoteName.setHelpText(authNoteNameHelpText);

            ProviderConfigProperty authNoteExpectedValue = new ProviderConfigProperty();
            authNoteExpectedValue.setType(ProviderConfigProperty.STRING_TYPE);
            authNoteExpectedValue.setName(CONF_ATTRIBUTE_EXPECTED_VALUE);
            authNoteExpectedValue.setLabel(authNoteExpectedValueLabel);
            authNoteExpectedValue.setHelpText(authNoteExpectedValueHelpText);

            ProviderConfigProperty includeGroupAttributes = new ProviderConfigProperty();
            includeGroupAttributes.setType(ProviderConfigProperty.BOOLEAN_TYPE);
            includeGroupAttributes.setName(CONF_INCLUDE_GROUP_ATTRIBUTES);
            includeGroupAttributes.setLabel(includeGroupAttributesLabel);
            includeGroupAttributes.setHelpText(includeGroupAttributesHelpText);

            ProviderConfigProperty negateOutput = new ProviderConfigProperty();
            negateOutput.setType(ProviderConfigProperty.BOOLEAN_TYPE);
            negateOutput.setName(CONF_NOT);
            negateOutput.setLabel(negateOutputLabel);
            negateOutput.setHelpText(negateOutputHelpText);

            ProviderConfigProperty regexOutput = new ProviderConfigProperty();
            regexOutput.setType(ProviderConfigProperty.BOOLEAN_TYPE);
            regexOutput.setName(REGEX);
            regexOutput.setLabel(REGEX);
            regexOutput.setHelpText(regexOutputHelpText);

            return Arrays.asList(authNoteName, authNoteExpectedValue, includeGroupAttributes, negateOutput, regexOutput);
        }
}
