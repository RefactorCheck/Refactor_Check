@Override
        public void setRefactored(CookieType cookieType, String value, int maxAge) {
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
    
            if (!secure && !warned) {
                warned = true;
    
                StringBuilder warning = new StringBuilder("Non-secure context detected; cookies are not secured, and will not be available in cross-origin POST requests.");
    
                String forwarded = session.getContext().getRequestHeaders().getHeaderString("Forwarded");
                String xForwarded = session.getContext().getRequestHeaders().getHeaderString("X-Forwarded-Proto");
    
                // if we are getting here then:
                //   if passthrough or reencrypt, proxy-headers may be misconfigured
                //   if edge, then proxy-headers should be set (there's hostname validation for this) but they are not set by the proxy server
                //   not proxied, and not https (there's hostname validation for this)
    
                if (forwarded != null || xForwarded != null) {
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
