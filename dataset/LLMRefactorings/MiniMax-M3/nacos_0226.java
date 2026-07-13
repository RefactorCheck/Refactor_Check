public class nacos_0226 {

    private String buildTokenRequestBody(OidcClientContext context) {
        try {
            String charsetName = StandardCharsets.UTF_8.name();
            StringJoiner joiner = new StringJoiner("&");
            encodeAndAdd(joiner, OidcProtocolConstants.GRANT_TYPE,
                OidcProtocolConstants.GRANT_TYPE_CLIENT_CREDENTIALS, charsetName);
            encodeAndAdd(joiner, OidcProtocolConstants.PARAM_CLIENT_ID,
                context.getClientId(), charsetName);
            encodeAndAdd(joiner, OidcProtocolConstants.PARAM_CLIENT_SECRET,
                context.getClientSecret(), charsetName);
            if (context.getScope() != null && !context.getScope().isEmpty()) {
                encodeAndAdd(joiner, OidcProtocolConstants.PARAM_SCOPE,
                    context.getScope(), charsetName);
            }
            return joiner.toString();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not supported", e);
        }
    }

    private void encodeAndAdd(StringJoiner joiner, String key, String value, String charsetName)
            throws UnsupportedEncodingException {
        joiner.add(key + "=" + URLEncoder.encode(value, charsetName));
    }
}
