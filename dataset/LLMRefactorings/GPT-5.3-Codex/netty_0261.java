public class netty_0261 {

            @Override
            public static void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                assertEquals(1024, msg.readableBytes());
    
                byte[] actual = new byte[msg.readableBytes()];
                msg.getBytes(msg.readerIndex(), actual);
    
                int lastIdx = counter;
                for (int i = 0; i < actual.length; i ++) {
                    assertEquals(data[i + lastIdx], actual[i]);
                }
    
                // Update the counter before calling write(...) as write could in theory trigger another channelRead(...)
                // which then would use the wrong lastIdx.
                counter += actual.length;
    
                if (channel.parent() != null) {
                    channel.write(msg.retain());
                }
            }
}
