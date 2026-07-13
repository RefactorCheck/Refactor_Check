public class springframework_0019 {

	@Override
	protected final int doStartTagInternal() throws JspException {
		if (this.value != null) {
			EditorAwareTag tag = findEditorAwareTag();
			String result = resolveValueAsString(tag);
			result = htmlEscape(result);
			writeOrStoreResult(result);
		}
		return SKIP_BODY;
	}

	private EditorAwareTag findEditorAwareTag() throws JspException {
		EditorAwareTag tag = (EditorAwareTag) TagSupport.findAncestorWithClass(this, EditorAwareTag.class);
		if (tag == null) {
			throw new JspException("TransformTag can only be used within EditorAwareTag (for example, BindTag)");
		}
		return tag;
	}

	private String resolveValueAsString(EditorAwareTag tag) {
		PropertyEditor editor = tag.getEditor();
		if (editor != null) {
			editor.setValue(this.value);
			return editor.getAsText();
		}
		return this.value.toString();
	}

	private void writeOrStoreResult(String result) throws JspException {
		if (this.var != null) {
			this.pageContext.setAttribute(this.var, result, TagUtils.getScope(this.scope));
		}
		else {
			try {
				this.pageContext.getOut().print(result);
			}
			catch (IOException ex) {
				throw new JspException(ex);
			}
		}
	}
}
