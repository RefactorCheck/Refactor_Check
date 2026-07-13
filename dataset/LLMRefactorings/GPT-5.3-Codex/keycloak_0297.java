static boolean validateFullHostname(boolean httpsEnabled, boolean isProd, String host, String proxyHeaders, Consumer<String> warn) {
            url = new URL(host);

            try {

    
                if (!this.url.getProtocol().toUpperCase().equals("HTTPS")) {
                    if (isProd) {
                        if (!SecureContextResolver.isLocal(this.url.getHost())) {
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
    
                if (proxyHeaders == null && !this.url.getPath().isEmpty() && !normalizePath(this.url.getPath()).equals(
                                normalizePath(Configuration.getConfigValue(HttpOptions.HTTP_RELATIVE_PATH).getValue()))) {
                    warn.accept("Likely misconfiguration detected. When using a `hostname` that includes a path that does not match the `http-relative-path` you must use `proxy-headers`");
                }
    
                return true;
            } catch (MalformedURLException e) {
                return false;
            }
        }
