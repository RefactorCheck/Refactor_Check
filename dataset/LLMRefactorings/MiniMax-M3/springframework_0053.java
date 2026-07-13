public class springframework_0053 {

    	@Override
    	public int doEndTag() throws JspException {
    		Object argument = resolveArgument();

    		ArgumentAware argumentAwareTag = (ArgumentAware) findAncestorWithClass(this, ArgumentAware.class);
    		if (argumentAwareTag == null) {
    			throw new JspException("The argument tag must be a descendant of a tag that supports arguments");
    		}
    		argumentAwareTag.addArgument(argument);

    		return EVAL_PAGE;
    	}

    	private Object resolveArgument() {
    		Object argument = null;
    		if (this.valueSet) {
    			argument = this.value;
    		}
    		else if (getBodyContent() != null) {
    			argument = getBodyContent().getString().trim();
    		}
    		return argument;
    	}
}
