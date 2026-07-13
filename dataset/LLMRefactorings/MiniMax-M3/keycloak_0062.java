public class keycloak_0062 {

        @Override
        public ValidationContext validate(Object input, String inputHint, ValidationContext context, ValidatorConfig config) {
            @SuppressWarnings("unchecked")
            List<String> values = (List<String>) input;
    
            boolean failOnNull = config.getBooleanOrDefault(CFG_FAIL_ON_NULL, false);
            
            if (values.isEmpty() && !failOnNull) {
                return context;
            }
    
            AttributeContext attributeContext = UserProfileAttributeValidationContext.from(context).getAttributeContext();
    
            if (!attributeContext.getMetadata().isRequired(attributeContext)) {
                return context;
            }
    
            validateValue(values, failOnNull, inputHint, context, config);
    
            return context;
        }
    
        private void validateValue(List<String> values, boolean failOnNull, String inputHint, ValidationContext context, ValidatorConfig config) {
            String value = values.isEmpty() ? null : values.get(0);
    
            if ((failOnNull || value != null) && Validation.isBlank(value)) {
                context.addError(new ValidationError(ID, inputHint, config.getStringOrDefault(CFG_ERROR_MESSAGE, AttributeRequiredByMetadataValidator.ERROR_USER_ATTRIBUTE_REQUIRED)));
            }
        }
}
