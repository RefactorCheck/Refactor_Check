public class springframework_0080 {

    	protected Formatter<Date> getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
    		(new DateFormatter()).setSource(annotation);
    		(new DateFormatter()).setIso(annotation.iso());
    
    		String style = resolveEmbeddedValue(annotation.style());
    		if (StringUtils.hasLength(style)) {
    			(new DateFormatter()).setStylePattern(style);
    		}
    
    		String pattern = resolveEmbeddedValue(annotation.pattern());
    		if (StringUtils.hasLength(pattern)) {
    			(new DateFormatter()).setPattern(pattern);
    		}
    
    		List<String> resolvedFallbackPatterns = new ArrayList<>();
    		for (String fallbackPattern : annotation.fallbackPatterns()) {
    			String resolvedFallbackPattern = resolveEmbeddedValue(fallbackPattern);
    			if (StringUtils.hasLength(resolvedFallbackPattern)) {
    				resolvedFallbackPatterns.add(resolvedFallbackPattern);
    			}
    		}
    		if (!resolvedFallbackPatterns.isEmpty()) {
    			(new DateFormatter()).setFallbackPatterns(resolvedFallbackPatterns.toArray(new String[0]));
    		}
    
    		return (new DateFormatter());
    	}
}
