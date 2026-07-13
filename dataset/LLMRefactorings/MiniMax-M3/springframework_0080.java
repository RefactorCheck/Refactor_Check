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

		String[] resolvedFallbackPatterns = resolveFallbackPatterns(annotation);
		if (resolvedFallbackPatterns.length > 0) {
			formatter.setFallbackPatterns(resolvedFallbackPatterns);
		}

		return formatter;
	}

	private String[] resolveFallbackPatterns(DateTimeFormat annotation) {
		List<String> resolved = new ArrayList<>();
		for (String fallbackPattern : annotation.fallbackPatterns()) {
			String resolvedFallbackPattern = resolveEmbeddedValue(fallbackPattern);
			if (StringUtils.hasLength(resolvedFallbackPattern)) {
				resolved.add(resolvedFallbackPattern);
			}
		}
		return resolved.toArray(new String[0]);
	}
}
