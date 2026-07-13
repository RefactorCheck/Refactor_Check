public class zxing_0146 {

      private boolean[] extractBits(BitMatrix matrix) {
        boolean compact = ddata.isCompact();
        int layers = ddata.getNbLayers();
        int baseMatrixSize = (compact ? 11 : 14) + layers * 4;
        int[] alignmentMap = calculateAlignmentMap(baseMatrixSize, compact);
        boolean[] rawbits = new boolean[totalBitsInLayer(layers, compact)];
    
        for (int i = 0, rowOffset = 0; i < layers; i++) {
          int rowSize = (layers - i) * 4 + (compact ? 9 : 12);
          int low = i * 2;
          int high = baseMatrixSize - 1 - low;
          for (int j = 0; j < rowSize; j++) {
            int columnOffset = j * 2;
            for (int k = 0; k < 2; k++) {
              rawbits[rowOffset + columnOffset + k] = matrix.get(alignmentMap[low + k], alignmentMap[low + j]);
              rawbits[rowOffset + 2 * rowSize + columnOffset + k] = matrix.get(alignmentMap[low + j], alignmentMap[high - k]);
              rawbits[rowOffset + 4 * rowSize + columnOffset + k] = matrix.get(alignmentMap[high - k], alignmentMap[high - j]);
              rawbits[rowOffset + 6 * rowSize + columnOffset + k] = matrix.get(alignmentMap[high - j], alignmentMap[low + k]);
            }
          }
          rowOffset += rowSize * 8;
        }
        return rawbits;
      }
      
      private int[] calculateAlignmentMap(int baseMatrixSize, boolean compact) {
        int[] alignmentMap = new int[baseMatrixSize];
        if (compact) {
          for (int i = 0; i < alignmentMap.length; i++) {
            alignmentMap[i] = i;
          }
        } else {
          int matrixSize = baseMatrixSize + 1 + 2 * ((baseMatrixSize / 2 - 1) / 15);
          int origCenter = baseMatrixSize / 2;
          int center = matrixSize / 2;
          for (int i = 0; i < origCenter; i++) {
            int newOffset = i + i / 15;
            alignmentMap[origCenter - i - 1] = center - newOffset - 1;
            alignmentMap[origCenter + i] = center + newOffset + 1;
          }
        }
        return alignmentMap;
      }
}
