public class netty_0023 {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        try {
            if (sender == null) {
                assertNotNull(msg.sender());
            } else {
                InetSocketAddress senderAddress = (InetSocketAddress) sender;
                if (senderAddress.getAddress().isAnyLocalAddress()) {
                    assertEquals(senderAddress.getPort(), msg.sender().getPort());
                } else {
                    assertEquals(sender, msg.sender());
                }
            }

            ByteBuf buf = msg.content();
            verifyContent(buf);

            // Test that the channel's localAddress is equal to the message's recipient
            assertEquals(ctx.channel().localAddress(), msg.recipient());

            if (echo) {
                ctx.writeAndFlush(new DatagramPacket(buf.retainedDuplicate(), msg.sender()));
            }
        } finally {
            latch.countDown();
        }
    }

    private void verifyContent(ByteBuf buf) {
        assertEquals(bytes.length, buf.readableBytes());
        for (int i = 0; i < bytes.length; i++) {
            assertEquals(bytes[i], buf.getByte(buf.readerIndex() + i));
        }
    }
}
