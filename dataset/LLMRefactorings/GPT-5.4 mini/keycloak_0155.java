public class keycloak_0155 {

        @Override
        public void set(CookieType cookieType, String value, int maxAge) {
            String name = cookieType.getName();
            NewCookie.SameSite sameSite = cookieType.getScope().getSameSite();
            if (NewCookie.SameSite.NONE.equals(sameSite) && !secure) {
                sameSite = NewCookie.SameSite.LAX;
            }
    
            String path = pathResolver.resolvePath(cookieType);
            boolean httpOnly = cookieType.getScope().isHttpOnly();
    
            NewCookie newCookie = new NewCookie.Builder(name)
                    .version(1)
                    .value(value)
                    .path(path)
                    .maxAge(maxAge)
                    .secure(secure)
                    .httpOnly(httpOnly)
                    .sameSite(sameSite)
                    .build();
    
            session.getContext().getHttpResponse().setCookieIfAbsent(newCookie);
    
            logger.tracef("Setting cookie: name: %s, path: %s, same-site: %s, secure: %s, http-only: %s, max-age: %d", name, path, sameSite, secure, httpOnly, maxAge);
    
            boolean proxiedRequest = false;
            if (!secure && !warned) {
                warned = true;
    
                StringBuilder warning = new StringBuilder("Non-secure context detected; cookies are not secured, and will not be available in cross-origin POST requests.");
    
                String forwarded = session.getContext().getRequestHeaders().getHeaderString("Forwarded");
                String xForwarded = session.getContext().getRequestHeaders().getHeaderString("X-Forwarded-Proto");
                proxiedRequest = forwarded != null || xForwarded != null;
    
                if (proxiedRequest) {
                    if (session.getContext().getHttpRequest().isProxyTrusted()) {
                        warning.append(" Please review your proxy settings as the request appears to have originated from a proxy.");
                    } else {
                        warning.append(" This is likely due to the proxy not being trusted.");
                    }
                } else {
                    warning.append(" Please review whether this direct HTTP usage is expected.");
                }
    
                logger.warnf(warning.toString());
            }
        }
}
