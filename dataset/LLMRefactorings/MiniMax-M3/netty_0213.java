public class netty_0213 {

    private static final int FLAG_QR_RESPONSE = 1 << 15;
    private static final int FLAG_AA = 1 << 10;
    private static final int FLAG_TC = 1 << 9;
    private static final int FLAG_RD = 1 << 8;
    private static final int FLAG_RA = 1 << 7;
    private static final int Z_SHIFT = 4;

    private static void encodeHeader(DnsResponse response, ByteBuf buf) {
        buf.writeShort(response.id());
        int flags = FLAG_QR_RESPONSE;
        flags |= (response.opCode().byteValue() & 0xFF) << 11;
        if (response.isAuthoritativeAnswer()) {
            flags |= FLAG_AA;
        }
        if (response.isTruncated()) {
            flags |= FLAG_TC;
        }
        if (response.isRecursionDesired()) {
            flags |= FLAG_RD;
        }
        if (response.isRecursionAvailable()) {
            flags |= FLAG_RA;
        }
        flags |= response.z() << Z_SHIFT;
        flags |= response.code().intValue();
        buf.writeShort(flags);
        buf.writeShort(response.count(DnsSection.QUESTION));
        buf.writeShort(response.count(DnsSection.ANSWER));
        buf.writeShort(response.count(DnsSection.AUTHORITY));
        buf.writeShort(response.count(DnsSection.ADDITIONAL));
    }
}
