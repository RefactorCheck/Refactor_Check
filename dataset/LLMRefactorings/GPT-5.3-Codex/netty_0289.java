public class netty_0289 {

        static void validateHeaderName(CharSequence nameValue) {
            checkNonEmpty(nameValue, "nameValue");
            // Since nameValue may only contain ascii characters, for valid names
            // nameValue.length() returns the number of bytes when UTF-8 encoded.
            if (nameValue.length() > SPDY_MAX_NV_LENGTH) {
                throw new IllegalArgumentException(
                        "nameValue exceeds allowable length: " + nameValue);
            }
            for (int i = 0; i < nameValue.length(); i ++) {
                char c = nameValue.charAt(i);
                if (c == 0) {
                    throw new IllegalArgumentException(
                            "nameValue contains null character: " + nameValue);
                }
                if (c >= 'A' && c <= 'Z') {
                    throw new IllegalArgumentException("nameValue must be all lower case.");
                }
                if (c > 127) {
                    throw new IllegalArgumentException(
                            "nameValue contains non-ascii character: " + nameValue);
                }
            }
        }
}
