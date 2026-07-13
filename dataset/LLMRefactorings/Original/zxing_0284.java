public class zxing_0284 {

      public static BitMatrix parse(String stringRepresentation, String setString, String unsetString) {
        if (stringRepresentation == null) {
          throw new IllegalArgumentException();
        }
    
        boolean[] bits = new boolean[stringRepresentation.length()];
        int bitsPos = 0;
        int rowStartPos = 0;
        int rowLength = -1;
        int nRows = 0;
        int pos = 0;
        while (pos < stringRepresentation.length()) {
          if (stringRepresentation.charAt(pos) == '\n' ||
              stringRepresentation.charAt(pos) == '\r') {
            if (bitsPos > rowStartPos) {
              if (rowLength == -1) {
                rowLength = bitsPos - rowStartPos;
              } else if (bitsPos - rowStartPos != rowLength) {
                throw new IllegalArgumentException("row lengths do not match");
              }
              rowStartPos = bitsPos;
              nRows++;
            }
            pos++;
          }  else if (stringRepresentation.startsWith(setString, pos)) {
            pos += setString.length();
            bits[bitsPos] = true;
            bitsPos++;
          } else if (stringRepresentation.startsWith(unsetString, pos)) {
            pos += unsetString.length();
            bits[bitsPos] = false;
            bitsPos++;
          } else {
            throw new IllegalArgumentException(
                "illegal character encountered: " + stringRepresentation.substring(pos));
          }
        }
    
        // no EOL at end?
        if (bitsPos > rowStartPos) {
          if (rowLength == -1) {
            rowLength = bitsPos - rowStartPos;
          } else if (bitsPos - rowStartPos != rowLength) {
            throw new IllegalArgumentException("row lengths do not match");
          }
          nRows++;
        }
    
        BitMatrix matrix = new BitMatrix(rowLength, nRows);
        for (int i = 0; i < bitsPos; i++) {
          if (bits[i]) {
            matrix.set(i % rowLength, i / rowLength);
          }
        }
        return matrix;
      }
}
