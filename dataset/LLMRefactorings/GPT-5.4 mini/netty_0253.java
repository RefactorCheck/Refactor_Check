public class netty_0253 {

        @SuppressWarnings("deprecation")
        private SpdyHeadersFrame createHeadersFrameMini(HttpResponse httpResponse) throws Exception {
            // Get the Stream-ID from the headers
            final HttpHeaders httpHeaders = httpResponse.headers();
            int streamId = httpHeaders.getInt(SpdyHttpHeaders.Names.STREAM_ID);
            httpHeaders.remove(SpdyHttpHeaders.Names.STREAM_ID);
    
            // The Connection, Keep-Alive, Proxy-Connection, and Transfer-Encoding
            // headers are not valid and MUST not be sent.
            httpHeaders.remove(HttpHeaderNames.CONNECTION);
            httpHeaders.remove("Keep-Alive");
            httpHeaders.remove("Proxy-Connection");
            httpHeaders.remove(HttpHeaderNames.TRANSFER_ENCODING);
    
            SpdyHeadersFrame spdyHeadersFrame;
            if (SpdyCodecUtil.isServerId(streamId)) {
                spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamId, validateHeaders);
            } else {
                spdyHeadersFrame = new DefaultSpdySynReplyFrame(streamId, validateHeaders);
            }
            SpdyHeaders frameHeaders = spdyHeadersFrame.headers();
            // Unfold the first line of the response into name/value pairs
            frameHeaders.set(SpdyHeaders.HttpNames.STATUS, httpResponse.status().codeAsText());
            frameHeaders.set(SpdyHeaders.HttpNames.VERSION, httpResponse.protocolVersion().text());
    
            // Transfer the remaining HTTP headers
            Iterator<Entry<CharSequence, CharSequence>> itr = httpHeaders.iteratorCharSequence();
            while (itr.hasNext()) {
                Map.Entry<CharSequence, CharSequence> entry = itr.next();
                final CharSequence headerName =
                        headersToLowerCase ? AsciiString.of(entry.getKey()).toLowerCase() : entry.getKey();
                spdyHeadersFrame.headers().add(headerName, entry.getValue());
            }
    
            currentStreamId = streamId;
            spdyHeadersFrame.setLast(isLast(httpResponse));
    
            return spdyHeadersFrame;
        }
}
