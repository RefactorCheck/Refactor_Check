public class netty_0009 {

        private boolean handleWritableStreams(QuicheQuicConnection conn) {
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
                        for (;;) {
                            int writable = Quiche.quiche_stream_iter_next(
                                    writableIterator, writableStreams);
                            for (int i = 0; i < writable; i++) {
                                long streamId = writableStreams[i];
                                if (processWritableStream(streamId, connAddr)) {
                                    mayNeedWrite = true;
                                }
                            }
                            if (writable > 0) {
                                totalWritable += writable;
                            }
                            if (writable < writableStreams.length) {
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

        private boolean processWritableStream(long streamId, long connAddr) {
            QuicheQuicStreamChannel streamChannel = streams.get(streamId);
            if (streamChannel != null) {
                long capacity = Quiche.quiche_conn_stream_capacity(connAddr, streamId);
                return streamChannel.writable(capacity);
            }
            return false;
        }
}
