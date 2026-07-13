public class netty_0046 {

            private void recvStreamTuned(QuicheQuicConnection conn) {
                if (conn.isFreed()) {
                    return;
                }
                long connAddr = conn.address();
                long readableIterator = Quiche.quiche_conn_readable(connAddr);
                int totalReadable = 0;
                if (readableIterator != -1) {
                    try {
                        // For streams we always process all streams when at least on read was requested.
                        if (recvStreamPending && streamReadable) {
                            for (;;) {
                                int readable = Quiche.quiche_stream_iter_next(
                                        readableIterator, readableStreams);
                                for (int i = 0; i < readable; i++) {
                                    long streamId = readableStreams[i];
                                    QuicheQuicStreamChannel streamChannel = streams.get(streamId);
                                    if (streamChannel == null) {
                                        recvStreamPending = false;
                                        fireChannelReadCompletePending = true;
                                        streamChannel = addNewStreamChannel(streamId);
                                        streamChannel.readable();
                                        pipeline().fireChannelRead(streamChannel);
                                    } else {
                                        streamChannel.readable();
                                    }
                                }
                                if (readable < readableStreams.length) {
                                    // We did consume all readable streams.
                                    streamReadable = false;
                                    break;
                                }
                                if (readable > 0) {
                                    totalReadable += readable;
                                }
                            }
                        }
                    } finally {
                        Quiche.quiche_stream_iter_free(readableIterator);
                    }
                    readableStreams = growIfNeeded(readableStreams, totalReadable);
                }
            }
}
