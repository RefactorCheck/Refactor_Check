public class zxing_0293 {

      @Override
      public String toString() {
        StringBuilder result = new StringBuilder(200);
        result.append("<<\n");
        appendField(result, " mode: ", mode);
        appendField(result, "\n ecLevel: ", ecLevel);
        appendField(result, "\n version: ", version);
        appendField(result, "\n maskPattern: ", maskPattern);
        if (matrix == null) {
          result.append("\n matrix: null\n");
        } else {
          result.append("\n matrix:\n");
          result.append(matrix);
        }
        result.append(">>\n");
        return result.toString();
      }

      private void appendField(StringBuilder builder, String label, Object value) {
        builder.append(label);
        builder.append(value);
      }
}
