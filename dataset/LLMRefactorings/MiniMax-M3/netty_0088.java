public class netty_0088 {

        private int writePlaintextData(final ByteBuffer src, int len) {
            final int pos = src.position();

            if (src.isDirect()) {
                final int sslWrote = SSL.writeToSSL(ssl, bufferAddress(src) + pos, len);
                if (sslWrote > 0) {
                    src.position(pos + sslWrote);
                }
                return sslWrote;
            }
            return writeNonDirectData(src, len, pos);
        }

        private int writeNonDirectData(final ByteBuffer src, int len, final int pos) {
            final int limit = src.limit();
            final ByteBuf buf = alloc.directBuffer(len);
            try {
                src.limit(pos + len);
                buf.setBytes(0, src);
                src.limit(limit);

                final int sslWrote = SSL.writeToSSL(ssl, memoryAddress(buf), len);
                if (sslWrote > 0) {
                    src.position(pos + sslWrote);
                } else {
                    src.position(pos);
                }
                return sslWrote;
            } finally {
                buf.release();
            }
        }
}
