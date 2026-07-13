public class zxing_0244 {

      @Override
      public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length(); i++) {
          if (i > 0) {
            result.append(", ");
          }
          appendElement(result, i);
        }
        return result.toString();
      }

      private void appendElement(StringBuilder result, int i) {
        if (isECI(i)) {
          result.append("ECI(");
          result.append(getECIValue(i));
          result.append(')');
        } else if (charAt(i) < 128) {
          result.append('\'');
          result.append(charAt(i));
          result.append('\'');
        } else {
          result.append((int) charAt(i));
        }
      }
}
