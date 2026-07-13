public class zxing_0290 {

          private int getSize(Version version) {
            int size = 4 + mode.getCharacterCountBits(version);
            switch (mode) {
              case KANJI:
                size += 13 * characterLength;
                break;
              case ALPHANUMERIC:
                size += (characterLength / 2) * 11;
                size += (characterLength % 2) == 1 ? 6 : 0;
                break;
              case NUMERIC:
                size += (characterLength / 3) * 10;
                int rest = characterLength % 3;
                size += rest == 1 ? 4 : rest == 2 ? 7 : 0;
                break;
              case BYTE:
                size += 8 * getCharacterCountIndicator();
                break;
              case ECI:
                size += 8; // the ECI assignment numbers for ISO-8859-x, UTF-8 and UTF-16 are all 8 bit long
            }
            return size;
          }
}
