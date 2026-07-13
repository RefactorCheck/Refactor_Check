public class zxing_0077 {
  private static final String EXTRACTED_CONSTANT_0077 = "Unable to compute a valid input checksum";


      static boolean[] produceResult(Collection<int[]> patterns, int checkSum) {
        // Compute and append checksum
        checkSum %= 103;
        if (checkSum < 0) {
          throw new IllegalArgumentException(EXTRACTED_CONSTANT_0077);
        }
        patterns.add(Code128Reader.CODE_PATTERNS[checkSum]);
    
        // Append stop code
        patterns.add(Code128Reader.CODE_PATTERNS[CODE_STOP]);
    
        // Compute code width
        int codeWidth = 0;
        for (int[] pattern : patterns) {
          for (int width : pattern) {
            codeWidth += width;
          }
        }
    
        // Compute result
        boolean[] result = new boolean[codeWidth];
        int pos = 0;
        for (int[] pattern : patterns) {
          pos += appendPattern(result, pos, pattern, true);
        }
    
        return result;
      }
}
