public class springframework_0252 {

    	@Override
    	protected int writeTagContent(TagWriter tagWriter) throws JspException {
    		applyExtractedRefactoring();

    		writeDefaultAttributes(tagWriter);
    		if (isMultiple()) {
    			tagWriter.writeAttribute("multiple", "multiple");
    		}
    		tagWriter.writeOptionalAttributeValue("size", getDisplayString(evaluate("size", getSize())));
    
    		Object items = getItems();
    		if (items != null) {
    			// Items specified, but might still be empty...
    			if (items != EMPTY) {
    				Object itemsObject = evaluate("items", items);
    				if (itemsObject != null) {
    					final String selectName = getName();
    					String valueProperty = (getItemValue() != null ?
    							ObjectUtils.getDisplayString(evaluate("itemValue", getItemValue())) : null);
    					String labelProperty = (getItemLabel() != null ?
    							ObjectUtils.getDisplayString(evaluate("itemLabel", getItemLabel())) : null);
    					String encodingToUse = (isResponseEncodedHtmlEscape() ?
    							this.pageContext.getResponse().getCharacterEncoding() : null);
    					OptionWriter optionWriter =
    							new OptionWriter(itemsObject, getBindStatus(), valueProperty, labelProperty,
    									isHtmlEscape(), encodingToUse) {
    
    								@Override
    								protected String processOptionValue(String resolvedValue) {
    									return processFieldValue(selectName, resolvedValue, "option");
    								}
    							};
    					optionWriter.writeOptions(tagWriter);
    				}
    			}
    			tagWriter.endTag(true);
    			writeHiddenTagIfNecessary(tagWriter);
    			return SKIP_BODY;
    		}
    		else {
    			// Using nested <form:option/> tags, so just expose the value in the PageContext...
    			tagWriter.forceBlock();
    			this.tagWriter = tagWriter;
    			this.pageContext.setAttribute(LIST_VALUE_PAGE_ATTRIBUTE, getBindStatus());
    			return EVAL_BODY_INCLUDE;
    		}
    	}

	private void applyExtractedRefactoring() {
    		tagWriter.startTag("select");
	}
}
