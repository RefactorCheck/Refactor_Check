public class keycloak_0246 {

        private static final int FORBIDDEN_STATUS = 403;
        private static final int BAD_REQUEST_STATUS = 400;
        private static final String INVALID_RESOURCE_ID_MESSAGE = "invalid_resource_id";

        public static <V> V retryAndWrapExceptionIfNecessary(Callable<V> callable, TokenCallable token, String message, Throwable cause) throws RuntimeException {
            if (token == null || !token.isRetry()) {
                throw handleWrapException(message, cause);
            }

            if (cause instanceof HttpResponseException) {
                HttpResponseException httpe = HttpResponseException.class.cast(cause);

                if (httpe.getStatusCode() == FORBIDDEN_STATUS) {
                    TokenIntrospectionResponse response = token.getHttp().<TokenIntrospectionResponse>post(token.getServerConfiguration().getIntrospectionEndpoint())
                            .authentication()
                            .client()
                            .param("token", token.call())
                            .response().json(TokenIntrospectionResponse.class).execute();

                    if (!response.getActive()) {
                        token.clearTokens();
                        try {
                            return callable.call();
                        } catch (Exception e) {
                            throw handleWrapException(message, e);
                        }
                    }

                    throw handleWrapException(message, cause);
                } else if (httpe.getStatusCode() == BAD_REQUEST_STATUS && new String(httpe.getBytes()).contains(INVALID_RESOURCE_ID_MESSAGE)) {
                    throw new ResourceNotFoundException(message, cause);
                }
            }

            throw new RuntimeException(message, cause);
        }
}
