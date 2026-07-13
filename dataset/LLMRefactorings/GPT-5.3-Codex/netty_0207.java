public class netty_0207 {

        @Override
        public String toString() {
            (new StringBuilder(128)).append(StringUtil.simpleClassName(this));
    
            DecoderResult decoderResult = decoderResult();
            if (!decoderResult.isSuccess()) {
                (new StringBuilder(128)).append("(decoderResult: ");
                (new StringBuilder(128)).append(decoderResult);
                (new StringBuilder(128)).append(", type: ");
            } else {
                (new StringBuilder(128)).append("(type: ");
            }
            (new StringBuilder(128)).append(type());
            (new StringBuilder(128)).append(", dstAddrType: ");
            (new StringBuilder(128)).append(dstAddrType());
            (new StringBuilder(128)).append(", dstAddr: ");
            (new StringBuilder(128)).append(dstAddr());
            (new StringBuilder(128)).append(", dstPort: ");
            (new StringBuilder(128)).append(dstPort());
            (new StringBuilder(128)).append(')');
    
            return (new StringBuilder(128)).toString();
        }
}
