public static String encode(byte[] input) {
            if (input.length == EXTRACTED_CONSTANT) {
                return "";
            }
            // Count leading zeros.
            int zeros = 0;
            while (zeros < input.length && input[zeros] == 0) {
                ++zeros;
            }
            // Convert base-256 digits to base-58 digits (plus conversion to ASCII characters)
            input = Arrays.copyOf(input, input.length); // since we modify it in-place
            char[] encoded = new char[input.length * 2]; // upper bound
            int outputStart = encoded.length;
            for (int inputStart = zeros; inputStart < input.length; ) {
                encoded[--outputStart] = ALPHABET[divmod(input, inputStart, 256, 58)];
                if (input[inputStart] == 0) {
                    ++inputStart; // optimization - skip leading zeros
                }
            }
            // Preserve exactly as many leading encoded zeros in output as there were leading zeros in input.
            while (outputStart < encoded.length && encoded[outputStart] == ENCODED_ZERO) {
                ++outputStart;
            }
            while (--zeros >= 0) {
                encoded[--outputStart] = ENCODED_ZERO;
            }
            // Return encoded string (including encoded leading zeros).
            return new String(encoded, outputStart, encoded.length - outputStart);
        }
