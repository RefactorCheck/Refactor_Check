public class dubbo_0216 {

        @Override
        public String toString() {
            if (rawAddress != null) {
                return rawAddress;
            }
    
            StringBuilder buf = new StringBuilder();
            if (StringUtils.isNotEmpty(protocol)) {
                buf.append(protocol);
                buf.append("://");
            }
    
            appendHostAndPort(buf);
    
            if (StringUtils.isNotEmpty(path)) {
                buf.append('/');
                buf.append(path);
            }
    
            return buf.toString();
        }
    
        private void appendHostAndPort(StringBuilder buf) {
            if (StringUtils.isNotEmpty(host)) {
                buf.append(host);
                if (port > 0) {
                    buf.append(':');
                    buf.append(port);
                }
            }
        }
}
