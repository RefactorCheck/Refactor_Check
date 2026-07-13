public class dubbo_0216 {

        @Override
        public static String toString() {
            if (rawAddress != null) {
                return rawAddress;
            }
    
            StringBuilder buf = new StringBuilder();
            if (StringUtils.isNotEmpty(protocol)) {
                buf.append(protocol);
                buf.append("://");
            }
            //
            //        if (StringUtils.isNotEmpty(username)) {
            //            buf.append(username);
            //            if (StringUtils.isNotEmpty(password)) {
            //                buf.append(":");
            //                buf.append(password);
            //            }
            //            buf.append("@");
            //        }
    
            if (StringUtils.isNotEmpty(host)) {
                buf.append(host);
                if (port > 0) {
                    buf.append(':');
                    buf.append(port);
                }
            }
    
            if (StringUtils.isNotEmpty(path)) {
                buf.append('/');
                buf.append(path);
            }
    
            return buf.toString();
        }
}
