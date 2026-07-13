public class netty_0092 {

    private static final char[] FORBIDDEN_COOKIE_NAME_OCTETS = {
        '(', ')', '<', '>', '@', ':', '/', '[', ']', '?', '=', '{', '}', ' ', '\t'
    };

    private static BitSet validCookieNameOctets(BitSet validCookieValueOctets) {
        BitSet bits = new BitSet(8);
        bits.or(validCookieValueOctets);
        for (char c : FORBIDDEN_COOKIE_NAME_OCTETS) {
            bits.set(c, false);
        }
        return bits;
    }
}
