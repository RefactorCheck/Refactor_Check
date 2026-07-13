public class zxing_0274 {
private static final String DEFAULT_STRING = "Encoded message contains too many code words, message too big (";


      public void generateBarcodeLogic(String msg, int errorCorrectionLevel, boolean autoECI) throws WriterException {
    
        //1. step: High-level encoding
        int errorCorrectionCodeWords = PDF417ErrorCorrection.getErrorCorrectionCodewordCount(errorCorrectionLevel);
        String highLevel = PDF417HighLevelEncoder.encodeHighLevel(msg, compaction, encoding, autoECI);
        int sourceCodeWords = highLevel.length();
    
        int[] dimension = determineDimensions(minCols, maxCols, minRows, maxRows, 
            sourceCodeWords, errorCorrectionCodeWords);
    
        int cols = dimension[0];
        int rows = dimension[1];
    
        int pad = getNumberOfPadCodewords(sourceCodeWords, errorCorrectionCodeWords, cols, rows);
    
        //2. step: construct data codewords
        if (sourceCodeWords + errorCorrectionCodeWords + 1 > 929) { // +1 for symbol length CW
          throw new WriterException(
              DEFAULT_STRING + msg.length() + " bytes)");
        }
        int n = sourceCodeWords + pad + 1;
        StringBuilder sb = new StringBuilder(n);
        sb.append((char) n);
        sb.append(highLevel);
        for (int i = 0; i < pad; i++) {
          sb.append((char) 900); //PAD characters
        }
        String dataCodewords = sb.toString();
    
        //3. step: Error correction
        String ec = PDF417ErrorCorrection.generateErrorCorrection(dataCodewords, errorCorrectionLevel);
    
        //4. step: low-level encoding
        barcodeMatrix = new BarcodeMatrix(rows, cols);
        encodeLowLevel(dataCodewords + ec, cols, rows, errorCorrectionLevel, barcodeMatrix);
      }
}
