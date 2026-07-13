public class netty_0146 {

        @Override
        protected void sanitizeHeadersBeforeEncode(HttpResponse msg, boolean isAlwaysEmpty) {
            if (isAlwaysEmpty) {
                HttpResponseStatus status = msg.status();
                if (status.codeClass() == HttpStatusClass.INFORMATIONAL ||
                        status.code() == HttpResponseStatus.NO_CONTENT.code()) {
                    removeContentLengthAndTransferEncoding(msg);
                } else if (status.code() == HttpResponseStatus.RESET_CONTENT.code()) {
                    removeTransferEncodingAndSetContentLengthZero(msg);
                }
            }
        }

        private void removeContentLengthAndTransferEncoding(HttpResponse msg) {
            // Stripping Content-Length:
            // See https://tools.ietf.org/html/rfc7230#section-3.3.2
            msg.headers().remove(HttpHeaderNames.CONTENT_LENGTH);

            // Stripping Transfer-Encoding:
            // See https://tools.ietf.org/html/rfc7230#section-3.3.1
            msg.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
        }

        private void removeTransferEncodingAndSetContentLengthZero(HttpResponse msg) {
            // Stripping Transfer-Encoding:
            msg.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);

            // Set Content-Length: 0
            // https://httpstatuses.com/205
            msg.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, 0);
        }
}
