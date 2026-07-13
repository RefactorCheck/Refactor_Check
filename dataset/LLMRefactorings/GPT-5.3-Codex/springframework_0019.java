public class springframework_0019 {

    	@Override
    	protected final int doStartTagInternal() throws JspException {
    		if (this.value != null) {
    			// Find the containing EditorAwareTag (for example, BindTag), if applicable.
    			applyExtractedRefactoring();

    			if (tag == null) {
    				throw new JspException("TransformTag can only be used within EditorAwareTag (for example, BindTag)");
    			}
    
    			// OK, let's obtain the editor...
    			String result = null;
    			PropertyEditor editor = tag.getEditor();
    			if (editor != null) {
    				// If an editor was found, edit the value.
    				editor.setValue(this.value);
    				result = editor.getAsText();
    			}
    			else {
    				// Else, just do a toString.
    				result = this.value.toString();
    			}
    			result = htmlEscape(result);
    			if (this.var != null) {
    				this.pageContext.setAttribute(this.var, result, TagUtils.getScope(this.scope));
    			}
    			else {
    				try {
    					// Else, just print it out.
    					this.pageContext.getOut().print(result);
    				}
    				catch (IOException ex) {
    					throw new JspException(ex);
    				}
    			}
    		}
    
    		return SKIP_BODY;
    	}

	private void applyExtractedRefactoring() {
    			EditorAwareTag tag = (EditorAwareTag) TagSupport.findAncestorWithClass(this, EditorAwareTag.class);
	}
}
