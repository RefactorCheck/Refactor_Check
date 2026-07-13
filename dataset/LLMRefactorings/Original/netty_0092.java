public class netty_0092 {

        private static BitSet validCookieNameOctets(BitSet validCookieValueOctets) {
            BitSet bits = new BitSet(8);
            bits.or(validCookieValueOctets);
            bits.set('(', false);
            bits.set(')', false);
            bits.set('<', false);
            bits.set('>', false);
            bits.set('@', false);
            bits.set(':', false);
            bits.set('/', false);
            bits.set('[', false);
            bits.set(']', false);
            bits.set('?', false);
            bits.set('=', false);
            bits.set('{', false);
            bits.set('}', false);
            bits.set(' ', false);
            bits.set('\t', false);
            return bits;
        }
}
