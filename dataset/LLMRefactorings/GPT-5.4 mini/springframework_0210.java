public class springframework_0210 {

    	public static UriComponentsBuilder adaptFromForwardedHeadersRefactored(URI uri, HttpHeaders headers) {
    		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(uri);
    		try {
    			String forwardedHeader = headers.getFirst("Forwarded");
    			if (StringUtils.hasText(forwardedHeader)) {
    				Matcher matcher = FORWARDED_PROTO_PATTERN.matcher(forwardedHeader);
    				if (matcher.find()) {
    					uriComponentsBuilder.scheme(matcher.group(1).trim());
    					uriComponentsBuilder.port(null);
    				}
    				else if (isForwardedSslOn(headers)) {
    					uriComponentsBuilder.scheme("https");
    					uriComponentsBuilder.port(null);
    				}
    				matcher = FORWARDED_HOST_PATTERN.matcher(forwardedHeader);
    				if (matcher.find()) {
    					adaptForwardedHost(uriComponentsBuilder, matcher.group(1).trim());
    				}
    			}
    			else {
    				String protocolHeader = headers.getFirst("X-Forwarded-Proto");
    				if (StringUtils.hasText(protocolHeader)) {
    					uriComponentsBuilder.scheme(StringUtils.tokenizeToStringArray(protocolHeader, ",")[0]);
    					uriComponentsBuilder.port(null);
    				}
    				else if (isForwardedSslOn(headers)) {
    					uriComponentsBuilder.scheme("https");
    					uriComponentsBuilder.port(null);
    				}
    				String hostHeader = headers.getFirst("X-Forwarded-Host");
    				if (StringUtils.hasText(hostHeader)) {
    					adaptForwardedHost(uriComponentsBuilder, StringUtils.tokenizeToStringArray(hostHeader, ",")[0]);
    				}
    				String portHeader = headers.getFirst("X-Forwarded-Port");
    				if (StringUtils.hasText(portHeader)) {
    					uriComponentsBuilder.port(Integer.parseInt(StringUtils.tokenizeToStringArray(portHeader, ",")[0]));
    				}
    			}
    		}
    		catch (NumberFormatException ex) {
    			throw new IllegalArgumentException("Failed to parse a port from \"forwarded\"-type headers. " +
    					"If not behind a trusted proxy, consider using ForwardedHeaderFilter " +
    					"with removeOnly=true. Request headers: " + headers);
    		}
    
    		uriComponentsBuilder.resetPortIfDefaultForScheme();
    
    		return uriComponentsBuilder;
    	}
}
