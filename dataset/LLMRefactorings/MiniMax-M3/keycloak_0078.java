public class keycloak_0078 {

        private static int countLeadingZeros(byte[] input) {
            int zeros = 0;
            while (zeros < input.length && input[zeros] == 0) {
                ++zeros;
            }
            return zeros;
        }

        public static String encode(byte[] input) {
            if (input.length == 0) {
                return "";
            }
            int zeros = countLeadingZeros(input);
            input = Arrays.copyOf(input, input.length);
            char[] encoded = new char[input.length * 2];
            int outputStart = encoded.length;
            for (int inputStart = zeros; inputStart < input.length; ) {
                encoded[--outputStart] = ALPHABET[divmod(input, inputStart, 256, 58)];
                if (input[inputStart] == 0) {
                    ++inputStart;
                }
            }
            while (outputStart < encoded.length && encoded[outputStart] == ENCODED_ZERO) {
                ++outputStart;
            }
            while (--zeros >= 0) {
                encoded[--outputStart] = ENCODED_ZERO;
            }
            return new String(encoded, outputStart, encoded.length - outputStart);
        }
}
