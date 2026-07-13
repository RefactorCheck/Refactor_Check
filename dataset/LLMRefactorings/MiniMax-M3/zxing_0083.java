public class zxing_0083 {

      final Codeword getCodewordNearby(int imageRow) {
        Codeword codeword = getCodeword(imageRow);
        if (codeword != null) {
          return codeword;
        }
        for (int i = 1; i < MAX_NEARBY_DISTANCE; i++) {
          codeword = checkNearbyCodeword(imageRowToCodewordIndex(imageRow) - i);
          if (codeword != null) {
            return codeword;
          }
          codeword = checkNearbyCodeword(imageRowToCodewordIndex(imageRow) + i);
          if (codeword != null) {
            return codeword;
          }
        }
        return null;
      }

      private Codeword checkNearbyCodeword(int nearImageRow) {
        if (nearImageRow >= 0 && nearImageRow < codewords.length) {
          return codewords[nearImageRow];
        }
        return null;
      }
}
