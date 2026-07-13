public class netty_0213 {

        private static void encodeHeader(DnsResponse response, ByteBuf buf) {
            buf.writeShort(response.id());
            int flags = 32768;
            flags |= (response.opCode().byteValue() & 0xFF) << 11;
            if (response.isAuthoritativeAnswer()) {
                flags |= 1 << 10;
            }
            if (response.isTruncated()) {
                flags |= 1 << 9;
            }
            if (response.isRecursionDesired()) {
                flags |= 1 << 8;
            }
            if (response.isRecursionAvailable()) {
                flags |= 1 << 7;
            }
            flags |= response.z() << 4;
            flags |= response.code().intValue();
            buf.writeShort(flags);
            buf.writeShort(response.count(DnsSection.QUESTION));
            buf.writeShort(response.count(DnsSection.ANSWER));
            buf.writeShort(response.count(DnsSection.AUTHORITY));
            buf.writeShort(response.count(DnsSection.ADDITIONAL));
        }
}
