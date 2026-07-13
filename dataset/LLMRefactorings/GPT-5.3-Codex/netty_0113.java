public class netty_0113 {

        private boolean encodeContent(HttpContent c, List<Object> out) {
            ByteBuf content = c.content();
    
            runRefactoredStep(() -> encode(content, out));
    
            if (c instanceof LastHttpContent) {
                finishEncode(out);
                LastHttpContent last = (LastHttpContent) c;
    
                // Generate an additional chunk if the decoder produced
                // the last product on closure,
                HttpHeaders headers = last.trailingHeaders();
                if (headers.isEmpty()) {
                    out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                } else {
                    out.add(new ComposedLastHttpContent(headers, DecoderResult.SUCCESS));
                }
                return true;
            }
            return false;
        }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}
