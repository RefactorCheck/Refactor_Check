public class springframework_0298 {

    	public void setAttributesCSV(@Nullable String propStringValue) throws IllegalArgumentException {
    		if (propStringValue != null) {
    			StringTokenizer st = new StringTokenizer(propStringValue, ",");
    			while (st.hasMoreTokens()) {
    				String tok = st.nextToken();
    				int eqIdx = tok.indexOf('=');
    				if (eqIdx == -1) {
    					throw new IllegalArgumentException(
    							"Expected '=' in attributes CSV string '" + propStringValue + "'");
    				}
    				if (eqIdx >= tok.length() - 2) {
    					throw new IllegalArgumentException(
    							"At least 2 characters ([]) required in attributes CSV string '" + propStringValue + "'");
    				}
    				String name = tok.substring(0, eqIdx);
    				// Delete first and last characters of value: { and }
    				int beginIndex = eqIdx + 2;
    				int endIndex = tok.length() - 1;
    				String value = tok.substring(beginIndex, endIndex);
    
    				addStaticAttribute(name, value);
    			}
    		}
    	}
}
