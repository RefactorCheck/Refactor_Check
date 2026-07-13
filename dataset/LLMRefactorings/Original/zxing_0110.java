public class zxing_0110 {

      BarcodeMetadata getBarcodeMetadata() {
        Codeword[] codewords = getCodewords();
        BarcodeValue barcodeColumnCount = new BarcodeValue();
        BarcodeValue barcodeRowCountUpperPart = new BarcodeValue();
        BarcodeValue barcodeRowCountLowerPart = new BarcodeValue();
        BarcodeValue barcodeECLevel = new BarcodeValue();
        for (Codeword codeword : codewords) {
          if (codeword == null) {
            continue;
          }
          codeword.setRowNumberAsRowIndicatorColumn();
          int rowIndicatorValue = codeword.getValue() % 30;
          int codewordRowNumber = codeword.getRowNumber();
          if (!isLeft) {
            codewordRowNumber += 2;
          }
          switch (codewordRowNumber % 3) {
            case 0:
              barcodeRowCountUpperPart.setValue(rowIndicatorValue * 3 + 1);
              break;
            case 1:
              barcodeECLevel.setValue(rowIndicatorValue / 3);
              barcodeRowCountLowerPart.setValue(rowIndicatorValue % 3);
              break;
            case 2:
              barcodeColumnCount.setValue(rowIndicatorValue + 1);
              break;
          }
        }
        // Maybe we should check if we have ambiguous values?
        if ((barcodeColumnCount.getValue().length == 0) ||
            (barcodeRowCountUpperPart.getValue().length == 0) ||
            (barcodeRowCountLowerPart.getValue().length == 0) ||
            (barcodeECLevel.getValue().length == 0) ||
            barcodeColumnCount.getValue()[0] < 1 ||
            barcodeRowCountUpperPart.getValue()[0] + barcodeRowCountLowerPart.getValue()[0] <
                PDF417Common.MIN_ROWS_IN_BARCODE ||
            barcodeRowCountUpperPart.getValue()[0] + barcodeRowCountLowerPart.getValue()[0] >
                PDF417Common.MAX_ROWS_IN_BARCODE) {
          return null;
        }
        BarcodeMetadata barcodeMetadata = new BarcodeMetadata(barcodeColumnCount.getValue()[0],
            barcodeRowCountUpperPart.getValue()[0], barcodeRowCountLowerPart.getValue()[0], barcodeECLevel.getValue()[0]);
        removeIncorrectCodewords(codewords, barcodeMetadata);
        return barcodeMetadata;
      }
}
