public class netty_0167 {

    @Override
    public boolean processMessage(Object msg) {
        final boolean added;
        if (msg instanceof DatagramPacket) {
            DatagramPacket packet = (DatagramPacket) msg;
            ByteBuf buf = packet.content();
            int segmentSize = computeSegmentSize(packet, buf);
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

    private static int computeSegmentSize(DatagramPacket packet, ByteBuf buf) {
        if (packet instanceof io.netty.channel.unix.SegmentedDatagramPacket) {
            int seg = ((io.netty.channel.unix.SegmentedDatagramPacket) packet).segmentSize();
            if (buf.readableBytes() > seg) {
                return seg;
            }
        }
        return 0;
    }
}
