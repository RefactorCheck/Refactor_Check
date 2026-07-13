public class zxing_0203 {

      private DecoderResult decode(BitMatrixParser parser, Map<DecodeHintType,?> hints)
          throws FormatException, ChecksumException {
        Version version = parser.readVersion();
        ErrorCorrectionLevel ecLevel = parser.readFormatInformation().getErrorCorrectionLevel();
    
        byte[] codewords = parser.readCodewords();
        DataBlock[] dataBlocks = DataBlock.getDataBlocks(codewords, version, ecLevel);
    
        DecodedData decoded = errorCorrectAndJoinDataBlocks(dataBlocks);
        DecoderResult result = DecodedBitStreamParser.decode(decoded.data, version, ecLevel, hints);
        result.setErrorsCorrected(decoded.errorsCorrected);
        return result;
      }

      private DecodedData errorCorrectAndJoinDataBlocks(DataBlock[] dataBlocks) throws ChecksumException, FormatException {
        int totalBytes = 0;
        for (DataBlock dataBlock : dataBlocks) {
          totalBytes += dataBlock.getNumDataCodewords();
        }
        byte[] resultBytes = new byte[totalBytes];
        int resultOffset = 0;
        int errorsCorrected = 0;
        for (DataBlock dataBlock : dataBlocks) {
          byte[] codewordBytes = dataBlock.getCodewords();
          int numDataCodewords = dataBlock.getNumDataCodewords();
          errorsCorrected += correctErrors(codewordBytes, numDataCodewords);
          for (int i = 0; i < numDataCodewords; i++) {
            resultBytes[resultOffset++] = codewordBytes[i];
          }
        }
        return new DecodedData(resultBytes, errorsCorrected);
      }

      private static class DecodedData {
        final byte[] data;
        final int errorsCorrected;
        DecodedData(byte[] data, int errorsCorrected) {
          this.data = data;
          this.errorsCorrected = errorsCorrected;
        }
      }
}
