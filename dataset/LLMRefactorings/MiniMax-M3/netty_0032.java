public class netty_0032 {

        private DefaultCookie initCookie(String name, String value) {
            if (name == null || name.length() == 0) {
                logger.debug("Skipping cookie with null name");
                return null;
            }
    
            if (value == null) {
                logger.debug("Skipping cookie with null value");
                return null;
            }
    
            CharSequence unwrappedValue = unwrapValue(value);
            if (unwrappedValue == null) {
                logger.debug("Skipping cookie because starting quotes are not properly balanced in '{}'",
                        unwrappedValue);
                return null;
            }
    
            int invalidOctetPos;
            if (strict && (invalidOctetPos = firstInvalidCookieNameOctet(name)) >= 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Skipping cookie because name '{}' contains invalid char '{}'",
                            name, name.charAt(invalidOctetPos));
                }
                return null;
            }
    
            final boolean wrap = unwrappedValue.length() != value.length();
    
            if (strict && (invalidOctetPos = firstInvalidCookieValueOctet(unwrappedValue)) >= 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Skipping cookie because value '{}' contains invalid char '{}'",
                            unwrappedValue, unwrappedValue.charAt(invalidOctetPos));
                }
                return null;
            }
    
            return createCookie(name, unwrappedValue, wrap);
        }
    
        private DefaultCookie createCookie(String name, CharSequence unwrappedValue, boolean wrap) {
            DefaultCookie cookie = new DefaultCookie(name, unwrappedValue.toString());
            cookie.setWrap(wrap);
            return cookie;
        }
}
