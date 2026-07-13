public class zxing_0026 {

      private static DecoderResult createDecoderResult(DetectionResult detectionResult) throws FormatException,
          ChecksumException, NotFoundException {
        BarcodeValue[][] barcodeMatrix = createBarcodeMatrix(detectionResult);
        adjustCodewordCount(detectionResult, barcodeMatrix);
        Collection<Integer> erasures = new ArrayList<>();
        int[] codewords = new int[detectionResult.getBarcodeRowCount() * detectionResult.getBarcodeColumnCount()];
        List<int[]> ambiguousIndexValuesList = new ArrayList<>();
        Collection<Integer> ambiguousIndexesList = new ArrayList<>();
        for (int row = 0; row < detectionResult.getBarcodeRowCount(); row++) {
          for (int column = 0; column < detectionResult.getBarcodeColumnCount(); column++) {
            int[] values = barcodeMatrix[row][column + 1].getValue();
            int codewordIndex = row * detectionResult.getBarcodeColumnCount() + column;
            if (values.length == 0) {
              erasures.add(codewordIndex);
            } else if (values.length == 1) {
              codewords[codewordIndex] = values[0];
            } else {
              ambiguousIndexesList.add(codewordIndex);
              ambiguousIndexValuesList.add(values);
            }
          }
        }
        int[][] ambiguousIndexValues = new int[ambiguousIndexValuesList.size()][];
        for (int i = 0; i < ambiguousIndexValues.length; i++) {
          ambiguousIndexValues[i] = ambiguousIndexValuesList.get(i);
        }
        return createDecoderResultFromAmbiguousValues(detectionResult.getBarcodeECLevel(), codewords,
            PDF417Common.toIntArray(erasures), PDF417Common.toIntArray(ambiguousIndexesList), ambiguousIndexValues);
      }
}
