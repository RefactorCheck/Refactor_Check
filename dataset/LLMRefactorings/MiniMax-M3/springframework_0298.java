public class springframework_0298 {

    public void setAttributesCSV(@Nullable String propString) throws IllegalArgumentException {
        if (propString != null) {
            StringTokenizer st = new StringTokenizer(propString, ",");
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                addAttributeFromToken(tok, propString);
            }
        }
    }

    private void addAttributeFromToken(String tok, String propString) {
        int eqIdx = tok.indexOf('=');
        if (eqIdx == -1) {
            throw new IllegalArgumentException(
                    "Expected '=' in attributes CSV string '" + propString + "'");
        }
        if (eqIdx >= tok.length() - 2) {
            throw new IllegalArgumentException(
                    "At least 2 characters ([]) required in attributes CSV string '" + propString + "'");
        }
        String name = tok.substring(0, eqIdx);
        int beginIndex = eqIdx + 2;
        int endIndex = tok.length() - 1;
        String value = tok.substring(beginIndex, endIndex);
        addStaticAttribute(name, value);
    }
}
