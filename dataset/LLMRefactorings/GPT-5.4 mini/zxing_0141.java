public class zxing_0141 {

      private static int modelYear(char c) {
        if (c >= 'E' && c <= 'H') {
          return (c - 'E') + 1984;
        }
        if (c >= 'J' && c <= 'N') {
          return (c - 'J') + 1988;
        }
        if (c == 'P') {
          return 1993;
        }
        if (c >= 'R' && c <= 'T') {
          return (c - 'R') + 1994;
        }
        if (c >= 'V' && c <= 'Y') {
          return (c - 'V') + 1997;
        }
        if (c >= '1' && c <= '9') {
          return (c - '1') + 2001;
        }
        if (c >= 'A' && c <= 'D') {
          return (c - 'A') + 2010;
        }
        throw new IllegalArgumentException();
      }
}
