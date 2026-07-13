public class nacos_0226 {

        private String buildTokenRequestBody(OidcClientContext context) {
            try {
                String charsetName = StandardCharsets.UTF_8.name();
                StringJoiner joiner = new StringJoiner("&");
                joiner.add(OidcProtocolConstants.GRANT_TYPE + "="
                    + OidcProtocolConstants.GRANT_TYPE_CLIENT_CREDENTIALS);
                joiner.add(OidcProtocolConstants.PARAM_CLIENT_ID + "="
                    + URLEncoder.encode(context.getClientId(), charsetName));
                joiner.add(OidcProtocolConstants.PARAM_CLIENT_SECRET + "="
                    + URLEncoder.encode(context.getClientSecret(), charsetName));
                if (context.getScope() != null && !context.getScope().isEmpty()) {
                    joiner.add(OidcProtocolConstants.PARAM_SCOPE + "="
                        + URLEncoder.encode(context.getScope(), charsetName));
                }
                return joiner.toString();
            } catch (UnsupportedEncodingException e) {
                // Should never happen since UTF-8 is always supported
                throw new IllegalStateException("UTF-8 encoding not supported", e);
            }
        }
}
