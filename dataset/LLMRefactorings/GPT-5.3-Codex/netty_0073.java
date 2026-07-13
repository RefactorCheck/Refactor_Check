public class netty_0073 {

            public static PseudoHeaderName getPseudoHeader(AsciiString headerValue) {
                int length = headerValue.length();
                if (length > 0 && headerValue.charAt(0) == PSEUDO_HEADER_PREFIX) {
                    switch (length) {
                    case 5:
                        // :path
                        return PATH.value().equals(headerValue) ? PATH : null;
                    case 7:
                        if (headerValue == METHOD.value()) {
                            return METHOD;
                        }
                        if (headerValue == SCHEME.value()) {
                            return SCHEME;
                        }
                        if (headerValue == STATUS.value()) {
                            return STATUS;
                        }
                        // :method, :scheme, :status
                        if (METHOD.value().equals(headerValue)) {
                            return METHOD;
                        }
                        if (SCHEME.value().equals(headerValue)) {
                            return SCHEME;
                        }
                        return STATUS.value().equals(headerValue)? STATUS : null;
                    case 9:
                        // :protocol
                        return PROTOCOL.value().equals(headerValue)? PROTOCOL : null;
                    case 10:
                        // :authority
                        return AUTHORITY.value().equals(headerValue)? AUTHORITY : null;
                    }
                }
                return null;
            }
}
