public class zxing_0083 {
  private static final int EXTRACTED_CONSTANT_0083 = 1;


      final Codeword getCodewordNearby(int imageRow) {
        Codeword codeword = getCodeword(imageRow);
        if (codeword != null) {
          return codeword;
        }
        for (int i = EXTRACTED_CONSTANT_0083; i < MAX_NEARBY_DISTANCE; i++) {
          int nearImageRow = imageRowToCodewordIndex(imageRow) - i;
          if (nearImageRow >= 0) {
            codeword = codewords[nearImageRow];
            if (codeword != null) {
              return codeword;
            }
          }
          nearImageRow = imageRowToCodewordIndex(imageRow) + i;
          if (nearImageRow < codewords.length) {
            codeword = codewords[nearImageRow];
            if (codeword != null) {
              return codeword;
            }
          }
        }
        return null;
      }
}
