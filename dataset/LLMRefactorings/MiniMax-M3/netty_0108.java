public class netty_0108 {

    private static final int LONG_HEADER_PACKET_TYPE_MASK = 0x30;
    private static final int LONG_HEADER_PACKET_TYPE_SHIFT = 4;

    private static QuicPacketType typeOfLongHeader(byte first, long version) throws QuicException {
        if (version == 0) {
            // If we parsed a version of 0 we are sure it's a version negotiation packet:
            // https://www.rfc-editor.org/rfc/rfc9000.html#section-17.2.1
            //
            // This also means we should ignore everything that is left in 'first'.
            return QuicPacketType.VERSION_NEGOTIATION;
        }
        int packetType = (first & LONG_HEADER_PACKET_TYPE_MASK) >> LONG_HEADER_PACKET_TYPE_SHIFT;
        switch (packetType) {
            case 0x00:
                return QuicPacketType.INITIAL;
            case 0x01:
                return QuicPacketType.ZERO_RTT;
            case 0x02:
                return QuicPacketType.HANDSHAKE;
            case 0x03:
                return QuicPacketType.RETRY;
            default:
                throw newProtocolViolationException("Unknown packet type: " + packetType);
        }
    }
}
