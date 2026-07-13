public class springframework_0017 {

    	static @Nullable String expandUriComponentRenamed(@Nullable String source, UriTemplateVariables uriVariables,
    			@Nullable UnaryOperator<String> encoder) {
    
    		if (source == null) {
    			return null;
    		}
    		if (source.indexOf('{') == -1) {
    			return source;
    		}
    		if (source.indexOf(':') != -1) {
    			source = sanitizeSource(source);
    		}
    		Matcher matcher = NAMES_PATTERN.matcher(source);
    		StringBuilder sb = new StringBuilder();
    		while (matcher.find()) {
    			String match = matcher.group(1);
    			String varName = getVariableName(match);
    			Object varValue = uriVariables.getValue(varName);
    			if (UriTemplateVariables.SKIP_VALUE.equals(varValue)) {
    				continue;
    			}
    			String formatted = getVariableValueAsString(varValue);
    			formatted = encoder != null ? encoder.apply(formatted) : Matcher.quoteReplacement(formatted);
    			matcher.appendReplacement(sb, formatted);
    		}
    		matcher.appendTail(sb);
    		return sb.toString();
    	}
}
