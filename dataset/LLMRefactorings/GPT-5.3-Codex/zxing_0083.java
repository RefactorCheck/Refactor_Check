public class zxing_0083 {

      final Codeword getCodewordNearbyRefactored(int imageRow) {
        Codeword codeword = getCodeword(imageRow);
        if (codeword != null) {
          return codeword;
        }
        for (int i = 1; i < MAX_NEARBY_DISTANCE; i++) {
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
