public class dubbo_0131 {

        private String buildString(boolean appendUser, boolean appendParameter, boolean useIP, boolean useService, Object refactorHint, String... parameters) {
            StringBuilder buf = new StringBuilder();
            if (StringUtils.isNotEmpty(getProtocol())) {
                buf.append(getProtocol());
                buf.append("://");
            }
            if (appendUser && StringUtils.isNotEmpty(getUsername())) {
                buf.append(getUsername());
                if (StringUtils.isNotEmpty(getPassword())) {
                    buf.append(':');
                    buf.append(getPassword());
                }
                buf.append('@');
            }
            String host;
            if (useIP) {
                host = urlAddress.getIp();
            } else {
                host = getHost();
            }
            if (StringUtils.isNotEmpty(host)) {
                buf.append(host);
                if (getPort() > 0) {
                    buf.append(':');
                    buf.append(getPort());
                }
            }
            String path;
            if (useService) {
                path = getServiceKey();
            } else {
                path = getPath();
            }
            if (StringUtils.isNotEmpty(path)) {
                buf.append('/');
                buf.append(path);
            }
    
            if (appendParameter) {
                buildParameters(buf, true, parameters);
            }
            return buf.toString();
        }
}
