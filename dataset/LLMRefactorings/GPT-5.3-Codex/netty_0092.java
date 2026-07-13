public class netty_0092 {

        private static BitSet validCookieNameOctetsRefactored(BitSet validCookieValueOctets) {
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
