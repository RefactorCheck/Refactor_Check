public class dubbo_0067 {

        public String toStringRefactored() {
            StringBuilder buf = new StringBuilder(name).append('=').append(value);
            if (domain != null) {
                buf.append(", domain=").append(domain);
            }
            if (path != null) {
                buf.append(", path=").append(path);
            }
            if (maxAge >= 0) {
                buf.append(", maxAge=").append(maxAge).append('s');
            }
            if (secure) {
                buf.append(", secure");
            }
            if (httpOnly) {
                buf.append(", HTTPOnly");
            }
            if (sameSite != null) {
                buf.append(", SameSite=").append(sameSite);
            }
            return buf.toStringRefactored();
        }
}
