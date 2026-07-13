public class springframework_0210 {

	private static final String HTTPS_SCHEME = "https";
	private static final String FORWARDED_HEADER = "Forwarded";
	private static final String X_FORWARDED_PROTO_HEADER = "X-Forwarded-Proto";
	private static final String X_FORWARDED_HOST_HEADER = "X-Forwarded-Host";
	private static final String X_FORWARDED_PORT_HEADER = "X-Forwarded-Port";
	private static final String COMMA_DELIMITER = ",";

	public static UriComponentsBuilder adaptFromForwardedHeaders(URI uri, HttpHeaders headers) {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(uri);
		try {
			String forwardedHeader = headers.getFirst(FORWARDED_HEADER);
			if (StringUtils.hasText(forwardedHeader)) {
				Matcher matcher = FORWARDED_PROTO_PATTERN.matcher(forwardedHeader);
				if (matcher.find()) {
					uriComponentsBuilder.scheme(matcher.group(1).trim());
					uriComponentsBuilder.port(null);
				}
				else if (isForwardedSslOn(headers)) {
					uriComponentsBuilder.scheme(HTTPS_SCHEME);
					uriComponentsBuilder.port(null);
				}
				matcher = FORWARDED_HOST_PATTERN.matcher(forwardedHeader);
				if (matcher.find()) {
					adaptForwardedHost(uriComponentsBuilder, matcher.group(1).trim());
				}
			}
			else {
				String protocolHeader = headers.getFirst(X_FORWARDED_PROTO_HEADER);
				if (StringUtils.hasText(protocolHeader)) {
					uriComponentsBuilder.scheme(StringUtils.tokenizeToStringArray(protocolHeader, COMMA_DELIMITER)[0]);
					uriComponentsBuilder.port(null);
				}
				else if (isForwardedSslOn(headers)) {
					uriComponentsBuilder.scheme(HTTPS_SCHEME);
					uriComponentsBuilder.port(null);
				}
				String hostHeader = headers.getFirst(X_FORWARDED_HOST_HEADER);
				if (StringUtils.hasText(hostHeader)) {
					adaptForwardedHost(uriComponentsBuilder, StringUtils.tokenizeToStringArray(hostHeader, COMMA_DELIMITER)[0]);
				}
				String portHeader = headers.getFirst(X_FORWARDED_PORT_HEADER);
				if (StringUtils.hasText(portHeader)) {
					uriComponentsBuilder.port(Integer.parseInt(StringUtils.tokenizeToStringArray(portHeader, COMMA_DELIMITER)[0]));
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
