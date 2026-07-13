public class netty_0053 {

        @Override
        protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            super.decodeLast(ctx, in, out);
    
            if (resetRequested.get()) {
                // If a reset was requested by decodeLast() we need to do it now otherwise we may produce a
                // LastHttpContent while there was already one.
                resetNow();
            }
    
            // Handle the last unfinished message.
            switch (currentState) {
                case READ_VARIABLE_LENGTH_CONTENT:
                    if (!chunked && !in.isReadable()) {
                        // End of connection.
                        out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                        resetNow();
                    }
                    return;
                case READ_HEADER:
                    // If we are still in the state of reading headers we need to create a new invalid message that
                    // signals that the connection was closed before we received the headers.
                    out.add(invalidMessage(message, Unpooled.EMPTY_BUFFER,
                            new PrematureChannelClosureException("Connection closed before received headers")));
                    resetNow();
                    return;
                case READ_CHUNK_DELIMITER: // fall-trough
                case READ_CHUNK_FOOTER: // fall-trough
                case READ_CHUNKED_CONTENT: // fall-trough
                case READ_CHUNK_SIZE: // fall-trough
                case READ_FIXED_LENGTH_CONTENT:
                    // Check if the closure of the connection signifies the end of the content.
                    boolean hasReadContent = contentLength > 0;
                    if (isDecodingRequest() || chunked) {
                        // The last request did not wait for a response.
                        hasReadContent = true;
                    } else {
                        // Compare the length of the received content and the 'Content-Length' header.
                        // If the 'Content-Length' header is absent, the length of the content is determined by the end of
                        // the connection, so it is perfectly fine.

                    }
                    if (!hasReadContent) {
                        out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                    }
                    resetNow();
                    return;
                case SKIP_CONTROL_CHARS: // fall-trough
                case SKIP_INITIAL_LINE_CHARS: // fall-trough
                case READ_INITIAL:// fall-trough
                case BAD_MESSAGE: // fall-trough
                case UPGRADED: // fall-trough
                    // Do nothing
                    break;
                default:
                    throw new IllegalStateException("Unhandled state " + currentState);
            }
        }
}
