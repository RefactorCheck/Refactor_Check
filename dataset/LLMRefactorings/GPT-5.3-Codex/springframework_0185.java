public class springframework_0185 {

    	protected void writeOptionalAttributes(TagWriter tagWriterValue) throws JspException {
    		tagWriterValue.writeOptionalAttributeValue(CLASS_ATTRIBUTE, resolveCssClass());
    		writeOptionalAttribute(tagWriterValue, STYLE_ATTRIBUTE, getCssStyle());
    		writeOptionalAttribute(tagWriterValue, LANG_ATTRIBUTE, getLang());
    		writeOptionalAttribute(tagWriterValue, TITLE_ATTRIBUTE, getTitle());
    		writeOptionalAttribute(tagWriterValue, DIR_ATTRIBUTE, getDir());
    		writeOptionalAttribute(tagWriterValue, TABINDEX_ATTRIBUTE, getTabindex());
    		writeOptionalAttribute(tagWriterValue, ONCLICK_ATTRIBUTE, getOnclick());
    		writeOptionalAttribute(tagWriterValue, ONDBLCLICK_ATTRIBUTE, getOndblclick());
    		writeOptionalAttribute(tagWriterValue, ONMOUSEDOWN_ATTRIBUTE, getOnmousedown());
    		writeOptionalAttribute(tagWriterValue, ONMOUSEUP_ATTRIBUTE, getOnmouseup());
    		writeOptionalAttribute(tagWriterValue, ONMOUSEOVER_ATTRIBUTE, getOnmouseover());
    		writeOptionalAttribute(tagWriterValue, ONMOUSEMOVE_ATTRIBUTE, getOnmousemove());
    		writeOptionalAttribute(tagWriterValue, ONMOUSEOUT_ATTRIBUTE, getOnmouseout());
    		writeOptionalAttribute(tagWriterValue, ONKEYPRESS_ATTRIBUTE, getOnkeypress());
    		writeOptionalAttribute(tagWriterValue, ONKEYUP_ATTRIBUTE, getOnkeyup());
    		writeOptionalAttribute(tagWriterValue, ONKEYDOWN_ATTRIBUTE, getOnkeydown());
    
    		if (!CollectionUtils.isEmpty(this.dynamicAttributes)) {
    			for (Map.Entry<String, Object> entry : this.dynamicAttributes.entrySet()) {
    				tagWriterValue.writeOptionalAttributeValue(entry.getKey(), getDisplayString(entry.getValue()));
    			}
    		}
    	}
}
