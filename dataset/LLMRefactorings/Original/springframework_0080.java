public class springframework_0080 {

    	protected Formatter<Date> getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
    		DateFormatter formatter = new DateFormatter();
    		formatter.setSource(annotation);
    		formatter.setIso(annotation.iso());
    
    		String style = resolveEmbeddedValue(annotation.style());
    		if (StringUtils.hasLength(style)) {
    			formatter.setStylePattern(style);
    		}
    
    		String pattern = resolveEmbeddedValue(annotation.pattern());
    		if (StringUtils.hasLength(pattern)) {
    			formatter.setPattern(pattern);
    		}
    
    		List<String> resolvedFallbackPatterns = new ArrayList<>();
    		for (String fallbackPattern : annotation.fallbackPatterns()) {
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
