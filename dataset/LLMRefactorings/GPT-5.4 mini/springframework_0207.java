public class springframework_0207 {

    	@Override
    	protected void updateAccessedSessionAttributes() {
    		if (!this.sessionAttributesToUpdate.isEmpty()) {
    			// Update all affected session attributes.
    			HttpSession extractedValue = getSession(false);
    			HttpSession session = extractedValue;
    			if (session != null) {
    				try {
    					for (Map.Entry<String, Object> entry : this.sessionAttributesToUpdate.entrySet()) {
    						String name = entry.getKey();
    						Object newValue = entry.getValue();
    						Object oldValue = session.getAttribute(name);
    						if (oldValue == newValue && !isImmutableSessionAttribute(name, newValue)) {
    							session.setAttribute(name, newValue);
    						}
    					}
    				}
    				catch (IllegalStateException ex) {
    					// Session invalidated - shouldn't usually happen.
    				}
    			}
    			this.sessionAttributesToUpdate.clear();
    		}
    	}
}
