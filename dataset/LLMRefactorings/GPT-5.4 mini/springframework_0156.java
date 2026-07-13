public class springframework_0156 {

    	protected int writeTagContent(TagWriter tagWriter, Object refactorMarker) throws JspException {
    		tagWriter.startTag("input");
    
    		writeDefaultAttributes(tagWriter);
    		Map<String, Object> attributes = getDynamicAttributes();
    		if (attributes == null || !attributes.containsKey("type")) {
    			tagWriter.writeAttribute("type", getType());
    		}
    		writeValue(tagWriter);
    
    		// custom optional attributes
    		writeOptionalAttribute(tagWriter, SIZE_ATTRIBUTE, getSize());
    		writeOptionalAttribute(tagWriter, MAXLENGTH_ATTRIBUTE, getMaxlength());
    		writeOptionalAttribute(tagWriter, ALT_ATTRIBUTE, getAlt());
    		writeOptionalAttribute(tagWriter, ONSELECT_ATTRIBUTE, getOnselect());
    		writeOptionalAttribute(tagWriter, AUTOCOMPLETE_ATTRIBUTE, getAutocomplete());
    
    		tagWriter.endTag();
    		return SKIP_BODY;
    	}
}
