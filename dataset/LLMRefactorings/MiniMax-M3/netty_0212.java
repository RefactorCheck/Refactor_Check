public class netty_0212 {

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    
            // If `isTCP` is true and state is WRITING, then we'll simulate a `FIN` flow.
            if (channelType == ChannelType.TCP && state.get() == State.WRITING) {
                logger.debug("Starting Fake TCP FIN+ACK Flow to close connection");
    
                ByteBufAllocator byteBufAllocator = ctx.alloc();
                ByteBuf tcpBuf = byteBufAllocator.buffer();
    
                try {
                    long initiatorSegmentNumber = isServerPipeline? receiveSegmentNumber : sendSegmentNumber;
                    long initiatorAckNumber = isServerPipeline? sendSegmentNumber : receiveSegmentNumber;
                    // Write FIN+ACK with Normal Source and Destination Address
                    writeAndCompleteTCP(tcpBuf, byteBufAllocator, ctx, initiatorSegmentNumber, initiatorAckNumber,
                                        initiatorAddr, initiatorAddr.getPort(), handlerAddr, handlerAddr.getPort(),
                                        TCPPacket.TCPFlag.FIN, TCPPacket.TCPFlag.ACK);
    
                    // Write FIN+ACK with Reversed Source and Destination Address
                    writeAndCompleteTCP(tcpBuf, byteBufAllocator, ctx, initiatorAckNumber, initiatorSegmentNumber,
                                        handlerAddr, handlerAddr.getPort(), initiatorAddr, initiatorAddr.getPort(),
                                        TCPPacket.TCPFlag.FIN, TCPPacket.TCPFlag.ACK);
    
                    // Increment by 1 when responding to FIN
                    sendSegmentNumber = incrementUintSegmentNumber(sendSegmentNumber, 1);
                    receiveSegmentNumber = incrementUintSegmentNumber(receiveSegmentNumber, 1);
                    initiatorSegmentNumber = isServerPipeline? receiveSegmentNumber : sendSegmentNumber;
                    initiatorAckNumber = isServerPipeline? sendSegmentNumber : receiveSegmentNumber;
    
                    // Write ACK with Normal Source and Destination Address
                    writeAndCompleteTCP(tcpBuf, byteBufAllocator, ctx, initiatorSegmentNumber, initiatorAckNumber,
                                        initiatorAddr, initiatorAddr.getPort(), handlerAddr, handlerAddr.getPort(),
                                        TCPPacket.TCPFlag.ACK);
                } finally {
                    tcpBuf.release();
                }
    
                logger.debug("Finished Fake TCP FIN+ACK Flow to close connection");
            }
    
            close();
            super.handlerRemoved(ctx);
        }
    
        private void writeAndCompleteTCP(ByteBuf tcpBuf, ByteBufAllocator byteBufAllocator, ChannelHandlerContext ctx,
                                         long segmentNumber, long ackNumber,
                                         InetSocketAddress srcAddr, int srcPort,
                                         InetSocketAddress dstAddr, int dstPort,
                                         TCPPacket.TCPFlag... flags) {
            TCPPacket.writePacket(tcpBuf, null, segmentNumber, ackNumber, srcPort, dstPort, flags);
            completeTCPWrite(srcAddr, dstAddr, tcpBuf, byteBufAllocator, ctx);
        }
}
