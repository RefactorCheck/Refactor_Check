public class zxing_0141 {

      private static final int YEAR_E_H_START = 1984;
      private static final int YEAR_J_N_START = 1988;
      private static final int YEAR_P = 1993;
      private static final int YEAR_R_T_START = 1994;
      private static final int YEAR_V_Y_START = 1997;
      private static final int YEAR_1_9_START = 2001;
      private static final int YEAR_A_D_START = 2010;

      private static int modelYear(char c) {
        if (c >= 'E' && c <= 'H') {
          return (c - 'E') + YEAR_E_H_START;
        }
        if (c >= 'J' && c <= 'N') {
          return (c - 'J') + YEAR_J_N_START;
        }
        if (c == 'P') {
          return YEAR_P;
        }
        if (c >= 'R' && c <= 'T') {
          return (c - 'R') + YEAR_R_T_START;
        }
        if (c >= 'V' && c <= 'Y') {
          return (c - 'V') + YEAR_V_Y_START;
        }
        if (c >= '1' && c <= '9') {
          return (c - '1') + YEAR_1_9_START;
        }
        if (c >= 'A' && c <= 'D') {
          return (c - 'A') + YEAR_A_D_START;
        }
        throw new IllegalArgumentException();
      }
}
