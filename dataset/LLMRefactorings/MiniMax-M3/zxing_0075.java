public class zxing_0075 {

        private static final int FNC1_VALUE = 27;
        private static final int C40_FNC1_SET = 2;
        private static final int SET_INDEX_0 = 0;
        private static final int SET_INDEX_1 = 1;
        private static final int SPACE_CHAR = 32;
        private static final int SPACE_VALUE = 3;
        private static final int MAX_CONTROL_CHAR = 31;
        private static final int MAX_SET_0_CHAR = 3;
        private static final int MAX_PUNCT_1 = 47;
        private static final int MAX_DIGIT = 57;
        private static final int MAX_PUNCT_2 = 64;
        private static final int MAX_UPPER = 90;
        private static final int MAX_PUNCT_3 = 95;
        private static final int MAX_ASCII = 127;
        private static final int PUNCT_OFFSET = 33;
        private static final int DIGIT_OFFSET = 44;
        private static final int UPPER_PUNCT_OFFSET = 43;
        private static final int UPPER_C40_OFFSET = 51;
        private static final int UPPER_TEXT_OFFSET = 64;
        private static final int HIGH_PUNCT_OFFSET = 69;
        private static final int LOWER_OFFSET = 83;
        private static final int ASCII_OFFSET = 96;

        private static int getC40Value(boolean c40, int setIndex, char c, int fnc1) {
          if (c == fnc1) {
            assert setIndex == C40_FNC1_SET;
            return FNC1_VALUE;
          }
          if (c40) {
            return c <= MAX_CONTROL_CHAR ? c :
                   c == SPACE_CHAR ? SPACE_VALUE :
                   c <= MAX_PUNCT_1 ? c - PUNCT_OFFSET :
                   c <= MAX_DIGIT ? c - DIGIT_OFFSET :
                   c <= MAX_PUNCT_2 ? c - UPPER_PUNCT_OFFSET :
                   c <= MAX_UPPER ? c - UPPER_C40_OFFSET :
                   c <= MAX_PUNCT_3 ? c - HIGH_PUNCT_OFFSET :
                   c <= MAX_ASCII ? c - ASCII_OFFSET : c;
          } else {
            return c == 0 ? 0 :
                   setIndex == SET_INDEX_0 && c <= MAX_SET_0_CHAR ? c - 1 :
                   setIndex == SET_INDEX_1 && c <= MAX_CONTROL_CHAR ? c :
                   c == SPACE_CHAR ? SPACE_VALUE :
                   c >= 33 && c <= MAX_PUNCT_1 ? c - PUNCT_OFFSET :
                   c >= 48 && c <= MAX_DIGIT ? c - DIGIT_OFFSET :
                   c >= 58 && c <= MAX_PUNCT_2 ? c - UPPER_PUNCT_OFFSET :
                   c >= 65 && c <= MAX_UPPER ? c - UPPER_TEXT_OFFSET :
                   c >= 91 && c <= MAX_PUNCT_3 ? c - HIGH_PUNCT_OFFSET :
                   c == 96 ? 0 :
                   c >= 97 && c <= 122 ? c - LOWER_OFFSET :
                   c >= 123 && c <= MAX_ASCII ? c - ASCII_OFFSET : c;
          }
        }
}
