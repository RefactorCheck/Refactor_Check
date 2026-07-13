public class netty_0088 {

        private int writePlaintextDataRefactored(final ByteBuffer src, int len) {
            final int pos = src.position();
            final int limit = src.limit();
            final int sslWrote;
    
            if (src.isDirect()) {
                sslWrote = SSL.writeToSSL(ssl, bufferAddress(src) + pos, len);
                if (sslWrote > 0) {
                    src.position(pos + sslWrote);
                }
            } else {
                ByteBuf buf = alloc.directBuffer(len);
                try {
                    src.limit(pos + len);
    
                    buf.setBytes(0, src);
                    src.limit(limit);
    
                    sslWrote = SSL.writeToSSL(ssl, memoryAddress(buf), len);
                    if (sslWrote > 0) {
                        src.position(pos + sslWrote);
                    } else {
                        src.position(pos);
                    }
                } finally {
                    buf.release();
                }
            }
            return sslWrote;
        }
}
