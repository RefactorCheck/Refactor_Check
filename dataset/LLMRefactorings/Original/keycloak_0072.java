public class keycloak_0072 {

        static boolean isSecureContext(URI uri, Supplier<DeviceRepresentation> deviceRepresentationSupplier, String referer, String secFetchDest) {
            if (uri.getScheme().equals("https")) {
                // Per the W3C Secure Contexts spec, a page is only contextually secure if all its
                // ancestor contexts are also secure. An HTTPS iframe embedded in an HTTP parent page
                // is therefore not a secure context. Detect this using browser-sent fetch metadata.
                // See:
                // - https://github.com/keycloak/keycloak/issues/37355
                // - https://w3c.github.io/webappsec-secure-contexts/#is-settings-object-contextually-secure
                // - https://w3c.github.io/webappsec-secure-contexts/#examples-framed
                if ("iframe".equals(secFetchDest) && isInsecureReferer(referer)) {
                    return false;
                }
                return true;
            }
    
            DeviceRepresentation deviceRepresentation = deviceRepresentationSupplier.get();
            String browser = deviceRepresentation != null ? deviceRepresentation.getBrowser() : null;
    
            // Safari has a bug where even a secure context is not able to set cookies with the 'Secure' directive.
            // Hence, we need to assume the worst case scenario and downgrade to an insecure context.
            // See:
            // - https://github.com/keycloak/keycloak/issues/33557
            // - https://webcompat.com/issues/142566
            // - https://bugs.webkit.org/show_bug.cgi?id=232088
            // - https://bugs.webkit.org/show_bug.cgi?id=276313
            if (browser != null && browser.toLowerCase().contains("safari")) {
                return false;
            }
    
            String host = uri.getHost();
    
            if (host == null) {
                return false;
            }
    
            return isLocal(host);
        }
}
