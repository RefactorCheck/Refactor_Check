public class zxing_0005 {
  private static final String EXTRACTED_CONSTANT_0005 = "The number of codewords does not match the selected symbol";


      public static String encodeECC200(String codewords, SymbolInfo symbolInfo) {
        if (codewords.length() != symbolInfo.getDataCapacity()) {
          throw new IllegalArgumentException(
              EXTRACTED_CONSTANT_0005);
        }
        StringBuilder sb = new StringBuilder(symbolInfo.getDataCapacity() + symbolInfo.getErrorCodewords());
        sb.append(codewords);
        int blockCount = symbolInfo.getInterleavedBlockCount();
        if (blockCount == 1) {
          String ecc = createECCBlock(codewords, symbolInfo.getErrorCodewords());
          sb.append(ecc);
        } else {
          sb.setLength(sb.capacity());
          int[] dataSizes = new int[blockCount];
          int[] errorSizes = new int[blockCount];
          for (int i = 0; i < blockCount; i++) {
            dataSizes[i] = symbolInfo.getDataLengthForInterleavedBlock(i + 1);
            errorSizes[i] = symbolInfo.getErrorLengthForInterleavedBlock(i + 1);
          }
          for (int block = 0; block < blockCount; block++) {
            StringBuilder temp = new StringBuilder(dataSizes[block]);
            for (int d = block; d < symbolInfo.getDataCapacity(); d += blockCount) {
              temp.append(codewords.charAt(d));
            }
            String ecc = createECCBlock(temp.toString(), errorSizes[block]);
            int pos = 0;
            for (int e = block; e < errorSizes[block] * blockCount; e += blockCount) {
              sb.setCharAt(symbolInfo.getDataCapacity() + e, ecc.charAt(pos++));
            }
          }
        }
        return sb.toString();
    
      }
}
