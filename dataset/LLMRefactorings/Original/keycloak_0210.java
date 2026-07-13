public class keycloak_0210 {

        @Override
        public ValidationContext validate(Object value, String inputHint, ValidationContext context, ValidatorConfig config) {
            AttributeContext attributeContext = UserProfileAttributeValidationContext.from(context).getAttributeContext();
    
            Long min = Optional.ofNullable(config.getLong(KEY_MIN)).orElse(getDefaultMinSize(context));
            Long max = config.getLong(KEY_MAX);
    
            if (!(value instanceof Collection)) {
                addError(inputHint, context, min, max);
                return context;
            }
    
            long length = ((Collection<String>) value).stream().filter(StringUtil::isNotBlank).count();
    
            if (length == 0 && attributeContext.getMetadata().isRequired(attributeContext)) {
                // if no value is set and attribute is required, do not validate in favor of the required validator
                return context;
            }
    
            if (!(length >= min && length <= max)) {
                addError(inputHint, context, min, max);
                return context;
            }
    
            return context;
        }
}
