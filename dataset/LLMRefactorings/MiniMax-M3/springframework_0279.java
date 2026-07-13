public class springframework_0279 {

	@Override
	protected @Nullable Object formatFieldValue(String field, @Nullable Object value) {
		String fixedField = fixedField(field);
		Object customEditorResult = formatUsingCustomEditor(fixedField, value);
		if (customEditorResult != null) {
			return customEditorResult;
		}
		Object conversionResult = formatUsingConversionService(fixedField, value);
		if (conversionResult != null) {
			return conversionResult;
		}
		return value;
	}

	private @Nullable Object formatUsingCustomEditor(String fixedField, @Nullable Object value) {
		PropertyEditor customEditor = getCustomEditor(fixedField);
		if (customEditor != null) {
			customEditor.setValue(value);
			String textValue = customEditor.getAsText();
			// If the PropertyEditor returned null, there is no appropriate
			// text representation for this value: only use it if non-null.
			if (textValue != null) {
				return textValue;
			}
		}
		return null;
	}

	private @Nullable Object formatUsingConversionService(String fixedField, @Nullable Object value) {
		if (this.conversionService != null) {
			TypeDescriptor fieldDesc = getPropertyAccessor().getPropertyTypeDescriptor(fixedField);
			TypeDescriptor strDesc = TypeDescriptor.valueOf(String.class);
			if (fieldDesc != null && this.conversionService.canConvert(fieldDesc, strDesc)) {
				return this.conversionService.convert(value, fieldDesc, strDesc);
			}
		}
		return null;
	}
}
