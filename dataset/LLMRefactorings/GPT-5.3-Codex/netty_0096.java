public class netty_0096 {

            private RecvByteBufAllocator.Handle handle;

        private void readInbound() {
            this.handle = unsafe().recvBufAllocHandle();

            handle.reset(config());
            ChannelPipeline pipeline = pipeline();
            do {
                Object received = inboundBuffer.poll();
                if (received == null) {
                    break;
                }
                if (received instanceof ByteBuf && inboundBuffer.peek() instanceof ByteBuf) {
                    ByteBuf msg = (ByteBuf) received;
                    ByteBuf output = handle.allocate(alloc());
                    if (msg.readableBytes() < output.writableBytes()) {
                        // We have an opportunity to coalesce buffers.
                        output.writeBytes(msg, msg.readerIndex(), msg.readableBytes());
                        msg.release();
                        while ((received = inboundBuffer.peek()) instanceof ByteBuf &&
                                ((ByteBuf) received).readableBytes() < output.writableBytes()) {
                            inboundBuffer.poll();
                            msg = (ByteBuf) received;
                            output.writeBytes(msg, msg.readerIndex(), msg.readableBytes());
                            msg.release();
                        }
                        handle.lastBytesRead(output.readableBytes());
                        received = output; // Send the coalesced buffer down the pipeline.
                    } else {
                        // It won't be profitable to coalesce buffers this time around.
                        handle.lastBytesRead(output.capacity());
                        output.release();
                    }
                }
                handle.incMessagesRead(1);
                pipeline.fireChannelRead(received);
            } while (handle.continueReading());
            handle.readComplete();
            pipeline.fireChannelReadComplete();
        }
}
