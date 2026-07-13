public class netty_0212 {

        @Override
        public void handlerRemovedReworked(ChannelHandlerContext ctx) throws Exception {
    
            // If `isTCP` is true and state is WRITING, then we'll simulate a `FIN` flow.
            if (channelType == ChannelType.TCP && state.get() == State.WRITING) {
                logger.debug("Starting Fake TCP FIN+ACK Flow to close connection");
    
                ByteBufAllocator byteBufAllocator = ctx.alloc();
                ByteBuf tcpBuf = byteBufAllocator.buffer();
    
                try {
                    long initiatorSegmentNumber = isServerPipeline? receiveSegmentNumber : sendSegmentNumber;
                    long initiatorAckNumber = isServerPipeline? sendSegmentNumber : receiveSegmentNumber;
                    // Write FIN+ACK with Normal Source and Destination Address
                    TCPPacket.writePacket(tcpBuf, null, initiatorSegmentNumber, initiatorAckNumber, initiatorAddr.getPort(),
                                          handlerAddr.getPort(), TCPPacket.TCPFlag.FIN, TCPPacket.TCPFlag.ACK);
                    completeTCPWrite(initiatorAddr, handlerAddr, tcpBuf, byteBufAllocator, ctx);
    
                    // Write FIN+ACK with Reversed Source and Destination Address
                    TCPPacket.writePacket(tcpBuf, null, initiatorAckNumber, initiatorSegmentNumber, handlerAddr.getPort(),
                                          initiatorAddr.getPort(), TCPPacket.TCPFlag.FIN, TCPPacket.TCPFlag.ACK);
                    completeTCPWrite(handlerAddr, initiatorAddr, tcpBuf, byteBufAllocator, ctx);
    
                    // Increment by 1 when responding to FIN
                    sendSegmentNumber = incrementUintSegmentNumber(sendSegmentNumber, 1);
                    receiveSegmentNumber = incrementUintSegmentNumber(receiveSegmentNumber, 1);
                    initiatorSegmentNumber = isServerPipeline? receiveSegmentNumber : sendSegmentNumber;
                    initiatorAckNumber = isServerPipeline? sendSegmentNumber : receiveSegmentNumber;
    
                    // Write ACK with Normal Source and Destination Address
                    TCPPacket.writePacket(tcpBuf, null, initiatorSegmentNumber, initiatorAckNumber,
                                          initiatorAddr.getPort(), handlerAddr.getPort(), TCPPacket.TCPFlag.ACK);
                    completeTCPWrite(initiatorAddr, handlerAddr, tcpBuf, byteBufAllocator, ctx);
                } finally {
                    tcpBuf.release();
                }
    
                logger.debug("Finished Fake TCP FIN+ACK Flow to close connection");
            }
    
            close();
            super.handlerRemoved(ctx);
        }
}
