public class netty_0113 {

        private boolean encodeContent(HttpContent c, List<Object> out) {
            ByteBuf content = c.content();
    
            encode(content, out);
    
            if (c instanceof LastHttpContent) {
                finishEncode(out);
                handleLastHttpContent((LastHttpContent) c, out);
                return true;
            }
            return false;
        }

        private void handleLastHttpContent(LastHttpContent last, List<Object> out) {
            HttpHeaders headers = last.trailingHeaders();
            if (headers.isEmpty()) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
            } else {
                out.add(new ComposedLastHttpContent(headers, DecoderResult.SUCCESS));
            }
        }
}
