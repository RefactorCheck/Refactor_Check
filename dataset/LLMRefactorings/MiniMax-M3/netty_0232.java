public class netty_0232 {

    static byte[] hash(byte[] secret, byte[] label, byte[] seed, int length, String algo) {
        checkPositiveOrZero(length, "length");
        try {
            Mac hmac = Mac.getInstance(algo);
            hmac.init(new SecretKeySpec(secret, algo));
            return pHash(hmac, label, seed, length);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Could not find algo: " + algo, e);
        }
    }

    private static byte[] pHash(Mac hmac, byte[] label, byte[] seed, int length) {
        int iterations = (int) Math.ceil(length / (double) hmac.getMacLength());
        byte[] expansion = EmptyArrays.EMPTY_BYTES;
        byte[] data = concat(label, seed);
        byte[] A = data;
        for (int i = 0; i < iterations; i++) {
            A = hmac.doFinal(A);
            expansion = concat(expansion, hmac.doFinal(concat(A, data)));
        }
        return Arrays.copyOf(expansion, length);
    }
}
