public class netty_0289 {

        static void validateHeaderName(CharSequence name) {
            checkNonEmpty(name, "name");
            // Since name may only contain ascii characters, for valid names
            // name.length() returns the number of bytes when UTF-8 encoded.
            if (name.length() > SPDY_MAX_NV_LENGTH) {
                throw new IllegalArgumentException(
                        "name exceeds allowable length: " + name);
            }
            for (int i = 0; i < name.length(); i ++) {
                char c = name.charAt(i);
                if (c == 0) {
                    throw new IllegalArgumentException(
                            "name contains null character: " + name);
                }
                if (c >= 'A' && c <= 'Z') {
                    throw new IllegalArgumentException("name must be all lower case.");
                }
                if (c > 127) {
                    throw new IllegalArgumentException(
                            "name contains non-ascii character: " + name);
                }
            }
        }
}
