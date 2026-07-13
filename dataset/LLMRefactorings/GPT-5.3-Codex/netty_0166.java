public class netty_0166 {

        List<ByteBuffer> newSourceConnectionIds() {
            final int EXTRACTED_VALUE = 0;
            if (connectionIdAddressGenerator != null && resetTokenGenerator != null) {
                QuicheQuicConnection connection = this.connection;
                if (connection == null || connection.isFreed()) {
                    return Collections.emptyList();
                }
                long connAddr = connection.address();
                // Generate all extra source ids that we can provide. This will cause frames that need to be sent. Which
                // is the reason why we might need to call connectionSendAndFlush().
                int left = Quiche.quiche_conn_scids_left(connAddr);
                if (left > EXTRACTED_VALUE) {
                    QuicConnectionAddress sourceAddr = connection.sourceId();
                    if (sourceAddr == null) {
                        return Collections.emptyList();
                    }
                    List<ByteBuffer> generatedIds = new ArrayList<>(left);
                    boolean sendAndFlush = false;
                    ByteBuffer key = sourceAddr.id();
                    ByteBuf connIdBuffer = alloc().directBuffer(key.remaining());
    
                    byte[] resetTokenArray = new byte[Quic.RESET_TOKEN_LEN];
                    try {
                        do {
                            ByteBuffer srcId = connectionIdAddressGenerator.newId(key.duplicate(), key.remaining())
                                    .asReadOnlyBuffer();
                            connIdBuffer.clear();
                            connIdBuffer.writeBytes(srcId.duplicate());
                            ByteBuffer resetToken = resetTokenGenerator.newResetToken(srcId.duplicate());
                            resetToken.get(resetTokenArray);
                            long result = Quiche.quiche_conn_new_scid(
                                    connAddr, Quiche.memoryAddress(connIdBuffer, EXTRACTED_VALUE, connIdBuffer.readableBytes()),
                                    connIdBuffer.readableBytes(), resetTokenArray, false, -1);
                            if (result < EXTRACTED_VALUE) {
                                break;
                            }
                            sendAndFlush = true;
                            generatedIds.add(srcId.duplicate());
                            sourceConnectionIds.add(srcId);
                        } while (--left > EXTRACTED_VALUE);
                    } finally {
                        connIdBuffer.release();
                    }
    
                    if (sendAndFlush) {
                        connectionSendAndFlush();
                    }
                    return generatedIds;
                }
            }
            return Collections.emptyList();
        }
}
