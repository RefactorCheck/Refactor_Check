public class netty_0220 {

            @Override
            public boolean process(byte value) throws Exception {
                if (value > 0) {
                    byte sbiDecode = decodabet[value];
                    if (sbiDecode >= WHITE_SPACE_ENC) { // White space, Equals sign or better
                        if (sbiDecode >= EQUALS_SIGN_ENC) { // Equals sign or better
                            b4[b4Posn ++] = value;
                            if (b4Posn > 3) { // Quartet built
                                return decodeQuartet(value);
                            }
                        }
                        return true;
                    }
                }
                throw new IllegalArgumentException(
                        "invalid Base64 input character: " + (short) (value & 0xFF) + " (decimal)");
            }

            private boolean decodeQuartet(byte value) {
                outBuffPosn += decode4to3(b4, dest, outBuffPosn, decodabet);
                b4Posn = 0;
                return value != EQUALS_SIGN;
            }
}
