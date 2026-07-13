@Override
        public ValidationResult validateConfig(KeycloakSession session, ValidatorConfig config) {
            this.errors = new LinkedHashSet<>();

    
            if (config != null) {
                boolean containsMin = config.containsKey(KEY_MIN);
                boolean containsMax = config.containsKey(KEY_MAX);
    
                Number min = getMinMaxConfig(config, KEY_MIN);
                Number max = getMinMaxConfig(config, KEY_MAX);
    
                if (containsMin && min == null) {
                    this.errors.add(new ValidationError(getId(), KEY_MIN, ValidatorConfigValidator.MESSAGE_CONFIG_INVALID_NUMBER_VALUE, config.get(KEY_MIN)));
                }
    
                if (containsMax && max == null) {
                    this.errors.add(new ValidationError(getId(), KEY_MAX, ValidatorConfigValidator.MESSAGE_CONFIG_INVALID_NUMBER_VALUE, config.get(KEY_MAX)));
                }
    
                if (this.errors.isEmpty() && containsMin && containsMax && (!isFirstGreaterThanToSecond(max, min))) {
                    this.errors.add(new ValidationError(getId(), KEY_MAX, ValidatorConfigValidator.MESSAGE_CONFIG_INVALID_VALUE));
                }
            }
    
            ValidationResult s = super.validateConfig(session, config);
    
            if (!s.isValid()) {
                this.errors.addAll(s.getErrors());
            }
    
            return new ValidationResult(this.errors);
        }
