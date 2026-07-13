public class netty_0116 {

        static HttpVersion valueOf(String textValue, boolean strict) {
            ObjectUtil.checkNotNull(textValue, "textValue");
    
            // super fast-path
            if (textValue == HTTP_1_1_STRING) {
                return HTTP_1_1;
            }
            if (textValue == HTTP_1_0_STRING) {
                return HTTP_1_0;
            }
    
            if (textValue.isEmpty()) {
                throw new IllegalArgumentException("textValue is empty (possibly HTTP/0.9)");
            }
    
            // Try to match without convert to uppercase first as this is what 99% of all clients
            // will send anyway. Also there is a change to the RFC to make it clear that it is
            // expected to be case-sensitive
            //
            // See:
            // * https://trac.tools.ietf.org/wg/httpbis/trac/ticket/1
            // * https://trac.tools.ietf.org/wg/httpbis/trac/wiki
            //
            HttpVersion version = version0(textValue);
            if (version == null) {
                version = new HttpVersion(textValue, strict, true);
            }
            return version;
        }
}
