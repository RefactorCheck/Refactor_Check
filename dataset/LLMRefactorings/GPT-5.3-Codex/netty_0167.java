public class netty_0167 {

            @Override
            public boolean processMessage(Object msg) {
                final boolean added;
                if (msg instanceof DatagramPacket) {

                    ByteBuf buf = ((DatagramPacket) msg).content();
                    int segmentSize = 0;
                    if (((DatagramPacket) msg) instanceof io.netty.channel.unix.SegmentedDatagramPacket) {
                        int seg = ((io.netty.channel.unix.SegmentedDatagramPacket) ((DatagramPacket) msg)).segmentSize();
                        // We only need to tell the kernel that we want to use UDP_SEGMENT if there are multiple
                        // segments in the ((DatagramPacket) msg).
                        if (buf.readableBytes() > seg) {
                            segmentSize = seg;
                        }
                    }
                    added = add0(buf, buf.readerIndex(), buf.readableBytes(), segmentSize, ((DatagramPacket) msg).recipient());
                } else if (msg instanceof ByteBuf && connected) {
                    ByteBuf buf = (ByteBuf) msg;
                    added = add0(buf, buf.readerIndex(), buf.readableBytes(), 0, null);
                } else {
                    added = false;
                }
                if (added) {
                    maxMessagesPerWrite--;
                    return maxMessagesPerWrite > 0;
                }
                return false;
            }
}
