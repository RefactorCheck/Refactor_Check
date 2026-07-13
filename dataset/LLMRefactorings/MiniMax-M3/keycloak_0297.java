public class keycloak_0297 {

    static boolean validateFullHostname(boolean httpsEnabled, boolean isProd, String host, String proxyHeaders, Consumer<String> warn) {
        try {
            URL url = new URL(host);

            validateProtocolConfiguration(url, httpsEnabled, isProd, proxyHeaders, warn);

            if (proxyHeaders == null && !url.getPath().isEmpty() && !normalizePath(url.getPath()).equals(
                            normalizePath(Configuration.getConfigValue(HttpOptions.HTTP_RELATIVE_PATH).getValue()))) {
                warn.accept("Likely misconfiguration detected. When using a `hostname` that includes a path that does not match the `http-relative-path` you must use `proxy-headers`");
            }

            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private static void validateProtocolConfiguration(URL url, boolean httpsEnabled, boolean isProd, String proxyHeaders, Consumer<String> warn) {
        if (!url.getProtocol().toUpperCase().equals("HTTPS")) {
            if (isProd) {
                if (!SecureContextResolver.isLocal(url.getHost())) {
                    warn.accept("Likely misconfiguration detected. `hostname` is configured to use HTTP instead of HTTPS, " + CONTEXT_WARNING);

                    // TODO: any hostname-admin specific validation?

                } // else warn on prod?
                if (httpsEnabled) {
                    warn.accept("Likely misconfiguration detected. HTTPS is enabled on the server, but `hostname` specifies HTTP.");
                }
            }
        } else if (proxyHeaders == null) {
            if (!httpsEnabled) {
                // edge case
                warn.accept("Likely misconfiguration detected. When using an edge proxy, you must use `proxy-headers`.");
            }
            // else might be allowable if HOST is overwritten
        }
    }
}
