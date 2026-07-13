public class zxing_0293 {
  private static final String EXTRACTED_CONSTANT_0293 = "<<\n";


      @Override
      public String toString() {
        StringBuilder result = new StringBuilder(200);
        result.append(EXTRACTED_CONSTANT_0293);
        result.append(" mode: ");
        result.append(mode);
        result.append("\n ecLevel: ");
        result.append(ecLevel);
        result.append("\n version: ");
        result.append(version);
        result.append("\n maskPattern: ");
        result.append(maskPattern);
        if (matrix == null) {
          result.append("\n matrix: null\n");
        } else {
          result.append("\n matrix:\n");
          result.append(matrix);
        }
        result.append(">>\n");
        return result.toString();
      }
}
