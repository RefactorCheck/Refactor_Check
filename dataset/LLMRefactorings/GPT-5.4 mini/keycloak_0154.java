public class keycloak_0154 {

        @Override
        public ValidationResult validateConfig(KeycloakSession session, ValidatorConfig config) {
            Set<ValidationError> errors = new LinkedHashSet<>();
    
            if (config != null) {
                boolean containsMin = config.containsKey(KEY_MIN);
                boolean containsMax = config.containsKey(KEY_MAX);
    
                Number min = getMinMaxConfig(config, KEY_MIN);
                Number max = getMinMaxConfig(config, KEY_MAX);
    
                if (containsMin && min == null) {
                    errors.add(new ValidationError(getId(), KEY_MIN, ValidatorConfigValidator.MESSAGE_CONFIG_INVALID_NUMBER_VALUE, config.get(KEY_MIN)));
                }
    
                if (containsMax && max == null) {
                    errors.add(new ValidationError(getId(), KEY_MAX, ValidatorConfigValidator.MESSAGE_CONFIG_INVALID_NUMBER_VALUE, config.get(KEY_MAX)));
                }
    
                if (errors.isEmpty() && containsMin && containsMax && (!isFirstGreaterThanToSecond(max, min))) {
                    errors.add(new ValidationError(getId(), KEY_MAX, ValidatorConfigValidator.MESSAGE_CONFIG_INVALID_VALUE));
                }
            }
    
            ValidationResult superValidationResult = super.validateConfig(session, config);
    
            if (!superValidationResult.isValid()) {
                errors.addAll(superValidationResult.getErrors());
            }
    
            return new ValidationResult(errors);
        }
}
