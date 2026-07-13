public class netty_0157 {

        private boolean setOrigin(final HttpResponse response) {
            final String origin = request.headers().get(HttpHeaderNames.ORIGIN);
            if (origin != null && config != null) {
                if (NULL_ORIGIN.equals(origin) && config.isNullOriginAllowed()) {
                    setNullOrigin(response);
                    return true;
                }
                if (handleAnyOrigin(response)) {
                    return true;
                }
                if (config.origins().contains(origin)) {
                    setOrigin(response, origin);
                    setVaryHeader(response);
                    return true;
                }
                logger.debug("Request origin [{}]] was not among the configured origins [{}]", origin, config.origins());
            }
            return false;
        }

        private boolean handleAnyOrigin(final HttpResponse response) {
            if (!config.isAnyOriginSupported()) {
                return false;
            }
            if (config.isCredentialsAllowed()) {
                echoRequestOrigin(response);
                setVaryHeader(response);
            } else {
                setAnyOrigin(response);
            }
            return true;
        }
}
