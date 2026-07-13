public class zxing_0203 {

      private DecoderResult decode(BitMatrixParser parser, Map<DecodeHintType,?> hints)
          throws FormatException, ChecksumException {
        Version version = parser.readVersion();
        ErrorCorrectionLevel ecLevel = parser.readFormatInformation().getErrorCorrectionLevel();
    
        // Read codewords
        byte[] codewords = parser.readCodewords();
        // Separate into data blocks
        DataBlock[] dataBlocks = DataBlock.getDataBlocks(codewords, version, ecLevel);
    
        // Count total number of data bytes
        int totalBytes = 0;
        for (DataBlock dataBlock : dataBlocks) {
          totalBytes += dataBlock.getNumDataCodewords();
        }
        byte[] resultBytes = new byte[totalBytes];
        int resultOffset = 0;
    
        // Error-correct and copy data blocks together into a stream of bytes
        int errorsCorrected = 0;
        for (DataBlock dataBlock : dataBlocks) {
          byte[] codewordBytes = dataBlock.getCodewords();
          int numDataCodewords = dataBlock.getNumDataCodewords();
          errorsCorrected += correctErrors(codewordBytes, numDataCodewords);
          for (int i = 0; i < numDataCodewords; i++) {
            resultBytes[resultOffset++] = codewordBytes[i];
          }
        }
    
        // Decode the contents of that stream of bytes
        DecoderResult result = DecodedBitStreamParser.decode(resultBytes, version, ecLevel, hints);
        result.setErrorsCorrected(errorsCorrected);
        return result;
      }
}
