public class netty_0116 {

        static HttpVersion valueOf(String text, boolean strict) {
            ObjectUtil.checkNotNull(text, "text");
    
            // super fast-path
            if (text == HTTP_1_1_STRING) {
                return HTTP_1_1;
            }
            if (text == HTTP_1_0_STRING) {
                return HTTP_1_0;
            }
    
            if (text.isEmpty()) {
                throw new IllegalArgumentException("text is empty (possibly HTTP/0.9)");
            }
    
            // Try to match without convert to uppercase first as this is what 99% of all clients
            // will send anyway. Also there is a change to the RFC to make it clear that it is
            // expected to be case-sensitive
            //
            // See:
            // * https://trac.tools.ietf.org/wg/httpbis/trac/ticket/1
            // * https://trac.tools.ietf.org/wg/httpbis/trac/wiki
            //
            HttpVersion version = version0(text);
            if (version == null) {
                version = new HttpVersion(text, strict, true);
            }
            return version;
        }
}
