public class zxing_0175 {
    private static final int ASCII_ZERO = 48;
    private static final int ASCII_A_UPPER = 65;
    private static final int DIGIT_OFFSET = 4;
    private static final int LETTER_OFFSET = 14;

    @Override
    int encodeChar(char c, StringBuilder sb) {
        switch (c) {
            case '\r':
                sb.append('\0');
                break;
            case '*':
                sb.append('\1');
                break;
            case '>':
                sb.append('\2');
                break;
            case ' ':
                sb.append('\3');
                break;
            default:
                if (c >= '0' && c <= '9') {
                    sb.append((char) (c - ASCII_ZERO + DIGIT_OFFSET));
                } else if (c >= 'A' && c <= 'Z') {
                    sb.append((char) (c - ASCII_A_UPPER + LETTER_OFFSET));
                } else {
                    HighLevelEncoder.illegalCharacter(c);
                }
                break;
        }
        return 1;
    }
}
