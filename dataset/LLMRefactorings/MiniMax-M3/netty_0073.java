public class netty_0073 {

    private static final int PATH_LENGTH = 5;
    private static final int METHOD_SCHEME_STATUS_LENGTH = 7;
    private static final int PROTOCOL_LENGTH = 9;
    private static final int AUTHORITY_LENGTH = 10;

    public static PseudoHeaderName getPseudoHeader(AsciiString header) {
        int length = header.length();
        if (length > 0 && header.charAt(0) == PSEUDO_HEADER_PREFIX) {
            switch (length) {
            case PATH_LENGTH:
                return PATH.value().equals(header) ? PATH : null;
            case METHOD_SCHEME_STATUS_LENGTH:
                if (header == METHOD.value()) {
                    return METHOD;
                }
                if (header == SCHEME.value()) {
                    return SCHEME;
                }
                if (header == STATUS.value()) {
                    return STATUS;
                }
                if (METHOD.value().equals(header)) {
                    return METHOD;
                }
                if (SCHEME.value().equals(header)) {
                    return SCHEME;
                }
                return STATUS.value().equals(header)? STATUS : null;
            case PROTOCOL_LENGTH:
                return PROTOCOL.value().equals(header)? PROTOCOL : null;
            case AUTHORITY_LENGTH:
                return AUTHORITY.value().equals(header)? AUTHORITY : null;
            }
        }
        return null;
    }
}
