public class springframework_0207 {

    	@Override
    	protected void updateAccessedSessionAttributes() {
    		if (!this.sessionAttributesToUpdate.isEmpty()) {
    			HttpSession session = getSession(false);
    			if (session != null) {
    				try {
    					for (Map.Entry<String, Object> entry : this.sessionAttributesToUpdate.entrySet()) {
    						updateSessionAttribute(session, entry.getKey(), entry.getValue());
    					}
    				}
    				catch (IllegalStateException ex) {
    					// Session invalidated - shouldn't usually happen.
    				}
    			}
    			this.sessionAttributesToUpdate.clear();
    		}
    	}
    	
    	private void updateSessionAttribute(HttpSession session, String name, Object newValue) {
    		Object oldValue = session.getAttribute(name);
    		if (oldValue == newValue && !isImmutableSessionAttribute(name, newValue)) {
    			session.setAttribute(name, newValue);
    		}
    	}
}
