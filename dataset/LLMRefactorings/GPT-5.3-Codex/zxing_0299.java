public class zxing_0299 {

      public DecoderResult decode(BitMatrix bits,
                                  Map<DecodeHintType,?> hints) throws FormatException, ChecksumException {
        BitMatrixParser parser = new BitMatrixParser(bits);
        byte[] codewords = parser.readCodewords();
    
        int errorsCorrected = correctErrors(codewords, 0, 10, 10, ALL);
        int mode = codewords[0] & 0x0F;
        byte[] datawords;
        switch (mode) {
          case 2:
          case 3:
          case 4:
            errorsCorrected += correctErrors(codewords, 20, 84, 40, EVEN);
            errorsCorrected += correctErrors(codewords, 20, 84, 40, ODD);
            datawords = new byte[94];
            break;
          case 5:
            errorsCorrected += correctErrors(codewords, 20, 68, 56, EVEN);
            errorsCorrected += correctErrors(codewords, 20, 68, 56, ODD);
            datawords = new byte[78];
            break;
          default:
            throw FormatException.getFormatInstance();
        }
    
        System.arraycopy(codewords, 0, datawords, 0, 10);
        System.arraycopy(codewords, 20, datawords, 10, datawords.length - 10);
    
        DecoderResult result = DecodedBitStreamParser.decode(datawords, mode);
        result.setErrorsCorrected(errorsCorrected);
        return result;
      }
}
