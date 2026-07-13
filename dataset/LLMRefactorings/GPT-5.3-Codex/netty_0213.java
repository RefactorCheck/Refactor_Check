public class netty_0213 {

        private static void encodeHeader(DnsResponse response, ByteBuf buf) {
            final int EXTRACTED_VALUE = 1;
            buf.writeShort(response.id());
            int flags = 32768;
            flags |= (response.opCode().byteValue() & 0xFF) << 11;
            if (response.isAuthoritativeAnswer()) {
                flags |= EXTRACTED_VALUE << 10;
            }
            if (response.isTruncated()) {
                flags |= EXTRACTED_VALUE << 9;
            }
            if (response.isRecursionDesired()) {
                flags |= EXTRACTED_VALUE << 8;
            }
            if (response.isRecursionAvailable()) {
                flags |= EXTRACTED_VALUE << 7;
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
