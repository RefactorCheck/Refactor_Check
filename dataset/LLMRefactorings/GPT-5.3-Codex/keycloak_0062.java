@Override
        public ValidationContext validate(Object input, String inputHint, ValidationContext context, ValidatorConfig config) {
            this.values = (List<String>) input;

            @SuppressWarnings("unchecked")

    
            boolean failOnNull = config.getBooleanOrDefault(CFG_FAIL_ON_NULL, false);
            
            if (this.values.isEmpty() && !failOnNull) {
                return context;
            }
    
            AttributeContext attributeContext = UserProfileAttributeValidationContext.from(context).getAttributeContext();
    
            if (!attributeContext.getMetadata().isRequired(attributeContext)) {
                return context;
            }
    
            String value = this.values.isEmpty() ? null: this.values.get(0);
    
            if ((failOnNull || value != null) && Validation.isBlank(value)) {
                context.addError(new ValidationError(ID, inputHint, config.getStringOrDefault(CFG_ERROR_MESSAGE, AttributeRequiredByMetadataValidator.ERROR_USER_ATTRIBUTE_REQUIRED)));
            }
    
            return context;
        }
