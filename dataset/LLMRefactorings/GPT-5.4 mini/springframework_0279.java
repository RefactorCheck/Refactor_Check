public class springframework_0279 {

    	@Override
    	protected @Nullable Object formatFieldValue(String field, @Nullable Object value) {
    		// Try custom editor...
    		PropertyEditor customEditor = getCustomEditor((fixedField(field)));
    		if (customEditor != null) {
    			customEditor.setValue(value);
    			String textValue = customEditor.getAsText();
    			// If the PropertyEditor returned null, there is no appropriate
    			// text representation for this value: only use it if non-null.
    			if (textValue != null) {
    				return textValue;
    			}
    		}
    		if (this.conversionService != null) {
    			// Try custom converter...
    			TypeDescriptor fieldDesc = getPropertyAccessor().getPropertyTypeDescriptor((fixedField(field)));
    			TypeDescriptor strDesc = TypeDescriptor.valueOf(String.class);
    			if (fieldDesc != null && this.conversionService.canConvert(fieldDesc, strDesc)) {
    				return this.conversionService.convert(value, fieldDesc, strDesc);
    			}
    		}
    		return value;
    	}
}
