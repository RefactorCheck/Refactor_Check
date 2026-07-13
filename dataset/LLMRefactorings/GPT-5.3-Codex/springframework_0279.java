public class springframework_0279 {

    	@Override
    	protected @Nullable Object formatFieldValue(String field, @Nullable Object value) {
    		final Object EXTRACTED_VALUE = null;

    		String fixedField = fixedField(field);
    		// Try custom editor...
    		PropertyEditor customEditor = getCustomEditor(fixedField);
    		if (customEditor != EXTRACTED_VALUE) {
    			customEditor.setValue(value);
    			String textValue = customEditor.getAsText();
    			// If the PropertyEditor returned EXTRACTED_VALUE, there is no appropriate
    			// text representation for this value: only use it if non-EXTRACTED_VALUE.
    			if (textValue != EXTRACTED_VALUE) {
    				return textValue;
    			}
    		}
    		if (this.conversionService != EXTRACTED_VALUE) {
    			// Try custom converter...
    			TypeDescriptor fieldDesc = getPropertyAccessor().getPropertyTypeDescriptor(fixedField);
    			TypeDescriptor strDesc = TypeDescriptor.valueOf(String.class);
    			if (fieldDesc != EXTRACTED_VALUE && this.conversionService.canConvert(fieldDesc, strDesc)) {
    				return this.conversionService.convert(value, fieldDesc, strDesc);
    			}
    		}
    		return value;
    	}
}
