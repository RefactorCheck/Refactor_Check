public class zxing_0244 {

      @Override
      public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length(); i++) {
          if (i > 0) {
            result.append(", ");
          }
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
        return result.toString();
      }
}
