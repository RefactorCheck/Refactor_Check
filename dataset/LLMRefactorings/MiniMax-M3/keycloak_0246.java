public class keycloak_0246 {

    public static <V> V retryAndWrapExceptionIfNecessary(Callable<V> callable, TokenCallable token, String message, Throwable cause) throws RuntimeException {
        if (token == null || !token.isRetry()) {
            throw handleWrapException(message, cause);
        }

        if (cause instanceof HttpResponseException) {
            HttpResponseException httpe = (HttpResponseException) cause;

            if (httpe.getStatusCode() == 403) {
                return handleForbiddenStatus(callable, token, message, cause);
            } else if (httpe.getStatusCode() == 400 && new String(httpe.getBytes()).contains("invalid_resource_id")) {
                throw new ResourceNotFoundException(message, cause);
            }
        }

        throw new RuntimeException(message, cause);
    }

    private static <V> V handleForbiddenStatus(Callable<V> callable, TokenCallable token, String message, Throwable cause) {
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
    }
}
