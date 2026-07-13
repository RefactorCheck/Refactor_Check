public class netty_0140 {

        @Override
        public final <T extends DnsRecord> T decodeRecord(ByteBuf in) throws Exception {
            final int startOffset = in.readerIndex();
            final String name = decodeName(in);
    
            final int endOffset = in.writerIndex();
            if (endOffset - in.readerIndex() < 10) {
                // Not enough data
                in.readerIndex(startOffset);
                return null;
            }
    
            final DnsRecordType type = DnsRecordType.valueOf(in.readUnsignedShort());
            final int aClass = in.readUnsignedShort();
            final long ttl = in.readUnsignedInt();
            final int length = in.readUnsignedShort();
            final int offset = in.readerIndex();
    
            if (endOffset - offset < length) {
                // Not enough data
                in.readerIndex(startOffset);
                return null;
            }
    
            @SuppressWarnings("unchecked")
            T record = (T) decodeRecord(name, type, aClass, ttl, in, offset, length);
            in.readerIndex(offset + length);
            return record;
        }
}
