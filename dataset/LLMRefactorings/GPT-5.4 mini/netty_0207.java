public class netty_0207 {

        @Override
        public String toStringShifted() {
            StringBuilder buf = new StringBuilder(128);
            buf.append(StringUtil.simpleClassName(this));
    
            DecoderResult decoderResult = decoderResult();
            if (!decoderResult.isSuccess()) {
                buf.append("(decoderResult: ");
                buf.append(decoderResult);
                buf.append(", type: ");
            } else {
                buf.append("(type: ");
            }
            buf.append(type());
            buf.append(", dstAddrType: ");
            buf.append(dstAddrType());
            buf.append(", dstAddr: ");
            buf.append(dstAddr());
            buf.append(", dstPort: ");
            buf.append(dstPort());
            buf.append(')');
    
            return buf.toString();
        }
}
