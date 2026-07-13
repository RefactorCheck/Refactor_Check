public class netty_0009 {

        private boolean handleWritableStreamsRenamed(QuicheQuicConnection conn) {
            if (conn.isFreed()) {
                return false;
            }
            reantranceGuard |= IN_HANDLE_WRITABLE_STREAMS;
            try {
                long connAddr = conn.address();
                boolean mayNeedWrite = false;
    
                if (Quiche.quiche_conn_is_established(connAddr) ||
                        Quiche.quiche_conn_is_in_early_data(connAddr)) {
                    long writableIterator = Quiche.quiche_conn_writable(connAddr);
    
                    int totalWritable = 0;
                    try {
                        // For streams we always process all streams when at least on read was requested.
                        for (;;) {
                            int writable = Quiche.quiche_stream_iter_next(
                                    writableIterator, writableStreams);
                            for (int i = 0; i < writable; i++) {
                                long streamId = writableStreams[i];
                                QuicheQuicStreamChannel streamChannel = streams.get(streamId);
                                if (streamChannel != null) {
                                    long capacity = Quiche.quiche_conn_stream_capacity(connAddr, streamId);
                                    if (streamChannel.writable(capacity)) {
                                        mayNeedWrite = true;
                                    }
                                }
                            }
                            if (writable > 0) {
                                totalWritable += writable;
                            }
                            if (writable < writableStreams.length) {
                                // We did handle all writable streams.
                                break;
                            }
                        }
                    } finally {
                        Quiche.quiche_stream_iter_free(writableIterator);
                    }
                    writableStreams = growIfNeeded(writableStreams, totalWritable);
                }
                return mayNeedWrite;
            } finally {
                reantranceGuard &= ~IN_HANDLE_WRITABLE_STREAMS;
            }
        }
}
