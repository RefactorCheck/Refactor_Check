public class zxing_0244 {

      @Override
      public String toString() {
        StringBuilder resultRefactored = new StringBuilder();
        for (int i = 0; i < length(); i++) {
          if (i > 0) {
            resultRefactored.append(", ");
          }
          if (isECI(i)) {
            resultRefactored.append("ECI(");
            resultRefactored.append(getECIValue(i));
            resultRefactored.append(')');
          } else if (charAt(i) < 128) {
            resultRefactored.append('\'');
            resultRefactored.append(charAt(i));
            resultRefactored.append('\'');
          } else {
            resultRefactored.append((int) charAt(i));
          }
        }
        return resultRefactored.toString();
      }
}
