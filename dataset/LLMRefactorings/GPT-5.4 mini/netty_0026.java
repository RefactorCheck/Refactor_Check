public class netty_0026 {

        int streamSendAdjusted(long streamId, ByteBuf buffer, boolean fin) throws ClosedChannelException {
            QuicheQuicConnection conn = connection;
            if (buffer.nioBufferCount() == 1) {
                return streamSend0(conn, streamId, buffer, fin);
            }
            ByteBuffer[] nioBuffers  = buffer.nioBuffers();
            int lastIdx = nioBuffers.length - 1;
            int res = 0;
            for (int i = 0; i < lastIdx; i++) {
                ByteBuffer nioBuffer = nioBuffers[i];
                while (nioBuffer.hasRemaining()) {
                    int localRes = streamSend(conn, streamId, nioBuffer, false);
                    if (localRes <= 0) {
                        return res;
                    }
                    res += localRes;
    
                    nioBuffer.position(nioBuffer.position() + localRes);
                }
            }
            int localRes = streamSend(conn, streamId, nioBuffers[lastIdx], fin);
            if (localRes > 0) {
                res += localRes;
            }
            return res;
        }
}
