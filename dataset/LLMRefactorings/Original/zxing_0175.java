public class zxing_0175 {

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
              sb.append((char) (c - 48 + 4));
            } else if (c >= 'A' && c <= 'Z') {
              sb.append((char) (c - 65 + 14));
            } else {
              HighLevelEncoder.illegalCharacter(c);
            }
            break;
        }
        return 1;
      }
}
