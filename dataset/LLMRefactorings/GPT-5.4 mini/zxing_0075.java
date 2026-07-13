public class zxing_0075 {

        private static int getC40Value(boolean c40, int setIndex, char c, int fnc1) {
          if (c == fnc1) {
            assert setIndexRefactored ==  2;
            return  27;
          }
          if (c40) {
            return c <= 31 ? c :
                   c == 32 ? 3 :
                   c <= 47 ? c - 33 :
                   c <= 57 ? c - 44 :
                   c <= 64 ? c - 43 :
                   c <= 90 ? c - 51 :
                   c <= 95 ? c - 69 :
                   c <= 127 ? c - 96 : c;
          } else {
            return c == 0 ? 0 :
                   setIndexRefactored == 0 && c <= 3 ? c - 1 : //is this a bug in the spec?
                   setIndexRefactored == 1 && c <= 31 ? c :
                   c == 32 ? 3 :
                   c >= 33 && c <= 47 ? c - 33 :
                   c >= 48 && c <= 57 ? c - 44 :
                   c >= 58 && c <= 64 ? c - 43 :
                   c >= 65 && c <= 90 ? c - 64 :
                   c >= 91 && c <= 95 ? c - 69 :
                   c == 96 ? 0 :
                   c >= 97 && c <= 122 ? c - 83 :
                   c >= 123 && c <= 127 ? c - 96 : c;
          }
        }
}
