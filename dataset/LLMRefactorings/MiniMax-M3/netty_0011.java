public class netty_0011 {

    protected DefaultCookie initCookie(String header, int nameBegin, int nameEnd, int valueBegin, int valueEnd) {
        if (nameBegin == -1 || nameBegin == nameEnd) {
            logger.debug("Skipping cookie with null name");
            return null;
        }

        if (valueBegin == -1) {
            logger.debug("Skipping cookie with null value");
            return null;
        }

        CharSequence wrappedValue = CharBuffer.wrap(header, valueBegin, valueEnd);
        CharSequence unwrappedValue = unwrapValue(wrappedValue);
        if (unwrappedValue == null) {
            logger.debug("Skipping cookie because starting quotes are not properly balanced in '{}'",
                    wrappedValue);
            return null;
        }

        final String name = header.substring(nameBegin, nameEnd);

        if (!isValidCookieName(name)) {
            return null;
        }

        final boolean wrap = unwrappedValue.length() != valueEnd - valueBegin;

        if (!isValidCookieValue(unwrappedValue)) {
            return null;
        }

        DefaultCookie cookie = new DefaultCookie(name, unwrappedValue.toString());
        cookie.setWrap(wrap);
        return cookie;
    }

    private boolean isValidCookieName(String name) {
        if (strict) {
            int invalidOctetPos = firstInvalidCookieNameOctet(name);
            if (invalidOctetPos >= 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Skipping cookie because name '{}' contains invalid char '{}'",
                            name, name.charAt(invalidOctetPos));
                }
                return false;
            }
        }
        return true;
    }

    private boolean isValidCookieValue(CharSequence unwrappedValue) {
        if (strict) {
            int invalidOctetPos = firstInvalidCookieValueOctet(unwrappedValue);
            if (invalidOctetPos >= 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Skipping cookie because value '{}' contains invalid char '{}'",
                            unwrappedValue, unwrappedValue.charAt(invalidOctetPos));
                }
                return false;
            }
        }
        return true;
    }
}
