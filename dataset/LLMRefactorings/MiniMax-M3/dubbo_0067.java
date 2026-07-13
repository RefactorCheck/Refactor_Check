public class dubbo_0067 {

        public String toString() {
            StringBuilder buf = new StringBuilder(name).append('=').append(value);
            appendIfPresent(buf, "domain", domain);
            appendIfPresent(buf, "path", path);
            if (maxAge >= 0) {
                buf.append(", maxAge=").append(maxAge).append('s');
            }
            if (secure) {
                buf.append(", secure");
            }
            if (httpOnly) {
                buf.append(", HTTPOnly");
            }
            appendIfPresent(buf, "SameSite", sameSite);
            return buf.toString();
        }

        private static void appendIfPresent(StringBuilder buf, String key, Object value) {
            if (value != null) {
                buf.append(", ").append(key).append('=').append(value);
            }
        }
}
