public class netty_0073 {

            public static PseudoHeaderName getPseudoHeaderRenamed(AsciiString header) {
                int length = header.length();
                if (length > 0 && header.charAt(0) == PSEUDO_HEADER_PREFIX) {
                    switch (length) {
                    case 5:
                        // :path
                        return PATH.value().equals(header) ? PATH : null;
                    case 7:
                        if (header == METHOD.value()) {
                            return METHOD;
                        }
                        if (header == SCHEME.value()) {
                            return SCHEME;
                        }
                        if (header == STATUS.value()) {
                            return STATUS;
                        }
                        // :method, :scheme, :status
                        if (METHOD.value().equals(header)) {
                            return METHOD;
                        }
                        if (SCHEME.value().equals(header)) {
                            return SCHEME;
                        }
                        return STATUS.value().equals(header)? STATUS : null;
                    case 9:
                        // :protocol
                        return PROTOCOL.value().equals(header)? PROTOCOL : null;
                    case 10:
                        // :authority
                        return AUTHORITY.value().equals(header)? AUTHORITY : null;
                    }
                }
                return null;
            }
}
