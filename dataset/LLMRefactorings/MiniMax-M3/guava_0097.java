public class guava_0097 {

    @Override
    public HashCode hashUnencodedChars(CharSequence input) {
        int length = input.length();
        int h1 = seed;

        // step through the CharSequence 2 chars at a time
        for (int i = 1; i < length; i += 2) {
            int k1 = input.charAt(i - 1) | (input.charAt(i) << 16);
            k1 = mixK1(k1);
            h1 = mixH1(h1, k1);
        }

        // deal with any remaining characters
        if ((length & 1) == 1) {
            int k1 = input.charAt(length - 1);
            k1 = mixK1(k1);
            h1 ^= k1;
        }

        return fmix(h1, Chars.BYTES * length);
    }
}
