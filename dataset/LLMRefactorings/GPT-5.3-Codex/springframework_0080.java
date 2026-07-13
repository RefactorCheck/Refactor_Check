public class springframework_0080 {

    	protected Formatter<Date> getFormatter(DateTimeFormat annotationValue, Class<?> fieldType) {
    		DateFormatter formatter = new DateFormatter();
    		formatter.setSource(annotationValue);
    		formatter.setIso(annotationValue.iso());
    
    		String style = resolveEmbeddedValue(annotationValue.style());
    		if (StringUtils.hasLength(style)) {
    			formatter.setStylePattern(style);
    		}
    
    		String pattern = resolveEmbeddedValue(annotationValue.pattern());
    		if (StringUtils.hasLength(pattern)) {
    			formatter.setPattern(pattern);
    		}
    
    		List<String> resolvedFallbackPatterns = new ArrayList<>();
    		for (String fallbackPattern : annotationValue.fallbackPatterns()) {
    			String resolvedFallbackPattern = resolveEmbeddedValue(fallbackPattern);
    			if (StringUtils.hasLength(resolvedFallbackPattern)) {
    				resolvedFallbackPatterns.add(resolvedFallbackPattern);
    			}
    		}
    		if (!resolvedFallbackPatterns.isEmpty()) {
    			formatter.setFallbackPatterns(resolvedFallbackPatterns.toArray(new String[0]));
    		}
    
    		return formatter;
    	}
}
