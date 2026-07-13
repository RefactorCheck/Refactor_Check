public class keycloak_0080 {

    private static void setCookie(final String name, final String value, final String path, final String domain, final int maxAge, final boolean secure, final boolean httpOnly, HttpServerResponse response) {
        response.setResponseCookie(new HttpServerCookie() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public String getDomain() {
                return domain;
            }

            @Override
            public int getMaxAge() {
                return maxAge;
            }

            @Override
            public String getPath() {
                return path;
            }

            @Override
            public boolean isSecure() {
                return secure;
            }

            @Override
            public int getVersion() {
                return 0;
            }

            @Override
            public boolean isHttpOnly() {
                return httpOnly;
            }
        });
    }
}
