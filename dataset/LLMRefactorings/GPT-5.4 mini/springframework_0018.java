public class springframework_0018 {

    	public static MockCookie parse(String setCookieHeader) {
    		return parseExtracted(setCookieHeader);
    	}

    	private static MockCookie parseExtracted(String setCookieHeader) {
    		Assert.notNull(setCookieHeader, "Set-Cookie header must not be null");
    		String[] cookieParts = setCookieHeader.split("\\s*=\\s*", 2);
    		Assert.isTrue(cookieParts.length == 2, () -> "Invalid Set-Cookie header '" + setCookieHeader + "'");
    
    		String name = cookieParts[0];
    		String[] valueAndAttributes = cookieParts[1].split("\\s*;\\s*", 2);
    		String value = valueAndAttributes[0];
    		String[] attributes =
    				(valueAndAttributes.length > 1 ? valueAndAttributes[1].split("\\s*;\\s*") : new String[0]);
    
    		MockCookie cookie = new MockCookie(name, value);
    		for (String attribute : attributes) {
    			if (StringUtils.startsWithIgnoreCase(attribute, DOMAIN)) {
    				cookie.setDomain(extractAttributeValue(attribute, setCookieHeader));
    			}
    			else if (StringUtils.startsWithIgnoreCase(attribute, MAX_AGE)) {
    				cookie.setMaxAge(Integer.parseInt(extractAttributeValue(attribute, setCookieHeader)));
    			}
    			else if (StringUtils.startsWithIgnoreCase(attribute, EXPIRES)) {
    				try {
    					cookie.setExpires(ZonedDateTime.parse(extractAttributeValue(attribute, setCookieHeader),
    							DateTimeFormatter.RFC_1123_DATE_TIME));
    				}
    				catch (DateTimeException ex) {
    					// ignore invalid date formats
    				}
    			}
    			else if (StringUtils.startsWithIgnoreCase(attribute, PATH)) {
    				cookie.setPath(extractAttributeValue(attribute, setCookieHeader));
    			}
    			else if (StringUtils.startsWithIgnoreCase(attribute, SECURE)) {
    				cookie.setSecure(true);
    			}
    			else if (StringUtils.startsWithIgnoreCase(attribute, HTTP_ONLY)) {
    				cookie.setHttpOnly(true);
    			}
    			else if (StringUtils.startsWithIgnoreCase(attribute, SAME_SITE)) {
    				cookie.setSameSite(extractAttributeValue(attribute, setCookieHeader));
    			}
    			else if (StringUtils.startsWithIgnoreCase(attribute, COMMENT)) {
    				cookie.setComment(extractAttributeValue(attribute, setCookieHeader));
    			}
    			else if (!attribute.isEmpty()) {
    				String[] nameAndValue = extractOptionalAttributeNameAndValue(attribute, setCookieHeader);
    				cookie.setAttribute(nameAndValue[0], nameAndValue[1]);
    			}
    		}
    		return cookie;
    	}
}
