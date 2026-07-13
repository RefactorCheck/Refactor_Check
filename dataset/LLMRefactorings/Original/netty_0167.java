public class netty_0167 {

            @Override
            public boolean processMessage(Object msg) {
                final boolean added;
                if (msg instanceof DatagramPacket) {
                    DatagramPacket packet = (DatagramPacket) msg;
                    ByteBuf buf = packet.content();
                    int segmentSize = 0;
                    if (packet instanceof io.netty.channel.unix.SegmentedDatagramPacket) {
                        int seg = ((io.netty.channel.unix.SegmentedDatagramPacket) packet).segmentSize();
                        // We only need to tell the kernel that we want to use UDP_SEGMENT if there are multiple
                        // segments in the packet.
                        if (buf.readableBytes() > seg) {
                            segmentSize = seg;
                        }
                    }
                    added = add0(buf, buf.readerIndex(), buf.readableBytes(), segmentSize, packet.recipient());
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
